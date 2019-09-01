package com.ntubcase.gift;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.data.getFriendList;
import com.ntubcase.gift.data.getGiftList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakePlanMultipleActivity extends AppCompatActivity {

    //選擇好友 使用的變數宣告---------------------------------------------------------------------------
    String[] friendItemList = new String[getFriendList.getFriendLength()];
    boolean[] mFriendChecked, tempFriendChecked;
    ArrayList<String> selectFriendIds = new ArrayList<>();
    //----------------------------------------------------------------------------------------------
    private EditText add_multi_name,add_multi_message,add_multi_friend,add_multi_dateS,add_multi_dateE;
    private Date selectStartDate, selectEndDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //----------------------------------------------------------------------------------------
    private List<Map<String, Object>> selectDates; //選取的時間區段
    private List<Map<String, Object>> oldSelectDates = new ArrayList<Map<String, Object>>();; //原有資料

    //--showPlan
    private String sender="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan_multiple);
        setTitle(R.string.planMultiple);

        //--------------------取得資料
        getGiftList.getJSON();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //宣告變數------------------------------------------------------------------------------
        add_multi_name = (EditText) findViewById(R.id.add_multi_name);
        add_multi_dateS = findViewById(R.id.add_multi_dateS);
        add_multi_dateE = findViewById(R.id.add_multi_dateE);
        add_multi_friend = (EditText) findViewById(R.id.add_multi_friend);
        add_multi_message = (EditText) findViewById(R.id.add_multi_message);
        //------------------------------------------------------------------------------

        //選擇好友使用的變數宣告---------------------------------------------------------------------------
        for(int i = 0; i < getFriendList.getFriendLength(); i++){
            friendItemList[i] = getFriendList.getFriendName(i);
        }
        mFriendChecked = new boolean[friendItemList.length];
        tempFriendChecked = new boolean[friendItemList.length];

        //---------------------------------若是計畫詳細-----------------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            String planID = bundle.getString("planID");
            showPlanDetail(planID);  //顯示計畫詳細資料
        }
        //--------------------------------------------------------------------------------------

        //點選選擇好友EditText跳出選擇好友選擇器------------------------------------------------------------------------
        add_multi_friend.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_multi_friend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    Showfrienddialog();
                }
            }
        });
        add_multi_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Showfrienddialog();
            }
        });

        //點選送禮日期EditText跳出選擇日期選擇器---------------------------------------
        add_multi_dateS.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_multi_dateS.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDateSPickerDialog();
                }
            }
        });

        add_multi_dateS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDateSPickerDialog();
            }
        });

        //點選結束日期EditText跳出選擇日期選擇器---------------------------------------
        add_multi_dateE.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        add_multi_dateE.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDateEPickerDialog();
                }
            }
        });

        add_multi_dateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDateEPickerDialog();
            }
        });

        //-----------下一步------------
        Button btn_next = (Button) findViewById(R.id.btn_plan_next);
        btn_next.setOnClickListener(nextPageListener);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    //設定選擇好友EditText傳入值---------------------------------------
    private void Showfrienddialog(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MakePlanMultipleActivity.this);
        mBuilder.setTitle("選擇好友");
        mBuilder.setMultiChoiceItems(friendItemList, mFriendChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) { //確認鈕
                String mSelectFriends = "";
                selectFriendIds = new ArrayList<>();

                for (int i = 0; i < mFriendChecked.length; i++) {
                    if(mFriendChecked[i]){
                        if (mSelectFriends.equals("")) mSelectFriends += friendItemList[i];
                        else mSelectFriends += " , " + friendItemList[i];
                        selectFriendIds.add(getFriendList.getFriendid(i));
                    }
                    tempFriendChecked[i]=mFriendChecked[i];
                }
                add_multi_friend.setText(mSelectFriends);
            }
        });

        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //取消鈕
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i=0; i<tempFriendChecked.length; i++) mFriendChecked[i]=tempFriendChecked[i];
            }
        });

        mBuilder.setNeutralButton("清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {  //清除鈕
                for (int i = 0; i < mFriendChecked.length; i++) {
                    mFriendChecked[i] = false;
                    tempFriendChecked[i] = false;
                }
                add_multi_friend.setText("");
                selectFriendIds = new ArrayList<>();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    //設定送禮日期EditText傳入值---------------------------------------
    private void showDateSPickerDialog () {
        Calendar c = Calendar.getInstance();

        if (selectStartDate!=null) c.setTime(selectStartDate);  //取得上次選的日期

        DatePickerDialog datePickerDialog = new DatePickerDialog(MakePlanMultipleActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    add_multi_dateS.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
                    selectStartDate = sdf.parse(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));

                    if (selectEndDate!=null && selectStartDate.after(selectEndDate)){ //若開始日期比結束日期晚
                        selectEndDate = selectStartDate;
                        add_multi_dateE.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());  //最小日期為當日
        datePickerDialog.show();
    }

    //設定結束日期EditText傳入值---------------------------------------
    private void showDateEPickerDialog () {
        Calendar c = Calendar.getInstance();

        if (selectEndDate!=null) c.setTime(selectEndDate);  //取得上次選的日期

        DatePickerDialog datePickerDialog = new DatePickerDialog(MakePlanMultipleActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    add_multi_dateE.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
                    selectEndDate = sdf.parse(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));

                    if (selectStartDate!=null && !selectEndDate.after(selectStartDate)){ //若結束日期比開始日期早
                        selectStartDate = selectEndDate;
                        add_multi_dateS.setText(year + "-" + dateAdd0(monthOfYear + 1) + "-" + dateAdd0(dayOfMonth));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());  //最小日期為當日
        datePickerDialog.show();
    }

    //-----------------------------------------------------------
    public String dateAdd0(int date){
        if(date <10){
            return "0"+date;
        }else{
            return String.valueOf(date);
        }
    }
    //-----------------------------------------------------------

    //---------------------------------------------------下一步按鈕---------------------------------------------------
    private static final int REQUEST_CODE=1;
    private View.OnClickListener nextPageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (selectStartDate==null || selectEndDate==null){   //送禮日期或結束日期不能為空值
                Toast.makeText(MakePlanMultipleActivity.this,
                        "請設定規劃期間", Toast.LENGTH_SHORT).show();
            }else{
                setNextPageDateData();  //設定傳入下一頁的日期資料

                Intent intent;
                intent = new Intent(MakePlanMultipleActivity .this, PlanMultipleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("planName", add_multi_name.getText().toString());
                bundle.putString("receiveFriend", add_multi_friend.getText().toString());
                bundle.putStringArrayList("receiveFriendId", selectFriendIds);
                bundle.putString("startDate", add_multi_dateS.getText().toString());
                bundle.putString("endDate", add_multi_dateE.getText().toString());
                bundle.putString("message", add_multi_message.getText().toString());
                bundle.putSerializable("selectDates", (Serializable) selectDates);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
            }
        }
    };

    //---------------設定傳入下一頁的日期資料-------------
    private void setNextPageDateData(){
        List<String> allDates = findDates(selectStartDate, selectEndDate);  //取得兩個日期間所有日期
        selectDates = new ArrayList<Map<String, Object>>();
        Map<String, Object> mDates;

        for(int i=0; i < allDates.size(); i++) {
            mDates = new HashMap<String, Object>();
            mDates.put("date", allDates.get(i));  //日期
            mDates.put("message", "");  //留言
            mDates.put("time", ""); //時間
            mDates.put("gifts", "");    //禮物
            mDates.put("mCheckedItems", new boolean[getGiftList.getGiftLength()]); //禮物選取的項目
            selectDates.add(mDates);
        }

        //-----------若有舊資料則設定舊資料-----------
        if (oldSelectDates.size()>0){
            //---取得舊資料所有日期---
            List<String> oldAllDates = new ArrayList<>();
            for (int i=0; i<oldSelectDates.size(); i++) {
                oldAllDates.add((String)oldSelectDates.get(i).get("date"));
            }

            //---檢查新選擇的日期期間內是否包含舊日期---
            for(int i=0; i<allDates.size(); i++) {
                if (oldAllDates.contains(allDates.get(i))){ //若新的日期期間有包含舊的日期
                    selectDates.set(i, oldSelectDates.get(oldAllDates.indexOf(allDates.get(i)))); //設定成舊資料
                }
            }
        }
    }

    //--------------取得兩個日期間所有日期------------------
    public List<String> findDates(Date dBegin, Date dEnd){
        List<String> lDate = new ArrayList<String>();
        lDate.add(sdf.format(dBegin));

        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);

        while (dEnd.after(calBegin.getTime())) { // 测试此日期是否在指定日期之后
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sdf.format(calBegin.getTime()));
        }
        return lDate;
    }

    //-------------------取得下一頁回傳的資料---------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE:
                if (resultCode==RESULT_OK){
                    oldSelectDates = (ArrayList<Map<String, Object>>) data.getExtras().getSerializable("selectDates");
                }else {
                    finish();  //結束製作計畫
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //------------------------------計畫詳細，顯示plan資料------------------------------
    private void showPlanDetail(String planid){
        planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(MakePlanMultipleActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray;

                    //----------------------------取得計畫資料----------------------------
                    jsonArray = object.getJSONArray("mulPlan");
                    //String mulPlanid =jsonArray.getJSONObject(0).getString("mulid"); //計畫ID
                    //String mulCreateDate = jsonArray.getJSONObject(0).getString("createDate"); //計畫建立日期
                    String mulPlanName =jsonArray.getJSONObject(0).getString("mulPlanName"); //計畫名稱
                    String mulStartDate = jsonArray.getJSONObject(0).getString("startDate"); //送禮日期
                    String mulEndDate = jsonArray.getJSONObject(0).getString("endDate"); //結束日期
                    String message =jsonArray.getJSONObject(0).getString("message"); //祝福

                    add_multi_name.setText(mulPlanName); //計畫名稱
                    add_multi_dateS.setText(mulStartDate.substring(0,10)); //送禮日期
                    selectStartDate = sdf.parse(mulStartDate);
                    add_multi_dateE.setText(mulEndDate.substring(0,10)); //結束日期
                    selectEndDate = sdf.parse(mulEndDate);
                    add_multi_message.setText(message); //祝福

                    //----------------------------取得好友資料----------------------------
                    jsonArray = object.getJSONArray("record");
                    int friendsLength = jsonArray.length();

                    String friendName = "";
                    for (int i = 0; i < friendsLength; i++) {
                        selectFriendIds.add(jsonArray.getJSONObject(i).getString("receiverid")); //好友ID
                        String nickname =jsonArray.getJSONObject(i).getString("nickname");

                        if (friendName.equals("")) friendName += jsonArray.getJSONObject(i).getString("nickname");
                        else friendName += " , " + jsonArray.getJSONObject(i).getString("nickname");

                        //---好友checkbox---
                        for (int k=0; k<mFriendChecked.length; k++){
                            if (getFriendList.getFriendid(k).equals(selectFriendIds.get(i)) ){
                                mFriendChecked[k]=true;
                                tempFriendChecked[k]=true;
                            }
                        }
                    }
                    add_multi_friend.setText(friendName);

                    //----------------------------取得下一頁計畫的資料----------------------------
                    jsonArray = object.getJSONArray("mulList");
                    int mulListLength = jsonArray.length();

                    Map<String, Object> mDates;
                    for (int i = 0 ; i < mulListLength ; i++){
                        //String mulGift = jsonArray.getJSONObject(i).getString("gift"); //禮物內容
                        String mulSendGiftDate = jsonArray.getJSONObject(i).getString("sendGiftDate"); //禮物送禮日期時間
                        String date = mulSendGiftDate.substring(0,10); //日期
                        String sendTime = mulSendGiftDate.substring(11,16); //送禮時間
                        String mulGoal = jsonArray.getJSONObject(i).getString("goal"); //禮物留言
                        String mulGiftName = jsonArray.getJSONObject(i).getString("giftName"); //禮物名稱
                        String mulGiftid = jsonArray.getJSONObject(i).getString("giftid"); //禮物ID

                        mDates = new HashMap<String, Object>();
                        mDates.put("date", date); //日期
                        mDates.put("message", mulGoal); //留言
                        mDates.put("time", sendTime); //時間
                        mDates.put("gifts", mulGiftName); //禮物
                        //---禮物checkbox---
                        boolean[] mCheckedGift = new boolean[getGiftList.getGiftLength()];
                        for (int k=0; k<mCheckedGift.length; k++){
                            if (getGiftList.getGiftid(k).equals(mulGiftid)) mCheckedGift[k]=true;
                        }
                        mDates.put("mCheckedItems", mCheckedGift); //禮物選取的項目
                        oldSelectDates.add(mDates);
                    }
                } catch (Exception e) {
                    Toast.makeText(MakePlanMultipleActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.planList , sender, planid);
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
