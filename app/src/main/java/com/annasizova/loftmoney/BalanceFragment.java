package com.annasizova.loftmoney;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.annasizova.loftmoney.MainActivity.AUTH_TOKEN;

public class BalanceFragment extends Fragment {

    private DiagramView diagramView;
    private Api api;
    private TextView totalMoney, expenseMoney, incomeMoney;

    public static BalanceFragment newInstance() {
        BalanceFragment fragment = new BalanceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ((LoftApp) getActivity().getApplication()).getApi();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_balance, container, false);
        diagramView = fragmentView.findViewById(R.id.diagramView);
        totalMoney = fragmentView.findViewById(R.id.total_money);
        expenseMoney = fragmentView.findViewById(R.id.expense_money);
        incomeMoney = fragmentView.findViewById(R.id.income_money);

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadBalance();
    }

    private void loadBalance() {
        final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AUTH_TOKEN, "");
        Call<BalanceResponse> balanceResponseCall = api.getBalance(token);
        balanceResponseCall.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(Call<BalanceResponse> call, Response<BalanceResponse> response) {
                totalMoney.setText(getString(R.string.price_template, String.valueOf(response.body().getTotalIncome() - response.body().getTotalExpense())));
                expenseMoney.setText(getString(R.string.price_template, String.valueOf(response.body().getTotalExpense())));
                incomeMoney.setText(getString(R.string.price_template, String.valueOf(response.body().getTotalIncome())));
                diagramView.update(response.body().getTotalExpense(), response.body().getTotalIncome());
            }

            @Override
            public void onFailure(Call<BalanceResponse> call, Throwable t) {
            }
        });
    }
}
