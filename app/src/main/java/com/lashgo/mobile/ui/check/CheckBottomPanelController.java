package com.lashgo.mobile.ui.check;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lashgo.mobile.LashgoConstants;
import com.lashgo.mobile.R;
import com.lashgo.mobile.ui.BaseActivity;
import com.lashgo.mobile.ui.comments.CommentsActivity;
import com.lashgo.mobile.ui.subscribes.SubscribesActivity;
import com.lashgo.mobile.ui.subscribes.SubscribesFragment;
import com.lashgo.mobile.utils.LashGoUtils;
import com.lashgo.mobile.utils.UiUtils;
import com.lashgo.model.dto.CheckDto;
import com.lashgo.model.dto.PhotoDto;

import java.lang.ref.WeakReference;
import java.text.DateFormat;

/**
 * Created by Eugene on 20.08.2014.
 */
public class CheckBottomPanelController implements View.OnClickListener {

    private View timeLayout;
    private CheckDto checkDto;

    private WeakReference<BaseActivity> activity;

    private TextView peoplesCount;

    private ImageView btnPeoplesCount;

    private TextView commentsCount;

    private ImageView btnComments;

    private PhotoDto photoDto;
    private TextView likesCount;
    private ImageView likesComments;
    private TextView checkTimeText;

    private void commonInit(final BaseActivity activity, final View view) {
        this.activity = new WeakReference<>(activity);
        btnPeoplesCount = (ImageView) view.findViewById(R.id.btn_peoples_count);
        btnPeoplesCount.setOnClickListener(this);
        peoplesCount = ((TextView) view.findViewById(R.id.peoples_count));
        commentsCount = ((TextView) view.findViewById(R.id.comments_count));
        btnComments = (ImageView) view.findViewById(R.id.btn_comments);
        btnComments.setOnClickListener(this);
        likesCount = ((TextView) view.findViewById(R.id.likes_count));
        likesComments = (ImageView) view.findViewById(R.id.btn_likes);
        likesComments.setOnClickListener(this);
    }

    public void updatePeoplesCount(int peoplesCount) {
        this.peoplesCount.setText(String.valueOf(peoplesCount));
    }

    public CheckBottomPanelController(final BaseActivity activity, View view, final PhotoDto photoDto) {
        commonInit(activity, view);
        this.photoDto = photoDto;
        if (photoDto == null) {
            throw new IllegalArgumentException("Photo info can't be empty!");
        }
        view.findViewById(R.id.time_layout).setVisibility(View.GONE);
        view.findViewById(R.id.peoples_layout).setVisibility(View.GONE);
        updateCommentsCount(photoDto.getCommentsCount());
        updateLikesCount(photoDto.getLikesCount());
    }

    public void updateCommentsCount(int commentsCount) {
        this.commentsCount.setText(String.valueOf(commentsCount));
    }

    public CheckBottomPanelController(final BaseActivity activity, final View view, final CheckDto checkDto) {
        commonInit(activity, view);
        this.checkDto = checkDto;
        view.findViewById(R.id.likes_layout).setVisibility(View.GONE);
        ((TextView)view.findViewById(R.id.start_date)).setText(DateFormat.getDateInstance().format(checkDto.getStartDate()));
        if (checkDto == null) {
            throw new IllegalArgumentException("Check can't be empty!");
        }
        updatePeoplesCount(checkDto.getPlayersCount());
        LashgoConstants.CheckState checkState = LashGoUtils.getCheckState(checkDto);
        /**
         * we are at check screen
         */
        if (LashgoConstants.CheckState.ACTIVE.equals(checkState)) {
            /**
             * check is active
             */
            view.findViewById(R.id.comments_layout).setVisibility(View.GONE);
            checkTimeText = (TextView) view.findViewById(R.id.check_time);
            timeLayout = view.findViewById(R.id.time_layout);
        } else {
            /**
             * check is finished
             */
            view.findViewById(R.id.comments_layout).setVisibility(View.GONE);
            view.findViewById(R.id.time_layout).setVisibility(View.GONE);
            view.findViewById(R.id.check_time).setVisibility(View.GONE);
        }
    }

    public CheckBottomPanelController(final BaseActivity activity, final CheckDto checkDto) {
        this(activity, activity.getWindow().getDecorView(), checkDto);
    }

    @Override
    public void onClick(View view) {
        if (activity != null && activity.get() != null) {
            if (view.getId() == R.id.btn_comments) {
                if (photoDto != null) {
                    activity.get().startActivity(CommentsActivity.buildPhotoIntent(activity.get(), photoDto.getId()));
                } else {
                    throw new IllegalStateException("Photo can't be null at comments click");
                }
            } else if (view.getId() == R.id.btn_peoples_count) {
                if (checkDto != null) {
                    activity.get().startActivity(SubscribesActivity.buildIntent(activity.get(), checkDto.getId(), SubscribesFragment.ScreenType.CHECK_USERS));
                } else {
                    throw new IllegalStateException("Check can't be null at peoples count click");
                }
            } else if (view.getId() == R.id.btn_likes) {
                if (photoDto != null) {
                    activity.get().startActivity(SubscribesActivity.buildIntent(activity.get(), photoDto.getId(), SubscribesFragment.ScreenType.VOTE_USERS));
                } else {
                    throw new IllegalStateException("Photo can't be null at liked click");
                }
            }
        }
    }

    public void updateLikesCount(int likesCount) {
        this.likesCount.setText(String.valueOf(likesCount));
    }

    public void onTimerFinished() {
        timeLayout.setVisibility(View.GONE);
    }

    public void updateTime(long millisUntilFinished) {
        UiUtils.updateCheckTime(millisUntilFinished, checkTimeText);
    }
}

