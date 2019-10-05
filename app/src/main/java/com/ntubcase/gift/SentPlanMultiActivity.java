package com.ntubcase.gift;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.PlanMultiAdapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planCancelSentAsyncTask;
import com.ntubcase.gift.login_model.userData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SentPlanMultiActivity extends AppCompatActivity {
    private Button btn_save, btn_send, btn_cancel, btn_feedback;
    private GridView gridView;
    private PlanMultiAdapter planMultiAdapter;
    private TextView tv_receiveFriend, tv_message;

    private String planName, receiveFriend, message; //------bundle傳遞的資料

    private String sender=userData.getUserID(), planid;
    ProgressDialog barProgressDialog;

    private List<Map<String, Object>> selectDates = new ArrayList<Map<String, Object>>();  //選取的時間區段

    //-----cutomlayout內物件
    private EditText alert_message, alert_time, alert_gifts;
    private TextView tv_title;

    private EditText et_feedback;
    private Button btn_ent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_multiple);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------------------上一頁資料-----------------------------------
        Bundle bundle = getIntent().getExtras();
        planid = bundle.getString("planid");
        planName = bundle.getString("planName");    //計畫名稱
        receiveFriend = bundle.getString("receiveFriend");  //收禮人名稱
        message = bundle.getString("message");  //祝福
        selectDates = (ArrayList<Map<String, Object>>) bundle.getSerializable("selectDates");  //選取的時間區段
        String from = bundle.getString("from"); //從哪個頁面開啟

        setTitle(planName); //-----標題為計畫名稱-----
        tv_receiveFriend = (TextView) findViewById(R.id.tv_receiveFriend);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_receiveFriend.setText("To. " + receiveFriend);   //-----顯示收禮人-----
        tv_message.setText(message); //-----顯示祝福-----

        //---------------------------------GridView---------------------------------------------
        gridView = (GridView) findViewById(R.id.gridView);
        planMultiAdapter = new PlanMultiAdapter(this, selectDates);
        gridView.setAdapter(planMultiAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialog(position,parent);  //顯示alertDialog
            }
        });

        //-----------------------------------------------------------------------
        btn_save = findViewById(R.id.btn_plan_save); //儲存按鈕
        btn_save.setVisibility(View.GONE);
        btn_send = findViewById(R.id.btn_plan_send); //預送按鈕
        btn_send.setVisibility(View.GONE);
        btn_cancel = findViewById(R.id.btn_plan_cancel); //取消預送按鈕
        btn_feedback = findViewById(R.id.btn_plan_feedback); //查看回饋按鈕
        if (from.equals("PlanSentFragment")){
            btn_cancel.setVisibility(View.VISIBLE);
            btn_cancel.setOnClickListener(planCancelClickListener);
        }else if (from.equals("PlanDoneFragment")){
            btn_feedback.setVisibility(View.VISIBLE);
            btn_feedback.setOnClickListener(checkFeedbackClickListener);
        }

    }

    //-----------------顯示alertDialog-----------------
    private void showAlertDialog(int position, final ViewGroup parent) {
        final int gridPosition = position;

        final Dialog mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.plan_multi_alert_layout);

        //----------------------------------------設定customLayout內顯示的資料--------------------------------------------------
        alert_message  = mDialog.findViewById(R.id.alert_message);
        alert_time  = mDialog.findViewById(R.id.alert_time);
        alert_gifts  = mDialog.findViewById(R.id.alert_gifts);
        tv_title  = mDialog.findViewById(R.id.tv_title);

        String[] mdate = selectDates.get(gridPosition).get("date").toString().split("-");
        tv_title.setText("請輸入"+ mdate[1] + "月" + mdate[2] +"日規劃");

        alert_message.setText(selectDates.get(gridPosition).get("message").toString());
        alert_time.setText(selectDates.get(gridPosition).get("time").toString());
        alert_gifts.setText(selectDates.get(gridPosition).get("gifts").toString());
        alert_message.setInputType(InputType.TYPE_NULL);
        alert_time.setInputType(InputType.TYPE_NULL);
        alert_gifts.setInputType(InputType.TYPE_NULL);

        if ((alert_gifts.getText().toString()).equals(""))
            alert_time.setEnabled(false); //若禮物空白則不能選時間

        //-------------dialog按鈕-------------
        TextView btn_ok = mDialog.findViewById(R.id.btn_ok);
        TextView btn_cancel = mDialog.findViewById(R.id.btn_cancel);
        TextView btn_delete = mDialog.findViewById(R.id.btn_delete);
        btn_cancel.setVisibility(View.INVISIBLE);
        btn_delete.setVisibility(View.INVISIBLE);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    //-------------------------------取消預送按鈕 監聽器----------------------------------------
    private static final int FINISH_ACTIVITY = 2;
    private View.OnClickListener planCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(SentPlanMultiActivity.this)
                    .setTitle("是否取消您的預送計畫?")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            planCancelSentAsyncTask planCancelSentAsyncTask = new planCancelSentAsyncTask(new planCancelSentAsyncTask.TaskListener() {
                                @Override
                                public void onFinished(String result) {

                                }
                            });
                            planCancelSentAsyncTask.execute(Common.planCancelSent, sender, planid, "0");
                            //-----回前頁結束製作計畫-----
                            setResult(FINISH_ACTIVITY);
                            finish();
                            Toast.makeText(getApplicationContext(), "已取消預送", Toast.LENGTH_SHORT).show();
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
            final Dialog mDialog = new Dialog(SentPlanMultiActivity.this);
            mDialog.setContentView(R.layout.feedback_check_layout);

            et_feedback  = mDialog.findViewById(R.id.et_feedback);
            btn_ent  = mDialog.findViewById(R.id.btn_ent);

            //et_feedback.setText(feedback);

            //-------------dialog按鈕-------------
            btn_ent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            mDialog.show();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
