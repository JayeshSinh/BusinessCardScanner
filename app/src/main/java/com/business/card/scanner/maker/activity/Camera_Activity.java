package com.business.card.scanner.maker.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.business.card.scanner.maker.model.Business_Card;
import com.business.card.scanner.maker.util.Constants;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.baseClass.BaseActivityBinding;
import com.business.card.scanner.maker.databinding.BusinessCardFragmentBinding;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Audio;
import com.otaliastudios.cameraview.controls.Engine;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Hdr;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.controls.PictureFormat;
import com.otaliastudios.cameraview.controls.Preview;
import com.otaliastudios.cameraview.controls.WhiteBalance;
import com.otaliastudios.cameraview.gesture.Gesture;
import com.otaliastudios.cameraview.gesture.GestureAction;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Camera_Activity extends BaseActivityBinding {
    public String TAG = "MainCamera";
    public boolean alreadyTookPicture = false;
    BusinessCardFragmentBinding binding;
    public JSONObject cameraMetadataLogs = new JSONObject();
    private int compressQuality = 100;
    public Rect cropRectangleDisplayOnly;
    private Executor executor = Executors.newSingleThreadExecutor();
    boolean isCardAdd = false;
    boolean isUpdate = false;


    public void initMethods() {
    }
    public void initVariable() {
    }
    public void onClick(View view) {
    }
    public void setAdapter() {
    }
    public void setOnClicks() {
    }
    public void setToolbar() {
    }


    public void setBinding() {
        BusinessCardFragmentBinding businessCardFragmentBinding = (BusinessCardFragmentBinding) DataBindingUtil.setContentView(this, R.layout.business_card_fragment);
        this.binding = businessCardFragmentBinding;
        businessCardFragmentBinding.viewFinder.setLifecycleOwner(this);
        setupCameraListeners(this.binding.viewFinder);
        this.isUpdate = getIntent().getBooleanExtra("cameraFlag", false);
        this.isCardAdd = getIntent().getBooleanExtra("IsAddcard", false);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public Bitmap getBitmap(Bitmap bitmap, File file) {
        ExifInterface exifInterface;
        InputStream inputStream;
        ExifInterface exifInterface2 = null;
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    inputStream = getContentResolver().openInputStream(Uri.fromFile(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    inputStream = null;
                }
                exifInterface = new ExifInterface(inputStream);
            } else {
                exifInterface = new ExifInterface(file.getPath());
            }
            exifInterface2 = exifInterface;
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        int attributeInt = exifInterface2.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 0);
        if (attributeInt == 3) {
            return TransformationUtils.rotateImage(bitmap, 180);
        }
        if (attributeInt == 6) {
            return TransformationUtils.rotateImage(bitmap, 90);
        }
        if (attributeInt != 8) {
            return bitmap;
        }
        return TransformationUtils.rotateImage(bitmap, 270);
    }

    private File saveImage(Bitmap bitmap, String str) {
        File file = new File(getTemp(this), str);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return file;
    }

    public static File getTemp(Context context) {
        File file = new File(context.getFilesDir(), "temp");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private void setupCameraListeners(final CameraView cameraView) {
        cameraView.addCameraListener(new CameraListener() {
            public void onCameraClosed() {
            }

            public void onCameraError(CameraException cameraException) {
            }

            public void onCameraOpened(CameraOptions cameraOptions) {
                Log.d(Camera_Activity.this.TAG, "onCameraOpened: camera opened! Will setup sensors");
                Camera_Activity.this.setupCamera(cameraView, cameraOptions);
                Camera_Activity.this.setupCameraButtons(cameraView);
                Camera_Activity.this.mountViewFinder(cameraView);
                try {
                    Camera_Activity.this.cameraMetadataLogs.put("cameraHeight", cameraView.getHeight());
                    Camera_Activity.this.cameraMetadataLogs.put("cameraWidth", cameraView.getWidth());
                } catch (JSONException unused) {
                }
            }

            public void onPictureTaken(PictureResult pictureResult) {
                String str = Camera_Activity.this.TAG;
                Log.d(str, "onPictureTaken: A snapshot of the preview window was taken! with size: " + pictureResult.getSize());
                pictureResult.toBitmap(new OnBitmapReady());
            }
        });
    }

    public void mountViewFinder(CameraView cameraView) {
        Rect createDynamicRectWithAspectRatio = createDynamicRectWithAspectRatio(cameraView.getWidth(), cameraView.getHeight());
        this.cropRectangleDisplayOnly = new Rect(createDynamicRectWithAspectRatio.left, createDynamicRectWithAspectRatio.top, createDynamicRectWithAspectRatio.right, createDynamicRectWithAspectRatio.bottom);
        String str = this.TAG;
        Log.d(str, "setupDynamicCropRectangle: new viewfinder for display only rect is: " + this.cropRectangleDisplayOnly);
        cameraView.addView(new ViewFinderRectangle(this));
    }

    private Bitmap cropBitmapUsingCropRect(Bitmap bitmap) {
        Rect createDynamicRectWithAspectRatio = createDynamicRectWithAspectRatio(bitmap.getWidth(), bitmap.getHeight());
        String str = this.TAG;
        Log.d(str, "takePictureFromCameraPreview: cameraPreviewBitmap croprect " + createDynamicRectWithAspectRatio.left + StringUtils.SPACE + createDynamicRectWithAspectRatio.top + StringUtils.SPACE + createDynamicRectWithAspectRatio.right + StringUtils.SPACE + createDynamicRectWithAspectRatio.bottom);
        return Bitmap.createBitmap(bitmap, createDynamicRectWithAspectRatio.left, createDynamicRectWithAspectRatio.top, createDynamicRectWithAspectRatio.width(), createDynamicRectWithAspectRatio.height());
    }

    private Rect createDynamicRectWithAspectRatio(int i, int i2) {
        int i3 = (i * 7) / 100;
        int i4 = i2 / 2;
        int i5 = (int) ((((double) (i - (i3 * 2))) / 1.75d) / 2.0d);
        return new Rect(i3, i4 - i5, i - i3, i4 + i5);
    }

    public void endActivityAndReturnResult(Bitmap bitmap) {
        Bitmap cropBitmapUsingCropRect = cropBitmapUsingCropRect(bitmap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        cropBitmapUsingCropRect.compress(Bitmap.CompressFormat.JPEG, this.compressQuality, byteArrayOutputStream);
        byteArrayOutputStream.toByteArray();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        Uri fromFile = Uri.fromFile(saveImage(cropBitmapUsingCropRect, simpleDateFormat.format(new Date()) + ".jpg"));
        Intent intent = new Intent();
        intent.putExtra("imageuri", fromFile);
        intent.putExtra("isCardAdd", this.isCardAdd);
        setResult(-1, intent);
        finish();
    }

    public void setupCamera(CameraView cameraView, CameraOptions cameraOptions) {
        cameraView.setMode(Mode.PICTURE);
        cameraView.setEngine(Engine.CAMERA1);
        cameraView.setPreview(Preview.GL_SURFACE);
        cameraView.setUseDeviceOrientation(false);
        cameraView.setFacing(Facing.BACK);
        cameraView.setFlash(Flash.OFF);
        cameraView.setWhiteBalance(WhiteBalance.AUTO);
        cameraView.setHdr(Hdr.OFF);
        cameraView.setPictureFormat(PictureFormat.JPEG);
        cameraView.setAudio(Audio.OFF);
        cameraView.setPlaySounds(false);
        float previewFrameRateMaxValue = cameraOptions.getPreviewFrameRateMaxValue();
        String str = this.TAG;
        Log.d(str, "setupCamera: max fps: " + previewFrameRateMaxValue);
        cameraView.setPreviewFrameRate(previewFrameRateMaxValue);
        if (cameraOptions.isAutoFocusSupported()) {
            cameraView.setPictureSnapshotMetering(false);
            cameraView.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS);
        }
        if (cameraOptions.isZoomSupported()) {
            cameraView.setZoom(0.0f);
            cameraView.mapGesture(Gesture.PINCH, GestureAction.ZOOM);
        }
    }

    public void setupCameraButtons(final CameraView cameraView) {
        this.binding.cameraCaptureImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!cameraView.isTakingPicture()) {
                    Camera_Activity.this.alreadyTookPicture = true;
                    cameraView.takePictureSnapshot();
                }
            }
        });
    }

    class OnBitmapReady implements BitmapCallback {
        OnBitmapReady() {
        }

        public void onBitmapReady(Bitmap bitmap) {
            if (bitmap != null) {
                Camera_Activity.this.endActivityAndReturnResult(bitmap);
            }
        }
    }

    private class ViewFinderRectangle extends View {
        private Paint blueRectPaint;
        private Paint clearPaint;
        private Paint overlayPaint;
        private Rect rect;

        public ViewFinderRectangle(Context context) {
            super(context);
            Paint paint = new Paint(1);
            this.overlayPaint = paint;
            paint.setColor(getResources().getColor(R.color.black));
            this.overlayPaint.setAlpha(90);
            this.overlayPaint.setStyle(Paint.Style.FILL);
            Paint paint2 = new Paint();
            this.clearPaint = paint2;
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            Paint paint3 = new Paint();
            this.blueRectPaint = paint3;
            paint3.setStyle(Paint.Style.STROKE);
            this.blueRectPaint.setStrokeWidth(10.0f);
            this.blueRectPaint.setColor(getResources().getColor(R.color.purple_200));
            this.blueRectPaint.setStrokeJoin(Paint.Join.MITER);
        }

        public void onDraw(Canvas canvas) {
            Rect rect2 = Camera_Activity.this.cropRectangleDisplayOnly;
            this.rect = rect2;
            if (rect2 != null) {
                canvas.drawPaint(this.overlayPaint);
                canvas.drawRect(this.rect, this.clearPaint);
                canvas.drawPath(createCornersPath(this.rect.left, this.rect.top, this.rect.right, this.rect.bottom, 100), this.blueRectPaint);
            }
        }

        private Path createCornersPath(int i, int i2, int i3, int i4, int i5) {
            Path path = new Path();
            float f = (float) i;
            float f2 = (float) (i2 + i5);
            path.moveTo(f, f2);
            float f3 = (float) i2;
            path.lineTo(f, f3);
            float f4 = (float) (i + i5);
            path.lineTo(f4, f3);
            float f5 = (float) (i3 - i5);
            path.moveTo(f5, f3);
            float f6 = (float) i3;
            path.lineTo(f6, f3);
            path.lineTo(f6, f2);
            float f7 = (float) (i4 - i5);
            path.moveTo(f, f7);
            float f8 = (float) i4;
            path.lineTo(f, f8);
            path.lineTo(f4, f8);
            path.moveTo(f5, f8);
            path.lineTo(f6, f8);
            path.lineTo(f6, f7);
            return path;
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2401) {
            Intent intent2 = new Intent();
            intent2.putExtra("card", (Business_Card) intent.getExtras().get("card"));
            setResult(Constants.ADD_CARD, intent2);
            finish();
        }
    }
}
