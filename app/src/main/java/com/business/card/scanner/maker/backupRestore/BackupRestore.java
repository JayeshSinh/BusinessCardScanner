package com.business.card.scanner.maker.backupRestore;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.sqlite.db.SupportSQLiteDatabase;


import com.business.card.scanner.maker.activity.AppConstant;
import com.business.card.scanner.maker.database.Database;
import com.business.card.scanner.maker.util.Constants;
import com.business.card.scanner.maker.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BackupRestore {
    String METADATA_FILE_PARENT = "appDataFolder";
    String METADATA_FILE_TYPE = "application/zip";

    public Activity activity;

    /* renamed from: dg */
    BackupRestoreProgress f171dg;

    public Drive driveService;
    Drive.Files.Create file;
    FileList fileList = null;
    File fileMetadata;
    java.io.File filePath;
    boolean isSuccessCreate = true;
    boolean isSuccessDelete = true;
    FileContent mediaContent;
    OutputStream outputStream = null;
    Drive.Files.Get request;

    public BackupRestore(Activity activity2) {
        this.activity = activity2;
    }

    public void backupRestore(BackupRestoreProgress backupRestoreProgress, boolean z, boolean z2, String str, boolean z3, OnBackupRestore onBackupRestore) {
        if (z) {
            localBackUpAndRestore(backupRestoreProgress, z2, str, z3, onBackupRestore);
        } else {
            driveBackupRestore(backupRestoreProgress, z2, str, z3, onBackupRestore);
        }
    }

    private void localBackUpAndRestore(BackupRestoreProgress backupRestoreProgress, boolean z, String str, boolean z2, OnBackupRestore onBackupRestore) {
        backupRestoreProgress.showDialog();
        AppConstant.deleteTempFile(this.activity);
        if (z) {
            startLocalBackUp(backupRestoreProgress, onBackupRestore, z2);
        } else {
            startLocalRestore(backupRestoreProgress, str, z2, onBackupRestore);
        }
    }

    private void startLocalBackUp(BackupRestoreProgress backupRestoreProgress, OnBackupRestore onBackupRestore, boolean z) {
        String localZipFilePath = AppConstant.getLocalZipFilePath();
        try {
            Activity activity2 = this.activity;
            new ZipUnZipAsync(backupRestoreProgress, activity2, true, getAllFilesForBackup(AppConstant.getRootPath(activity2)), "", localZipFilePath, onBackupRestore, z).execute(new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<java.io.File> getAllFilesForBackup(String str) {
        ArrayList<java.io.File> arrayList = new ArrayList<>();
        java.io.File[] listFiles = new java.io.File(AppConstant.getDatabsePath(this.activity)).listFiles();
        if (listFiles != null && listFiles.length > 0) {
            arrayList.addAll(Arrays.asList(listFiles));
        }
        return arrayList;
    }


    public void startLocalRestore(final BackupRestoreProgress backupRestoreProgress, String str, final boolean z, final OnBackupRestore onBackupRestore) {
        new ZipUnZipAsync(backupRestoreProgress, this.activity, false, (ArrayList<java.io.File>) null, str, "", new OnBackupRestore() {
            public void onSuccess(boolean z) {
                BackupRestore.this.localRestoreAsync(backupRestoreProgress, z, onBackupRestore, z);
            }

            public void getList(ArrayList<RestoreRowModel> arrayList) {
                onBackupRestore.getList(arrayList);
            }
        }, z).execute(new Object[0]);
    }


    public void localRestoreAsync(BackupRestoreProgress backupRestoreProgress, boolean z, OnBackupRestore onBackupRestore, boolean z2) {
        final BackupRestoreProgress backupRestoreProgress2 = backupRestoreProgress;
        final boolean z3 = z;
        final OnBackupRestore onBackupRestore2 = onBackupRestore;
        final boolean z4 = z2;
        new AsyncTask<Void, Void, Void>() {

            public void onPreExecute() {
                backupRestoreProgress2.setMessage(BackupRestore.this.activity.getString(R.string.importing_data));
                backupRestoreProgress2.showDialog();
                super.onPreExecute();
            }


            public Void doInBackground(Void... voidArr) {
                BackupRestore.this.localRestore(z3);
                return null;
            }


            public void onPostExecute(Void voidR) {
                backupRestoreProgress2.dismissDialog();
                onBackupRestore2.onSuccess(z4);
                super.onPostExecute(voidR);
            }
        }.execute(new Void[0]);
    }


    public void localRestore(boolean z) {
        SupportSQLiteDatabase writableDatabase = Database.getAppDatabase(this.activity).getOpenHelper().getWritableDatabase();
        if (!z) {
            deleteAllTableData(writableDatabase);
        }
        writableDatabase.execSQL(String.format("ATTACH DATABASE '%s' AS encrypted;", new Object[]{AppConstant.getTempDirectory(this.activity) + java.io.File.separator + Database.DB_NAME}));
        replaceAllTableData(z, writableDatabase);
        writableDatabase.execSQL("DETACH DATABASE encrypted");
    }

    public static void deleteAllTableData(SupportSQLiteDatabase supportSQLiteDatabase) {
        supportSQLiteDatabase.execSQL("DELETE FROM Business_Card");
        supportSQLiteDatabase.execSQL("DELETE FROM Group_tb");
    }

    private void replaceAllTableData(boolean z, SupportSQLiteDatabase supportSQLiteDatabase) {
        replaceAll(supportSQLiteDatabase, z, "Business_Card", "id");
        replaceAll(supportSQLiteDatabase, z, "Group_tb", "group_id");
    }

    private void replaceAll(SupportSQLiteDatabase supportSQLiteDatabase, boolean z, String str, String str2) {
        if (z) {
            try {
                if (str.equalsIgnoreCase("SetsImages")) {
                    supportSQLiteDatabase.execSQL("insert into " + str + " select b.* from encrypted." + str + " b left join " + str + " c on c.setdetailid=b.setdetailid and c.Imagename=b.Imagename \n where c.setdetailid is null and c.Imagename is null ");
                    return;
                }
                supportSQLiteDatabase.execSQL("insert into " + str + " select b.* from encrypted." + str + " b left join " + str + " c on c." + str2 + "=b." + str2 + " where c." + str2 + " is null ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                supportSQLiteDatabase.execSQL("insert into " + str + " select * from encrypted." + str);
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void driveBackupRestore(BackupRestoreProgress backupRestoreProgress, boolean z, String str, boolean z2, OnBackupRestore onBackupRestore) {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this.activity);
        if (lastSignedInAccount == null) {
            signIn();
            return;
        }
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this.activity), new Scope("https://www.googleapis.com/auth/drive.appdata"))) {
            GoogleSignIn.requestPermissions(this.activity, Constants.REQUEST_CODE_SIGN_IN, GoogleSignIn.getLastSignedInAccount(this.activity), new Scope("https://www.googleapis.com/auth/drive.appdata"));
            return;
        }
        setCredentials(lastSignedInAccount);
//        startDriveOperation(z, Environment.getExternalStorageDirectory() + java.io.File.separator +  Environment.DIRECTORY_DCIM + java.io.File.separator + AppConstant.getBackupName(), z2, backupRestoreProgress, onBackupRestore);
        startDriveOperation(z, str, z2, backupRestoreProgress, onBackupRestore);
    }

    private void signIn() {
        this.activity.startActivityForResult(buildGoogleSignInClient().getSignInIntent(), Constants.REQUEST_CODE_SIGN_IN);
    }

    public void driveBackupList(BackupRestoreProgress backupRestoreProgress, OnBackupRestore onBackupRestore) {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this.activity);
        if (lastSignedInAccount == null) {
            signIn();
            return;
        }
        setCredentials(lastSignedInAccount);
        listFilesFromAppFolder(backupRestoreProgress, onBackupRestore);
    }

    private void listFilesFromAppFolder(final BackupRestoreProgress backupRestoreProgress, final OnBackupRestore onBackupRestore) {
        new AsyncTask<Void, Void, Void>() {

            public void onPreExecute() {
                backupRestoreProgress.setMessage("Fetching backups...");
                backupRestoreProgress.showDialog();
                super.onPreExecute();
            }


            public Void doInBackground(Void... voidArr) {
                try {
                    BackupRestore backupRestore = BackupRestore.this;
                    Drive.Files.List list = backupRestore.driveService.files().list();
                    backupRestore.fileList = (FileList) list.setQ("mimeType ='" + BackupRestore.this.METADATA_FILE_TYPE + "'").setSpaces(BackupRestore.this.METADATA_FILE_PARENT).setFields("files(id, name,size,createdTime,modifiedTime)").execute();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }


            public void onPostExecute(Void voidR) {
                backupRestoreProgress.dismissDialog();
                onBackupRestore.onSuccess(true);
                onBackupRestore.getList(BackupRestore.this.getBackupList());
                super.onPostExecute(voidR);
            }
        }.execute(new Void[0]);
    }


    public ArrayList<RestoreRowModel> getBackupList() {
        ArrayList<RestoreRowModel> arrayList = new ArrayList<>();
        if (this.fileList != null) {
            for (int i = 0; i < this.fileList.getFiles().size(); i++) {
                File file2 = this.fileList.getFiles().get(i);
                String name = file2.getName();
                String id = file2.getId();
                String formattedDate = AppConstant.getFormattedDate(file2.getModifiedTime().getValue(), (DateFormat) AppConstant.FILE_DATE_FORMAT);
                arrayList.add(new RestoreRowModel(name, id, formattedDate, (file2.getSize().longValue() / 1024) + "KB", file2.getModifiedTime().getValue()));
            }
        }
        return arrayList;
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        return GoogleSignIn.getClient(this.activity, new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope("https://www.googleapis.com/auth/drive.appdata"), new Scope[0])
//                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestEmail()
                .requestProfile()
                .build());
    }


    public void startDriveOperation(boolean z, String str, boolean z2, BackupRestoreProgress backupRestoreProgress, OnBackupRestore onBackupRestore) {
        AppConstant.deleteTempFile(this.activity);
        if (z) {
            startDriveBackup(str, backupRestoreProgress, onBackupRestore, z2);
            return;
        }
        downloadRestore(backupRestoreProgress, str, AppConstant.getRemoteZipFilePath(this.activity), z2, onBackupRestore);
    }

    public void handleSignInResult(Intent intent, boolean z, boolean z2, String str, BackupRestoreProgress backupRestoreProgress, OnBackupRestore onBackupRestore) {
        try {
            final boolean z3 = z2;
            final boolean z4 = z;
            final String str2 = str;
            final BackupRestoreProgress backupRestoreProgress2 = backupRestoreProgress;
            final OnBackupRestore onBackupRestore2 = onBackupRestore;
            GoogleSignIn.getSignedInAccountFromIntent(intent).addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                    Log.e("handleSignInResult", "Signed in as " + googleSignInAccount.getEmail());
                    BackupRestore.this.setCredentials(googleSignInAccount);
                    if (!z3) {
                        BackupRestore.this.startDriveOperation(z4, str2, false, backupRestoreProgress2, onBackupRestore2);
                    } else {
                        BackupRestore.this.driveBackupList(backupRestoreProgress2, onBackupRestore2);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(Exception exc) {
                    Log.e("handleSignInResult", exc.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e( "handleSignInResult: ", e.getMessage());
            e.printStackTrace();
        }
    }


    public void setCredentials(GoogleSignInAccount googleSignInAccount) {

        GoogleAccountCredential usingOAuth2 = GoogleAccountCredential.usingOAuth2(this.activity, Collections.singleton("https://www.googleapis.com/auth/drive.appdata"));
        usingOAuth2.setSelectedAccount(googleSignInAccount.getAccount());
        this.driveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), usingOAuth2).setApplicationName(this.activity.getString(R.string.app_name)).build();
    }

    private void startDriveBackup(String str, final BackupRestoreProgress backupRestoreProgress, final OnBackupRestore onBackupRestore, boolean z) {
        backupRestoreProgress.showDialog();
        if (str == null) {
            final String remoteZipFilePath = AppConstant.getRemoteZipFilePath(this.activity);
            Activity activity2 = this.activity;
            new ZipUnZipAsync(backupRestoreProgress, activity2, true, getAllFilesForBackup(AppConstant.getRootPath(activity2)), "", remoteZipFilePath, new OnBackupRestore() {
                public void getList(ArrayList<RestoreRowModel> arrayList) {
                }

                public void onSuccess(boolean z) {
                    BackupRestore backupRestore = BackupRestore.this;
                    backupRestore.createFileInAppFolder(backupRestoreProgress, remoteZipFilePath, backupRestore.driveService, onBackupRestore);
                }
            }, z).execute(new Object[0]);
            return;
        }
        createFileInAppFolder(backupRestoreProgress, str, this.driveService, onBackupRestore);
    }


    public void createFileInAppFolder(BackupRestoreProgress backupRestoreProgress, String str, Drive drive, OnBackupRestore onBackupRestore) {
        final BackupRestoreProgress backupRestoreProgress2 = backupRestoreProgress;
        final String str2 = str;
        final Drive drive2 = drive;
        final OnBackupRestore onBackupRestore2 = onBackupRestore;
        new AsyncTask<Void, Void, Void>() {

            public void onPreExecute() {
                BackupRestore.this.f171dg = backupRestoreProgress2;
                backupRestoreProgress2.setMessage("Uploading to drive...");
                backupRestoreProgress2.showDialog();
                BackupRestore.this.fileMetadata = new File();
                BackupRestore.this.fileMetadata.setName(AppConstant.getBackupName());
                BackupRestore.this.fileMetadata.setParents(Collections.singletonList(BackupRestore.this.METADATA_FILE_PARENT));
                BackupRestore.this.filePath = new java.io.File(str2);
                BackupRestore backupRestore = BackupRestore.this;
                backupRestore.mediaContent = new FileContent(backupRestore.METADATA_FILE_TYPE, BackupRestore.this.filePath);
                super.onPreExecute();
            }


            public Void doInBackground(Void... voidArr) {
                try {
                    BackupRestore.this.file = drive2.files().create(BackupRestore.this.fileMetadata, BackupRestore.this.mediaContent);
                    MediaHttpUploader mediaHttpUploader = BackupRestore.this.file.getMediaHttpUploader();
                    mediaHttpUploader.setDirectUploadEnabled(false);
                    mediaHttpUploader.setProgressListener(new FileUploadProgressListener());
                    BackupRestore.this.file.execute();
                    return null;
                } catch (IOException e) {
                    BackupRestore.this.isSuccessCreate = false;
                    e.printStackTrace();
                    return null;
                }
            }


            public void onPostExecute(Void voidR) {
                backupRestoreProgress2.dismissDialog();
                onBackupRestore2.onSuccess(BackupRestore.this.isSuccessCreate);
                super.onPostExecute(voidR);
            }
        }.execute(new Void[0]);
    }

    public class FileUploadProgressListener implements MediaHttpUploaderProgressListener {
        public FileUploadProgressListener() {
        }

        public void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException {
            if (mediaHttpUploader != null) {
                int i = C090110.f173xc191db1b[mediaHttpUploader.getUploadState().ordinal()];
                if (i == 1) {
                    final double progress = mediaHttpUploader.getProgress() * 100.0d;
                    BackupRestore.this.activity.runOnUiThread(new Runnable() {
                        public void run() {
                            BackupRestoreProgress backupRestoreProgress = BackupRestore.this.f171dg;
                            backupRestoreProgress.setPercent(((int) progress) + "%");
                        }
                    });
                    Log.d("progressChanged", "progressChanged: " + progress);
                } else if (i == 2) {
                    final double progress2 = mediaHttpUploader.getProgress() * 100.0d;
                    BackupRestore.this.activity.runOnUiThread(new Runnable() {
                        public void run() {
                            BackupRestoreProgress backupRestoreProgress = BackupRestore.this.f171dg;
                            backupRestoreProgress.setPercent(((int) progress2) + "%");
                        }
                    });
                } else if (i == 3) {
                    final double progress3 = mediaHttpUploader.getProgress() * 100.0d;
                    BackupRestore.this.activity.runOnUiThread(new Runnable() {
                        public void run() {
                            BackupRestoreProgress backupRestoreProgress = BackupRestore.this.f171dg;
                            backupRestoreProgress.setPercent(((int) progress3) + "%");
                        }
                    });
                    Log.d("progressChanged", "progressChanged: " + progress3);
                } else if (i == 4) {
                    final double progress4 = mediaHttpUploader.getProgress() * 100.0d;
                    BackupRestore.this.activity.runOnUiThread(new Runnable() {
                        public void run() {
                            BackupRestoreProgress backupRestoreProgress = BackupRestore.this.f171dg;
                            backupRestoreProgress.setPercent(((int) progress4) + "%");
                        }
                    });
                    Log.d("progressChanged", "progressChanged: " + progress4);
                }
            }
        }
    }

    private void downloadRestore(BackupRestoreProgress backupRestoreProgress, String str, String str2, boolean z, OnBackupRestore onBackupRestore) {
        final String str3 = str2;
        final boolean z2 = z;
        final OnBackupRestore onBackupRestore2 = onBackupRestore;
        new AsyncTask<Void, Void, Void>() {

            public void onPreExecute() {
//                backupRestoreProgress2.showDialog();
                super.onPreExecute();
            }


            public Void doInBackground(Void... voidArr) {
                try {
                    BackupRestore.this.outputStream = new FileOutputStream(str3);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    BackupRestore backupRestore = BackupRestore.this;
                    backupRestore.request = backupRestore.driveService.files().get(str);
                    BackupRestore.this.request.getMediaHttpDownloader().setProgressListener(new DownloadProgressListener()).setChunkSize(1000000);
                    BackupRestore.this.request.executeMediaAndDownloadTo(BackupRestore.this.outputStream);
                    return null;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return null;
                }
            }


            public void onProgressUpdate(Void... voidArr) {
                super.onProgressUpdate(voidArr);
            }


            public void onPostExecute(Void voidR) {
//                backupRestoreProgress2.dismissDialog();
                if (new java.io.File(str3).exists()) {
                    BackupRestore.this.startLocalRestore(backupRestoreProgress, str3, z2, onBackupRestore2);
                }
                super.onPostExecute(voidR);
            }
        }.execute(new Void[0]);
    }

    class DownloadProgressListener implements MediaHttpDownloaderProgressListener {
        DownloadProgressListener() {
        }

        public void progressChanged(MediaHttpDownloader mediaHttpDownloader) throws IOException {
            int i = C090110.f172x7384ce7b[mediaHttpDownloader.getDownloadState().ordinal()];
            if (i == 1) {
                final double progress = mediaHttpDownloader.getProgress() * 100.0d;
                BackupRestore.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        BackupRestoreProgress backupRestoreProgress = BackupRestore.this.f171dg;
                        backupRestoreProgress.setPercent(((int) progress) + "%");
                    }
                });
                Log.d("progressChanged", "progressChanged: " + progress);
            } else if (i == 2) {
                final double progress2 = mediaHttpDownloader.getProgress() * 100.0d;
                BackupRestore.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        BackupRestoreProgress backupRestoreProgress = BackupRestore.this.f171dg;
                        backupRestoreProgress.setPercent(((int) progress2) + "%");
                    }
                });
                Log.d("progressChanged", "progressChanged: " + progress2);
            }
        }
    }

    /* renamed from: com.fittech.bizcardscanner.backupRestore.BackupRestore$10 */
    static /* synthetic */ class C090110 {

        /* renamed from: $SwitchMap$com$google$api$client$googleapis$media$MediaHttpDownloader$DownloadState */
        static final /* synthetic */ int[] f172x7384ce7b;

        /* renamed from: $SwitchMap$com$google$api$client$googleapis$media$MediaHttpUploader$UploadState */
        static final /* synthetic */ int[] f173xc191db1b;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|(2:1|2)|3|(2:5|6)|7|9|10|11|12|13|14|15|16|18) */
        /* JADX WARNING: Can't wrap try/catch for region: R(15:0|(2:1|2)|3|5|6|7|9|10|11|12|13|14|15|16|18) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|5|6|7|9|10|11|12|13|14|15|16|18) */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x002e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0038 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0043 */
        static {

            MediaHttpDownloader.DownloadState[] r0 = MediaHttpDownloader.DownloadState.values();

            f172x7384ce7b = new int[r0.length];
//            r1 = 1
            MediaHttpDownloader.DownloadState r2 = MediaHttpDownloader.DownloadState.MEDIA_IN_PROGRESS;     // Catch:{ NoSuchFieldError -> 0x0012 }
            f172x7384ce7b[r2.ordinal()] = 1 ;
            f172x7384ce7b[r2.ordinal()] = 2 ;

            MediaHttpUploader.UploadState[] uploadStates = MediaHttpUploader.UploadState.values();
//            int[] r2 =
            f173xc191db1b = new int[uploadStates.length];
            MediaHttpUploader.UploadState uploadState = MediaHttpUploader.UploadState.INITIATION_STARTED     ;
            f173xc191db1b[uploadState.ordinal()] = 1;
            f173xc191db1b[uploadState.ordinal() ] = 2 ;    // Catch:{ NoSuchFieldError -> 0x0038 }
            f173xc191db1b[uploadState.ordinal()] = 3;
            f173xc191db1b[uploadState.ordinal()] = 4;
//            return;

            /*
                com.google.api.client.googleapis.media.MediaHttpDownloader$DownloadState[] r0 = com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f172x7384ce7b = r0
                r1 = 1
                com.google.api.client.googleapis.media.MediaHttpDownloader$DownloadState r2 = com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadState.MEDIA_IN_PROGRESS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = f172x7384ce7b     // Catch:{ NoSuchFieldError -> 0x001d }
                com.google.api.client.googleapis.media.MediaHttpDownloader$DownloadState r3 = com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadState.MEDIA_COMPLETE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                com.google.api.client.googleapis.media.MediaHttpUploader$UploadState[] r2 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.values()
                int r2 = r2.length
                int[] r2 = new int[r2]
                f173xc191db1b = r2
                com.google.api.client.googleapis.media.MediaHttpUploader$UploadState r3 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.INITIATION_STARTED     // Catch:{ NoSuchFieldError -> 0x002e }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x002e }
                r2[r3] = r1     // Catch:{ NoSuchFieldError -> 0x002e }
            L_0x002e:
                int[] r1 = f173xc191db1b     // Catch:{ NoSuchFieldError -> 0x0038 }
                com.google.api.client.googleapis.media.MediaHttpUploader$UploadState r2 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.INITIATION_COMPLETE     // Catch:{ NoSuchFieldError -> 0x0038 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0038 }
                r1[r2] = r0     // Catch:{ NoSuchFieldError -> 0x0038 }
            L_0x0038:
                int[] r0 = f173xc191db1b     // Catch:{ NoSuchFieldError -> 0x0043 }
                com.google.api.client.googleapis.media.MediaHttpUploader$UploadState r1 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS     // Catch:{ NoSuchFieldError -> 0x0043 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0043 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0043 }
            L_0x0043:
                int[] r0 = f173xc191db1b     // Catch:{ NoSuchFieldError -> 0x004e }
                com.google.api.client.googleapis.media.MediaHttpUploader$UploadState r1 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.MEDIA_COMPLETE     // Catch:{ NoSuchFieldError -> 0x004e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x004e }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x004e }
            L_0x004e:
                return
            */
//            throw new UnsupportedOperationException("Method not decompiled: com.fittech.bizcardscanner.backupRestore.BackupRestore.C090110.<clinit>():void");
        }
    }

    public void deleteFromDrive(final BackupRestoreProgress backupRestoreProgress, final String str, final OnBackupRestore onBackupRestore) {
        new AsyncTask<Void, Void, Void>() {

            public void onPreExecute() {
                backupRestoreProgress.setMessage("Deleting from Drive...");
                backupRestoreProgress.showDialog();
                super.onPreExecute();
            }


            public Void doInBackground(Void... voidArr) {
                try {
                    BackupRestore.this.driveService.files().delete(str).execute();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    BackupRestore.this.isSuccessDelete = false;
                    return null;
                }
            }


            public void onPostExecute(Void voidR) {
                backupRestoreProgress.dismissDialog();
                onBackupRestore.onSuccess(BackupRestore.this.isSuccessDelete);
                super.onPostExecute(voidR);
            }
        }.execute(new Void[0]);
    }
}
