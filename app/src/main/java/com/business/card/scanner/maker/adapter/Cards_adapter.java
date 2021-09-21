package com.business.card.scanner.maker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.business.card.scanner.maker.model.Business_Card;
import com.business.card.scanner.maker.R;
import com.business.card.scanner.maker.activity.AppConstant;
import com.business.card.scanner.maker.databinding.RowItemBinding;
import com.business.card.scanner.maker.listener.ClickLisner;

import net.lingala.zip4j.util.InternalZipConstants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Cards_adapter extends RecyclerView.Adapter<Cards_adapter.ViewHolder> implements Filterable {
    public Comparator<Business_Card> Ascending_company = new Comparator<Business_Card>() {
        public int compare(Business_Card business_Card, Business_Card business_Card2) {
            return business_Card.getCompany().toUpperCase().compareTo(business_Card2.getCompany().toUpperCase());
        }
    };
    public Comparator<Business_Card> Ascending_date = new Comparator<Business_Card>() {
        public int compare(Business_Card business_Card, Business_Card business_Card2) {
            return Long.compare(business_Card.getDate().longValue(), business_Card2.getDate().longValue());
        }
    };
    public Comparator<Business_Card> Ascending_name = new Comparator<Business_Card>() {
        public int compare(Business_Card business_Card, Business_Card business_Card2) {
            return business_Card.getName().toUpperCase().compareTo(business_Card2.getName().toUpperCase());
        }
    };
    public Comparator<Business_Card> Descending_company = new Comparator<Business_Card>() {
        public int compare(Business_Card business_Card, Business_Card business_Card2) {
            return business_Card2.getCompany().toUpperCase().compareTo(business_Card.getCompany().toUpperCase());
        }
    };
    public Comparator<Business_Card> Descending_name = new Comparator<Business_Card>() {
        public int compare(Business_Card business_Card, Business_Card business_Card2) {
            return business_Card2.getName().toUpperCase().compareTo(business_Card.getName().toUpperCase());
        }
    };
    List<Business_Card> cardList;
    ClickLisner clickLisner;
    Context context;
    public Comparator<Business_Card> descending_date = new Comparator<Business_Card>() {
        public int compare(Business_Card business_Card, Business_Card business_Card2) {
            return Long.compare(business_Card2.getDate().longValue(), business_Card.getDate().longValue());
        }
    };
    List<Business_Card> filteredList;
    public boolean isFilter = false;

    public Cards_adapter(Context context2, List<Business_Card> list, ClickLisner clickLisner2) {
        this.context = context2;
        this.cardList = list;
        this.clickLisner = clickLisner2;
        this.filteredList = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder((RowItemBinding) DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.row_item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.binding.setCards(this.filteredList.get(i));
        if (this.filteredList.get(i).getImage_name() == null) {
            Glide.with(this.context).load(Integer.valueOf(R.drawable.big_image)).into(viewHolder.binding.ivCard);
            return;
        }
        RequestManager with = Glide.with(this.context);
        ((RequestBuilder) with.load(AppConstant.getCameraImage(this.context) + InternalZipConstants.ZIP_FILE_SEPARATOR + this.filteredList.get(i).getImage_name()).error((int) R.drawable.defult_card2)).into(viewHolder.binding.ivCard);
    }

    public List<Business_Card> getList() {
        return this.filteredList;
    }

    public void setList(List<Business_Card> list) {
        this.filteredList = list;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return this.filteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {

            public FilterResults performFiltering(CharSequence charSequence) {
                String charSequence2 = charSequence.toString();
                if (charSequence2.isEmpty()) {
                    Cards_adapter cards_adapter = Cards_adapter.this;
                    cards_adapter.filteredList = cards_adapter.cardList;
                    Cards_adapter.this.isFilter = false;
                } else {
                    Cards_adapter.this.isFilter = true;
                    ArrayList arrayList = new ArrayList();
                    for (Business_Card next : Cards_adapter.this.cardList) {
                        if (next.getName().toLowerCase().contains(charSequence2.toLowerCase()) || next.getCompany().toLowerCase().contains(charSequence.toString())) {
                            arrayList.add(next);
                        }
                    }
                    Cards_adapter.this.filteredList = arrayList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = Cards_adapter.this.filteredList;
                return filterResults;
            }


            public void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Cards_adapter.this.filteredList = (ArrayList) filterResults.values;
                Cards_adapter.this.notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowItemBinding binding;

        public ViewHolder(RowItemBinding rowItemBinding) {
            super(rowItemBinding.getRoot());
            this.binding = rowItemBinding;
            rowItemBinding.click.setOnClickListener(this);
            this.binding.ivMenu.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (view.getId() == R.id.click) {
                Cards_adapter.this.clickLisner.cardClick(getAdapterPosition());
            }
            if (view.getId() == R.id.ivMenu) {
                Cards_adapter.this.clickLisner.editCard(getAdapterPosition(), view);
            }
        }
    }
}
