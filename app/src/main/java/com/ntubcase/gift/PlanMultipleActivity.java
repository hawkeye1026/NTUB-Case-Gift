package com.ntubcase.gift;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.PlanMultiAdapter;
import com.ntubcase.gift.data.getGiftList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanMultipleActivity extends AppCompatActivity {

    private GridView gridView;
    private PlanMultiAdapter planMultiAdapter;
    private TextView tv_receiveFriend;

    private String planName, receiveFriend, startDate, endDate, message; //------bundle傳遞的資料
    private Date dateStart, dateEnd;
    private List<Map<String, Object>> selectDates; //選取的時間區段

    //-----cutomlayout內物件
    private EditText alert_message;
    private EditText alert_time;
    private EditText alert_gifts;

    //選擇禮物 使用的變數宣告---------------------------------------------------------------------------
    private String[] giftItemList = new String[getGiftList.getGiftLength()];
    private boolean[][] mCheckedItems;
    private boolean[][] tempCheckedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_multiple);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //-----------------------------------------------------------------------------------------
        Bundle bundle = getIntent().getExtras();
        planName = bundle.getString("planName");
        receiveFriend = bundle.getString("receiveFriend");
        startDate = bundle.getString("startDate");
        endDate = bundle.getString("endDate");
        message = bundle.getString("message");

        setTitle(planName); //-----標題為計畫名稱-----
        tv_receiveFriend = (TextView) findViewById(R.id.tv_receiveFriend); //-----顯示收禮人-----
        tv_receiveFriend.setText("To. " + receiveFriend);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateStart = sdf.parse(startDate);
            dateEnd = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<String> allDates = findDates(dateStart, dateEnd);  //取得兩個日期間所有日期
        selectDates = new ArrayList<Map<String, Object>>();
        Map<String, Object> mDates;

        for(int i=0; i < allDates.size(); i++) {
            mDates = new HashMap<String, Object>();
            mDates.put("date", allDates.get(i));
            mDates.put("message", "");
            mDates.put("time", "");
            mDates.put("gifts", "");
            selectDates.add(mDates);
        }

        //------選擇禮物用-----
        for(int i = 0 ; i < getGiftList.getGiftLength();i++){
            giftItemList[i] = getGiftList.getGiftName(i);  //禮物名稱資料
        }
        mCheckedItems = new boolean[selectDates.size()][giftItemList.length];
        tempCheckedItems = new boolean[selectDates.size()][giftItemList.length];


        //---------------------------------GridView---------------------------------------------
        gridView = (GridView) findViewById(R.id.gridView);
        planMultiAdapter = new PlanMultiAdapter(this, selectDates);
        gridView.setAdapter(planMultiAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialog(view,position,parent);  //顯示alertDialog
            }
        });
    }

    //-----------------顯示alertDialog-----------------
    private void showAlertDialog(final View view, int position, final ViewGroup parent) {
        final int gridPosition = position;

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("請輸入該日規劃");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.plan_multi_alert_layout, null);
        builder.setView(customLayout);

        //----------------------------------------設定cutomlayout內顯示的資料--------------------------------------------------
        alert_message  = customLayout.findViewById(R.id.alert_message);
        alert_time  = customLayout.findViewById(R.id.alert_time);
        alert_gifts  = customLayout.findViewById(R.id.alert_gifts);

        alert_message.setText(selectDates.get(gridPosition).get("message").toString());
        alert_time.setText(selectDates.get(gridPosition).get("time").toString());
        alert_gifts.setText(selectDates.get(gridPosition).get("gifts").toString());

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

        //----------------選擇禮物---------------
        alert_gifts.setInputType(InputType.TYPE_NULL);
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
        //--------------------------------------------------------------------------------------------------------------------------------


        //-------------alert按鈕-------------
        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {   // send data from the AlertDialog to the Activity
                sendDialogDataToActivity(view, gridPosition, parent, alert_message.getText().toString()
                        , alert_time.getText().toString(), alert_gifts.getText().toString());
            }
        });

        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //--------------------- 處理AlertDialog回傳的資料-----------------------------
    private void sendDialogDataToActivity(View view, int gridPosition, ViewGroup parent, String newMessage, String newTime, String newGifts) {
        Map<String, Object> updateData = selectDates.get(gridPosition);
        updateData.put("message", newMessage);
        updateData.put("time", newTime);
        updateData.put("gifts", newGifts);

        selectDates.set(gridPosition, updateData);
        planMultiAdapter.notifyDataSetChanged(); //更新資料
        planMultiAdapter.setItemHeight(view, gridPosition, parent); //設定grid高度
    }


    //--------------顯示TimePicker------------------
    private void showTimePickerDialog () {
        Calendar t = Calendar.getInstance();
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

    //--------------顯示選擇禮物dialog------------------
    private void showGiftsDialog(int position){
        final int gridPosition = position;

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("選擇禮物");

        mBuilder.setMultiChoiceItems(giftItemList, mCheckedItems[gridPosition], new DialogInterface.OnMultiChoiceClickListener() {
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
                    if(mCheckedItems[gridPosition][i]){
                        if (mSelectGifts.equals("")) mSelectGifts += giftItemList[i];
                        else mSelectGifts += " , " + giftItemList[i];
                    }
                    tempCheckedItems[gridPosition][i]=mCheckedItems[gridPosition][i];
                }
                alert_gifts.setText(mSelectGifts);
            }
        });

        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //取消鈕
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i=0; i<giftItemList.length; i++) mCheckedItems[gridPosition][i]=tempCheckedItems[gridPosition][i];
            }
        });

        mBuilder.setNeutralButton("清除", new DialogInterface.OnClickListener() {   //清除鈕
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < giftItemList.length; i++) {
                    mCheckedItems[gridPosition][i] = false;
                    tempCheckedItems[gridPosition][i] = false;
                }
                alert_gifts.setText("");
            }
        });


        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //--------------取得兩個日期間所有日期------------------
    public List<String> findDates(Date dBegin, Date dEnd){
        List<String> lDate = new ArrayList<String>();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        lDate.add(sd.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }
        return lDate;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
