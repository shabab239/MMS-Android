package com.shabab.mezz.util;

import android.app.ProgressDialog;
import android.content.Context;

public class Wait {

    private static ProgressDialog progressDialog;

    private Wait() {}

    public static void show(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait...");
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public static void dismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}

