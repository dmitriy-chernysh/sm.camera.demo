package com.mobiledevpro.camera.ui.mainscreen.view;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mobiledevpro.camera.App;
import com.mobiledevpro.camera.R;
import com.mobiledevpro.camera.helper.Constants;
import com.mobiledevpro.camera.helper.StorageHelper;
import com.mobiledevpro.commons.fragment.BaseFragment;
import com.mobiledevpro.smcamera.AutoFitTextureView;
import com.mobiledevpro.smcamera.CameraSettings;
import com.mobiledevpro.smcamera.ISMCamera;
import com.mobiledevpro.smcamera.SMCameraPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Main fragment for main activity
 * <p>
 * Created by Dmitriy V. Chernysh on 20.10.17.
 * dmitriy.chernysh@gmail.com
 * <p>
 * https://fb.me/mobiledevpro/
 * <p>
 * #MobileDevPro
 */

public class SMCameraFragment extends BaseFragment implements ISMCamera.View, TextureView.SurfaceTextureListener {

    @BindView(R.id.camera_view)
    AutoFitTextureView mCameraPreview;

    @BindView(R.id.btn_record_start_stop)
    ImageButton mBtnVideoRecord;

    @BindView(R.id.btn_take_picture)
    ImageButton mBtnTakePicture;

    @BindView(R.id.btn_aspect)
    ImageButton mBtnAspectRation;

    @BindView(R.id.btn_flashlight)
    ImageButton mBtnFlashlight;

    @BindView(R.id.tv_camera_loading)
    TextView mTvCameraLoading;

    @BindView(R.id.layout_capture_buttons)
    View mLayoutCaptureButtons;

    @BindView(R.id.metering_view)
    View mMeteringView;

    private ISMCamera.Presenter mPresenter;
    private Unbinder mButterKnife;

    public static SMCameraFragment newInstance() {

        Bundle args = new Bundle();

        SMCameraFragment fragment = new SMCameraFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        if (activity != null) {
            //keep screen on
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    protected View populateView(View layoutView, @Nullable Bundle savedInstanceState) {
        mButterKnife = ButterKnife.bind(this, layoutView);
        mCameraPreview.setSurfaceTextureListener(this);
        return layoutView;
    }

    @Override
    protected void initPresenters() {
        mPresenter = new SMCameraPresenter();
    }


    @Override
    public void onStart() {
        super.onStart();
        mPresenter.bindView(
                this,
                StorageHelper.get(App.getAppContext()).getVideoFilesDir(),
                StorageHelper.get(App.getAppContext()).getPhotoFilesDir(),
                new CameraSettings()
                        .setVideoStabilisationEnabled(true)
                        .setAudioEnabled(true)
                        .setManualPhotoExposureEnabled(true)
                        .setRotation(getActivity().getWindow().getWindowManager().getDefaultDisplay().getRotation())
        );
        if (mCameraPreview.isAvailable()) {
            onSurfaceTextureAvailable(mCameraPreview.getSurfaceTexture(),
                    mCameraPreview.getWidth(),
                    mCameraPreview.getHeight());
        }
    }

    @Override
    public void onStop() {
        mPresenter.unbindView();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mButterKnife != null) mButterKnife.unbind();
        mPresenter.unbindView();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        //Log.d(Constants.LOG_TAG_DEBUG, "SMCameraFragment.onSurfaceTextureUpdated(): ");
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.d(Constants.LOG_TAG_DEBUG, "SMCameraFragment.onSurfaceTextureSizeChanged(): ");
        mPresenter.onCameraViewSizeChanged(surfaceTexture, i, i1);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        Log.d(Constants.LOG_TAG_DEBUG, "SMCameraFragment.onSurfaceTextureDestroyed(): ");
        return true;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.d(Constants.LOG_TAG_DEBUG, "SMCameraFragment.onSurfaceTextureAvailable(): ");
        mPresenter.onCameraViewAvailable(surfaceTexture, i, i1);
    }

    @Override
    public AutoFitTextureView getCameraPreview() {
        return mCameraPreview;
    }

    @Override
    public View getMeteringView() {
        return mMeteringView;
    }

    @Override
    public void setRecordingState(boolean isRecording) {
        mBtnVideoRecord.setActivated(isRecording);
    }

    @Override
    public void setFullAspectRatio(boolean b) {
        mBtnAspectRation.setActivated(b);
    }

    @Override
    public void setIsCameraLoading(boolean isLoading) {
        Log.d(Constants.LOG_TAG_DEBUG, "SMCameraFragment.setIsCameraLoading(): isLoading - " + isLoading);
        mTvCameraLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        mLayoutCaptureButtons.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
        //  mCameraPreview.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void setIsFlashlightAvailable(boolean isAvailable) {
        mBtnFlashlight.setVisibility(isAvailable ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setFlashLightOn(boolean b) {
        mBtnFlashlight.setActivated(b);
    }

    @OnClick(R.id.btn_record_start_stop)
    void onVideoRecordButtonClick(ImageButton button) {
        mPresenter.onVideoRecordButtonClick();
    }

    @OnClick(R.id.btn_take_picture)
    void onPhotoCaptureButtonClick(ImageButton button) {
        mPresenter.onPhotoCaptureButtonClick();
    }

    @OnClick(R.id.btn_aspect)
    void onChangeAspectRatio(ImageButton button) {
        mPresenter.onAspectRatioButtonClick();
    }

    @OnClick(R.id.btn_flashlight)
    void onFlashlightClick(ImageButton button) {
        mPresenter.onFlashlightClick();
    }
}
