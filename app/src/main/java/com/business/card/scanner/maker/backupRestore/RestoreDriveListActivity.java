package com.business.card.scanner.maker.backupRestore;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.business.card.scanner.maker.activity.AppConstant;
import com.business.card.scanner.maker.util.Constants;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.databinding.ActivityRestoreListBinding;
import com.business.card.scanner.maker.databinding.DialogConfRestoreBinding;
import com.business.card.scanner.maker.databinding.DialogDeleteBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RestoreDriveListActivity extends AppCompatActivity {
    private BackupRestore backupRestore;
    boolean backupScuccess = false;

    public ActivityRestoreListBinding binding;
    Context context;

    public boolean isDesc;

    public boolean isResultOK;
    MenuItem menuItemSort;

    public RestoreListModel model;
    private BackupRestoreProgress progressDialog;



    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(null);
        this.binding = (ActivityRestoreListBinding) DataBindingUtil.setContentView(this, R.layout.activity_restore_list);
        RestoreListModel restoreListModel = new RestoreListModel();
        this.model = restoreListModel;
        this.context = this;
        restoreListModel.setArrayList(new ArrayList());
        this.model.setNoDataIcon(R.drawable.no_data_icon);
        this.model.setNoDataText(getString(R.string.noDataTitleBackup));
        this.model.setNoDataDetail(getString(R.string.noDataDescBackup));
        this.binding.setViewModel(this.model);
        this.backupRestore = new BackupRestore(this);
        this.progressDialog = new BackupRestoreProgress(this);
        setToolbar();
        setRecycler();
        fillData();
    }

    public void setToolbar() {
        setSupportActionBar(this.binding.toolBar);
        this.binding.toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        this.binding.toolBar.setNavigationIcon((int) R.drawable.ic_baseline_arrow_back_24);
        this.binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RestoreDriveListActivity.this.onBackPressed();
            }
        });
        String string = getResources().getString(R.string.restore_drive_backups);
        new SpannableStringBuilder(string).setSpan(new StyleSpan(1), 0, string.length(), 33);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort, menu);
        this.menuItemSort = menu.findItem(R.id.sort);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.sort) {
            this.isDesc = !this.isDesc;
            shortList();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    public void fillData() {
        getDriveBackupList();
    }

    private void getDriveBackupList() {
        this.backupRestore.driveBackupList(this.progressDialog, new OnBackupRestore() {
            public void onSuccess(boolean z) {
            }

            public void getList(ArrayList<RestoreRowModel> arrayList) {
                RestoreDriveListActivity.this.model.getArrayList().addAll(arrayList);
                RestoreDriveListActivity.this.notifyAdapter();
            }
        });
    }

    private void shortList() {
        this.menuItemSort.setIcon(this.isDesc ? R.drawable.ic_down : R.drawable.ic_up);
        Collections.sort(this.model.getArrayList(), new Comparator<RestoreRowModel>() {
            public int compare(RestoreRowModel restoreRowModel, RestoreRowModel restoreRowModel2) {
                if (RestoreDriveListActivity.this.isDesc) {
                    return Long.compare(restoreRowModel.getTimestamp().longValue(), restoreRowModel2.getTimestamp().longValue());
                }
                return Long.compare(restoreRowModel2.getTimestamp().longValue(), restoreRowModel.getTimestamp().longValue());
            }
        });
        notifyAdapter();
    }


    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.binding.recycler.setAdapter(new RestoreAdapter(this.context, this.model.getArrayList(), new OnRecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 2) {
                    RestoreDriveListActivity.this.deleteItem(i);
                } else {
                    RestoreDriveListActivity.this.restoreItem(i);
                }
            }
        }));
    }


    public void notifyAdapter() {
        setViewVisibility();
        if (this.binding.recycler.getAdapter() != null) {
            this.binding.recycler.getAdapter().notifyDataSetChanged();
        }
    }

    private void setViewVisibility() {
        int i = 0;
        this.binding.linData.setVisibility(this.model.isListData() ? View.VISIBLE : View.GONE);
        LinearLayout linearLayout = this.binding.linNoData;
        if (this.model.isListData()) {
            i = 8;
        }
        linearLayout.setVisibility(i);
    }

    @SuppressLint("ResourceType")
    public void deleteItem(final int i) {
        DialogDeleteBinding dialogDeleteBinding = (DialogDeleteBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_delete, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogDeleteBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(-1, -2);
            dialog.getWindow().setBackgroundDrawableResource(17170445);
        }
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialogDeleteBinding.llOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RestoreDriveListActivity.this.deleteFile(i);
                dialog.dismiss();
            }
        });
        dialogDeleteBinding.llCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    public void deleteFile(final int i) {
        this.backupRestore.deleteFromDrive(this.progressDialog, this.model.getArrayList().get(i).getPath(), new OnBackupRestore() {
            public void getList(ArrayList<RestoreRowModel> arrayList) {
            }

            public void onSuccess(boolean z) {
                if (z) {
                    RestoreDriveListActivity.this.model.getArrayList().remove(i);
                    RestoreDriveListActivity.this.binding.recycler.getAdapter().notifyItemRemoved(i);
                    AppConstant.toastShort(RestoreDriveListActivity.this.context, "File delete");
                    RestoreDriveListActivity.this.notifyAdapter();
                    return;
                }
                AppConstant.toastShort(RestoreDriveListActivity.this.context, "Unable to delete");
            }
        });
    }

    @SuppressLint("ResourceType")
    public void restoreItem(final int i) {
        DialogConfRestoreBinding dialogConfRestoreBinding = (DialogConfRestoreBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_conf_restore, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogConfRestoreBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(-1, -2);
            dialog.getWindow().setBackgroundDrawableResource(17170445);
        }
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialogConfRestoreBinding.llMarge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean unused = RestoreDriveListActivity.this.isResultOK = true;
                RestoreDriveListActivity restoreDriveListActivity = RestoreDriveListActivity.this;
                restoreDriveListActivity.backupData(restoreDriveListActivity.model.getArrayList().get(i).getPath(), true);
                dialog.dismiss();
            }
        });
        dialogConfRestoreBinding.lldelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean unused = RestoreDriveListActivity.this.isResultOK = true;
                RestoreDriveListActivity restoreDriveListActivity = RestoreDriveListActivity.this;
                restoreDriveListActivity.backupData(restoreDriveListActivity.model.getArrayList().get(i).getPath(), false);
                dialog.dismiss();
            }
        });
    }


    public void backupData(String str, boolean z) {
        this.backupRestore.backupRestore(this.progressDialog, false, false, str, z, new OnBackupRestore() {
            public void getList(ArrayList<RestoreRowModel> arrayList) {
            }

            public void onSuccess(boolean z) {
                if (z) {
                    AppConstant.deleteTempFile();
                    RestoreDriveListActivity.this.backupScuccess = true;
                    AppConstant.toastShort(RestoreDriveListActivity.this.context, RestoreDriveListActivity.this.context.getString(R.string.import_successfully));
                    return;
                }
                AppConstant.toastShort(RestoreDriveListActivity.this.context, RestoreDriveListActivity.this.context.getString(R.string.failed_to_export));
            }
        });
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Log.e("TAG", "onActivityResult: " );
        if (i == Constants.REQUEST_CODE_SIGN_IN) {
            handleSignIn(intent);
        }
    }

    private void handleSignIn(Intent intent) {
        this.backupRestore.handleSignInResult(intent, true, true, (String) null, this.progressDialog, new OnBackupRestore() {
            public void onSuccess(boolean z) {
            }

            public void getList(ArrayList<RestoreRowModel> arrayList) {
                RestoreDriveListActivity.this.model.getArrayList().addAll(arrayList);
                RestoreDriveListActivity.this.notifyAdapter();
            }
        });
    }

    public void onBackPressed() {
        if (this.backupScuccess) {
            Intent intent = getIntent();
            intent.putExtra("backupScuccess", this.backupScuccess);
            setResult(-1, intent);
        }
        finish();
    }
}
