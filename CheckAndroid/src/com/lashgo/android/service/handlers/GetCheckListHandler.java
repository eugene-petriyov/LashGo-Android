package com.lashgo.android.service.handlers;

import android.content.Intent;
import android.os.Bundle;
import com.lashgo.model.dto.CheckDto;
import com.lashgo.model.dto.ResponseList;
import retrofit.RetrofitError;

import java.io.IOException;

/**
 * Created by Eugene on 15.07.2014.
 */
public class GetCheckListHandler extends BaseIntentHandler {

    @Override
    protected Bundle doExecute(Intent intent) throws IOException, RetrofitError {
        ResponseList<CheckDto> checkDtoResponseList = service.getChecks();
        Bundle data = new Bundle();
        data.putSerializable(ServiceExtraNames.KEY_CHECK_DTO_LIST.name(), checkDtoResponseList);
        return data;
    }
}
