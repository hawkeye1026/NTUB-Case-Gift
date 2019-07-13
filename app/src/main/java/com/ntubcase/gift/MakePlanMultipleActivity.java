package com.ntubcase.gift;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.planUpdateAsyncTask;
import com.ntubcase.gift.data.getFriendList;
import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.data.getGiftReceived;
import com.ntubcase.gift.data.getPlanList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MakePlanMultipleActivity extends AppCompatActivity {

    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] friendItemList = new String[getFriendList.getFriendLength()];
    boolean[] mFriendChecked, tempFriendChecked;

    //----------------------------------------------------------------------------------------------
    private static String[] friendid = new String[100];
    private static int friendidPositionIndex = 0 ;
    private EditText add_multi_name;
    private EditText add_multi_message;
    private EditText add_multi_friend;
    private EditText add_multi_dateS ;
    private EditText add_multi_dateE ;
    //----------------------------------------------------------------------------------------------
    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan_multiple);
        setTitle(R.string.planMultiple);

        //--------------------取得資料
        getGiftList.getJSON();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //---------------------------------------------------------------------------------
        //宣告變數------------------------------------------------------------------------------
        add_multi_name = (EditText) findViewById(R.id.add_multi_name);
        add_multi_dateS = findViewById(R.id.add_multi_dateS);
        add_multi_dateE = findViewById(R.id.add_multi_dateE);
        add_multi_friend = (EditText) findViewById(R.id.add_multi_friend);
        add_multi_message = (EditText) findViewById(R.id.add_multi_message);

        //------------------------------------------------------------------------------

        //選擇好友使用的變數宣告--------------------------------------------------------------------------- 好友資料
        for(int i = 0; i < getFriendList.getFriendLength(); i++){
            friendItemList[i] = getFriendList.getFriendName(i);
        }
        mFriendChecked = new boolean[friendItemList.length];
        tempFriendChecked = new boolean[friendItemList.length];


        //點選選擇好友EditText跳出選擇好友選擇器------------------------------------------------------------------------
        add_multi_friend.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_multi_friend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Showfrienddialog();
                }
            }
        });
        add_multi_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Showfrienddialog();
            }
        });

        //點選送禮日期EditText跳出選擇日期選擇器---------------------------------------
        add_multi_dateS.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_multi_dateS.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDateSPickerDialog();
                }
            }
        });

        add_multi_dateS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDateSPickerDialog();
            }
        });

        //點選結束日期EditText跳出選擇日期選擇器---------------------------------------
        add_multi_dateE.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_multi_dateE.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDateEPickerDialog();
                }
            }
        });

        add_multi_dateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDateEPickerDialog();
            }
        });


        //-----------下一步------------
        Button btn_next = (Button) findViewById(R.id.btn_plan_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MakePlanMultipleActivity .this, PlanMultipleActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("planName", add_multi_name.getText().toString());
                bundle.putString("receiveFriend", add_multi_friend.getText().toString());
                bundle.putString("startDate", add_multi_dateS.getText().toString());
                bundle.putString("endDate", add_multi_dateE.getText().toString());
                bundle.putString("message", add_multi_message.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    protected void onDestroy() {
        friendidPositionIndex = 0;
        super.onDestroy();
    }

    //設定選擇好友EditText傳入值---------------------------------------
    private void Showfrienddialog(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MakePlanMultipleActivity.this);
        mBuilder.setTitle("選擇好友");
        mBuilder.setMultiChoiceItems(friendItemList, mFriendChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) { //確認鈕
                String mSelectFriends = "";
                for (int i = 0; i < mFriendChecked.length; i++) {
                    if(mFriendChecked[i]){
                        if (mSelectFriends.equals("")) mSelectFriends += friendItemList[i];
                        else mSelectFriends += " , " + friendItemList[i];
                    }
                    tempFriendChecked[i]=mFriendChecked[i];
                }
                add_multi_friend.setText(mSelectFriends);
            }
        });

        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //取消鈕
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i=0; i<tempFriendChecked.length; i++) mFriendChecked[i]=tempFriendChecked[i];
            }
        });

        mBuilder.setNeutralButton("清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {  //清除鈕
                for (int i = 0; i < mFriendChecked.length; i++) {
                    mFriendChecked[i] = false;
                    tempFriendChecked[i] = false;
                }
                add_multi_friend.setText("");
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //設定送禮日期EditText傳入值---------------------------------------
    private void showDateSPickerDialog () {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(MakePlanMultipleActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                add_multi_dateS.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    //設定結束日期EditText傳入值---------------------------------------
    private void showDateEPickerDialog () {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(MakePlanMultipleActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                add_multi_dateE.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    //-----------------------------------------------------------
    public String dateAdd0(int date){
        if(date <10){
            return "0"+date;
        }else{
            return String.valueOf(date);
        }
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
