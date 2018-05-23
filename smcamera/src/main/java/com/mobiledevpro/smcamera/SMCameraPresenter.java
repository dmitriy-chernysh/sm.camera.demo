package com.mobiledevpro.smcamera;

import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mobiledevpro.commons.helpers.BasePermissionsHelper;

import java.io.File;

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

public class SMCameraPresenter implements ISMCamera.Presenter,
        ICameraHelper.IVideoCaptureCallbacks,
        ICameraHelper.IPhotoCaptureCallbacks,
        ICameraHelper.IOpenCameraCallbacks {
    private static final int CODE_REQUEST_PERMISSION_CAMERA = 10001;

    private ISMCamera.View mView;

    private AutoFitTextureView mCameraPreview;
    private int mTextureWidth, mTextureHeight;

    private boolean mIsVideoRecording;
    private boolean mIsAspectRationFull;
    private CameraHelper mCameraHelper;
    private CameraSettings mCameraSettings;

    @Override
    public void bindView(ISMCamera.View view,
                         @NonNull File videoFilesDir,
                         @NonNull File photoFilesDir,
                         @NonNull CameraSettings cameraSettings) {
        mView = view;
        mCameraSettings = cameraSettings;
        mCameraPreview = mView.getCameraPreview();
        //ratio by default

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
                mView.getActivity(),
                videoFilesDir,
                photoFilesDir,
                this,
                this,
                this
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
        mView = null;
        stopCameraPreview();
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
        // stopCameraPreview();
        // startCameraPreview();
        if (mIsAspectRationFull) {
            mCameraSettings.setAspectRatio((double) 16 / 9);
        } else {
            mCameraSettings.setAspectRatio((double) 4 / 3);
        }
    }

    @Override
    public void onVideoCaptureFinished(File outputVideoFile) {
        Log.d(Constants.LOG_TAG_DEBUG, "SMCameraPresenter.onVideoCaptureFinished(): outputVideoFile: " + outputVideoFile.getAbsolutePath());
        if (outputVideoFile == null || mView == null) return;
        mView.getActivity().runOnUiThread(() ->
                Toast.makeText(mView.getActivity(), outputVideoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public void onPhotoCaptureFinished(File outputPhotoFile) {
        Log.d(Constants.LOG_TAG_DEBUG, "SMCameraPresenter.onPhotoCaptureFinished(): outputPhotoFile: " + outputPhotoFile.getAbsolutePath());
        if (outputPhotoFile == null || mView == null) return;
        mView.getActivity().runOnUiThread(() ->
                Toast.makeText(mView.getActivity(), outputPhotoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show()
        );

    }

    @Override
    public void onCameraReady() {
        if (mView == null) return;
        mView.getActivity().runOnUiThread(() ->
                mView.setIsCameraLoading(false)
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
}
