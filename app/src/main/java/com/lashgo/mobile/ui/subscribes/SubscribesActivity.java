package com.lashgo.mobile.ui.subscribes;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.lashgo.mobile.R;
import com.lashgo.mobile.ui.BaseActivity;
import com.lashgo.mobile.ui.BaseFragment;

/**
 * Created by Eugene on 20.10.2014.
 */
public class SubscribesActivity extends BaseActivity {

    private static final String SUBSCRIBES_TAG = "subscribes";
    private SubscribesFragment.ScreenType screenType;
    private int objectId;
    private long photoId;

    public static Intent buildIntent(Context context, int objectId, SubscribesFragment.ScreenType screenType) {
        Intent intent = new Intent(context, SubscribesActivity.class);
        intent.putExtra(SubscribesFragment.SCREEN_TYPE, screenType);
        intent.putExtra(ExtraNames.USER_ID.name(), objectId);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SubscribesFragment.SCREEN_TYPE, screenType);
        outState.putInt(ExtraNames.USER_ID.name(), objectId);
        outState.putLong(ExtraNames.PHOTO_ID.name(),photoId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            screenType = (SubscribesFragment.ScreenType) savedInstanceState.getSerializable(SubscribesFragment.SCREEN_TYPE);
            objectId = savedInstanceState.getInt(ExtraNames.USER_ID.name());
            photoId = savedInstanceState.getLong(ExtraNames.PHOTO_ID.name());
        } else if (intent != null) {
            screenType = (SubscribesFragment.ScreenType) intent.getSerializableExtra(SubscribesFragment.SCREEN_TYPE);
            objectId = intent.getIntExtra(ExtraNames.USER_ID.name(), -1);
            photoId = intent.getLongExtra(ExtraNames.PHOTO_ID.name(), -1);
        }
        initCustomActionBar(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        setContentView(R.layout.act_container);
        if (SubscribesFragment.ScreenType.SUBSCRIBERS.name().equals(screenType.name())) {
            setScreenTitle(R.string.subscribers_list);
        } else if (SubscribesFragment.ScreenType.SUBSCRIPTIONS.name().equals(screenType.name())) {
            setScreenTitle(R.string.subscribes_list);
        } else {
            setScreenTitle(R.string.users);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, SubscribesFragment.newInstance(objectId, photoId,screenType), SUBSCRIBES_TAG).commit();
    }

    @Override
    protected void refresh() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            ((BaseFragment) fragment).refresh();
        }
    }

    @Override
    public void logout() {
        if (settingsHelper.getUserId() == objectId) {
            settingsHelper.logout();
            finish();
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (fragment != null) {
                ((BaseFragment) fragment).refresh();
            }
        }
    }

    public static Intent buildIntent(Context context, long photoId, SubscribesFragment.ScreenType screenType) {
        Intent intent = new Intent(context, SubscribesActivity.class);
        intent.putExtra(SubscribesFragment.SCREEN_TYPE, screenType);
        intent.putExtra(ExtraNames.PHOTO_ID.name(), photoId);
        return intent;
    }
}
