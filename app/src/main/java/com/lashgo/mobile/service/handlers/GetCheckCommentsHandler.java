package com.lashgo.mobile.service.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lashgo.model.dto.CommentDto;
import com.lashgo.model.dto.ResponseList;

import java.io.IOException;

import retrofit.RetrofitError;

/**
 * Created by Eugene on 11.09.2014.
 */
public class GetCheckCommentsHandler extends BaseIntentHandler {
    public GetCheckCommentsHandler(Context context) {
        super(context);
    }

    @Override
    protected Bundle doExecute(Intent intent) throws IOException, RetrofitError {
        ResponseList<CommentDto> responseList = service.getCheckComments(intent.getIntExtra(ServiceExtraNames.CHECK_ID.name(), -1));
        Bundle bundle = new Bundle();
        bundle.putSerializable(ServiceExtraNames.COMMENTS_LIST.name(), responseList.getResultCollection());
        return bundle;
    }
}
