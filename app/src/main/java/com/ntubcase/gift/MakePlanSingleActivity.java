package com.ntubcase.gift;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.plan_single_adapter;
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

public class MakePlanSingleActivity extends AppCompatActivity {
    //選擇禮物 使用的變數宣告---------------------------------------------------------------------------
    String[] single_giftlistItems = new String[getGiftList.getGiftLength()];
    boolean[] single_giftcheckedItems,tempGiftChecked;
    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] single_friendlistItems = new String[getFriendList.getFriendLength()];
    boolean[] single_friendcheckedItems, tempFriendChecked;
    //----------------------------------------------------------------------------------------------
    private static String[] giftid = new String[100];
    private static String[] friendid = new String[100];
    private static int giftidPositionIndex = 0 ;
    private static int friendidPositionIndex = 0 ;
    static EditText edt_single_name, edt_single_message,edt_single_date,edt_single_friend,edt_single_giftName,edt_single_sentTime;
    //----------------------------------------------------------------------------------------------
    ProgressDialog barProgressDialog;
    private RecyclerView recycler_view;
    private plan_single_adapter adapter;
    private ArrayList<String> mData = new ArrayList<>();
    private Button btnAdd, btn_ent, btn_can, btn_save, btn_send;
    String single_giftName, single_sentTime, single_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan_single);
        setTitle(R.string.planSingle);

        //--------------------取得資料
        getGiftList.getJSON();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //---------------------------------------------------------------------------------
        //宣告變數------------------------------------------------------------------------------
        edt_single_name = findViewById(R.id.add_surprise_name);
        edt_single_date = findViewById(R.id.add_surprise_date);
        edt_single_friend = findViewById(R.id.add_surprise_friend);
        edt_single_message = findViewById(R.id.add_surprice_message);
        btn_save = findViewById(R.id.btn_plan_save);
        btn_send = findViewById(R.id.btn_plan_send);

        //-----------送出計畫
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planUpdateAsyncTask mPlanInsertAsyncTask = new planUpdateAsyncTask(new planUpdateAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {
                    }
                });
                //---------------------------選擇日期

                String sendPlanDate = edt_single_date.getText().toString() +" "+ edt_single_sentTime.getText().toString();
                //---------------------------目前時間
                Date date =new Date();
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String spCreateDate = sdFormat.format(date);

                mPlanInsertAsyncTask.execute(Common.insertPlan , giftid[0], edt_single_name.getText().toString() ,spCreateDate ,sendPlanDate,edt_single_message.getText().toString(),"1",friendid[0]);

                //-------------讀取時間-----------
                barProgressDialog = ProgressDialog.show(MakePlanSingleActivity.this,
                        "讀取中", "請等待...",true);
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            getPlanList.getJSON();
                            getGiftReceived.getJSON();
                            Thread.sleep(1000);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        finally{
                            barProgressDialog.dismiss();
//                            Intent intent;
//                            intent = new Intent(MakePlanMultipleActivity .this, loadingActivity.class);
//                            startActivity(intent);
                            finish();

                        }
                    }
                }).start();

                Toast.makeText(v.getContext(), "預送成功", Toast.LENGTH_SHORT).show();

            }
        });

        //-----------儲存計畫
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planUpdateAsyncTask mPlanInsertAsyncTask = new planUpdateAsyncTask(new planUpdateAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {
                    }
                });
                //---------------------------選擇日期
                String sendPlanDate = edt_single_date.getText().toString() +" "+ edt_single_sentTime.getText().toString();
                //---------------------------目前時間
                Date date =new Date();
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String spCreateDate = sdFormat.format(date);

                //mPlanInsertAsyncTask.execute(Common.insertPlan , giftid[0], add_surprise_name.getText().toString() ,spCreateDate ,sendPlanDate,add_surprice_message.getText().toString(),"1",friendid[0]);

                //-------------讀取時間-----------
                barProgressDialog = ProgressDialog.show(MakePlanSingleActivity.this,
                        "讀取中", "請等待...",true);
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            getPlanList.getJSON();
                            Thread.sleep(1000);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        finally{
                            barProgressDialog.dismiss();
                            /*Intent intent;
                            intent = new Intent(MakePlanMultipleActivity .this, loadingActivity.class);
                            startActivity(intent);*/
                            finish();

                        }
                    }
                }).start();

                Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
            }
        });


        //------------------------------------------------------------------------------
        //選擇禮物 使用的變數宣告-------------------------------------------------------------------------- 禮物資料
        for(int i = 0 ; i < getGiftList.getGiftLength();i++){
            single_giftlistItems[i] = getGiftList.getGiftName(i);
        }
        single_giftcheckedItems = new boolean[single_giftlistItems.length];
        tempGiftChecked = new boolean[single_giftlistItems.length];
        //選擇好友使用的變數宣告--------------------------------------------------------------------------- 好友資料
        for(int i = 0; i < getFriendList.getFriendLength(); i++){
            single_friendlistItems[i] = getFriendList.getFriendName(i);
        }
        single_friendcheckedItems = new boolean[single_friendlistItems.length];
        tempFriendChecked = new boolean[single_friendlistItems.length];

        //--------點選新增事件
        btnAdd =(Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //開啟對話視窗

                final Dialog dialog = new Dialog(MakePlanSingleActivity.this);
                dialog.setContentView(R.layout.single_dialog);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);//讓使用者點選背景或上一頁沒有用

                //宣告
                edt_single_giftName = dialog.findViewById(R.id.single_giftName);
                edt_single_sentTime = dialog.findViewById(R.id.single_sentTime);
                edt_single_message = dialog.findViewById(R.id.single_message);

                //點選送禮日期EditText跳出選擇時間選擇器---------------------------------------
                edt_single_sentTime.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
                edt_single_sentTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        // TODO Auto-generated method stub
                        if (hasFocus) {
                            showTimePickerDialog();
                        }
                    }
                });

                edt_single_sentTime.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        showTimePickerDialog();
                    }
                });
                //點選選擇禮物EditText跳出選擇好友選擇器------------------------------------------------------------------------
                edt_single_giftName.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
                edt_single_giftName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        // TODO Auto-generated method stub
                        if (hasFocus) {
                            Showgiftdialog();
                        }
                    }
                });
                edt_single_giftName.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Showgiftdialog();
                    }
                });

                //點選取消
                btn_can = dialog.findViewById(R.id.btn_can);
                btn_can.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                //點選確認
                btn_ent = dialog.findViewById(R.id.btn_ent);
                btn_ent.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    //新增一個項目
                    public void onClick(View view) {
                        //抓取輸入方塊訊息
                        single_giftName = edt_single_giftName.getText().toString();
                        single_sentTime = edt_single_sentTime.getText().toString();
                        single_message = edt_single_message.getText().toString();
                       //在item內傳送文字
                        adapter.addItem(single_sentTime+"-"+single_giftName+single_message);
                        dialog.cancel();
                    }
                });
                dialog.show();
            }

        });

        // 準備資料，塞項目到ArrayList裡
        for(int i = 0; i <0; i++) {
            mData.add("項目"+i);
        }

        // 連結元件
        recycler_view = findViewById(R.id.recycler_view);
        // 設置RecyclerView為列表型態
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        // 設置格線
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // 將資料交給adapter
        adapter = new plan_single_adapter(mData);
        // 設置adapter給recycler_view
        recycler_view.setAdapter(adapter);

        //點選選擇好友EditText跳出選擇好友選擇器------------------------------------------------------------------------
        edt_single_friend.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edt_single_friend.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Showfrienddialog();
                }
            }
        });
        edt_single_friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Showfrienddialog();
            }
        });
        //點選送禮日期EditText跳出選擇日期選擇器---------------------------------------
        edt_single_date.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edt_single_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        edt_single_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog();
            }
        });



    }

    protected void onDestroy() {
        giftidPositionIndex = 0;
        friendidPositionIndex = 0;
        super.onDestroy();
    }

    //設定選擇禮物EditText傳入值---------------------------------------
    private void Showgiftdialog(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MakePlanSingleActivity.this);
        mBuilder.setTitle("選擇禮物");

        mBuilder.setMultiChoiceItems(single_giftlistItems, single_giftcheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < single_giftcheckedItems.length; i++) {
                    if(single_giftcheckedItems[i]){
                        if (item.equals("")) item += single_giftlistItems[i];
                        else item += " , " + single_giftlistItems[i];
                    }
                    tempGiftChecked[i]=single_giftcheckedItems[i];
                }
                edt_single_giftName.setText(item);
            }
        });
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //取消鈕
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i=0; i<tempGiftChecked.length; i++) single_giftcheckedItems[i]=tempGiftChecked[i];
            }
        });

        mBuilder.setNeutralButton("清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {  //清除鈕
                for (int i = 0; i < single_giftcheckedItems.length; i++) {
                    single_giftcheckedItems[i] = false;
                    tempGiftChecked[i] = false;
                }
                edt_single_giftName.setText("");
            }
        });



        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    //設定選擇好友EditText傳入值---------------------------------------
    private void Showfrienddialog(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MakePlanSingleActivity.this);
        mBuilder.setTitle("選擇好友");
        mBuilder.setMultiChoiceItems(single_friendlistItems, single_friendcheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < single_friendcheckedItems.length; i++) {
                    if(single_friendcheckedItems[i]){
                        if (item.equals("")) item += single_friendlistItems[i];
                        else item += " , " + single_friendlistItems[i];
                    }
                    tempFriendChecked[i]=single_friendcheckedItems[i];
                }
                edt_single_friend.setText(item);
            }
        });

        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //取消鈕
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i=0; i<tempFriendChecked.length; i++) single_friendcheckedItems[i]=tempFriendChecked[i];
            }
        });

        mBuilder.setNeutralButton("清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {  //清除鈕
                for (int i = 0; i < single_friendcheckedItems.length; i++) {
                    single_friendcheckedItems[i] = false;
                    tempFriendChecked[i] = false;
                }
                edt_single_friend.setText("");
            }
        });


        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    //設定送禮日期EditText傳入值---------------------------------------
    private void showDatePickerDialog () {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(MakePlanSingleActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                EditText add_surprise_date = findViewById(R.id.add_surprise_date);
                String month;
                String day;

                add_surprise_date.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }
    //設定送禮時間EditText傳入值---------------------------------------
    private void showTimePickerDialog () {
        Calendar t = Calendar.getInstance();
        new TimePickerDialog(MakePlanSingleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                edt_single_sentTime.setText(dateAdd0(hourOfDay) + ":" + dateAdd0(minute));
            }
        }, t.get(Calendar.HOUR_OF_DAY), t.get(Calendar.MINUTE),false).show();

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
