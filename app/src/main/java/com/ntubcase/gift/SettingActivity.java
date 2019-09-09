package com.ntubcase.gift;

import android.app.Activity;
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
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.friend.friendInsertAsyncTask;
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

    ImageView mQrcode,mLogout;
    Button mScanner;

    String ownerid = "1";

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
        Uri imageURI = userData.getUserPhotoUri();
        if (imageURI!=null && !imageURI.toString().equals("")){
            Picasso.get().load(imageURI)
                    .transform(new CircleTransform())
                    .into(mUserPhoto);
        }

        //-------顯示使用者個人資料
        mNickname.setText(userData.getUserName());
        mMail.setText(userData.getUserMail());
        mBirthday.setText("");


        //------設置
        mLogout = (ImageView)findViewById(R.id.iv_logout);

        mQrcode = (ImageView)findViewById(R.id.iv_qrcode);

        mScanner = (Button) findViewById(R.id.iv_scanner);
        final Activity activity = this;
        mScanner.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("請掃描好友的QRcode");
                integrator.setOrientationLocked(false);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

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
        QRGEncoder qrgEncoder = new QRGEncoder(ownerid, null, QRGContents.Type.TEXT, smallerDimension);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result!= null)
        {
            if (result.getContents()==null)
            {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String contents = data.getStringExtra("SCAN_RESULT");   //取得QR Code內容

                friendInsertAsyncTask friendInsertAsyncTask = new friendInsertAsyncTask(new friendInsertAsyncTask.TaskListener() {
                    @Override
                    public void onFinished(String result) {
                        Toast.makeText(SettingActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
                    }
                });
                friendInsertAsyncTask.execute(Common.insertFriend , ownerid, contents);

            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }



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
