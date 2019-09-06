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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakePlanListActivity extends AppCompatActivity{
    //選擇禮物 使用的變數宣告---------------------------------------------------------------------------
    int a=0;
    String[] list_giftlistItems = new String[getGiftList.getGiftLength()];
    boolean[] list_giftcheckedItems, tempGiftChecked;
    ArrayList<String> selectGiftIds = new ArrayList<>(); ;  //選擇的禮物ID

    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] list_friendlistItems = new String[getFriendList.getFriendLength()];
    boolean[] list_friendcheckedItems, tempFriendChecked;
    ArrayList<String> selectFriendIds = new ArrayList<>(); //選擇的好友ID
    //----------------------------------------------------------------------------------------------
    static EditText edt_list_name, edt_list_message, edt_list_lastDate,edt_list_friend, edt_list_giftName, edt_list_sentDate,edt_list_lastTime;
    private String sender= "1", planid, planType="3", dateTime, dateOnly;

    //----------------------------------------------------------------------------------------------

    private RecyclerView recycler_view;
    private plan_list_adapter adapter;
    private List<String> mData = new ArrayList<>();

    private Button btnAdd, btn_ent, btn_can, btn_save, btn_send;
    String list_message;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm");

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

        //----------------------點選新增事件----------------------
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(true, mData.size());
            }
        });

        //-----------------------------------------------------------------------
        recycler_view = findViewById(R.id.list_recycle_view);  // 設置RecyclerView為列表型態
        recycler_view.setLayoutManager(new LinearLayoutManager(this));  // 設置格線
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new plan_list_adapter(mData);  // 將資料交給adapter
        recycler_view.setAdapter(adapter); // 設置adapter給recycler_view
        adapter.setOnItemClickListener(new plan_list_adapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                showDialog(false, position);
            }
        });

        //---------------------------------若是計畫詳細-----------------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            String planID = bundle.getString("planID");
            showPlanDetail(planID);  //顯示計畫詳細資料
        }
        //--------------------------------------------------------------------------------------

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
                    showDateEPickerDialog();
                }


            }
        });

        edt_list_lastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDateEPickerDialog();

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
                    showDateSPickerDialog();
                }
            }
        });

        edt_list_sentDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDateSPickerDialog();
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
    private void showDateSPickerDialog () {
        final Calendar c = Calendar.getInstance();

        try {
            String oldDate = edt_list_sentDate.getText().toString();  //取得上次選的日期
            if (!oldDate.equals("")) c.setTime(sdf.parse(oldDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final DatePickerDialog datePickerDialog = new DatePickerDialog(MakePlanListActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                edt_list_sentDate.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());  //最小日期為當日
        try {
            if (!edt_list_lastDate.getText().toString().equals("")){
                Date lastDate =  sdf.parse(edt_list_lastDate.getText().toString());
                datePickerDialog.getDatePicker().setMaxDate(lastDate.getTime()); //最大日期為截止日期
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        datePickerDialog.show();
    }

    //設定截止日期EditText傳入值---------------------------------------
    private void showDateEPickerDialog () {
        final Calendar c = Calendar.getInstance();

        try {
            String oldDate = edt_list_lastDate.getText().toString();  //取得上次選的日期
            if (!oldDate.equals("")) c.setTime(sdf.parse(oldDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(MakePlanListActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                edt_list_lastDate.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));

                //-----設定截止時間初值-----
                String dateLast = edt_list_lastDate.getText().toString();//截止日期
                String timeLast = edt_list_lastTime.getText().toString();//截止時間
                if (!dateLast.equals("") && timeLast.equals(""))
                    edt_list_lastTime.setText("23:59");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edt_list_lastDate.setText("");
                edt_list_lastTime.setText("");
            }
        });

        try {
            if (!edt_list_sentDate.getText().toString().equals("")){
                Date sentDate =  sdf.parse(edt_list_sentDate.getText().toString());
                datePickerDialog.getDatePicker().setMinDate(sentDate.getTime());  //最小日期為送禮日期
            }else{
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());  //最小日期為當日
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

    //設定截止時間EditText傳入值---------------------------------------
    private void showTimePickerDialog() {
        Calendar t = Calendar.getInstance();
        try {
            String oldTime = edt_list_lastTime.getText().toString();  //取得上次選的時間
            if (!oldTime.equals("")) t.setTime(sdfT.parse(oldTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TimePickerDialog TimePickerDialog = new TimePickerDialog(MakePlanListActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    edt_list_lastTime.setText(dateAdd0(hourOfDay) + ":" + dateAdd0(minute));

                    //-----設定截止日期初值-----
                    String sentDate  = edt_list_sentDate.getText().toString();//送禮日期
                    String dateLast = edt_list_lastDate.getText().toString();//截止日期
                    String timeLast = edt_list_lastTime.getText().toString();//截止時間
                    if (!timeLast.equals("") && dateLast.equals("")){
                        if (!sentDate.equals("")) edt_list_lastDate.setText(sentDate); //截止日期為送禮日期
                        else edt_list_lastDate.setText(sdf.format(new Date(System.currentTimeMillis()))); //截止日期為當日
                    }
            }
        }, t.get(Calendar.HOUR_OF_DAY), t.get(Calendar.MINUTE), false);

        TimePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edt_list_lastDate.setText("");
                edt_list_lastTime.setText("");
            }
        });

        TimePickerDialog.show();

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
                if (isNew && !list_message.equals("")) {    //新增
                    mData.add(list_message);
                    adapter.notifyItemInserted(mData.size());
                }else if (!isNew && !list_message.equals("")){  //編輯
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

    //--------------檢查時間是否不為過去式---------------------------
    private boolean isTimeCheck() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String dateLast = edt_list_lastDate.getText().toString();//截止日期
        String timeLast = edt_list_lastTime.getText().toString();//截止時間
        Date nowDateTime = new Date(System.currentTimeMillis()); //現在日期與時間
        Date lastDateTime;

        if (dateLast.equals("") && timeLast.equals("")){ //可以沒有截止日期
            return true;
        }else{
            try {
                lastDateTime = sdFormat.parse(dateLast+" "+timeLast); //截止日期與時間
                //若截止時間比現在時間小
                if (lastDateTime.before(nowDateTime)){
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return true;
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

            //-----檢查是否有輸入計畫名稱-----
            if (edt_list_name.getText().toString().equals("")) {
                Toast.makeText(v.getContext(), "請輸入計畫名稱", Toast.LENGTH_SHORT).show();
            }else if (!isTimeCheck()){
                    Toast.makeText(v.getContext(), "截止期限不可為過去時間", Toast.LENGTH_SHORT).show();
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
            if (isDataCompleted() && isTimeCheck()) { //---資料填完才能預送---
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
            }else if(!isTimeCheck()){
                Toast.makeText(v.getContext(), "送禮期限不可為過去時間", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(v.getContext(), "您尚有計畫細節未完成喔!", Toast.LENGTH_SHORT).show();
            }
        }

    };

    //------------------------------上傳plan資料
    public void uploadPlan(String store){
        //---upload giftRecord
        Log.v("selectFriendIds.size", String.valueOf(selectFriendIds.size()));
        if(selectFriendIds.size()>0){
            for (int i = 0 ; i < selectFriendIds.size(); i++) {
                giftRecordInsertAsyncTask giftRecordInsertAsyncTask = new giftRecordInsertAsyncTask(new giftRecordInsertAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {

                    }
                });
                giftRecordInsertAsyncTask.execute(Common.insertMisPlan, sender, selectFriendIds.get(i), planid, store, planType);
            }
        }else {
            giftRecordInsertAsyncTask giftRecordInsertAsyncTask = new giftRecordInsertAsyncTask(new giftRecordInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            giftRecordInsertAsyncTask.execute(Common.insertMisPlan, sender, "", planid, store, planType);
        }
        Log.v("giftRecord", "//---upload giftRecord");

        //---upload missionPlan
        missionPlanInsertAsyncTask missionPlanInsertAsyncTask = new missionPlanInsertAsyncTask(new missionPlanInsertAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        Log.v("deadline", edt_list_lastDate.getText().toString()+" "+edt_list_lastTime.getText().toString());
        missionPlanInsertAsyncTask.execute(Common.insertMisPlan, planid, edt_list_name.getText().toString(), dateTime, edt_list_sentDate.getText().toString(), edt_list_lastDate.getText().toString()+" "+edt_list_lastTime.getText().toString());
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
        super.onResume();
    }

    //------------------------------計畫詳細，顯示plan資料------------------------------
    private void showPlanDetail(String planid){
        planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(MakePlanListActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray;

                    //----------------------------取得計畫資料----------------------------
                    jsonArray = object.getJSONArray("misPlan");
                    //String misPlanid =jsonArray.getJSONObject(0).getString("misid"); //計畫ID
                    //String misCreateDate = jsonArray.getJSONObject(0).getString("createDate"); //計畫建立日期
                    String misPlanName =jsonArray.getJSONObject(0).getString("misPlanName"); //計畫名稱
                    String misSendPlanDate = jsonArray.getJSONObject(0).getString("sendPlanDate").substring(0,10); //送禮日期
                    String deadline = jsonArray.getJSONObject(0).getString("deadline"); //截止日期時間
                    String lastDate = deadline.substring(0,10); //截止日期
                    String lastTime = deadline.substring(11,16); //截止時間

                    if (misSendPlanDate.equals("0000-00-00")) misSendPlanDate="";  //若為0則顯示空值
                    //若為0則顯示空值
                    if (deadline.equals("0000-00-00 00:00:00")){
                        lastDate="";
                        lastTime="";
                    }

                    edt_list_name.setText(misPlanName); //計畫名稱
                    edt_list_sentDate.setText(misSendPlanDate); //送禮日期
                    edt_list_lastDate.setText(lastDate); //截止日期
                    edt_list_lastTime.setText(lastTime); //截止時間

                    //----------------------------取得好友資料----------------------------
                    jsonArray = object.getJSONArray("record");
                    int friendsLength = jsonArray.length();;

                    String friendName = "";
                    for (int i = 0; i < friendsLength; i++) {
                        selectFriendIds.add(jsonArray.getJSONObject(i).getString("receiverid")); //好友ID

                        if (friendName.equals("")) friendName += jsonArray.getJSONObject(i).getString("nickname");
                        else friendName += " , " + jsonArray.getJSONObject(i).getString("nickname");

                        //---好友checkbox---
                        for (int k=0; k<list_friendcheckedItems.length; k++){
                            if (getFriendList.getFriendid(k).equals(selectFriendIds.get(i)) ){
                                list_friendcheckedItems[k]=true;
                                tempFriendChecked[k]=true;
                            }
                        }
                    }
                    if (friendName.equals("null")) friendName="";  //若為null則顯示空值
                    edt_list_friend.setText(friendName); //好友名稱

                    //----------------------------取得禮物資料----------------------------
                    jsonArray = object.getJSONArray("misList");
                    int misListLength = jsonArray.length();

                    String giftName = "";
                    for (int i = 0 ; i < misListLength ; i++){
                        //String misListid = jsonArray.getJSONObject(i).getString("misid"); //計畫ID
                        //String misGift = jsonArray.getJSONObject(i).getString("gift"); //禮物內容
                        selectGiftIds.add(jsonArray.getJSONObject(i).getString("giftid")); //禮物ID

                        if (giftName.equals("")) giftName += jsonArray.getJSONObject(i).getString("giftName");
                        else giftName += " , " + jsonArray.getJSONObject(i).getString("giftName");

                        //---禮物checkbox---
                        for (int k=0; k<list_giftcheckedItems.length; k++){
                            if (getGiftList.getGiftid(k).equals(selectGiftIds.get(i)) ){
                                list_giftcheckedItems[k]=true;
                                tempGiftChecked[k]=true;
                            }
                        }
                    }
                    edt_list_giftName.setText(giftName);

                    //----------------------------取得任務項目----------------------------
                    jsonArray = object.getJSONArray("misItem");
                    int misItemLength = jsonArray.length();

                    for (int i = 0 ; i < misItemLength ; i++){
                        mData.add(jsonArray.getJSONObject(i).getString("content")); //項目內容
                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(MakePlanListActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.planList , sender, planid);
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

