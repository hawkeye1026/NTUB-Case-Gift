package com.ntubcase.gift;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentPlanMultipleActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------
    private EditText add_multi_name,add_multi_message,add_multi_friend,add_multi_dateS,add_multi_dateE;
    private Date selectStartDate, selectEndDate;
    private SimpleDateFormat sdfD = new SimpleDateFormat("yyyy-MM-dd");
    private List<Map<String, Object>> selectDates = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> oldSelectDates = new ArrayList<Map<String, Object>>(); //原有資料
    //----------------------------------------------------------------------------------------------
    private String mulPlanName, message, friendName;
    private String from;
    private List<String[]> feedback = new ArrayList<>();

    //--showPlan
    private String sender=userData.getUserID(), mulPlanid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan_multiple);
        setTitle(R.string.planMultiple);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //------------------------------------------------------------------------------
        add_multi_name = (EditText) findViewById(R.id.add_multi_name);
        add_multi_friend = (EditText) findViewById(R.id.add_multi_friend);
        add_multi_dateS = findViewById(R.id.add_multi_dateS);
        add_multi_dateE = findViewById(R.id.add_multi_dateE);
        add_multi_message = (EditText) findViewById(R.id.add_multi_message);
        add_multi_name.setInputType(InputType.TYPE_NULL);
        add_multi_friend.setInputType(InputType.TYPE_NULL);
        add_multi_dateS.setInputType(InputType.TYPE_NULL);
        add_multi_dateE.setInputType(InputType.TYPE_NULL);
        add_multi_message.setKeyListener(null);

        Button btn_next = (Button) findViewById(R.id.btn_plan_next);
        btn_next.setOnClickListener(nextPageListener);

        //-------------------------取得計畫詳細---------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            String planID = bundle.getString("planID");
            showPlanDetail(planID);  //顯示計畫詳細資料
        }
        from = bundle.getString("from");

    }

    //---------------------------------------------------下一步按鈕---------------------------------------------------
    private static final int REQUEST_CODE=1;
    private View.OnClickListener nextPageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setNextPageDateData();

            Intent intent;
            intent = new Intent(SentPlanMultipleActivity.this, SentPlanMultiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("planid", mulPlanid);
            bundle.putString("planName", mulPlanName);
            bundle.putString("receiveFriend", friendName);
            bundle.putString("message", message);
            bundle.putSerializable("feedback", (Serializable) feedback);
            bundle.putSerializable("selectDates", (Serializable) selectDates);
            bundle.putString("from", from); //從哪個頁面開啟
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE);
        }
    };

    //---------------設定傳入下一頁的日期資料-------------
    private void setNextPageDateData() {
        List<String> allDates = findDates(selectStartDate, selectEndDate);  //取得兩個日期間所有日期
        selectDates = new ArrayList<Map<String, Object>>();
        Log.v("allDates", String.valueOf(allDates));

        //-----------若有舊資料則設定舊資料-----------
        if (oldSelectDates.size()>0){
            //---取得舊資料所有日期---
            List<String> oldAllDates = new ArrayList<>();

            String mes = " ", time = " ";
            Map<String, Object> nDates = null;
            for (int i=0; i<allDates.size(); i++) {
                List<String> gifts = new ArrayList<>();
                for (int j=0; j<oldSelectDates.size(); j++) {
                    Log.v("oldSelectDates", (String) oldSelectDates.get(j).get("date"));
                    if((((String)oldSelectDates.get(j).get("date")).equals(allDates.get(i)))){
                        gifts.add((String) oldSelectDates.get(j).get("gifts"));
                        mes = (String) oldSelectDates.get(j).get("message");
                        time = (String) oldSelectDates.get(j).get("time");
                    }
                }
                nDates = new HashMap<String, Object>();
                nDates.put("date", allDates.get(i)); //日期
                nDates.put("message", mes); //留言
                nDates.put("time", time); //時間

                if(gifts.isEmpty()) nDates.put("gifts", ""); //禮物
                else nDates.put("gifts", gifts); //禮物

                selectDates.add(nDates);
            }
        }
        Log.v("selectDates", String.valueOf(selectDates));
    }


    //--------------取得兩個日期間所有日期------------------
    public List<String> findDates(Date dBegin, Date dEnd){
        List<String> lDate = new ArrayList<String>();
        lDate.add(sdfD.format(dBegin));

        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);

        while (dEnd.after(calBegin.getTime())) { // 测试此日期是否在指定日期之后
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sdfD.format(calBegin.getTime()));
        }
        return lDate;
    }

    //------------------------------計畫詳細，顯示plan資料------------------------------
    private void showPlanDetail(String planid){
        planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(SentPlanMultipleActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray;

                    //----------------------------取得計畫資料----------------------------
                    jsonArray = object.getJSONArray("mulPlan");
                    mulPlanid =jsonArray.getJSONObject(0).getString("mulid");
                    mulPlanName =jsonArray.getJSONObject(0).getString("mulPlanName"); //計畫名稱
                    String mulStartDate = jsonArray.getJSONObject(0).getString("startDate"); //送禮日期
                    String mulEndDate = jsonArray.getJSONObject(0).getString("endDate"); //結束日期
                    message =jsonArray.getJSONObject(0).getString("message"); //祝福

                    selectStartDate = sdfD.parse(mulStartDate);
                    selectEndDate = sdfD.parse(mulEndDate);

                    add_multi_name.setText(mulPlanName); //計畫名稱
                    add_multi_dateS.setText(mulStartDate.substring(0,10)); //送禮日期
                    add_multi_dateE.setText(mulEndDate.substring(0,10)); //結束日期
                    add_multi_message.setText(message); //祝福

                    //----------------------------取得收禮人,feedback資料----------------------------
                    jsonArray = object.getJSONArray("record");
                    int recordLength = jsonArray.length();

                    friendName = "";
                    for (int i = 0; i < recordLength; i++) {
                        String fNickname = jsonArray.getJSONObject(i).getString("nickname");
                        String fFeedback = jsonArray.getJSONObject(i).getString("feedback");
                        String[] a ={fNickname,fFeedback};

                        if (friendName.equals("")) friendName += fNickname;
                        else friendName += " , " + fNickname;

                        feedback.add(a);
                    }
                    add_multi_friend.setText(friendName);

                    //----------------------------取得下一頁計畫的資料----------------------------
                    jsonArray = object.getJSONArray("mulList");
                    int mulListLength = jsonArray.length();
                    
                    Map<String, Object> mDates;
                    for (int i = 0 ; i < mulListLength ; i++){
                        String mulSendGiftDate = jsonArray.getJSONObject(i).getString("sendGiftDate"); //禮物送禮日期時間
                        String date = mulSendGiftDate.substring(0,10); //日期
                        String sendTime = mulSendGiftDate.substring(11,16); //送禮時間
                        String mulGoal = jsonArray.getJSONObject(i).getString("goal"); //禮物留言
                        String mulGiftName = jsonArray.getJSONObject(i).getString("giftName"); //禮物名稱

                        if (mulGiftName.equals("null")){
                            mulGiftName = "";  //若為null則顯示空值
                            sendTime = "";
                        }



                        mDates = new HashMap<String, Object>();
                        mDates.put("date", date); //日期
                        mDates.put("message", mulGoal); //留言
                        mDates.put("time", sendTime); //時間
                        mDates.put("gifts", mulGiftName); //禮物
                        oldSelectDates.add(mDates);
                    }
                } catch (Exception e) {
                    Toast.makeText(SentPlanMultipleActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.planList , sender, planid);
    }

    //-------------------取得下一頁回傳的資料---------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE:
                if (resultCode==2) finish();  //結束製作計畫
                break;
        }
    }

    //-----------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
