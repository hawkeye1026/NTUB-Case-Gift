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
import com.ntubcase.gift.MyAsyncTask.spGiftRecieivedDetailAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ntubcase.gift.MakeGiftsActivity.dateTime;

public class GiftReceivedDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_received_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        setTitle(R.string.giftReceivedDetail);


        //cardview資料傳入---------------------------------
        List<surpriseCardviewGiftItem> giftList = new ArrayList<>();
        giftList.add(new surpriseCardviewGiftItem("白沙屯海灘1"));
        giftList.add(new surpriseCardviewGiftItem("白沙屯海灘2"));
        giftList.add(new surpriseCardviewGiftItem("白沙屯海灘3"));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
// MemberAdapter 會在步驟7建立
        recyclerView.setAdapter(new reSurpriseGiftAdapter(this, giftList));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
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

        Log.v("type",
                type);
        Log.v("planid",
                planid);

        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        final SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        dateTime = sdFormat.format(date);
        Log.v("date",dateTime);

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

                    } catch (Exception e) {
                        Toast.makeText(GiftReceivedDetailActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                    }
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
