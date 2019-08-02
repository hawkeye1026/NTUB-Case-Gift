package com.ntubcase.gift;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ntubcase.gift.Adapter.PagerAdapter;

public class GiftReceivedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_received);
        setTitle(R.string.mReceivedGift);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---TabLayout---
        ViewPager mViewpager = (ViewPager) findViewById(R.id.mViewpager);
        TabLayout giftTabLayout = (TabLayout) findViewById(R.id.rGiftTab);
        giftTabLayout.setupWithViewPager(mViewpager);
        setupViewPager(mViewpager);
    }

    //---------ViewPager初始化----------
    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new GiftReceivedNewFragment(), "新禮物");
        //adapter.addFragment(new GiftReceivedProcessFragment(), "進行中禮物");
        adapter.addFragment(new GiftReceivedDoneFragment(), "已接收禮物");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
