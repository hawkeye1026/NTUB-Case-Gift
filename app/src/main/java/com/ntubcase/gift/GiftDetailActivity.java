package com.ntubcase.gift;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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


        //-----取得intent的bundle資料-----
        Bundle bundle = this.getIntent().getExtras();
        String giftName = bundle.getString("name");
        String giftContent = bundle.getString("content");
        et_giftName.setText(giftName);
        et_giftContent.setText(giftContent);


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
}
