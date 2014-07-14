package com.lashgo.android;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lashgo.android.service.*;
import com.lashgo.android.service.handlers.*;
import com.lashgo.android.settings.SettingsHelper;
import com.lashgo.android.ui.auth.LoginActivity;
import com.lashgo.android.ui.main.MainActivity;
import com.lashgo.android.ui.start.StartActivity;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

import javax.inject.Singleton;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene
 * Date: 13.03.14
 * Time: 20:08
 * To change this template use File | Settings | File Templates.
 */
@Module(
        injects = {
                LoginHandler.class,
                RegisterHandler.class,
                GcmRegisterHandler.class,
                SocialSignInHandler.class,
                SocialSignUpHandler.class,
                ServiceHelper.class,
                CheckInterceptor.class
        },
        library = true
)
public class LashgoModule {

    private final LashgoApplication application;

    public LashgoModule(LashgoApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    SettingsHelper provideSettingsHelper() {
        return new SettingsHelper(application);
    }

    @Provides
    @Singleton
    ServiceHelper provideServiceHelper(SettingsHelper settingsHelper) {
        return new ServiceHelper(application, settingsHelper);
    }

    @Provides
    @Singleton
    RestService provideRestService(SettingsHelper settingsHelper) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setDateFormat(new SimpleDateFormat(LashgoConfig.DATE_FORMAT));
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(LashgoConfig.BASE_URL)
                .setConverter(new JacksonConverter(objectMapper))
                .setErrorHandler(new CheckErrorHandler())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new CheckInterceptor(settingsHelper))
                .build();
        return restAdapter.create(RestService.class);
    }


    @Provides
    Handler provideHandler() {
        return new Handler(Looper.getMainLooper());
    }
}
