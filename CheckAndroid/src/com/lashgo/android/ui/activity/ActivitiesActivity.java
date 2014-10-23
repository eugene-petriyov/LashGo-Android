package com.lashgo.android.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.lashgo.android.R;
import com.lashgo.android.ui.BaseActivity;
import com.lashgo.android.ui.main.MainActivity;

/**
 * Created by Eugene on 20.10.2014.
 */
public class ActivitiesActivity extends BaseActivity {

    private static final String ACTIVITIES_TAG = "activities";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomActionBar(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        setContentView(R.layout.act_container);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new ActivityFragment(), ACTIVITIES_TAG).commit();
    }

    @Override
    protected void refresh() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(fragment != null)
        {
            ((ActivityFragment)fragment).refresh();
        }
    }

    @Override
    public void logout() {
        finish();
    }

}
