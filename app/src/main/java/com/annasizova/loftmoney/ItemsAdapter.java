package com.annasizova.loftmoney;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter <ItemsAdapter.ItemViewHolder> {

    private List <Item> itemList = new ArrayList<>();
    private int priceColor;
    private ItemAdapterListener itemAdapterListener;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public ItemsAdapter(int priceColor) {
        this.priceColor = priceColor;
    }

    public void setListener(ItemAdapterListener listener) {
        itemAdapterListener = listener;
    }

    public boolean isSelected(final int position) {
        return selectedItems.get(position);
    }


    public void toggleItem(final int position) {
        selectedItems.put(position, !selectedItems.get(position));
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = View.inflate(viewGroup.getContext(), R.layout.item_view, null);
        TextView priceView = itemView.findViewById(R.id.item_price);
        priceView.setTextColor(itemView.getContext().getResources().getColor(priceColor));
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemViewHolder viewHolder, int position) {
        final Item item = itemList.get(position);
        viewHolder.bindItem(item, selectedItems.get(position));
        viewHolder.setListener(item, itemAdapterListener, position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem (final Item item) {
        itemList.add(item);
        notifyItemInserted(itemList.size());
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

    public void clearSelections() {
        selectedItems.clear();
    }

    public List<Integer> getSelectedItemIds() {
        List<Integer> selectedIds = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (selectedItems.get(i)) {
                selectedIds.add(itemList.get(i).getId());
            }
        }
        return selectedIds;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView, priceView;
        private View itemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            nameView = itemView.findViewById(R.id.item_name);
            priceView = itemView.findViewById(R.id.item_price);
        }

        public void bindItem(final Item item, final boolean selected) {
            itemView.setSelected(selected);
            nameView.setText(item.getName());
            priceView.setText(priceView.getContext().getResources().getString(R.string.price_template, String.valueOf(item.getPrice())));
        }

        public void setListener(final Item item, final ItemAdapterListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(item, position);
                    return false;
                }
            });
        }
    }
}
