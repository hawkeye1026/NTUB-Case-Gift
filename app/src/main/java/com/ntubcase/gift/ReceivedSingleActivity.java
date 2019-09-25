package com.ntubcase.gift;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.data.getFriendList;
import com.ntubcase.gift.data.getGiftList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReceivedSingleActivity extends AppCompatActivity {

    static TextView tv_single_name, tv_single_date, tv_single_friend;
    private String sender= "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_single);
        setTitle("收禮細節");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //------------------------------------------------------------------------------------------

        //宣告變數------------------------------------------------------------------------------
        tv_single_name = findViewById(R.id.add_surprise_name);
        tv_single_date = findViewById(R.id.add_surprise_date);
        tv_single_friend = findViewById(R.id.add_surprise_friend);


    }

    @Override
    protected void onResume() {
        showPlanDetail();
        super.onResume();
    }

    //------------------------------計畫詳細，顯示plan資料------------------------------
    private void showPlanDetail(){
        planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(ReceivedSingleActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray;

                    //----------------------------取得計畫資料----------------------------
                    jsonArray = object.getJSONArray("sinPlan");
                    String sinPlanid =jsonArray.getJSONObject(0).getString("sinid"); //計畫ID
                    String sinCreateDate = DateFormat.dateFormat(jsonArray.getJSONObject(0).getString("createDate")); //計畫建立日期
                    String sinPlanName =jsonArray.getJSONObject(0).getString("sinPlanName"); //計畫名稱
                    String sinSendPlanDate = jsonArray.getJSONObject(0).getString("sendPlanDate").substring(0,10); //送禮日期
                    String nickname =jsonArray.getJSONObject(0).getString("nickname"); //計畫名稱
                    String image = jsonArray.getJSONObject(0).getString("image"); //計畫名稱

                    if (sinSendPlanDate.equals("0000-00-00")) sinSendPlanDate="";  //若為0則顯示空值

                    tv_single_name.setText(sinPlanName); //計畫名稱
                    tv_single_date.setText(sinSendPlanDate); //送禮日期
                    tv_single_friend.setText(nickname); //計畫名稱

                    //----------------------------取得禮物資料----------------------------
                    jsonArray = object.getJSONArray("sinList");
                    int sinListLength = jsonArray.length();

                    for (int i = 0 ; i < sinListLength ; i++){
                        //String sinPlanid = jsonArray.getJSONObject(i).getString("sinid"); //計畫ID
                        //String sinGift = jsonArray.getJSONObject(i).getString("gift"); //禮物內容
                        String sinGiftName = jsonArray.getJSONObject(i).getString("giftName"); //禮物名稱
                        String sinSendGiftDate = jsonArray.getJSONObject(i).getString("sendGiftDate"); //送出時間
                        String sinMessage = jsonArray.getJSONObject(i).getString("message"); //留言
                        String sinGiftid = jsonArray.getJSONObject(i).getString("giftid"); //禮物ID
                    }

                } catch (Exception e) {
                    Toast.makeText(ReceivedSingleActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.receiveDetail , sender, "sin_20190919234325");
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
