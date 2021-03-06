package com.lashgo.mobile.service.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lashgo.model.dto.RecoverInfo;

import java.io.IOException;

import retrofit.RetrofitError;

/**
 * Created by Eugene on 12.09.2014.
 */
public class PasswordRecoverHandler extends BaseIntentHandler {
    public PasswordRecoverHandler(Context context) {
        super(context);
    }

    @Override
    protected Bundle doExecute(Intent intent) throws IOException, RetrofitError {
        service.passwordRecover(new RecoverInfo(intent.getStringExtra(ServiceExtraNames.EMAIL.name())));
        return intent.getExtras();
    }
}
