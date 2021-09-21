package com.business.card.scanner.maker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import com.business.card.scanner.maker.adapter.RGroupAdapter;
import com.business.card.scanner.maker.model.Business_Card;
import com.business.card.scanner.maker.model.Group_tab;
import com.business.card.scanner.maker.model.Group_tb;
import com.business.card.scanner.maker.util.AppPref;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.baseClass.BaseActivityBinding;
import com.business.card.scanner.maker.database.Database;
import com.business.card.scanner.maker.databinding.DialogAddGroupBinding;
import com.business.card.scanner.maker.databinding.SaveCardActivityBinding;
import com.business.card.scanner.maker.listener.ClickLisner;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Create_Activity extends BaseActivityBinding implements EasyPermissions.PermissionCallbacks {
    private static final int IMAGE_PICK_CODE = 1000;
    SaveCardActivityBinding binding;
    Business_Card create_Card;
    Database database;
    String gid;
    boolean groupbool = true;
    String image_name = "";
    boolean isSaved = false;
    RGroupAdapter rGroupAdapter;
    Uri selectedUri = null;


    public void setAdapter() {
    }


    public void setBinding() {
        binding = (SaveCardActivityBinding) DataBindingUtil.setContentView(this, R.layout.save_card_activity);
        this.database = Database.getAppDatabase(this);
        binding.setClickToGetImage(this);
    }

    public void initVariable() {
        binding.rlScanImage.setVisibility(View.GONE);
        binding.llGalleryImage.setVisibility(View.VISIBLE);
        binding.retack.setVisibility(View.GONE);
        this.create_Card = new Business_Card();
        setGroup();
        if (AppPref.isGroup_choose(this)) {
            binding.grouplay.setVisibility(View.VISIBLE);
        } else {
            binding.grouplay.setVisibility(View.GONE);
        }
        binding.hide.setVisibility(View.GONE);
    }


    public void setToolbar() {
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle((CharSequence) "Create Manually");
        binding.toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        binding.toolBar.setNavigationIcon((int) R.drawable.ic_baseline_arrow_back_24);
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Create_Activity.this.onBackPressed();
            }
        });
    }


    public void setOnClicks() {
        binding.etPhone.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > View.VISIBLE) {
                    binding.ivCall.setColorFilter(Create_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    binding.ivSMS.setColorFilter(Create_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    binding.ivCall.setEnabled(true);
                    binding.ivSMS.setEnabled(true);
                    return;
                }
                binding.ivCall.setColorFilter(Create_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                binding.ivSMS.setColorFilter(Create_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                binding.ivCall.setEnabled(false);
                binding.ivSMS.setEnabled(false);
            }
        });
        binding.etEmailAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > View.VISIBLE) {
                    binding.ivEmail.setColorFilter(Create_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    binding.ivEmail.setEnabled(true);
                    return;
                }
                binding.ivEmail.setColorFilter(Create_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                binding.ivEmail.setEnabled(false);
            }
        });
        binding.etAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > View.VISIBLE) {
                    binding.ivMap.setColorFilter(Create_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    binding.ivMap.setEnabled(true);
                    return;
                }
                binding.ivMap.setColorFilter(Create_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                binding.ivMap.setEnabled(false);
            }
        });
        binding.openGroupdialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Create_Activity.this.groupbool) {
                    binding.hide.setVisibility(View.VISIBLE);
                    binding.hide.requestFocus();
                    Create_Activity.this.groupbool = false;
                    return;
                }
                binding.hide.setVisibility(View.GONE);
                Create_Activity.this.groupbool = true;
            }
        });
        binding.addtoGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Create_Activity.this.openAddNewDialog();
            }
        });
    }


    public void initMethods() {
        setInVisibal();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.cardImage) {
            startGallery();
        }
        if (view.getId() == R.id.tvDone) {
            if (binding.etName.getText().toString().trim().isEmpty() || binding.etCompany.getText().toString().trim().isEmpty() || binding.etPhone.getText().toString().trim().isEmpty()) {
                if (binding.etName.getText().toString().trim().equals("")) {
                    binding.etName.setError("Name is required!");
                    binding.etName.requestFocus();
                } else if (binding.etCompany.getText().toString().trim().equals("")) {
                    binding.etCompany.setError("Company is required!");
                    binding.etCompany.requestFocus();
                }
                else if (binding.etPhone.getText().toString().trim().equals("")) {
                    binding.etPhone.setError("Phone is required!");
                    binding.etPhone.requestFocus();
                }
            } else if (!AppPref.isAuto_save(this.context) || Build.VERSION.SDK_INT >= 29) {
                setData();
            } else {
                methodRequiresStoragePermission();
            }
        }
    }

    private void startGallery() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1000);
    }

    /*public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.done) {
            if (binding.etName.getText().toString().trim().isEmpty() || binding.etCompany.getText().toString().trim().isEmpty() || binding.etPhone.getText().toString().trim().isEmpty()) {
                if (binding.etName.getText().toString().trim().equals("")) {
                    binding.etName.setError("Name is required!");
                    binding.etName.requestFocus();
                } else if (binding.etCompany.getText().toString().trim().equals("")) {
                    binding.etCompany.setError("Company is required!");
                    binding.etCompany.requestFocus();
                }
                else if (binding.etPhone.getText().toString().trim().equals("")) {
                    binding.etPhone.setError("Phone is required!");
                    binding.etPhone.requestFocus();
                }
            } else if (!AppPref.isAuto_save(this.context) || Build.VERSION.SDK_INT >= 29) {
                setData();
            } else {
                methodRequiresStoragePermission();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }*/

    public void setInVisibal() {
        binding.ivName.setVisibility(View.GONE);
        binding.ivCompany.setVisibility(View.GONE);
        binding.ivJobTitle.setVisibility(View.GONE);
        binding.ivAddress.setVisibility(View.GONE);
        binding.ivphone.setVisibility(View.GONE);
        if (AppPref.isGroup_choose(this)) {
            binding.grouplay.setVisibility(View.VISIBLE);
        } else {
            binding.grouplay.setVisibility(View.GONE);
        }
    }

    public void setData() {
        this.create_Card.setId(Const.getUniqueId());
        if (AppPref.isGroup_choose(this)) {
            this.create_Card.setGroup_id(this.gid);
        } else {
            this.create_Card.setGroup_id("");
        }
        this.create_Card.setFav("");
        this.create_Card.setDate(Long.valueOf(System.currentTimeMillis()));
        if (!binding.etName.getText().toString().trim().isEmpty()) {
            this.create_Card.setName(binding.etName.getText().toString().trim());
        } else {
            binding.etName.setError(" Name required");
            binding.etName.requestFocus();
        }
        if (!binding.etCompany.getText().toString().trim().isEmpty()) {
            this.create_Card.setCompany(binding.etCompany.getText().toString().trim());
        } else {
            binding.etCompany.setError("Company Name required");
            binding.etCompany.requestFocus();
        }
        if (!binding.etJobTitle.getText().toString().trim().isEmpty()) {
            this.create_Card.setJob_title(binding.etJobTitle.getText().toString().trim());
        } else {
            this.create_Card.setJob_title("");
        }
        if (!binding.etAddress.getText().toString().trim().isEmpty()) {
            this.create_Card.setAddress(binding.etAddress.getText().toString().trim());
        } else {
            this.create_Card.setAddress("");
        }
        if (!binding.etPhone.getText().toString().trim().isEmpty()) {
            this.create_Card.setPhone(binding.etPhone.getText().toString().trim());
        } else {
            binding.etPhone.setError("phone number required");
            binding.etPhone.requestFocus();
        }
        if (!binding.etWebsite.getText().toString().trim().isEmpty()) {
            this.create_Card.setWebsite(binding.etWebsite.getText().toString().trim());
        } else {
            this.create_Card.setWebsite("");
        }
        if (!binding.etEmailAddress.getText().toString().trim().isEmpty()) {
            this.create_Card.setEmail(binding.etEmailAddress.getText().toString().trim());
        } else {
            this.create_Card.setEmail("");
        }
        if (this.selectedUri != null) {
            Bitmap bitmap = null;
            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getApplicationContext().getContentResolver(), this.selectedUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), this.selectedUri);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            File saveImage = saveImage(bitmap, System.currentTimeMillis() + ".jpg");
            Uri fromFile = Uri.fromFile(saveImage);
