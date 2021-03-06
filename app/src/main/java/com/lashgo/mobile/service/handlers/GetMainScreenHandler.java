package com.lashgo.mobile.service.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lashgo.model.dto.MainScreenInfoDto;
import com.lashgo.model.dto.ResponseObject;

import java.io.IOException;

import retrofit.RetrofitError;

/**
 * Created by Eugene on 09.07.2014.
 */
public class GetMainScreenHandler extends BaseIntentHandler {

    public GetMainScreenHandler(Context context) {
        super(context);
    }

    @Override
    protected Bundle doExecute(Intent intent)
            throws IOException, RetrofitError {
        String lastNewsViewDate = intent.getStringExtra(ServiceExtraNames.LAST_NEWS_VIEW_DATE.name());
        String lastSubscriptionsViewDate = intent.getStringExtra(ServiceExtraNames.LAST_SUBSCRIPTIONS_VIEW_DATE.name());
        ResponseObject<MainScreenInfoDto> responseObject = service.getUserMainScreenInfo(lastNewsViewDate, lastSubscriptionsViewDate);
        Bundle result = new Bundle();
        result.putSerializable(ServiceExtraNames.MAIN_SCREEN_INFO.name(), responseObject.getResult());
        return result;
    }
}
