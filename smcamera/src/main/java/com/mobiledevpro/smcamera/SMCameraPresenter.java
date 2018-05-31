package com.mobiledevpro.smcamera;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mobiledevpro.commons.helpers.BasePermissionsHelper;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Presenter for main screen
 * <p>
 * Created by Dmitriy V. Chernysh on 20.10.17.
 * dmitriy.chernysh@gmail.com
 * <p>
 * https://fb.me/mobiledevpro/
 * <p>
 * #MobileDevPro
 */

public class SMCameraPresenter implements ISMCamera.Presenter, View.OnTouchListener {
    private static final int CODE_REQUEST_PERMISSION_CAMERA = 10001;

    private ISMCamera.View mView;

    private AutoFitTextureView mCameraPreview;
    private int mTextureWidth, mTextureHeight;

    private boolean mIsVideoRecording;
    private boolean mIsAspectRationFull;
    private boolean mIsFlashlightOn;
    private CameraHelper mCameraHelper;
    private CameraSettings mCameraSettings;
    private CompositeDisposable mSubscriptions = new CompositeDisposable();

    @Override
    public void bindView(ISMCamera.View view,
                         @NonNull File videoFilesDir,
                         @NonNull File photoFilesDir,
                         @NonNull CameraSettings cameraSettings) {
        registerRxEvents();

        Log.d(Constants.LOG_TAG_DEBUG, "SMCameraPresenter.bindView(): ");
        mView = view;
        mCameraSettings = cameraSettings;
        mCameraPreview = mView.getCameraPreview();

        //set touch listener to draw metering area
        if (mCameraSettings.isManualPhotoExposureEnabled()) {
            mView.getMeteringView().setOnTouchListener(this);
        }

        mView.setIsCameraLoading(true);

        if (cameraSettings == null || cameraSettings.getAspectRatio() == (double) 16 / 9) {
            mCameraPreview.setAspectRatio(16, 9);
            mIsAspectRationFull = true;
        } else {
            mCameraPreview.setAspectRatio(4, 3);
            mIsAspectRationFull = false;
        }
        mView.setFullAspectRatio(mIsAspectRationFull);

        mCameraHelper = CameraHelper.init(
                videoFilesDir,
                photoFilesDir
        );

        //set screen brightness to max
        Window window = mView.getActivity().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.screenBrightness = 1; //from dark (0) to full bright (1)
            window.setAttributes(lp);
        }

    }

    @Override
    public void unbindView() {
        Log.d(Constants.LOG_TAG_DEBUG, "SMCameraPresenter.unbindView(): ");
        unregisterRxEvents();
        stopCameraPreview();
        mView = null;
    }


    @Override
    public void onCameraViewAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        // mSurfaceTexture = surfaceTexture;
        mTextureWidth = width;
        mTextureHeight = height;
        //check permissions to use camera and microphone and storage
        if (checkRuntimePermissions()) {
            startCameraPreview();
        }
    }

    @Override
    public void onCameraViewSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
        mTextureWidth = width;
        mTextureHeight = height;
        restartCameraPreview();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraPreview();
                }
                break;
        }
    }

    @Override
    public void onVideoRecordButtonClick() {
        mIsVideoRecording = !mIsVideoRecording;
        mView.setRecordingState(mIsVideoRecording);

        //start/stop recording
        mCameraHelper.startStopVideoRecording();
    }

    @Override
    public void onPhotoCaptureButtonClick() {
        mCameraHelper.takePicture();
    }

    @Override
    public void onAspectRatioButtonClick() {
        mIsAspectRationFull = !mIsAspectRationFull;
        mCameraPreview.setAspectRatio(mIsAspectRationFull ? 16 : 4, mIsAspectRationFull ? 9 : 3);
        mView.setFullAspectRatio(mIsAspectRationFull);

        if (mIsAspectRationFull) {
            mCameraSettings.setAspectRatio((double) 16 / 9);
        } else {
            mCameraSettings.setAspectRatio((double) 4 / 3);
        }
    }

    @Override
    public void onFlashlightClick() {
        mIsFlashlightOn = !mIsFlashlightOn;
        mCameraHelper.setFlashlightOn(mIsFlashlightOn);
        mView.setFlashLightOn(mIsFlashlightOn);
    }

    /**
     * Touch listener for metering area on camera preview
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            drawMeteringRectangle((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
            return true;
        }

        return false;
    }

    private void onCameraReady() {
        if (mView == null) return;
        mView.setIsCameraLoading(false);
        mView.setIsFlashlightAvailable(
                mCameraHelper.isFlashlightSupported()
        );
    }

    public void onVideoCaptureFinished(File outputVideoFile) {
        Log.d(Constants.LOG_TAG_DEBUG, "SMCameraPresenter.onVideoCaptureFinished(): outputVideoFile: " + outputVideoFile.getAbsolutePath());
        if (outputVideoFile == null || mView == null) return;
        Toast.makeText(mView.getActivity(), outputVideoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        mView.getActivity().startActivity(
                new Intent(
                        Intent.ACTION_VIEW,
                        FileProvider.getUriForFile(
                                mView.getActivity(),
                                mView.getActivity().getPackageName() + ".provider",
                                outputVideoFile)
                )
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        );
    }

    public void onPhotoCaptureFinished(File outputPhotoFile) {
        Log.d(Constants.LOG_TAG_DEBUG, "SMCameraPresenter.onPhotoCaptureFinished(): outputPhotoFile: " + outputPhotoFile.getAbsolutePath());
        if (outputPhotoFile == null || mView == null) return;
        Toast.makeText(mView.getActivity(), outputPhotoFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

        mView.getActivity().startActivity(
                new Intent(
                        Intent.ACTION_VIEW,
                        FileProvider.getUriForFile(
                                mView.getActivity(),
                                mView.getActivity().getPackageName() + ".provider",
                                outputPhotoFile)
                )
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        );
    }

    private void startCameraPreview() {
        if (mView == null) return;
        mView.setIsCameraLoading(true);
        if (mCameraHelper != null) {
            mCameraHelper.startCamera(
                    mView.getActivity(),
                    mCameraPreview,
                    mTextureWidth,
                    mTextureHeight,
                    mCameraSettings
            );
        }
    }

    private void stopCameraPreview() {
        if (mView == null) return;
        if (mCameraHelper != null)
            mCameraHelper.stopCamera(mView.getActivity());
    }

    private void restartCameraPreview() {
        if (mView == null) return;
        mView.setIsCameraLoading(true);
        if (mCameraHelper != null) {
            mCameraHelper.restartCamera(
                    mView.getActivity(),
                    mCameraPreview,
                    mTextureWidth,
                    mTextureHeight,
                    mCameraSettings
            );
        }
    }

    private boolean checkRuntimePermissions() {
        if (BasePermissionsHelper.isCaptureVideoPermissionsGranted(mView.getActivity()))
            return true;
        BasePermissionsHelper.requestCaptureVideoPermissions(
                (Fragment) mView,
                CODE_REQUEST_PERMISSION_CAMERA
        );

        return false;
    }

    private void registerRxEvents() {
        if (mSubscriptions == null) mSubscriptions = new CompositeDisposable();
        Disposable disposable = RxEventBus.getInstance().getEvents()
                .doOnSubscribe(mSubscriptions::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    Log.d(Constants.LOG_TAG_DEBUG, "SMCameraPresenter.registerRxEvents(): event " + o.toString());
                    if (o instanceof CameraHelper.RxEventOnCameraReady) {
                        onCameraReady();
                    } else if (o instanceof CameraHelper.RxEventOnPhotoCaptureFinished) {
                        onPhotoCaptureFinished(((CameraHelper.RxEventOnPhotoCaptureFinished) o).getOutputPhotoFile());
                    } else if (o instanceof CameraHelper.RxEventOnVideoCaptureFinished) {
                        onVideoCaptureFinished(((CameraHelper.RxEventOnVideoCaptureFinished) o).getOutputVideoFile());
                    }
                });
        mSubscriptions.add(disposable);
    }

    private void unregisterRxEvents() {
        if (mSubscriptions == null) return;
        mSubscriptions.dispose();
        mSubscriptions = null;
    }

    private void drawMeteringRectangle(int centerX, int centerY) {
        if (mView == null) return;
        ((MeteringAreaView) mView.getMeteringView()).setRectangle(centerX, centerY);
    }
}
