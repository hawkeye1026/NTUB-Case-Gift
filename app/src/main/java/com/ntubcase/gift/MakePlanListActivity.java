package com.ntubcase.gift;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.ntubcase.gift.Adapter.plan_list_adapter;
import com.ntubcase.gift.Adapter.plan_single_adapter;
import com.ntubcase.gift.data.getFriendList;
import com.ntubcase.gift.data.getGiftList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakePlanListActivity extends AppCompatActivity{
    //選擇禮物 使用的變數宣告---------------------------------------------------------------------------
    int a=0;
    String[] list_giftlistItems = new String[getGiftList.getGiftLength()];
    boolean[] list_giftcheckedItems, tempGiftChecked;
    ArrayList<String> selectGiftIds;  //選擇的禮物ID

    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] list_friendlistItems = new String[getFriendList.getFriendLength()];
    boolean[] list_friendcheckedItems, tempFriendChecked;
    ArrayList<String> selectFriendIds;  //選擇的好友ID
    //----------------------------------------------------------------------------------------------

    static EditText edt_list_name, edt_list_message, edt_list_edate,edt_list_friend, edt_list_giftName, edt_list_sentTime;

    //----------------------------------------------------------------------------------------------
    ProgressDialog barProgressDialog;
    private RecyclerView recycler_view;
    private plan_list_adapter adapter;
    private List<String> mData = new ArrayList<>();

    private Button btnAdd, btn_ent, btn_can, btn_save, btn_send;
    String list_message;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan_list);
        setTitle(R.string.planList);
        //--------------------取得資料
        getGiftList.getJSON();
        //--------------------
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //---------------------------------------------------------------------------------
        //宣告變數------------------------------------------------------------------------------
        edt_list_name = findViewById(R.id.list_name);
        edt_list_edate = findViewById(R.id.list_edate);
        edt_list_friend = findViewById(R.id.list_friend);
        edt_list_giftName = findViewById(R.id.list_gift);
        edt_list_sentTime = findViewById(R.id.list_time);
        btn_save = findViewById(R.id.btn_plan_save);
        btn_send = findViewById(R.id.btn_plan_send);

        btn_save.setOnClickListener(planSaveClickListener); //設置監聽器
        btn_send.setOnClickListener(planSendClickListener); //設置監聽器

        //------------------------------------------------------------------------------
        //選擇禮物 使用的變數宣告-------------------------------------------------------------------------- 禮物資料
        for (int i = 0; i < getGiftList.getGiftLength(); i++) {
            list_giftlistItems[i] = getGiftList.getGiftName(i);
        }

        list_giftcheckedItems = new boolean[list_giftlistItems.length];
        tempGiftChecked =  new boolean[list_giftlistItems.length];

        //選擇好友使用的變數宣告--------------------------------------------------------------------------- 好友資料
        for (int i = 0; i < getFriendList.getFriendLength(); i++) {
            list_friendlistItems[i] = getFriendList.getFriendName(i);
        }
        list_friendcheckedItems = new boolean[list_friendlistItems.length];
        tempFriendChecked = new boolean[list_friendlistItems.length];

        // 連結元件
        recycler_view = findViewById(R.id.list_recycle_view);
        // 設置RecyclerView為列表型態
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        // 設置格線
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // 將資料交給adapter
        adapter = new plan_list_adapter(mData);
        // 設置adapter給recycler_view
        recycler_view.setAdapter(adapter);

        //--------點選新增事件
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(true, mData.size());
            }

        });
        adapter.setOnItemClickListener(new plan_list_adapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                showDialog(false, position);
            }
        });
        //--------------------------------------

        //點選選擇好友EditText跳出選擇好友選擇器------------------------------------------------------------------------
        edt_list_friend.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edt_list_friend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Showfrienddialog();
                }
            }
        });
        edt_list_friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Showfrienddialog();
            }
        });
       
        //點選結束日期EditText跳出選擇日期選擇器---------------------------------------
        edt_list_edate.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edt_list_edate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        edt_list_edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog();
            }
        });
        //點選選擇禮物EditText跳出選擇禮物選擇器------------------------------------------------------------------------
        edt_list_giftName.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edt_list_giftName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Showgiftdialog();
                }
            }
        });
        edt_list_giftName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Showgiftdialog();
            }
        });
        //點選送禮日期EditText跳出選擇時間選擇器---------------------------------------
        edt_list_sentTime.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edt_list_sentTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showTimePickerDialog();
                }
            }
        });

        edt_list_sentTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTimePickerDialog();
            }
        });
    }
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //設定選擇禮物EditText傳入值---------------------------------------
    private void Showgiftdialog() {
        AlertDialog.Builder gBuilder = new AlertDialog.Builder(MakePlanListActivity.this);
        gBuilder.setTitle("選擇");
        gBuilder.setMultiChoiceItems(list_giftlistItems, list_giftcheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
            }
        });

        gBuilder.setCancelable(false);
        gBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                selectGiftIds = new ArrayList<>();

                for (int i = 0; i < list_giftcheckedItems.length; i++) {
                    if (list_giftcheckedItems[i]) {
                        if (item.equals("")) item += list_giftlistItems[i];
                        else item += " , " + list_giftlistItems[i];
                        selectGiftIds.add(getGiftList.getGiftid(i));
                    }
                    tempGiftChecked[i] = list_giftcheckedItems[i];
                }
                edt_list_giftName.setText(item);
            }
        });

        gBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //取消鈕
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < tempGiftChecked.length; i++)
                    list_giftcheckedItems[i] = tempGiftChecked[i];
            }
        });

        gBuilder.setNeutralButton("清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {  //清除鈕
                for (int i = 0; i < list_giftcheckedItems.length; i++) {
                    list_giftcheckedItems[i] = false;
                    tempGiftChecked[i] = false;
                }
                edt_list_giftName.setText("");
            }
        });
        AlertDialog gDialog = gBuilder.create();
        gDialog.show();
    }

    //設定選擇好友EditText傳入值---------------------------------------
    private void Showfrienddialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MakePlanListActivity.this);
        mBuilder.setTitle("選擇好友");
        mBuilder.setMultiChoiceItems(list_friendlistItems, list_friendcheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                selectFriendIds = new ArrayList<>();

                for (int i = 0; i < list_friendcheckedItems.length; i++) {
                    if (list_friendcheckedItems[i]) {
                        if (item.equals("")) item += list_friendlistItems[i];
                        else item += " , " + list_friendlistItems[i];
                        selectFriendIds.add(getFriendList.getFriendid(i));
                    }
                    tempFriendChecked[i] = list_friendcheckedItems[i];
                }
                edt_list_friend.setText(item);
            }
        });

        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //取消鈕
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < tempFriendChecked.length; i++)
                    list_friendcheckedItems[i] = tempFriendChecked[i];
            }
        });

        mBuilder.setNeutralButton("清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {  //清除鈕
                for (int i = 0; i < list_friendcheckedItems.length; i++) {
                    list_friendcheckedItems[i] = false;
                    tempFriendChecked[i] = false;
                }
                edt_list_friend.setText("");
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //設定送禮日期EditText傳入值---------------------------------------
    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(MakePlanListActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                String month;
                String day;
                    edt_list_edate.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    //-----------------------------------------------------------
    public String dateAdd0(int date) {
        if (date < 10) {
            return "0" + date;
        } else {
            return String.valueOf(date);
        }
    }
    //-----------------------------------------------------------

    //設定送禮時間EditText傳入值---------------------------------------
    private void showTimePickerDialog() {
        Calendar t = Calendar.getInstance();
        new TimePickerDialog(MakePlanListActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                edt_list_sentTime.setText(dateAdd0(hourOfDay) + ":" + dateAdd0(minute));
            }
        }, t.get(Calendar.HOUR_OF_DAY), t.get(Calendar.MINUTE), false).show();

    }
    //-----------------------------------------------------------------

    //跳出送禮輸入窗
    private void showDialog(final boolean isNew, final int position) {
        final Dialog dialog = new Dialog(MakePlanListActivity.this);
        dialog.setContentView(R.layout.list_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);//讓使用者點選背景或上一頁沒有用

        //宣告
        edt_list_message = dialog.findViewById(R.id.list_message);

        if(!isNew){ //若為編輯則設定資料
            edt_list_message.setText(mData.get(position));
        }
        //點選取消
        btn_can = dialog.findViewById(R.id.btn_can);
        btn_can.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNew){
                    edt_list_message.setText("");
                }
                dialog.dismiss();
            }
        });

        //點選確認
        btn_ent = dialog.findViewById(R.id.btn_ent);
        btn_ent.setOnClickListener(new Button.OnClickListener() {
            @Override
            //新增一個項目
            public void onClick(View view) {
                //抓取輸入方塊訊息
                list_message = edt_list_message.getText().toString();
                if (isNew) {    //新增
                    mData.add(list_message);
                    adapter.notifyItemInserted(mData.size());
                }else{  //編輯
                    mData.set(position, list_message);
                    adapter.notifyDataSetChanged();
                }
                //在item內傳送文字
                dialog.cancel();
            }
        });
        dialog.show();
    }

    //-------------------------------儲存按鈕 監聽器----------------------------------------
    private View.OnClickListener planSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }

    };


    //-------------------------------預送禮物 監聽器----------------------------------------
    private View.OnClickListener planSendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }

    };

}

