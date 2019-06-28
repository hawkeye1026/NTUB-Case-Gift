package com.ntubcase.gift;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.giftInsertAsyncTask;
import com.ntubcase.gift.data.getGiftList;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MakeGiftVideoActivity extends AppCompatActivity  implements MediaPlayer.OnPreparedListener {

    private Button btn_save, btn_makePlan, btn_selectVideo, btn_openCamera;
    private static EditText et_giftName;
    private VideoView vv_content;
    private MediaController mc;
    private Uri cam_videoUri;

    private static String giftName, giftContent;

    protected static Date date =new Date();
    protected static String owner = "wayne";
    protected static String dateTime, giftType;
    ProgressDialog barProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_gift_video);
        setTitle("製作影片");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------------------------------------------------------------------
        et_giftName = (EditText) findViewById(R.id.et_giftName);
        btn_save = findViewById(R.id.btn_save);
        btn_makePlan = (Button) findViewById(R.id.btn_makePlan);
        btn_save.setOnClickListener(saveClickListener); //設置監聽器
        btn_makePlan.setOnClickListener(makePlanClickListener); //設置監聽器

        btn_selectVideo = (Button) findViewById(R.id.btn_selectVideo);
        btn_selectVideo.setOnClickListener(selectVideoClickListener); //設置監聽器
        btn_openCamera = (Button) findViewById(R.id.btn_openCamera);
        btn_openCamera.setOnClickListener(openCameraClickListener); //設置監聽器

        vv_content = (VideoView) findViewById(R.id.vv_content);
        mc = new MediaController(this); // 設定影片控制台
        vv_content.setMediaController(mc);
        vv_content.setOnPreparedListener(this); // 呼叫 VideoView.setVideoURI() 後觸發

        checkPermission();  //確認權限
    }

    private static final int SELECT_VIDEO = 0; //相簿
    private static final int OPEN_CAMERA = 1; // 相機

    //-------------------------------從相簿選取----------------------------------------
    private View.OnClickListener selectVideoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
            galleryIntent.setType("video/*"); //圖片
            startActivityForResult(galleryIntent, SELECT_VIDEO);   //跳轉，傳遞打開相冊請求碼
        }
    };

    //-------------------------------從相機拍攝----------------------------------------
    private View.OnClickListener openCameraClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date(System.currentTimeMillis());
            String filename = format.format(date);
            File outputFile = new File(Environment.getExternalStorageDirectory() +"/giftPlanner",filename+".mp4");
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdir();
            }

            cam_videoUri = FileProvider.getUriForFile(
                    MakeGiftVideoActivity.this,
                    getPackageName() + ".fileprovider",
                    outputFile);

            Intent tTntent = new Intent("android.media.action.VIDEO_CAPTURE"); //錄影
            tTntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_videoUri); //指定输出地址
            startActivityForResult(tTntent,OPEN_CAMERA); //啟動照相

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case SELECT_VIDEO:
                    Uri uri = data.getData();
                    vv_content.setVideoURI(uri);
                    break;
                case OPEN_CAMERA:
                    vv_content.setVideoURI(cam_videoUri);
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        vv_content.start();
    }

    //-------------------------------儲存按鈕 監聽器----------------------------------------
    private View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            giftName = et_giftName.getText().toString();    //取得使用者輸入的禮物名稱
            giftContent = "影片";    //取得使用者輸入的禮物內容
            giftType="2";
            //--------取得目前時間：yyyy/MM/dd hh:mm:ss
            Date date =new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            dateTime = sdFormat.format(date);


            giftInsertAsyncTask mgiftInsertAsyncTask = new giftInsertAsyncTask(new giftInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            mgiftInsertAsyncTask.execute(Common.insertGift , giftContent, dateTime ,giftName ,owner, giftType);

            //-------------讀取Dialog-----------
            barProgressDialog = ProgressDialog.show(MakeGiftVideoActivity.this,
                    "讀取中", "請等待...",true);
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        getGiftList.getJSON();
                        Thread.sleep(1000);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    finally{
                        barProgressDialog.dismiss();
                        finish();
                    }
                }
            }).start();

            Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
        }
    };

    //-------------------------------製作計畫按鈕 監聽器----------------------------------------
    private View.OnClickListener makePlanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            giftName = et_giftName.getText().toString();    //取得使用者輸入的禮物名稱
            giftContent = "影片";    //取得使用者輸入的禮物內容
            giftType="2";
            //--------取得目前時間：yyyy/MM/dd hh:mm:ss
            Date date =new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            dateTime = sdFormat.format(date);


            giftInsertAsyncTask mgiftInsertAsyncTask = new giftInsertAsyncTask(new giftInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            mgiftInsertAsyncTask.execute(Common.insertGift , giftContent, dateTime ,giftName ,owner,giftType);

            Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
            Intent intent;
            intent = new Intent(MakeGiftVideoActivity.this, PlanActivity.class);
            startActivity(intent);
            finish();
        }
    };

    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); //網路
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    //---------------------確認權限-----------------------
    private void checkPermission(){
        //檢查是否取得權限
        int checkCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int checkStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //沒有權限時
        if (checkCamera != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},1);
        }
        if(checkStorage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }
}