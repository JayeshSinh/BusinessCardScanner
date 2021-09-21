package com.business.card.scanner.maker.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

import com.business.card.scanner.maker.R;


import com.business.card.scanner.maker.databinding.SplashActivityBinding;


import java.lang.ref.WeakReference;

public class Splash_Activity extends AppCompatActivity {
    public static boolean Ad_Show = false;
    private static final int LOCK_REQUEST_CODE = 221;
    private static final int SECURITY_SETTING_REQUEST_CODE = 233;
    public static boolean isRated = false;
    public static boolean isRatedFlag = false;
    int SPLASH_DISPLAY_LENGTH = 3000;
    SplashActivityBinding binding;
    Context context;
    Splash_Activity splash_activity;

    public WeakReference<Splash_Activity> splash_activityWeakReference;

    public Splash_Activity() {
        Ad_Show = true;
        //Ad_Global.adCount = 0;
        isRated = false;
        isRatedFlag = false;
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        this.binding = (SplashActivityBinding) DataBindingUtil.setContentView(this, R.layout.splash_activity);
        this.context = this;
        this.splash_activity = this;
        this.splash_activityWeakReference = new WeakReference<>(this.splash_activity);
        Glide.with((FragmentActivity) this).load(R.drawable.splash_logo).into(this.binding.llLogo);

        new Handler(getMainLooper()).postDelayed(new NextScreen(), 4000);

    }

    public void authenticApp() {
        GoToMainScreen();
        /*if (AppPref.isAppLock(this)) {
            authenticateApp();
        } else {
        }*/
    }

    private void GoToMainScreen() {

        startActivity(new Intent(this.context, MainActivity.class));
        /*if (AppPref.IsTermsAccept(this.context)) {
        } else {
            startActivity(new Intent(this.context, DisclosureActivity.class));
        }*/
        finish();
    }

    private void authenticateApp() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                startActivityForResult(keyguardManager.createConfirmDeviceCredentialIntent(getResources().getString(R.string.unlock), getResources().getString(R.string.confirm_pattern)), LOCK_REQUEST_CODE);
            } catch (Exception unused) {
                try {
                    startActivityForResult(new Intent("android.settings.SECURITY_SETTINGS"), SECURITY_SETTING_REQUEST_CODE);
                } catch (Exception unused2) {
                }
            }
        }
    }

    private boolean isDeviceSecure() {
        return Build.VERSION.SDK_INT >= 16 && ((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).isKeyguardSecure();
    }

    public void authenticateAgain(View view) {
        authenticateApp();
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != LOCK_REQUEST_CODE) {
            if (i == SECURITY_SETTING_REQUEST_CODE && isDeviceSecure()) {
                Toast.makeText(this, getResources().getString(R.string.device_is_secure), Toast.LENGTH_SHORT).show();
                authenticateApp();
            }
        } else if (i2 == -1) {
            GoToMainScreen();
        }
    }



    class NextScreen implements Runnable {
        NextScreen() {
        }

        public void run() {
            if (Splash_Activity.Ad_Show) {
                Splash_Activity.this.authenticApp();
            }
        }
    }
}
