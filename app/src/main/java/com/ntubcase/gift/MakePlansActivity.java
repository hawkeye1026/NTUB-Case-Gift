package com.ntubcase.gift;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.MakePlanAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class MakePlansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plans);
        setTitle("製作計畫");

        Intent intent = this.getIntent();
        String planType = intent.getStringExtra("planType"); //取得選擇的計畫種類

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //---------------------------------------------------------------------------------
        //選告變數------------------------------------------------------------------------------
        EditText add_surprise_name = (EditText) findViewById(R.id.add_surprise_name);
        EditText add_surprise_date = findViewById(R.id.add_surprise_date);
        EditText add_surprise_time = findViewById(R.id.add_surprise_time);
        Spinner add_surprise_gift = (Spinner) findViewById(R.id.add_surprise_gift);
        Spinner add_surprise_friend = (Spinner) findViewById(R.id.add_surprise_friend);
        EditText add_surprice_message = (EditText) findViewById(R.id.add_surprice_message);
        //------------------------------------------------------------------------------
        //選擇禮物------------------------------------------------------------------------
        final String[] surprise_addgift = {
                "選擇禮物", "母親卡", "父親卡", "蛋糕兌換劵", "生日影片",
                "出遊兌換劵", "生日卡"};
        ArrayList<Makeplan_StateVO> listVOs = new ArrayList<>();
        for (int i = 0; i < surprise_addgift.length; i++) {
            Makeplan_StateVO stateVO = new Makeplan_StateVO();
            stateVO.setTitle(surprise_addgift[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        MakePlanAdapter myAdapter = new MakePlanAdapter(MakePlansActivity.this, 0, listVOs);
        add_surprise_gift.setAdapter(myAdapter);
        //選擇好友------------------------------------------------------------------------
        final String[] surprise_addfriend = {
                "選擇好友", "陳源", "王父", "高家", "吳生",
                "蕭花", "呂昀"};
        ArrayList<Makeplan_StateVO> listfriend = new ArrayList<>();
        for (int i = 0; i < surprise_addgift.length; i++) {
            Makeplan_StateVO stateVO = new Makeplan_StateVO();
            stateVO.setTitle(surprise_addfriend[i]);
            stateVO.setSelected(false);
            listfriend.add(stateVO);
        }
        MakePlanAdapter myfriendAdapter = new MakePlanAdapter(MakePlansActivity.this, 0, listfriend);
        add_surprise_friend.setAdapter(myfriendAdapter);

        //點選送禮日期EditText跳出選擇日期選擇器---------------------------------------

        add_surprise_date.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_surprise_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        add_surprise_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog();
            }
        });
        //點選送禮日期EditText跳出選擇時間選擇器---------------------------------------

        add_surprise_time.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_surprise_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showTimePickerDialog();
                }
            }
        });

        add_surprise_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTimePickerDialog();
            }
        });


    }
    //設定送禮日期EditText傳入值---------------------------------------
    private void showDatePickerDialog () {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(MakePlansActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                EditText birthday = findViewById(R.id.add_surprise_date);
                birthday.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }
    //設定送禮時間EditText傳入值---------------------------------------
    private void showTimePickerDialog () {
        Calendar t = Calendar.getInstance();
        new TimePickerDialog(MakePlansActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                EditText add_surprise_time = findViewById(R.id.add_surprise_time);
                add_surprise_time.setText(hourOfDay + "：" + minute);

            }
        }, t.get(Calendar.HOUR_OF_DAY), t.get(Calendar.MINUTE),false).show();

    }
    //-----------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
