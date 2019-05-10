package com.ntubcase.gift;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
//----------------------API
import com.ntubcase.gift.Common.*;
import com.ntubcase.gift.MyAsyncTask.giftUpdateAsyncTask;
import com.ntubcase.gift.MyAsyncTask.testAsyncTask;
//----------------------
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MakeGiftsActivity extends AppCompatActivity {

    private Button btn_save, btn_makePlan;
    private ImageView iv_giftIcon;
    private static EditText et_giftName, et_giftContent;

    private static String giftName, giftContent;

    protected static String giftType;
    protected static Date date =new Date();
    protected static String owner = "wayne";
    protected static String dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_gifts);
        setTitle("製作禮物");

        Intent intent = this.getIntent();
        giftType = intent.getStringExtra("giftType"); //取得選擇的禮物種類

        switch (giftType){
            case "兌換券":
                giftType = "2";
            case "影片":
                giftType = "1";
            case "照片":
                giftType = "0";
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //------------------------設定icon-------------------------------
        Resources res = getResources();
        String[] mGiftStrings = res.getStringArray(R.array.gift_type);

        iv_giftIcon = (ImageView) findViewById(R.id.iv_giftIcon);
        if(giftType.equals(mGiftStrings[0])){
            iv_giftIcon.setImageResource(R.drawable.ic_gift_camera);
        }else if(giftType.equals(mGiftStrings[1])){
            iv_giftIcon.setImageResource(R.drawable.ic_gift_video);
        }else if(giftType.equals(mGiftStrings[2])){
            iv_giftIcon.setImageResource(R.drawable.ic_gift_ticket);
        }

        //---------------------------------------------------------------------------------
        btn_save = findViewById(R.id.btn_save);
        btn_makePlan = (Button) findViewById(R.id.btn_makePlan);
        et_giftName = (EditText) findViewById(R.id.et_giftName);
        et_giftContent = (EditText) findViewById(R.id.et_giftContent);

        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        dateTime = sdFormat.format(date);
        Log.v("giftName",dateTime);
        //--------

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giftName = et_giftName.getText().toString();    //取得使用者輸入的禮物名稱
                giftContent = et_giftContent.getText().toString();    //取得使用者輸入的禮物內容

                Log.v("log",giftContent +"|"+ dateTime + "|"+giftName +"|"+owner +"|"+ giftType);

                giftUpdateAsyncTask mgiftUpdateAsyncTask = new giftUpdateAsyncTask(new giftUpdateAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {
                    }
                });
                mgiftUpdateAsyncTask.execute(Common.giftUpdate , giftContent, dateTime ,giftName ,owner,giftType);

                testAsyncTask mtestAsyncTask = new testAsyncTask(new testAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {
                    }
                });
                mtestAsyncTask.execute(Common.test , "1232");

                Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        //---------------------------------------------------------------------------------
        btn_makePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }
    public void onStop() {
        getGiftList.getJSON();
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
