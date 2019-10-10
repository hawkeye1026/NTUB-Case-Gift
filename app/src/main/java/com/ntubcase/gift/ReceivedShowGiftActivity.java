package com.ntubcase.gift;

import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.writeFeedbackAsyncTask;
import com.ntubcase.gift.login_model.userData;

import java.util.List;

public class ReceivedShowGiftActivity extends AppCompatActivity {

    private List<String> giftContent, giftType;
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_list_gift);
        setTitle("收禮細節");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------------------上一頁資料-----------------------------------
        Bundle bundle = getIntent().getExtras();
        giftContent = (List<String>)bundle.getSerializable("giftContent");
        giftType = (List<String>)bundle.getSerializable("giftType");

        Log.e("***",String.valueOf(giftContent)+" ; "+String.valueOf(giftType));

        tv_name = findViewById(R.id.tv_name);
        tv_name.setText("計畫名稱?");
    }

    //------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar返回建
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
