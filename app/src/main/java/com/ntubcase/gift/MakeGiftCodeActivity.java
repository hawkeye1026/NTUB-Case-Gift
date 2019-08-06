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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.giftInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.gift.giftInsertCodeAsyncTask;
import com.ntubcase.gift.data.getGiftList;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MakeGiftCodeActivity extends AppCompatActivity {
    private Button btn_add, btn_addMulti, btn_remove, btn_save, btn_makePlan;
    private static EditText et_giftName;

    private static String giftName, giftContent;

    protected static Date date =new Date();
    protected static String owner = "wayne";
    protected static String dateTime;
    protected static String giftType="5";
    ProgressDialog barProgressDialog;

    private TableLayout tableLayout;
    private TableRow tabRow;
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
        btn_add = findViewById(R.id.btn_add);
        btn_addMulti = findViewById(R.id.btn_addMulti);
        btn_remove = findViewById(R.id.btn_remove);
        btn_save = findViewById(R.id.btn_save);
        btn_makePlan = (Button) findViewById(R.id.btn_makePlan);
        btn_save.setOnClickListener(saveClickListener); //設置監聽器
        btn_makePlan.setOnClickListener(makePlanClickListener); //設置監聽器

        btn_add.setOnClickListener(new View.OnClickListener() { //設置 新增一行按鈕 監聽器
            @Override
            public void onClick(View v) { tableAddRow(1); }
        });
        btn_addMulti.setOnClickListener(addMultiClickListener); //設置監聽器
        btn_remove.setOnClickListener(removeClickListener); //設置監聽器
        //---------------------------------------------------------------------------------

        tableLayout = (TableLayout) findViewById(R.id.tab_01);
        tabRow = new TableRow(getApplicationContext()); //範例行
        for (int col = 0 ; col< 2; col++){
            EditText editText = new EditText(getApplicationContext());
            Button button = new Button(getApplicationContext());

            editText.setBackgroundColor(Color.WHITE);
            editText.setGravity(Gravity.CENTER);
            editText.setTextSize(18);
            editText.setPadding(10,10,10,10);
            LinearLayout.LayoutParams lp = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(3,0,3,3);
            editText.setLayoutParams(lp);

            if(col == 0) editText.setHint("例：A");
            else if(col == 1) editText.setHint("我");

            tabRow.addView(editText);
        }
        tableLayout.addView(tabRow);
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
                        tableAddRow(lines);
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
    private void tableAddRow(int lines){
        for (int i=0; i<lines; i++){    //行數
            tabRow = new TableRow(getApplicationContext());
            for (int col = 0 ; col< 2; col++){
                EditText editText = new EditText(getApplicationContext());

                editText.setBackgroundColor(Color.WHITE);
                editText.setGravity(Gravity.CENTER);
                editText.setTextSize(18);
                editText.setPadding(10,10,10,10);
                LinearLayout.LayoutParams lp = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(3,0,3,3);
                editText.setLayoutParams(lp);

                tabRow.addView(editText);

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
                    int rowHeight = tabRow.getHeight()-3;  //減掉marginBottom

                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setBackgroundColor(Color.WHITE);
                    imageView.setPadding(10,10,10,10);
                    LinearLayout.LayoutParams lp = new TableRow.LayoutParams(rowHeight, rowHeight);

                    if (row==0){
                        lp.setMargins(3,0,0,3);
                    }else{
                        lp.setMargins(3,0,3,3);
                        imageView.setImageResource(R.drawable.delete);
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
                //tableLayout.setColumnShrinkable(2,true);
                btn_add.setVisibility(View.INVISIBLE);
                btn_addMulti.setVisibility(View.INVISIBLE);
                btn_remove.setText("關閉刪除鈕");

            }else{ //-----關閉 行刪除鈕-----
                int lineNum = tableLayout.getChildCount();  //總行數
                for (int row=0; row<lineNum; row++){
                    tabRow = (TableRow) tableLayout.getChildAt(row);
                    tabRow.removeViewAt(2);
                }

                btn_add.setVisibility(View.VISIBLE);
                btn_addMulti.setVisibility(View.VISIBLE);
                btn_remove.setText("顯示刪除鈕");
            }


        }
    };

    //-------------------------------儲存按鈕 監聽器----------------------------------------
    private View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            giftName = et_giftName.getText().toString();    //取得使用者輸入的禮物名稱

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