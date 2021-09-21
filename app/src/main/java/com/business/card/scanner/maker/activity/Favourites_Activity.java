package com.business.card.scanner.maker.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import com.business.card.scanner.maker.adapter.Cards_adapter;
import com.business.card.scanner.maker.model.Business_Card;
import com.business.card.scanner.maker.model.Group_tab;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.baseClass.BaseActivityBinding;
import com.business.card.scanner.maker.database.Database;
import com.business.card.scanner.maker.databinding.DialogDeleteBinding;
import com.business.card.scanner.maker.databinding.DialogShareBinding;
import com.business.card.scanner.maker.databinding.FavouritesListActivityBinding;
import com.business.card.scanner.maker.listener.ClickLisner;

import com.google.android.gms.measurement.api.AppMeasurementSdk;

import net.lingala.zip4j.util.InternalZipConstants;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Favourites_Activity extends BaseActivityBinding {
    Cards_adapter adapter;
    FavouritesListActivityBinding binding;
    Database database;
    int flag = 0;
    Group_tab gid;
    List<Business_Card> list = new ArrayList();
    String toolbarTitle;


    public void initMethods() {
    }

    public void onClick(View view) {
    }


    public void setOnClicks() {
    }


    public void setBinding() {
        this.binding = (FavouritesListActivityBinding) DataBindingUtil.setContentView(this, R.layout.favourites_list_activity);
        this.database = Database.getAppDatabase(this);
        //Ad_Global.setBannerAd(this, this.binding.llAdBack, this.binding.flAdplaceholder);
    }


    public void initVariable()  {
        this.flag = getIntent().getIntExtra("flag", 0);
        Group_tab group_tab = (Group_tab) getIntent().getParcelableExtra("groupid");
        this.gid = group_tab;
        int i = this.flag;
        if (i == 0) {
            this.toolbarTitle = "Favourites";
            try {
                this.list = this.database.database_dao().getFavData();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            SetNoData();
        } else if (i == 1) {
            this.toolbarTitle = group_tab.getGroup_tb().getGroup_name();
            try {
                this.list = this.database.database_dao().getFromGroup(this.gid.getGroup_tb().getGroup_id());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            SetNoData();
        }
    }


    public void setToolbar() {
        setSupportActionBar(this.binding.toolBar);
        getSupportActionBar().setTitle((CharSequence) this.toolbarTitle);
        this.binding.toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        this.binding.toolBar.setNavigationIcon((int) R.drawable.ic_baseline_arrow_back_24);
        this.binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Favourites_Activity.this.onBackPressed();
            }
        });
    }


    public void setAdapter() {
        this.adapter = new Cards_adapter(this, this.list, new ClickLisner() {
            public void cardClick(int i) {
            }

            public void editCard(final int i, View view) {
                PopupMenu popupMenu = new PopupMenu(Favourites_Activity.this.context, view);
                popupMenu.inflate(R.menu.fav_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.favaddContact:
                                Favourites_Activity.this.openAddContact(i);
                                return false;
                            case R.id.favdelete:
                                if (Favourites_Activity.this.flag == 0) {
                                    Favourites_Activity.this.openDeleteDialog(i);
                                    return false;
                                } else if (Favourites_Activity.this.flag != 1) {
                                    return false;
                                } else {
                                    Favourites_Activity.this.gid.setCount(Favourites_Activity.this.gid.getCount() - 1);
                                    Favourites_Activity.this.database.database_dao().removeGroup(Favourites_Activity.this.list.get(i).getId());
                                    Favourites_Activity.this.list.remove(i);
                                    Favourites_Activity.this.adapter.notifyItemRemoved(i);
                                    Favourites_Activity.this.onBackPressed();
                                    return false;
                                }
                            case R.id.favshare:
                                Favourites_Activity.this.shareCard(i);
                                return false;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        this.binding.rvCards.setLayoutManager(new LinearLayoutManager(this));
        this.binding.setClickItem(this.adapter);
        this.binding.rvCards.setAdapter(this.adapter);
    }


    @SuppressLint("ResourceType")
    public void openDeleteDialog(final int i) {
        DialogDeleteBinding dialogDeleteBinding = (DialogDeleteBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.dialog_delete, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogDeleteBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialogDeleteBinding.llOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                File file = new File(AppConstant.getCameraImage(Favourites_Activity.this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + Favourites_Activity.this.list.get(i).getImage_name());
                if (file.exists()) {
                    file.delete();
                }
                Favourites_Activity.this.database.database_dao().setFav(Favourites_Activity.this.list.get(i).getId(), "");
                Favourites_Activity.this.list.remove(i);
                Favourites_Activity.this.adapter.notifyItemRemoved(i);
                dialog.dismiss();
            }
        });
        dialogDeleteBinding.llCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    @SuppressLint("ResourceType")
    public void shareCard(int i) {
        final Business_Card business_Card = this.adapter.getList().get(i);
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
        dialogShareBinding.llCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialogShareBinding.llShareText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.setType(HTTP.PLAIN_TEXT_TYPE);
                intent.putExtra("android.intent.extra.TEXT", "\nName:  " + business_Card.getName() + "\nJob Title:  " + business_Card.getJob_title() + "\nAddress:  " + business_Card.getAddress() + "\nCompany:  " + business_Card.getCompany() + "\nPhone:  " + business_Card.getPhone() + "\nEmail:  " + business_Card.getEmail() + "\nWebsite:  " + business_Card.getWebsite());
                Favourites_Activity.this.startActivity(Intent.createChooser(intent, "Select App to Share Text and Image"));
            }
        });
        dialogShareBinding.llShareCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                File file = new File(AppConstant.getCameraImage(Favourites_Activity.this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + business_Card.getImage_name());
                Context context = Favourites_Activity.this.context;
                Uri uriForFile = FileProvider.getUriForFile(context, Favourites_Activity.this.context.getApplicationContext().getPackageName() + ".provider", file);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.setType("image/*");
                intent.addFlags(1);
                Favourites_Activity.this.startActivity(Intent.createChooser(intent, "Select App to Share Text and Image"));
            }
        });
        dialogShareBinding.llShareCardWithText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                File file = new File(AppConstant.getCameraImage(Favourites_Activity.this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + business_Card.getImage_name());
                Uri uriForFile = FileProvider.getUriForFile(Favourites_Activity.this.context, Favourites_Activity.this.context.getApplicationContext().getPackageName() + ".provider", file);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.setType(HTTP.PLAIN_TEXT_TYPE);
                intent.putExtra("android.intent.extra.TEXT", "\nName:  " + business_Card.getName() + "\nJob Title:  " + business_Card.getJob_title() + "\nAddress:  " + business_Card.getAddress() + "\nCompany:  " + business_Card.getCompany() + "\nPhone:  " + business_Card.getPhone() + "\nEmail:  " + business_Card.getEmail() + "\nWebsite:  " + business_Card.getWebsite());
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.setType("image/*");
                intent.addFlags(1);
                Favourites_Activity.this.startActivity(Intent.createChooser(intent, "Select App to Share Text and Image"));
            }
        });
    }


    public void openAddContact(int i) {
        Business_Card business_Card = this.list.get(i);
        Intent intent = new Intent("android.intent.action.INSERT");
        intent.setType("vnd.android.cursor.dir/raw_contact");
        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(AppMeasurementSdk.ConditionalUserProperty.NAME, business_Card.getName());
            intent.putExtra("email", business_Card.getEmail());
            intent.putExtra("phone", business_Card.getPhone());
            intent.putExtra("company", business_Card.getCompany());
            intent.putExtra("postal", business_Card.getAddress());
            startActivity(intent);
        }
    }

    public void SetNoData() {
        if (this.list.size() == 0) {
            this.binding.NoData.setVisibility(View.VISIBLE);
            Glide.with(this.context).load(R.drawable.ic_no_data).into(this.binding.icNoData);
            this.binding.rvCards.setVisibility(View.GONE);
            return;
        }
        this.binding.NoData.setVisibility(View.GONE);
        this.binding.rvCards.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("groupId", this.gid);
        setResult(Group_Activity.upadte, intent);
        super.onBackPressed();
    }
}
