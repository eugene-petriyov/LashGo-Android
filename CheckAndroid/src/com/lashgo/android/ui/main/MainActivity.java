package com.lashgo.android.ui.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.lashgo.android.LashgoConfig;
import com.lashgo.android.R;
import com.lashgo.android.service.handlers.BaseIntentHandler;
import com.lashgo.android.service.handlers.GetMainScreenHandler;
import com.lashgo.android.service.handlers.RestHandlerFactory;
import com.lashgo.android.ui.BaseActivity;
import com.lashgo.android.ui.auth.AuthController;
import com.lashgo.android.ui.check.CheckListFragment;
import com.lashgo.android.ui.news.NewsFragment;
import com.lashgo.android.ui.subscribes.SubscribesFragment;
import com.lashgo.android.utils.ContextUtils;
import com.lashgo.android.utils.PhotoUtils;
import com.lashgo.model.dto.GcmRegistrationDto;
import com.lashgo.model.dto.MainScreenInfoDto;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Eugene on 17.06.2014.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String KEY_CHECK_DTO = "check_dto";
    private String[] menuItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    @Inject
    AuthController authController;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private GoogleCloudMessaging gcm;
    private ImageView userAvatar;
    private TextView userName;
    private View itemTasks;
    private TextView tasksCount;
    private TextView newsCount;
    private TextView subscribesCount;
    private View drawerMenu;

    @Override
    protected void registerActionsListener() {
        addActionListener(RestHandlerFactory.ACTION_LOGIN);
        addActionListener(RestHandlerFactory.ACTION_REGISTER);
        addActionListener(RestHandlerFactory.ACTION_PASSWORD_RECOVER);
        addActionListener(RestHandlerFactory.ACTION_SOCIAL_SIGN_IN);
        addActionListener(RestHandlerFactory.ACTION_CONFIRM_SOCIAL_SIGN_UP);
        addActionListener(RestHandlerFactory.ACTION_GCM_REGISTER_ID);
        addActionListener(RestHandlerFactory.ACTION_GET_MAIN_SCREEN_INFO);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getData() != null) {
            twitterHelper.handleCallbackUrl(intent.getData());
        }
    }

    @Override
    public void processServerResult(String action, int resultCode, Bundle data) {
        authController.handleServerResponse(action, resultCode, data);
        if (RestHandlerFactory.ACTION_GET_MAIN_SCREEN_INFO.equals(action)) {
            if (resultCode == BaseIntentHandler.SUCCESS_RESPONSE) {
                MainScreenInfoDto mainScreenInfoDto = (MainScreenInfoDto) data.getSerializable(GetMainScreenHandler.MAIN_SCREEN_INFO);
                updateMainScreenInfo(mainScreenInfoDto);
            }
        }
    }

    private void updateMainScreenInfo(MainScreenInfoDto mainScreenInfoDto) {
        userName.setText(mainScreenInfoDto.getUserName());
        Picasso.with(this).load(PhotoUtils.getFullPhotoUrl(mainScreenInfoDto.getUserAvatar())).into(userAvatar);
        tasksCount.setText(mainScreenInfoDto.getTasksCount());
        newsCount.setText(mainScreenInfoDto.getNewsCount());
        subscribesCount.setText(mainScreenInfoDto.getSubscribesCount());
    }

    @Override
    protected void unregisterActionsListener() {
        removeActionListener(RestHandlerFactory.ACTION_LOGIN);
        removeActionListener(RestHandlerFactory.ACTION_REGISTER);
        removeActionListener(RestHandlerFactory.ACTION_PASSWORD_RECOVER);
        removeActionListener(RestHandlerFactory.ACTION_SOCIAL_SIGN_IN);
        removeActionListener(RestHandlerFactory.ACTION_CONFIRM_SOCIAL_SIGN_UP);
        removeActionListener(RestHandlerFactory.ACTION_GCM_REGISTER_ID);
        removeActionListener(RestHandlerFactory.ACTION_GET_MAIN_SCREEN_INFO);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        if (!settingsHelper.isFirstLaunch()) {
            settingsHelper.setFirstLaunch();
        }
        mTitle = mDrawerTitle = getTitle();
        menuItems = getResources().getStringArray(R.array.menus_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (settingsHelper.isLoggedIn()) {
//            initAuthDrawerMenu();
        } else {
            initNotAuthDrawerMenu();
        }

        // Set the adapter for the list view
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            String regid = settingsHelper.getRegistrationId();
            if (TextUtils.isEmpty(regid)) {
                registerInBackground();
            } else {
                sendRegistrationIdToBackend(regid);
            }
        } else {
            ContextUtils.showToast(this, "No valid Google Play Services APK found.");
        }
    }

    private void initNotAuthDrawerMenu() {
        ViewStub drawerMenuStub = (ViewStub) findViewById(R.id.not_auth_drawer_menu);
        drawerMenu = drawerMenuStub.inflate();
        authController.initViews(drawerMenu);
        showFragment(CheckListFragment.newInstance());
    }

//    private void initAuthDrawerMenu() {
//        ViewStub drawerMenuStub = (ViewStub) findViewById(R.id.auth_drawer_menu);
//        drawerMenu = drawerMenuStub.inflate();
//        userAvatar = (ImageView) drawerMenu.findViewById(R.id.img_user_avatar);
//        userName = (TextView) drawerMenu.findViewById(R.id.text_user_name);
//        itemTasks = drawerMenu.findViewById(R.id.item_tasks);
//        itemTasks.setOnClickListener(this);
//        tasksCount = (TextView) drawerMenu.findViewById(R.id.tasks_count);
//        drawerMenu.findViewById(R.id.item_news).setOnClickListener(this);
//        newsCount = (TextView) drawerMenu.findViewById(R.id.news_count);
//        drawerMenu.findViewById(R.id.item_subscribes).setOnClickListener(this);
//        subscribesCount = (TextView) drawerMenu.findViewById(R.id.subscribes_count);
//        serviceHelper.getMainScreenInfo(settingsHelper.getLastNewsView(), settingsHelper.getLastSubscriptionsView());
//        selectItem(itemTasks);
//
//    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                ContextUtils.showToast(this, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String regId = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
                    }
                    regId = gcm.register(LashgoConfig.GCM_API_KEY);
                    // Persist the regID - no need to register again.
                    settingsHelper.saveRegistrationId(regId);
                } catch (IOException ex) {
                    ContextUtils.showToast(MainActivity.this, ex.getMessage());
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return regId;
            }

            @Override
            protected void onPostExecute(String regId) {
                sendRegistrationIdToBackend(regId);
            }
        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(final String registrationId) {
        serviceHelper.gcmRegisterId(new GcmRegistrationDto(registrationId));
    }

    @Override
    public void onClick(View v) {
        selectItem(v);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(View view) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        int position = 0;
        if (view.getId() == R.id.item_tasks) {
            fragment = CheckListFragment.newInstance();
            position = 0;
        } else if (view.getId() == R.id.item_news) {
            fragment = NewsFragment.newInstance();
            position = 1;
        } else if (view.getId() == R.id.item_subscribes) {
            fragment = SubscribesFragment.newInstance();
            position = 2;
        }
        showFragment(fragment);

        // Highlight the selected item, update the title, and close the drawer
        view.setBackgroundColor(getResources().getColor(R.color.selected_item_color));
        setTitle(menuItems[position]);
        mDrawerLayout.closeDrawer(drawerMenu);
    }

    private void showFragment(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
}
