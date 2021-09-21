package com.business.card.scanner.maker.backupRestore;

import android.app.Activity;
import android.os.AsyncTask;


import com.business.card.scanner.maker.activity.AppConstant;
import com.business.card.scanner.maker.R;


import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ZipUnZipAsync extends AsyncTask<Object, Object, Boolean> {
    Activity activity;
    BackupRestoreProgress dialog;
    ArrayList<File> fileListForZip;
    String fileToRestore;
    boolean isMerge;
    boolean isZip;
    OnBackupRestore onBackupRestore;
    //    String pass = strPassword();
    String tempZipFilePath;
    WeakReference<Activity> weakReference;

//    private static native String strPassword();

//    static {
//        System.loadLibrary("native-lib");
//    }

    public ZipUnZipAsync(BackupRestoreProgress backupRestoreProgress, Activity activity2, boolean z, ArrayList<File> arrayList, String str, String str2, OnBackupRestore onBackupRestore2, boolean z2) {
        this.weakReference = new WeakReference<>(activity2);
        this.activity = activity2;
        this.isZip = z;
        this.dialog = backupRestoreProgress;
        this.tempZipFilePath = str2;
        this.fileToRestore = str;
        this.fileListForZip = arrayList;
        this.onBackupRestore = onBackupRestore2;
        this.isMerge = z2;
    }


    public void onPreExecute() {
        super.onPreExecute();
//        if(isZip){
        this.dialog.setMessage(this.isZip ? "Creating Zip..." : "Extracting Zip...");
        this.dialog.showDialog();
//        }
    }


    public Boolean doInBackground(Object... objArr) {
        if (this.isZip) {
            return Boolean.valueOf(encryptedZip(this.fileListForZip, this.tempZipFilePath));
        }
        return Boolean.valueOf(decryptedZip(this.fileToRestore));
    }


    public void onPostExecute(Boolean bool) {
        super.onPostExecute(bool);
        this.dialog.dismissDialog();
        this.onBackupRestore.onSuccess(bool.booleanValue());
//        if(isZip){
//            this.dialog.dismissDialog();
//        }
//        this.onBackupRestore.onSuccess(bool.booleanValue());
    }

    public boolean encryptedZip(ArrayList<File> arrayList, String str) {
        try {
            ZipFile zipFile = new ZipFile(str);
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
            zipParameters.setCompressionLevel(CompressionLevel.NORMAL);
            zipParameters.setEncryptFiles(false);
//            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
//            zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
//            zipParameters.setPassword(this.pass);
            for (int i = 0; i < arrayList.size(); i++) {
                if (!arrayList.get(i).isDirectory()) {
                    zipFile.addFile(arrayList.get(i), zipParameters);
                } else if (arrayList.get(i).listFiles().length > 0) {
                    zipFile.addFolder(arrayList.get(i), zipParameters);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean decryptedZip(String str) {
        AppConstant.getRootPath(this.activity);
        try {
            ZipFile zipFile = new ZipFile(str);
            if (zipFile.isEncrypted()) {
                //  zipFile.setPassword(this.pass);
            }
            zipFile.extractAll(AppConstant.getTempDirectory(this.activity));
            File file = new File(AppConstant.getTempDirectory(this.activity) + File.separator + this.activity.getString(R.string.app_name));
            if (!this.isMerge) {
                AppConstant.deleteImageDirectory();
            }
            if (file.exists()) {
                FileUtils.copyDirectoryToDirectory(file, new File(AppConstant.getDatabsePath(this.activity)));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean dirChecker(String str) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.exists();
    }
}
