package com.lashgo.mobile.service.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lashgo.model.dto.ResponseObject;
import com.lashgo.model.dto.UserDto;

import java.io.IOException;

import retrofit.RetrofitError;

/**
 * Created by Eugene on 08.09.2014.
 */
public class GetMyUserProfileHandler extends BaseIntentHandler {
    public GetMyUserProfileHandler(Context context) {
        super(context);
    }

    @Override
    protected Bundle doExecute(Intent intent) throws IOException, RetrofitError {
        ResponseObject<UserDto> responseObject = service.getMyUserProfile();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServiceExtraNames.USER_PROFILE.name(), responseObject.getResult());
        return bundle;
    }
}
