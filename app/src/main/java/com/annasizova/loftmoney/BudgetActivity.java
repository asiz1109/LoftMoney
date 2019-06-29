package com.annasizova.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class BudgetActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;
    private ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        itemsAdapter = new ItemsAdapter();
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration decorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decorator.setDrawable(this.getResources().getDrawable(R.drawable.divider_line));
        recyclerView.addItemDecoration(decorator);

        itemsAdapter.addItem(new Item("Молоко", 70));
        itemsAdapter.addItem(new Item("Зубная щетка", 70));
        itemsAdapter.addItem(new Item("Сковородка с антипригарным покрытием", 1670));

        Button openAddScreenButton = findViewById(R.id.open_add_screen);
        openAddScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BudgetActivity.this, AddItemActivity.class), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Item item = new Item(data.getStringExtra("name"), Integer.parseInt(data.getStringExtra("price")));
            itemsAdapter.addItem(item);
        }
    }
}
