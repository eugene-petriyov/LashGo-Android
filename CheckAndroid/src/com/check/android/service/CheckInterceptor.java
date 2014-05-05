package com.check.android.service;

import com.check.model.CheckApiHeaders;
import com.check.model.ClientTypes;
import retrofit.RequestInterceptor;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene
 * Date: 26.02.14
 * Time: 0:08
 * To change this template use File | Settings | File Templates.
 */
public class CheckInterceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade requestFacade) {
        requestFacade.addHeader(CheckApiHeaders.UUID, UUID.randomUUID().toString());
        requestFacade.addHeader(CheckApiHeaders.CLIENT_TYPE, ClientTypes.ANDROID);
    }

}
