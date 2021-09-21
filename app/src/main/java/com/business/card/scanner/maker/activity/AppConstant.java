package com.business.card.scanner.maker.activity;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.business.card.scanner.maker.App;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.database.Database;

import net.lingala.zip4j.util.InternalZipConstants;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class AppConstant {
    public static final int CLICK_TYPE_BTN = 2;
    public static final int CLICK_TYPE_ROW = 1;
    public static final String DB_BACKUP_DIRECTORY = "BusinessCardbackup";
    public static final String DB_BACKUP_FILE_NAME_PRE = "Backup";
    public static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    public static final int RC_STORAGE = 1566;
    public static final int REQUEST_CODE_DETAIL = 1002;

    public static String getRootPath(Context context) {
        return new File(context.getDatabasePath(Database.DB_NAME).getParent()).getParent();
    }

    public static File getTemp(Context context) {
        File file = new File(context.getFilesDir(), "temp");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getCameraImage(Context context) {
        File file = new File(getDatabsePath(context), context.getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static String getRemoteZipFilePath(Context context) {
        return getTempDirectory(context) + File.separator + getBackupName();
    }

    public static String getBackupName() {
        return "Backup_" + getFileNameCurrentDateTime() + ".zip";
    }

    public static String getFileNameCurrentDateTime() {
        return new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss").format(new Date(Calendar.getInstance().getTimeInMillis()));
    }

    public static String getDatabsePath(Context context) {
        return new File(context.getDatabasePath(Database.DB_NAME).getParent()).getAbsolutePath();
    }

    public static void DeleteTempFile(Context context) {
        File[] listFiles = new File(String.valueOf(getTemp(context))).listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File delete : listFiles) {
                delete.delete();
            }
        }
    }

    public static String getTempDirectory(Context context) {
        File file = new File(context.getFilesDir(), "temp");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static void deleteTempFile() {
        try {
            deleteFolder(new File(getTempDirectory(App.getContext())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteImageDirectory() {
        try {
            deleteFolder(getCameraImage(App.getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteTempFile(Context context) {
        try {
            deleteFolder(new File(getRootPath(context) + "/temp"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFolder(File file) {
        for (File file2 : file.listFiles()) {
            if (file2.isDirectory()) {
                deleteFolder(file2);
            } else {
                file2.delete();
            }
        }
        file.delete();
    }

    public static String getLocalZipFilePath() {
        return getLocalFileDir() + File.separator + getBackupName();
    }

    public static String getLocalFileDir() {
        File file = new File(Environment.getExternalStorageDirectory() + InternalZipConstants.ZIP_FILE_SEPARATOR + DB_BACKUP_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static String getFormattedDate(long j, DateFormat dateFormat) {
        return dateFormat.format(new Date(j));
    }

    public static String getFormattedDate(long j, String str) {
        return new SimpleDateFormat(str).format(new Date(j));
    }

    public static void toastShort(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
