package com.lashgo.android.ui.check;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import com.lashgo.android.R;
import com.lashgo.android.utils.IntentUtils;

import java.io.File;
import java.util.Date;

/**
 * Created by Eugene on 05.08.2014.
 */
public class MakePhotoDialog extends DialogFragment {
    public static final String TAG = "MAKE_PHOTO_DIALOG_FRAGMENT";
    final public static int PICK_IMAGE = 1;
    final public static int CAPTURE_IMAGE = 2;

    private OnImageDoneListener listener;

    public interface OnImageDoneListener {
        void imageDone(String imagePath);
    }

    public MakePhotoDialog() {
        super();
    }

    public MakePhotoDialog(OnImageDoneListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_make_photo, null);
        view.findViewById(R.id.take_pictute_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(IntentUtils.makeOpenCameraIntent(setImageUri()), CAPTURE_IMAGE);
                dismiss();
            }
        });
        view.findViewById(R.id.choose_from_gallery_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(
                        Intent.createChooser(IntentUtils.makeOpenGalleryIntent(), ""),
                        PICK_IMAGE);
                dismiss();
            }
        });
        return new AlertDialog.Builder(getActivity()).setView(view)
                .create();
    }

    private Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory()
                + "/DCIM/Camera/", "IMG_" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        if (listener != null) {
            listener.imageDone(file.getAbsolutePath());
        }
        return imgUri;
    }
}
