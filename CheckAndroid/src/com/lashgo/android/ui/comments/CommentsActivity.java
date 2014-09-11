package com.lashgo.android.ui.comments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.lashgo.android.R;
import com.lashgo.android.service.handlers.BaseIntentHandler;
import com.lashgo.android.ui.BaseActivity;
import com.lashgo.model.dto.CommentDto;

import java.util.ArrayList;

/**
 * Created by Eugene on 11.09.2014.
 */
public class CommentsActivity extends BaseActivity implements View.OnClickListener {

    private int checkId;

    private long photoId;

    private CommentsAdapter commentsAdapter;

    private EditText editComment;

    public static Intent buildCheckIntent(Context context, int checkId) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(ExtraNames.CHECK_ID.name(), checkId);
        return intent;
    }

    public static Intent buildPhotoIntent(Context context, long photoId) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(ExtraNames.PHOTO_ID.name(), photoId);
        return intent;
    }

    private void initExtras(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            checkId = intent.getIntExtra(ExtraNames.CHECK_ID.name(), -1);
            photoId = intent.getLongExtra(ExtraNames.PHOTO_ID.name(), -1);
        }
        if (savedInstanceState != null && checkId <= 0 && photoId <= 0) {
            checkId = savedInstanceState.getInt(ExtraNames.CHECK_ID.name());
            photoId = savedInstanceState.getLong(ExtraNames.PHOTO_ID.name());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.act_comments);
        initExtras(savedInstanceState);
        initViews();
        if (checkId > 0) {
            serviceHelper.getCheckComments(checkId);
        } else {
            serviceHelper.getPhotoComments(photoId);
        }
    }

    private void initViews() {
        ListView commentsListView = (ListView) findViewById(R.id.comments_list);
        commentsAdapter = new CommentsAdapter(this);
        commentsListView.setAdapter(commentsAdapter);
        if (!settingsHelper.isLoggedIn()) {
            findViewById(R.id.add_comment_layout).setVisibility(View.GONE);
        }
        else
        {
            findViewById(R.id.btn_add_comment).setOnClickListener(this);
            editComment = (EditText) findViewById(R.id.edit_comment);
        }
    }


    @Override
    protected void registerActionsListener() {
        super.registerActionsListener();
        addActionListener(BaseIntentHandler.ServiceActionNames.ACTION_GET_CHECK_COMMENTS.name());
        addActionListener(BaseIntentHandler.ServiceActionNames.ACTION_GET_PHOTO_COMMENTS.name());
        addActionListener(BaseIntentHandler.ServiceActionNames.ACTION_ADD_CHECK_COMMENT.name());
        addActionListener(BaseIntentHandler.ServiceActionNames.ACTION_ADD_PHOTO_COMMENT.name());
    }


    @Override
    protected void unregisterActionsListener() {
        super.unregisterActionsListener();
        removeActionListener(BaseIntentHandler.ServiceActionNames.ACTION_GET_CHECK_COMMENTS.name());
        removeActionListener(BaseIntentHandler.ServiceActionNames.ACTION_GET_PHOTO_COMMENTS.name());
        removeActionListener(BaseIntentHandler.ServiceActionNames.ACTION_ADD_CHECK_COMMENT.name());
        removeActionListener(BaseIntentHandler.ServiceActionNames.ACTION_ADD_PHOTO_COMMENT.name());
    }

    @Override
    public void processServerResult(String action, int resultCode, Bundle data) {
        super.processServerResult(action, resultCode, data);
        if (BaseIntentHandler.SUCCESS_RESPONSE == resultCode && data != null) {
            if ((BaseIntentHandler.ServiceActionNames.ACTION_GET_CHECK_COMMENTS.name().equals(action) ||
                    BaseIntentHandler.ServiceActionNames.ACTION_GET_PHOTO_COMMENTS.name().equals(action))) {
                onCommentsLoaded((ArrayList<CommentDto>) data.getSerializable(BaseIntentHandler.ServiceExtraNames.COMMENTS_LIST.name()));
            } else {
                onCommentAdded((CommentDto) data.getSerializable(BaseIntentHandler.ServiceExtraNames.COMMENT.name()));
            }
        }
    }

    private void onCommentAdded(CommentDto commentDto) {
        if (commentDto != null) {
            commentsAdapter.add(commentDto);
            commentsAdapter.notifyDataSetChanged();
        }
    }

    private void onCommentsLoaded(ArrayList<CommentDto> commentsList) {
        if (commentsList != null) {
            for (CommentDto commentDto : commentsList) {
                commentsAdapter.add(commentDto);
            }
            commentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add_comment) {
            String commentText = editComment.getText().toString().trim();
            if (!TextUtils.isEmpty(commentText)) {
                if (checkId > 0) {
                    serviceHelper.addCheckComment(checkId, commentText);
                } else {
                    serviceHelper.addPhotoComment(photoId, commentText);
                }
            } else {
                editComment.setError(getString(R.string.error_empty_comment));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}