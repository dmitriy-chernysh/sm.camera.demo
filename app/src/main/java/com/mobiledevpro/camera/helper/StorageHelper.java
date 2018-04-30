package com.mobiledevpro.camera.helper;

import android.content.Context;
import android.os.Environment;

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

    private static final String VIDEO_FOLDER_NAME = "SM Camera Demo";

    private File mAppFolder;
    private static StorageHelper sStorageHelper;

    private StorageHelper() {
        mAppFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    public static StorageHelper get(Context appContext) {
        if (sStorageHelper == null) {
            sStorageHelper = new StorageHelper();
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
        String fileName = mAppFolder + File.separator + VIDEO_FOLDER_NAME + File.separator + "sm_video_" + timeStamp + ".mp4";
        File file = new File(fileName);
        if (!file.exists()) file.mkdirs();
        return file;
    }
}
