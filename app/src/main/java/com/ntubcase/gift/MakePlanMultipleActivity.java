package com.ntubcase.gift;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import com.ntubcase.gift.data.getFriendList;
import com.ntubcase.gift.data.getGiftList;

import java.util.ArrayList;
import java.util.Calendar;

public class MakePlanMultipleActivity extends AppCompatActivity {

    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] add_friendlistItems = new String[getFriendList.getFriendLength()];
    boolean[] add_friendcheckedItems;
    ArrayList<Integer> add_friendItems = new ArrayList<>();
    //----------------------------------------------------------------------------------------------
    private static String[] friendid = new String[100];
    private static int friendidPositionIndex = 0 ;
    private EditText add_surprise_name;
    private EditText add_surprice_message;
    private EditText add_surprise_friend;
    private EditText add_surprise_dateS ;
    private EditText add_surprise_dateE ;
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
        add_surprise_name = (EditText) findViewById(R.id.add_surprise_name);
        add_surprise_dateS = findViewById(R.id.add_surprise_dateS);
        add_surprise_dateE = findViewById(R.id.add_surprise_dateE);
        add_surprise_friend = (EditText) findViewById(R.id.add_surprise_friend);
        add_surprice_message = (EditText) findViewById(R.id.add_surprice_message);

        //------------------------------------------------------------------------------

        //選擇好友使用的變數宣告--------------------------------------------------------------------------- 好友資料
        for(int i = 0; i < getFriendList.getFriendLength(); i++){
            add_friendlistItems[i] = getFriendList.getFriendName(i);
        }
        add_friendcheckedItems = new boolean[add_friendlistItems.length];

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
        add_surprise_dateS.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_surprise_dateS.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDateSPickerDialog();
                }
            }
        });

        add_surprise_dateS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDateSPickerDialog();
            }
        });

        //點選結束日期EditText跳出選擇日期選擇器---------------------------------------
        add_surprise_dateE.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_surprise_dateE.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDateEPickerDialog();
                }
            }
        });

        add_surprise_dateE.setOnClickListener(new View.OnClickListener() {
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
                bundle.putString("planName", add_surprise_name.getText().toString());
                bundle.putString("receiveFriend", add_surprise_friend.getText().toString());
                bundle.putString("startDate", add_surprise_dateS.getText().toString());
                bundle.putString("endDate", add_surprise_dateE.getText().toString());
                bundle.putString("message", add_surprice_message.getText().toString());
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
        mBuilder.setMultiChoiceItems(add_friendlistItems, add_friendcheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    add_friendItems.add(position);
                    friendid[friendidPositionIndex] = getFriendList.getFriendid(position);
                    Log.v("friend",friendid[0]);
                    friendidPositionIndex++;
                }else{
                    add_friendItems.remove((Integer.valueOf(position)));
                    friendidPositionIndex--;
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                add_surprise_friend = (EditText) findViewById(R.id.add_surprise_friend);
                for (int i = 0; i < add_friendItems.size(); i++) {
                    item = item + add_friendlistItems[add_friendItems.get(i)];
                    if (i != add_friendItems.size() - 1) {
                        item = item + " , ";
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
    private void showDateSPickerDialog () {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(MakePlanMultipleActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                add_surprise_dateS.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    //設定結束日期EditText傳入值---------------------------------------
    private void showDateEPickerDialog () {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(MakePlanMultipleActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                add_surprise_dateE.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
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
