package com.lashgo.mobile.ui.subscribes;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lashgo.mobile.R;
import com.lashgo.mobile.settings.SettingsHelper;
import com.lashgo.mobile.utils.LashGoUtils;
import com.lashgo.mobile.utils.PhotoUtils;
import com.lashgo.model.dto.SubscriptionDto;

/**
 * Created by Eugene on 20.10.2014.
 */
public class SubscriptionAdapter extends ArrayAdapter<SubscriptionDto> {

    private ActionBtnClickListener listener;

    private SettingsHelper settingsHelper;

    public SubscriptionAdapter(Context context, ActionBtnClickListener listener, SettingsHelper settingsHelper) {
        super(context, -1);
        this.listener = listener;
        this.settingsHelper = settingsHelper;
    }

    private static class ViewHolder {
        private ImageView userAvatar;
        private TextView userName;
        private ImageView actionBtn;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adt_subscription, null);
            viewHolder.userAvatar = (ImageView) convertView.findViewById(R.id.user_avatar);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.actionBtn = (ImageView) convertView.findViewById(R.id.action_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onActionBtnClicked(position);
                }
            }
        });
        SubscriptionDto subscriptionDto = getItem(position);
        int photoSize = PhotoUtils.convertDpToPixels(40, getContext());
        if (!TextUtils.isEmpty(subscriptionDto.getUserAvatar())) {
            PhotoUtils.displayImage(getContext(), viewHolder.userAvatar, LashGoUtils.getUserAvatarUrl(subscriptionDto.getUserAvatar()), photoSize, R.drawable.ava, false);
        } else {
            viewHolder.userAvatar.setImageResource(R.drawable.ava);
        }
        viewHolder.userName.setText(!TextUtils.isEmpty(subscriptionDto.getFio()) ? subscriptionDto.getFio() : subscriptionDto.getUserLogin());
        if (settingsHelper.isLoggedIn() && subscriptionDto.getUserId() != settingsHelper.getUserId()) {
            viewHolder.actionBtn.setVisibility(View.VISIBLE);
            if (!subscriptionDto.isAmISubscribed()) {
                viewHolder.actionBtn.setImageResource(R.drawable.btn_subscribe);
            } else {
                viewHolder.actionBtn.setImageResource(R.drawable.btn_unsubscribe);
            }
        } else {
            viewHolder.actionBtn.setVisibility(View.GONE);
        }
        return convertView;
    }

    public interface ActionBtnClickListener {

        void onActionBtnClicked(int position);
    }
}
