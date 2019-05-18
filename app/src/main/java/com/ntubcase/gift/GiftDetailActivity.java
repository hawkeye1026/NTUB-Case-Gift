package com.ntubcase.gift;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.giftDetailAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ntubcase.gift.MakeGiftsActivity.dateTime;

public class GiftDetailActivity extends AppCompatActivity {

    private Button btn_save, btn_makePlan;
    private ImageView iv_giftIcon;
    private EditText et_giftName, et_giftContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);
        setTitle(R.string.giftDetail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //---------------------------------------------------------------------------------
        et_giftName = (EditText) findViewById(R.id.et_giftName);
        et_giftContent = (EditText) findViewById(R.id.et_giftContent);
        btn_save = (Button) findViewById(R.id.btn_save);

        //-----製作計畫按鈕-----
        btn_makePlan = (Button) findViewById(R.id.btn_makePlan);
        btn_makePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = new Intent(GiftDetailActivity.this, MakePlansActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
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

        //-----取得intent的bundle資料-----
        Bundle bundle = this.getIntent().getExtras();
        String giftName = bundle.getString("name");
        String giftContent = bundle.getString("content");
        String giftid = bundle.getString("giftid");
        //et_giftName.setText(giftName);
        //et_giftContent.setText(giftid);

        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        dateTime = sdFormat.format(date);
        Log.v("giftName",dateTime);


        giftDetailAsyncTask giftDetailAsyncTask = new giftDetailAsyncTask(new giftDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(GiftDetailActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 將字串轉為JSONArray, 取出第一個JSON物件.
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObj = jsonArray.getJSONObject(0);

                    String giftid =jsonObj.getString("giftid");
                    String gift =jsonObj.getString("gift");
                    String giftCreateDate =dateFormat.dateFormat(jsonObj.getString("giftCreateDate"));
                    String giftName =jsonObj.getString("giftName");
                    String ownerid =jsonObj.getString("ownerid");
                    String type =jsonObj.getString("type");

                    et_giftName.setText(giftid);
                    et_giftContent.setText(giftName);

                } catch (Exception e) {
                    Toast.makeText(GiftDetailActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.v("giftid",
                giftid);
        giftDetailAsyncTask.execute(Common.giftDetail , giftid);
        //getGiftList.getJSON();

        super.onResume();
    }

}
