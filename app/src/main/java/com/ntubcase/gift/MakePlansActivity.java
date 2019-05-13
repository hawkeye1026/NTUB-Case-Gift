package com.ntubcase.gift;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class MakePlansActivity extends AppCompatActivity {
    //選擇禮物 使用的變數宣告---------------------------------------------------------------------------
    String[] add_giftlistItems;
    boolean[] add_giftcheckedItems;
    ArrayList<Integer> add_giftItems = new ArrayList<>();
    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] add_friendlistItems;
    boolean[] add_friendcheckedItems;
    ArrayList<Integer> add_friendItems = new ArrayList<>();
    //----------------------------------------------------------------------------------------------
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
        //宣告變數------------------------------------------------------------------------------
        EditText add_surprise_name = (EditText) findViewById(R.id.add_surprise_name);
        EditText add_surprise_date = findViewById(R.id.add_surprise_date);
        EditText add_surprise_time = findViewById(R.id.add_surprise_time);
        EditText add_surprise_gift = (EditText) findViewById(R.id.add_surprise_gift);
        EditText add_surprise_friend = (EditText) findViewById(R.id.add_surprise_friend);
        EditText add_surprice_message = (EditText) findViewById(R.id.add_surprice_message);
        //------------------------------------------------------------------------------
        //選擇禮物 使用的變數宣告-------------------------------------------------------------------------- 禮物資料
        add_giftlistItems = getResources().getStringArray(R.array.gift_item);
        add_giftcheckedItems = new boolean[add_giftlistItems.length];
        //選擇好友使用的變數宣告--------------------------------------------------------------------------- 好友資料
        add_friendlistItems = getResources().getStringArray(R.array.friend_item);
        add_friendcheckedItems = new boolean[add_friendlistItems.length];
        //點選選擇禮物EditText跳出選擇禮物選擇器------------------------------------------------------------------------
        add_surprise_gift.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_surprise_gift.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Showgiftdialog();
                }
            }
        });

        add_surprise_gift.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Showgiftdialog();
            }
        });
        //點選選擇好友EditText跳出選擇好友選擇器------------------------------------------------------------------------
        add_surprise_friend.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_surprise_friend.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Showfrienddialog();
                }
            }
        });
        add_surprise_friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Showfrienddialog();
            }
        });
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


    //設定選擇禮物EditText傳入值---------------------------------------
    private void Showgiftdialog(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MakePlansActivity.this);
        mBuilder.setTitle("選擇禮物");
        mBuilder.setMultiChoiceItems(add_giftlistItems, add_giftcheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    add_giftItems.add(position);
                }else{
                    add_giftItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                EditText add_surprise_gift = (EditText) findViewById(R.id.add_surprise_gift);
                for (int i = 0; i < add_giftItems.size(); i++) {
                    item = item + add_giftlistItems[add_giftItems.get(i)];
                    if (i != add_giftItems.size() - 1) {
                        item = item + ", ";
                    }
                }
                add_surprise_gift.setText(item);
            }
        });

//        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                EditText add_surprise_gift = (EditText) findViewById(R.id.add_surprise_gift);
                for (int i = 0; i < add_giftcheckedItems.length; i++) {
                    add_giftcheckedItems[i] = false;
                    add_giftItems.clear();
                    add_surprise_gift.setText("");
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    //設定選擇好友EditText傳入值---------------------------------------
    private void Showfrienddialog(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MakePlansActivity.this);
        mBuilder.setTitle("選擇好友");
        mBuilder.setMultiChoiceItems(add_friendlistItems, add_friendcheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    add_friendItems.add(position);
                }else{
                    add_friendItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                EditText add_surprise_friend = (EditText) findViewById(R.id.add_surprise_friend);
                for (int i = 0; i < add_friendItems.size(); i++) {
                    item = item + add_friendlistItems[add_friendItems.get(i)];
                    if (i != add_friendItems.size() - 1) {
                        item = item + ", ";
                    }
                }
                add_surprise_friend.setText(item);
            }
        });

//        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                EditText add_surprise_friend = (EditText) findViewById(R.id.add_surprise_friend);
                for (int i = 0; i < add_friendcheckedItems.length; i++) {
                    add_friendcheckedItems[i] = false;
                    add_friendItems.clear();
                    add_surprise_friend.setText("");
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    //設定送禮日期EditText傳入值---------------------------------------
    private void showDatePickerDialog () {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(MakePlansActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                EditText add_surprise_date = findViewById(R.id.add_surprise_date);
                add_surprise_date.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
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
