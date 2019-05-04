package com.ntubcase.gift;

import android.content.Intent;
import android.content.res.Resources;
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

import java.util.ArrayList;

public class MakeGiftsActivity extends AppCompatActivity {

    private Button btn_save, btn_makePlan;
    private ImageView iv_giftIcon;
    private EditText et_giftName, et_giftContent;
    private String giftName, giftContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_gifts);
        setTitle("製作禮物");

        Intent intent = this.getIntent();
        String giftType = intent.getStringExtra("giftType"); //取得選擇的禮物種類

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //------------------------設定icon-------------------------------
        Resources res = getResources();
        String[] mGiftStrings = res.getStringArray(R.array.gift_type);

        iv_giftIcon = (ImageView) findViewById(R.id.iv_giftIcon);
        if(giftType.equals(mGiftStrings[0])){
            iv_giftIcon.setImageResource(R.drawable.ic_gift_camera);
        }else if(giftType.equals(mGiftStrings[1])){
            iv_giftIcon.setImageResource(R.drawable.ic_gift_video);
        }else if(giftType.equals(mGiftStrings[2])){
            iv_giftIcon.setImageResource(R.drawable.ic_gift_ticket);
        }

        //---------------------------------------------------------------------------------
        btn_save = findViewById(R.id.btn_save);
        btn_makePlan = (Button) findViewById(R.id.btn_makePlan);
        et_giftName = (EditText) findViewById(R.id.et_giftName);
        et_giftContent = (EditText) findViewById(R.id.et_giftContent);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giftName = et_giftName.getText().toString();    //取得使用者輸入的禮物名稱
                giftContent = et_giftContent.getText().toString();    //取得使用者輸入的禮物內容

                Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btn_makePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
