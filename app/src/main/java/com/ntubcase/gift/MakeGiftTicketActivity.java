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

import com.ntubcase.gift.checkPackage.checkGiftid;
import com.ntubcase.gift.checkPackage.checkRepeatGift;
import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.login_model.userData;

import java.util.Date;

public class MakeGiftTicketActivity extends AppCompatActivity {

    private Button btn_save, btn_makePlan;
    private static EditText et_giftName, et_giftContent;

    private static String giftName, giftContent;

    protected static Date date =new Date();
//    protected static String owner = userData.getUserMail();
    protected static String owner = userData.getUserMail();
    protected static String giftType = "4";
    ProgressDialog barProgressDialog;
    private static int giftid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_gift_ticket);
        setTitle("製作兌換券");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------------------------------------------------------------------
        et_giftName = (EditText) findViewById(R.id.et_giftName);
        et_giftContent = (EditText) findViewById(R.id.et_giftContent);
        btn_save = findViewById(R.id.btn_save);
        btn_makePlan = (Button) findViewById(R.id.btn_makePlan);
        btn_save.setOnClickListener(saveClickListener); //設置監聽器
        btn_makePlan.setOnClickListener(makePlanClickListener); //設置監聽器

        giftid = 0;
        //------------禮物詳細，判斷禮物是否有初值
        Bundle bundle = this.getIntent().getExtras();
        //position 代表第幾個禮物的位置(按照giftActivity的順序排) EX: 第一筆是粽子(position = 0) ，第二筆是湯圓(position = 1)
        int position ;
        giftid =bundle.getInt("giftid");
        position = checkGiftid.checkGiftid(giftid);

        if (position>=0){
            //-------存入禮物詳細的editText
            et_giftName.setText( getGiftList.getGiftName(position));
            et_giftContent.setText(getGiftList.getGift(position));
            //--------
        }
        //------------禮物詳細結束

    }

    //-------------------------------儲存按鈕 監聽器----------------------------------------
    private View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            uploadGift(v);
        }
    };

    //-------------------------------製作計畫按鈕 監聽器----------------------------------------
    private View.OnClickListener makePlanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            uploadGift(v);

            Intent intent;
            intent = new Intent(MakeGiftTicketActivity.this, PlanActivity.class);
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

    public void uploadGift(View v) {
        giftName = et_giftName.getText().toString().trim();    //取得使用者輸入的禮物名稱

        if(checkRepeatGift.checkRepeatGift(giftName)) {
            giftContent = et_giftContent.getText().toString();    //取得使用者輸入的禮物內容

            //--------取得目前時間：yyyy/MM/dd hh:mm:ss

            //------------------------------上傳禮物資料

            if(giftid > 0){
                new updateGift(String.valueOf(giftid),giftContent, giftName, owner, giftType);
            }else{
                new uploadGift(giftContent, giftName, owner, giftType);
            }
            //-------------讀取Dialog-----------
            barProgressDialog = ProgressDialog.show(MakeGiftTicketActivity.this,
                    "讀取中", "請等待...", true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        getGiftList.getJSON();
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        barProgressDialog.dismiss();
                        finish();
                    }
                }
            }).start();

            Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(v.getContext(), "儲存失敗，禮物名稱重複囉", Toast.LENGTH_SHORT).show();
        }
    }
}
