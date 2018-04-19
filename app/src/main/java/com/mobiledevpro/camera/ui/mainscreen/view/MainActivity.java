package com.mobiledevpro.camera.ui.mainscreen.view;

import android.view.View;

import com.mobiledevpro.camera.R;
import com.mobiledevpro.camera.helper.FragmentsHelper;
import com.mobiledevpro.commons.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToolbar() {
        //no need
    }

    @Override
    protected void initPresenters() {
        //no need
    }

    @Override
    protected void populateView(View layoutView) {
        //work with view
        FragmentsHelper.showMainFragment(mFragmentManager, R.id.fragment_container);
    }
}
