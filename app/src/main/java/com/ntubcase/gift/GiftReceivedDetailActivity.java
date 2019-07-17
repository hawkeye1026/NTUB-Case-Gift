package com.ntubcase.gift;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.reSurpriseGiftAdapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.spGiftRecieivedDetailAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GiftReceivedDetailActivity extends AppCompatActivity {
    //---
    ArrayList gifts; //禮物清單

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_received_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        setTitle(R.string.giftReceivedDetail);

//        final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollview_gift_rec));
//        scrollview.post(new Runnable() {
//            @Override
//            public void run() {
//                scrollview.fullScroll(ScrollView.FOCUS_UP);
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showData(){
        //cardview資料傳入---------------------------------
        List<surpriseCardviewGiftItem> giftList = new ArrayList<>();

        for(int i = 0 ;i < gifts.size(); i++){
            giftList.add(new surpriseCardviewGiftItem(gifts.get(i).toString()));
            Log.v("giftName", (gifts.get(i).toString()));
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
// MemberAdapter 會在步驟7建立
        recyclerView.setAdapter(new reSurpriseGiftAdapter(this, giftList));
    }

    //-----------------
    public void onResume(){
        //變數宣告------------------------
        final EditText surprice_sender =(EditText) findViewById(R.id.surprice_sender);
        final EditText surprice_planName =(EditText) findViewById(R.id.surprice_planName);
        final EditText surprice_message =(EditText) findViewById(R.id.surprice_message);
        //-----取得intent的bundle資料-----
        Bundle bundle = this.getIntent().getExtras();
        //String sender = bundle.getString("sender");
        //String title = bundle.getString("title");
        //surprice_sender.setText(sender);
        //surprice_planName.setText(title);
        String type = bundle.getString("type");
        String planid = bundle.getString("planid");

        Log.v("type", type);
        Log.v("planid", planid);


        if(type.equals("驚喜式")){
            final spGiftRecieivedDetailAsyncTask spGiftRecieivedDetailAsyncTask = new spGiftRecieivedDetailAsyncTask(new spGiftRecieivedDetailAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {
                    try {
                        if (result == null) {
                            Toast.makeText(GiftReceivedDetailActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject object = new JSONObject(result);
                        JSONArray jsonArray = object.getJSONArray("result");
                        Log.v("result",result);

                        String senderid =jsonArray.getJSONObject(0).getString("senderid");
                        String nickname =jsonArray.getJSONObject(0).getString("nickname");
                        String spid =jsonArray.getJSONObject(0).getString("spid");
                        String spPlanName =jsonArray.getJSONObject(0).getString("spPlanName");
                        String message =jsonArray.getJSONObject(0).getString("message");

                        Log.v("sender",senderid+", "+nickname);
                        Log.v("spPlanName",spid+", "+spPlanName);

                        surprice_sender.setText(nickname);
                        surprice_planName.setText(spPlanName);
                        surprice_message.setText(message);

                        //---adapter data---
                        gifts = new ArrayList();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            String giftid =jsonArray.getJSONObject(i).getString("giftid");
                            String giftName =jsonArray.getJSONObject(i).getString("giftName");
                            String type =jsonArray.getJSONObject(i).getString("type");
                            gifts.add(giftName);
                        }

                    } catch (Exception e) {
                        Toast.makeText(GiftReceivedDetailActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                    }

                    //---
                    showData();
                }
            });
            spGiftRecieivedDetailAsyncTask.execute(Common.spGiftReceivedDetail , planid);
        }

        if(type.equals("期間式")){

        }

        if(type.equals("問答式")){

        }

        super.onResume();
    }
}
