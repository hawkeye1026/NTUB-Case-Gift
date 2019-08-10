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
import com.ntubcase.gift.MyAsyncTask.gift.giftDetailAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

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
                intent = new Intent(GiftDetailActivity.this, MakePlanSingleActivity.class);
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
        String giftid = bundle.getString("giftid");

        giftDetailAsyncTask giftDetailAsyncTask = new giftDetailAsyncTask(new giftDetailAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(GiftDetailActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("result");

                    String giftid =jsonArray.getJSONObject(0).getString("giftid");
                    String gift =jsonArray.getJSONObject(0).getString("gift");
                    String giftName =jsonArray.getJSONObject(0).getString("giftName");
                    String giftCreateDate = DateFormat.dateFormat(jsonArray.getJSONObject(0).getString("giftCreateDate"));
                    String ownerid =jsonArray.getJSONObject(0).getString("ownerid");
                    String type =jsonArray.getJSONObject(0).getString("type");
                    Log.v("giftid",
                            giftid);
                    Log.v("giftName",
                            giftName);
                    Log.v("type",
                            type);

                    et_giftName.setText(giftName);
                    et_giftContent.setText(gift);

                } catch (Exception e) {
                    Toast.makeText(GiftDetailActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        giftDetailAsyncTask.execute(Common.giftDetail , giftid);

        super.onResume();
    }

}
