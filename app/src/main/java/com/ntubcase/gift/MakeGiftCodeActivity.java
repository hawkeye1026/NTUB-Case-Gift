package com.ntubcase.gift;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MakeGiftCodeActivity extends AppCompatActivity {
    private Button btn_add, btn_save, btn_makePlan;
    private static EditText et_giftName, et_mainCode, et_matchCode;

    private static String giftName, mainCode, matchCode;

    protected static Date date =new Date();
    protected static String owner = "wayne";
    protected static String dateTime, giftType;
    ProgressDialog barProgressDialog;

    //private final int H = ViewGroup.LayoutParams.WRAP_CONTENT;
    //private final int W = ViewGroup.LayoutParams.MATCH_PARENT;
    private TableLayout tableLayout;
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
        //btn_add.setOnClickListener(addClickListener); //設置監聽器

        //---------------------------------------------------------------------------------
        maincodes.add("秘密代碼");
        matchcodes.add("對應訊息");

        tableLayout = (TableLayout) findViewById(R.id.tab_01);
        //控制行數
        for (int row = 0; row < matchcodes.size(); row++) {

            TableRow tabRow = new TableRow(this);
            //控制列數
            for (int col = 0 ; col< 3; col++){

                TextView tv = new TextView(this);
                if(col == 0) tv.setText(maincodes.get(row));
                if(col == 1) tv.setText(" = ");
                if(col == 2) tv.setText(matchcodes.get(row));

                tv.setGravity(Gravity.CENTER);
                tabRow.addView(tv);

            }
            tableLayout.addView(tabRow);
        }
        /*
        maincodes.add("秘密代碼");
        matchcodes.add("對應訊息");


        tableLayout = (TableLayout) findViewById(R.id.tab_01);
        //控制列數
        for (int row = 0; row < 3; row++) {

            TableRow tabRow = new TableRow(this);
            //控制欄數
            for (int col = 0 ; col < 2; col++){
                Log.v("maincodes.size()", String.valueOf(maincodes.size()));

                TextView tv = new TextView(this);
                tv.setText(maincodes.get(col)+matchcodes.get(col));
                tv.setGravity(Gravity.CENTER);
                tabRow.addView(tv);

            }
            tableLayout.addView(tabRow);
        }
*/
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
