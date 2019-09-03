package com.ntubcase.gift;

import android.annotation.SuppressLint;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.plan_list_adapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.data.getGiftList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SentPlanListActivity extends AppCompatActivity{

    //----------------------------------------------------------------------------------------------
    static EditText edt_list_name, edt_list_message, edt_list_lastDate,edt_list_friend, edt_list_giftName, edt_list_sentDate,edt_list_lastTime;
    private String sender= "1";

    //----------------------------------------------------------------------------------------------
    ProgressDialog barProgressDialog;
    private RecyclerView recycler_view;
    private plan_list_adapter adapter;
    private List<String> mData = new ArrayList<>();

    private Button btn_save, btn_cancel, btnAdd, btnEnt, btnCan ;
    String list_message;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan_list);
        setTitle(R.string.planList);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //---------------------------------------------------------------------------------

        edt_list_name = findViewById(R.id.list_name);
        edt_list_sentDate = findViewById(R.id.list_time);
        edt_list_friend = findViewById(R.id.list_friend);
        edt_list_giftName = findViewById(R.id.list_gift);
        edt_list_lastDate = findViewById(R.id.list_lastDate);
        edt_list_lastTime= findViewById(R.id.list_lastTime);
        edt_list_name.setInputType(InputType.TYPE_NULL);
        edt_list_sentDate.setInputType(InputType.TYPE_NULL);
        edt_list_friend.setInputType(InputType.TYPE_NULL);
        edt_list_giftName.setInputType(InputType.TYPE_NULL);
        edt_list_lastDate.setInputType(InputType.TYPE_NULL);
        edt_list_name.setInputType(InputType.TYPE_NULL);
        btnAdd = (Button) findViewById(R.id.btnAdd); //新增按鈕
        btnAdd.setVisibility(View.INVISIBLE);

        //-----------------------------------------------------------------------
        recycler_view = findViewById(R.id.list_recycle_view);  // 設置RecyclerView為列表型態
        recycler_view.setLayoutManager(new LinearLayoutManager(this));  // 設置格線
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new plan_list_adapter(mData);  // 將資料交給adapter
        recycler_view.setAdapter(adapter); // 設置adapter給recycler_view
        adapter.setOnItemClickListener(new plan_list_adapter.OnItemClickListener(){
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
        btn_cancel = findViewById(R.id.btn_plan_cancel); //取消預送按鈕
        String from = bundle.getString("from");
        if (from.equals("PlanSentFragment")){
            btn_cancel.setVisibility(View.VISIBLE);
            btn_cancel.setOnClickListener(planCancelClickListener);
        }

    }

    //-----------------------------------------------顯示一筆禮物的詳細----------------------------------------------
    private void showDataDialog(final int position) {
        final Dialog dialog = new Dialog(SentPlanListActivity.this);
        dialog.setContentView(R.layout.list_dialog);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);//讓使用者點選背景或上一頁沒有用

        //----------------------------------------------------------------------------------------------
        edt_list_message = dialog.findViewById(R.id.list_message);
        edt_list_message.setText(mData.get(position));
        edt_list_message.setInputType(InputType.TYPE_NULL);

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
            new AlertDialog.Builder(SentPlanListActivity.this)
                    .setTitle("是否取消您的預送計畫?")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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

    //------------------------------計畫詳細，顯示plan資料------------------------------
    private void showPlanDetail(String planid){
        planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(SentPlanListActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray;

                    //----------------------------取得計畫資料----------------------------
                    jsonArray = object.getJSONArray("misPlan");
                    //String misPlanid =jsonArray.getJSONObject(0).getString("misid"); //計畫ID
                    //String misCreateDate = jsonArray.getJSONObject(0).getString("createDate"); //計畫建立日期
                    String misPlanName =jsonArray.getJSONObject(0).getString("misPlanName"); //計畫名稱
                    String misSendPlanDate = jsonArray.getJSONObject(0).getString("sendPlanDate").substring(0,10); //送禮日期
                    String deadline = jsonArray.getJSONObject(0).getString("deadline"); //截止日期時間
                    String lastDate = deadline.substring(0,10); //截止日期
                    String lastTime = deadline.substring(11,16); //截止時間

                    edt_list_name.setText(misPlanName); //計畫名稱
                    edt_list_sentDate.setText(misSendPlanDate); //送禮日期
                    edt_list_lastDate.setText(lastDate); //截止日期
                    edt_list_lastTime.setText(lastTime); //截止時間

                    //----------------------------取得好友資料----------------------------
                    jsonArray = object.getJSONArray("record");
                    int friendsLength = jsonArray.length();;

                    String friendName = "";
                    for (int i = 0; i < friendsLength; i++) {
                        if (friendName.equals("")) friendName += jsonArray.getJSONObject(i).getString("nickname");
                        else friendName += " , " + jsonArray.getJSONObject(i).getString("nickname");
                    }
                    edt_list_friend.setText(friendName); //好友名稱

                    //----------------------------取得禮物資料----------------------------
                    jsonArray = object.getJSONArray("misList");
                    int misListLength = jsonArray.length();

                    String giftName = "";
                    for (int i = 0 ; i < misListLength ; i++){
                        if (giftName.equals("")) giftName += jsonArray.getJSONObject(i).getString("giftName");
                        else giftName += " , " + jsonArray.getJSONObject(i).getString("giftName");
                    }
                    edt_list_giftName.setText(giftName);

                    //----------------------------取得任務項目----------------------------
                    jsonArray = object.getJSONArray("misItem");
                    int misItemLength = jsonArray.length();

                    for (int i = 0 ; i < misItemLength ; i++){
                        mData.add(jsonArray.getJSONObject(i).getString("content")); //項目內容
                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(SentPlanListActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.planList , sender, planid);
    }

    //-----------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

