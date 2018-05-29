package com.mobiledevpro.camera.helper;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Storage helper
 * <p>
 * Created by Dmitriy V. Chernysh on 30.04.18.
 * <p>
 * https://instagr.am/mobiledevpro
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */
public class StorageHelper {

    private File mAppFolder;
    private static StorageHelper sStorageHelper;

    private StorageHelper(Context appContext) {
        mAppFolder = ContextCompat.getExternalFilesDirs(appContext, null)[0];
    }

    public static StorageHelper get(Context appContext) {
        if (sStorageHelper == null) {
            sStorageHelper = new StorageHelper(appContext);
        }
        return sStorageHelper;
    }

    /**
     * Get path for next Video file
     *
     * @return String File Name
     */
    public File createNewVideoFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault()).format(new Date());
        String fileName = mAppFolder + File.separator + File.separator + "temp_video_" + timeStamp + ".mp4";
        File file = new File(fileName);
        if (!file.exists()) file.mkdirs();
        return file;
    }

    /**
     * Get path for video files
     *
     * @return File path
     */
    public File getVideoFilesDir() {
        if (!mAppFolder.exists()) mAppFolder.mkdirs();
        return mAppFolder;
    }


    /**
     * Get path for photo files
     *
     * @return File path
     */
    public File getPhotoFilesDir() {
        if (!mAppFolder.exists()) mAppFolder.mkdirs();
        return mAppFolder;
    }
}
