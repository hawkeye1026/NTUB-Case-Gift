package com.ntubcase.gift;

import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntubcase.gift.login_model.googleAccount;
import com.squareup.picasso.Picasso;

public class SettingActivity extends AppCompatActivity {

    ImageView mUserPhoto;
    TextView mNickname,mMail,mBirthday;

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
        Uri imageURI = googleAccount.getPhotoUrl();
        Picasso.get().load(imageURI).into(mUserPhoto);
        //-------顯示使用者個人資料
        mNickname.setText("名字： " + googleAccount.getUserName());
        mMail.setText("Email： " + googleAccount.getUserMail());
        mBirthday.setText("Birthday："  );
        //顯示使用者資訊

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
