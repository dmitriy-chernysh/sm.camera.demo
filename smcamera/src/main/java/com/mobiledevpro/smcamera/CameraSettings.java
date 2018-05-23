package com.mobiledevpro.smcamera;

/**
 * Model to set Camera characteristics
 * <p>
 * Created by Dmitriy V. Chernysh on 22.05.18.
 * <p>
 * https://instagr.am/mobiledevpro
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */
public class CameraSettings {
    private boolean useBackCamera = true;
    private boolean isVideoStabilisationEnabled;
    private boolean isAudioEnabled;
    private boolean isManualPhotoExposureEnabled;
    private double aspectRatio = (double) 16 / 9;
    private int rotation;

    public CameraSettings() {
    }

    public boolean isUseBackCamera() {
        return useBackCamera;
    }

    public CameraSettings setUseBackCamera(boolean useBackCamera) {
        this.useBackCamera = useBackCamera;
        return this;
    }

    public boolean isVideoStabilisationEnabled() {
        return isVideoStabilisationEnabled;
    }

    public CameraSettings setVideoStabilisationEnabled(boolean videoStabilisationEnabled) {
        isVideoStabilisationEnabled = videoStabilisationEnabled;
        return this;
    }

    public boolean isAudioEnabled() {
        return isAudioEnabled;
    }

    public CameraSettings setAudioEnabled(boolean audioEnabled) {
        isAudioEnabled = audioEnabled;
        return this;
    }

    public boolean isManualPhotoExposureEnabled() {
        return isManualPhotoExposureEnabled;
    }

    public CameraSettings setManualPhotoExposureEnabled(boolean manualPhotoExposureEnabled) {
        isManualPhotoExposureEnabled = manualPhotoExposureEnabled;
        return this;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public CameraSettings setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
        return this;
    }

    public int getRotation() {
        return rotation;
    }

    public CameraSettings setRotation(int rotation) {
        this.rotation = rotation;
        return this;
    }
}
