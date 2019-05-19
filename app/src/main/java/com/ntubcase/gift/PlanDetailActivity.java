package com.ntubcase.gift;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.spPlanDetailAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.ntubcase.gift.MakeGiftsActivity.dateTime;

public class PlanDetailActivity extends AppCompatActivity {
    //選擇禮物 使用的變數宣告---------------------------------------------------------------------------
    String[] edit_giftlistItems;
    boolean[] edit_giftcheckedItems;
    ArrayList<Integer> edit_giftItems = new ArrayList<>();
    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] edit_friendlistItems;
    boolean[] edit_friendcheckedItems;
    ArrayList<Integer> edit_friendItems = new ArrayList<>();
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        setTitle(R.string.planDetail);

    }
    //設定選擇禮物EditText傳入值---------------------------------------
    private void Showgiftdialog(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlanDetailActivity.this);
        mBuilder.setTitle("選擇禮物");
        mBuilder.setMultiChoiceItems(edit_giftlistItems, edit_giftcheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    edit_giftItems.add(position);
                }else{
                    edit_giftItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                EditText edit_surprise_gift = (EditText) findViewById(R.id.edit_surprise_gift);
                for (int i = 0; i < edit_giftItems.size(); i++) {
                    item = item + edit_giftlistItems[edit_giftItems.get(i)];
                    if (i != edit_giftItems.size() - 1) {
                        item = item + ", ";
                    }
                }
                edit_surprise_gift.setText(item);
            }
        });

//        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                EditText edit_surprise_gift = (EditText) findViewById(R.id.edit_surprise_gift);
                for (int i = 0; i < edit_giftcheckedItems.length; i++) {
                    edit_giftcheckedItems[i] = false;
                    edit_giftItems.clear();
                    edit_surprise_gift.setText("");
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    //設定選擇好友EditText傳入值---------------------------------------
    private void Showfrienddialog(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlanDetailActivity.this);
        mBuilder.setTitle("選擇好友");
        mBuilder.setMultiChoiceItems(edit_friendlistItems, edit_friendcheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    edit_friendItems.add(position);
                }else{
                    edit_friendItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                EditText edit_surprise_friend = (EditText) findViewById(R.id.edit_surprise_friend);
                for (int i = 0; i < edit_friendItems.size(); i++) {
                    item = item + edit_friendlistItems[edit_friendItems.get(i)];
                    if (i != edit_friendItems.size() - 1) {
                        item = item + ", ";
                    }
                }
                edit_surprise_friend.setText(item);
            }
        });

