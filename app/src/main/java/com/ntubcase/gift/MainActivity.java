package com.ntubcase.gift;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.net.Uri;
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
import com.ntubcase.gift.login_model.facebookAccount;
import com.ntubcase.gift.login_model.googleAccount;
import com.ntubcase.gift.login_model.revokeAccess;
import com.ntubcase.gift.login_model.signOut;
import com.ntubcase.gift.login_model.userData;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView mGift, mPlan, mReceivedGift, mFriend, mSetting;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //----------取得禮物、計畫、收禮、好友資料
        getGiftList.getJSON();
        getPlanList.getJSON();
        getPlanningList.getJSON();
        getPlanSent.getJSON();
        getPlanGot.getJSON();
        getFriendList.getJSON();
        getGiftReceived.getJSON();
        getGiftReceivedDone.getJSON();
        //----------
        mSetting = (ImageView)findViewById(R.id.mSetting);

        //-------顯示使用者頭像

        Uri imageURI = userData.getUserPhotoUri();
        Picasso.get().load(imageURI).into(mSetting);



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
