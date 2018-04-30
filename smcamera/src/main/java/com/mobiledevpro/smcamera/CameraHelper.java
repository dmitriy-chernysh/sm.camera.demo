package com.mobiledevpro.smcamera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.camera.SCamera;

/**
 * Camera helper
 * <p>
 * Created by Dmitriy V. Chernysh on 30.04.18.
 * <p>
 * https://instagr.am/mobiledevpro
 * https://github.com/dmitriy-chernysh
 * #MobileDevPro
 */
public class CameraHelper {

    private static CameraHelper sHelper;
    private SCamera mSCamera;

    private CameraHelper(Context context) {
        initSCamera(context);
    }

    public static CameraHelper get(Context context) {
        if (sHelper == null) sHelper = new CameraHelper(context);
        return sHelper;
    }

    public boolean isThisSamsungDevice() {
        if (mSCamera.isFeatureEnabled(SCamera.SCAMERA_PROCESSOR)) {
            return true;
        }

        return false;
    }

    private boolean initSCamera(Context context) {
        mSCamera = new SCamera();
        String message = null;
        try {
            mSCamera.initialize(context);
            return true;
        } catch (SsdkUnsupportedException e) {
            int eType = e.getType();
            message = e.getLocalizedMessage();
            if (eType == SsdkUnsupportedException.VENDOR_NOT_SUPPORTED) {
                message = "It's not a Samsung camera.\n" + message;
            } else if (eType == SsdkUnsupportedException.DEVICE_NOT_SUPPORTED) {
                message = "This device is not supported.\n" + message;
            } else {
                message = "Fail to initialize SCamera.\n" + message;
            }
            showAlertDialog(context, message, true);
        }

        return false;
    }

    /**
     * Shows alert dialog.
     */
    private void showAlertDialog(final Context context, String message, final boolean finishActivity) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.CommonAppTheme_AlertDialog);
        dialog.setMessage(message)
                .setTitle(R.string.dialog_title_error)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (context instanceof Activity) {
                            if (finishActivity) ((Activity) context).finish();
                        }
                    }
                }).setCancelable(false);

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }
}