//        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                EditText edit_surprise_friend = (EditText) findViewById(R.id.edit_surprise_friend);
                for (int i = 0; i < edit_friendcheckedItems.length; i++) {
                    edit_friendcheckedItems[i] = false;
                    edit_friendItems.clear();
                    edit_surprise_friend.setText("");
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    //設定送禮日期EditText傳入值---------------------------------------
    private void showDatePickerDialog () {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(PlanDetailActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                EditText edit_surprise_date = findViewById(R.id.edit_surprise_date);
                edit_surprise_date.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }
    //設定送禮時間EditText傳入值---------------------------------------
    private void showTimePickerDialog () {
        Calendar t = Calendar.getInstance();
        new TimePickerDialog(PlanDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                EditText edit_surprise_time = findViewById(R.id.edit_surprise_time);
                edit_surprise_time.setText(hourOfDay + "：" + minute);

            }
        }, t.get(Calendar.HOUR_OF_DAY), t.get(Calendar.MINUTE),false).show();

    }
    //-----------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //-----------------
    public void onResume(){

        //---------------------------------------------------------------------------------
        //選告變數------------------------------------------------------------------------------
        final EditText edit_surprise_name = (EditText) findViewById(R.id.edit_surprise_name);
        final EditText edit_surprise_date = findViewById(R.id.edit_surprise_date);
        final EditText edit_surprise_time = findViewById(R.id.edit_surprise_time);
        final EditText edit_surprise_gift = (EditText) findViewById(R.id.edit_surprise_gift);
        final EditText edit_surprise_friend = (EditText) findViewById(R.id.edit_surprise_friend);
        final EditText edit_surprice_message = (EditText) findViewById(R.id.edit_surprice_message);
        //------------------------------------------------------------------------------
        //選擇禮物 使用的變數宣告---------------------------------------------------------------------------
        edit_giftlistItems = getResources().getStringArray(R.array.edit_gift_item);
        edit_giftcheckedItems = new boolean[edit_giftlistItems.length];
        //選擇好友使用的變數宣告---------------------------------------------------------------------------
        edit_friendlistItems = getResources().getStringArray(R.array.edit_friend_item);
        edit_friendcheckedItems = new boolean[edit_friendlistItems.length];
        //點選選擇禮物EditText跳出選擇禮物選擇器------------------------------------------------------------------------
        edit_surprise_gift.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edit_surprise_gift.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Showgiftdialog();
                }
            }
        });

        edit_surprise_gift.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Showgiftdialog();
            }
        });
        //點選選擇好友EditText跳出選擇好友選擇器------------------------------------------------------------------------
        edit_surprise_friend.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edit_surprise_friend.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Showfrienddialog();
                }
            }
        });
        edit_surprise_friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Showfrienddialog();
            }
        });
        //點選送禮日期EditText跳出選擇日期選擇器---------------------------------------
        edit_surprise_date.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edit_surprise_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        edit_surprise_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog();
            }
        });
        //點選送禮日期EditText跳出選擇時間選擇器---------------------------------------
        edit_surprise_time.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        edit_surprise_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showTimePickerDialog();
                }
            }
        });

        edit_surprise_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTimePickerDialog();
            }
        });

        //-----取得intent的bundle資料-----
        Bundle bundle = this.getIntent().getExtras();
        //String giftName = bundle.getString("name");
        //String giftContent = bundle.getString("content");
        String type = bundle.getString("type");
        String planid = bundle.getString("planid");
        //et_giftName.setText(giftName);
        //et_giftContent.setText(giftid);

        Log.v("type",
                type);
        Log.v("planid",
                planid);

        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        final SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        dateTime = sdFormat.format(date);
        Log.v("date",dateTime);

        if(type.equals("驚喜式")){
            spPlanDetailAsyncTask spPlanDetailAsyncTask = new spPlanDetailAsyncTask(new spPlanDetailAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {
                    try {
                        if (result == null) {
                            Toast.makeText(PlanDetailActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject object = new JSONObject(result);
                        JSONArray jsonArray = object.getJSONArray("result");

                        String spid =jsonArray.getJSONObject(0).getString("spid");
                        String spPlanName =jsonArray.getJSONObject(0).getString("spPlanName");
                        String sendPlanDate =dateFormat.dateFormat(jsonArray.getJSONObject(0).getString("sendPlanDate"));
                        String message =jsonArray.getJSONObject(0).getString("message");
                        String giftid =jsonArray.getJSONObject(0).getString("giftid");
                        String giftName =jsonArray.getJSONObject(0).getString("giftName");
                        String receiveid =jsonArray.getJSONObject(0).getString("receiveid");
                        String nickname =jsonArray.getJSONObject(0).getString("nickname");

                        //SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
                        //SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
                        String[] arr = sendPlanDate.split(" ");
                        //Date date = sdfDate.parse(arr[0]);
                        //Date time = sdfTime.parse(arr[1]);

                        edit_surprise_name.setText(spPlanName);
                        //--- Q. 時間格式顯示方式不同!!! ---
                        edit_surprise_date.setText(arr[0]); //2019-05-09
                        edit_surprise_time.setText(arr[1]);  //05:59
                        //---
                        edit_surprise_gift.setText(giftName);
                        edit_surprise_friend.setText(receiveid +", "+ nickname);
                        edit_surprice_message.setText(message);

                    } catch (Exception e) {
                        Toast.makeText(PlanDetailActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            spPlanDetailAsyncTask.execute(Common.spPalnDetail , planid);
        }

        if(type.equals("期間式")){

        }

        if(type.equals("問答式")){

        }



/*
        giftDetailAsyncTask giftDetailAsyncTask = new giftDetailAsyncTask(new giftDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(GiftDetailActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("result");

                    String giftid =jsonArray.getJSONObject(0).getString("giftid");
                    String gift =jsonArray.getJSONObject(0).getString("gift");
                    String giftName =jsonArray.getJSONObject(0).getString("giftName");
                    String giftCreateDate =dateFormat.dateFormat(jsonArray.getJSONObject(0).getString("giftCreateDate"));
                    String ownerid =jsonArray.getJSONObject(0).getString("ownerid");
                    String type =jsonArray.getJSONObject(0).getString("type");
                    Log.v("giftid",
                            giftid);
                    Log.v("giftName",
                            giftName);
                    Log.v("type",
                            type);

                    et_giftName.setText(giftName);
                    et_giftContent.setText(gift);

                } catch (Exception e) {
                    Toast.makeText(GiftDetailActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        giftDetailAsyncTask.execute(Common.giftDetail , giftid);
        //getGiftList.getJSON();
*/
        super.onResume();
    }
}
