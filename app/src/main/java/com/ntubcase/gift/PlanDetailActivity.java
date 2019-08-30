package com.ntubcase.gift;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.spPlanDetailAsyncTask;
import com.ntubcase.gift.data.getFriendList;
import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.data.getPlanList;
import com.ntubcase.gift.data.getPlanningList;
import com.ntubcase.gift.data.getSinglePlan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlanDetailActivity extends AppCompatActivity {
    //選擇禮物 使用的變數宣告---------------------------------------------------------------------------
    String[] edit_giftlistItems = new String[getGiftList.getGiftLength()];;
    boolean[] edit_giftcheckedItems ;
    ArrayList<Integer> edit_giftItems = new ArrayList<>();
    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] edit_friendlistItems = new String[getFriendList.getFriendLength()];;
    boolean[] edit_friendcheckedItems ;
    ArrayList<Integer> edit_friendItems = new ArrayList<>();
    //----------------------------------------------------------------------------------------------
    private static String[] giftid = new String[100];
    private static String[] friendid = new String[100];
    private static int giftidPositionIndex = 0 ;
    private static int friendidPositionIndex = 0 ;
    private String receiveid, userid="1";

    private int[] gift_position = new int[1] ;
    private int[] friend_position = new int[1] ;

    private List<Map<String, Object>> mPlansList; //計畫清單
    private List<Map<String, Object>> friends;
    private List<Map<String, Object>> gifts;

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
                    giftid[giftidPositionIndex] = getGiftList.getGiftid(position);
                    giftidPositionIndex++;
                }else{
                    edit_giftItems.remove((Integer.valueOf(position)));
                    giftidPositionIndex--;
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
                    friendid[friendidPositionIndex] = getFriendList.getFriendid(position);
                    friendidPositionIndex++;
                }else{
                    edit_friendItems.remove((Integer.valueOf(position)));
                    friendidPositionIndex--;
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
                edit_surprise_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
    protected void onDestroy() {
        giftidPositionIndex = 0;
        friendidPositionIndex = 0;
        super.onDestroy();
    }
    //-----------------
    public void onResume(){

        Intent intent = this.getIntent();
        //-----取得intent的bundle資料-----
        Bundle bundle = this.getIntent().getExtras();
        //String giftName = bundle.getString("name");
        //String giftContent = bundle.getString("content");
        String type = bundle.getString("type");
        String planid = bundle.getString("planid");

        //getPlanList.getJSON(userid, planid);

        //et_giftName.setText(giftName);
        //et_giftContent.setText(giftid);

        //選告變數------------------------------------------------------------------------------
        final EditText edit_surprise_name = (EditText) findViewById(R.id.edit_surprise_name);
        final EditText edit_surprise_date = findViewById(R.id.edit_surprise_date);
        final EditText edit_surprise_time = findViewById(R.id.edit_surprise_time);
        final EditText edit_surprise_gift = (EditText) findViewById(R.id.edit_surprise_gift);
        final EditText edit_surprise_friend = (EditText) findViewById(R.id.edit_surprise_friend);
        final EditText edit_surprice_message = (EditText) findViewById(R.id.edit_surprice_message);
        edit_giftcheckedItems = new boolean[edit_giftlistItems.length];
        edit_friendcheckedItems = new boolean[edit_friendlistItems.length];
        /*
        //選擇禮物 使用的變數宣告-------------------------------------------------------------------------- 禮物資料
        for(int i = 0; i < getGiftList.getGiftLength(); i++){
            edit_giftlistItems[i] = getGiftList.getGiftName(i);
        }
        //---------------------------------傳入預設禮物
        for(int i = 0 ; i < getPlanList.getPlanLength(); i++){
            if(planid.equals(getPlanList.getSpPlanid(i))){
                gift_position[0] = i ;
            }
        }
        //---------------------------------傳入禮物初始勾選方塊
        for(int i = 0; i < gift_position.length; i++){
            for(int j = 0 ; j < getGiftList.getGiftLength(); j++){
                if(getGiftList.getGiftid(j).equals(getPlanList.getGiftid(gift_position[i]))){
                    edit_giftcheckedItems[j] = true;
                    edit_giftItems.add(j);
                    giftidPositionIndex++;
                    break;
                }
            }
        }
        //---------------------------------
        //選擇好友使用的變數宣告--------------------------------------------------------------------------- 好友資料
        for(int i = 0; i < getFriendList.getFriendLength(); i++){
            edit_friendlistItems[i] = getFriendList.getFriendName(i);
        }

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
        */

        planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(PlanDetailActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);

                    //取得禮物紀錄
                    JSONArray jsonArray = object.getJSONArray("record");
                    int recordLength = jsonArray.length();
                    Log.v("recordLength", String.valueOf(recordLength));

                    friends = new ArrayList<Map<String, Object>>();
                    Map<String, Object> mFriends;

                    for (int i = 0; i < recordLength; i++) {
                        String receiverid =jsonArray.getJSONObject(i).getString("receiverid");
                        String nickname =jsonArray.getJSONObject(i).getString("nickname");

                        edit_surprise_friend.setText(nickname);

                        mFriends = new HashMap<String, Object>();
                        mFriends.put("receiverid", receiverid);
                        mFriends.put("nickname", nickname);
                        friends.add(mFriends);
                    }
                    Log.v("friends", String.valueOf(friends));

                    //取得單日計畫
                    jsonArray = object.getJSONArray("sinPlan");
                    int sinPlanLength = jsonArray.length();
                    Log.v("sinPlanLength", String.valueOf(sinPlanLength));

                    String sinPlanid =jsonArray.getJSONObject(0).getString("sinid");
                    String sinPlanName =jsonArray.getJSONObject(0).getString("sinPlanName");
                    String sinCreateDate = DateFormat.dateFormat(jsonArray.getJSONObject(0).getString("createDate"));
                    String sinSendPlanDate = DateFormat.dateFormat(jsonArray.getJSONObject(0).getString("sendPlanDate"));

                    edit_surprise_name.setText(sinPlanName);
                    edit_surprise_date.setText(sinSendPlanDate);

                    //取得單日禮物清單
                    jsonArray = object.getJSONArray("sinList");
                    int sinListLength = jsonArray.length();
                    Log.v("sinListLength", String.valueOf(sinListLength));

                    gifts = new ArrayList<Map<String, Object>>();
                    Map<String, Object> mGifts;

                    for (int i = 0 ; i < sinListLength ; i++){
                        //Log.v("abc","10000");
                        String sinListid = jsonArray.getJSONObject(i).getString("sinid");
                        String sinGiftid = jsonArray.getJSONObject(i).getString("giftid");
                        String sinSendGiftDate = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("sendGiftDate"));
                        String sinMessage = jsonArray.getJSONObject(i).getString("message");
                        String sinGift = jsonArray.getJSONObject(i).getString("gift");
                        String sinGiftName = jsonArray.getJSONObject(i).getString("giftName");

                        mGifts = new HashMap<String, Object>();
                        mGifts.put("giftid", sinGiftid);
                        mGifts.put("sendGiftDate", sinSendGiftDate);
                        mGifts.put("message", sinMessage);
                        mGifts.put("sinGift", sinGift);
                        mGifts.put("sinGiftName", sinGiftName);
                        gifts.add(mGifts);

                        edit_surprise_gift.setText(sinListid+sinGift);
                        edit_surprice_message.setText(sinMessage+sinGiftName);
                    }
                    Log.v("gifts", String.valueOf(gifts));


                } catch (Exception e) {
                    Toast.makeText(PlanDetailActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.planList , userid, planid);

        super.onResume();
    }
}
