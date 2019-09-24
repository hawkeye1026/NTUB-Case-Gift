package com.ntubcase.gift;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.plan_list_adapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceivedListActivity extends AppCompatActivity {

    private TextView tv_name, tv_sender, tv_deadline;
    private String sender= "1";

    private RecyclerView recycler_view;
    private plan_list_adapter planListAdapter;
    private List<String> mData = new ArrayList<>();
    private String misPlanid;

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

        //---------------------------------取得收禮詳細-----------------------------------
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null){
            String planid = bundle.getString("planID");
            showPlanDetail(planid);  //顯示收禮詳細資料
        }

        //-----------------------------------------------------------------------
        recycler_view = findViewById(R.id.list_recycle_view);  // 設置RecyclerView為列表型態
        recycler_view.setLayoutManager(new LinearLayoutManager(this));  // 設置格線
        recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        planListAdapter = new plan_list_adapter(mData);  // 將資料交給adapter
        planListAdapter.isFromMake=false;
        recycler_view.setAdapter(planListAdapter); // 設置adapter給recycler_view
        planListAdapter.setOnItemClickListener(new plan_list_adapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                //showDataDialog(position);
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
                        Toast.makeText(ReceivedListActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray;

                    //----------------------------取得計畫資料----------------------------
                    jsonArray = object.getJSONArray("misPlan");
                    misPlanid =jsonArray.getJSONObject(0).getString("misid"); //計畫ID
                    //String misCreateDate = jsonArray.getJSONObject(0).getString("createDate"); //計畫建立日期
                    String misPlanName =jsonArray.getJSONObject(0).getString("misPlanName"); //計畫名稱
                    //String misSendPlanDate = jsonArray.getJSONObject(0).getString("sendPlanDate").substring(0,10); //送禮日期
                    String deadline = jsonArray.getJSONObject(0).getString("deadline"); //截止日期時間

                    tv_name.setText(misPlanName); //計畫名稱
                    tv_deadline.setText(deadline.substring(0,16)); //截止日期時間


                    //----------------------------取得禮物資料----------------------------
                    jsonArray = object.getJSONArray("misList");
                    int misListLength = jsonArray.length();

                    String giftName = "";
                    for (int i = 0 ; i < misListLength ; i++){
                        if (giftName.equals("")) giftName += jsonArray.getJSONObject(i).getString("giftName");
                        else giftName += " , " + jsonArray.getJSONObject(i).getString("giftName");
                    }

                    //----------------------------取得任務項目----------------------------
                    jsonArray = object.getJSONArray("misItem");
                    int misItemLength = jsonArray.length();

                    for (int i = 0 ; i < misItemLength ; i++){
                        String itemNumber = jsonArray.getJSONObject(i).getString("itemNumber"); //項目編號
                        mData.add(jsonArray.getJSONObject(i).getString("content")); //項目內容
                        //String itemCheck = jsonArray.getJSONObject(i).getString("itemCheck"); //打勾項目編號
                    }
                    planListAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Toast.makeText(ReceivedListActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        planDetailAsyncTask.execute(Common.planList , sender, planid);
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
