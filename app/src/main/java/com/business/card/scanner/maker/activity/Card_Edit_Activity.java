package com.business.card.scanner.maker.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.business.card.scanner.maker.model.Business_Card;
import com.business.card.scanner.maker.util.Constants;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.baseClass.BaseActivityBinding;
import com.business.card.scanner.maker.databinding.DialogShareBinding;
import com.business.card.scanner.maker.databinding.EditCardActivityBinding;
import com.business.card.scanner.maker.databinding.ImageDialogBinding;
import com.davemorrissey.labs.subscaleview.ImageSource;

import net.lingala.zip4j.util.InternalZipConstants;

import org.apache.http.protocol.HTTP;

import java.io.File;

public class Card_Edit_Activity extends BaseActivityBinding {
    EditCardActivityBinding binding;
    Boolean isCardAdd;
    Boolean isupdate = false;
    Business_Card updateCard;


    public void initMethods() {
    }

    public void onClick(View view) {
    }


    public void setAdapter() {
    }

    public void setBinding() {
        binding = (EditCardActivityBinding) DataBindingUtil.setContentView(this, R.layout.edit_card_activity);
    }

    public void initVariable() {
        this.updateCard = (Business_Card) getIntent().getParcelableExtra("card");

        Log.e("TAG", "initVariable: "+this.updateCard.getName());

        binding.setViewModel(this.updateCard);
        binding.rlScanImage.setVisibility(View.VISIBLE);
        binding.llGalleryImage.setVisibility(View.GONE);
        this.isCardAdd = getIntent().getBooleanExtra("isCardAdd", false);
        String imgPath = AppConstant.getCameraImage(this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + this.updateCard.getImage_name();
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openZoomImageDialog(imgPath);
            }
        });
        if (this.updateCard.getImage_name() != null) {
            RequestManager with = Glide.with((FragmentActivity) this);
            ((RequestBuilder) with.load(imgPath).error((int) R.drawable.ic_add_img)).into(binding.image);
            return;
        }
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.ic_add_img)).into(binding.image);

    }

    public void setToolbar() {
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle((CharSequence) "View Card");
        binding.toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        binding.toolBar.setNavigationIcon((int) R.drawable.ic_baseline_arrow_back_24);
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Card_Edit_Activity.this.onBackPressed();
            }
        });
    }

    public void setOnClicks() {
        binding.ivCall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Card_Edit_Activity.this.openCall();
            }
        });
        binding.ivSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Card_Edit_Activity.this.openSMS();
            }
        });
        binding.ivEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Card_Edit_Activity.this.openEmail();
            }
        });
        /*binding.ivEmailAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Card_Edit_Activity.this.openEmail();
            }
        });*/
        binding.ivMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Card_Edit_Activity.this.openMap();
            }
        });
        /*binding.ivWebsite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Card_Edit_Activity.this.openWebsite();
            }
        });*/
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_card_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.editCard) {
            Log.e("TAG", "onOptionsItemSelected: in item" );
            Intent intent = new Intent(this, Save_card_Activity.class);
            intent.putExtra("card", this.updateCard);
            intent.putExtra("isCardAdd", false);
            startActivityForResult(intent, Constants.ADD_CARD_UPDATE);
        } else if (menuItem.getItemId() == R.id.shareCard) {
            shareCard();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @SuppressLint("ResourceType")
    private void shareCard() {
        DialogShareBinding dialogShareBinding = (DialogShareBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_share, (ViewGroup) null, false);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogShareBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialogShareBinding.llShareText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.setType(HTTP.PLAIN_TEXT_TYPE);
                intent.putExtra("android.intent.extra.TEXT", "\nName:  " + Card_Edit_Activity.this.updateCard.getName() + "\nJob Title:  " + Card_Edit_Activity.this.updateCard.getJob_title() + "\nAddress:  " + Card_Edit_Activity.this.updateCard.getAddress() + "\nCompany:  " + Card_Edit_Activity.this.updateCard.getCompany() + "\nPhone:  " + Card_Edit_Activity.this.updateCard.getPhone() + "\nEmail:  " + Card_Edit_Activity.this.updateCard.getEmail() + "\nWebsite:  " + Card_Edit_Activity.this.updateCard.getWebsite());
                Card_Edit_Activity.this.startActivity(Intent.createChooser(intent, "Select App to Share Card"));
            }
        });
        dialogShareBinding.llShareCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                File file = new File(AppConstant.getCameraImage(Card_Edit_Activity.this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + Card_Edit_Activity.this.updateCard.getImage_name());
                Context context = Card_Edit_Activity.this.context;
                Uri uriForFile = FileProvider.getUriForFile(context, Card_Edit_Activity.this.context.getApplicationContext().getPackageName() + ".provider", file);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.setType("image/*");
                intent.addFlags(1);
                Card_Edit_Activity.this.startActivity(Intent.createChooser(intent, "Select App to Share Card"));
            }
        });
        dialogShareBinding.llShareCardWithText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                File file = new File(AppConstant.getCameraImage(Card_Edit_Activity.this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + Card_Edit_Activity.this.updateCard.getImage_name());
                Uri uriForFile = FileProvider.getUriForFile(Card_Edit_Activity.this.context, Card_Edit_Activity.this.context.getApplicationContext().getPackageName() + ".provider", file);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", "\nName:  " + Card_Edit_Activity.this.updateCard.getName() + "\nJob Title:  " + Card_Edit_Activity.this.updateCard.getJob_title() + "\nAddress:  " + Card_Edit_Activity.this.updateCard.getAddress() + "\nCompany:  " + Card_Edit_Activity.this.updateCard.getCompany() + "\nPhone:  " + Card_Edit_Activity.this.updateCard.getPhone() + "\nEmail:  " + Card_Edit_Activity.this.updateCard.getEmail() + "\nWebsite:  " + Card_Edit_Activity.this.updateCard.getWebsite());
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.setType("image/*");
                intent.addFlags(1);
                Card_Edit_Activity.this.startActivity(Intent.createChooser(intent, "Select App to Share Text and Image"));
            }
        });
        dialogShareBinding.llCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void openZoomImageDialog(String path) {
        ImageDialogBinding dialogDeleteBinding = (ImageDialogBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.image_dialog, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogDeleteBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialogDeleteBinding.ivZoomView.setImage(ImageSource.uri(path));
        dialogDeleteBinding.llCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void openWebsite() {
        if (!binding.etWebsite.getText().toString().trim().isEmpty()) {
            Uri parse = Uri.parse("http://" + binding.etWebsite.getText().toString());
            Intent intent = new Intent("android.intent.action.VIEW", parse);
            intent.addFlags(1208483840);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                startActivity(new Intent("android.intent.action.VIEW", parse));
            }
        }
    }


    public void openMap() {
        if (!binding.etAddress.getText().toString().trim().isEmpty()) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("geo:0,0?q=" + binding.etAddress.getText().toString().trim()));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        }
    }


    public void openSMS() {
        if (!binding.etPhone.getText().toString().trim().isEmpty()) {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + binding.etPhone.getText().toString().trim()));
            intent.putExtra("sms_body", "The SMS text");
            startActivity(intent);
        }
    }


    public void openCall() {
        if (!binding.etPhone.getText().toString().trim().isEmpty()) {
            Intent intent = new Intent("android.intent.action.DIAL");
            intent.setData(Uri.parse("tel:" + binding.etPhone.getText()));
            startActivity(intent);
        }
    }


    public void openEmail() {
        if (!binding.etEmailAddress.getText().toString().trim().isEmpty()) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("mailto:" + binding.etEmailAddress.getText().toString())));
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2405 && i2 == -1) {
            this.updateCard = (Business_Card) intent.getExtras().get("card");
            RequestManager with = Glide.with((FragmentActivity) this);
            with.load(AppConstant.getCameraImage(this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + this.updateCard.getImage_name()).into(binding.image);
            binding.setViewModel(this.updateCard);
            this.isupdate = true;
        }
    }

    public void onBackPressed() {
        if (this.isupdate.booleanValue()) {
            Intent intent = new Intent();
            intent.putExtra("card", this.updateCard);
            setResult(-1, intent);
            finish();
            return;
        }
        Card_Edit_Activity.this.finish();
    }
}
