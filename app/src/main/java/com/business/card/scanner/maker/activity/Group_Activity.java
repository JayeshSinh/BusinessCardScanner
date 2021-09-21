package com.business.card.scanner.maker.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.business.card.scanner.maker.adapter.Groups_Adapter;
import com.business.card.scanner.maker.model.Group_tab;
import com.business.card.scanner.maker.model.Group_tb;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.baseClass.BaseActivityBinding;
import com.business.card.scanner.maker.database.Database;
import com.business.card.scanner.maker.databinding.DialogAddGroupBinding;
import com.business.card.scanner.maker.databinding.DialogDeleteBinding;
import com.business.card.scanner.maker.databinding.DialogUpdateGroupBinding;
import com.business.card.scanner.maker.databinding.GroupActivityBinding;
import com.business.card.scanner.maker.listener.ClickLisner;
import com.business.card.scanner.maker.listener.LongClickLisner;


import java.util.UUID;

public class Group_Activity extends BaseActivityBinding {
    public static int upadte = 123;
    GroupActivityBinding binding;
    Database database;
    Groups_Adapter groups_adapter;


    public void initMethods() {
    }


    public void setOnClicks() {
    }


    public void setBinding() {
        GroupActivityBinding groupActivityBinding = (GroupActivityBinding) DataBindingUtil.setContentView(this, R.layout.group_activity);
        this.binding = groupActivityBinding;
        groupActivityBinding.setAddClick(this);
        this.database = Database.getAppDatabase(this);
        MainActivity.groupTabList.clear();
        //Ad_Global.setBannerAd(this, this.binding.llAdBack, this.binding.flAdplaceholder);
    }


    public void initVariable() {
        MainActivity.groupTabList = this.database.group_dao().get_AllGroup();
    }


    public void setToolbar() {
        setSupportActionBar(this.binding.toolBar);
        getSupportActionBar().setTitle((CharSequence) "Groups");
        this.binding.toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        this.binding.toolBar.setNavigationIcon((int) R.drawable.ic_baseline_arrow_back_24);
        this.binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Group_Activity.this.onBackPressed();
            }
        });
    }


    public void setAdapter() {
        this.groups_adapter = new Groups_Adapter(this, MainActivity.groupTabList, new ClickLisner() {
            public void editCard(int i, View view) {
            }

            public void cardClick(int i) {
                Intent intent = new Intent(Group_Activity.this, Favourites_Activity.class);
                intent.putExtra("flag", 1);
                intent.putExtra("groupid", MainActivity.groupTabList.get(i));
                Group_Activity.this.startActivityForResult(intent, Group_Activity.upadte);
            }
        }, new LongClickLisner() {
            public void longClickListner(int i) {
                if (MainActivity.groupTabList.get(i).getGroup_tb().getDefultcard().isEmpty()) {
                    Group_Activity.this.updateGroup(i);
                }
            }
        }, false);
        this.binding.rvGroup.setLayoutManager(new LinearLayoutManager(this));
        this.binding.rvGroup.setAdapter(this.groups_adapter);
    }


    @SuppressLint("ResourceType")
    public void updateGroup(final int i) {
        DialogUpdateGroupBinding dialogUpdateGroupBinding = (DialogUpdateGroupBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_update_group, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogUpdateGroupBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialogUpdateGroupBinding.llrename.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Group_Activity.this.updateGroupDialog(i);
                dialog.dismiss();
            }
        });
        dialogUpdateGroupBinding.lldelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Group_Activity.this.DeleteGroupDialog(i);
                dialog.dismiss();
            }
        });
    }


    @SuppressLint("ResourceType")
    public void updateGroupDialog(final int i) {
        final DialogAddGroupBinding dialogAddGroupBinding = (DialogAddGroupBinding) DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_add_group, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogAddGroupBinding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialogAddGroupBinding.etGroupName.setText(MainActivity.groupTabList.get(i).getGroup_tb().getGroup_name());
        dialogAddGroupBinding.llSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!dialogAddGroupBinding.etGroupName.getText().toString().isEmpty()) {
                    Group_tab group_tab = MainActivity.groupTabList.get(i);
                    Group_tb group_tb = new Group_tb();
                    group_tb.setGroup_id(group_tab.getGroup_tb().getGroup_id());
                    group_tb.setGroup_name(dialogAddGroupBinding.etGroupName.getText().toString());
                    group_tb.setDefultcard("");
                    group_tab.setGroup_tb(group_tb);
                    group_tab.setCount(group_tab.getCount());
                    MainActivity.groupTabList.set(i, group_tab);
                    Group_Activity.this.groups_adapter.notifyItemChanged(i);
                    Group_Activity.this.database.group_dao().updateGroup(group_tb);
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


    @SuppressLint("ResourceType")
    public void DeleteGroupDialog(final int i) {
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
        dialogDeleteBinding.llCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialogDeleteBinding.llOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Group_Activity.this.database.group_dao().deleteGroup(MainActivity.groupTabList.get(i).getGroup_tb());
                MainActivity.groupTabList.remove(i);
                Group_Activity.this.groups_adapter.notifyItemRemoved(i);
                Toast.makeText(Group_Activity.this.context, "Group Delete.", 0).show();
                dialog.dismiss();
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.addgroupiv) {
            openAddNewDialog();
        }
    }

    @SuppressLint("ResourceType")
    private void openAddNewDialog() {
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
                    Group_Activity.this.groups_adapter.notifyDataSetChanged();
                    Group_Activity.this.database.group_dao().insertGroup(group_tb);
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


    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == upadte) {
            Group_tab group_tab = (Group_tab) intent.getParcelableExtra("groupId");
            int indexOf = MainActivity.groupTabList.indexOf(group_tab);
            MainActivity.groupTabList.set(indexOf, group_tab);
            this.groups_adapter.notifyItemChanged(indexOf);
        }
        super.onActivityResult(i, i2, intent);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
