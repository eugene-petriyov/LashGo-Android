package com.lashgo.android.service.handlers;

import android.content.Intent;
import android.os.Bundle;
import com.lashgo.model.dto.LoginInfo;
import com.lashgo.model.dto.ResponseObject;
import com.lashgo.model.dto.SessionInfo;
import retrofit.RetrofitError;

import java.io.IOException;

/**
 * User: eugene.petriyov
 * Date: 25.06.13
 * Time: 13:42
 */
public class LoginHandler extends BaseIntentHandler {

    public LoginHandler() {
        super();
    }

    @Override
    public Bundle doExecute(Intent intent) throws RetrofitError, IOException {
        LoginInfo loginInfo = (LoginInfo) intent.getSerializableExtra(ServiceExtraNames.LOGIN_DTO.name());
        ResponseObject<SessionInfo> sessionInfo = service.login(loginInfo);
        settingsHelper.login(sessionInfo.getResult(), loginInfo);
        Bundle bundle = intent.getExtras();
        bundle.putSerializable(ServiceExtraNames.SESSION_INFO.name(), sessionInfo);
        return bundle;
    }
}
