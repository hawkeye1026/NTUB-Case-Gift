package com.ntubcase.gift;

import android.net.Uri;
import android.content.Intent;
import android.media.Image;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntubcase.gift.login_model.googleAccount;
import com.ntubcase.gift.login_model.signOut;
import com.squareup.picasso.Picasso;

public class SettingActivity extends AppCompatActivity {

    ImageView mUserPhoto;
    TextView mNickname,mMail,mBirthday;

    Button mLogout;
    ImageView mQrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.mSetting);


        mUserPhoto = (ImageView) findViewById(R.id.iv_pic);
        mNickname = (TextView) findViewById(R.id.et_nickname);
        mMail = (TextView) findViewById(R.id.et_mail);
        mBirthday = (TextView) findViewById(R.id.et_birthday);

        //-------顯示使用者頭像
        Uri imageURI = googleAccount.getPhotoUri();
        Picasso.get().load(imageURI).into(mUserPhoto);
        //-------顯示使用者個人資料
        mNickname.setText("名字： " + googleAccount.getUserName());
        mMail.setText("Email： " + googleAccount.getUserMail());
        mBirthday.setText("Birthday："  );
        //顯示使用者資訊

        //------設置
        mLogout = (Button)findViewById(R.id.iv_logout);
        mQrcode = (ImageView)findViewById(R.id.iv_qrcode);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new signOut();
                Intent intent;
                intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //------------------------------------------------------------------------------------------

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
