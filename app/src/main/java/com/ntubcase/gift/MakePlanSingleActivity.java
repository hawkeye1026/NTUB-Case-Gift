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
    String[] add_giftlistItems = new String[getGiftList.getGiftLength()];
    boolean[] add_giftcheckedItems;
    ArrayList<Integer> add_giftItems = new ArrayList<>();
    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] add_friendlistItems = new String[getFriendList.getFriendLength()];
    boolean[] add_friendcheckedItems;
    ArrayList<Integer> add_friendItems = new ArrayList<>();
    //----------------------------------------------------------------------------------------------
    private static String[] giftid = new String[100];
    private static String[] friendid = new String[100];
    private static int giftidPositionIndex = 0 ;
    private static int friendidPositionIndex = 0 ;
    static EditText add_surprise_name;
    static EditText add_surprice_message;
    //----------------------------------------------------------------------------------------------
    ProgressDialog barProgressDialog;
    private RecyclerView recycler_view;
    private plan_single_adapter adapter;
    private ArrayList<String> mData = new ArrayList<>();
    private Button btnAdd;
    private Button btn_ent,btn_can;
    private EditText edt_single_giftName,edt_single_sentTime,edt_single_message;
    String single_giftName,single_sentTime,single_message;


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
        add_surprise_name = (EditText) findViewById(R.id.add_surprise_name);
        EditText add_surprise_date = findViewById(R.id.add_surprise_date);
//        EditText add_surprise_time = findViewById(R.id.add_surprise_time);
//        EditText add_surprise_gift = (EditText) findViewById(R.id.add_surprise_gift);
        EditText add_surprise_friend = (EditText) findViewById(R.id.add_surprise_friend);
        add_surprice_message = (EditText) findViewById(R.id.add_surprice_message);
        Button savePlan = (Button) findViewById(R.id.btn_plan_save);
        Button sendPlan = (Button) findViewById(R.id.btn_plan_send);

        //-----------送出計畫
        sendPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planUpdateAsyncTask mPlanInsertAsyncTask = new planUpdateAsyncTask(new planUpdateAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {
                    }
                });
                //---------------------------選擇日期
                EditText add_surprise_date = findViewById(R.id.add_surprise_date);//日期
                //EditText add_surprise_time = findViewById(R.id.add_surprise_time);//分秒
                //String sendPlanDate = add_surprise_date.getText().toString() +" "+   add_surprise_time.getText().toString();
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
        savePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planUpdateAsyncTask mPlanInsertAsyncTask = new planUpdateAsyncTask(new planUpdateAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {
                    }
                });
                //---------------------------選擇日期
                EditText add_surprise_date = findViewById(R.id.add_surprise_date);//日期
                //EditText add_surprise_time = findViewById(R.id.add_surprise_time);//分秒
                //String sendPlanDate = add_surprise_date.getText().toString() +" "+   add_surprise_time.getText().toString();
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
            add_giftlistItems[i] = getGiftList.getGiftName(i);
        }
        add_giftcheckedItems = new boolean[add_giftlistItems.length];
        //選擇好友使用的變數宣告--------------------------------------------------------------------------- 好友資料
        for(int i = 0; i < getFriendList.getFriendLength(); i++){
            add_friendlistItems[i] = getFriendList.getFriendName(i);
        }
        add_friendcheckedItems = new boolean[add_friendlistItems.length];


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
                btn_can = (Button)dialog.findViewById(R.id.btn_can);
                btn_can.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                //點選確認
                btn_ent = (Button)dialog.findViewById(R.id.btn_ent);
                btn_ent.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    //新增一個項目
                    public void onClick(View view) {

                        //抓取輸入方塊訊息

                        single_giftName = edt_single_giftName.getText().toString();
                        single_sentTime = edt_single_sentTime.getText().toString();
                        single_message = edt_single_message.getText().toString();
                        //在item內傳送文字
                        TextView txtItem =(TextView) findViewById(R.id.txtItem);
                        txtItem.setText(single_sentTime+"-"+single_giftName+single_message);


                        dialog.cancel();
                    }
                });
                dialog.show();
            }

        });

        // 準備資料，塞項目到ArrayList裡
        for(int i = 0; i < 10; i++) {
            mData.add("項目"+i);
        }

        // 連結元件
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        // 設置RecyclerView為列表型態
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        // 設置格線
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // 將資料交給adapter
        adapter = new plan_single_adapter(mData);
        // 設置adapter給recycler_view
        recycler_view.setAdapter(adapter);

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

        mBuilder.setMultiChoiceItems(add_giftlistItems, add_giftcheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    add_giftItems.add(position);
                    giftid[giftidPositionIndex] = getGiftList.getGiftid(position);
                    giftidPositionIndex++;
                }else{
                    add_giftItems.remove((Integer.valueOf(position)));
                    giftidPositionIndex--;
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < add_giftItems.size(); i++) {
                    item = item + add_giftlistItems[add_giftItems.get(i)];
                    if (i != add_giftItems.size() - 1) {
                        item = item + ",";
                    }
                }
                edt_single_giftName.setText(item);
            }
        });

        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton("取消全選", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                EditText add_surprise_gift = (EditText) findViewById(R.id.single_giftName);
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

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MakePlanSingleActivity.this);
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
                EditText add_surprise_friend = (EditText) findViewById(R.id.add_surprise_friend);
                for (int i = 0; i < add_friendItems.size(); i++) {
                    item = item + add_friendlistItems[add_friendItems.get(i)];
                    if (i != add_friendItems.size() - 1) {
                        item = item + ",";
                    }
                }
                add_surprise_friend.setText(item);
            }
        });

        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

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
