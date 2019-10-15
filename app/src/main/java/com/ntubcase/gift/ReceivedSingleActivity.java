package com.ntubcase.gift;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.ReceivedPlanSingleAdapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.writeFeedbackAsyncTask;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceivedSingleActivity extends AppCompatActivity {

    private TextView tv_name, tv_sender;

    private RecyclerView recycler_view;
    private ReceivedPlanSingleAdapter adapter;
    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

    private String planID, feedback;
    private EditText et_feedback;
    private Button btn_can, btn_ent;
    private SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm");

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

        //-----------------------------------------------------------------------
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // 設置格線
        adapter = new ReceivedPlanSingleAdapter(this, mData); // 將資料交給adapter
        adapter.isFromMake=false;
        recycler_view.setAdapter(adapter);  // 設置adapter給recycler_view

        //---------------------------------取得收禮詳細-----------------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            planID = bundle.getString("planID");
            showPlanDetail(planID);  //顯示收禮詳細資料
        }

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
                        String sinSendGiftDate = jsonArray.getJSONObject(i).getString("sendGiftDate"); //送出日期時間
                        String sinSendGiftTime =sinSendGiftDate.substring(11,16); //送出時間
                        String sinMessage = jsonArray.getJSONObject(i).getString("message"); //留言
                        //String sinGiftid = jsonArray.getJSONObject(i).getString("giftid"); //禮物ID
                        String sinGiftContent = jsonArray.getJSONObject(i).getString("gift"); //禮物內容
                        //String sinGiftName = jsonArray.getJSONObject(i).getString("giftName"); //禮物名稱
                        String sinGiftType = jsonArray.getJSONObject(i).getString("type"); //禮物類型

                        //-----同時間 則 更新禮物資料-----
                        int checkSameTime=0;
                        for (checkSameTime=0; checkSameTime<mData.size(); checkSameTime++){
                            if (sinSendGiftTime.equals(mData.get(checkSameTime).get("sentTime"))){ //同時間
                                List<String> giftContent = (List<String>) mData.get(checkSameTime).get("giftContent"); //禮物內容
                                List<String> giftType = (List<String>) mData.get(checkSameTime).get("giftType"); //禮物類型
                                giftContent.add(sinGiftContent);
                                giftType.add(sinGiftType);

                                mData.get(checkSameTime).put("giftContent", giftContent);
                                mData.get(checkSameTime).put("giftType", giftType);

                                break;
                            }
                        }

                        //-----不同時間 則 新增禮物資料-----
                        if (checkSameTime==mData.size()){
                            List<String> giftContent=new ArrayList<>();//禮物內容
                            giftContent.add(sinGiftContent);
                            List<String> giftType=new ArrayList<>();//禮物類型
                            giftType.add(sinGiftType);

                            Map<String, Object> mGiftsData = new HashMap<String, Object>();
                            mGiftsData.put("sentTime", sinSendGiftTime);
                            mGiftsData.put("message", sinMessage);
                            mGiftsData.put("giftContent", giftContent);
                            mGiftsData.put("giftType", giftType);
                            mGiftsData.put("sinSendPlanDate", sinSendPlanDate);
                            mData.add(mGiftsData);
                        }

                    }

                    //--------按時間排序--------
                    Collections.sort(mData, new Comparator<Map<String, Object>>() {
                        @Override
                        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                            try {
                                Date time1 = sdfT.parse((String)o1.get("sentTime"));
                                Date time2 = sdfT.parse((String)o2.get("sentTime"));
                                return time1.compareTo(time2);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });

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
}
