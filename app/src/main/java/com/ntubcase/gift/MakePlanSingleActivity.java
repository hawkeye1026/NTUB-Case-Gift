package com.ntubcase.gift;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.TimePicker;
import android.widget.Toast;
import com.ntubcase.gift.Adapter.plan_single_adapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.giftRecordInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.singleListInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.singlePlanInsertAsyncTask;
import com.ntubcase.gift.data.getFriendList;
import com.ntubcase.gift.data.getGiftList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakePlanSingleActivity extends AppCompatActivity {

    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] single_friendlistItems = new String[getFriendList.getFriendLength()];
    boolean[] single_friendcheckedItems, tempFriendChecked;
    ArrayList<String> selectFriendIds;

    //----------------------------------------------------------------------------------------------
    static EditText edt_single_name, edt_single_message, edt_single_date, edt_single_friend, edt_single_giftName, edt_single_sentTime;
    //----------------------------------------------------------------------------------------------
    ProgressDialog barProgressDialog;
    private RecyclerView recycler_view;
    private plan_single_adapter adapter;
    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

    private Button btnAdd, btn_ent, btn_can, btn_save, btn_send;
    String single_giftName, single_sentTime, single_message;
    private String sender= "1", planid, planType="1", dateTime, date_time;

    //選擇禮物 使用的變數宣告---------------------------------------------------------------------------
    int a=0;
    String[] single_giftlistItems = new String[getGiftList.getGiftLength()]; //禮物名稱資料
    private boolean[] mCheckedGift;
    private List<List<String>> mSelectGiftIds;

    //---------------------------------------------------------------------------------------------------------------
    private SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm");

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
        btn_save = findViewById(R.id.btn_plan_save);
        btn_send = findViewById(R.id.btn_plan_send);

        btn_save.setOnClickListener(planSaveClickListener); //-----------儲存計畫
        btn_send.setOnClickListener(planSendClickListener); //-----------送出計畫

        //------------------------------------------------------------------------------
        //選擇禮物 使用的變數宣告-------------------------------------------------------------------------- 禮物資料
        for (int i = 0; i < getGiftList.getGiftLength(); i++) {
            single_giftlistItems[i] = getGiftList.getGiftName(i);
        }

        //選擇好友使用的變數宣告--------------------------------------------------------------------------- 好友資料
        for (int i = 0; i < getFriendList.getFriendLength(); i++) {
            single_friendlistItems[i] = getFriendList.getFriendName(i);
        }
        single_friendcheckedItems = new boolean[single_friendlistItems.length];
        tempFriendChecked = new boolean[single_friendlistItems.length];

        //----------------------點選新增事件----------------------
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckedGift = new boolean[single_giftlistItems.length];
                Map<String, Object> newData = new HashMap<String, Object>();
                newData.put("giftName", "");
                newData.put("sentTime", "");
                newData.put("message", "");
                newData.put("mCheckedGift", mCheckedGift);
                mData.add(newData);

                showDialog(true, mData.size()-1);  //新增一筆禮物
            }
        });

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
        adapter.setOnItemClickListener(new plan_single_adapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                showDialog(false, position);
            }
        });

        //--------------------------------------

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

        //----------------檢查必填資料是否填完----------------
        if (isDataCompleted()) btn_send.setVisibility(View.VISIBLE);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    //設定選擇好友EditText傳入值---------------------------------------
    private void Showfrienddialog() {

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
            public void onClick(DialogInterface dialogInterface, int which) {  //確認鈕
                String item = "";
                selectFriendIds = new ArrayList<>();

                for (int i = 0; i < single_friendcheckedItems.length; i++) {
                    if (single_friendcheckedItems[i]) {
                        if (item.equals("")) item += single_friendlistItems[i];
                        else item += " , " + single_friendlistItems[i];
                        selectFriendIds.add(getFriendList.getFriendid(i));
                    }
                    tempFriendChecked[i] = single_friendcheckedItems[i];
                }
                edt_single_friend.setText(item);
            }
        });

        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //取消鈕
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {  //取消鈕
                for (int i = 0; i < tempFriendChecked.length; i++)
                    single_friendcheckedItems[i] = tempFriendChecked[i];
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
                selectFriendIds = new ArrayList<>();
            }
        });


        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //設定送禮日期EditText傳入值---------------------------------------
    private void showDatePickerDialog(){
        Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(MakePlanSingleActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                edt_single_date.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());  //最小日期為當日
        datePickerDialog.show();
    }

    //-----------------------------------------------新增一筆禮物----------------------------------------------
    private void showDialog(final boolean isNew, final int position) {
        final Dialog dialog = new Dialog(MakePlanSingleActivity.this);
        dialog.setContentView(R.layout.single_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);//讓使用者點選背景或上一頁沒有用

        //宣告
        edt_single_giftName = dialog.findViewById(R.id.single_giftName);
        edt_single_sentTime = dialog.findViewById(R.id.single_sentTime);
        edt_single_message = dialog.findViewById(R.id.single_message);

        if(!isNew){ //若為編輯則設定資料
            edt_single_giftName.setText(mData.get(position).get("giftName").toString());
            edt_single_sentTime.setText(mData.get(position).get("sentTime").toString());
            edt_single_message.setText(mData.get(position).get("message").toString());
        }

        //點選送禮時間EditText跳出選擇時間選擇器---------------------------------------
        edt_single_sentTime.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        if (edt_single_sentTime.getText().toString().equals(""))
            edt_single_sentTime.setText("00:00"); //一定要有時間,預設為00:00
        edt_single_sentTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showTimePickerDialog(position);
                }
            }
        });

        edt_single_sentTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTimePickerDialog(position);
            }
        });

        //點選選擇禮物EditText跳出選擇禮物選擇器------------------------------------------------------------------------
        edt_single_giftName.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘

        //---若新增則為空陣列,若編輯則為原選取資料---
        boolean[] checked = (boolean[]) mData.get(position).get("mCheckedGift");
        mCheckedGift = Arrays.copyOf(checked, checked.length);

        edt_single_giftName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Showgiftdialog(position);
                }
            }
        });
        edt_single_giftName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Showgiftdialog(position);
            }
        });

        //--------------------------------------------------dialog按鈕-------------------------------------------------
        //點選確認
        btn_ent = dialog.findViewById(R.id.btn_ent);
        btn_ent.setOnClickListener(new Button.OnClickListener() {
            @Override
            //新增一個項目
            public void onClick(View view) {
                //抓取輸入方塊訊息
                single_giftName = edt_single_giftName.getText().toString();
                single_sentTime = edt_single_sentTime.getText().toString();
                single_message = edt_single_message.getText().toString();

                //------儲存輸入的資料-----
                Map<String, Object> newData = new HashMap<String, Object>();
                newData.put("giftName", single_giftName);
                newData.put("sentTime", single_sentTime);
                newData.put("message", single_message);
                newData.put("mCheckedGift", mCheckedGift);
                mData.set(position, newData);

                //--------按時間排序--------
                Collections.sort(mData, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        try {
                            Date time1 = sdfT.parse((String)o1.get("sentTime"));
                            Date time2 = sdfT.parse((String)o2.get("sentTime"));
                            return time1.compareTo(time2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });

                adapter.notifyDataSetChanged();

                dialog.cancel();
            }
        });

        //點選取消
        btn_can = dialog.findViewById(R.id.btn_can);
        btn_can.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNew) mData.remove(position);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //設定選擇禮物EditText傳入值---------------------------------------
    private void Showgiftdialog(final int position) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MakePlanSingleActivity.this);
        mBuilder.setTitle("選擇禮物");

        //---暫存資料以備取消後復原---
        final boolean[] tempCheckedGift = Arrays.copyOf(mCheckedGift, mCheckedGift.length);

        mBuilder.setMultiChoiceItems(single_giftlistItems, mCheckedGift, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

            }
        });

        //---------確認、取消、清除-----------
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {  //確認鈕
                String item = "";

                for (int i = 0; i < single_giftlistItems.length; i++) {
                    if (mCheckedGift[i]) {
                        if (item.equals("")){ item += single_giftlistItems[i];
                        }else{ item += " , " + single_giftlistItems[i];}
                    }
                }
                edt_single_giftName.setText(item);
            }
        });

        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //取消鈕
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {  //取消鈕
                for (int i = 0; i < single_giftlistItems.length; i++) {
                    mCheckedGift[i] = tempCheckedGift[i];
                }
            }
        });

        mBuilder.setNeutralButton("清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {  //清除鈕
                mCheckedGift = new boolean[single_giftlistItems.length];
                edt_single_giftName.setText("");
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //設定送禮時間EditText傳入值---------------------------------------
    private void showTimePickerDialog(int position) {
        Calendar t = Calendar.getInstance();

        //---若有資料則預設為上次輸入的時間---
        try {
            String oldTime = edt_single_sentTime.getText().toString();  //取得上次選的時間
            if (!oldTime.equals("")) t.setTime(sdfT.parse(oldTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        new TimePickerDialog(MakePlanSingleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                edt_single_sentTime.setText(dateAdd0(hourOfDay) + ":" + dateAdd0(minute));
            }
        }, t.get(Calendar.HOUR_OF_DAY), t.get(Calendar.MINUTE), false).show();
    }

    //-----------------------------------------------------------
    public String dateAdd0(int date) {
        if (date < 10) {
            return "0" + date;
        } else {
            return String.valueOf(date);
        }
    }

    //----------------檢查必填資料是否填完----------------
    private boolean isDataCompleted(){
        //---檢查每個送禮時間點是否有禮物---
        boolean isTimeHaveGift=false;
        for (int i=0; i<mData.size(); i++){
            String gifts =(String)mData.get(i).get("giftName");
            if (gifts.equals("")) break;
            if (i==mData.size()-1) isTimeHaveGift = true;
        }

        String planName = edt_single_name.getText().toString();
        String sendPlanDate = edt_single_date.getText().toString();
        String receiveFriend = edt_single_friend.getText().toString();

        if (!planName.equals("") && !sendPlanDate.equals("")
                && !receiveFriend.equals("") && isTimeHaveGift){
            return true;
        }
        return false;
    }

    //-------------------------------儲存按鈕 監聽器----------------------------------------
    private View.OnClickListener planSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            Log.v("planName",edt_single_name.getText().toString());
//            Log.v("single_date", edt_single_date.getText().toString());
//            Log.v("selectFriendIds", String.valueOf(selectFriendIds));
//            Log.v("mData", String.valueOf(mData));

            //--------取得目前時間：yyyy/MM/dd hh:mm:ss
            Date date = new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateTime = sdFormat.format(date);

            SimpleDateFormat sdFormat_giftContent = new SimpleDateFormat("yyyyMMddHHmmss");
            planid = "sin_" + sdFormat_giftContent.format(date);

            //----若預送按鈕尚未出現 並填完必填資料---
            if (btn_send.getVisibility()==View.GONE && isDataCompleted()){
                btn_send.setVisibility(View.VISIBLE);
                new AlertDialog.Builder(MakePlanSingleActivity.this)
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
                            }
                        })
                        .show();
            }else{
                uploadPlan("0");
                Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //-------------------------------預送禮物按鈕 監聽器----------------------------------------
    private View.OnClickListener planSendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isDataCompleted()){ //---資料填完才能預送---
                //取得目前時間：yyyy/MM/dd hh:mm:ss
                Date date = new Date();
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                dateTime = sdFormat.format(date);

                SimpleDateFormat sdFormat_giftContent = new SimpleDateFormat("yyyyMMddHHmmss");
                planid = "sin_" + sdFormat_giftContent.format(date);

                uploadPlan("1");
                Toast.makeText(v.getContext(), "已預送!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(v.getContext(), "您尚有計畫細節未完成喔!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //------------------------------上傳plan資料
    public void uploadPlan(String store){
        //---------------------------選擇日期
        String sendPlanDate = edt_single_date.getText().toString() + " " + edt_single_sentTime.getText().toString();

        Log.v("selectFriendIds.size", String.valueOf(selectFriendIds.size()));
        //---upload giftRecord
        for (int i = 0 ; i < selectFriendIds.size(); i++) {
            giftRecordInsertAsyncTask giftRecordInsertAsyncTask = new giftRecordInsertAsyncTask(new giftRecordInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            Log.v("store:::::::",store);
            giftRecordInsertAsyncTask.execute(Common.insertSinPlan, sender, selectFriendIds.get(i), planid, store, planType);
        }
        Log.v("giftRecord", "//---upload giftRecord");

        //--- upload singlePlan
        singlePlanInsertAsyncTask singlePlanInsertAsyncTask = new singlePlanInsertAsyncTask(new singlePlanInsertAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        singlePlanInsertAsyncTask.execute(Common.insertSinPlan, planid, edt_single_name.getText().toString(), dateTime, sendPlanDate);
        Log.v("singlePlan", "//---upload singlePlan");

        //--- upload singleList
        Log.v("mData.size", String.valueOf(mData.size()));

        //-------------------------------------------------------------------------------------------
        //------取得禮物id-----
        mSelectGiftIds = new ArrayList<List<String>>();
        for (int i = 0 ; i < mData.size(); i++) {
            boolean[] checked = (boolean[]) mData.get(i).get("mCheckedGift");
            List<String> giftIds = new ArrayList<>();
            for (int k= 0; k< single_giftlistItems.length; k++){
                if (checked[k]) giftIds.add(getGiftList.getGiftid(k));
            }
            mSelectGiftIds.add(giftIds);
        }

        for (int i = 0 ; i < mData.size(); i++) {
            for (int j = 0; j < mSelectGiftIds.get(i).size(); j++) {
                Log.v("get(i).size()", String.valueOf(mSelectGiftIds.get(i).size()));
                String space=" ";
                date_time = edt_single_date.getText().toString() +" "+mData.get(i).get("sentTime").toString();  //-- x

                singleListInsertAsyncTask singleListInsertAsyncTask = new singleListInsertAsyncTask(new singleListInsertAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {

                    }
                });
                Log.v("planid", planid);
                Log.v("get(i).get(j)", String.valueOf(mSelectGiftIds.get(i).get(j)));
                Log.v("date_time", date_time);
                Log.v("message", mData.get(i).get("message").toString());
                singleListInsertAsyncTask.execute(Common.insertSinPlan, planid, mSelectGiftIds.get(i).get(j), date_time, mData.get(i).get("message").toString());
            }

        }
        Log.v("singleList", "//---upload singleList");
        finish(); //結束製作計畫
    }


    //-----------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
