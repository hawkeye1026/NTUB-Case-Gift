package com.ntubcase.gift;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.PagerAdapter;

public class PlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        setTitle(R.string.mPlan);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //---TabLayout---
        ViewPager mViewpager = (ViewPager) findViewById(R.id.mViewpager);
        TabLayout planTabLayout = (TabLayout) findViewById(R.id.planTab);
        planTabLayout.setupWithViewPager(mViewpager);
        setupViewPager(mViewpager);
    }

    //---------ViewPager初始化----------
    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new PlanNewFragment(), "未送出");
        adapter.addFragment(new PlanSentFragment(), "已預送");
        adapter.addFragment(new PlanDoneFragment(), "已送達");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar返回建
                finish();
                return true;
            case R.id.action_help:  //說明鈕
                Intent intent;
                intent = new Intent(this, HelpActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("from", "PlanActivity");
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
