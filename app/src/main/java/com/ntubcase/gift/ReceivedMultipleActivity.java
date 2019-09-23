package com.ntubcase.gift;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceivedMultipleActivity extends AppCompatActivity {

    private TextView tv_multi_name;
    private String sender= "1";
    //----------------------------------------------------------------------------------------------
    private String mulPlanName, message, friendName, mulPlanid;
    private List<Map<String, Object>> oldSelectDates = new ArrayList<Map<String, Object>>();; //原有資料

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_multiple);
        setTitle("收禮細節(多日)");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //------------------------------------------------------------------------------------------

        tv_multi_name = findViewById(R.id.tv_name);

        //---------------------------------取得收禮詳細-----------------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            String planid = bundle.getString("planID");
            showPlanDetail(planid);  //顯示收禮詳細資料
        }
    }

    //------------------------------收禮詳細，顯示plan資料------------------------------
    private void showPlanDetail(String planid){
        planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(ReceivedMultipleActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray;

                    //----------------------------取得計畫資料----------------------------
                    jsonArray = object.getJSONArray("mulPlan");
                    mulPlanid =jsonArray.getJSONObject(0).getString("mulid");
                    mulPlanName =jsonArray.getJSONObject(0).getString("mulPlanName"); //計畫名稱
                    message =jsonArray.getJSONObject(0).getString("message"); //祝福

                    tv_multi_name.setText(mulPlanName); //計畫名稱


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
                    Toast.makeText(ReceivedMultipleActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.planList , sender, planid);
    }

    //------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
