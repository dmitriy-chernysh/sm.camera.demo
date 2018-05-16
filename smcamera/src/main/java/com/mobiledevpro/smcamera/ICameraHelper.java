package com.mobiledevpro.smcamera;

import android.content.Context;
import android.view.TextureView;

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

    /**
     * Check if it's a Samsung device
     */
    boolean isThisSamsungDevice();

    void startCamera(Context context, boolean useBackCamera, TextureView textureView);

    void stopCamera(Context context);
}
