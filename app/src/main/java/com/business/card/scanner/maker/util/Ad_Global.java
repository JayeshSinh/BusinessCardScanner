/*
package com.fittech.bizcardscanner.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fittech.bizcardscanner.R;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.List;

public class Ad_Global {
    public static String AD_AppOpen = strAdAppOpen();
    public static String AD_Banner = StrADBANNER();
    public static String AD_Full = StrADFULL();
    public static String NATIVE_ID = strADNATIVE();
    public static int adCount = 0;
    public static int adLimit = 2;
    public static boolean npaflag = false;
    public static String[] publisherIds = {StrPUBLISHERID()};

    public static native String StrADBANNER();

    public static native String StrADFULL();

    public static native String StrPUBLISHERID();

    private static native String strADNATIVE();

    private static native String strAdAppOpen();

    static {
        System.loadLibrary("native-lib");
    }

    public static void setBannerAd(Context context, final LinearLayout linearLayout, FrameLayout frameLayout) {
        if (AppPref.getIsAdfree()) {
            linearLayout.setVisibility(8);
        } else {
            loadBannerAd(context, frameLayout).setAdListener(new AdListener() {
                public void onAdLoaded() {
                    linearLayout.setVisibility(8);
                }

                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    linearLayout.setVisibility(8);
                }
            });
        }
    }

    public static AdView loadBannerAd(Context context, FrameLayout frameLayout) {
        AdManagerAdRequest adManagerAdRequest;
        AdView adView = new AdView(context);
        adView.setAdUnitId(AD_Banner);
        frameLayout.removeAllViews();
        frameLayout.addView(adView);
        adView.setAdSize(getAdSize(context));
        if (npaflag) {
            Log.d("NPA", "" + npaflag);
            Bundle bundle = new Bundle();
            bundle.putString("npa", "1");
            adManagerAdRequest = (AdManagerAdRequest) new AdManagerAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
        } else {
            Log.d("NPA", "" + npaflag);
            adManagerAdRequest = new AdManagerAdRequest.Builder().build();
        }
        adView.loadAd(adManagerAdRequest);
        return adView;
    }

    public static AdSize getAdSize(Context context) {
        Display defaultDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, (int) (((float) displayMetrics.widthPixels) / displayMetrics.density));
    }

    public static void populateLarge(NativeAd nativeAd, NativeAdView nativeAdView) {
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.C0769id.appinstall_headline));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.C0769id.appinstall_body));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.C0769id.appinstall_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.C0769id.appinstall_app_icon));
        nativeAdView.setPriceView(nativeAdView.findViewById(R.C0769id.appinstall_price));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.C0769id.appinstall_stars));
        nativeAdView.setStoreView(nativeAdView.findViewById(R.C0769id.appinstall_store));
        ((TextView) nativeAdView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) nativeAdView.getBodyView()).setText(nativeAd.getBody());
        ((Button) nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
        if (nativeAd.getIcon() != null) {
            nativeAdView.getIconView().setVisibility(0);
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
        } else {
            nativeAdView.getIconView().setVisibility(4);
        }
        ImageView imageView = (ImageView) nativeAdView.findViewById(R.C0769id.appinstall_image);
        nativeAdView.setImageView(imageView);
        imageView.setVisibility(0);
        List<NativeAd.Image> images = nativeAd.getImages();
        if (images != null && images.size() > 0) {
            imageView.setImageDrawable(images.get(0).getDrawable());
        }
        if (nativeAd.getPrice() != null) {
            nativeAdView.getPriceView().setVisibility(0);
            ((TextView) nativeAdView.getPriceView()).setText(nativeAd.getPrice());
        } else {
            nativeAdView.getPriceView().setVisibility(4);
        }
        if (nativeAd.getStore() != null) {
            nativeAdView.getStoreView().setVisibility(0);
            ((TextView) nativeAdView.getStoreView()).setText(nativeAd.getStore());
        } else {
            nativeAdView.getStoreView().setVisibility(4);
        }
        if (nativeAd.getStarRating() != null) {
            nativeAdView.getStarRatingView().setVisibility(0);
            ((RatingBar) nativeAdView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
        } else {
            nativeAdView.getStarRatingView().setVisibility(4);
        }
        nativeAdView.setNativeAd(nativeAd);
    }

    public static void showPersonalizeDialog(boolean z, final Context context, String str, String str2, String str3, String str4, NPATwoButtonDialogListener nPATwoButtonDialogListener) {
        Context context2 = context;
        final NPATwoButtonDialogListener nPATwoButtonDialogListener2 = nPATwoButtonDialogListener;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_dialog_addmob);
        boolean z2 = false;
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView textView = (TextView) dialog.findViewById(R.C0769id.txtprivacy);
        textView.setText("Learn how " + context.getResources().getString(R.string.app_name) + " and our partners collect and use data.");
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Ad_Global.privacyuriparse(Constants.PRIVACY_POLICY_URL, context);
            }
        });
        RadioButton radioButton = (RadioButton) dialog.findViewById(R.C0769id.radioPersonalized);
        RadioButton radioButton2 = (RadioButton) dialog.findViewById(R.C0769id.radioNonPersonalized);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.C0769id.radioGroup);
        Button button = (Button) dialog.findViewById(R.C0769id.btnOk);
        ((TextView) dialog.findViewById(R.C0769id.txtTitle)).setText(str);
        ((TextView) dialog.findViewById(R.C0769id.txtDesc1)).setText(str2);
        ((TextView) dialog.findViewById(R.C0769id.txtDesc2)).setText(str3);
        ((TextView) dialog.findViewById(R.C0769id.txtDesc3)).setText(str4);
        ((Button) dialog.findViewById(R.C0769id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                nPATwoButtonDialogListener2.onCancel();
            }
        });
        if (!z) {
            Log.d(" consentStatus setti", "" + ConsentInformation.getInstance(context).getConsentStatus());
            radioButton2.setChecked(ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED);
            if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
                z2 = true;
            }
            radioButton.setChecked(z2);
        }
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    if (radioGroup.getCheckedRadioButtonId() == R.C0769id.radioPersonalized) {
                        nPATwoButtonDialogListener2.onOk(true);
                    } else {
                        nPATwoButtonDialogListener2.onOk(false);
                    }
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void privacyuriparse(String str, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addFlags(1208483840);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Constants.PRIVACY_POLICY_URL)));
        }
    }

    public static void setnpa(Context context) {
        if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
            npaflag = false;
        }
        if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED) {
            npaflag = true;
        }
        Log.d("consentStatus setting", "" + ConsentInformation.getInstance(context).getConsentStatus());
    }

    public static void populateMedium(NativeAd nativeAd, NativeAdView nativeAdView) {
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.C0769id.contentad_headline));
        nativeAdView.setImageView(nativeAdView.findViewById(R.C0769id.contentad_image));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.C0769id.contentad_body));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.C0769id.contentad_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.C0769id.contentad_logo));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.C0769id.contentad_advertiser));
        ((TextView) nativeAdView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) nativeAdView.getBodyView()).setText(nativeAd.getBody());
        ((TextView) nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
        ((TextView) nativeAdView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
        List<NativeAd.Image> images = nativeAd.getImages();
        if (images.size() > 0) {
            ((ImageView) nativeAdView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }
        NativeAd.Image icon = nativeAd.getIcon();
        if (icon == null) {
            nativeAdView.getIconView().setVisibility(8);
        } else {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(icon.getDrawable());
            nativeAdView.getIconView().setVisibility(0);
        }
        nativeAdView.setNativeAd(nativeAd);
    }
}
*/
