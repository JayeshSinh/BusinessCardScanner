package com.business.card.scanner.maker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;


import com.business.card.scanner.maker.adapter.Cards_adapter;
import com.business.card.scanner.maker.adapter.Groups_Adapter;
import com.business.card.scanner.maker.model.Business_Card;
import com.business.card.scanner.maker.model.Group_tab;
import com.business.card.scanner.maker.model.Group_tb;
import com.business.card.scanner.maker.util.AppPref;
import com.business.card.scanner.maker.util.Constants;
import com.business.card.scanner.maker.util.adBackScreenListener;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.baseClass.BaseActivityBinding;
import com.business.card.scanner.maker.database.Database;
import com.business.card.scanner.maker.databinding.ActivityMainBinding;
import com.business.card.scanner.maker.databinding.DialogAddGroupBinding;
import com.business.card.scanner.maker.databinding.DialogDeleteBinding;
import com.business.card.scanner.maker.databinding.DialogGroupBinding;
import com.business.card.scanner.maker.databinding.DialogShareBinding;
import com.business.card.scanner.maker.listener.ClickLisner;
import com.business.card.scanner.maker.listener.LongClickLisner;
import com.business.card.scanner.maker.listener.OnFragmentInteractionListener;


import net.lingala.zip4j.util.InternalZipConstants;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MainActivity extends BaseActivityBinding implements OnFragmentInteractionListener {
    public static final int RequestPermissionCode = 1;

    //public static AdManagerInterstitialAd admob_interstitial = null;
    //public static NativeAd desireNativeAd = null;
    public static int edit = 12;
    public static List<Group_tab> groupTabList = new ArrayList();
    public static boolean is_ad_failed = false;
    private static adBackScreenListener mAdBackScreenListener;
    public Cards_adapter adapter;
    ActivityMainBinding binding;
    public List<Business_Card> cardList = new ArrayList();
    Database database;
    Groups_Adapter groups_adapter;
   // public NativeAd nativeAd;
    SearchView searchView;
    int temppos;


    public void initVariable() {
    }

    public void onFragmentInteraction(Uri uri) { }

    public void setOnClicks() {}


    public void setToolbar() {
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setSupportActionBar(this.binding.toolBar);

    }

    public void onDestroy() {
       
        super.onDestroy();
    }


    public void setBinding() {
        this.binding = (ActivityMainBinding) DataBindingUtil.setContentView(this, R.layout.activity_main);
        Database appDatabase = Database.getAppDatabase(this);
        this.database = appDatabase;
        try {
            this.cardList = appDatabase.database_dao().getAll_Card();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        this.binding.setToolbarclick(this);

    }

    public void addDefultValue() {
        this.database.group_dao().insertGroup(new Group_tb("1", "Business", "true"));
        this.database.group_dao().insertGroup(new Group_tb(ExifInterface.GPS_MEASUREMENT_2D, "Family", "true"));
        this.database.group_dao().insertGroup(new Group_tb(ExifInterface.GPS_MEASUREMENT_3D, "VIP", "true"));
        this.database.group_dao().insertGroup(new Group_tb("4", "Colleagues", "true"));
        this.database.group_dao().insertGroup(new Group_tb("5", "Customers", "true"));
        this.database.group_dao().insertGroup(new Group_tb("6", "Office", "true"));
    }


    public void initMethods() {
        if (!AppPref.isDefault(this)) {
            addDefultValue();
            AppPref.setIsDefault(this, true);
        }
        groupTabList = this.database.group_dao().get_AllGroup();
        SetNoData();
    }


    public void setAdapter() {
        Cards_adapter cards_adapter = new Cards_adapter(this, this.cardList, new ClickLisner() {
            public void cardClick(int i) {
                new Business_Card();
                Business_Card onecard = MainActivity.this.database.database_dao().getOnecard(adapter.getList().get(i).getId());

                Log.e("onecard", "cardClick: onecard--> 😍 "+onecard );

                Intent intent = new Intent(MainActivity.this, Card_Edit_Activity.class);
                intent.putExtra("card", onecard);
                intent.putExtra("isCardAdd", false);
                MainActivity.this.startActivityForResult(intent, Constants.ADD_CARD_UPDATE);
            }

            public void editCard(final int i, View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.inflate(R.menu.edit_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.addContact:
                                MainActivity.this.openAddContact(i);
                                break;
                            case R.id.addGroup:
                                MainActivity.groupTabList.clear();
                                MainActivity.groupTabList.addAll(MainActivity.this.database.group_dao().get_AllGroup());
                                MainActivity.this.openGroupDialog(i);
                                break;
                            case R.id.delete:
                                MainActivity.this.openDeleteDialog(i);
                                break;
                            case R.id.edit:
                                new Business_Card();
                                Intent intent = new Intent(MainActivity.this, Save_card_Activity.class);
                                intent.putExtra("card", MainActivity.this.adapter.getList().get(i));
                                intent.putExtra("flag", 1);
                                MainActivity.this.startActivityForResult(intent, MainActivity.edit);
                                break;
                            case R.id.fav:
                                MainActivity.this.database.database_dao().setFav(MainActivity.this.adapter.getList().get(i).getId(), "true");
                                Toast.makeText(MainActivity.this.context, "Add To Favourites", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.share:
                                MainActivity.this.shareCard(i);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        this.adapter = cards_adapter;
        sortList(cards_adapter.descending_date);
        this.binding.rvCards.setLayoutManager(new LinearLayoutManager(this));
        this.binding.setRecyclerviewClick(this.adapter);
        this.binding.rvCards.setAdapter(this.adapter);
        SetNoData();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_card:
                if (checkPermission()) {
                    afterPermission();
                    return;
                } else {
                    requestPermission();
                    return;
                }
            case R.id.create:
                startActivityForResult(new Intent(this, Create_Activity.class), Constants.ADD_CARD);
                return;
            case R.id.mine:
                if (!this.searchView.isIconified()) {
                    this.searchView.setIconified(true);
                    this.searchView.onActionViewCollapsed();
                }
                startActivityForResult(new Intent(this, Mine_Activity.class), 1002);
                return;
            case R.id.ll_Favourite:
                startActivity(new Intent(this, Favourites_Activity.class));
                return;
            default:
                return;
        }
    }

    public void afterPermission() {
        Intent intent = new Intent(this, Camera_Activity.class);
        intent.putExtra("IsAddcard", true);
        startActivityForResult(intent, Constants.Camera_Retake);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.serch, menu);
        this.searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        this.searchView.setSearchableInfo(((SearchManager) getSystemService(SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) this.searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setBackground(getResources().getDrawable(R.drawable.rounded_shape_white));
        searchAutoComplete.setHint("Search here");
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.gnt_gray));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.background));
        this.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            public boolean onClose() {
                MainActivity.this.adapter.setList(MainActivity.this.cardList);
                return false;
            }
        });
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                MainActivity.this.adapter.getFilter().filter(str);
                return false;
            }

            public boolean onQueryTextChange(String str) {
                MainActivity.this.adapter.getFilter().filter(str);
                return false;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.company) {
            if (AppPref.isLast_first(this)) {
                sortList(this.adapter.Descending_company);
            } else {
                sortList(this.adapter.Ascending_company);
            }
            return true;
        }
        else if (itemId == R.id.date) {
            if (AppPref.isLast_first(this)) {
                sortList(this.adapter.descending_date);
            } else {
                sortList(this.adapter.Ascending_date);
            }
            return true;
        }
        else if (itemId != R.id.name) {
            return super.onOptionsItemSelected(menuItem);
        }
        else {
            if (AppPref.isLast_first(this)) {
                sortList(this.adapter.Descending_name);
            } else {
                sortList(this.adapter.Ascending_name);
            }
            return true;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO"}, 1);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1 && iArr.length > 0) {
            boolean z = false;
            boolean z2 = iArr[0] == 0;
            if (iArr[1] == 0) {
                z = true;
            }
            if (!z2 || !z) {
                Toast.makeText(this, "Camera Permission Required, To Scan Your Card..!", Toast.LENGTH_LONG).show();
            } else {
                afterPermission();
            }
        }
    }

    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.CAMERA") == 0 && ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") == 0;
    }

    public void SetNoData() {
        if (this.cardList.size() == 0) {
            this.binding.NoData.setVisibility(View.VISIBLE);
            Glide.with(this.context).load(R.drawable.ic_no_data).into(this.binding.icNoData);
            this.binding.rvCards.setVisibility(View.GONE);
            return;
        }
        this.binding.NoData.setVisibility(View.GONE);
        this.binding.rvCards.setVisibility(View.VISIBLE);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        List<Business_Card> list;
        super.onActivityResult(i, i2, intent);
        if (i != 1002) {
            if (i != 2401) {
                if (i != 2403) {
                    if (i != 2405) {
                        if (i == 2406) {
                            if (intent != null && i2 == -1) {
                                Business_Card business_Card = (Business_Card) intent.getExtras().get("QRcard");
                                if (this.adapter.isFilter) {
                                    this.cardList.add(business_Card);
                                }
                                this.adapter.getList().add(business_Card);
                                SetNoData();
                                sortList(this.adapter.descending_date);
                            }
                        } else {
                            return;
                        }
                    } else if (intent != null && i2 == -1) {
                        Business_Card business_Card2 = (Business_Card) intent.getExtras().get("card");
                        if (this.adapter.isFilter) {
                            List<Business_Card> list2 = this.cardList;
                            list2.set(list2.indexOf(business_Card2), business_Card2);
                        }
                        int indexOf = this.adapter.getList().indexOf(business_Card2);
                        this.adapter.getList().set(indexOf, business_Card2);
                        this.adapter.notifyItemChanged(indexOf);
                        return;
                    } else {
                        return;
                    }
                } else if (intent != null) {
                    Intent intent2 = new Intent(this, Save_card_Activity.class);
                    intent2.putExtra("imageuri", (Uri) intent.getExtras().get("imageuri"));
                    intent2.putExtra("isCardAdd", true);
                    startActivityForResult(intent2, Constants.ADD_CARD);
                    return;
                } else {
                    return;
                }
            }
            else if (intent != null && i2 == -1) {
                Business_Card business_Card3 = (Business_Card) intent.getExtras().get("card");
                boolean booleanExtra = intent.getBooleanExtra("isCardAdd", false);
                if (this.adapter.isFilter) {
                    this.cardList.add(business_Card3);
                }
                if (booleanExtra) {
                    this.adapter.getList().add(business_Card3);
                    this.adapter.notifyDataSetChanged();
                }
                SetNoData();
                sortList(this.adapter.descending_date);
                return;
            } else {
                return;
            }
        }
        if (i2 == -1 && intent != null && (list = this.cardList) != null) {
            list.clear();
            try {
                this.cardList.addAll(this.database.database_dao().getAll_Card());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            this.adapter.notifyDataSetChanged();
            SetNoData();
        }
    }





    public void openAddContact(int i) {
        Business_Card business_Card = this.adapter.getList().get(i);
        Intent intent = new Intent("android.intent.action.INSERT");
        intent.setType("vnd.android.cursor.dir/contact");
        intent.putExtra("name", business_Card.getName());
        intent.putExtra("email", business_Card.getEmail());
        intent.putExtra("phone", business_Card.getPhone());
        intent.putExtra("company", business_Card.getCompany());
        intent.putExtra("postal", business_Card.getAddress());
        startActivity(intent);
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
                MainActivity.this.startActivity(Intent.createChooser(intent, "Select App to Share Text and Image"));
            }
        });
        dialogShareBinding.llShareCard.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                File file = new File(AppConstant.getCameraImage(MainActivity.this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + business_Card.getImage_name());
                Context context = MainActivity.this.context;
                Uri uriForFile = FileProvider.getUriForFile(context, MainActivity.this.context.getApplicationContext().getPackageName() + ".provider", file);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.setType("image/*");
                intent.addFlags(1);
                MainActivity.this.startActivity(Intent.createChooser(intent, "Select App to Share Text and Image"));
            }
        });
        dialogShareBinding.llShareCardWithText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                File file = new File(AppConstant.getCameraImage(MainActivity.this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + business_Card.getImage_name());
                Uri uriForFile = FileProvider.getUriForFile(MainActivity.this.context, MainActivity.this.context.getApplicationContext().getPackageName() + ".provider", file);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.setType(HTTP.PLAIN_TEXT_TYPE);
                intent.putExtra("android.intent.extra.TEXT", "\nName:  " + business_Card.getName() + "\nJob Title:  " + business_Card.getJob_title() + "\nAddress:  " + business_Card.getAddress() + "\nCompany:  " + business_Card.getCompany() + "\nPhone:  " + business_Card.getPhone() + "\nEmail:  " + business_Card.getEmail() + "\nWebsite:  " + business_Card.getWebsite());
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                intent.setType("image/*");
                intent.addFlags(1);
                MainActivity.this.startActivity(Intent.createChooser(intent, "Select App to Share Text and Image"));
            }
        });
    }


    @SuppressLint("ResourceType")
    public void openDeleteDialog(final int i) {
        DialogDeleteBinding dialogDeleteBinding = (DialogDeleteBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_delete, (ViewGroup) null, false);
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
                File file = new File(AppConstant.getCameraImage(MainActivity.this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + MainActivity.this.adapter.getList().get(i).getImage_name());
                if (file.exists()) {
                    file.delete();
                }
                MainActivity.this.database.database_dao().delete(MainActivity.this.adapter.getList().get(i));
                if (MainActivity.this.adapter.isFilter) {
                    MainActivity.this.cardList.remove(MainActivity.this.cardList.indexOf(MainActivity.this.adapter.getList().get(i)));
                }
                MainActivity.this.adapter.getList().remove(i);
                MainActivity.this.adapter.notifyItemRemoved(i);
                MainActivity.this.SetNoData();
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
    public void openGroupDialog(final int i1) {
        DialogGroupBinding dialogGroupBinding = (DialogGroupBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_group, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogGroupBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialogGroupBinding.rvGroup.setLayoutManager(new LinearLayoutManager(this));
        Log.e("TAG", "openGroupDialog: groupTabList-->"+ groupTabList.size() );
        this.groups_adapter = new Groups_Adapter(this, groupTabList, new ClickLisner() {
            public void editCard(int i, View view) {
            }

            public void cardClick(int i) {
                Log.e("TAG", "cardClick: adapter.getList().size()---> "+adapter.getList().size() );
                Log.e("TAG", "cardClick: getGroup_tb().groupTabList()---> "+groupTabList.size() );
                Log.e("TAG", "cardClick: i---> "+i );
                MainActivity.this.database.database_dao().updateGroup(MainActivity.this.adapter.getList().get(i1).getId(), MainActivity.groupTabList.get(i).getGroup_tb().getGroup_id());
                MainActivity.this.adapter.getList().get(i1).setGroup_id(MainActivity.groupTabList.get(i).getGroup_tb().getGroup_id());
                MainActivity mainActivity = MainActivity.this;
                Toast.makeText(mainActivity, "Added to " + MainActivity.groupTabList.get(i).getGroup_tb().getGroup_name(), 0).show();
                MainActivity.this.groups_adapter.notifyItemChanged(i);
                dialog.dismiss();
            }
        }, new LongClickLisner() {
            public void longClickListner(int i) {
            }
        }, true);
        dialogGroupBinding.rvGroup.setAdapter(this.groups_adapter);
        this.groups_adapter.notifyDataSetChanged();
        dialogGroupBinding.llCreateNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.openAddNewDialog();
            }
        });
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
                    MainActivity.this.groups_adapter.notifyDataSetChanged();
                    MainActivity.this.database.group_dao().insertGroup(group_tb);
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

    public void sortList(Comparator<Business_Card> comparator) {
        Collections.sort(this.adapter.getList(), comparator);
        this.adapter.notifyDataSetChanged();
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!AppPref.IsRateUs(this)) {
            Splash_Activity.isRated = false;
            Constants.showDialogRate(this);
            AppPref.setRateUs(this, true);
        } else if (AppPref.IsRateUsNew(this) || !Splash_Activity.isRated) {
            super.onBackPressed();
        } else {
            Splash_Activity.isRated = false;
            Constants.showDialogRateAction(this);
        }
    }

    public static void BackScreen() {


    }

    public static void BackPressedAd(Activity activity, adBackScreenListener adbackscreenlistener) {
        mAdBackScreenListener = adbackscreenlistener;

        BackScreen();
    }


}
