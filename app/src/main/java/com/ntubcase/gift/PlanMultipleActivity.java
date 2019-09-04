package com.ntubcase.gift;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.PlanMultiAdapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.giftRecordInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.multipleListInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.multiplePlanInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.data.getGiftList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanMultipleActivity extends AppCompatActivity {
    private Button btn_plan_save, btn_plan_send;
    private GridView gridView;
    private PlanMultiAdapter planMultiAdapter;
    private TextView tv_receiveFriend, tv_message, tv_sender;

    private String planName, receiveFriend, startDate, endDate, message; //------bundle傳遞的資料
    private ArrayList<String> receiveFriendId; //------bundle傳遞的資料

    private String sender= "1", planid, planType="2", dateTime, date_time, goal;
    ProgressDialog barProgressDialog;

    private List<Map<String, Object>> selectDates = new ArrayList<Map<String, Object>>();  //選取的時間區段
    private SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm");

    //-----cutomlayout內物件
    private EditText alert_message, alert_time, alert_gifts;
    private LinearLayout ll_time;

    //選擇禮物 使用的變數宣告---------------------------------------------------------------------------
    private String[] giftItemList = new String[getGiftList.getGiftLength()];  //所有禮物
    private boolean[] mCheckedItems;
    private List<List<String>> mSelectGiftIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_multiple);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //---------------------------------------------------------------------------------
        btn_plan_save = findViewById(R.id.btn_plan_save);
        btn_plan_send = (Button) findViewById(R.id.btn_plan_send);
        btn_plan_save.setOnClickListener(planSaveClickListener); //設置監聽器
        btn_plan_send.setOnClickListener(planSendClickListener); //設置監聽器

        //---------------------------------上一頁資料-----------------------------------
        Bundle bundle = getIntent().getExtras();
        planName = bundle.getString("planName");    //計畫名稱
        receiveFriend = bundle.getString("receiveFriend");  //收禮人名稱
        receiveFriendId = bundle.getStringArrayList("receiveFriendId"); //收禮人ID
        if (receiveFriendId==null) receiveFriendId = new ArrayList<>();
        startDate = bundle.getString("startDate");  //起始日期
        endDate = bundle.getString("endDate");  //結束日期
        message = bundle.getString("message");  //祝福
        selectDates = (ArrayList<Map<String, Object>>) bundle.getSerializable("selectDates");  //選取的時間區段

        setTitle(planName); //-----標題為計畫名稱-----
        tv_receiveFriend = (TextView) findViewById(R.id.tv_receiveFriend);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_sender = (TextView) findViewById(R.id.tv_sender);
        tv_receiveFriend.setText("To. " + receiveFriend);   //-----顯示收禮人-----
        tv_message.setText(message); //-----顯示祝福-----
        tv_sender.setText("From. " + sender); //-----顯示送禮人-----

        //------選擇禮物用-----
        for(int i = 0 ; i < getGiftList.getGiftLength();i++){
            giftItemList[i] = getGiftList.getGiftName(i);  //禮物名稱資料
        }

        //---------------------------------GridView---------------------------------------------
        gridView = (GridView) findViewById(R.id.gridView);
        planMultiAdapter = new PlanMultiAdapter(this, selectDates);
        gridView.setAdapter(planMultiAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialog(position,parent);  //顯示alertDialog
            }
        });

        //----------------檢查必填資料是否填完----------------
        if (isDataCompleted()) btn_plan_send.setVisibility(View.VISIBLE);
    }

    //-----------------顯示alertDialog-----------------
    private void showAlertDialog(int position, final ViewGroup parent) {
        final int gridPosition = position;

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] mdate = selectDates.get(gridPosition).get("date").toString().split("-");
        builder.setTitle("請輸入"+ mdate[1] + "月" + mdate[2] +"日規劃");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.plan_multi_alert_layout, null);
        builder.setView(customLayout);

        //----------------------------------------設定customLayout內顯示的資料--------------------------------------------------
        alert_message  = customLayout.findViewById(R.id.alert_message);
        alert_time  = customLayout.findViewById(R.id.alert_time);
        alert_gifts  = customLayout.findViewById(R.id.alert_gifts);
        ll_time = customLayout.findViewById(R.id.ll_time);

        alert_message.setText(selectDates.get(gridPosition).get("message").toString());
        alert_time.setText(selectDates.get(gridPosition).get("time").toString());
        alert_gifts.setText(selectDates.get(gridPosition).get("gifts").toString());

        alert_message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);  //---關閉軟鍵盤---
                    }
                }
            }
        });

        if ((alert_gifts.getText().toString()).equals(""))
            ll_time.setVisibility(View.GONE); //若禮物空白則不能選時間

        //----------------選擇禮物---------------
        alert_gifts.setInputType(InputType.TYPE_NULL);

        //---取得position原選取資料---
        boolean[] checked = (boolean[]) selectDates.get(position).get("mCheckedItems");
        mCheckedItems = Arrays.copyOf(checked, checked.length);

        alert_gifts.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showGiftsDialog(gridPosition);
                }
            }
        });

        alert_gifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGiftsDialog(gridPosition);
            }
        });

        //----------------選擇時間---------------
        alert_time.setInputType(InputType.TYPE_NULL);
        alert_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePickerDialog(); //顯示TimePicker
                }
            }
        });

        alert_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(); //顯示TimePicker
            }
        });
        //--------------------------------------------------------------------------------------------------------------------------------

        //-------------alert按鈕-------------
        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {   // send data from the AlertDialog to the Activity
                //------儲存輸入的資料-----
                sendDialogDataToActivity(gridPosition, alert_message.getText().toString()
                        , alert_time.getText().toString(), alert_gifts.getText().toString(),mCheckedItems);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setNeutralButton("刪除規劃", null);

        // create and show the alert dialog
        final AlertDialog mDialog = builder.create();
        mDialog.show();

        mDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PlanMultipleActivity.this)
                        .setTitle("您確定要刪除此規劃嗎?")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendDialogDataToActivity(gridPosition, "", ""
                                        , "",new boolean[giftItemList.length]);

                                mDialog.dismiss();
                            }
                        })
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

    }

    //--------------------- 處理AlertDialog回傳的資料-----------------------------
    private void sendDialogDataToActivity(int gridPosition, String newMessage, String newTime, String newGifts, boolean[] mCheckedItems) {
        Map<String, Object> updateData = selectDates.get(gridPosition);
        updateData.put("message", newMessage);
        updateData.put("time", newTime);
        updateData.put("gifts", newGifts);
        updateData.put("mCheckedItems", mCheckedItems);
        selectDates.set(gridPosition, updateData);  //更新item資料

        planMultiAdapter.refreshOneView(gridView,gridPosition); //刷新item
    }

    //--------------顯示選擇禮物dialog------------------
    private void showGiftsDialog(int position){
        final int gridPosition = position;

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("選擇禮物");

        //---暫存資料以備取消後復原---
        final boolean[] tempCheckedItems = Arrays.copyOf(mCheckedItems, mCheckedItems.length);

        mBuilder.setMultiChoiceItems(giftItemList, mCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() { //確認鈕
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String mSelectGifts ="";

                for (int i = 0; i < giftItemList.length; i++) {
                    if(mCheckedItems[i]){
                        if (mSelectGifts.equals("")) mSelectGifts += giftItemList[i];
                        else mSelectGifts += " , " + giftItemList[i];
                    }
                }
                alert_gifts.setText(mSelectGifts);

                //---禮物有值才可選時間---
                if (mSelectGifts.equals("")){
                    ll_time.setVisibility(View.GONE);
                    alert_time.setText("");
                }else{  //時間預設為上午十二點
                    ll_time.setVisibility(View.VISIBLE);
                    if ((alert_time.getText().toString()).equals("")) alert_time.setText("00:00");
                }
            }
        });

        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //取消鈕
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i=0; i<giftItemList.length; i++)
                    mCheckedItems[i]=tempCheckedItems[i];
            }
        });

        mBuilder.setNeutralButton("清除", new DialogInterface.OnClickListener() {   //清除鈕
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCheckedItems = new boolean[giftItemList.length];
                alert_gifts.setText("");
                ll_time.setVisibility(View.GONE);  //---沒有禮物不能選時間
                alert_time.setText("");
            }
        });


        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //--------------顯示TimePicker------------------
    private void showTimePickerDialog () {
        Calendar t = Calendar.getInstance();

        try {
            String oldTime = alert_time.getText().toString();  //取得上次選的時間
            if (!oldTime.equals("")) t.setTime(sdfT.parse(oldTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                alert_time.setText(dateAdd0(hourOfDay) + ":" + dateAdd0(minute));
            }
        }, t.get(Calendar.HOUR_OF_DAY), t.get(Calendar.MINUTE),false).show();
    }

    //--------------時間加0------------------
    private String dateAdd0(int date){
        if(date <10){
            return "0"+date;
        }else{
            return String.valueOf(date);
        }
    }

    //----------------檢查必填資料是否填完----------------
    private boolean isDataCompleted(){
        //---檢查選取的日期期間內有沒有填資料---
        boolean isDatesHaveData=false;
        for (int i=0; i<selectDates.size(); i++){
            String message =(String)selectDates.get(i).get("message");
            String gifts =(String)selectDates.get(i).get("gifts");
            if (!message.equals("") || !gifts.equals("")){ //留言或是禮物有填資料
                isDatesHaveData = true;
                break;
            }
        }

        if (!planName.equals("") && !receiveFriend.equals("") && isDatesHaveData){
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
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            dateTime = sdFormat.format(date);

            SimpleDateFormat sdFormat_giftContent = new SimpleDateFormat("yyyyMMddHHmmss");
            planid = "mul_" + sdFormat_giftContent.format(date);
            Log.v("receiveFriendId.size", String.valueOf(receiveFriendId.size()));

            //-----檢查是否有輸入計畫名稱-----
            if (planName.equals("")) {
                Toast.makeText(v.getContext(), "請輸入計畫名稱", Toast.LENGTH_SHORT).show();
            }else if (btn_plan_send.getVisibility()==View.GONE && isDataCompleted()){  //----若預送按鈕尚未出現 並填完必填資料---
                btn_plan_send.setVisibility(View.VISIBLE);
                new AlertDialog.Builder(PlanMultipleActivity.this)
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
    //-------------------------------結束儲存按鈕 監聽器----------------------------------------

    //-------------------------------預送禮物按鈕 監聽器----------------------------------------
    private View.OnClickListener planSendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isDataCompleted()){ //---資料填完才能預送---
                //取得目前時間：yyyy/MM/dd hh:mm:ss
                Date date = new Date();
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                dateTime = sdFormat.format(date);

                SimpleDateFormat sdFormat_giftContent = new SimpleDateFormat("yyyyMMddHHmmss");
                planid = "mul_" + sdFormat_giftContent.format(date);

                uploadPlan("1");
                Toast.makeText(v.getContext(), "已預送!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(v.getContext(), "您尚有計畫細節未完成喔!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //-------------------------------結束預送禮物按鈕 監聽器----------------------------------------

    //------------------------------上傳plan資料
    public void uploadPlan(String store){
        //---upload giftRecord
        Log.v("receiveFriendId.size", String.valueOf(receiveFriendId.size()));
        for (int i = 0 ; i < receiveFriendId.size(); i++) {
            giftRecordInsertAsyncTask giftRecordInsertAsyncTask = new giftRecordInsertAsyncTask(new giftRecordInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            giftRecordInsertAsyncTask.execute(Common.insertMulPlan, sender, receiveFriendId.get(i), planid, store, planType);
        }
        Log.v("giftRecord", "//---upload giftRecord");

        //---upload multiplePlan
        multiplePlanInsertAsyncTask multiplePlanInsertAsyncTask = new multiplePlanInsertAsyncTask(new multiplePlanInsertAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        multiplePlanInsertAsyncTask.execute(Common.insertMulPlan, planid, planName, dateTime, startDate, endDate, message);
        Log.v("multiplePlan", "//---upload multiplePlan");

        //--- upload multipleList
        Log.v("selectDates.size", String.valueOf(selectDates.size()));

        //----------取得禮物ID----------
        mSelectGiftIds = new ArrayList<List<String>>();
        for (int i = 0 ; i < selectDates.size(); i++) {
            boolean[] checked = (boolean[]) selectDates.get(i).get("mCheckedItems");
            List<String> giftIds = new ArrayList<>();
            for (int k= 0; k< giftItemList.length; k++){
                if (checked[k]) giftIds.add(getGiftList.getGiftid(k));
            }
            mSelectGiftIds.add(giftIds);
        }

        for (int i = 0 ; i < selectDates.size(); i++) {
            String space=" ";
            date_time = selectDates.get(i).get("date").toString()+space+selectDates.get(i).get("time").toString();  //-- x
            goal = selectDates.get(i).get("message").toString();

            multipleListInsertAsyncTask multipleListInsertAsyncTask = new multipleListInsertAsyncTask(new multipleListInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {
                }
            });

            if (mSelectGiftIds.get(i).size()>0){
                Log.v("mSelectGiftIds.length", ""+mSelectGiftIds.get(i).size());
                for (int j = 0; j < mSelectGiftIds.get(i).size(); j++) {
                    multipleListInsertAsyncTask.execute(Common.insertMulPlan, planid, mSelectGiftIds.get(i).get(j), date_time, goal);
                }
            }else{
                multipleListInsertAsyncTask.execute(Common.insertMulPlan, planid, "0", date_time, goal);
            }
        }
        Log.v("multipleList", "//---upload multipleList");
        
        //-----回前頁結束製作計畫-----
        setResult(FINISH_ACTIVITY);
        finish();
    }

    private static final int FINISH_ACTIVITY = 2;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("selectDates", (Serializable) selectDates);
            intent.putExtras(bundle);

            setResult(RESULT_OK, intent);    //-----回傳資料-----
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
