package com.business.card.scanner.maker.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


import com.business.card.scanner.maker.R;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class ScannerActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {
    private DecoratedBarcodeView barcodeScannerView;
    private CaptureManager capture;
    ImageView flash;
    private boolean isFlashLightOn = false;
    private FrameLayout switchFlashlightButton;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.scan_qr_code_fragment);
        this.barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        this.flash = (ImageView) findViewById(R.id.flash);
        this.barcodeScannerView.setTorchListener(this);
        this.switchFlashlightButton = (FrameLayout) findViewById(R.id.switch_flashlight);
        if (!hasFlash()) {
            this.switchFlashlightButton.setVisibility(View.GONE);
        } else {
            this.switchFlashlightButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ScannerActivity.this.switchFlashlight();
                }
            });
        }
        CaptureManager captureManager = new CaptureManager(this, this.barcodeScannerView);
        this.capture = captureManager;
        captureManager.initializeFromIntent(getIntent(), bundle);
        this.capture.decode();
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    }

    public void switchFlashlight() {
        if (this.isFlashLightOn) {
            this.barcodeScannerView.setTorchOff();
            this.isFlashLightOn = false;
            return;
        }
        this.barcodeScannerView.setTorchOn();
        this.isFlashLightOn = true;
    }

    public void onTorchOn() {
        this.flash.setImageResource(R.drawable.flashlight_on);
    }

    public void onTorchOff() {
        this.flash.setImageResource(R.drawable.flashlight_off);
    }


    public void onResume() {
        super.onResume();
        this.capture.onResume();
    }


    public void onPause() {
        super.onPause();
        this.capture.onPause();
    }


    public void onDestroy() {
        super.onDestroy();
        this.capture.onDestroy();
    }


    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.capture.onSaveInstanceState(bundle);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return this.barcodeScannerView.onKeyDown(i, keyEvent) || super.onKeyDown(i, keyEvent);
    }
}
