package com.business.card.scanner.maker.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


import com.business.card.scanner.maker.BuildConfig;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.business.card.scanner.maker.R;

public class Constants {
    public static final int ADD_CARD = 2401;
    public static final int ADD_CARD_After_Camera = 2402;
    public static final int ADD_CARD_UPDATE = 2405;
    public static final String APP_PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";
    public static String APP_TITLE = "BizReader: Business Card Scanner & Reader";
    public static final int Camera_Retake = 2403;
    public static String DISCLOSURE_DIALOG_DESC = "We would like to inform you regarding the 'Consent to Collection and Use Of PostFeed'\n\nTo save business card image into gallery, allow access to storage permission.\n\nWe store your data on your device only, we don’t store them on our server.";
    public static final String DOWNLOAD_DIRECTORY = "Business Card";
    public static String EMAIL = "fittech315@gmail.com";
    public static String PRIVACY_POLICY_URL = "https://sites.google.com/view/fittech-privacypolicy";
    public static final int Qr_CARD = 2406;
    public static final String RATTING_BAR_TITLE = "Support us by giving rate and your precious review !!\nIt will take few seconds only.";
    public static int REQUEST_CODE_SIGN_IN = 1005;
    public static String TERMS_OF_SERVICE_URL = "https://sites.google.com/view/fittech-termsofservice";

    @SuppressLint("WrongConstant")
    public static void openUrl(Context context, String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addFlags(1208483840);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(PRIVACY_POLICY_URL)));
        }
    }

    public static void showDialogRate(final Activity activity) {
        final SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getPackageName(), 0);
        RatingDialog.Builder ratingBarBackgroundColor = new RatingDialog.Builder(activity).session(1).title(RATTING_BAR_TITLE).threshold(4.0f).icon(activity.getResources().getDrawable(R.mipmap.ic_launcher)).titleTextColor(R.color.black).negativeButtonText("Never").positiveButtonTextColor(R.color.white).negativeButtonTextColor(R.color.black).formTitle("Submit Feedback").formHint("Tell us where we can improve").formSubmitText("Submit").formCancelText("Cancel")
                .ratingBarColor(R.color.ratingBarColor)
                .ratingBarBackgroundColor(R.color.ratingBarBackgroundColor);
        RatingDialog build = ratingBarBackgroundColor.playstoreUrl(APP_PLAY_STORE_URL + activity.getPackageName()).onRatingChanged(new RatingDialog.Builder.RatingDialogListener() {
            public void onRatingSelected(float f, boolean z) {
                double d = (double) f;
                if (d == 4.0d || d == 5.0d) {
                    AppPref.setIsRateUsNew(activity, true);
                }
            }
        }).onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
            public void onFormSubmitted(String str) {
                Constants.EmailUs(str, activity);
                AppPref.setIsRateUsNew(activity, true);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean("shownever", true);
                edit.commit();
            }
        }).build();
        if (sharedPreferences.getBoolean("shownever", false)) {
            Toast.makeText(activity, "Already Submitted", Toast.LENGTH_SHORT).show();
        } else {
            build.show();
        }
    }

    public static void showDialogRateAction(final Activity activity) {
        /*final SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getPackageName(), 0);
        RatingDialog.Builder ratingBarBackgroundColor = new RatingDialog.Builder(activity).session(1).title(RATTING_BAR_TITLE).threshold(5.0f).icon(activity.getResources().getDrawable(R.mipmap.ic_launcher)).titleTextColor(R.C0767color.black).negativeButtonText("Never").positiveButtonTextColor(R.C0767color.white).negativeButtonTextColor(R.C0767color.black).formTitle("Submit Feedback").formHint("Tell us where we can improve").formSubmitText("Submit").formCancelText("Cancel").ratingBarColor(R.C0767color.ratingBarColor).ratingBarBackgroundColor(R.C0767color.ratingBarBackgroundColor);
        ratingBarBackgroundColor.playstoreUrl(APP_PLAY_STORE_URL + activity.getPackageName()).onRatingChanged(new RatingDialog.Builder.RatingDialogListener() {
            public void onRatingSelected(float f, boolean z) {
                double d = (double) f;
                if (d == 4.0d || d == 5.0d) {
                    AppPref.setIsRateUsNew(activity, true);
                }
            }
        }).onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
            public void onFormSubmitted(String str) {
                AppPref.setIsRateUsNew(activity, true);
                Constants.EmailUs(str, activity);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putBoolean("shownever", true);
                edit.commit();
            }
        }).build().show();*/
    }

    public static void EmailUs(String str, Activity activity) {
        try {
            String str2 = Build.MODEL;
            String str3 = Build.MANUFACTURER;
            Intent intent = new Intent("android.intent.action.SENDTO");
            intent.setData(Uri.parse("mailto:"));
            Intent intent2 = new Intent("android.intent.action.SENDTO");
            intent2.putExtra("android.intent.extra.EMAIL", new String[]{EMAIL});
            intent2.putExtra("android.intent.extra.SUBJECT", APP_TITLE + "(" + activity.getPackageName() + ")");
            intent2.putExtra("android.intent.extra.TEXT", str + "\n\nDevice Manufacturer : " + str3 + "\nDevice Model : " + str2 + "\nAndroid Version : " + Build.VERSION.RELEASE + "\nApp Version : " + BuildConfig.VERSION_NAME);
            intent2.setSelector(intent);
            ActivityCompat.startActivityForResult(activity, intent2, 9, (Bundle) null);
        } catch (Exception unused) {
        }
    }
}
