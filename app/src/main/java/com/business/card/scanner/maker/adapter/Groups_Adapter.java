package com.business.card.scanner.maker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.business.card.scanner.maker.model.Group_tab;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.database.Database;
import com.business.card.scanner.maker.databinding.ItemGroupBinding;
import com.business.card.scanner.maker.listener.ClickLisner;
import com.business.card.scanner.maker.listener.LongClickLisner;

import java.util.List;

public class Groups_Adapter extends RecyclerView.Adapter<Groups_Adapter.viewHolder> {
    ClickLisner clickListener;
    Context context;
    Database database;
    List<Group_tab> group_tbList;
    boolean isDialog;
    LongClickLisner longClickLisner;

    public Groups_Adapter(Context context2, List<Group_tab> list, ClickLisner clickLisner, LongClickLisner longClickLisner2, boolean z) {
        this.context = context2;
        this.group_tbList = list;
        this.clickListener = clickLisner;
        this.longClickLisner = longClickLisner2;
        this.isDialog = z;
    }

    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new viewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_group, viewGroup, false));
    }

    public void onBindViewHolder(viewHolder viewholder, int i) {
        viewholder.binding.setGroupname(this.group_tbList.get(i));
        viewholder.binding.txtGrpCount.setText(""+(i+1));
    }

    public int getItemCount() {
        return this.group_tbList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ItemGroupBinding binding;

        public viewHolder(View view) {
            super(view);
            this.binding = (ItemGroupBinding) DataBindingUtil.bind(view);
            view.setOnClickListener(view1 -> Groups_Adapter.this.clickListener.cardClick(viewHolder.this.getAdapterPosition()));
            view.setOnLongClickListener(view12 -> {
                Groups_Adapter.this.longClickLisner.longClickListner(viewHolder.this.getAdapterPosition());
                return false;
            });
        }
    }
}