//            try {
                moveFile(saveImage, AppConstant.getCameraImage(getApplicationContext()));

            String lastPathSegment = fromFile.getLastPathSegment();
            this.image_name = lastPathSegment;
            if (!lastPathSegment.trim().isEmpty()) {
                this.create_Card.setImage_name(this.image_name);
            } else {
                this.create_Card.setImage_name("");
            }
        }
        if (!binding.etNote.getText().toString().trim().isEmpty()) {
            this.create_Card.setNote(binding.etNote.getText().toString().trim());
        } else {
            this.create_Card.setNote(StringUtils.SPACE);
        }
        Create_Activity.this.isSaved = true;
        Splash_Activity.isRated = true;
        Create_Activity.this.database.database_dao().insert(Create_Activity.this.create_Card);
        Create_Activity create_Activity = Create_Activity.this;
        Toast.makeText(create_Activity, create_Activity.getResources().getString(R.string.save_card), Toast.LENGTH_SHORT).show();
        onBackPressed();
     /*   MainActivity.BackPressedAd(this, new adBackScreenListener() {
            public void BackScreen() {

            }
        });*/
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1000 && intent != null) {
            this.selectedUri = intent.getData();
            binding.rlScanImage.setVisibility(View.VISIBLE);
            binding.llGalleryImage.setVisibility(View.GONE);
            if (this.selectedUri != null) {
                Glide.with((FragmentActivity) this).load(this.selectedUri).into(binding.image);
            }
        }
    }

    private File saveImage(Bitmap bitmap, String str) {
        File file = new File(AppConstant.getTemp(this), str);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return file;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0043  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void moveFile(File r9, File r10)  {



        try {


//        r8 = this;
            java.lang.String r1 = r9.getName();
            java.io.File r0 = new java.io.File(r10, r1);
//        r10 = 0

            java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(r0);     // Catch:{ all -> 0x003a }
            java.nio.channels.FileChannel fileChannel = fileOutputStream.getChannel();   // Catch:{ all -> 0x003a }
            java.io.FileInputStream inputStream = new java.io.FileInputStream(r9);     // Catch:{ all -> 0x0038 }
            java.nio.channels.FileChannel fileChannel1 = inputStream.getChannel();  // Catch:{ all -> 0x0038 }
//        r3 = 0
            long r5 = fileChannel1.size();     // Catch:{ all -> 0x0038 }
//        r2 = fileChannel1;
//        r7 = fileChannel;
            fileChannel1.transferTo(0, r5, fileChannel);    // Catch:{ all -> 0x0038 }
            fileChannel1.close();    // Catch:{ all -> 0x0038 }
            r9.delete();    // Catch:{ all -> 0x0038 }
            if (fileChannel1 == null) {
//            goto L_0x0032

                if (fileChannel == null) {
//                goto L_0x0037
                    return;
                }
                fileChannel.close();
                return;
            }
            fileChannel1.close();
        }
        catch (IOException e)
        {
            Log.e("Catch", "moveFile: "+e.getMessage() );
        }

        /*
            r8 = this;
            java.io.File r0 = new java.io.File
            java.lang.String r1 = r9.getName()
            r0.<init>(r10, r1)
            r10 = View.VISIBLE
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ all -> 0x003a }
            r1.<init>(r0)     // Catch:{ all -> 0x003a }
            java.nio.channels.FileChannel r0 = r1.getChannel()     // Catch:{ all -> 0x003a }
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x0038 }
            r1.<init>(r9)     // Catch:{ all -> 0x0038 }
            java.nio.channels.FileChannel r10 = r1.getChannel()     // Catch:{ all -> 0x0038 }
            r3 = View.VISIBLE
            long r5 = r10.size()     // Catch:{ all -> 0x0038 }
            r2 = r10
            r7 = r0
            r2.transferTo(r3, r5, r7)     // Catch:{ all -> 0x0038 }
            r10.close()     // Catch:{ all -> 0x0038 }
            r9.delete()     // Catch:{ all -> 0x0038 }
            if (r10 == View.VISIBLE) goto L_0x0032
            r10.close()
        L_0x0032:
            if (r0 == View.VISIBLE) goto L_0x0037
            r0.close()
        L_0x0037:
            return
        L_0x0038:
            r9 = move-exception
            goto L_0x003c
        L_0x003a:
            r9 = move-exception
            r0 = r10
        L_0x003c:
            if (r10 == View.VISIBLE) goto L_0x0041
            r10.close()
        L_0x0041:
            if (r0 == View.VISIBLE) goto L_0x0046
            r0.close()
        L_0x0046:
            throw r9
        */
      //  throw new UnsupportedOperationException("Method not decompiled: com.fittech.bizcardscanner.activity.Create_Activity.moveFile(java.io.File, java.io.File):void");
    }

    public void setGroup() {
        binding.rvRGroup.setLayoutManager(new LinearLayoutManager(this));
        this.rGroupAdapter = new RGroupAdapter(this, MainActivity.groupTabList, new ClickLisner() {
            public void editCard(int i, View view) {
            }

            public void cardClick(int i) {
                Log.e("called", i + "");
                Create_Activity.this.checkGroupFalse();
                MainActivity.groupTabList.get(i).setCheck(true);
                Create_Activity.this.gid = MainActivity.groupTabList.get(i).getGroup_tb().getGroup_id();
                Create_Activity.this.rGroupAdapter.notifyItemChanged(i);
            }
        });
        binding.rvRGroup.setAdapter(this.rGroupAdapter);
        this.rGroupAdapter.notifyDataSetChanged();
    }

    public void checkGroupFalse() {
        for (int i = View.VISIBLE; i < MainActivity.groupTabList.size(); i++) {
            MainActivity.groupTabList.get(i).setCheck(false);
            this.rGroupAdapter.notifyItemChanged(i);
        }
    }


    @SuppressLint("ResourceType")
    public void openAddNewDialog() {
        final DialogAddGroupBinding dialogAddGroupBinding = (DialogAddGroupBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_add_group, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogAddGroupBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialogAddGroupBinding.llSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!dialogAddGroupBinding.etGroupName.getText().toString().isEmpty()) {
                    Group_tab group_tab = new Group_tab();
                    Group_tb group_tb = new Group_tb();
                    group_tb.setGroup_id(UUID.randomUUID().toString());
                    group_tb.setGroup_name(dialogAddGroupBinding.etGroupName.getText().toString());
                    group_tb.setDefultcard("");
                    group_tab.setGroup_tb(group_tb);
                    group_tab.setCount(View.VISIBLE);
                    MainActivity.groupTabList.add(group_tab);
                    Create_Activity.this.rGroupAdapter.notifyDataSetChanged();
                    Create_Activity.this.database.group_dao().insertGroup(group_tb);
                    dialog.dismiss();
                }
            }
        });
        dialogAddGroupBinding.llCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }

    public void onBackPressed() {
        if (this.isSaved) {
            Intent intent = new Intent();
            intent.putExtra("card", this.create_Card);
            intent.putExtra("isCardAdd", true);
            setResult(-1, intent);
        } else {
            setResult(View.VISIBLE);
        }
        finish();
    }

    private void methodRequiresStoragePermission() {
        String[] strArr = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (EasyPermissions.hasPermissions(this, strArr)) {
            setData();
        } else {
            EasyPermissions.requestPermissions((Activity) this, getString(R.string.storage_permission), (int) AppConstant.RC_STORAGE, strArr);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    public void onPermissionsGranted(int i, List<String> list) {
        setData();
    }

    public void onPermissionsDenied(int i, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }
}
