package com.ntubcase.gift;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.writeFeedbackAsyncTask;
import com.ntubcase.gift.login_model.userData;

public class ReceivedListGiftActivity extends AppCompatActivity {

    private String planID, planName, feedback;
    private Button btn_feedback;
    private TextView tv_name;
    private EditText et_feedback;
    private Button btn_can, btn_ent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_list_gift);
        setTitle("收禮細節");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------------------上一頁資料-----------------------------------
        Bundle bundle = getIntent().getExtras();
        planID = bundle.getString("planID");
        planName = bundle.getString("planName");
        feedback = bundle.getString("feedback");

        tv_name = findViewById(R.id.tv_name);
        tv_name.setText(planName);

        //---------------------------------填寫回饋 按鈕---------------------------------------------
        btn_feedback = findViewById(R.id.btn_feedback);
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog mDialog = new Dialog(ReceivedListGiftActivity.this);
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
                    public void onClick(View v) { mDialog.dismiss(); }
                });

                mDialog.show();
            }
        });

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
