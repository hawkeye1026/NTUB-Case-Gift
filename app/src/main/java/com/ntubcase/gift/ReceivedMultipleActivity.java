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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.PlanMultiAdapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.writeFeedbackAsyncTask;
import com.ntubcase.gift.MyAsyncTask.receive.receiveUploadCompleteAsyncTask;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceivedMultipleActivity extends AppCompatActivity {

    private EditText et_name, et_message, et_sender, et_gift_message;
    private String planID;
    private LinearLayout ll_button;
    private Button btn_complete, btn_reward;
    private TextView tv_date, tv_reward_time;

    private PlanMultiAdapter planMultiAdapter;
    private GridView gridView;

    private String mulPlanName, message, sender, feedback;
    private List<Map<String, Object>> selectDates = new ArrayList<Map<String, Object>>(); //收禮資料

    private EditText et_feedback;
    private Button btn_can, btn_ent;

    private String endDate;
    //-------判斷按鈕是否可按 true = 可
    private Boolean isClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_multiple);
        setTitle("收禮細節");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //------------------------------------------------------------------------------------------
        et_name = findViewById(R.id.et_name);
        et_message = findViewById(R.id.et_message);
        et_sender = findViewById(R.id.et_sender);
        ll_button = findViewById(R.id.ll_button);
        btn_complete = findViewById(R.id.muti_btn_complete);

        //---------------------------------取得收禮詳細-----------------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            String from = bundle.getString("from");
            if (!from.equals("GiftReceivedDone")){
                ll_button.setVisibility(View.VISIBLE); //進行中禮物才會顯示按鈕
            }

            planID = bundle.getString("planID");
            showPlanDetail(planID);  //顯示收禮詳細資料
        }

        //--------------------------------收禮完成按鈕-----------------------------------
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final receiveUploadCompleteAsyncTask myAsyncTask = new receiveUploadCompleteAsyncTask(new receiveUploadCompleteAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {
                    }
                });
                if(isClick){
                    new AlertDialog.Builder(ReceivedMultipleActivity.this)
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
                    Toast.makeText(getApplicationContext(),"禮物還沒全部領取完喔", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //---------------------------------GridView---------------------------------------------
        gridView = (GridView) findViewById(R.id.gridView);
        planMultiAdapter = new PlanMultiAdapter(this, selectDates);
        gridView.setAdapter(planMultiAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialog(position);  //顯示Dialog
            }
        });
    }

    //-----------------顯示Dialog-----------------
    private void showAlertDialog(int position) {
        final int gridPosition = position;

        final Dialog mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.received_multi_grid_layout);

        //--------------------------------設定顯示的資料--------------------------------
        tv_date  = mDialog.findViewById(R.id.tv_date);
        et_gift_message  = mDialog.findViewById(R.id.et_gift_message);
        btn_reward  = mDialog.findViewById(R.id.btn_reward);
        tv_reward_time  = mDialog.findViewById(R.id.tv_reward_time);

        tv_date.setText(""+selectDates.get(gridPosition).get("date"));

        et_gift_message.setText(selectDates.get(gridPosition).get("message").toString());
        et_gift_message.setKeyListener(null);
        tv_reward_time.setText("領取時間："+selectDates.get(gridPosition).get("time").toString());

        //--------------------------------判斷是否能領取禮物--------------------------------
        final List<String> giftContent = (List<String>)selectDates.get(position).get("giftContent");
        final List<String> giftType = (List<String>)selectDates.get(position).get("giftType");

        if (giftContent.size()>0){  //若有禮物
            if(checkRewardTime(position)){  //已過領取時間
                btn_reward.getBackground().clearColorFilter(); //-----按鈕顯示正常-----
                btn_reward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReceivedMultipleActivity.this, ReceivedShowGiftActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("giftContent", (Serializable) giftContent);
                        bundle.putSerializable("giftType", (Serializable) giftType);
                        intent.putExtras(bundle);
                        startActivity(intent);

                        mDialog.dismiss();
                    }
                });
            }else{  //還沒到領取時間
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                btn_reward.getBackground().setColorFilter(filter);  //-----按鈕顯示灰階-----

                btn_reward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ReceivedMultipleActivity.this,"領取時間還沒到喔",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }else{ //沒有禮物僅顯示留言
            btn_reward.setVisibility(View.GONE);
            tv_reward_time.setVisibility(View.GONE);
        }

        mDialog.show();
    }

    //------------------------------檢查是否已過領取時間------------------------------
    private boolean checkRewardTime(int position){
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date nowDateTime = new Date(System.currentTimeMillis()); //現在日期與時間

        try {
            Date rewardTime = sdFormat.parse(selectDates.get(position).get("date")+" "+selectDates.get(position).get("time"));

            if (nowDateTime.before(rewardTime)) return false; //還沒到領取時間
            else return true;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
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
                    endDate =jsonArray.getJSONObject(0).getString("endDate"); //計畫名稱
                    message =jsonArray.getJSONObject(0).getString("message"); //祝福
                    sender = jsonArray.getJSONObject(0).getString("nickname"); //送禮人

                    isClick = whatColor(endDate);

                    et_name.setText(mulPlanName); //計畫名稱
                    et_name.setKeyListener(null);
                    et_message.setText("祝福："+message); //祝福
                    et_message.setKeyListener(null);
                    et_sender.setText("送禮人："+sender); //送禮人
                    et_sender.setKeyListener(null);

                    //----------------------------取得feedback----------------------------
                    jsonArray = object.getJSONArray("record");
                    feedback =jsonArray.getJSONObject(0).getString("feedback");

                    //----------------------------取得規劃日期內的資料----------------------------
                    jsonArray = object.getJSONArray("mulList");
                    int mulListLength = jsonArray.length();

                    Map<String, Object> mDates;
                    for (int i = 0 ; i < mulListLength ; i++){
                        String mulSendGiftDate = jsonArray.getJSONObject(i).getString("sendGiftDate"); //禮物送禮日期時間
                        String date = mulSendGiftDate.substring(0,10); //日期
                        String mulGoal = jsonArray.getJSONObject(i).getString("goal"); //留言
                        //String mulGiftid = jsonArray.getJSONObject(i).getString("giftid"); //禮物ID
                        String mulGiftName = jsonArray.getJSONObject(i).getString("giftName"); //禮物名稱
                        String mulGiftContent = jsonArray.getJSONObject(i).getString("gift"); //禮物內容
                        String mulGiftType = jsonArray.getJSONObject(i).getString("type"); //禮物類型
                        String sendTime = mulSendGiftDate.substring(11,16); //送禮時間

                        if (mulGiftName.equals("null")){ //若為null則顯示空值
                            mulGiftName = "";
                            sendTime = "";
                            mulGiftContent ="";
                            mulGiftType="";
                        }

                        //-----同日期 則 更新禮物資料-----
                        int checkSameDate=0;
                        for (checkSameDate=0; checkSameDate<selectDates.size(); checkSameDate++){
                            if (date.equals(selectDates.get(checkSameDate).get("date"))){ //同日期
                                List<String> giftContent = (List<String>) selectDates.get(checkSameDate).get("giftContent"); //禮物內容
                                List<String> giftType = (List<String>) selectDates.get(checkSameDate).get("giftType"); //禮物類型
                                giftContent.add(mulGiftContent);
                                giftType.add(mulGiftType);

                                selectDates.get(checkSameDate).put("giftContent", giftContent);
                                selectDates.get(checkSameDate).put("giftType", giftType);

                                //禮物名稱
                                selectDates.get(checkSameDate).put("gifts",
                                        selectDates.get(checkSameDate).get("gifts")+" , "+mulGiftName);
                                break;
                            }
                        }

                        //-----不同日期 則 新增資料-----
                        if (checkSameDate==selectDates.size()){
                            List<String> giftContent=new ArrayList<>();//禮物內容
                            List<String> giftType=new ArrayList<>();//禮物類型

                            if (!mulGiftContent.equals("")){ //有禮物才新增
                                giftContent.add(mulGiftContent);
                                giftType.add(mulGiftType);
                            }

                            mDates = new HashMap<String, Object>();
                            mDates.put("date", date); //日期
                            mDates.put("message", mulGoal); //留言
                            mDates.put("time", sendTime); //時間
                            mDates.put("gifts", mulGiftName); //禮物名稱
                            mDates.put("giftContent", giftContent); //禮物內容
                            mDates.put("giftType", giftType); //禮物類型
                            selectDates.add(mDates);
                        }
                    }

                    planMultiAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(ReceivedMultipleActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.receiveDetail , userData.getUserID(), planID);
    }

    //---------------------------------填寫回饋---------------------------------------------
    private void writeFeedback(){
        final Dialog mDialog = new Dialog(ReceivedMultipleActivity.this);
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
                Intent intent;
                intent = new Intent(this, HelpActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("from", "ReceivedMultipleActivity");
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

    public Boolean whatColor(String lastSentTime){

        Log.v("deadline",endDate);
        //截止日期時間

        if(checkReceivedTime.checkReceivedTime(lastSentTime)){
            //-----------解除灰色
            btn_complete.getBackground().clearColorFilter();
            return true;
        }else{
            //-----------灰色模糊
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            btn_complete.getBackground().setColorFilter(filter);  //-----按鈕顯示灰階-----
            return false;
        }

    }
}
