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
        getPlanList.getJSON();


        finish();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }
}
