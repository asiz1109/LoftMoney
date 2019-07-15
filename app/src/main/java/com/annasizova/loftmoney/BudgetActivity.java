package com.annasizova.loftmoney;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static com.annasizova.loftmoney.BudgetFragment.REQUEST_CODE;

public class BudgetActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BudgetViewPagerAdapter viewPagerAdapter;
    private FloatingActionButton floatingActionButton;
    private Window window;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        window = BudgetActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(this);

        viewPagerAdapter = new BudgetViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.outcome);
        tabLayout.getTabAt(1).setText(R.string.income);
        tabLayout.getTabAt(2).setText(R.string.balance);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));

        floatingActionButton = findViewById(R.id.fab_open_add_screen);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                for (Fragment fragment : fragmentManager.getFragments()){
                    if (fragment.getUserVisibleHint())
                    fragment.startActivityForResult(new Intent(BudgetActivity.this, AddItemActivity.class), REQUEST_CODE);
                }
            }
        });

    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey_blue));
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey_blue));
        floatingActionButton.hide();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.action_mode_status_bar));

    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        floatingActionButton.show();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i == 2) {
            floatingActionButton.hide();
        } else {
            floatingActionButton.show();
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    static class BudgetViewPagerAdapter extends FragmentPagerAdapter {

        public BudgetViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return BudgetFragment.newInstance(FragmentType.expense);
                case 1:
                    return BudgetFragment.newInstance(FragmentType.income);
                case 2:
                    return BalanceFragment.newInstance();
            }
                return null;
            }

            @Override
            public int getCount () {
                return 3;
            }
        }
    }
