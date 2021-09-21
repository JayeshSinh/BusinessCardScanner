package com.business.card.scanner.maker.activity;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import androidx.databinding.DataBindingUtil;


import com.business.card.scanner.maker.util.AppPref;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.baseClass.BaseActivityBinding;
import com.business.card.scanner.maker.databinding.SettingActivityBinding;


import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Setting_Activity extends BaseActivityBinding implements EasyPermissions.PermissionCallbacks {
    SettingActivityBinding binding;



    public void initMethods() {
    }


    public void initVariable() {
    }


    public void setAdapter() {
    }


    public void setOnClicks() {
    }


    public void setBinding() {
        SettingActivityBinding settingActivityBinding = (SettingActivityBinding) DataBindingUtil.setContentView(this, R.layout.setting_activity);
        this.binding = settingActivityBinding;
        settingActivityBinding.setClickLisner(this);

        if (AppPref.isGroup_choose(this)) {
            this.binding.grouplock.setImageResource(R.drawable.switch_on);
        } else {
            this.binding.grouplock.setImageResource(R.drawable.switch_off);
        }
        if (AppPref.isAuto_save(this)) {
            this.binding.ivgallary.setImageResource(R.drawable.switch_on);
        } else {
            this.binding.ivgallary.setImageResource(R.drawable.switch_off);
        }
        if (AppPref.isLast_first(this)) {
            this.binding.lastfirst.setChecked(true);
        } else {
            this.binding.firstlast.setChecked(true);
        }
    }


    public void setToolbar() {
        setSupportActionBar(this.binding.toolBar);
        getSupportActionBar().setTitle((CharSequence) "Setting");
        this.binding.toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        this.binding.toolBar.setNavigationIcon((int) R.drawable.ic_baseline_arrow_back_24);
        this.binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Setting_Activity.this.onBackPressed();
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.lastfirst) {
            AppPref.setLastFirst(this, true);
        } else if (view.getId() == R.id.firstlast) {
            AppPref.setLastFirst(this, false);
        }
        if (view.getId() == R.id.grouplock) {
            if (!AppPref.isGroup_choose(this)) {
                AppPref.setGroup_choose(this, true);
                this.binding.grouplock.setImageResource(R.drawable.switch_on);
                return;
            }
            AppPref.setGroup_choose(this, false);
            this.binding.grouplock.setImageResource(R.drawable.switch_off);
        } else if (view.getId() != R.id.ivgallary) {
        } else {
            if (Build.VERSION.SDK_INT > 29) {
                afterStoragePermission();
            } else {
                methodRequiresStoragePermission();
            }
        }
    }

    private void methodRequiresStoragePermission() {
        String[] strArr = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (EasyPermissions.hasPermissions(this, strArr)) {
            afterStoragePermission();
        } else {
            EasyPermissions.requestPermissions((Activity) this, getString(R.string.storage_permission), (int) AppConstant.RC_STORAGE, strArr);
        }
    }

    private void afterStoragePermission() {
        if (!AppPref.isAuto_save(this)) {
            AppPref.setAutoSave(this, true);
            this.binding.ivgallary.setImageResource(R.drawable.switch_on);
            return;
        }
        AppPref.setAutoSave(this, false);
        this.binding.ivgallary.setImageResource(R.drawable.switch_off);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    public void onPermissionsGranted(int i, List<String> list) {
        afterStoragePermission();
    }

    public void onPermissionsDenied(int i, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }




    public void onDestroy() {
        super.onDestroy();
    }
}
