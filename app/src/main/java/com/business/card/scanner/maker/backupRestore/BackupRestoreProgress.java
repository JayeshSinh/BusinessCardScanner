package com.business.card.scanner.maker.backupRestore;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;


import com.business.card.scanner.maker.R;

import java.lang.ref.WeakReference;

public class BackupRestoreProgress {
    Activity activity;
    boolean isShowing = false;
    TextView percent;
    private Dialog progressDialog = null;
    TextView textView;
    WeakReference<Activity> weakReference;

    public BackupRestoreProgress(Activity activity2) {
        this.progressDialog = new Dialog(activity2);
        this.activity = activity2;
        this.weakReference = new WeakReference<>(activity2);
        this.progressDialog.setContentView(R.layout.bacup_restore_progress_dialog);
        this.textView = (TextView) this.progressDialog.findViewById(R.id.message);
        this.percent = (TextView) this.progressDialog.findViewById(R.id.percent);
        this.progressDialog.setCancelable(false);
    }

    public void showDialog() {
        Dialog dialog = this.progressDialog;
        if (dialog != null && !this.isShowing) {
            dialog.show();
            this.isShowing = true;
        }
    }

    public void dismissDialog() {
        Dialog dialog = this.progressDialog;
        if (dialog != null && dialog.isShowing() && this.weakReference.get() != null) {
            this.progressDialog.dismiss();
            this.isShowing = false;
        }
    }

    public void setMessage(String str) {
        this.textView.setText(str);
    }

    public void setPercent(String str) {
        this.percent.setText(str);
    }

    public boolean isShowing() {
        return this.isShowing;
    }
}
