package com.lashgo.mobile.service.transport;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene
 * Date: 24.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class CheckErrorHandler implements ErrorHandler {
    @Override
    public Throwable handleError(RetrofitError retrofitError) {
        return retrofitError;
    }
}
