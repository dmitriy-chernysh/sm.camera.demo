package com.mobiledevpro.smcamera;

import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.mobiledevpro.commons.helpers.BasePermissionsHelper;

import java.io.IOException;

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

public class SMCameraPresenter implements ISMCamera.Presenter {
    private static final int CODE_REQUEST_PERMISSION_CAMERA = 10001;

    private ISMCamera.View mView;

    private Camera mCamera;
    private SurfaceTexture mSurfaceTexture;
    private AutoFitTextureView mCameraPreview;

    private boolean mIsVideoRecording;
    private boolean mIsAspectRationFull = true;

    @Override
    public void bindView(ISMCamera.View view) {
        mView = view;
        //check permissions to use camera and microphone
        checkRuntimePermissions();

        mCameraPreview = mView.getCameraPreview();
        //ratio by default
        mCameraPreview.setAspectRatio(16, 9);
        mView.setFullAspectRatio(mIsAspectRationFull);

    }

    @Override
    public void unbindView() {
        mView = null;
        stopCameraPreview();
    }


    @Override
    public void onCameraViewAvailable(SurfaceTexture surfaceTexture) {
        mSurfaceTexture = surfaceTexture;
        startCameraPreview();
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
    }

    @Override
    public void onAspectRatioButtonClick() {
        mIsAspectRationFull = !mIsAspectRationFull;
        mCameraPreview.setAspectRatio(mIsAspectRationFull ? 16 : 4, mIsAspectRationFull ? 9 : 3);
        mView.setFullAspectRatio(mIsAspectRationFull);
    }

    private void startCameraPreview() {
        try {
            mCamera = Camera.open();
            mCamera.setPreviewTexture(mSurfaceTexture);
            mCamera.startPreview();
        } catch (IOException | RuntimeException e) {
            Log.e(Constants.LOG_TAG_ERROR, "MainPresenter.onCameraViewAvailable: ", e);
        }
    }

    private void stopCameraPreview() {
        if (mCamera == null) return;
        mCamera.stopPreview();
        mCamera.release();
    }

    private void checkRuntimePermissions() {
        if (BasePermissionsHelper.isCaptureVideoPermissionsGranted(mView.getActivity())) return;
        BasePermissionsHelper.requestCaptureVideoPermissions(
                (Fragment) mView,
                CODE_REQUEST_PERMISSION_CAMERA
        );
    }
}
