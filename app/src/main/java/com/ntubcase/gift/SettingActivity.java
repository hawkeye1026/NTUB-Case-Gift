package com.ntubcase.gift;

import android.graphics.Bitmap;
import android.net.Uri;
import android.content.Intent;
import android.media.Image;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.zxing.WriterException;
import com.ntubcase.gift.login_model.facebookAccount;
import com.ntubcase.gift.login_model.googleAccount;
import com.ntubcase.gift.login_model.signOut;
import com.ntubcase.gift.login_model.userData;
import com.squareup.picasso.Picasso;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

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
//        Uri imageURI = userData.getUserPhotoUri();
        Uri imageURI = Uri.parse("https://lh3.googleusercontent.com/a-/AAuE7mCRxEJ_jmu1slG-m1RDRJGeLt4ni98tb2mUWT1KfQ");

        Picasso.get().load(imageURI).into(mUserPhoto);
        //-------顯示使用者個人資料
        mNickname.setText("名字： " + userData.getUserName());
        mMail.setText("Email： " + userData.getUserMail());
        mBirthday.setText("Birthday："  );


        //顯示使用者資訊

        //------設置
        mLogout = (Button)findViewById(R.id.iv_logout);
        mQrcode = (ImageView)findViewById(R.id.iv_qrcode);

        String protal = userData.getLoginProtal();

        if(protal == null){
            protal = "";
        }
        final String finalProtal = protal;

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (finalProtal){
                    case "google":
                        new signOut();
                        break;
                    case "FB":
                        LoginManager.getInstance().logOut();
                        break;
                }

                Intent intent;
                intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });


        int smallerDimension = 15 * 15;
        //---------QRcode產生
        QRGEncoder qrgEncoder = new QRGEncoder("http://140.131.114.156/NTUB_gift_server/giftList.php", null, QRGContents.Type.TEXT, smallerDimension);
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
