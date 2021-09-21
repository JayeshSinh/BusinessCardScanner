package com.business.card.scanner.maker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import com.business.card.scanner.maker.adapter.RGroupAdapter;
import com.business.card.scanner.maker.model.Business_Card;
import com.business.card.scanner.maker.model.Group_tab;
import com.business.card.scanner.maker.model.Group_tb;
import com.business.card.scanner.maker.util.AppPref;
import com.business.card.scanner.maker.util.Constants;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.baseClass.BaseActivityBinding;
import com.business.card.scanner.maker.database.Database;
import com.business.card.scanner.maker.databinding.DialogAddGroupBinding;
import com.business.card.scanner.maker.databinding.SaveCardActivityBinding;
import com.business.card.scanner.maker.listener.ClickLisner;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import net.lingala.zip4j.util.InternalZipConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Save_card_Activity extends BaseActivityBinding implements EasyPermissions.PermissionCallbacks {
    String address;
    SaveCardActivityBinding binding;
    Bitmap bitmap;
    List<String> blocks;
    Business_Card card;
    String companyname;
    Database database;
    List<String> emails = new ArrayList();
    String gid;
    boolean groupbool = true;
    Uri uri;
    String image_name;
    boolean isCardAdd = false;
    boolean isRetake = false;
    String jobtitle;
    String name;

    /* renamed from: ph */
    String f169ph;
    List<String> phones = new ArrayList();
    RGroupAdapter rGroupAdapter;
    List<String> textList = new ArrayList();


    public void initMethods() {
    }

    public void onClick(View view) {
    }


    public void setAdapter() {
    }


    public void setBinding() {
        this.binding = (SaveCardActivityBinding) DataBindingUtil.setContentView(this, R.layout.save_card_activity);
        this.database = Database.getAppDatabase(getApplicationContext());
    }


    public void initVariable() {
        boolean booleanExtra = getIntent().getBooleanExtra("isCardAdd", false);
        this.isCardAdd = booleanExtra;
        if (booleanExtra) {
            Log.e("business_Card", "initVariable: booleanExtra--> "+booleanExtra );
            setImageOcr(getIntent());
        } else {
            Business_Card business_Card = (Business_Card) getIntent().getParcelableExtra("card");

            Log.e("business_Card", "initVariable: business_Card--> "+business_Card );

            this.card = business_Card;
            this.binding.setViewModel(business_Card);
            setVisiblity(8);
            this.binding.toolBar.setTitle((CharSequence) "Edit Card");
            this.binding.rlScanImage.setVisibility(View.VISIBLE);
            this.binding.llGalleryImage.setVisibility(View.GONE);
            if (this.card.getImage_name() == null) {
                Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.big_image)).into(this.binding.image);
            } else {
                RequestManager with = Glide.with((FragmentActivity) this);
                with.load(AppConstant.getCameraImage(this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + this.card.getImage_name()).into(this.binding.image);
            }
            this.image_name = this.card.getImage_name();
        }
        if (AppPref.isGroup_choose(this)) {
            setGroup();
            this.binding.grouplay.setVisibility(View.GONE);
        } else {
            this.binding.grouplay.setVisibility(View.GONE);
        }
        this.binding.hide.setVisibility(View.GONE);
    }

    private Bitmap getIMGSize(Uri uri) {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                return ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri), new ImageDecoder.OnHeaderDecodedListener() {
                    public void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                        imageDecoder.setMutableRequired(true);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public void setImageOcr(Intent intent) {
        Uri uri = (Uri) intent.getExtras().get("imageuri");
        this.uri = uri;
        this.image_name = uri.getLastPathSegment();
      //  getContentResolver();
        try {
            this.bitmap = getIMGSize(this.uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.binding.rlScanImage.setVisibility(View.VISIBLE);
        this.binding.llGalleryImage.setVisibility(View.GONE);


        if (this.bitmap != null) {
            Glide.with((FragmentActivity) this).load(this.bitmap).into(this.binding.image);
        } else {
      //      Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.big_image)).into(this.binding.image);
        }
        new GetDataFromString(this, this.bitmap).execute(new Void[0]);
    }


    public void setToolbar() {
        setSupportActionBar(this.binding.toolBar);
        this.binding.toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        this.binding.toolBar.setNavigationIcon((int) R.drawable.ic_baseline_arrow_back_24);
        this.binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Save_card_Activity.this.onBackPressed();
            }
        });
    }


    public void setOnClicks() {
        this.binding.ivName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Save_card_Activity.this.listOfData(AppMeasurementSdk.ConditionalUserProperty.NAME);
            }
        });
        this.binding.ivCompany.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Save_card_Activity.this.listOfData("company");
            }
        });
        this.binding.ivJobTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Save_card_Activity.this.listOfData("title");
            }
        });
        this.binding.ivphone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Save_card_Activity.this.listOfData("phone");
            }
        });
        this.binding.ivAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Save_card_Activity.this.listOfData("Address");
            }
        });
        this.binding.etPhone.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    Save_card_Activity.this.binding.ivCall.setColorFilter(Save_card_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    Save_card_Activity.this.binding.ivSMS.setColorFilter(Save_card_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    Save_card_Activity.this.binding.ivCall.setEnabled(true);
                    Save_card_Activity.this.binding.ivSMS.setEnabled(true);
                    return;
                }
                Save_card_Activity.this.binding.ivCall.setColorFilter(Save_card_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                Save_card_Activity.this.binding.ivSMS.setColorFilter(Save_card_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                Save_card_Activity.this.binding.ivCall.setEnabled(false);
                Save_card_Activity.this.binding.ivSMS.setEnabled(false);
            }
        });
        this.binding.etEmailAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    Save_card_Activity.this.binding.ivEmail.setColorFilter(Save_card_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    Save_card_Activity.this.binding.ivEmail.setEnabled(true);
                    return;
                }
                Save_card_Activity.this.binding.ivEmail.setColorFilter(Save_card_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                Save_card_Activity.this.binding.ivEmail.setEnabled(false);
            }
        });
        this.binding.etAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.length() > 0) {
                    Save_card_Activity.this.binding.ivMap.setColorFilter(Save_card_Activity.this.getResources().getColor(R.color.fontcolor_main));
                    Save_card_Activity.this.binding.ivMap.setEnabled(true);
                    return;
                }
                Save_card_Activity.this.binding.ivMap.setColorFilter(Save_card_Activity.this.getResources().getColor(R.color.fontcolor_gray));
                Save_card_Activity.this.binding.ivMap.setEnabled(false);
            }
        });
        this.binding.openGroupdialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Save_card_Activity.this.binding.nestScroll.scrollTo(0, Save_card_Activity.this.binding.nestScroll.getBottom());
                if (Save_card_Activity.this.groupbool) {
                    Save_card_Activity.this.binding.hide.setVisibility(View.GONE);
                    Save_card_Activity.this.binding.hide.requestFocus();
                    Save_card_Activity.this.groupbool = false;
                    return;
                }
                Save_card_Activity.this.binding.hide.setVisibility(View.VISIBLE);
                Save_card_Activity.this.groupbool = true;
            }
        });
        this.binding.retack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Save_card_Activity.this.startActivityForResult(new Intent(Save_card_Activity.this, Camera_Activity.class), Constants.Camera_Retake);
            }
        });
        this.binding.addtoGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Save_card_Activity.this.openAddNewDialog();
            }
        });
        this.binding.tvDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (binding.etName.getText().toString().trim().isEmpty() ||
                        binding.etCompany.getText().toString().trim().isEmpty() ||
                        binding.etPhone.getText().toString().trim().isEmpty()) {
                    if (binding.etName.getText().toString().trim().equals("")) {
                        binding.etName.setError("Name is required!");
                        binding.etName.requestFocus();
                    }
                    else if (binding.etCompany.getText().toString().trim().equals("")) {
                        binding.etCompany.setError("Company is required!");
                        binding.etCompany.requestFocus();
                    }
                    else if (binding.etPhone.getText().toString().trim().equals("")) {
                        binding.etPhone.setError("Phone is required!");
                        binding.etPhone.requestFocus();
                    }
                }
                else if (!AppPref.isAuto_save(context) || Build.VERSION.SDK_INT >= 29) {
                    setData();
                }
                else {
                    methodRequiresStoragePermission();
                }
            }
        });
    }

   /* public boolean onCreateOptionsMenu(Menu menu) {
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
*/
    /* access modifiers changed from: package-private */
    public void setData() {
        final Database appDatabase = Database.getAppDatabase(this);
        if (this.isCardAdd) {
            Business_Card business_Card = new Business_Card();
            this.card = business_Card;
            business_Card.setId(Const.getUniqueId());
            this.card.setFav("");
            this.card.setDate(Long.valueOf(System.currentTimeMillis()));
            if (!this.binding.etName.getText().toString().trim().isEmpty()) {
                this.card.setName(this.binding.etName.getText().toString().trim());
            }
            if (!this.binding.etCompany.getText().toString().trim().isEmpty()) {
                this.card.setCompany(this.binding.etCompany.getText().toString().trim());
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
            }
            if (AppPref.isGroup_choose(this)) {
                this.card.setGroup_id(this.gid);
            } else {
                this.card.setGroup_id("");
            }
            if (!this.binding.etNote.getText().toString().trim().isEmpty()) {
                this.card.setNote(this.binding.etNote.getText().toString().trim());
            } else {
                this.card.setNote(StringUtils.SPACE);
            }
            try {
                moveFile(new File(this.uri.getPath()), AppConstant.getCameraImage(getApplicationContext()));
                if (AppPref.isAuto_save(this)) {
                    saveImage(this.bitmap, String.valueOf(System.currentTimeMillis()));
                }
                DeleteTempFile(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Splash_Activity.isRated = true;
            appDatabase.database_dao().insert(this.card);
            Intent intent = new Intent();
            intent.putExtra("card", this.card);
            intent.putExtra("isCardAdd", this.isCardAdd);
            setResult(-1, intent);
            Toast.makeText(this, getResources().getString(R.string.save_card), Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!this.isCardAdd) {
            if (!this.binding.etName.getText().toString().trim().isEmpty()) {
                this.card.setName(this.binding.etName.getText().toString().trim());
            }
            if (!this.binding.etCompany.getText().toString().trim().isEmpty()) {
                this.card.setCompany(this.binding.etCompany.getText().toString().trim());
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
            if (this.isRetake) {
                File file = new File(AppConstant.getCameraImage(this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + this.card.getImage_name());
                if (file.exists()) {
                    file.delete();
                }
                try {
                    moveFile(new File(this.uri.getPath()), AppConstant.getCameraImage(getApplicationContext()));
                    if (AppPref.isAuto_save(this)) {
                        saveImage(this.bitmap, String.valueOf(System.currentTimeMillis()));
                    }
                    DeleteTempFile(this);
                    if (!this.image_name.trim().isEmpty()) {
                        this.card.setImage_name(this.image_name);
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (!this.binding.etNote.getText().toString().trim().isEmpty()) {
                this.card.setNote(this.binding.etNote.getText().toString().trim());
            } else {
                this.card.setNote("");
            }
            String str = this.gid;
            if (str != null) {
                this.card.setGroup_id(str);
            }
            Splash_Activity.isRated = true;
            appDatabase.database_dao().update(Save_card_Activity.this.card);
            Intent intent = new Intent();
            intent.putExtra("card", Save_card_Activity.this.card);
            intent.putExtra("isCardAdd", Save_card_Activity.this.isCardAdd);
            Save_card_Activity.this.setResult(-1, intent);
            Save_card_Activity.this.onBackPressed();
        }
    }

    public void setVisiblity(int i) {
        this.binding.ivName.setVisibility(i);
        this.binding.ivCompany.setVisibility(i);
        this.binding.ivJobTitle.setVisibility(i);
        this.binding.ivAddress.setVisibility(i);
        this.binding.ivphone.setVisibility(i);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2403 && intent != null) {
            setImageOcr(intent);
            this.isRetake = true;
            setVisiblity(0);
        }
    }

    private class GetDataFromString extends AsyncTask<Void, Void, List<String>> {
        Bitmap bitmap;
        private ProgressDialog dialog;
        boolean isCompanyFound;
        boolean isDesignFound;
        boolean isFullName;

        public GetDataFromString(Activity activity, Bitmap bitmap2) {
            this.dialog = new ProgressDialog(activity);
            this.bitmap = bitmap2;
        }


        public void onPreExecute() {
            this.dialog.setMessage("Card is Scanning, please wait.");
            this.dialog.show();
        }


        public List<String> doInBackground(Void... voidArr) {
            TextRecognizer build = new TextRecognizer.Builder(Save_card_Activity.this.context).build();
            if (build.isOperational()) {
                SparseArray<TextBlock> detect = build.detect(new Frame.Builder().setBitmap(this.bitmap).build());
                Save_card_Activity.this.blocks = new ArrayList();
                for (int i = 0; i < detect.size(); i++) {
                    Save_card_Activity.this.blocks.add(detect.valueAt(i).getValue());
                }
            }
            return Save_card_Activity.this.blocks;
        }


        public void onPostExecute(List<String> list) {
            StringBuilder sb = new StringBuilder();
            try {
                Save_card_Activity save_card_Activity = Save_card_Activity.this;
                save_card_Activity.phones = save_card_Activity.parsePhone(String.valueOf(list));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Iterator<String> it = Save_card_Activity.this.phones.iterator();
            if (it.hasNext()) {
                sb.append(it.next());
                sb.append("\n");
            }
            Save_card_Activity.this.binding.etPhone.setText(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            Iterator<String> it2 = list.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                String next = it2.next();
                if (next.contains("\n")) {
                    for (String str : next.split("\n")) {
                        Save_card_Activity.this.textList.add(str);
                        if (Save_card_Activity.this.mo12106U(str).booleanValue() && !this.isCompanyFound) {
                            this.isCompanyFound = true;
                            Save_card_Activity.this.companyname = str;
                            Save_card_Activity.this.binding.etCompany.setText(str);
                        }
                        if (Save_card_Activity.this.validDesign(str).booleanValue() && !this.isDesignFound) {
                            this.isDesignFound = true;
                            Save_card_Activity.this.binding.etJobTitle.setText(str);
                            Save_card_Activity.this.jobtitle = str;
                        }
                        if (Save_card_Activity.this.validateFullName(str).booleanValue() && !this.isFullName) {
                            this.isFullName = true;
                            Save_card_Activity.this.name = str;
                            Save_card_Activity.this.binding.etName.setText(str);
                        }
                        if (Save_card_Activity.this.extractAddress(str)) {
                            sb2.append(str);
                            sb2.append(StringUtils.SPACE);
                        }
                    }
                } else {
                    Save_card_Activity.this.textList.add(next);
                    String lowerCase = next.toLowerCase();
                    if (Save_card_Activity.this.extractAddress(lowerCase)) {
                        sb2.append(lowerCase);
                        sb2.append(StringUtils.SPACE);
                    }
                    if (Save_card_Activity.this.mo12106U(lowerCase).booleanValue() && !this.isCompanyFound) {
                        this.isCompanyFound = true;
                        Save_card_Activity.this.binding.etCompany.setText(lowerCase);
                    }
                    if (Save_card_Activity.this.validDesign(lowerCase).booleanValue() && !this.isDesignFound) {
                        this.isDesignFound = true;
                        Save_card_Activity.this.binding.etJobTitle.setText(lowerCase);
                    }
                    if (Save_card_Activity.this.validateFullName(lowerCase).booleanValue() && !this.isFullName) {
                        this.isFullName = true;
                        Save_card_Activity.this.binding.etName.setText(lowerCase);
                    }
                }
            }
            Save_card_Activity.this.address = sb2.toString();
            Save_card_Activity.this.binding.etAddress.setText(sb2.toString());
            if (Save_card_Activity.this.phones.size() > 0) {
                Save_card_Activity save_card_Activity2 = Save_card_Activity.this;
                save_card_Activity2.f169ph = save_card_Activity2.phones.get(Save_card_Activity.this.phones.size() - 1);
                Save_card_Activity.this.binding.etPhone.setText(Save_card_Activity.this.phones.get(0));
            }
            Save_card_Activity.this.emails = Save_card_Activity.getEmails(String.valueOf(list));
            Save_card_Activity.this.binding.etWebsite.setText(Save_card_Activity.this.getWebsite(String.valueOf(list)));
            if (Save_card_Activity.this.emails.size() == 0) {
                Save_card_Activity.this.binding.etEmailAddress.setText(StringUtils.SPACE);
            } else {
                Save_card_Activity.this.binding.etEmailAddress.setText(Save_card_Activity.this.emails.get(0));
            }
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
        }
    }


    public ArrayList<String> parsePhone(String str) {
        Iterable<PhoneNumberMatch> findNumbers = PhoneNumberUtil.getInstance().findNumbers(str, Locale.US.getCountry());
        ArrayList<String> arrayList = new ArrayList<>();
        for (PhoneNumberMatch rawString : findNumbers) {
            arrayList.add(rawString.rawString());
        }
        return arrayList;
    }

    public boolean extractAddress(String str) {
        return str.toLowerCase().contains("city") || str.toLowerCase().contains("floor") || str.toLowerCase().contains("road") || str.toLowerCase().contains("mall") || str.toLowerCase().contains("opposite") || str.toLowerCase().contains("colony") || str.toLowerCase().contains("garden") || str.toLowerCase().contains("plaza") || str.toLowerCase().contains("town") || str.toLowerCase().contains("nearby") || str.toLowerCase().contains("avenue") || str.toLowerCase().contains("street") || str.toLowerCase().contains("state") || str.toLowerCase().contains("opp");
    }

    public String getWebsite(String str) {
        Matcher matcher = Pattern.compile("(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?").matcher(str);
        String str2 = "";
        while (matcher.find()) {
            if (matcher.group().startsWith(HttpHost.DEFAULT_SCHEME_NAME) || matcher.group().startsWith("https") || matcher.group().startsWith("www")) {
                str2 = matcher.group();
            }
        }
        return str2;
    }

    public static List<String> getEmails(String str) {
        ArrayList arrayList = new ArrayList();
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(str);
        while (matcher.find()) {
            arrayList.add(str.substring(matcher.start(0), matcher.end(0)));
        }
        return arrayList;
    }

    public void listOfData(final String str_) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems((CharSequence[]) this.textList.toArray(new String[0]), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String str = str_;
                str.hashCode();
                char c = 65535;
                switch (str.hashCode()) {
                    case 3373707:
                        if (str.equals(AppMeasurementSdk.ConditionalUserProperty.NAME)) {
                            c = 0;
                            break;
                        }
                        break;
                    case 106642798:
                        if (str.equals("phone")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 110371416:
                        if (str.equals("title")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 516961236:
                        if (str.equals("Address")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 950484093:
                        if (str.equals("company")) {
                            c = 4;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        Save_card_Activity.this.binding.etName.setText(Save_card_Activity.this.textList.get(i));
                        Save_card_Activity save_card_Activity = Save_card_Activity.this;
                        save_card_Activity.name = save_card_Activity.textList.get(i);
                        return;
                    case 1:
                        Save_card_Activity.this.binding.etPhone.setText(Save_card_Activity.this.textList.get(i));
                        Save_card_Activity save_card_Activity2 = Save_card_Activity.this;
                        save_card_Activity2.f169ph = save_card_Activity2.textList.get(i);
                        return;
                    case 2:
                        Save_card_Activity.this.binding.etJobTitle.setText(Save_card_Activity.this.textList.get(i));
                        Save_card_Activity save_card_Activity3 = Save_card_Activity.this;
                        save_card_Activity3.jobtitle = save_card_Activity3.textList.get(i);
                        return;
                    case 3:
                        Save_card_Activity.this.binding.etAddress.setText(Save_card_Activity.this.textList.get(i));
                        Save_card_Activity save_card_Activity4 = Save_card_Activity.this;
                        save_card_Activity4.address = save_card_Activity4.textList.get(i);
                        return;
                    case 4:
                        Save_card_Activity.this.binding.etCompany.setText(Save_card_Activity.this.textList.get(i));
                        Save_card_Activity save_card_Activity5 = Save_card_Activity.this;
                        save_card_Activity5.companyname = save_card_Activity5.textList.get(i);
                        return;
                    default:
                        return;
                }
            }
        });
        builder.create().show();
    }

    public static void DeleteTempFile(Context context) {
        File[] listFiles = AppConstant.getTemp(context).listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File delete : listFiles) {
                delete.delete();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0043  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void moveFile(File r9, File r10) throws IOException {

//        r8 = this;
        java.lang.String r1 = r9.getName();
        java.io.File r0 = new java.io.File(r10, r1);
//        r10 = 0

        java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(r0);     // Catch:{ all -> 0x003a }
                java.nio.channels.FileChannel fileChannel = fileOutputStream.getChannel()  ;   // Catch:{ all -> 0x003a }
        java.io.FileInputStream inputStream = new java.io.FileInputStream(r9);     // Catch:{ all -> 0x0038 }
                java.nio.channels.FileChannel fileChannel1 = inputStream.getChannel()   ;  // Catch:{ all -> 0x0038 }
//        r3 = 0
        long r5 = fileChannel1.size();     // Catch:{ all -> 0x0038 }
//        r2 = fileChannel1;
//        r7 = fileChannel;
        fileChannel1.transferTo(0, r5, fileChannel) ;    // Catch:{ all -> 0x0038 }
        fileChannel1.close() ;    // Catch:{ all -> 0x0038 }
        r9.delete() ;    // Catch:{ all -> 0x0038 }
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
//        throw new UnsupportedOperationException("Method not decompiled: com.fittech.bizcardscanner.activity.Save_card_Activity.moveFile(java.io.File, java.io.File):void");
    }

    private void saveImage(Bitmap bitmap2, String str) throws IOException {
        OutputStream outputStream;
        if (Build.VERSION.SDK_INT >= 29) {
            ContentResolver contentResolver = this.context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", str);
            contentValues.put("mime_type", "image/jpeg");
            contentValues.put("relative_path", Environment.DIRECTORY_DCIM + File.separator + Constants.DOWNLOAD_DIRECTORY);
            outputStream = contentResolver.openOutputStream(contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues));
        } else {
            String str2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + Constants.DOWNLOAD_DIRECTORY;
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdir();
            }
            outputStream = new FileOutputStream(new File(str2, str + ".jpg"));
        }
        bitmap2.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
        outputStream.flush();
        outputStream.close();
    }

    public void setGroup() {
        this.binding.rvRGroup.setLayoutManager(new LinearLayoutManager(this));
        this.rGroupAdapter = new RGroupAdapter(this, MainActivity.groupTabList, new ClickLisner() {
            public void editCard(int i, View view) {
            }

            public void cardClick(int i) {
                Log.e("called", i + "");
                Save_card_Activity.this.checkGroupFalse();
                MainActivity.groupTabList.get(i).setCheck(true);
                Save_card_Activity.this.gid = MainActivity.groupTabList.get(i).getGroup_tb().getGroup_id();
                Save_card_Activity.this.rGroupAdapter.notifyItemChanged(i);
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
                    Save_card_Activity.this.rGroupAdapter.notifyDataSetChanged();
                    Save_card_Activity.this.database.group_dao().insertGroup(group_tb);
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

    public void onBackPressed() {
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
