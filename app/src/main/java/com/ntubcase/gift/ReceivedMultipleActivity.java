package com.ntubcase.gift;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.PlanMultiAdapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceivedMultipleActivity extends AppCompatActivity {

    private TextView tv_name, tv_message, tv_sender;
    private Button btn_feedback;
    private String planID;

    private PlanMultiAdapter planMultiAdapter;
    private GridView gridView;

    private String mulPlanName, message, sender;
    private List<Map<String, Object>> selectDates = new ArrayList<Map<String, Object>>(); //收禮資料

    private EditText et_feedback;
    private Button btn_can, btn_ent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_multiple);
        setTitle("收禮細節");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //------------------------------------------------------------------------------------------
        tv_name = findViewById(R.id.tv_name);
        tv_message = findViewById(R.id.tv_message);
        tv_sender = findViewById(R.id.tv_sender);
        btn_feedback = findViewById(R.id.btn_feedback);

        //---------------------------------取得收禮詳細-----------------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            planID = bundle.getString("planID");
            showPlanDetail(planID);  //顯示收禮詳細資料
        }

        //---------------------------------GridView---------------------------------------------
        gridView = (GridView) findViewById(R.id.gridView);
        planMultiAdapter = new PlanMultiAdapter(this, selectDates);
        gridView.setAdapter(planMultiAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //showAlertDialog(position,parent);  //顯示alertDialog
            }
        });

        //---------------------------------填寫回饋 按鈕---------------------------------------------
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog mDialog = new Dialog(ReceivedMultipleActivity.this);
                mDialog.setContentView(R.layout.feedback_layout);

                et_feedback  = mDialog.findViewById(R.id.et_feedback);
                btn_can  = mDialog.findViewById(R.id.btn_can);
                btn_ent  = mDialog.findViewById(R.id.btn_ent);

                //-------------dialog按鈕-------------
                btn_ent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
    private void showPlanDetail(String planID){
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
                    //String mulPlanid =jsonArray.getJSONObject(0).getString("mulid");
                    mulPlanName =jsonArray.getJSONObject(0).getString("mulPlanName"); //計畫名稱
                    message =jsonArray.getJSONObject(0).getString("message"); //祝福
                    sender = jsonArray.getJSONObject(0).getString("nickname"); //送禮人

                    tv_name.setText(mulPlanName); //計畫名稱
                    tv_message.setText("留言："+message); //祝福
                    tv_sender.setText("送禮人："+sender); //送禮人

                    //----------------------------取得規劃日期內的資料----------------------------
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
                        selectDates.add(mDates);
                    }
                    planMultiAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(ReceivedMultipleActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.receiveDetail , "", planID);
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
