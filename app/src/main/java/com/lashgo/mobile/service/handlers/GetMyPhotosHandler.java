package com.lashgo.mobile.service.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lashgo.model.dto.PhotoDto;
import com.lashgo.model.dto.ResponseList;

import java.io.IOException;

import retrofit.RetrofitError;

/**
 * Created by Eugene on 08.09.2014.
 */
public class GetMyPhotosHandler extends BaseIntentHandler {
    public GetMyPhotosHandler(Context context) {
        super(context);
    }

    @Override
    protected Bundle doExecute(Intent intent) throws IOException, RetrofitError {
        ResponseList<PhotoDto> responseList = service.getMyPhotos();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServiceExtraNames.PHOTOS_LIST.name(), responseList.getResultCollection());
        return bundle;
    }
}
