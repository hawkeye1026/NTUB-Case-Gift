package com.ntubcase.gift;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.giftInsertAsyncTask;
import com.ntubcase.gift.data.getGiftList;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MakeGiftMessageActivity extends AppCompatActivity {

    private Button btn_save, btn_makePlan;
    private static EditText et_giftName, et_giftContent;

    private static String giftName, giftContent;

    protected static Date date =new Date();
    protected static String owner = "wayne";
    protected static String dateTime, giftType;
    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_gift_message);
        setTitle("製作悄悄話");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------------------------------------------------------------------
        et_giftName = (EditText) findViewById(R.id.et_giftName);
        et_giftContent = (EditText) findViewById(R.id.et_giftContent);
        btn_save = findViewById(R.id.btn_save);
        btn_makePlan = (Button) findViewById(R.id.btn_makePlan);
        btn_save.setOnClickListener(saveClickListener); //設置監聽器
        btn_makePlan.setOnClickListener(makePlanClickListener); //設置監聽器

    }

    //-------------------------------儲存按鈕 監聽器----------------------------------------
    private View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            giftName = et_giftName.getText().toString();    //取得使用者輸入的禮物名稱
            giftContent = et_giftContent.getText().toString();    //取得使用者輸入的禮物內容
            giftType="3";
            //--------取得目前時間：yyyy/MM/dd hh:mm:ss
            Date date =new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            dateTime = sdFormat.format(date);


            giftInsertAsyncTask mgiftInsertAsyncTask = new giftInsertAsyncTask(new giftInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            mgiftInsertAsyncTask.execute(Common.insertGift , giftContent, dateTime ,giftName ,owner, giftType);

            //-------------讀取Dialog-----------
            barProgressDialog = ProgressDialog.show(MakeGiftMessageActivity.this,
                    "讀取中", "請等待...",true);
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        getGiftList.getJSON();
                        Thread.sleep(1000);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    finally{
                        barProgressDialog.dismiss();
                        finish();
                    }
                }
            }).start();

            Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
        }
    };

    //-------------------------------製作計畫按鈕 監聽器----------------------------------------
    private View.OnClickListener makePlanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            giftName = et_giftName.getText().toString();    //取得使用者輸入的禮物名稱
            giftContent = et_giftContent.getText().toString();    //取得使用者輸入的禮物內容
            giftType="3";
            //--------取得目前時間：yyyy/MM/dd hh:mm:ss
            Date date =new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            dateTime = sdFormat.format(date);


            giftInsertAsyncTask mgiftInsertAsyncTask = new giftInsertAsyncTask(new giftInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            mgiftInsertAsyncTask.execute(Common.insertGift , giftContent, dateTime ,giftName ,owner,giftType);

            Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
            Intent intent;
            intent = new Intent(MakeGiftMessageActivity.this, PlanActivity.class);
            startActivity(intent);
            finish();
        }
    };

    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); //網路
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}