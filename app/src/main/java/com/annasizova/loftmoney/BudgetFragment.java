package com.annasizova.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BudgetFragment extends Fragment {

    private static final String PRICE_COLOR = "price_color";
    private ItemsAdapter itemsAdapter;
    public static final int REQUEST_CODE = 1001;

    public BudgetFragment() {
    }

    public static BudgetFragment newInstance(int priceColor) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(PRICE_COLOR, priceColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment_view = inflater.inflate(R.layout.fragment_budget, container, false);

        RecyclerView recyclerView = fragment_view.findViewById(R.id.recycler_view);
        itemsAdapter = new ItemsAdapter(getArguments().getInt(PRICE_COLOR));
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration decorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decorator.setDrawable(this.getResources().getDrawable(R.drawable.divider_line));
        recyclerView.addItemDecoration(decorator);

        itemsAdapter.addItem(new Item("Молоко", 70));
        itemsAdapter.addItem(new Item("Зубная щетка", 70));
        itemsAdapter.addItem(new Item("Сковородка с антипригарным покрытием", 1670));

        Button openAddScreenButton = fragment_view.findViewById(R.id.open_add_screen);
        openAddScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddItemActivity.class), REQUEST_CODE);
            }
        });

        return fragment_view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Item item = new Item(data.getStringExtra("name"), Integer.parseInt(data.getStringExtra("price")));
            itemsAdapter.addItem(item);
        }
    }
}
