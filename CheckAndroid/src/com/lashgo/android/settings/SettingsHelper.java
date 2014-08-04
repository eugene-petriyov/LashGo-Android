package com.lashgo.android.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lashgo.android.LashgoConfig;
import com.lashgo.model.dto.LoginInfo;
import com.lashgo.model.dto.SessionInfo;
import com.lashgo.model.dto.SocialInfo;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: eugene.petriyov
 * Date: 27.06.13
 * Time: 13:35
 */
public class SettingsHelper {

    private static final String KEY_SESSION = "session_id";
    private static final String KEY_LOGIN_INFO = "login_info";
    private static final String GCM_REGISTRATION_ID = "gcm_registration_id";
    private static final String KEY_IS_FIRST_LAUNCH = "is_first_launch";
    private static final String KEY_SOCIAL_INFO = "social_info";
    private static final String KEY_LAST_SUBSCRIPTIONS_VIEW = "last_subscription_date";
    private static final String KEY_LAST_NEWS_VIEW = "last_news_view";
    private SharedPreferences preferences;

    public SettingsHelper(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private float getFloat(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    private void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        synchronized (editor) {
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    private void setFloat(String key, float value) {
        SharedPreferences.Editor editor = preferences.edit();
        synchronized (editor) {
            editor.putFloat(key, value);
            editor.commit();
        }
    }

    private void setString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        synchronized (editor) {
            editor.putString(key, value);
            editor.commit();
        }
    }

    private void setInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        synchronized (editor) {
            editor.putInt(key, value);
            editor.commit();
        }
    }

    private void setLong(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        synchronized (editor) {
            editor.putLong(key, value);
            editor.commit();
        }
    }

    private long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    private int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    private String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public void login(SessionInfo sessionInfo, LoginInfo loginInfo) {
        setString(KEY_SESSION, sessionInfo.getSessionId());
        saveSerializable(KEY_LOGIN_INFO, loginInfo);
    }

    public void logout() {
        remove(KEY_SESSION);
        remove(KEY_LOGIN_INFO);
        remove(KEY_SOCIAL_INFO);
    }

    private void remove(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        synchronized (editor) {
            editor.remove(key);
            editor.commit();
        }
    }

    public void saveRegistrationId(String registrationId) {
        setString(GCM_REGISTRATION_ID, registrationId);
    }

    public String getRegistrationId() {
        return getString(GCM_REGISTRATION_ID, "");
    }

    public boolean isLoggedIn() {
        String session = getString(KEY_SESSION, "");
        return !TextUtils.isEmpty(session);
    }

    public void setFirstLaunch() {
        setBoolean(KEY_IS_FIRST_LAUNCH, true);
    }

    public boolean isFirstLaunch() {
        return getBoolean(KEY_IS_FIRST_LAUNCH, true);
    }

    public SocialInfo getSocialInfo() {
        return (SocialInfo) getSerializable(KEY_SOCIAL_INFO, SocialInfo.class);
    }

    public LoginInfo getLoginInfo() {
        return (LoginInfo) getSerializable(KEY_LOGIN_INFO, LoginInfo.class);
    }

    private Serializable getSerializable(String key, Class<? extends Serializable> clazz) {
        String infoString = getString(key, null);
        if (!TextUtils.isEmpty(infoString)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(infoString, clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void saveSerializable(String key, Serializable info) {
        ObjectMapper objectMapper = new ObjectMapper();
        String infoString = null;
        try {
            infoString = objectMapper.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        setString(key, infoString);
    }

    public void socialLogin(SessionInfo sessionInfo, SocialInfo socialInfo) {
        if (sessionInfo != null) {
            setString(KEY_SESSION, sessionInfo.getSessionId());
        }
        saveSerializable(KEY_SOCIAL_INFO, socialInfo);
    }

    public String getSessionId() {
        return getString(KEY_SESSION, null);
    }

    public String getLastNewsView() {
        return getString(KEY_LAST_NEWS_VIEW, new SimpleDateFormat(LashgoConfig.DATE_FORMAT).format(new Date()));
    }

    public String getLastSubscriptionsView() {
        return getString(KEY_LAST_SUBSCRIPTIONS_VIEW, new SimpleDateFormat(LashgoConfig.DATE_FORMAT).format(new Date()));
    }
}
