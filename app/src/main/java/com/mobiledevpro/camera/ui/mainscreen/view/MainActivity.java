package com.mobiledevpro.camera.ui.mainscreen.view;

import android.support.v7.widget.Toolbar;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
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
