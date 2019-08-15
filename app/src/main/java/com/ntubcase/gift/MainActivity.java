package com.ntubcase.gift;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.Toast;

import com.ntubcase.gift.data.getFriendList;
import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.data.getGiftReceived;
import com.ntubcase.gift.data.getGiftReceivedDone;
import com.ntubcase.gift.data.getPlanGot;
import com.ntubcase.gift.data.getPlanList;
import com.ntubcase.gift.data.getPlanSent;
import com.ntubcase.gift.data.getPlanningList;
import com.ntubcase.gift.login_model.googleAccount;
import com.ntubcase.gift.login_model.revokeAccess;
import com.ntubcase.gift.login_model.signOut;

public class MainActivity extends AppCompatActivity {

    private ImageView mGift, mPlan, mReceivedGift, mFriend, mSetting;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getGiftList.getJSON();
        getPlanList.getJSON();
        getPlanningList.getJSON();
        getPlanSent.getJSON();
        getPlanGot.getJSON();
        getFriendList.getJSON();
        getGiftReceived.getJSON();
        getGiftReceivedDone.getJSON();

        Toast.makeText(this, "親愛的"+googleAccount.getUserName()+"您好,登入成功", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "親愛的"+googleAccount.getPhotoUrl()+"您好,登入成功", Toast.LENGTH_SHORT).show();

        //-------禮物區 按鈕------
        mGift = findViewById(R.id.mGift);
        mGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , GiftActivity.class);
                startActivity(intent);
            }
        });

        //-------預送計畫 按鈕------
        mPlan = findViewById(R.id.mPlan);
        mPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , PlanActivity.class);
                startActivity(intent);
            }
        });

        //-------收禮區 按鈕------
        mReceivedGift = findViewById(R.id.mReceivedGift);
        mReceivedGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , GiftReceivedActivity.class);
                startActivity(intent);
            }
        });

        //-------我的好友 按鈕------
        mFriend = findViewById(R.id.mFriend);
        mFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , FriendActivity.class);
                startActivity(intent);
            }
        });

        //-------設定 按鈕------
        mSetting = findViewById(R.id.mSetting);
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , SettingActivity.class);
                startActivity(intent);
            }
        });
    }

}
