package com.ntubcase.gift;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ntubcase.gift.Adapter.GiftPagerAdapter;

public class GiftActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        setTitle(R.string.mGift);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---TabLayout---
        ViewPager mViewpager = (ViewPager) findViewById(R.id.mViewpager);
        TabLayout giftTabLayout = (TabLayout) findViewById(R.id.giftTab);
        giftTabLayout.setupWithViewPager(mViewpager);
        setupViewPager(mViewpager);
    }

    //---------ViewPager初始化----------
    private void setupViewPager(ViewPager viewPager) {
        GiftPagerAdapter adapter = new GiftPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GiftListFragment(), "送禮區");
        adapter.addFragment(new GiftReceivedFragment(), "收禮區");
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
