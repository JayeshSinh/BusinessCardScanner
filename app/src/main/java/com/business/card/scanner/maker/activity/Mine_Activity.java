package com.business.card.scanner.maker.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.business.card.scanner.maker.BuildConfig;
import com.business.card.scanner.maker.backupRestore.RestoreDriveListActivity;
import com.business.card.scanner.maker.databinding.BackupRestoreBinding;
import com.business.card.scanner.maker.databinding.MineActivityBinding;
import com.business.card.scanner.maker.util.AppPref;
import com.business.card.scanner.maker.util.Constants;

import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.backupRestore.BackupRestore;
import com.business.card.scanner.maker.backupRestore.BackupRestoreProgress;
import com.business.card.scanner.maker.backupRestore.OnBackupRestore;
import com.business.card.scanner.maker.backupRestore.RestoreRowModel;
import com.business.card.scanner.maker.baseClass.BaseActivityBinding;


import org.apache.http.protocol.HTTP;

import java.util.ArrayList;

public class Mine_Activity extends BaseActivityBinding {
    private BackupRestore backupRestore;
    boolean backupScuccess = false;
    MineActivityBinding binding;
    
    private BackupRestoreProgress progressDialog;


    public void initMethods() {
    }


    public void setAdapter() {
    }


    public void setOnClicks() {
    }


    public void setBinding() {
        MineActivityBinding mineActivityBinding = (MineActivityBinding) DataBindingUtil.setContentView(this, R.layout.mine_activity);
        this.binding = mineActivityBinding;
        mineActivityBinding.setLayoutClick(this);

        if (AppPref.isAppLock(this)) {
            this.binding.lock.setImageResource(R.drawable.switch_on);
        } else {
            this.binding.lock.setImageResource(R.drawable.switch_off);
        }
        /*if (!ConsentInformation.getInstance(this).isRequestLocationInEeaOrUnknown() || AppPref.getIsAdfree()) {
            this.binding.adsSettings.setVisibility(View.GONE);
            this.binding.adsSeparator.setVisibility(View.GONE);
            return;
        }
        this.binding.adsSettings.setVisibility(0);
        this.binding.adsSeparator.setVisibility(0);*/
    }


    public void initVariable() {
        this.backupRestore = new BackupRestore(this);
        this.progressDialog = new BackupRestoreProgress(this);
    }


