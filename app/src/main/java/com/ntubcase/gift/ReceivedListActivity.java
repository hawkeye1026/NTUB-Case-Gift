package com.ntubcase.gift;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.ReceivedPlanListAdapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.writeFeedbackAsyncTask;
import com.ntubcase.gift.MyAsyncTask.receive.receiveUploadCompleteAsyncTask;
import com.ntubcase.gift.MyAsyncTask.receive.missionInsertCheckAsyncTask;
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
    private String planID, from;
    private LinearLayout ll_complete;
    private Button btn_complete;

    private RecyclerView recycler_view;
    private ReceivedPlanListAdapter receivedPlanListAdapter;
    private List<Map<String, String>> missionData = new ArrayList<Map<String, String>>(); //任務清單資料

    private Button btn_reward;
    private String planName, feedback;

    private EditText et_feedback;
    private Button btn_can, btn_ent;

    private List<String> giftContent=new ArrayList<>();//禮物內容
    private List<String> giftType=new ArrayList<>();//禮物類型

    private String deadline;

    //-------判斷按鈕是否可按 true = 可
    private Boolean isClick;

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
        btn_reward = findViewById(R.id.btn_reward);
        ll_complete = findViewById(R.id.ll_complete);
        btn_complete = findViewById(R.id.list_btn_complete);

        //---------------------------------取得收禮詳細-----------------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            from = bundle.getString("from");
            if (!from.equals("GiftReceivedDone")){
                ll_complete.setVisibility(View.VISIBLE); //進行中禮物才會顯示按鈕
            }

            planID = bundle.getString("planID");
            showPlanDetail(planID);  //顯示收禮詳細資料
        }

        //----------------------------清單內容RecyclerView------------------------------
        recycler_view = findViewById(R.id.list_recycle_view);  // 設置RecyclerView為列表型態
        recycler_view.setLayoutManager(new LinearLayoutManager(this));  // 設置格線
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        receivedPlanListAdapter = new ReceivedPlanListAdapter(missionData);  // 將資料交給adapter
        recycler_view.setAdapter(receivedPlanListAdapter); // 設置adapter給recycler_view

        //----------------------------領取禮物 按鈕------------------------------
        btn_reward.setOnClickListener(new View.OnClickListener() {
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
                }else if (receivedPlanListAdapter.isCheckDisable){
                    Toast.makeText(getApplicationContext(),"任務時限已過", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"還有任務未完成喔", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //---------------------------------收禮完成按鈕-----------------------------------
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final receiveUploadCompleteAsyncTask myAsyncTask = new receiveUploadCompleteAsyncTask(new receiveUploadCompleteAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {

                    }
                });
                if(isClick){
                    new AlertDialog.Builder(ReceivedListActivity.this)
                            .setTitle("您確定要完成收禮嗎?")
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myAsyncTask.execute(Common.updateComplete, planID, userData.getUserID());
                                    finish();
                                }
                            })
                            .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }else{
                    Toast.makeText(getApplicationContext(),"尚未完成任務喔", Toast.LENGTH_SHORT).show();
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
                    deadline = jsonArray.getJSONObject(0).getString("deadline"); //截止日期時間
                    String sender = jsonArray.getJSONObject(0).getString("nickname"); //送禮人

                    tv_name.setText(planName); //計畫名稱
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

                        data = new HashMap<String, String>();
                        data.put("itemNumber", itemNumber);
                        data.put("itemContent", itemContent);
                        data.put("itemChecked", "");
                        missionData.add(data);
                    }

                    //----------------------------取得任務勾選項目----------------------------
                    jsonArray = object.getJSONArray("misCheck");
                    int misCheckLength = jsonArray.length();

                    for (int i = 0 ; i < misCheckLength ; i++){
                        String itemChecked = jsonArray.getJSONObject(i).getString("itemChecked"); //勾選項目編號
                        missionData.get(Integer.parseInt(itemChecked)-1).put("itemChecked", itemChecked);
                    }

                    isClick = whatColor(deadline); //判斷按鈕顏色
                    receivedPlanListAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(ReceivedListActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.receiveDetail , userData.getUserID(), planID);
    }

    //------------------------------先刪除舊的勾選------------------------------
    private void deleteCheck(){
        planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(ReceivedListActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (Exception e) {
                    Toast.makeText(ReceivedListActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.deleteMisCheck , userData.getUserID(), planID); // receiveid , planid
    }

    //------------------------------再新增新的勾選------------------------------
    private void insertCheck(){
        List<Boolean> misstionCheck = receivedPlanListAdapter.getMissionCheck();

        for (int i=0; i<missionData.size(); i++){
            if (misstionCheck.get(i)){
                missionInsertCheckAsyncTask missionInsertCheckAsyncTask = new missionInsertCheckAsyncTask(new missionInsertCheckAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {
                        try {
                            if (result == null) {
                                Toast.makeText(ReceivedListActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (Exception e) {
                            Toast.makeText(ReceivedListActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                missionInsertCheckAsyncTask.execute(Common.insertMisCheck, planID, String.valueOf(i+1) , userData.getUserID()); //planid, itemNumber, receiveid
            }
        }
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
                //---上傳勾選紀錄---
                deleteCheck();
                insertCheck();

                finish();
                return true;
            case R.id.action_help:  //說明鈕
                Intent intent;
                intent = new Intent(this, HelpActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("from", "ReceivedListActivity");
                intent.putExtras(bundle);
                startActivity(intent);
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

    //----------------------------判斷按鈕顏色----------------------------
    private boolean whatColor(String lastSentTime){

        btn_reward.getBackground().clearColorFilter();

        if (from.equals("GiftReceivedDone")){
            receivedPlanListAdapter.isCheckDisable=true; //checkbox不可選取
            if (!checkIsMissionComplete()) btn_reward.setVisibility(View.INVISIBLE);
        }

        if (lastSentTime.equals("0000-00-00 00:00:00")){ //---------------無時限---------------
            tv_deadline.setText("無限制");

            if (checkIsMissionComplete()) { //判斷是否勾完
                btn_complete.getBackground().clearColorFilter();
                return true;
            }else{
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                btn_complete.getBackground().setColorFilter(filter);  //-----按鈕顯示灰階-----

                return false;
            }
        }else { //---------------有時限---------------
            tv_deadline.setText(lastSentTime.substring(0,16));

            if(checkReceivedTime.checkReceivedTime(lastSentTime)){
                //----------時限已過----------
                btn_complete.getBackground().clearColorFilter(); //解除灰色
                receivedPlanListAdapter.isCheckDisable=true; //checkbox不可選取

                if (!checkIsMissionComplete()) { //判斷是否勾完
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    btn_reward.getBackground().setColorFilter(filter);  //-----按鈕顯示灰階-----
                }
                return true;

            }else{
                //----------時限還沒過----------
                if (checkIsMissionComplete()) { //判斷是否勾完
                    btn_complete.getBackground().clearColorFilter();
                    return true;
                }else{
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    btn_complete.getBackground().setColorFilter(filter);  //-----按鈕顯示灰階-----
                    return false;
                }
            }
        }

    }

    //-----檢查任務清單是否都完成---
    private boolean checkIsMissionComplete(){
        int count=0;
        for (int i=0; i<missionData.size(); i++){
            String isCheck = missionData.get(i).get("itemChecked");
            if (!isCheck.equals("")) count++;
        }

        if (count==missionData.size()) return true;

        return false;
    }

}
