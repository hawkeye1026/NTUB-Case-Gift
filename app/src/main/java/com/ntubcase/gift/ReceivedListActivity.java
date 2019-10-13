package com.ntubcase.gift;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.ReceivedPlanListAdapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.writeFeedbackAsyncTask;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceivedListActivity extends AppCompatActivity {

    private TextView tv_name, tv_sender, tv_deadline;
    private String planID;

    private RecyclerView recycler_view;
    private ReceivedPlanListAdapter receivedPlanListAdapter;
    private List<Map<String, String>> missionData = new ArrayList<Map<String, String>>(); //任務清單資料

    private Button btn_complete;
    private String planName, feedback;

    private EditText et_feedback;
    private Button btn_can, btn_ent;

    private List<String> giftContent=new ArrayList<>();//禮物內容
    private List<String> giftType=new ArrayList<>();//禮物類型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_list);
        setTitle("收禮細節");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //------------------------------------------------------------------------------------------
        tv_name = findViewById(R.id.tv_name);
        tv_sender = findViewById(R.id.tv_sender);
        tv_deadline = findViewById(R.id.tv_deadline);
        btn_complete = findViewById(R.id.btn_complete);

        //---------------------------------取得收禮詳細-----------------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            planID = bundle.getString("planID");
            showPlanDetail(planID);  //顯示收禮詳細資料
        }

        //----------------------------清單內容RecyclerView------------------------------
        recycler_view = findViewById(R.id.list_recycle_view);  // 設置RecyclerView為列表型態
        recycler_view.setLayoutManager(new LinearLayoutManager(this));  // 設置格線
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        receivedPlanListAdapter = new ReceivedPlanListAdapter(missionData);  // 將資料交給adapter
        recycler_view.setAdapter(receivedPlanListAdapter); // 設置adapter給recycler_view

        //----------------------------完成任務 按鈕------------------------------
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receivedPlanListAdapter.isMissionComplete()){
                    Intent intent;
                    intent = new Intent(ReceivedListActivity.this, ReceivedShowGiftActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("giftContent", (Serializable) giftContent);
                    bundle.putSerializable("giftType", (Serializable) giftType);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"還有任務未完成喔", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //------------------------------收禮詳細，顯示plan資料------------------------------
    private void showPlanDetail(String planID){
        planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(ReceivedListActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray;

                    //----------------------------取得計畫資料----------------------------
                    jsonArray = object.getJSONArray("misPlan");
                    //String misPlanid =jsonArray.getJSONObject(0).getString("misid"); //計畫ID
                    //String misCreateDate = jsonArray.getJSONObject(0).getString("createDate"); //計畫建立日期
                    planName =jsonArray.getJSONObject(0).getString("misPlanName"); //計畫名稱
                    //String misSendPlanDate = jsonArray.getJSONObject(0).getString("sendPlanDate").substring(0,10); //送禮日期
                    String deadline = jsonArray.getJSONObject(0).getString("deadline"); //截止日期時間
                    String sender = jsonArray.getJSONObject(0).getString("nickname"); //送禮人

                    tv_name.setText(planName); //計畫名稱

                    //截止日期時間
                    if (deadline.equals("0000-00-00 00:00:00")) tv_deadline.setText("無限制");
                    else tv_deadline.setText(deadline.substring(0,16)); //截止日期時間

                    tv_sender.setText(sender); //送禮人

                    //----------------------------取得禮物資料----------------------------
                    jsonArray = object.getJSONArray("misList");
                    int misListLength = jsonArray.length();

                    giftContent=new ArrayList<>();//禮物內容
                    giftType=new ArrayList<>();//禮物類型

                    for (int i = 0 ; i < misListLength ; i++){
                        giftContent.add(jsonArray.getJSONObject(i).getString("gift")); //禮物內容
                        giftType.add(jsonArray.getJSONObject(i).getString("type")); //禮物類型
                    }

                    //----------------------------取得feedback----------------------------
                    jsonArray = object.getJSONArray("record");
                    feedback =jsonArray.getJSONObject(0).getString("feedback");

                    //----------------------------取得任務項目----------------------------
                    jsonArray = object.getJSONArray("misItem");
                    int misItemLength = jsonArray.length();

                    Map<String, String> data;
                    for (int i = 0 ; i < misItemLength ; i++){
                        String itemNumber = jsonArray.getJSONObject(i).getString("itemNumber"); //項目編號
                        String itemContent = jsonArray.getJSONObject(i).getString("content"); //項目內容
//                        String itemCheck = jsonArray.getJSONObject(i).getString("itemCheck"); //打勾項目編號

//                        Log.e("***","no."+itemNumber+" "+itemCheck);

                        data = new HashMap<String, String>();
                        data.put("itemNumber", itemNumber);
                        data.put("itemContent", itemContent);
//                        data.put("itemCheck", itemCheck);
                        missionData.add(data);
                    }
                    receivedPlanListAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(ReceivedListActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.receiveDetail , userData.getUserID(), planID);
    }

    //------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_received, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar返回建
                finish();
                return true;
            case R.id.action_help:  //說明鈕
                Toast.makeText(this, "顯示說明圖", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_feedback:  //填寫回饋鈕
                writeFeedback();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //---------------------------------填寫回饋---------------------------------------------
    private void writeFeedback(){
        final Dialog mDialog = new Dialog(ReceivedListActivity.this);
        mDialog.setContentView(R.layout.feedback_write_layout);

        et_feedback  = mDialog.findViewById(R.id.et_feedback);
        btn_can  = mDialog.findViewById(R.id.btn_can);
        btn_ent  = mDialog.findViewById(R.id.btn_ent);

        et_feedback.setText(feedback);

        //-------------dialog按鈕-------------
        btn_ent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = et_feedback.getText().toString();

                writeFeedbackAsyncTask writeFeedbackAsyncTask = new writeFeedbackAsyncTask(new writeFeedbackAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {
                        try {
                            if (result == null) { return; }
                        } catch (Exception e) {
                        }
                    }
                });
                writeFeedbackAsyncTask.execute(Common.writeFeedback , planID, userData.getUserID(), feedback);
                mDialog.dismiss();
            }
        });

        btn_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }
}
