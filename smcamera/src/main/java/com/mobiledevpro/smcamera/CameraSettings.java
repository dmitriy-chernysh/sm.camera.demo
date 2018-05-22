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
    private boolean isVideoStabilisationEnabled;
    private boolean isAudioEnabled;
    private boolean isManualPhotoExposureEnabled;

    public CameraSettings() {
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
}
