package com.business.card.scanner.maker.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;

import com.business.card.scanner.maker.listener.ImageListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import io.reactivex.disposables.CompositeDisposable;

public class ImageCompressionAsyncTask {
    Context context;
    public CompositeDisposable disposable;

    /* renamed from: id */
    long f178id;
    ImageListener imageCopy;
    String initialFilePath = "";
    String path = "";

   /* public ImageCompressionAsyncTask(Context context2, String str, ImageListener imageListener) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        this.disposable = compositeDisposable;
        this.context = context2;
        this.f178id = this.f178id;
        this.initialFilePath = str;
        this.imageCopy = imageListener;
        compositeDisposable.add(Observable.fromCallable(new Callable<Call>(context2) {
            @Override
            public Call call() throws Exception {
                return ImageCompressionAsyncTask.this.lambda$new$0$ImageCompressionAsyncTask(context2);
            }
        }))

        compositeDisposable.add(Observable.fromCallable(new Callable(context2) {
            public final *//* synthetic *//* Context f$1;

            {
                this.f$1 = r2;
            }

            public final Object call() {
                return ImageCompressionAsyncTask.this.lambda$new$0$ImageCompressionAsyncTask(this.f$1);
            }
        }).subscribeOn(Schedulers.m268io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
            public final void accept(Object obj) {
                ImageCompressionAsyncTask.lambda$new$1(ImageListener.this, (String) obj);
            }
        }));
    }
*/
    public /* synthetic */ String lambda$new$0$ImageCompressionAsyncTask(Context context2) throws Exception {
        try {
            return getBitmapFormUri(context2, Uri.parse(this.initialFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    static /* synthetic */ void lambda$new$1(ImageListener imageListener, String str) throws Exception {
        try {
            imageListener.onImageCopy(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0042  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String getBitmapFormUri(Context r9, Uri r10) throws java.io.FileNotFoundException, java.io.IOException {
        /*
            r8 = this;
            android.content.ContentResolver r0 = r9.getContentResolver()
            java.io.InputStream r0 = r0.openInputStream(r10)
            android.graphics.BitmapFactory$Options r1 = new android.graphics.BitmapFactory$Options
            r1.<init>()
            r2 = 1
            r1.inJustDecodeBounds = r2
            r1.inDither = r2
            android.graphics.Bitmap$Config r3 = android.graphics.Bitmap.Config.ARGB_8888
            r1.inPreferredConfig = r3
            r3 = 0
            android.graphics.BitmapFactory.decodeStream(r0, r3, r1)
            r0.close()
            int r0 = r1.outWidth
            int r1 = r1.outHeight
            r4 = -1
            if (r0 == r4) goto L_0x0077
            if (r1 != r4) goto L_0x0027
            goto L_0x0077
        L_0x0027:
            r4 = 1145831424(0x444c0000, float:816.0)
            r5 = 1142489088(0x44190000, float:612.0)
            if (r0 <= r1) goto L_0x0035
            float r6 = (float) r0
            int r7 = (r6 > r5 ? 1 : (r6 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x0035
            float r6 = r6 / r5
            int r0 = (int) r6
            goto L_0x0040
        L_0x0035:
            if (r0 >= r1) goto L_0x003f
            float r0 = (float) r1
            int r1 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r1 <= 0) goto L_0x003f
            float r0 = r0 / r4
            int r0 = (int) r0
            goto L_0x0040
        L_0x003f:
            r0 = 1
        L_0x0040:
            if (r0 > 0) goto L_0x0043
            r0 = 1
        L_0x0043:
            android.graphics.BitmapFactory$Options r1 = new android.graphics.BitmapFactory$Options
            r1.<init>()
            r1.inSampleSize = r0
            r1.inDither = r2
            android.graphics.Bitmap$Config r0 = android.graphics.Bitmap.Config.ARGB_8888
            r1.inPreferredConfig = r0
            android.content.ContentResolver r9 = r9.getContentResolver()
            java.io.InputStream r9 = r9.openInputStream(r10)
            android.graphics.Bitmap r10 = android.graphics.BitmapFactory.decodeStream(r9, r3, r1)
            r9.close()
            java.lang.String r9 = r8.getFilename()
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0072 }
            java.lang.String r1 = r8.initialFilePath     // Catch:{ FileNotFoundException -> 0x0072 }
            r0.<init>(r1)     // Catch:{ FileNotFoundException -> 0x0072 }
            android.graphics.Bitmap$CompressFormat r1 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ FileNotFoundException -> 0x0072 }
            r2 = 90
            r10.compress(r1, r2, r0)     // Catch:{ FileNotFoundException -> 0x0072 }
            goto L_0x0076
        L_0x0072:
            r10 = move-exception
            r10.printStackTrace()
        L_0x0076:
            return r9
        L_0x0077:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fittech.bizcardscanner.util.ImageCompressionAsyncTask.getBitmapFormUri(android.content.Context, android.net.Uri):java.lang.String");
    }

    public static Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        int i = 100;
        while (byteArrayOutputStream.toByteArray().length / 1024 > 100) {
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, i, byteArrayOutputStream);
            i -= 10;
        }
        return BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), (Rect) null, (BitmapFactory.Options) null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00fd A[Catch:{ IOException -> 0x015f }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0115 A[Catch:{ IOException -> 0x015f }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public String compressImage(String r21) {
        /*
            r20 = this;
            r1 = r20
            java.lang.String r2 = "Exif: "
            java.lang.String r3 = "EXIF"
            java.lang.String r4 = r20.getRealPathFromURI(r21)
            android.graphics.BitmapFactory$Options r5 = new android.graphics.BitmapFactory$Options
            r5.<init>()
            r0 = 1
            r5.inJustDecodeBounds = r0
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "compressImage: "
            r6.append(r7)
            r7 = r21
            r6.append(r7)
            java.lang.String r7 = "----"
            r6.append(r7)
            r6.append(r4)
            java.lang.String r6 = r6.toString()
            java.lang.String r7 = "compressImage"
            android.util.Log.i(r7, r6)
            android.graphics.Bitmap r6 = android.graphics.BitmapFactory.decodeFile(r4, r5)
            int r7 = r5.outHeight
            int r8 = r5.outWidth
            float r9 = (float) r7
            r10 = 1145831424(0x444c0000, float:816.0)
            r11 = 1142489088(0x44190000, float:612.0)
            int r12 = (r9 > r10 ? 1 : (r9 == r10 ? 0 : -1))
            if (r12 > 0) goto L_0x004c
            float r12 = (float) r8
            int r12 = (r12 > r11 ? 1 : (r12 == r11 ? 0 : -1))
            if (r12 <= 0) goto L_0x0049
            goto L_0x004c
        L_0x0049:
            int r7 = (int) r10
        L_0x004a:
            int r8 = (int) r11
            goto L_0x006d
        L_0x004c:
            int r7 = r8 / r7
            float r7 = (float) r7
            r12 = 1061158912(0x3f400000, float:0.75)
            int r13 = (r7 > r12 ? 1 : (r7 == r12 ? 0 : -1))
            if (r13 >= 0) goto L_0x0062
            float r7 = r10 / r9
            float r8 = (float) r8
            float r7 = r7 * r8
            int r7 = (int) r7
            int r8 = (int) r10
            r19 = r8
            r8 = r7
            r7 = r19
            goto L_0x006d
        L_0x0062:
            int r7 = (r7 > r12 ? 1 : (r7 == r12 ? 0 : -1))
            if (r7 <= 0) goto L_0x0049
            float r7 = (float) r8
            float r7 = r11 / r7
            float r7 = r7 * r9
            int r7 = (int) r7
            goto L_0x004a
        L_0x006d:
            int r9 = r1.calculateInSampleSize(r5, r8, r7)
            r5.inSampleSize = r9
            r9 = 0
            r5.inJustDecodeBounds = r9
            r5.inDither = r9
            r5.inPurgeable = r0
            r5.inInputShareable = r0
            r0 = 16384(0x4000, float:2.2959E-41)
            byte[] r0 = new byte[r0]
            r5.inTempStorage = r0
            android.graphics.Bitmap r6 = android.graphics.BitmapFactory.decodeFile(r4, r5)     // Catch:{ OutOfMemoryError -> 0x0087 }
            goto L_0x008c
        L_0x0087:
            r0 = move-exception
            r10 = r0
            r10.printStackTrace()
        L_0x008c:
            if (r8 <= 0) goto L_0x009b
            if (r7 <= 0) goto L_0x009b
            android.graphics.Bitmap$Config r0 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ OutOfMemoryError -> 0x0097 }
            android.graphics.Bitmap r0 = android.graphics.Bitmap.createBitmap(r8, r7, r0)     // Catch:{ OutOfMemoryError -> 0x0097 }
            goto L_0x009c
        L_0x0097:
            r0 = move-exception
            r0.printStackTrace()
        L_0x009b:
            r0 = 0
        L_0x009c:
            float r8 = (float) r8
            int r10 = r5.outWidth
            float r10 = (float) r10
            float r10 = r8 / r10
            float r7 = (float) r7
            int r5 = r5.outHeight
            float r5 = (float) r5
            float r5 = r7 / r5
            r11 = 1073741824(0x40000000, float:2.0)
            float r8 = r8 / r11
            float r7 = r7 / r11
            android.graphics.Matrix r11 = new android.graphics.Matrix
            r11.<init>()
            r11.setScale(r10, r5, r8, r7)
            if (r0 != 0) goto L_0x00b8
            r5 = r6
            goto L_0x00b9
        L_0x00b8:
            r5 = r0
        L_0x00b9:
            android.graphics.Canvas r0 = new android.graphics.Canvas
            r0.<init>(r5)
            r0.setMatrix(r11)
            int r10 = r6.getWidth()
            r11 = 2
            int r10 = r10 / r11
            float r10 = (float) r10
            float r8 = r8 - r10
            int r10 = r6.getHeight()
            int r10 = r10 / r11
            float r10 = (float) r10
            float r7 = r7 - r10
            android.graphics.Paint r10 = new android.graphics.Paint
            r10.<init>(r11)
            r0.drawBitmap(r6, r8, r7, r10)
            android.media.ExifInterface r0 = new android.media.ExifInterface     // Catch:{ IOException -> 0x015f }
            r0.<init>(r4)     // Catch:{ IOException -> 0x015f }
            java.lang.String r4 = "Orientation"
            int r0 = r0.getAttributeInt(r4, r9)     // Catch:{ IOException -> 0x015f }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x015f }
            r4.<init>()     // Catch:{ IOException -> 0x015f }
            r4.append(r2)     // Catch:{ IOException -> 0x015f }
            r4.append(r0)     // Catch:{ IOException -> 0x015f }
            java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x015f }
            android.util.Log.d(r3, r4)     // Catch:{ IOException -> 0x015f }
            android.graphics.Matrix r4 = new android.graphics.Matrix     // Catch:{ IOException -> 0x015f }
            r4.<init>()     // Catch:{ IOException -> 0x015f }
            r6 = 6
            if (r0 != r6) goto L_0x0115
            r6 = 1119092736(0x42b40000, float:90.0)
            r4.postRotate(r6)     // Catch:{ IOException -> 0x015f }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x015f }
            r6.<init>()     // Catch:{ IOException -> 0x015f }
            r6.append(r2)     // Catch:{ IOException -> 0x015f }
            r6.append(r0)     // Catch:{ IOException -> 0x015f }
            java.lang.String r0 = r6.toString()     // Catch:{ IOException -> 0x015f }
            android.util.Log.d(r3, r0)     // Catch:{ IOException -> 0x015f }
            goto L_0x014b
        L_0x0115:
            r6 = 3
            if (r0 != r6) goto L_0x0130
            r6 = 1127481344(0x43340000, float:180.0)
            r4.postRotate(r6)     // Catch:{ IOException -> 0x015f }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x015f }
            r6.<init>()     // Catch:{ IOException -> 0x015f }
            r6.append(r2)     // Catch:{ IOException -> 0x015f }
            r6.append(r0)     // Catch:{ IOException -> 0x015f }
            java.lang.String r0 = r6.toString()     // Catch:{ IOException -> 0x015f }
            android.util.Log.d(r3, r0)     // Catch:{ IOException -> 0x015f }
            goto L_0x014b
        L_0x0130:
            r6 = 8
            if (r0 != r6) goto L_0x014b
            r6 = 1132920832(0x43870000, float:270.0)
            r4.postRotate(r6)     // Catch:{ IOException -> 0x015f }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x015f }
            r6.<init>()     // Catch:{ IOException -> 0x015f }
            r6.append(r2)     // Catch:{ IOException -> 0x015f }
            r6.append(r0)     // Catch:{ IOException -> 0x015f }
            java.lang.String r0 = r6.toString()     // Catch:{ IOException -> 0x015f }
            android.util.Log.d(r3, r0)     // Catch:{ IOException -> 0x015f }
        L_0x014b:
            r13 = 0
            r14 = 0
            int r15 = r5.getWidth()     // Catch:{ IOException -> 0x015f }
            int r16 = r5.getHeight()     // Catch:{ IOException -> 0x015f }
            r18 = 1
            r12 = r5
            r17 = r4
            android.graphics.Bitmap r5 = android.graphics.Bitmap.createBitmap(r12, r13, r14, r15, r16, r17, r18)     // Catch:{ IOException -> 0x015f }
            goto L_0x0163
        L_0x015f:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0163:
            java.lang.String r2 = r20.getFilename()
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0176 }
            java.lang.String r3 = r1.initialFilePath     // Catch:{ FileNotFoundException -> 0x0176 }
            r0.<init>(r3)     // Catch:{ FileNotFoundException -> 0x0176 }
            android.graphics.Bitmap$CompressFormat r3 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ FileNotFoundException -> 0x0176 }
            r4 = 80
            r5.compress(r3, r4, r0)     // Catch:{ FileNotFoundException -> 0x0176 }
            goto L_0x017a
        L_0x0176:
            r0 = move-exception
            r0.printStackTrace()
        L_0x017a:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fittech.bizcardscanner.util.ImageCompressionAsyncTask.compressImage(java.lang.String):java.lang.String");
    }

    private String getRealPathFromURI(String str) {
        Uri parse = Uri.parse(str);
        Cursor query = this.context.getContentResolver().query(parse, (String[]) null, (String) null, (String[]) null, (String) null);
        if (query == null) {
            return parse.getPath();
        }
        query.moveToFirst();
        return query.getString(query.getColumnIndex("_data"));
    }

    public String getFilename() {
        return System.currentTimeMillis() + ".jpg";
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3;
        int i4 = options.outHeight;
        int i5 = options.outWidth;
        if (i4 > i2 || i5 > i) {
            i3 = Math.round(((float) i4) / ((float) i2));
            int round = Math.round(((float) i5) / ((float) i));
            if (i3 >= round) {
                i3 = round;
            }
        } else {
            i3 = 1;
        }
        while (((float) (i5 * i4)) / ((float) (i3 * i3)) > ((float) (i * i2 * 2))) {
            i3++;
        }
        return i3;
    }
}
