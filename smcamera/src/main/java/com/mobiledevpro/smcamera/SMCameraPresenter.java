package com.mobiledevpro.smcamera;

import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
        ICameraHelper.IPhotoCaptureCallbacks {
    private static final int CODE_REQUEST_PERMISSION_CAMERA = 10001;

    private ISMCamera.View mView;

    private AutoFitTextureView mCameraPreview;
    private int mTextureWidth, mTextureHeight;

    private boolean mIsVideoRecording;
    private boolean mIsAspectRationFull = true;
    private CameraHelper mCameraHelper;


    @Override
    public void bindView(ISMCamera.View view,
                         @NonNull File videoFilesDir,
                         @NonNull File photoFilesDir,
                         @NonNull CameraSettings cameraSettings) {
        mView = view;

        mCameraPreview = mView.getCameraPreview();
        //ratio by default
        mCameraPreview.setAspectRatio(16, 9);
        mView.setFullAspectRatio(mIsAspectRationFull);

        mCameraHelper = CameraHelper.init(
                mView.getActivity(),
                videoFilesDir,
                photoFilesDir,
                cameraSettings,
                this,
                this
        );

        //check if it's a Samsung device
        if (!mCameraHelper.isThisSamsungDevice()) {
            new AlertDialog.Builder(mView.getActivity(), R.style.CommonAppTheme_AlertDialog)
                    .setTitle(R.string.dialog_title_error)
                    .setMessage("This is not a Samsung camera")
                    .setPositiveButton(R.string.button_ok, (dialogInterface, i) -> {
                        // mView.getActivity().finish();
                    })
                    .create()
                    .show();

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
    }

    @Override
    public void onVideoCaptureFinished(File outputVideoFile) {
        if (outputVideoFile == null || mView == null) return;
        Toast.makeText(mView.getActivity(), outputVideoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhotoCaptureFinished(File outputPhotoFile) {
        if (outputPhotoFile == null || mView == null) return;
        Toast.makeText(mView.getActivity(), outputPhotoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    private void startCameraPreview() {
        if (mView == null) return;
        if (mCameraHelper != null) {
            mCameraHelper.startCamera(
                    mView.getActivity(),
                    true,
                    mCameraPreview,
                    mTextureWidth,
                    mTextureHeight,
                    mView.getActivity().getWindow().getWindowManager().getDefaultDisplay().getRotation()
            );
        }
    }

    private void stopCameraPreview() {
        if (mView == null) return;
        if (mCameraHelper != null)
            mCameraHelper.stopCamera(mView.getActivity());
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
