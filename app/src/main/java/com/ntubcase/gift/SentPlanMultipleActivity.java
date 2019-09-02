package com.ntubcase.gift;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentPlanMultipleActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------
    private EditText add_multi_name,add_multi_message,add_multi_friend,add_multi_dateS,add_multi_dateE;
    private List<Map<String, Object>> oldSelectDates = new ArrayList<Map<String, Object>>();; //原有資料
    //----------------------------------------------------------------------------------------------
    private String mulPlanName, message, friendName;
    private String from;

    //--showPlan
    private String sender="1";

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
        add_multi_name.setFocusable(false);
        add_multi_friend.setFocusable(false);
        add_multi_dateS.setFocusable(false);
        add_multi_dateE.setFocusable(false);
        add_multi_message.setFocusable(false);

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
            Intent intent;
            intent = new Intent(SentPlanMultipleActivity.this, SentPlanMultiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("planName", mulPlanName);
            bundle.putString("receiveFriend", friendName);
            bundle.putString("message", message);
            bundle.putSerializable("selectDates", (Serializable) oldSelectDates);
            bundle.putString("from", from); //從哪個頁面開啟
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE);
        }
    };

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
                    mulPlanName =jsonArray.getJSONObject(0).getString("mulPlanName"); //計畫名稱
                    String mulStartDate = jsonArray.getJSONObject(0).getString("startDate"); //送禮日期
                    String mulEndDate = jsonArray.getJSONObject(0).getString("endDate"); //結束日期
                    message =jsonArray.getJSONObject(0).getString("message"); //祝福

                    add_multi_name.setText(mulPlanName); //計畫名稱
                    add_multi_dateS.setText(mulStartDate.substring(0,10)); //送禮日期
                    add_multi_dateE.setText(mulEndDate.substring(0,10)); //結束日期
                    add_multi_message.setText(message); //祝福

                    //----------------------------取得好友資料----------------------------
                    jsonArray = object.getJSONArray("record");
                    int friendsLength = jsonArray.length();

                    friendName = "";
                    for (int i = 0; i < friendsLength; i++) {
                        if (friendName.equals("")) friendName += jsonArray.getJSONObject(i).getString("nickname");
                        else friendName += " , " + jsonArray.getJSONObject(i).getString("nickname");
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
