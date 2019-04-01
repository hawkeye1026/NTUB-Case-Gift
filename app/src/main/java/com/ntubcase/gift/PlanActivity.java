package com.ntubcase.gift;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;

public class PlanActivity extends AppCompatActivity {

    private FloatingActionButton fab_surprise, fab_calendar, fab_qa;
    private FloatingActionMenu newPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        setTitle(R.string.mPlan);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //------------------------------FAB_newGift----------------------
        newPlan = (FloatingActionMenu) findViewById(R.id.newPlan);
        fab_surprise = (FloatingActionButton) findViewById(R.id.fab_surprise);
        fab_calendar = (FloatingActionButton) findViewById(R.id.fab_calendar);
        fab_qa = (FloatingActionButton) findViewById(R.id.fab_qa);
        fab_surprise.setOnClickListener(fabClickListener);
        fab_calendar.setOnClickListener(fabClickListener);
        fab_qa.setOnClickListener(fabClickListener);


    }

    // ----------------設定FAB的點擊監聽---------------
    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.fab_surprise:
                    intent = new Intent(PlanActivity.this, MakePlansActivity.class);
                    break;
                case R.id.fab_calendar:
                    intent = new Intent(PlanActivity.this, MakePlansActivity.class);
                    break;
                case R.id.fab_qa:
                    intent = new Intent(PlanActivity.this, MakePlansActivity.class);
                    break;
            }

            newPlan.close(true);
            startActivity(intent);*/
            Toast.makeText(PlanActivity.this,"新增計畫",Toast.LENGTH_SHORT).show();
        }
    };



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
