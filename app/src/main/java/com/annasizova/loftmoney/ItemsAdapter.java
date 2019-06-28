package com.annasizova.loftmoney;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter <ItemsAdapter.ItemViewHolder> {

    private List <Item> itemList = new ArrayList<>();

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = View.inflate(viewGroup.getContext(), R.layout.item_view, null);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemViewHolder viewHolder, int i) {
        final Item item = itemList.get(i);
        viewHolder.bindItem(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem (final Item item) {
        itemList.add(0,item);
        notifyItemInserted(0);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView, priceView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.item_name);
            priceView = itemView.findViewById(R.id.item_price);
        }

        public void bindItem (final Item item) {
            nameView.setText(item.getName());
            priceView.setText(priceView.getContext().getResources().getString(R.string.price_template, String.valueOf(item.getPrice())));
        }
    }
}
