package com.lashgo.mobile.ui.auth;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.facebook.CallbackManager;
import com.lashgo.mobile.R;
import com.lashgo.mobile.service.handlers.BaseIntentHandler;
import com.lashgo.mobile.social.TwitterHelper;
import com.lashgo.mobile.social.VkontakteListener;
import com.lashgo.mobile.ui.BaseActivity;
import com.lashgo.mobile.ui.main.MainActivity;
import com.lashgo.model.dto.UserDto;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;

/**
 * Created by Eugene on 18.02.14.
 */
public class LoginActivity extends BaseActivity implements AuthController.AuthListener {
    private TwitterHelper twitterHelper;
    private VkontakteListener vkSdkListener;

    private AuthController authController;

    private OpenMode openMode;

    private CallbackManager callbackManager;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ExtraNames.OPEN_MODE.name(), openMode);
        outState.putSerializable(TwitterHelper.KEY_REQUEST_TOKEN, twitterHelper.getRequestToken());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterHelper = new TwitterHelper(this);
        vkSdkListener = new VkontakteListener(this);
        setContentView(R.layout.act_login);
        callbackManager = CallbackManager.Factory.create();
        VKSdk.initialize(vkSdkListener, getString(R.string.vkontakte_app_id), null);
        twitterHelper.onCreate(savedInstanceState);
        initCustomActionBar(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        initExtras(savedInstanceState);
        authController = new AuthController(this, serviceHelper, callbackManager,twitterHelper, this);
        authController.setOpenMode(openMode);
        authController.initViews(getWindow().getDecorView().getRootView());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    private void initExtras(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            openMode = (OpenMode) savedInstanceState.getSerializable(ExtraNames.OPEN_MODE.name());
        } else {
            Intent intent = getIntent();
            if (intent != null) {
                openMode = (OpenMode) intent.getSerializableExtra(ExtraNames.OPEN_MODE.name());
            }
        }
    }

    @Override
    protected void registerActionsListener() {
        addActionListener(BaseIntentHandler.ServiceActionNames.ACTION_LOGIN.name());
        addActionListener(BaseIntentHandler.ServiceActionNames.ACTION_REGISTER.name());
        addActionListener(BaseIntentHandler.ServiceActionNames.ACTION_SOCIAL_SIGN_IN.name());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this,requestCode, resultCode, data);
        if (requestCode == TwitterHelper.TWITTER_AUTH) {

            if (resultCode == Activity.RESULT_OK) {
                twitterHelper.handleCallbackUrl(data.getData());
            }
        }
    }

    @Override
    public void startProgress() {
        showOverlayProgress();
    }

    @Override
    public void stopProgress() {
        hideOverlayProgress();
    }

    @Override
    public void processServerResult(String action, int resultCode, Bundle data) {
        authController.handleServerResponse(action, resultCode, data);
    }

    @Override
    protected void unregisterActionsListener() {
        removeActionListener(BaseIntentHandler.ServiceActionNames.ACTION_LOGIN.name());
        removeActionListener(BaseIntentHandler.ServiceActionNames.ACTION_REGISTER.name());
        removeActionListener(BaseIntentHandler.ServiceActionNames.ACTION_SOCIAL_SIGN_IN.name());
    }

    public static Intent buildIntent(Context context, OpenMode openMode) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(ExtraNames.OPEN_MODE.name(), openMode);
        return intent;
    }

    @Override
    public void onLoginSuccessFull() {
        setResult(Activity.RESULT_OK);
        finish();
        if (openMode == null || !openMode.equals(LoginActivity.OpenMode.FROM_CHECK)) {
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    @Override
    public void onRegisterSuccessFull(UserDto registerResponse) {
        setResult(Activity.RESULT_OK);
        finish();
        startActivity(SuccessfulRegisterActivity.buildIntent(this, registerResponse, openMode));
    }

    public enum OpenMode {
        FROM_SPLASH, FROM_CHECK
    }


    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void refresh() {

    }

    @Override
    public void logout() {
        //TODO not to implement
    }
}
