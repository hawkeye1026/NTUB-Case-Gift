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
import android.widget.TimePicker;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.plan_list_adapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.giftRecordInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.missionItemInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.missionListInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.missionPlanInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.data.getFriendList;
import com.ntubcase.gift.data.getGiftList;

import org.json.JSONArray;
import org.json.JSONObject;

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

    static EditText edt_list_name, edt_list_message, edt_list_lastDate,edt_list_friend, edt_list_giftName, edt_list_sentDate,edt_list_lastTime;
    private String sender= "1", planid, planType="3", dateTime, dateOnly;

    //----------------------------------------------------------------------------------------------
    ProgressDialog barProgressDialog;
    private RecyclerView recycler_view;
    private plan_list_adapter adapter;
    private List<String> mData = new ArrayList<>();

    private Button btnAdd, btn_ent, btn_can, btn_save, btn_send;
    String list_message;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //--showPlan
    private List<Map<String, Object>> friends;
    private List<Map<String, Object>> gifts;
    private List<Map<String, Object>> items;

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
        edt_list_lastDate = findViewById(R.id.list_lastDate);
        edt_list_friend = findViewById(R.id.list_friend);
        edt_list_giftName = findViewById(R.id.list_gift);
        edt_list_sentDate = findViewById(R.id.list_time);
        edt_list_lastTime= findViewById(R.id.list_lastTime);
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

        recycler_view = findViewById(R.id.list_recycle_view);  // 設置RecyclerView為列表型態
        recycler_view.setLayoutManager(new LinearLayoutManager(this));  // 設置格線
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new plan_list_adapter(mData);  // 將資料交給adapter
        recycler_view.setAdapter(adapter); // 設置adapter給recycler_view

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
        edt_list_lastDate.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edt_list_lastDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDatePickerDialog(false);
                }
            }
        });

        edt_list_lastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog(false);
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
        //點選送禮日期EditText跳出選擇日期選擇器---------------------------------------
        edt_list_sentDate.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edt_list_sentDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDatePickerDialog(true);
                }
            }
        });

        edt_list_sentDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog(true);
            }
        });

        //點選截止時間EditText跳出選擇時間選擇器---------------------------------------
        edt_list_lastTime.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edt_list_lastTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showTimePickerDialog();
                }
            }
        });

        edt_list_lastTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTimePickerDialog();
            }
        });

        //----------------檢查必填資料是否填完----------------
        if (isDataCompleted()) btn_send.setVisibility(View.VISIBLE);
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
                selectGiftIds = new ArrayList<>();
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
                selectFriendIds = new ArrayList<>();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //設定送禮日期EditText傳入值---------------------------------------
    private void showDatePickerDialog(final boolean isNew){
        Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(MakePlanListActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                if (isNew==true){
                        edt_list_sentDate.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
                }else{
                        edt_list_lastDate.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
                }

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());  //最小日期為當日
        datePickerDialog.show();
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
                    edt_list_lastTime.setText(dateAdd0(hourOfDay) + ":" + dateAdd0(minute));
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

    //----------------檢查必填資料是否填完----------------
    private boolean isDataCompleted(){
        //---檢查每個任務項目是否有內容---
        boolean isMissonHaveContent=false;
        for (int i=0; i<mData.size(); i++){
            if (mData.get(i).equals("")) break;
            if (i==mData.size()-1) isMissonHaveContent = true;
        }

        String planName = edt_list_name.getText().toString();
        String sendPlanDate = edt_list_sentDate.getText().toString();
        String receiveFriend = edt_list_friend.getText().toString();
        String gifts = edt_list_giftName.getText().toString();

        if (!planName.equals("") && !sendPlanDate.equals("") && !receiveFriend.equals("")
                 && !gifts.equals("") && mData.size()>0 && isMissonHaveContent){
            return true;
        }
        return false;
    }

    //-------------------------------儲存按鈕 監聽器----------------------------------------
    private View.OnClickListener planSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //--------取得目前時間：yyyy/MM/dd hh:mm:ss
            Date date = new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateTime = sdFormat.format(date);
            SimpleDateFormat _sdFormat = new SimpleDateFormat("yyyy-MM-dd ");
            dateOnly = _sdFormat.format(date);

            SimpleDateFormat sdFormat_giftContent = new SimpleDateFormat("yyyyMMddHHmmss");
            planid = "mis_" + sdFormat_giftContent.format(date);

            //----若預送按鈕尚未出現 並填完必填資料---
            if (btn_send.getVisibility()==View.GONE && isDataCompleted()){
                btn_send.setVisibility(View.VISIBLE);
                new AlertDialog.Builder(MakePlanListActivity.this)
                        .setTitle("是否直接預送您的計畫?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadPlan("1");
                                Toast.makeText(getApplicationContext(), "已預送!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNeutralButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadPlan("0");
                                Toast.makeText(getApplicationContext(), "儲存成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }else{
                uploadPlan("0");
                Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
            }
        }

    };

    //-------------------------------預送禮物 監聽器----------------------------------------
    private View.OnClickListener planSendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isDataCompleted()) { //---資料填完才能預送---
                //取得目前時間：yyyy/MM/dd hh:mm:ss
                Date date = new Date();
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                dateTime = sdFormat.format(date);
                SimpleDateFormat _sdFormat = new SimpleDateFormat("yyyy-MM-dd ");
                dateOnly = _sdFormat.format(date);

                SimpleDateFormat sdFormat_giftContent = new SimpleDateFormat("yyyyMMddHHmmss");
                planid = "mis_" + sdFormat_giftContent.format(date);

                uploadPlan("1");
                Toast.makeText(v.getContext(), "已預送!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(v.getContext(), "您尚有計畫細節未完成喔!", Toast.LENGTH_SHORT).show();
            }
        }

    };

    //------------------------------上傳plan資料
    public void uploadPlan(String store){
        //---upload giftRecord
        Log.v("selectFriendIds.size", String.valueOf(selectFriendIds.size()));
        for (int i = 0 ; i < selectFriendIds.size(); i++) {
            giftRecordInsertAsyncTask giftRecordInsertAsyncTask = new giftRecordInsertAsyncTask(new giftRecordInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            giftRecordInsertAsyncTask.execute(Common.insertMisPlan, sender, selectFriendIds.get(i), planid, store, planType);
        }
        Log.v("giftRecord", "//---upload giftRecord");

        //---upload missionPlan
        missionPlanInsertAsyncTask missionPlanInsertAsyncTask = new missionPlanInsertAsyncTask(new missionPlanInsertAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        missionPlanInsertAsyncTask.execute(Common.insertMisPlan, planid, edt_list_name.getText().toString(), dateTime, dateOnly+" "+edt_list_sentDate.getText().toString(), edt_list_lastDate.getText().toString());
        Log.v("missionPlan", "//---upload missionPlan");

        //---upload missionItem
        for(int i = 0 ; i < mData.size(); i++) {
            missionItemInsertAsyncTask missionItemInsertAsyncTask = new missionItemInsertAsyncTask(new missionItemInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            missionItemInsertAsyncTask.execute(Common.insertMisPlan, planid, mData.get(i));
        }
        Log.v("missionItem", "//---upload missionItem");

        //---upload missionList
        for(int i = 0 ; i < selectGiftIds.size(); i++) {
            missionListInsertAsyncTask missionListInsertAsyncTask = new missionListInsertAsyncTask(new missionListInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            missionListInsertAsyncTask.execute(Common.insertMisPlan, planid, selectGiftIds.get(i));
        }
        //Log.v("missionList", "//---upload missionList");
        finish(); //結束製作計畫
    }

    @Override
    protected void onResume() {
        showPlan("mis_20190830215741");
        super.onResume();
    }

    //------------------------------顯示plan資料mis_20190830215741
    public void showPlan(String planid){
        planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(MakePlanListActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);

                    //取得禮物紀錄
                    JSONArray jsonArray = object.getJSONArray("record");
                    int recordLength = jsonArray.length();
                    Log.v("recordLength", String.valueOf(recordLength));

                    friends = new ArrayList<Map<String, Object>>();
                    Map<String, Object> mFriends;

                    for (int i = 0; i < recordLength; i++) {
                        String receiverid =jsonArray.getJSONObject(i).getString("receiverid");
                        String nickname =jsonArray.getJSONObject(i).getString("nickname");

                        edt_list_friend.setText(nickname);

                        mFriends = new HashMap<String, Object>();
                        mFriends.put("receiverid", receiverid);
                        mFriends.put("nickname", nickname);
                        friends.add(mFriends);
                    }
                    Log.v("friends", String.valueOf(friends));

                    //取得單日計畫
                    jsonArray = object.getJSONArray("misPlan");
                    int misPlanLength = jsonArray.length();
                    Log.v("misPlanLength", String.valueOf(misPlanLength));

                    String misPlanid =jsonArray.getJSONObject(0).getString("misid");
                    String misPlanName =jsonArray.getJSONObject(0).getString("misPlanName");
                    String misCreateDate = DateFormat.dateFormat(jsonArray.getJSONObject(0).getString("createDate"));
                    String misSendPlanDate = DateFormat.dateFormat(jsonArray.getJSONObject(0).getString("sendPlanDate"));
                    String deadline = DateFormat.dateFormat(jsonArray.getJSONObject(0).getString("deadline"));

                    edt_list_name.setText(misPlanName);

                    //取得任務禮物清單
                    jsonArray = object.getJSONArray("misList");
                    int misListLength = jsonArray.length();
                    Log.v("misListLength", String.valueOf(misListLength));

                    gifts = new ArrayList<Map<String, Object>>();
                    Map<String, Object> mGifts;

                    for (int i = 0 ; i < misListLength ; i++){
                        String misListid = jsonArray.getJSONObject(i).getString("misid");
                        String misGiftid = jsonArray.getJSONObject(i).getString("giftid");
                        String misGift = jsonArray.getJSONObject(i).getString("gift");
                        String misGiftName = jsonArray.getJSONObject(i).getString("giftName");

                        mGifts = new HashMap<String, Object>();
                        mGifts.put("giftid", misGiftid);
                        mGifts.put("misGift", misGift);
                        mGifts.put("misGiftName", misGiftName);
                        gifts.add(mGifts);
                    }
                    Log.v("gifts", String.valueOf(gifts));

                    //取得任務項目
                    jsonArray = object.getJSONArray("misItem");
                    int misItemLength = jsonArray.length();
                    Log.v("misItemLength", String.valueOf(misItemLength));

                    items = new ArrayList<Map<String, Object>>();
                    Map<String, Object> mItems;

                    for (int i = 0 ; i < misListLength ; i++){
                        String misItemid = jsonArray.getJSONObject(i).getString("misid");
                        String content = jsonArray.getJSONObject(i).getString("content");

                        mItems = new HashMap<String, Object>();
                        mItems.put("misid", misItemid);
                        mItems.put("content", content);
                        items.add(mItems);
                    }
                    Log.v("items", String.valueOf(items));


                } catch (Exception e) {
                    Toast.makeText(MakePlanListActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.planList , sender, planid);
    }


    public void check(){
        String a=edt_list_sentDate.getText().toString();
        String b= edt_list_lastDate.getText().toString();

        if (a.compareTo(b)<0) {
            Toast.makeText(MakePlanListActivity.this, "截止日期不能早於送出日期", Toast.LENGTH_SHORT).show();
        }
        if (edt_list_name == null || edt_list_lastDate == null || edt_list_friend == null || edt_list_giftName == null|
                edt_list_sentDate == null || edt_list_lastTime == null) {
            //Toast.makeText(MakePlanListActivity.this, "請輸入完整計畫資訊", Toast.LENGTH_SHORT).show();
            btn_send.setVisibility(View.VISIBLE);
        }
    }

}

