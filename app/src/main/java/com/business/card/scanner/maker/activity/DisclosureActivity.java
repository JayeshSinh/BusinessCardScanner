package com.business.card.scanner.maker.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;


import com.business.card.scanner.maker.util.AppPref;
import com.business.card.scanner.maker.util.Constants;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.baseClass.BaseActivityBinding;
import com.business.card.scanner.maker.databinding.DisclosureActivityBinding;

public class DisclosureActivity extends BaseActivityBinding {
    DisclosureActivityBinding binding;


    public void initMethods() {
    }


    public void initVariable() {
    }


    public void setAdapter() {
    }


    public void setOnClicks() {
    }


    public void setToolbar() {
    }


    public void setBinding() {
        this.binding = DataBindingUtil.setContentView(this, R.layout.disclosure_activity);
        this.context = this;
        this.binding.termsOfService.setOnClickListener(this);
        this.binding.privacyPolicy.setOnClickListener(this);
        this.binding.userAgreement.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.termsOfService) {
            Constants.openUrl(this, Constants.TERMS_OF_SERVICE_URL);
        } else if (id == R.id.privacyPolicy) {
            Constants.openUrl(this, Constants.PRIVACY_POLICY_URL);
        } else if (id == R.id.userAgreement) {
            agreeAndContinueDialog();
        } else if (id == R.id.agreeAndContinue) {
            AppPref.setIsTermsAccept(this.context, true);
            goToMainScreen();
        }
    }

    public void agreeAndContinueDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) Constants.DISCLOSURE_DIALOG_DESC);
        builder.setCancelable(false);
        builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    @SuppressLint("WrongConstant")
    private void goToMainScreen() {
        try {
            startActivity(new Intent(this, MainActivity.class).setFlags(67108864));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
