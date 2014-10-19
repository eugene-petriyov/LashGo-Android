package com.lashgo.android.service.handlers;

import android.content.Intent;
import android.os.Bundle;
import com.lashgo.model.dto.ResponseList;
import com.lashgo.model.dto.SubscriptionDto;
import retrofit.RetrofitError;

import java.io.IOException;

/**
 * Created by Eugene on 20.10.2014.
 */
public class GetSubscribersHandler extends BaseIntentHandler {
    @Override
    protected Bundle doExecute(Intent intent) throws IOException, RetrofitError {
        ResponseList<SubscriptionDto> responseList = service.getSubscribers();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServiceExtraNames.SUBSCRIBERS_DTO.name(), responseList.getResultCollection());
        return bundle;
    }
}
