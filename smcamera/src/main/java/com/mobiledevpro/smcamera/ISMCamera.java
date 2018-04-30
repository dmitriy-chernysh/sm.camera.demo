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
    }

    interface Presenter {

        void bindView(ISMCamera.View view);

        void unbindView();

        void onCameraViewAvailable(SurfaceTexture surfaceTexture);

        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

        void onVideoRecordButtonClick(File newVideoFile);

        void onAspectRatioButtonClick();
    }
}
