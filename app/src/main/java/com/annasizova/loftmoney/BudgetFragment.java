package com.annasizova.loftmoney;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.annasizova.loftmoney.MainActivity.AUTH_TOKEN;

public class BudgetFragment extends Fragment implements ItemAdapterListener, ActionMode.Callback {

    private static final String TYPE = "type";
    private static final String PRICE_COLOR = "price_color";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemsAdapter itemsAdapter;
    private Api api;
    private ActionMode actionMode;

    public static final int REQUEST_CODE = 1001;


    public BudgetFragment() {
    }

    public static BudgetFragment newInstance(FragmentType fragmentType) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(PRICE_COLOR, fragmentType.getPriceColor());
        args.putString(TYPE, fragmentType.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ((LoftApp) getActivity().getApplication()).getApi();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);

        RecyclerView recyclerView = fragmentView.findViewById(R.id.recycler_view);
        swipeRefreshLayout = fragmentView.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });
        itemsAdapter = new ItemsAdapter(getArguments().getInt(PRICE_COLOR));
        itemsAdapter.setListener(this);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration decorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decorator.setDrawable(this.getResources().getDrawable(R.drawable.divider_line));
        recyclerView.addItemDecoration(decorator);

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AUTH_TOKEN, "");
            final int price = Integer.parseInt(data.getStringExtra("price"));
            final String name = data.getStringExtra("name");
            Call <Status> call = api.addItems(new AddItemRequest(price, name, getArguments().getString(TYPE)), token);
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    loadItems();
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {

                }
            });
        }
    }

    private void loadItems() {
        final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AUTH_TOKEN, "");
        Call <List<Item>> itemsResponseCall = api.getItems(getArguments().getString(TYPE), token);
        itemsResponseCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                swipeRefreshLayout.setRefreshing(false);
                itemsAdapter.clear();
                List <Item> itemsList = response.body();
                for (Item item : itemsList) {
                    itemsAdapter.addItem(item);
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemClick(final Item item, final int position) {
        if (itemsAdapter.isSelected(position)) {
            itemsAdapter.toggleItem(position);
            itemsAdapter.notifyDataSetChanged();
        }
        actionMode.setTitle(getContext().getResources().getString(R.string.selected, String.valueOf(itemsAdapter.getSelectedItemsCount())));
    }

    @Override
    public void onItemLongClick(final Item item, final int position) {
        itemsAdapter.toggleItem(position);
        itemsAdapter.notifyDataSetChanged();
        if (actionMode == null) {
            ((AppCompatActivity) getActivity()).startSupportActionMode(this);
        }
        actionMode.setTitle(getContext().getResources().getString(R.string.selected, String.valueOf(itemsAdapter.getSelectedItemsCount())));
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        actionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(R.menu.item_menu_remove, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.delete_menu_item) {
            showDialog();
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        itemsAdapter.clearSelections();
        itemsAdapter.notifyDataSetChanged();
        actionMode = null;
    }

    private void showDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.remove_confirmation)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItems();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void removeItems() {
        List<Integer> selectedIds = itemsAdapter.getSelectedItemIds();
        for (int selectedId : selectedIds) {
            removeItem(selectedId);
        }
        itemsAdapter.clearSelections();
        actionMode.setTitle(getContext().getResources().getString(R.string.selected, String.valueOf(itemsAdapter.getSelectedItemsCount())));
    }

    private void removeItem(final int selectedId) {
        final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AUTH_TOKEN, "");
        Call<Status> itemsRemoveCall = api.removeItem(selectedId, token);
        itemsRemoveCall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                loadItems();
                itemsAdapter.clearSelections();
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

            }
        });
    }
}
