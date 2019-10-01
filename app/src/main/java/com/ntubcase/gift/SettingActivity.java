package com.ntubcase.gift;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.Intent;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.zxing.WriterException;
import com.ntubcase.gift.login_model.signOut;
import com.ntubcase.gift.login_model.userData;
import com.squareup.picasso.Picasso;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class SettingActivity extends AppCompatActivity {

    ImageView mUserPhoto;
    TextView mNickname,mMail,mBirthday;
    ImageView mQrcode,mLogout;

    String ownerEmail = userData.getUserMail();

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.mSetting);


        mUserPhoto = (ImageView) findViewById(R.id.iv_pic);
        mNickname = (TextView) findViewById(R.id.et_nickname);
        mMail = (TextView) findViewById(R.id.et_mail);
        mBirthday = (TextView) findViewById(R.id.et_birthday);
        mNickname.setInputType(InputType.TYPE_NULL);
        mMail.setInputType(InputType.TYPE_NULL);
        mBirthday.setInputType(InputType.TYPE_NULL);

        //-------顯示使用者頭像
        Uri imageURI = userData.getUserPhotoUri();
        if (imageURI!=null && !imageURI.toString().equals("")){
            Picasso.get().load(imageURI)
                    .transform(new CircleTransform())
                    .into(mUserPhoto);
        }

        //-------顯示使用者個人資料
        mNickname.setText(userData.getUserName());
        mMail.setText(userData.getUserMail());
        mBirthday.setText(userData.getUserBirthday());


        //------設置
        mLogout = (ImageView)findViewById(R.id.iv_logout);
        mQrcode = (ImageView)findViewById(R.id.iv_qrcode);


        String portal = userData.getLoginPortal();

        if(portal == null){
            portal = "";
        }
        final String finalPortal = portal;

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (finalPortal){
                    case "google":
                        new signOut();
                        break;
                    case "FB":
                        LoginManager.getInstance().logOut();
                        break;
                }

                //---清除儲存的登入資訊---
                mSharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
                mSharedPreferences.edit().clear().commit();

                //---回登入頁---
                Intent intent;
                intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        int smallerDimension = 15 * 15;
        //---------QRcode產生
        QRGEncoder qrgEncoder = new QRGEncoder(ownerEmail, null, QRGContents.Type.TEXT, smallerDimension);
        try {
            // Getting QR-Code as Bitmap
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            mQrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v("qrcode", e.toString());
        }

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
