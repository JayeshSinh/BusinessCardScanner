package com.business.card.scanner.maker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Scan_Qr_Activity extends BaseActivityBinding implements EasyPermissions.PermissionCallbacks {
    private static final int IMAGE_PICK_CODE = 1000;
    String address;
    SaveCardActivityBinding binding;
    List<String> blocks;
    Business_Card card;
    String companyname;
    Database database;
    List<String> emails = new ArrayList();
    String gid;
    boolean groupbool = true;
    String image_name = "";
    boolean isSaved = false;
    String jobtitle;
    String name;

    /* renamed from: ph */
    String f170ph;
    List<String> phones = new ArrayList();
    public String qrCode;
    RGroupAdapter rGroupAdapter;
    List<String> textList = new ArrayList();
    String url;


    public void initMethods() {
    }

    public void onClick(View view) {
    }


    public void setAdapter() {
    }


    public void setBinding() {
        SaveCardActivityBinding saveCardActivityBinding = (SaveCardActivityBinding) DataBindingUtil.setContentView(this, R.layout.save_card_activity);
        this.binding = saveCardActivityBinding;
        saveCardActivityBinding.rlScanImage.setVisibility(View.GONE);
        this.database = Database.getAppDatabase(this);
        new IntentIntegrator(this).setCaptureActivity(ScannerActivity.class).initiateScan();
    }


    public void initVariable() {
        setVisiblity(8);
        this.binding.llGalleryImage.setVisibility(View.VISIBLE);
        this.binding.rlScanImage.setVisibility(View.GONE);
        setGroup();
        if (AppPref.isGroup_choose(this)) {
            this.binding.grouplay.setVisibility(View.VISIBLE);
        } else {
            this.binding.grouplay.setVisibility(View.GONE);
        }
        this.card = new Business_Card();
    }


    public void setToolbar() {
        setSupportActionBar(this.binding.toolBar);
        this.binding.toolBar.setTitle((CharSequence) "Scan QR Code");
        this.binding.toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        this.binding.toolBar.setNavigationIcon((int) R.drawable.ic_baseline_arrow_back_24);
        this.binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Scan_Qr_Activity.this.onBackPressed();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.done) {
            if (this.binding.etName.getText().toString().trim().isEmpty() || this.binding.etCompany.getText().toString().trim().isEmpty() || this.binding.etPhone.getText().toString().trim().isEmpty()) {
                if (this.binding.etName.getText().toString().trim().equals("")) {
                    this.binding.etName.setError("Name is required!");
                    this.binding.etName.requestFocus();
                } else if (this.binding.etCompany.getText().toString().trim().equals("")) {
                    this.binding.etCompany.setError("Company is required!");
                    this.binding.etCompany.requestFocus();
                } else if (this.binding.etPhone.getText().toString().trim().equals("")) {
                    this.binding.etPhone.setError("Phone is required!");
                    this.binding.etPhone.requestFocus();
                }
            } else if (!AppPref.isAuto_save(this.context) || Build.VERSION.SDK_INT >= 29) {
                setData();
            } else {
                methodRequiresStoragePermission();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void setData() {
        this.card.setId(Const.getUniqueId());
        if (AppPref.isGroup_choose(this)) {
            this.card.setGroup_id(this.gid);
        } else {
            this.card.setGroup_id("");
        }
        this.card.setFav("");
        this.card.setDate(Long.valueOf(System.currentTimeMillis()));
        if (!this.binding.etName.getText().toString().trim().isEmpty()) {
            this.card.setName(this.binding.etName.getText().toString().trim());
        } else {
            this.binding.etName.setError(" Name required");
            this.binding.etName.requestFocus();
        }
        if (!this.binding.etCompany.getText().toString().trim().isEmpty()) {
            this.card.setCompany(this.binding.etCompany.getText().toString().trim());
        } else {
            this.binding.etCompany.setError("Company Name required");
            this.binding.etCompany.requestFocus();
        }
        if (!this.binding.etJobTitle.getText().toString().trim().isEmpty()) {
            this.card.setJob_title(this.binding.etJobTitle.getText().toString().trim());
        } else {
            this.card.setJob_title("");
        }
        if (!this.binding.etAddress.getText().toString().trim().isEmpty()) {
            this.card.setAddress(this.binding.etAddress.getText().toString().trim());
        } else {
            this.card.setAddress("");
        }
        if (!this.binding.etPhone.getText().toString().trim().isEmpty()) {
            this.card.setPhone(this.binding.etPhone.getText().toString().trim());
        } else {
            this.binding.etPhone.setError("phone number required");
            this.binding.etPhone.requestFocus();
        }
        if (!this.binding.etWebsite.getText().toString().trim().isEmpty()) {
            this.card.setWebsite(this.binding.etWebsite.getText().toString().trim());
        } else {
            this.card.setWebsite("");
        }
        if (!this.binding.etEmailAddress.getText().toString().trim().isEmpty()) {
            this.card.setEmail(this.binding.etEmailAddress.getText().toString().trim());
        } else {
            this.card.setEmail("");
        }
        if (!this.image_name.trim().isEmpty()) {
            this.card.setImage_name(this.image_name);
        } else {
            this.card.setImage_name("");
        }
        if (!this.binding.etNote.getText().toString().trim().isEmpty()) {
            this.card.setNote(this.binding.etNote.getText().toString().trim());
        } else {
            this.card.setNote(StringUtils.SPACE);
        }
        this.isSaved = true;
        Splash_Activity.isRated = true;
        this.database.database_dao().insert(this.card);
        onBackPressed();
        Toast.makeText(this, getResources().getString(R.string.save_card), Toast.LENGTH_SHORT).show();
    }


    public void setOnClicks() {
        this.binding.ivName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Scan_Qr_Activity.this.listOfData(AppMeasurementSdk.ConditionalUserProperty.NAME);
            }
        });
        this.binding.ivCompany.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Scan_Qr_Activity.this.listOfData("company");
            }
        });
        this.binding.ivJobTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Scan_Qr_Activity.this.listOfData("title");
            }
        });
        this.binding.ivphone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Scan_Qr_Activity.this.listOfData("phone");
            }
        });
        this.binding.ivAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Scan_Qr_Activity.this.listOfData("Address");
            }
        });
        this.binding.addtoGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Scan_Qr_Activity.this.openAddNewDialog();
            }
        });
        this.binding.cardImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Scan_Qr_Activity.this.startGallery();
            }
        });
        this.binding.etPhone.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    Scan_Qr_Activity.this.binding.ivCall.setColorFilter(Scan_Qr_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    Scan_Qr_Activity.this.binding.ivSMS.setColorFilter(Scan_Qr_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    Scan_Qr_Activity.this.binding.ivCall.setEnabled(true);
                    Scan_Qr_Activity.this.binding.ivSMS.setEnabled(true);
                    return;
                }
                Scan_Qr_Activity.this.binding.ivCall.setColorFilter(Scan_Qr_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                Scan_Qr_Activity.this.binding.ivSMS.setColorFilter(Scan_Qr_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                Scan_Qr_Activity.this.binding.ivCall.setEnabled(false);
                Scan_Qr_Activity.this.binding.ivSMS.setEnabled(false);
            }
        });
        this.binding.etEmailAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    Scan_Qr_Activity.this.binding.ivEmail.setColorFilter(Scan_Qr_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    Scan_Qr_Activity.this.binding.ivEmail.setEnabled(true);
                    return;
                }
                Scan_Qr_Activity.this.binding.ivEmail.setColorFilter(Scan_Qr_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                Scan_Qr_Activity.this.binding.ivEmail.setEnabled(false);
            }
        });
        this.binding.etAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    Scan_Qr_Activity.this.binding.ivMap.setColorFilter(Scan_Qr_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    Scan_Qr_Activity.this.binding.ivMap.setEnabled(true);
                    return;
                }
                Scan_Qr_Activity.this.binding.ivMap.setColorFilter(Scan_Qr_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                Scan_Qr_Activity.this.binding.ivMap.setEnabled(false);
            }
        });
        this.binding.openGroupdialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Scan_Qr_Activity.this.groupbool) {
                    Scan_Qr_Activity.this.binding.hide.setVisibility(View.VISIBLE);
                    Scan_Qr_Activity.this.binding.hide.requestFocus();
                    Scan_Qr_Activity.this.groupbool = false;
                    return;
                }
                Scan_Qr_Activity.this.binding.hide.setVisibility(View.GONE);
                Scan_Qr_Activity.this.groupbool = true;
            }
        });
        this.binding.addtoGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Scan_Qr_Activity.this.openAddNewDialog();
            }
        });
    }


    public void startGallery() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1000);
    }

    public void listOfData(final String _str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems((CharSequence[]) this.textList.toArray(new String[0]), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String str = _str;
                str.hashCode();
                char c = 65535;
                switch (str.hashCode()) {
                    case -1147692044:
                        if (str.equals("address")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 3373707:
                        if (str.equals(AppMeasurementSdk.ConditionalUserProperty.NAME)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 106642798:
                        if (str.equals("phone")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 110371416:
                        if (str.equals("title")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 516961236:
                        if (str.equals("Address")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 950484093:
                        if (str.equals("company")) {
                            c = 5;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        Scan_Qr_Activity.this.binding.etAddress.setText(Scan_Qr_Activity.this.textList.get(i));
                        Scan_Qr_Activity scan_Qr_Activity = Scan_Qr_Activity.this;
                        scan_Qr_Activity.address = scan_Qr_Activity.textList.get(i);
                        return;
                    case 1:
                        Scan_Qr_Activity.this.binding.etName.setText(Scan_Qr_Activity.this.textList.get(i));
                        Scan_Qr_Activity scan_Qr_Activity2 = Scan_Qr_Activity.this;
                        scan_Qr_Activity2.name = scan_Qr_Activity2.textList.get(i);
                        return;
                    case 2:
                        Scan_Qr_Activity scan_Qr_Activity3 = Scan_Qr_Activity.this;
                        scan_Qr_Activity3.f170ph = scan_Qr_Activity3.textList.get(i);
                        Scan_Qr_Activity.this.binding.etPhone.setText(Scan_Qr_Activity.this.f170ph);
                        return;
                    case 3:
                        Scan_Qr_Activity.this.binding.etJobTitle.setText(Scan_Qr_Activity.this.textList.get(i));
                        Scan_Qr_Activity scan_Qr_Activity4 = Scan_Qr_Activity.this;
                        scan_Qr_Activity4.jobtitle = scan_Qr_Activity4.textList.get(i);
                        return;
                    case 4:
                        Scan_Qr_Activity.this.binding.etAddress.setText(Scan_Qr_Activity.this.textList.get(i));
                        Scan_Qr_Activity scan_Qr_Activity5 = Scan_Qr_Activity.this;
                        scan_Qr_Activity5.address = scan_Qr_Activity5.textList.get(i);
                        return;
                    case 5:
                        Scan_Qr_Activity.this.binding.etCompany.setText(Scan_Qr_Activity.this.textList.get(i));
                        Scan_Qr_Activity scan_Qr_Activity6 = Scan_Qr_Activity.this;
                        scan_Qr_Activity6.companyname = scan_Qr_Activity6.textList.get(i);
                        return;
                    default:
                        return;
                }
            }
        });
        builder.create().show();
    }

    public void setMcardData() {
        for (String next : this.blocks) {
            if (next.toUpperCase().contains("MECARD")) {
                String[] split = next.split(":");
                if (split.length > 2) {
                    String str = split[2];
                    this.name = str;
                    this.card.setName(str);
                }
            } else if (next.contains(":")) {
                String[] split2 = next.split(":");
                String upperCase = split2[0].toUpperCase();
                upperCase.hashCode();
                char c = 65535;
                switch (upperCase.hashCode()) {
                    case 64655:
                        if (upperCase.equals("ADR")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 78532:
                        if (upperCase.equals("ORG")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 82939:
                        if (upperCase.equals("TEL")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 84303:
                        if (upperCase.equals("URL")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 2064738:
                        if (upperCase.equals("CELL")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 2402290:
                        if (upperCase.equals("NOTE")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 66081660:
                        if (upperCase.equals("EMAIL")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 79833656:
                        if (upperCase.equals("TITLE")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 81848594:
                        if (upperCase.equals("VOICE")) {
                            c = 8;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        String str2 = split2[1];
                        this.address = str2;
                        this.card.setAddress(str2);
                        break;
                    case 1:
                        String str3 = split2[1];
                        this.companyname = str3;
                        this.card.setCompany(str3);
                        break;
                    case 2:
                    case 4:
                    case 8:
                        this.phones.add(split2[1]);
                        this.card.setPhone(this.phones.get(0));
                        break;
                    case 3:
                        if (split2.length > 2) {
                            this.url = split2[1] + split2[2];
                        } else {
                            this.url = split2[1];
                        }
                        this.card.setWebsite(this.url);
                        break;
                    case 5:
                        this.card.setNote(split2[1]);
                        break;
                    case 6:
                        this.emails.add(split2[1]);
                        this.card.setEmail(this.emails.get(0));
                        break;
                    case 7:
                        String str4 = split2[1];
                        this.jobtitle = str4;
                        this.card.setJob_title(str4);
                        break;
                }
            }
        }
    }

    public void setVcarddata() {
        for (String next : this.blocks) {
            if (next.contains(":")) {
                String[] split = next.split(":");
                String upperCase = split[0].toUpperCase();
                upperCase.hashCode();
                char c = 65535;
                switch (upperCase.hashCode()) {
                    case 78:
                        if (upperCase.equals("N")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 2248:
                        if (upperCase.equals("FN")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 64655:
                        if (upperCase.equals("ADR")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 78532:
                        if (upperCase.equals("ORG")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 82939:
                        if (upperCase.equals("TEL")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 84303:
                        if (upperCase.equals("URL")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 2064738:
                        if (upperCase.equals("CELL")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 2388619:
                        if (upperCase.equals("NAME")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 66081660:
                        if (upperCase.equals("EMAIL")) {
                            c = 8;
                            break;
                        }
                        break;
                    case 79833656:
                        if (upperCase.equals("TITLE")) {
                            c = 9;
                            break;
                        }
                        break;
                    case 81848594:
                        if (upperCase.equals("VOICE")) {
                            c = 10;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                    case 7:
                        String str = split[1];
                        this.name = str;
                        this.card.setName(str);
                        break;
                    case 2:
                        String str2 = split[1];
                        this.address = str2;
                        this.card.setAddress(str2);
                        break;
                    case 3:
                        String str3 = split[1];
                        this.companyname = str3;
                        this.card.setCompany(str3);
                        break;
                    case 4:
                    case 6:
                    case 10:
                        this.phones.add(split[1]);
                        this.card.setPhone(this.phones.get(0));
                        break;
                    case 5:
                        if (split.length > 2) {
                            this.url = split[1] + split[2];
                        } else {
                            this.url = split[1];
                        }
                        this.card.setWebsite(this.url);
                        break;
                    case 8:
                        this.emails.add(split[1]);
                        this.card.setEmail(this.emails.get(0));
                        break;
                    case 9:
                        String str4 = split[1];
                        this.jobtitle = str4;
                        this.card.setJob_title(str4);
                        break;
                }
            }
        }
    }

    public void setVcardData() {
        for (String next : this.blocks) {
            if (next.contains("\n")) {
                String[] split = next.split("\n");
                int length = split.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        String str = split[i];
                        this.textList.add(str);
                        String[] split2 = str.split(":");
                        if (split2[0].equalsIgnoreCase("FN")) {
                            String str2 = split2[1];
                            this.name = str2;
                            this.card.setName(str2);
                        } else if (split2[0].equalsIgnoreCase("N")) {
                            String str3 = split2[1];
                            this.name = str3;
                            this.card.setName(str3);
                        } else if (split2[0].equalsIgnoreCase("NAME")) {
                            String str4 = split2[1];
                            this.name = str4;
                            this.card.setName(str4);
                        } else {
                            String str5 = split2[0];
                            str5.hashCode();
                            char c = 65535;
                            switch (str5.hashCode()) {
                                case 78532:
                                    if (str5.equals("ORG")) {
                                        c = 0;
                                        break;
                                    }
                                    break;
                                case 84303:
                                    if (str5.equals("URL")) {
                                        c = 1;
                                        break;
                                    }
                                    break;
                                case 79833656:
                                    if (str5.equals("TITLE")) {
                                        c = 2;
                                        break;
                                    }
                                    break;
                            }
                            switch (c) {
                                case 0:
                                    String str6 = split2[1];
                                    this.companyname = str6;
                                    this.card.setCompany(str6);
                                    break;
                                case 1:
                                    String str7 = split2[1];
                                    this.url = str7;
                                    this.card.setWebsite(str7);
                                    break;
                                case 2:
                                    String str8 = split2[1];
                                    this.jobtitle = str8;
                                    this.card.setJob_title(str8);
                                    break;
                            }
                            if (split2[0].contains("ADR")) {
                                String str9 = split2[1];
                                this.address = str9;
                                this.card.setAddress(str9);
                            }
                            if (split2[0].contains("EMAIL")) {
                                this.emails.add(split2[1]);
                                this.card.setEmail(this.emails.get(0));
                            }
                            if (split2[0].contains("CELL")) {
                                this.phones.add(split2[1]);
                                this.card.setPhone(this.phones.get(0));
                            } else if (split2[0].contains("VOICE")) {
                                this.phones.add(split2[1]);
                                this.card.setPhone(this.phones.get(0));
                            }
                            i++;
                        }
                    }
                }
            }
        }
    }

    public void setQRData() {
        StringBuilder sb = new StringBuilder();
        try {
            this.phones = parsePhone(String.valueOf(this.blocks));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator<String> it = this.phones.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            sb.append("\n");
        }
        this.binding.etPhone.setText(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        for (String next : this.blocks) {
            if (next.contains("\n")) {
                for (String str : next.split("\n")) {
                    this.textList.add(str);
                    if (mo12106U(str).booleanValue() && !z) {
                        this.binding.etCompany.setText(str);
                        z = true;
                    }
                    if (validDesign(str).booleanValue() && !z2) {
                        this.binding.etJobTitle.setText(str);
                        z2 = true;
                    }
                    if (validateFullName(str).booleanValue() && !z3) {
                        this.binding.etName.setText(str);
                        z3 = true;
                    }
                    if (extractAddress(str)) {
                        sb2.append(str);
                        sb2.append(StringUtils.SPACE);
                    }
                }
            } else {
                this.textList.add(next);
                String lowerCase = next.toLowerCase();
                if (extractAddress(lowerCase)) {
                    sb2.append(lowerCase);
                    sb2.append(StringUtils.SPACE);
                }
                if (mo12106U(lowerCase).booleanValue() && !z) {
                    this.binding.etCompany.setText(lowerCase);
                    z = true;
                }
                if (validDesign(lowerCase).booleanValue() && !z2) {
                    this.binding.etJobTitle.setText(lowerCase);
                    z2 = true;
                }
                if (validateFullName(lowerCase).booleanValue() && !z3) {
                    this.binding.etName.setText(lowerCase);
                    z3 = true;
                }
            }
        }
        this.address = sb2.toString();
        this.binding.etAddress.setText(sb2.toString());
        if (this.phones.size() > 0) {
            List<String> list = this.phones;
            this.f170ph = list.get(list.size() - 1);
            this.binding.etPhone.setText(this.phones.get(0));
        }
        this.emails = getEmails(String.valueOf(this.blocks));
        this.binding.etWebsite.setText(getWebsite(String.valueOf(this.blocks)));
        if (this.emails.size() == 0) {
            this.binding.etEmailAddress.setText(StringUtils.SPACE);
        } else {
            this.binding.etEmailAddress.setText(this.emails.get(0));
        }
    }

    public static List<String> getEmails(String str) {
        ArrayList arrayList = new ArrayList();
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(str);
        while (matcher.find()) {
            arrayList.add(str.substring(matcher.start(0), matcher.end(0)));
        }
        return arrayList;
    }

    public boolean extractname(String str) {
        return str.toLowerCase().equals(AppMeasurementSdk.ConditionalUserProperty.NAME) || str.toLowerCase().equals("n") || str.toLowerCase().equals("fn");
    }

    public boolean extractAddress(String str) {
        return str.toLowerCase().contains("city") || str.toLowerCase().contains("floor") || str.toLowerCase().contains("road") || str.toLowerCase().contains("mall") || str.toLowerCase().contains("opposite") || str.toLowerCase().contains("colony") || str.toLowerCase().contains("garden") || str.toLowerCase().contains("plaza") || str.toLowerCase().contains("town") || str.toLowerCase().contains("nearby") || str.toLowerCase().contains("avenue") || str.toLowerCase().contains("street") || str.toLowerCase().contains("state") || str.toLowerCase().contains("opp");
    }

    private ArrayList<String> parsePhone(String str) {
        Iterable<PhoneNumberMatch> findNumbers = PhoneNumberUtil.getInstance().findNumbers(str, Locale.US.getCountry());
        ArrayList<String> arrayList = new ArrayList<>();
        for (PhoneNumberMatch rawString : findNumbers) {
            arrayList.add(rawString.rawString());
        }
        return arrayList;
    }

    public Boolean mo12106U(String str) {
        String[] strArr = new String[0];
        if (str.contains(StringUtils.SPACE)) {
            strArr = str.split(StringUtils.SPACE);
        }
        String[] stringArray = getResources().getStringArray(R.array.companynamesList);
        int length = stringArray.length;
        int i = 0;
        boolean z = false;
        while (true) {
            if (i >= length) {
                break;
            }
            String str2 = stringArray[i];
            if (str2.contains(StringUtils.SPACE)) {
                str2 = str2.replaceAll(StringUtils.SPACE, "");
            }
            if (strArr.length != 0) {
                int length2 = strArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length2) {
                        break;
                    } else if (str2.toLowerCase().equals(strArr[i2].toLowerCase())) {
                        z = true;
                        break;
                    } else {
                        i2++;
                    }
                }
            } else if (str.toLowerCase().equals(str2.toLowerCase())) {
                z = true;
                break;
            }
            i++;
        }
        return Boolean.valueOf(z);
    }

    public Boolean validDesign(String str) {
        String[] strArr = new String[0];
        if (str.contains(StringUtils.SPACE)) {
            strArr = str.split(StringUtils.SPACE);
        }
        String[] stringArray = getResources().getStringArray(R.array.designationList);
        int length = stringArray.length;
        int i = 0;
        boolean z = false;
        while (true) {
            if (i >= length) {
                break;
            }
            String str2 = stringArray[i];
            if (str2.contains(StringUtils.SPACE)) {
                str2 = str2.replaceAll(StringUtils.SPACE, "");
            }
            if (strArr.length != 0) {
                int length2 = strArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length2) {
                        break;
                    } else if (str2.toLowerCase().equals(strArr[i2].toLowerCase())) {
                        z = true;
                        break;
                    } else {
                        i2++;
                    }
                }
            } else if (str.toLowerCase().equals(str2.toLowerCase())) {
                z = true;
                break;
            }
            i++;
        }
        return Boolean.valueOf(z);
    }

    public Boolean validateFullName(String str) {
        String[] strArr = new String[0];
        if (str.contains(StringUtils.SPACE)) {
            strArr = str.split(StringUtils.SPACE);
        }
        String[] stringArray = getResources().getStringArray(R.array.namesList);
        int length = stringArray.length;
        int i = 0;
        boolean z = false;
        while (true) {
            if (i >= length) {
                break;
            }
            String str2 = stringArray[i];
            if (str2.contains(StringUtils.SPACE)) {
                str2 = str2.replaceAll(StringUtils.SPACE, "");
            }
            if (strArr.length != 0) {
                int length2 = strArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length2) {
                        break;
                    } else if (str2.toLowerCase().equals(strArr[i2].toLowerCase())) {
                        z = true;
                        break;
                    } else {
                        i2++;
                    }
                }
            } else if (str.toLowerCase().equals(str2.toLowerCase())) {
                z = true;
                break;
            }
            i++;
        }
        return Boolean.valueOf(z);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        IntentResult parseActivityResult = IntentIntegrator.parseActivityResult(i, i2, intent);
        Log.e("knhfkjsf", "result :- " + parseActivityResult);
        if (parseActivityResult == null) {
            super.onActivityResult(i, i2, intent);
        } else if (parseActivityResult.getContents() == null) {
            onBackPressed();
            Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
        } else {
            this.qrCode = parseActivityResult.getContents();
            this.blocks = new ArrayList();
            Log.e("qrCode", "qrCode :- " + this.qrCode);
            if (parseActivityResult.toString().contains("MECARD")) {
                this.blocks = Arrays.asList(this.qrCode.split(";"));
                setMcardData();
                this.binding.setViewModel(this.card);
            } else if (parseActivityResult.toString().contains("VCARD")) {
                this.blocks = Arrays.asList(this.qrCode.split("\n"));
                setVcarddata();
                this.binding.setViewModel(this.card);
            } else {
                setQRData();
            }
        }
        if (i == 1000) {
            Uri data = intent.getData();
            Bitmap bitmap = null;
            if (Build.VERSION.SDK_INT >= 29) {
                try {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getApplicationContext().getContentResolver(), data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            File saveImage = saveImage(bitmap, System.currentTimeMillis() + ".jpg");
            Uri fromFile = Uri.fromFile(saveImage);
            try {
                moveFile(saveImage, AppConstant.getCameraImage(getApplicationContext()));
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            this.image_name = fromFile.getLastPathSegment();
            this.binding.rlScanImage.setVisibility(View.VISIBLE);
            this.binding.llGalleryImage.setVisibility(View.GONE);
            if (!data.getPath().isEmpty()) {
                Glide.with((FragmentActivity) this).load(data).into(this.binding.image);
            }
        }
    }

    public String getWebsite(String str) {
        Matcher matcher = Pattern.compile("(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?").matcher(str);
        String str2 = "";
        while (matcher.find()) {
            if (matcher.group().startsWith(HttpHost.DEFAULT_SCHEME_NAME) || matcher.group().startsWith("https") || matcher.group().startsWith("www")) {
                str2 = matcher.group();
            }
        }
        Log.e("jfdgoibdf", "web1 :- " + str2);
        return str2;
    }

    private File saveImage(Bitmap bitmap, String str) {
        File file = new File(AppConstant.getTemp(this), str);
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

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0043  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void moveFile(File r9, File r10) throws IOException {
        /*
            r8 = this;
            java.io.File r0 = new java.io.File
            java.lang.String r1 = r9.getName()
            r0.<init>(r10, r1)
            r10 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ all -> 0x003a }
            r1.<init>(r0)     // Catch:{ all -> 0x003a }
            java.nio.channels.FileChannel r0 = r1.getChannel()     // Catch:{ all -> 0x003a }
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x0038 }
            r1.<init>(r9)     // Catch:{ all -> 0x0038 }
            java.nio.channels.FileChannel r10 = r1.getChannel()     // Catch:{ all -> 0x0038 }
            r3 = 0
            long r5 = r10.size()     // Catch:{ all -> 0x0038 }
            r2 = r10
            r7 = r0
            r2.transferTo(r3, r5, r7)     // Catch:{ all -> 0x0038 }
            r10.close()     // Catch:{ all -> 0x0038 }
            r9.delete()     // Catch:{ all -> 0x0038 }
            if (r10 == 0) goto L_0x0032
            r10.close()
        L_0x0032:
            if (r0 == 0) goto L_0x0037
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
            if (r10 == 0) goto L_0x0041
            r10.close()
        L_0x0041:
            if (r0 == 0) goto L_0x0046
            r0.close()
        L_0x0046:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fittech.bizcardscanner.activity.Scan_Qr_Activity.moveFile(java.io.File, java.io.File):void");
    }

    public void setGroup() {
        this.binding.rvRGroup.setLayoutManager(new LinearLayoutManager(this));
        this.rGroupAdapter = new RGroupAdapter(this, MainActivity.groupTabList, new ClickLisner() {
            public void editCard(int i, View view) {
            }

            public void cardClick(int i) {
                Log.e("called", i + "");
                Scan_Qr_Activity.this.checkGroupFalse();
                MainActivity.groupTabList.get(i).setCheck(true);
                Scan_Qr_Activity.this.gid = MainActivity.groupTabList.get(i).getGroup_tb().getGroup_id();
                Scan_Qr_Activity.this.rGroupAdapter.notifyItemChanged(i);
            }
        });
        this.binding.rvRGroup.setAdapter(this.rGroupAdapter);
        this.rGroupAdapter.notifyDataSetChanged();
    }

    public void checkGroupFalse() {
        for (int i = 0; i < MainActivity.groupTabList.size(); i++) {
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
                    group_tab.setCount(0);
                    MainActivity.groupTabList.add(group_tab);
                    Scan_Qr_Activity.this.rGroupAdapter.notifyDataSetChanged();
                    Scan_Qr_Activity.this.database.group_dao().insertGroup(group_tb);
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

    public void setVisiblity(int i) {
        this.binding.ivName.setVisibility(i);
        this.binding.ivCompany.setVisibility(i);
        this.binding.ivJobTitle.setVisibility(i);
        this.binding.ivAddress.setVisibility(i);
        this.binding.ivphone.setVisibility(i);
    }

    public void onBackPressed() {
        if (this.isSaved) {
            Intent intent = new Intent();
            intent.putExtra("QRcard", this.card);
            setResult(-1, intent);
        } else {
            setResult(0);
        }
        super.onBackPressed();
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
