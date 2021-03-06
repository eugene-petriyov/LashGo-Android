package com.lashgo.mobile.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lashgo.mobile.R;
import com.lashgo.mobile.ui.BaseActivity;
import com.lashgo.mobile.ui.main.MainActivity;
import com.lashgo.mobile.ui.profile.EditProfileActivity;
import com.lashgo.mobile.utils.LashGoUtils;
import com.lashgo.mobile.utils.PhotoUtils;
import com.lashgo.model.dto.UserDto;

/**
 * Created by Eugene on 06.08.2014.
 */
public class SuccessfulRegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final int EDIT_PROFILE_REQUEST = 2;
    private UserDto userDto;

    private LoginActivity.OpenMode openMode;

    private int imageSize;
    private ImageView userAvatarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_success_register);
        getActionBar().hide();
        imageSize = PhotoUtils.convertDpToPixels(64, this);
        initRegisterResponse(savedInstanceState);
        findViewById(R.id.fill_profile).setOnClickListener(this);
        findViewById(R.id.continue_register).setOnClickListener(this);
        userAvatarView = (ImageView) findViewById(R.id.user_avatar);
        if (userDto != null) {
            String avatarUrl = LashGoUtils.getUserAvatarUrl(userDto.getAvatar());
            if (!TextUtils.isEmpty(avatarUrl)) {
                PhotoUtils.displayImage(this, userAvatarView, avatarUrl, imageSize, R.drawable.ava, false);
            }
            ((TextView) findViewById(R.id.user_name)).setText("@" + (TextUtils.isEmpty(userDto.getFio()) ? userDto.getLogin() : userDto.getFio()));
        }
    }

    private void initRegisterResponse(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            userDto = (UserDto) savedInstanceState.getSerializable(ExtraNames.USER_DTO.name());
            openMode = (LoginActivity.OpenMode) savedInstanceState.getSerializable(ExtraNames.OPEN_MODE.name());
        } else {
            Intent intent = getIntent();
            if (intent != null) {
                userDto = (UserDto) intent.getSerializableExtra(ExtraNames.USER_DTO.name());
                openMode = (LoginActivity.OpenMode) intent.getSerializableExtra(ExtraNames.OPEN_MODE.name());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ExtraNames.USER_DTO.name(), userDto);
        outState.putSerializable(ExtraNames.OPEN_MODE.name(), openMode);
        super.onSaveInstanceState(outState);
    }

    public static Intent buildIntent(Context context, UserDto userDto, LoginActivity.OpenMode openMode) {
        Intent intent = new Intent(context, SuccessfulRegisterActivity.class);
        intent.putExtra(ExtraNames.USER_DTO.name(), userDto);
        return intent;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.continue_register) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else if (view.getId() == R.id.fill_profile) {
            startActivityForResult(EditProfileActivity.buildIntent(this, userDto, EditProfileActivity.FROM.REGISTER), EDIT_PROFILE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void logout() {
        //TODO not to implement
    }

    @Override
    protected void refresh() {

    }
}
