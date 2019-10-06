package com.ntubcase.gift;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.plan_single_adapter;
import com.ntubcase.gift.Adapter.re_plan_single_adapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.writeFeedbackAsyncTask;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceivedSingleActivity extends AppCompatActivity {

    private TextView tv_name, tv_sender,tv_sentTime;
    private Button btn_feedback, btn_Received;

    private RecyclerView recycler_view;
    private re_plan_single_adapter adapter;
    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

    private String planID, feedback;
    private EditText et_feedback;
    private Button btn_can, btn_ent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_single);
        setTitle("收禮細節");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //------------------------------------------------------------------------------------------
        tv_name = findViewById(R.id.tv_name);
        tv_sender = findViewById(R.id.tv_sender);
        tv_sentTime = findViewById(R.id.sentTime);
        btn_Received = findViewById(R.id.btnReceived);
        btn_feedback = findViewById(R.id.btn_feedback);

        //-----------------------------------------------------------------------
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // 設置格線
        adapter = new re_plan_single_adapter(mData); // 將資料交給adapter
        adapter.isFromMake=false;
        recycler_view.setAdapter(adapter);  // 設置adapter給recycler_view
        adapter.setOnItemClickListener(new re_plan_single_adapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                //showDataDialog(position);
            }
        });

        //---------------------------------取得收禮詳細-----------------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            planID = bundle.getString("planID");
            showPlanDetail(planID);  //顯示收禮詳細資料
        }

        //---------------------------------填寫回饋 按鈕---------------------------------------------
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog mDialog = new Dialog(ReceivedSingleActivity.this);
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
        });

    }

    //------------------------------收禮詳細，顯示plan資料------------------------------
    private void showPlanDetail(String planid){
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

                    tv_name.setText(sinPlanName); //計畫名稱
                    tv_sender.setText(nickname); //送禮人

                    //----------------------------取得feedback----------------------------
                    jsonArray = object.getJSONArray("record");
                    feedback =jsonArray.getJSONObject(0).getString("feedback");

                    //----------------------------取得禮物資料----------------------------
                    jsonArray = object.getJSONArray("sinList");
                    int sinListLength = jsonArray.length();

                    for (int i = 0 ; i < sinListLength ; i++){
                        //String sinPlanid = jsonArray.getJSONObject(i).getString("sinid"); //計畫ID
                        //String sinGift = jsonArray.getJSONObject(i).getString("gift"); //禮物內容
                        String sinGiftName = jsonArray.getJSONObject(i).getString("giftName"); //禮物名稱
                        String sinSendGiftDate = jsonArray.getJSONObject(i).getString("sendGiftDate"); //送出時間
                        String sinMessage = jsonArray.getJSONObject(i).getString("message"); //留言
                        String sinGiftType = jsonArray.getJSONObject(i).getString("type"); //禮物類型
                        String sinGiftid = jsonArray.getJSONObject(i).getString("giftid"); //禮物ID

                        Map<String, Object> mGiftsData = new HashMap<String, Object>();
                        mGiftsData.put("giftName", sinGiftName);
                        mGiftsData.put("sentTime", sinSendGiftDate.substring(11,16));
                        mGiftsData.put("message", sinMessage);
                        mGiftsData.put("type", sinGiftType);
                        mData.add(mGiftsData);
                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(ReceivedSingleActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.receiveDetail , userData.getUserID(), planid);
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
