package com.check.android;


import android.os.Build;
import android.os.StrictMode;
import com.check.android.service.CheckErrorHandler;
import com.check.android.service.CheckInterceptor;
import com.check.android.service.JacksonConverter;
import com.check.android.service.RestService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.holoeverywhere.app.Application;
import retrofit.RestAdapter;

/**
 * Created by Eugene on 18.02.14.
 */
public class CheckApplication extends Application {

    private RestService service;

    private static CheckApplication instance;

    public RestService getService() {
        return service;
    }

    public static CheckApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= 9) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(CheckConfig.BASE_URL)
                .setConverter(new JacksonConverter(objectMapper))
                .setErrorHandler(new CheckErrorHandler())
                .setRequestInterceptor(new CheckInterceptor())
                .build();
        service = restAdapter.create(RestService.class);
        instance = this;
    }
}
