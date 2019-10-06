package com.ntubcase.gift;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.plan_single_adapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planCancelSentAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentPlanSingleActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------
    static EditText edt_single_name, edt_single_message, edt_single_date, edt_single_friend, edt_single_giftName, edt_single_sentTime;
    //----------------------------------------------------------------------------------------------
    ProgressDialog barProgressDialog;
    private RecyclerView recycler_view;
    private plan_single_adapter adapter;
    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

    private Button btn_save, btn_send, btn_cancel, btn_feedback, btnAdd, btnEnt, btnCan;
    private String sender=userData.getUserID(), sinPlanid;

    private EditText et_feedback;
    private List<String[]> feedback = new ArrayList<>();
    private Button btn_ent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan_single);
        setTitle(R.string.planSingle);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------------------------------------------------------------------
        edt_single_name = findViewById(R.id.add_surprise_name);
        edt_single_date = findViewById(R.id.add_surprise_date);
        edt_single_friend = findViewById(R.id.add_surprise_friend);
        edt_single_name.setInputType(InputType.TYPE_NULL);
        edt_single_date.setInputType(InputType.TYPE_NULL);
        edt_single_friend.setInputType(InputType.TYPE_NULL);
        btnAdd = (Button) findViewById(R.id.btnAdd); //新增按鈕
        btnAdd.setVisibility(View.INVISIBLE);

        //-----------------------------------------------------------------------
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this)); // 設置RecyclerView為列表型態
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); // 設置格線
        adapter = new plan_single_adapter(mData); // 將資料交給adapter
        adapter.isFromMake=false;
        recycler_view.setAdapter(adapter);  // 設置adapter給recycler_view
        adapter.setOnItemClickListener(new plan_single_adapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                showDataDialog(position);
            }
        });

        //-------------------------取得計畫詳細---------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            String planID = bundle.getString("planID");
            showPlanDetail(planID);  //顯示計畫詳細資料
        }

        //-----------------------------------------------------------------------
        btn_save = findViewById(R.id.btn_plan_save); //儲存按鈕
        btn_save.setVisibility(View.GONE);
        btn_send = findViewById(R.id.btn_plan_send); //預送按鈕
        btn_send.setVisibility(View.GONE);
        btn_cancel = findViewById(R.id.btn_plan_cancel); //取消預送按鈕
        btn_feedback = findViewById(R.id.btn_plan_feedback); //查看回饋按鈕
        String from = bundle.getString("from");
        if (from.equals("PlanSentFragment")){
            btn_cancel.setVisibility(View.VISIBLE);
            btn_cancel.setOnClickListener(planCancelClickListener);
        }else if (from.equals("PlanDoneFragment")){
            btn_feedback.setVisibility(View.VISIBLE);
            btn_feedback.setOnClickListener(checkFeedbackClickListener);
        }

    }
    //-----------------------------------------------顯示一筆禮物的詳細----------------------------------------------
    private void showDataDialog(final int position) {
        final Dialog dialog = new Dialog(SentPlanSingleActivity.this);
        dialog.setContentView(R.layout.single_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);//讓使用者點選背景或上一頁沒有用

        //----------------------------------------------------------------------------------------------
        edt_single_giftName = dialog.findViewById(R.id.single_giftName);
        edt_single_sentTime = dialog.findViewById(R.id.single_sentTime);
        edt_single_message = dialog.findViewById(R.id.single_message);
        edt_single_giftName.setText(mData.get(position).get("giftName").toString());
        edt_single_sentTime.setText(mData.get(position).get("sentTime").toString());
        edt_single_message.setText(mData.get(position).get("message").toString());
        edt_single_giftName.setInputType(InputType.TYPE_NULL);
        edt_single_sentTime.setInputType(InputType.TYPE_NULL);
        edt_single_message.setInputType(InputType.TYPE_NULL);

        btnEnt = dialog.findViewById(R.id.btn_ent);
        btnEnt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btnCan = dialog.findViewById(R.id.btn_can);
        btnCan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    //-------------------------------取消預送按鈕 監聽器----------------------------------------
    private View.OnClickListener planCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(SentPlanSingleActivity.this)
                    .setTitle("是否取消您的預送計畫?")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            planCancelSentAsyncTask planCancelSentAsyncTask = new planCancelSentAsyncTask(new planCancelSentAsyncTask.TaskListener() {
                                @Override
                                public void onFinished(String result) {
                                }
                            });
                            planCancelSentAsyncTask.execute(Common.planCancelSent, sender, sinPlanid, "0");

                            Toast.makeText(getApplicationContext(), "已取消預送", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNeutralButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    };

    //-------------------------------查看回饋按鈕 監聽器----------------------------------------
    private View.OnClickListener checkFeedbackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Dialog mDialog = new Dialog(SentPlanSingleActivity.this);
            mDialog.setContentView(R.layout.feedback_check_layout);

            et_feedback  = mDialog.findViewById(R.id.et_feedback);
            et_feedback.setKeyListener(null);

            String allFeedback="";
            for (int i=0; i<feedback.size(); i++){
                allFeedback+= feedback.get(i)[0]+":\n"+feedback.get(i)[1]+"\n";
            }
            et_feedback.setText(allFeedback);

            //-------------dialog按鈕-------------
            btn_ent  = mDialog.findViewById(R.id.btn_ent);
            btn_ent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            mDialog.show();
        }
    };

    //------------------------------計畫詳細，顯示plan資料------------------------------
    private void showPlanDetail(String planid){
        planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(SentPlanSingleActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray;

                    //----------------------------取得計畫資料----------------------------
                    jsonArray = object.getJSONArray("sinPlan");
                    sinPlanid =jsonArray.getJSONObject(0).getString("sinid");
                    String sinPlanName =jsonArray.getJSONObject(0).getString("sinPlanName"); //計畫名稱
                    String sinSendPlanDate = jsonArray.getJSONObject(0).getString("sendPlanDate").substring(0,10); //送禮日期

                    edt_single_name.setText(sinPlanName); //計畫名稱
                    edt_single_date.setText(sinSendPlanDate); //送禮日期

                    //----------------------------取得收禮人,feedback資料----------------------------
                    jsonArray = object.getJSONArray("record");
                    int recordLength = jsonArray.length();

                    String friendName = "";
                    for (int i = 0; i < recordLength; i++) {
                        String fNickname = jsonArray.getJSONObject(i).getString("nickname");
                        String fFeedback = jsonArray.getJSONObject(i).getString("feedback");
                        String[] a ={fNickname,fFeedback};

                        if (friendName.equals("")) friendName += fNickname;
                        else friendName += " , " + fNickname;

                        feedback.add(a);
                    }
                    edt_single_friend.setText(friendName); //收禮人名稱

                    //----------------------------取得禮物資料----------------------------
                    jsonArray = object.getJSONArray("sinList");
                    int sinListLength = jsonArray.length();

                    for (int i = 0 ; i < sinListLength ; i++){
                        String sinGiftName = jsonArray.getJSONObject(i).getString("giftName"); //禮物名稱
                        String sinSendGiftDate = jsonArray.getJSONObject(i).getString("sendGiftDate"); //送出時間
                        String sinMessage = jsonArray.getJSONObject(i).getString("message"); //留言

                        Map<String, Object> mGiftsData = new HashMap<String, Object>();
                        mGiftsData.put("giftName", sinGiftName);
                        mGiftsData.put("sentTime", sinSendGiftDate.substring(11,16));
                        mGiftsData.put("message", sinMessage);
                        mData.add(mGiftsData);
                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(SentPlanSingleActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.planList , sender, planid);
    }

    //-----------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
