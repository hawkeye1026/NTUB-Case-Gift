package com.ntubcase.gift;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.giftRecordInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.singleListInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.singlePlanInsertAsyncTask;
import com.ntubcase.gift.data.getFriendList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SendGiftDirectlyActivity extends AppCompatActivity {

    private EditText edt_direct_name, edt_direct_friend, edt_direct_message;
    private Button btn_send;

    private String sender= "1", planid, planType="1", dateTime;

    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] single_friendlistItems = new String[getFriendList.getFriendLength()];
    boolean[] single_friendcheckedItems, tempFriendChecked;
    private ArrayList<String> selectFriendIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_gift_directly);
        setTitle("直接送禮");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------------------------------------------------------------------
        edt_direct_name = findViewById(R.id.add_direct_name);
        edt_direct_friend = findViewById(R.id.add_direct_friend);
        edt_direct_message = findViewById(R.id.add_direct_message);
        btn_send = findViewById(R.id.btn_directly_send);
        btn_send.setOnClickListener(planSendClickListener);  //-----------送出計畫

        //---------------------------------------------------------------------------------
        //選擇好友使用的變數宣告--------------------------------------------------------------------------- 好友資料
        for (int i = 0; i < getFriendList.getFriendLength(); i++) {
            single_friendlistItems[i] = getFriendList.getFriendName(i);
        }
        single_friendcheckedItems = new boolean[single_friendlistItems.length];
        tempFriendChecked = new boolean[single_friendlistItems.length];

        //點選選擇好友EditText跳出選擇好友選擇器------------------------------------------------------------------------
        edt_direct_friend.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edt_direct_friend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Showfrienddialog();
                }
            }
        });
        edt_direct_friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Showfrienddialog();
            }
        });

    }

    //設定選擇好友EditText傳入值---------------------------------------
    private void Showfrienddialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SendGiftDirectlyActivity.this);
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
                edt_direct_friend.setText(item);
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
                edt_direct_friend.setText("");
                selectFriendIds = new ArrayList<>();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //----------------檢查必填資料是否填完----------------
    private boolean isDataCompleted(){
        String planName = edt_direct_name.getText().toString();
        String receiveFriend = edt_direct_friend.getText().toString();

        if (!planName.equals("") && !receiveFriend.equals("")){
            return true;
        }
        return false;
    }

    //-------------------------------直接送禮按鈕 監聽器----------------------------------------
    private View.OnClickListener planSendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Date date = new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateTime = sdFormat.format(date);
            SimpleDateFormat sdFormat_giftContent = new SimpleDateFormat("yyyyMMddHHmmss");

            if (isDataCompleted()){ //---資料填完才能預送---
                planid = "mis_" + sdFormat_giftContent.format(date);

                //---upload giftRecord
                Log.v("selectFriendIds.size", String.valueOf(selectFriendIds.size()));
                if(selectFriendIds.size()>0){
                    for (int i = 0 ; i < selectFriendIds.size(); i++) {
                        giftRecordInsertAsyncTask giftRecordInsertAsyncTask = new giftRecordInsertAsyncTask(new giftRecordInsertAsyncTask.TaskListener() {
                            @Override
                            public void onFinished(String result) {

                            }
                        });
                        giftRecordInsertAsyncTask.execute(Common.insertMisPlan, planid, selectFriendIds.get(i));
                    }
                }
                Log.v("giftRecord", "//---upload giftRecord");

                singlePlanInsertAsyncTask singlePlanInsertAsyncTask = new singlePlanInsertAsyncTask(new singlePlanInsertAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {

                    }
                });
                singlePlanInsertAsyncTask.execute(Common.insertSinPlan, planid, edt_direct_name.getText().toString(), dateTime, dateTime, sender, "1", planType);
                Log.v("singlePlan", "//---upload singlePlan");

                singleListInsertAsyncTask singleListInsertAsyncTask = new singleListInsertAsyncTask(new singleListInsertAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {

                    }
                });
                singleListInsertAsyncTask.execute(Common.insertSinPlan, planid," "/*giftid*/, dateTime, edt_direct_message.toString());

                Toast.makeText(v.getContext(), "已送禮!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(v.getContext(), "您尚有計畫細節未完成喔!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
