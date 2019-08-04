package com.ntubcase.gift;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.giftInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.gift.giftInsertCodeAsyncTask;
import com.ntubcase.gift.data.getGiftList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MakeGiftCodeActivity extends AppCompatActivity {
    private Button btn_add, btn_save, btn_makePlan;
    private static EditText et_giftName, et_mainCode, et_matchCode;

    private static String giftName, giftContent, mainCode, matchCode;

    protected static Date date =new Date();
    protected static String owner = "wayne";
    protected static String dateTime, giftType;
    ProgressDialog barProgressDialog;

    //private final int H = ViewGroup.LayoutParams.WRAP_CONTENT;
    //private final int W = ViewGroup.LayoutParams.MATCH_PARENT;
    private TableLayout tableLayout;
    private TableRow tabRow;
    private TextView tv;
    private ArrayList<String> maincodes = new ArrayList<>();
    private ArrayList<String> matchcodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_gift_code);
        setTitle("製作密碼表");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //---------------------------------------------------------------------------------
        et_giftName = (EditText) findViewById(R.id.et_giftName);
        et_mainCode = (EditText) findViewById(R.id.et_mainCode);
        et_matchCode = (EditText) findViewById(R.id.et_matchCode);
        btn_add = findViewById(R.id.btn_add);
        btn_save = findViewById(R.id.btn_save);
        btn_makePlan = (Button) findViewById(R.id.btn_makePlan);
        btn_add.setOnClickListener(addClickListener); //設置監聽器
        btn_save.setOnClickListener(saveClickListener); //設置監聽器
        btn_makePlan.setOnClickListener(makePlanClickListener); //設置監聽器

        //---------------------------------------------------------------------------------
        maincodes.add("秘密代碼");
        matchcodes.add("對應訊息");

        tableLayout = (TableLayout) findViewById(R.id.tab_01);
        //控制行數
        for (int row = 0; row < matchcodes.size(); row++) {

            tabRow = new TableRow(this);
            //控制列數
            for (int col = 0 ; col< 3; col++){

                tv = new TextView(this);
                if(col == 0) tv.setText(maincodes.get(row));
                if(col == 1) tv.setText(" = ");
                if(col == 2) tv.setText(matchcodes.get(row));

                tv.setGravity(Gravity.CENTER);
                tabRow.addView(tv);

            }
            tableLayout.addView(tabRow);
        }

        //------------禮物詳細，判斷禮物是否有初值
//        try{
//            Bundle bundle = this.getIntent().getExtras();
//            //position 代表第幾個禮物的位置(按照giftActivity的順序排) EX: 第一筆是粽子(position = 0) ，第二筆是湯圓(position = 1)
//            int position =Integer.valueOf( bundle.getString("position"));
//
//            //-------存入禮物詳細的editText
//            et_giftName.setText( getGiftList.getGiftName(position));
//            et_giftContent.setText(getGiftList.getGift(position));
//            //--------
//
//        }catch (Exception e){
//
//        }
        //------------禮物詳細結束
    }

    //-------------------------------add按鈕 監聽器----------------------------------------
    private View.OnClickListener addClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainCode = et_mainCode.getText().toString();    //取得使用者輸入的禮物名稱
            matchCode = et_matchCode.getText().toString();
            giftType = "5";
            //--------取得目前時間：yyyy/MM/dd hh:mm:ss
            Date date = new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            dateTime = sdFormat.format(date);

            maincodes.add(mainCode);
            matchcodes.add(matchCode);

            //控制行數
            //for (int row = 0; row < matchcodes.size(); row++) {

                tabRow = new TableRow(getApplicationContext());
                //控制列數
                for (int col = 0 ; col< 3; col++){

                    tv = new TextView(getApplicationContext());
                    if(col == 0) tv.setText(maincodes.get(maincodes.size()-1));
                    if(col == 1) tv.setText(" = ");
                    if(col == 2) tv.setText(matchcodes.get(matchcodes.size()-1));

                    tv.setGravity(Gravity.CENTER);
                    tabRow.addView(tv);

                }
                tableLayout.addView(tabRow);
            //}
            et_mainCode.setText("");
            et_matchCode.setText("");

        }
    };

    //-------------------------------儲存按鈕 監聽器----------------------------------------
    private View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            giftName = et_giftName.getText().toString();    //取得使用者輸入的禮物名稱
            giftType="5";
            //--------取得目前時間：yyyy/MM/dd hh:mm:ss
            Date date =new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            dateTime = sdFormat.format(date);
            SimpleDateFormat sdFormat_giftContent = new SimpleDateFormat("yyyyMMddHHmmss");
            giftContent = sdFormat_giftContent.format(date);
            Log.v("giftType", giftType);

            giftInsertAsyncTask mgiftInsertAsyncTask = new giftInsertAsyncTask(new giftInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            mgiftInsertAsyncTask.execute(Common.insertGift , giftContent, dateTime ,giftName ,owner, giftType);
            Log.v("maincodes", String.valueOf(maincodes));
            Log.v("matchcodes", String.valueOf(matchcodes));

            for (int i = 1 ; i< maincodes.size(); i++){
                Log.v("maincodes.get(i + 1)", maincodes.get(i));
                Log.v("matchcodes.get(i + 1)", matchcodes.get(i));

                giftInsertCodeAsyncTask mgiftInsertCodAsyncTask = new giftInsertCodeAsyncTask(new giftInsertCodeAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {

                    }
                });
                mgiftInsertCodAsyncTask.execute(Common.insertGiftCode , giftContent, maincodes.get(i), matchcodes.get(i));

            }


            //-------------讀取Dialog-----------
            barProgressDialog = ProgressDialog.show(MakeGiftCodeActivity.this,
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
            giftType="5";
            //--------取得目前時間：yyyy/MM/dd hh:mm:ss
            Date date =new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            dateTime = sdFormat.format(date);
            SimpleDateFormat sdFormat_giftContent = new SimpleDateFormat("yyyyMMddHHmmss");
            giftContent = sdFormat_giftContent.format(date);
            Log.v("giftType", giftType);

            giftInsertAsyncTask mgiftInsertAsyncTask = new giftInsertAsyncTask(new giftInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            mgiftInsertAsyncTask.execute(Common.insertGift , giftContent, dateTime ,giftName ,owner, giftType);
            Log.v("maincodes", String.valueOf(maincodes));
            Log.v("matchcodes", String.valueOf(matchcodes));

            for (int i = 1 ; i< maincodes.size(); i++){
                Log.v("maincodes.get(i + 1)", maincodes.get(i));
                Log.v("matchcodes.get(i + 1)", matchcodes.get(i));

                giftInsertCodeAsyncTask mgiftInsertCodAsyncTask = new giftInsertCodeAsyncTask(new giftInsertCodeAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {

                    }
                });
                mgiftInsertCodAsyncTask.execute(Common.insertGiftCode , giftContent, maincodes.get(i), matchcodes.get(i));

            }

            Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
            Intent intent;
            intent = new Intent(MakeGiftCodeActivity.this, PlanActivity.class);
            startActivity(intent);
            finish();
        }
    };

    /*
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
*/

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