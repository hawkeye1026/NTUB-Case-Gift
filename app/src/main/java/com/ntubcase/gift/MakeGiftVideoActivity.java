package com.ntubcase.gift;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.insert.giftInsertVid_viedoAsyncTask;
import com.ntubcase.gift.checkPackage.checkGiftid;
import com.ntubcase.gift.checkPackage.checkRepeatGift;
import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.login_model.userData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MakeGiftVideoActivity extends AppCompatActivity  implements MediaPlayer.OnPreparedListener {

    private Button btn_save, btn_directly_send, btn_selectVideo, btn_openCamera;
    private static EditText et_giftName;
    private VideoView vv_content;
    private MediaController mc;
    private Uri cam_videoUri;

    private static String giftName, giftContent;

    protected static Date date =new Date();
    protected static String owner = userData.getUserID();
    protected static String dateTime, giftType = "2";
    ProgressDialog barProgressDialog;

    int currentapiVersion = android.os.Build.VERSION.SDK_INT; //取得目前版本

    private static int giftid;
    private static String old_giftContent = "";
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
        btn_directly_send = (Button) findViewById(R.id.btn_directly_send);
        btn_save.setOnClickListener(saveClickListener); //設置監聽器
        btn_directly_send.setOnClickListener(directlySendClickListener); //設置監聽器

        btn_selectVideo = (Button) findViewById(R.id.btn_selectVideo);
        btn_selectVideo.setOnClickListener(selectVideoClickListener); //設置監聽器
        btn_openCamera = (Button) findViewById(R.id.btn_openCamera);
        btn_openCamera.setOnClickListener(openCameraClickListener); //設置監聽器

        vv_content = (VideoView) findViewById(R.id.vv_content);
        mc = new MediaController(this); // 設定影片控制台
        vv_content.setMediaController(mc);
        vv_content.setOnPreparedListener(this); // 呼叫 VideoView.setVideoURI() 後觸發

        giftid = 0;
        //-----判斷是否為修改
        Bundle bundle = this.getIntent().getExtras();
        //position 代表第幾個禮物的位置(按照giftActivity的順序排) EX: 第一筆是粽子(position = 0) ，第二筆是湯圓(position = 1)
        int position ;
        giftid =bundle.getInt("giftid");
        position = checkGiftid.checkGiftid(giftid);

        if (position>=0){
            //-------圖片網址 getGift(n) 取得第n筆資料的禮物資料
            Uri videoURI = Uri.parse(Common.vidPath + getGiftList.getGift(position));
            old_giftContent = getGiftList.getGift(position).replace(owner+"/","");

            cam_videoUri = videoURI;

            vv_content.setVideoURI(videoURI);

            //-------set該禮物名稱
            et_giftName.setText( getGiftList.getGiftName(position));
            //--------
        }

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
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
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

            if (currentapiVersion < 24) {
                cam_videoUri = Uri.fromFile(outputFile);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, outputFile.getAbsolutePath());
                cam_videoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            }

            Intent tTntent = new Intent("android.media.action.VIDEO_CAPTURE"); //錄影
            tTntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_videoUri); //指定输出地址
            startActivityForResult(tTntent,OPEN_CAMERA); //啟動照相

        }
    };

    //-------------------取得回傳的資料---------------------
    private static final int FINISH_ACTIVITY = 22;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_VIDEO:
                    cam_videoUri = data.getData();
                    Uri uri = data.getData();
                    vv_content.setVideoURI(uri);
                    break;
                case OPEN_CAMERA:
                    vv_content.setVideoURI(cam_videoUri);
                    break;
                case REQUEST_CODE:
                    break;
            }
        }else if (resultCode==FINISH_ACTIVITY){
            finish();  //結束製作禮物
        }
        else {
            return;
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
            if ( et_giftName.getText().toString().trim().equals("")){ //檢查是否有輸入禮物名稱
                Toast.makeText(v.getContext(), "請輸入禮物名稱!", Toast.LENGTH_SHORT).show();
            }else{
                if(uploadViedo(v))finish();

            }
        }
    };

    //-------------------------------直接送禮按鈕 監聽器----------------------------------------
    private static final int REQUEST_CODE=11;
    private View.OnClickListener directlySendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ( et_giftName.getText().toString().trim().equals("")){ //檢查是否有輸入禮物名稱
                Toast.makeText(v.getContext(), "請輸入禮物名稱!", Toast.LENGTH_SHORT).show();
            }else{
                uploadViedo(v);
                finish();
            }


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

    public boolean uploadViedo(View v) {
        giftName = et_giftName.getText().toString().trim();    //取得使用者輸入的禮物名稱
        //--------若沒有選擇照片，跳出提醒
        try{
            if(cam_videoUri == null) {
                //顯示提示訊息
                Toast.makeText(v.getContext(), "儲存失敗，請選擇一個影片！", Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        giftName = et_giftName.getText().toString().trim();    //取得使用者輸入的禮物名稱

        giftContent = getFileName(cam_videoUri);    //取得使用者輸入的禮物內容
        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        dateTime = sdFormat.format(date);

        if(giftid > 0) {
            //------------------------------上傳禮物資料
            Log.v("upload", giftid + "");
            new updateGift(String.valueOf(giftid), userData.getUserID()+"/" + giftContent, giftName, owner, giftType);
            new uploadGiftFile(getApplicationContext(), cam_videoUri, giftContent, old_giftContent, owner, "vid", "update");
        }else {
            if(checkRepeatGift.checkRepeatGift(giftName)) {
                //------------------------------上傳禮物資料
                Log.v("upload", giftid + "");
                new uploadGift(userData.getUserID()+"/"  + giftContent, giftName, owner, giftType);
                new uploadGiftFile(getApplicationContext(), cam_videoUri, giftContent, old_giftContent, owner, "vid", "insert");
            }else{
                Toast.makeText(v.getContext(), "儲存失敗，禮物名稱重複囉", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        //-------------讀取Dialog-----------
        barProgressDialog = ProgressDialog.show(MakeGiftVideoActivity.this,
                "讀取中", "請等待...", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getGiftList.getJSON();
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    barProgressDialog.dismiss();
                    finish();
                }
            }
        }).start();
        return true;
    }

    String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}