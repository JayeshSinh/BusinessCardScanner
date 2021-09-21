package com.business.card.scanner.maker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.business.card.scanner.maker.model.Group_tab;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.databinding.CustomRadioRowBinding;
import com.business.card.scanner.maker.listener.ClickLisner;

import java.util.List;

public class RGroupAdapter extends RecyclerView.Adapter<RGroupAdapter.viewHolder> {
    ClickLisner clickListener;
    Context context;
    List<Group_tab> group_tbList;

    public RGroupAdapter(Context context2, List<Group_tab> list, ClickLisner clickLisner) {
        this.context = context2;
        this.group_tbList = list;
        this.clickListener = clickLisner;
    }

    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new viewHolder(LayoutInflater.from(this.context).inflate(R.layout.custom_radio_row, viewGroup, false));
    }

    public void onBindViewHolder(viewHolder viewholder, int i) {
        if (this.group_tbList.get(i).getCheck().booleanValue()) {
            viewholder.binding.groupName.setChecked(true);
            viewholder.binding.groupName.setBackgroundResource(R.drawable.tick);
        } else {
            viewholder.binding.groupName.setChecked(false);
            viewholder.binding.groupName.setBackgroundResource(R.drawable.button);
        }
        viewholder.binding.setGroupname(this.group_tbList.get(i));
    }

    public int getItemCount() {
        return this.group_tbList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        CustomRadioRowBinding binding;

        public viewHolder(View view) {
            super(view);
            this.binding = (CustomRadioRowBinding) DataBindingUtil.bind(view);
            view.setOnClickListener(view1 -> RGroupAdapter.this.clickListener.cardClick(viewHolder.this.getAdapterPosition()));
        }
    }
}