    public void setToolbar() {
        this.binding.toolBar.setNavigationIcon((int) R.drawable.ic_baseline_arrow_back_24);
        this.binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Mine_Activity.this.onBackPressed();
            }
        });
    }

    @SuppressLint("ResourceType")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adsSettings:
                showDialog();
                return;
            case R.id.backup_restorelay:

                BackupRestoreBinding backupRestoreBinding = (BackupRestoreBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.backup_restore, (ViewGroup) null, false);
                Dialog dialog = new Dialog(this);
                dialog.setContentView(backupRestoreBinding.getRoot());
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(-1, -2);
                    dialog.getWindow().setBackgroundDrawableResource(17170445);
                }
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
                backupRestoreBinding.driveBackup.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Mine_Activity.this.backupData(false);
                    }
                });
                backupRestoreBinding.restoreDrive.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Mine_Activity.this.startActivityForResult(new Intent(Mine_Activity.this, RestoreDriveListActivity.class).setFlags(67108864), 1002);
                    }
                });
                return;

                //startActivity(new Intent(this, Pro_Version_Activity.class).setFlags(67108864));
                //return;
            case R.id.favlay:
                startActivity(new Intent(this, Favourites_Activity.class));
                return;
            case R.id.feedlay:
                EmailUs("");
                return;
            case R.id.grouplay:
                startActivity(new Intent(this, Group_Activity.class));
                return;
            case R.id.locklay:
                if (!AppPref.isAppLock(this)) {
                    AppPref.setAppLock(this, true);
                    this.binding.lock.setImageResource(R.drawable.switch_on);
                    return;
                }
                AppPref.setAppLock(this, false);
                this.binding.lock.setImageResource(R.drawable.switch_off);
                return;
            case R.id.policylay:
                Constants.openUrl(this, Constants.PRIVACY_POLICY_URL);
                return;
            case R.id.premiumlay:
                shareApp();
                return;
            case R.id.rateApp:
                Constants.showDialogRate(this);
                return;
            case R.id.servicelay:
                Constants.openUrl(this, Constants.TERMS_OF_SERVICE_URL);
                return;
            case R.id.settinglay:
                startActivity(new Intent(this, Setting_Activity.class));
                return;
            case R.id.sharelay:
                //shareApp();
                return;
            default:
                return;
        }
    }

    private void showDialog() {
        /*Ad_Global.showPersonalizeDialog(false, this, getString(R.string.app_name), getString(R.string.app_description1), getString(R.string.app_description2), getString(R.string.app_description3), new NPATwoButtonDialogListener() {
            public void onCancel() {
            }

            public void onOk(boolean z) {
                if (z) {
                  //  ConsentInformation.getInstance(Mine_Activity.this).setConsentStatus(ConsentStatus.PERSONALIZED);
                } else {
                   // ConsentInformation.getInstance(Mine_Activity.this).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                }
                //Ad_Global.setnpa(Mine_Activity.this);
            }
        });*/
    }


    public void backupData(boolean z) {
        this.backupRestore.backupRestore(this.progressDialog, z, true, (String) null, false, new OnBackupRestore() {
            public void getList(ArrayList<RestoreRowModel> arrayList) {
            }

            public void onSuccess(boolean z) {
                if (z) {
                    Mine_Activity mine_Activity = Mine_Activity.this;
                    AppConstant.toastShort(mine_Activity, mine_Activity.getString(R.string.export_successfully));
                    Log.e("Drive", "onSuccess: Uploaded " );
                    return;
                }
                Mine_Activity mine_Activity2 = Mine_Activity.this;
                Log.e("Drive", "onSuccess: not Uploaded " );
                AppConstant.toastShort(mine_Activity2, mine_Activity2.getString(R.string.failed_to_import));
            }
        });
    }

    public void shareApp() {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType(HTTP.PLAIN_TEXT_TYPE);
            intent.putExtra("android.intent.extra.SUBJECT", Constants.APP_TITLE);
            intent.putExtra("android.intent.extra.TEXT", Constants.APP_TITLE + "Business Card Scanner scans and manages all your business cards and contacts.\n- Organize your cards with custom group names\n- Sort data by Name, Company, and Date\n- Easy Search your cards from the list\n- Share a business card via email. (Including text and image)\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e) {
            Log.d("", e.toString());
        }
    }

    public void EmailUs(String str) {
        try {
            String str2 = Build.MODEL;
            String str3 = Build.MANUFACTURER;
            Intent intent = new Intent("android.intent.action.SENDTO");
            intent.setData(Uri.parse("mailto:"));
            Intent intent2 = new Intent("android.intent.action.SENDTO");
            intent2.putExtra("android.intent.extra.EMAIL", new String[]{Constants.EMAIL});
            intent2.putExtra("android.intent.extra.SUBJECT", "Your Suggestion - " + Constants.APP_TITLE + "(" + getPackageName() + ")");
            intent2.putExtra("android.intent.extra.TEXT", str + "\n\nDevice Manufacturer : " + str3 + "\nDevice Model : " + str2 + "\nAndroid Version : " + Build.VERSION.RELEASE + "\nApp Version : " + BuildConfig.VERSION_NAME);
            intent2.setSelector(intent);
            startActivityForResult(intent2, 9);
        } catch (Exception e) {
            Log.d("", e.toString());
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (intent != null && i2 == -1 && i == 1002) {
            this.backupScuccess = true;
        }
    }

    public void onBackPressed() {
        if (this.backupScuccess) {
            Intent intent = getIntent();
            intent.putExtra("backupScuccess", this.backupScuccess);
            setResult(-1, intent);
        }
        finish();
    }




    public void onDestroy() {

        super.onDestroy();
    }
}
