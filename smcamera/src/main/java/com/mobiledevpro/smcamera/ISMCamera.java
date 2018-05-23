package com.mobiledevpro.smcamera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Interface for main screen
 * <p>
 * Created by Dmitriy V. Chernysh on 20.10.17.
 * dmitriy.chernysh@gmail.com
 * <p>
 * https://fb.me/mobiledevpro/
 * <p>
 * #MobileDevPro
 */

public interface ISMCamera {
    interface View {
        Activity getActivity();

        AutoFitTextureView getCameraPreview();

        void setRecordingState(boolean isRecording);

        void setFullAspectRatio(boolean b);

        void setIsCameraLoading(boolean isLoading);
    }

    interface Presenter {

        void bindView(ISMCamera.View view,
                      @NonNull File videoFilesDir,
                      @NonNull File photoFilesDir,
                      @NonNull CameraSettings cameraSettings);

        void unbindView();

        void onCameraViewAvailable(SurfaceTexture surfaceTexture, int width, int height);

        void onCameraViewSizeChanged(SurfaceTexture surfaceTexture, int width, int height);

        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

        void onVideoRecordButtonClick();

        void onPhotoCaptureButtonClick();

        void onAspectRatioButtonClick();
    }
}
