package com.ntubcase.gift;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.insert.giftInsertCodeAsyncTask;
import com.ntubcase.gift.checkPackage.checkGiftid;
import com.ntubcase.gift.checkPackage.checkRepeatGift;
import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MakeGiftCodeActivity extends AppCompatActivity {
    private Button btn_add, btn_addMulti, btn_remove, btn_save, btn_directly_send;
    private static EditText et_giftName;

    private static String giftName, giftContent;

    protected static Date date =new Date();
    protected static String owner = userData.getUserID();
    protected static String dateTime;
    protected static String giftType="5";
    ProgressDialog barProgressDialog;

    private TableLayout tableLayout;
    private TableRow tabRow;
    private ArrayList<String> mainCodes = new ArrayList<>();
    private ArrayList<String> matchCodes = new ArrayList<>();
    private String maincode_array = "";
    private String matchcode_array = "";

    private static String decodeid = "";

    private static int giftid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_gift_code);
        setTitle("製作密碼表");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //---------------------------------------------------------------------------------
        et_giftName = (EditText) findViewById(R.id.et_giftName);
        btn_add = findViewById(R.id.btn_add);
        btn_addMulti = findViewById(R.id.btn_addMulti);
        btn_remove = findViewById(R.id.btn_remove);
        btn_save = findViewById(R.id.btn_save);
        btn_directly_send = (Button) findViewById(R.id.btn_directly_send);
        btn_save.setOnClickListener(saveClickListener); //設置監聽器
        btn_directly_send.setOnClickListener(directlySendClickListener); //設置監聽器
        tableLayout = (TableLayout) findViewById(R.id.tab_01);

        btn_add.setOnClickListener(new View.OnClickListener() { //設置 新增一行按鈕 監聽器
            @Override
            public void onClick(View v) {
                tableAddRow(1,"","");
            }
        });
        btn_addMulti.setOnClickListener(addMultiClickListener); //設置監聽器
        btn_remove.setOnClickListener(removeClickListener); //設置監聽器
        //---------------------------------------------------------------------------------

        //------------判斷禮物是否有初值------------
        Bundle bundle = this.getIntent().getExtras();
        int position ;
        giftid =bundle.getInt("giftid");
        position = checkGiftid.checkGiftid(giftid);

        if (position>=0){  //-----顯示禮物詳細-----
            et_giftName.setText( getGiftList.getGiftName(position)); //禮物名稱

            //---資料內容放進表格---
            decodeid=getGiftList.getGift(position);
            for (int i=0; i<(getGiftList.getDecodeLength()); i++){
                if (getGiftList.getDecodeid(i).equals(decodeid))
                    tableAddRow(1,getGiftList.getDecodeMaincode(i),getGiftList.getDecodeMatchCode(i));
            }

        }else{  //-----若為新增則顯示範例行-----
            tabRow = new TableRow(getApplicationContext()); //---範例行
            for (int col = 0 ; col< 2; col++){
                EditText editText = new EditText(getApplicationContext());

                //editText.setBackgroundColor(Color.WHITE);
                editText.setTextColor(Color.rgb(135,51,36));
                editText.setBackgroundResource(R.drawable.bg_text);
                editText.setGravity(Gravity.CENTER);
                editText.setTextSize(18);
                editText.setPadding(10,10,10,10);

                LinearLayout.LayoutParams lp = new TableRow.LayoutParams(50,100);
                lp.setMargins(3,0,3,3);
                editText.setLayoutParams(lp);

                if(col == 0) editText.setHint("例：A");
                else if(col == 1) editText.setHint("我");

                tabRow.addView(editText);
            }
            tableLayout.addView(tabRow);
        }
        //----------------------------------------------------
    }

    //-------------------------------新增多行按鈕 監聽器----------------------------------------
    private View.OnClickListener addMultiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MakeGiftCodeActivity.this);
            final EditText et = new EditText(getApplicationContext());
            et.setGravity(Gravity.CENTER);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);

            builder.setTitle("請輸入要新增的行數");
            builder.setView(et);
            builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        int lines =Integer.parseInt(et.getText().toString());
                        tableAddRow(lines,"","");
                    }catch (NumberFormatException e) {
                       Toast.makeText(getApplicationContext(),"請輸入有效的整數!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setCancelable(true);

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    };

    //------------------------------tableLayout新增行------------------------------
    private void tableAddRow(int lines, String mainCode, String matchCode){
        for (int i=0; i<lines; i++){    //行數
            tabRow = new TableRow(getApplicationContext());
            for (int col = 0 ; col< 2; col++){
                EditText editText = new EditText(getApplicationContext());

                editText.setTextColor(Color.rgb(135,51,36));
                editText.setBackgroundResource(R.drawable.bg_text);
                editText.setGravity(Gravity.CENTER);
                editText.setTextSize(18);
                editText.setPadding(10,10,10,10);
                LinearLayout.LayoutParams lp = new TableRow.LayoutParams(50,100);
                lp.setMargins(3,0,3,3);
                editText.setLayoutParams(lp);

                if(col == 0) editText.setText(mainCode);  //設定mainCode文字
                else if(col == 1) editText.setText(matchCode);  //設定matchCode文字

                tabRow.addView(editText);

                if (i==0 && col==0) editText.requestFocus(); //新增行數的第一行第一欄取得focus
            }
            tableLayout.addView(tabRow);
        }
    }

    //-------------------------------刪除按鈕 監聽器----------------------------------------
    private View.OnClickListener removeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tabRow = (TableRow) tableLayout.getChildAt(0);

            //-----顯示 行刪除鈕-----
            if (tabRow.getChildCount()<3){
                int lineNum = tableLayout.getChildCount();  //總行數

                for (int row=0; row<lineNum; row++){
                    tabRow = (TableRow) tableLayout.getChildAt(row);
                    //int rowHeight = tabRow.getHeight()-3;  //減掉marginBottom

                    ImageView imageView = new ImageView(getApplicationContext());

                    //imageView.setBackgroundColor(Color.WHITE);
                    LinearLayout.LayoutParams lp = new TableRow.LayoutParams(30, 90);

                    if (row==0){
                        lp.setMargins(3,3,0,0);
                    }else{
                        lp.setMargins(3,3,0,0);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(5, 5);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageResource(R.drawable.clear);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tableLayout.removeView((TableRow)v.getParent());  //刪除行
                            }
                        });
                    }
                    imageView.setLayoutParams(lp);

                    tabRow.addView(imageView);
                }
                btn_add.setVisibility(View.INVISIBLE);
                btn_addMulti.setVisibility(View.INVISIBLE);

                btn_remove.setBackgroundResource(R.drawable.back);

            }else{ //-----關閉 行刪除鈕-----
                int lineNum = tableLayout.getChildCount();  //總行數
                for (int row=0; row<lineNum; row++){
                    tabRow = (TableRow) tableLayout.getChildAt(row);
                    tabRow.removeViewAt(2);
                }

                btn_add.setVisibility(View.VISIBLE);
                btn_addMulti.setVisibility(View.VISIBLE);
                btn_remove.setBackgroundResource(R.drawable.delete);
            }
        }
    };

    //------------------------取得使用者輸入的資料--------------------------------
    private void getCodeData(){
        int lineNum = tableLayout.getChildCount();  //總行數
        String mainCode, matchCode;

        for (int row=1; row<lineNum; row++){ //首行不算
            tabRow = (TableRow) tableLayout.getChildAt(row);
            mainCode = ((EditText)tabRow.getChildAt(0)).getText().toString();
            matchCode = ((EditText)tabRow.getChildAt(1)).getText().toString();

            if(mainCode.equals("") && matchCode.equals("")) ;
            else{
                mainCodes.add(mainCode);
                matchCodes.add(matchCode);
            }
        }
    }

    //-------------------------------儲存按鈕 監聽器----------------------------------------
    private View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ( et_giftName.getText().toString().trim().equals("")){ //檢查是否有輸入禮物名稱
                Toast.makeText(v.getContext(), "請輸入禮物名稱!", Toast.LENGTH_SHORT).show();
            }else{
                uploadGift(v);
            }
        }
    };

    //-------------------------------直接送禮按鈕 監聽器----------------------------------------
    private static final int REQUEST_CODE=11;
    private View.OnClickListener directlySendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ( et_giftName.getText().toString().trim().equals("")){ //檢查是否有輸入禮物名稱
                Toast.makeText(v.getContext(), "請輸入禮物名稱!", Toast.LENGTH_SHORT).show();
            }else{
                uploadGift(v);
            }
            Intent intent;
            intent = new Intent(MakeGiftCodeActivity.this, SendGiftDirectlyActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
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

        getCodeData(); //取得使用者輸入的資料

        giftName = et_giftName.getText().toString().trim();    //取得使用者輸入的禮物名稱
        Date date =new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        dateTime = sdFormat.format(date);
        giftContent = "";

        String rowNumber = "";
        for (int i = 0; i < mainCodes.size(); i++) {
            if( i ==  mainCodes.size() - 1){
                rowNumber += i ;
                maincode_array += mainCodes.get(i);
                matchcode_array += matchCodes.get(i);
            }else{
                rowNumber += i + ",";
                maincode_array += mainCodes.get(i) + ",";
                matchcode_array += matchCodes.get(i) + ",";
            }
        }
        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
//                SimpleDateFormat sdFormat_giftContent = new SimpleDateFormat("yyyyMMddHHmmss");
//                giftContent = sdFormat_giftContent.format(date);
        Log.v("MCAgiftid",giftid + "");
        if(giftid > 0){
            try{
                if(rowNumber.equals("")){
                    Toast.makeText(v.getContext(), "內容不可以是空的喔", Toast.LENGTH_SHORT).show();
                    return;
                }
                new updateGift(String.valueOf(giftid),giftContent, giftName, owner, giftType);

                new updateGiftCode(decodeid , rowNumber, maincode_array , matchcode_array);

                Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(v.getContext(), "儲存失敗，禮物名稱重複囉", Toast.LENGTH_SHORT).show();
            }
            //decodeid
        }else{
            if(checkRepeatGift.checkRepeatGift(giftName)) {
                //------------------------------上傳禮物資料
                new uploadGift(giftName, owner, giftType, rowNumber, maincode_array , matchcode_array);

                Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(v.getContext(), "儲存失敗，禮物名稱重複囉", Toast.LENGTH_SHORT).show();
            }
        }
        //-------------讀取Dialog-----------
        barProgressDialog = ProgressDialog.show(MakeGiftCodeActivity.this,
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
    }

    //-------------------取得回傳的資料---------------------
    private static final int FINISH_ACTIVITY = 22;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE:
                if (resultCode==FINISH_ACTIVITY){
                    finish();  //結束製作禮物
                }else if (resultCode==RESULT_OK){
                }
                break;
        }
    }
}