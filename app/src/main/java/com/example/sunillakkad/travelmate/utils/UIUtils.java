package com.example.sunillakkad.travelmate.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by mm98568 on 9/16/16.
 */
public class UIUtils {

    public static void activityFullScreenMode(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static ProgressDialog createProgressDialog(Context context, String title) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(title);

        return mProgressDialog;
    }
}
