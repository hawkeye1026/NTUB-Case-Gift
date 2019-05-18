package com.ntubcase.gift;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.data.getPlanList;

public class loadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //------------------------取得資料
        getGiftList.getJSON();
        //------------------------跳轉頁
        Intent intent = this.getIntent();

        switch (intent.getStringExtra("loadData")){
            case "loadGift":
                getGiftList.getJSON();
                intent = new Intent(loadingActivity.this, GiftActivity.class);

                startActivity(intent);
                break;
            case "sendToPlan":
                getGiftList.getJSON();
                intent = new Intent(loadingActivity.this, PlanActivity.class);

                startActivity(intent);
                break;
            case "loadPlan":
                getPlanList.getJSON();
                intent = new Intent(loadingActivity.this, PlanActivity.class);

                startActivity(intent);
                break;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }
}
