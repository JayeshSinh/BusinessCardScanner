/*
package com.fittech.bizcardscanner.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.fittech.bizcardscanner.baseClass.BaseActivityBinding;
import com.fittech.bizcardscanner.databinding.ProActivityBinding;
import com.fittech.bizcardscanner.util.AppPref;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Pro_Version_Activity extends BaseActivityBinding implements View.OnClickListener, PurchasesUpdatedListener {
    static final String TAG = "INAPP";
    ProActivityBinding binding;
    ImageView close;
    boolean isServiceConnected;
    private BillingClient mBillingClient;
    boolean mIsPremium = false;
    Activity mactivity;
    SkuDetails skuDetail;
    String skuID = "businesscard_pro";


    public void initMethods() {
    }


    public void setAdapter() {
    }


    public void setToolbar() {
    }


    public void setBinding() {
        ProActivityBinding proActivityBinding = (ProActivityBinding) DataBindingUtil.setContentView(this, R.layout.pro_activity);
        this.binding = proActivityBinding;
        proActivityBinding.setCloseclick(this);
    }


    public void initVariable() {
        setViews();
        setOnClicks();
    }


    public void setViews() {
        this.mactivity = this;
        this.close = (ImageView) findViewById(R.C0769id.close);
        try {
            Activity activity = this.mactivity;
            if (activity != null) {
                this.mBillingClient = BillingClient.newBuilder(activity).enablePendingPurchases().setListener(this).build();
                if (!AppPref.getIsAdfree()) {
                    setUpBilling();
                } else {
                    changeAlreadyOwnItem();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Pro_Version_Activity.this.onBackPressed();
            }
        });
    }


    public void setOnClicks() {
        this.binding.cardBuyPlan.setOnClickListener(this);
    }

    public void changeAlreadyOwnItem() {
        try {
            this.binding.textDesc.setText("Enjoy premium version of application.\n(Pro version is activated)");
            this.binding.cardPrice.setVisibility(8);
            this.binding.cardBuyPlan.setVisibility(8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpBilling() {
        this.mBillingClient.startConnection(new BillingClientStateListener() {
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == 0) {
                    Pro_Version_Activity.this.isServiceConnected = true;
                    if (Pro_Version_Activity.this.restorePurchases()) {
                        Pro_Version_Activity.this.mIsPremium = true;
                        AppPref.setIsAdfree(Pro_Version_Activity.this.mIsPremium);
                        Pro_Version_Activity.this.changeAlreadyOwnItem();
                        Log.e(Pro_Version_Activity.TAG, "Purchased already item");
                        Pro_Version_Activity.this.restartapp("The Premium Version is already purchased.");
                        return;
                    }
                    Log.e(Pro_Version_Activity.TAG, "isServiceConnected == " + Pro_Version_Activity.this.isServiceConnected);
                    Pro_Version_Activity.this.OkBillingProcess();
                }
            }

            public void onBillingServiceDisconnected() {
                Log.e(Pro_Version_Activity.TAG, "isServiceConnected == " + Pro_Version_Activity.this.isServiceConnected);
                Pro_Version_Activity.this.isServiceConnected = false;
            }
        });
    }

    public void OkBillingProcess() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.skuID);
        SkuDetailsParams.Builder newBuilder = SkuDetailsParams.newBuilder();
        newBuilder.setSkusList(arrayList).setType(BillingClient.SkuType.INAPP);
        this.mBillingClient.querySkuDetailsAsync(newBuilder.build(), new SkuDetailsResponseListener() {
            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                if (billingResult.getResponseCode() == 0 && list != null) {
                    for (SkuDetails next : list) {
                        String sku = next.getSku();
                        String price = next.getPrice();
                        if (Pro_Version_Activity.this.skuID.equals(sku) && Pro_Version_Activity.this.binding.textPrice != null) {
                            try {
                                Pro_Version_Activity.this.skuDetail = next;
                                Pro_Version_Activity.this.binding.textPrice.setText(price);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

 access modifiers changed from: private

    public boolean restorePurchases() {
        List<Purchase> purchasesList = this.mBillingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList();
        if (purchasesList == null) {
            return false;
        }
        for (Purchase next : purchasesList) {
            if (next.getSku().equals(this.skuID) && next.getPurchaseState() == 1) {
                return true;
            }
        }
        return false;
    }

    private void buyPlan() {
        if (this.isServiceConnected) {
            try {
                if (this.skuDetail != null) {
                    BillingResult launchBillingFlow = this.mBillingClient.launchBillingFlow(this.mactivity, BillingFlowParams.newBuilder().setSkuDetails(this.skuDetail).build());
                    Log.e("responseCode", launchBillingFlow + "==========>");
                    launchBillingFlow.getResponseCode();
                    return;
                }
                setUpBilling();
                Toast.makeText(this.mactivity, "Server Error try once again..", 0).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setUpBilling();
            Toast.makeText(this.mactivity, "Server Error try once again..", 0).show();
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.C0769id.cardBuyPlan) {
            buyPlan();
        }
    }

    public void onBackPressed() {
        finish();
    }

    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> list) {
        if (billingResult.getResponseCode() == 0 && list != null) {
            for (Purchase handlePurchase : list) {
                handlePurchase(handlePurchase);
            }
        } else if (billingResult.getResponseCode() == 1) {
            Log.e(TAG, "Purchased Canceled");
        } else if (billingResult.getResponseCode() == 7) {
            this.mIsPremium = true;
            AppPref.setIsAdfree(true);
            changeAlreadyOwnItem();
            Log.e(TAG, "Purchased already item");
            restartapp("The item already purchased.");
        }
    }

 access modifiers changed from: package-private

    public void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == 1 && !purchase.isAcknowledged()) {
            this.mBillingClient.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build(), new AcknowledgePurchaseResponseListener() {
                public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                    int responseCode = billingResult.getResponseCode();
                    String debugMessage = billingResult.getDebugMessage();
                    if (responseCode == 0) {
                        Log.d(Pro_Version_Activity.TAG, "acknowledgePurchase: " + responseCode + StringUtils.SPACE + debugMessage);
                        Pro_Version_Activity.this.mIsPremium = true;
                        AppPref.setIsAdfree(Pro_Version_Activity.this.mIsPremium);
                        Log.e(Pro_Version_Activity.TAG, "Purchased Ok");
                        Pro_Version_Activity.this.changeAlreadyOwnItem();
                        Pro_Version_Activity.this.restartapp("Purchased Successfully Done.");
                        return;
                    }
                    AppPref.setIsAdfree(false);
                }
            });
        }
    }

    public void restartapp(String str) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(str + " Restart Application for Pro Version!").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent launchIntentForPackage = Pro_Version_Activity.this.getBaseContext().getPackageManager().getLaunchIntentForPackage(Pro_Version_Activity.this.getBaseContext().getPackageName());
                    launchIntentForPackage.addFlags(335577088);
                    Pro_Version_Activity.this.startActivity(launchIntentForPackage);
                    Pro_Version_Activity.this.finish();
                    System.exit(0);
                }
            });
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
