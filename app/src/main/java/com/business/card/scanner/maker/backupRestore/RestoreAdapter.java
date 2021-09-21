package com.business.card.scanner.maker.backupRestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.business.card.scanner.maker.R;

import java.util.ArrayList;

public class RestoreAdapter extends RecyclerView.Adapter<RestoreAdapter.ViewHolder> {
    private ArrayList<RestoreRowModel> arrayList;
    Context context;

    public OnRecyclerItemClick itemClick;

    public RestoreAdapter(Context context2, ArrayList<RestoreRowModel> arrayList2, OnRecyclerItemClick onRecyclerItemClick) {
        this.arrayList = arrayList2;
        this.context = context2;
        this.itemClick = onRecyclerItemClick;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.restore_item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        RestoreRowModel restoreRowModel = this.arrayList.get(i);
        viewHolder.txtFileName.setText(restoreRowModel.getTitle());
        viewHolder.txtFileDate.setText(restoreRowModel.getDateModified());
        viewHolder.txtFileSize.setText(restoreRowModel.getSize());
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDelete;
        TextView txtFileDate;
        TextView txtFileName;
        TextView txtFileSize;

        ViewHolder(View view) {
            super(view);
            this.txtFileName = (TextView) view.findViewById(R.id.txtName);
            this.txtFileDate = (TextView) view.findViewById(R.id.txtDate);
            this.txtFileSize = (TextView) view.findViewById(R.id.txtSize);
            this.imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            view.setOnClickListener(view1 -> RestoreAdapter.this.itemClick.onClick(ViewHolder.this.getAdapterPosition(), 1));
            this.imgDelete.setOnClickListener(view12 -> RestoreAdapter.this.itemClick.onClick(ViewHolder.this.getAdapterPosition(), 2));
        }
    }
}
