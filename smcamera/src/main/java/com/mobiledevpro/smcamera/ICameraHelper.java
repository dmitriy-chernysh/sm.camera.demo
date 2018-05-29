package com.mobiledevpro.smcamera;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.TextureView;

import java.io.File;

/**
 * Interface for camera helper
 * <p>
 * Created by Dmitriy V. Chernysh on 02.05.18.
 * <p>
 * https://instagr.am/mobiledevpro
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */
public interface ICameraHelper {

    interface IVideoCaptureCallbacks {
        void onVideoCaptureFinished(File outputVideoFile);
    }

    interface IPhotoCaptureCallbacks {
        void onPhotoCaptureFinished(File outputPhotoFile);
    }

    /**
     * Check if it's a Samsung device
     */
    boolean isThisSamsungDevice();

    boolean isFlashlightSupported();

    void startCamera(Context context,
                     TextureView textureView,
                     int textureWidth,
                     int textureHeight,
                     @NonNull CameraSettings cameraSettings);

    void stopCamera(Context context);

    /**
     * Start or stop video recording
     */
    void startStopVideoRecording();

    /**
     * Take photo picture
     */
    void takePicture();

    void restartCamera(Context context,
                       TextureView textureView,
                       int textureWidth,
                       int textureHeight,
                       @NonNull CameraSettings cameraSettings);

    void setFlashlightOn(boolean isOn);
}
