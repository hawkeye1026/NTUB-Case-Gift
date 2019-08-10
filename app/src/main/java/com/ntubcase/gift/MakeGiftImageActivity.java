package com.ntubcase.gift;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.giftInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.gift.giftInsertImg_giftAsyncTask;
import com.ntubcase.gift.MyAsyncTask.gift.giftInsertImg_imageAsyncTask;
import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.login_model.googleAccount;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MakeGiftImageActivity extends AppCompatActivity {

    private Button btn_save, btn_makePlan, btn_selectImage, btn_openCamera;
    private static EditText et_giftName;
    private ImageView iv_image;
    private Uri cam_imageUri;

    private static String giftName, giftContent;
    private String filename;
    protected static Date date =new Date();
    protected static String owner = "test";
//    protected static String owner = googleAccount.getUserName()
    protected static String dateTime, giftType;
    ProgressDialog barProgressDialog;

    int currentapiVersion = android.os.Build.VERSION.SDK_INT; //取得目前版本

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_gift_image);
        setTitle("製作照片");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //--------------------------------------------------------------------------------
        et_giftName = (EditText) findViewById(R.id.et_giftName);
        iv_image =(ImageView) findViewById(R.id.iv_image);
        btn_save = findViewById(R.id.btn_save);
        btn_makePlan = (Button) findViewById(R.id.btn_makePlan);
        btn_save.setOnClickListener(saveClickListener); //設置監聽器
        btn_makePlan.setOnClickListener(makePlanClickListener); //設置監聽器

        btn_selectImage = (Button) findViewById(R.id.btn_selectImage);
        btn_selectImage.setOnClickListener(selectImageClickListener); //設置監聽器
        btn_openCamera = (Button) findViewById(R.id.btn_openCamera);
        btn_openCamera.setOnClickListener(openCameraClickListener); //設置監聽器


        //------------禮物詳細，判斷禮物是否有初值
        try{
            Bundle bundle = this.getIntent().getExtras();
            //position 代表第幾個禮物的位置(按照giftActivity的順序排) EX: 第一筆是粽子(position = 0) ，第二筆是湯圓(position = 1)
            int position =Integer.valueOf( bundle.getString("position"));

            //-------圖片網址 getGift(n) 取得第n筆資料的禮物資料
            Uri imageURI = Uri.parse(Common.imgPath + getGiftList.getGift(position));
            Log.v("gift",Common.imgPath + getGiftList.getGift(position));
            Picasso.get().load(imageURI).into(iv_image);
            //-------

            //-------存入禮物詳細的editText
            et_giftName.setText( getGiftList.getGiftName(position));
            //--------

        }catch (Exception e){

        }
        //------------禮物詳細結束

        checkPermission();  //確認權限
    }

    private static final int SELECT_IMAGE = 0; //相簿
    private static final int OPEN_CAMERA = 1; // 相機

    //-------------------------------從相簿選取----------------------------------------
    private View.OnClickListener selectImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
            galleryIntent.setType("image/*"); //圖片
            startActivityForResult(galleryIntent, SELECT_IMAGE);   //跳轉，傳遞打開相冊請求碼
        }
    };

    //-------------------------------從相機拍攝----------------------------------------
    private View.OnClickListener openCameraClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
            Date date = new Date(System.currentTimeMillis());
            String filename = format.format(date);
            File outputFile = new File(Environment.getExternalStorageDirectory() +"/giftPlanner",filename+".jpg");

            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdir();
            }


            cam_imageUri = FileProvider.getUriForFile(
                        MakeGiftImageActivity.this,
                        getPackageName() + ".fileprovider",
                        outputFile);


            if (currentapiVersion < 24) {
                cam_imageUri = Uri.fromFile(outputFile);
            }else{
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, outputFile.getAbsolutePath());
                cam_imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            }


            Intent tTntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //照相
            tTntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_imageUri); //指定输出地址
            startActivityForResult(tTntent,OPEN_CAMERA); //啟動照相
        }
    };

    //-------------------------------儲存按鈕 監聽器----------------------------------------
    private View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            uploadImage();

            //-------------讀取Dialog-----------
            barProgressDialog = ProgressDialog.show(MakeGiftImageActivity.this,
                    "讀取中", "請等待...",true);
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        //uploadFile(imagepath);
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
            //-------------結束Dialog-----------
            Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
        }
    };
    //-------------------------------結束儲存按鈕 監聽器----------------------------------------

    //-------------------------------製作計畫按鈕 監聽器----------------------------------------
    private View.OnClickListener makePlanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //上傳圖片
            uploadImage();

            Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
            Intent intent;
            intent = new Intent(MakeGiftImageActivity.this, PlanActivity.class);
            startActivity(intent);
            finish();
        }
    };
    //-------------------------------結束製作計畫按鈕 監聽器----------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case SELECT_IMAGE:
                    cam_imageUri = data.getData();

                    decodeUri(data.getData());

                    break;
                case OPEN_CAMERA:
                    decodeUri(cam_imageUri);

                    if (currentapiVersion < 24) delDefaultSavePic(); //刪除相機自動儲存的照片
                    break;
            }
        }
    }

    public void decodeUri(Uri uri) {
        ParcelFileDescriptor parcelFD = null;
        try {
            parcelFD = getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);

            iv_image.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            // handle errors
        } catch (IOException e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
    }

    private void delDefaultSavePic(){
        final String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
        final String imageOrderBy = MediaStore.Images.Media._ID+" DESC";
        Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
        if(imageCursor.moveToFirst()){
            int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //imageCursor.close();
            ContentResolver cr = getContentResolver();
            cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media._ID + "=?", new String[]{ Long.toString(id) } );
        }
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
    public void uploadImage(){

        //--------若沒有選擇照片，跳出提醒
        try{
            if(cam_imageUri == null) {
                //顯示提示訊息
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        giftName = et_giftName.getText().toString();    //取得使用者輸入的禮物名稱
        giftContent = getFileName(cam_imageUri);    //取得使用者輸入的禮物內容
        giftType="1";
        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        dateTime = sdFormat.format(date);

        //------------------------------上傳禮物圖片
        giftInsertImg_imageAsyncTask mGiftInsertImgAsyncTask = new giftInsertImg_imageAsyncTask(new giftInsertImg_imageAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        },ImageFilePath.getPath(getApplicationContext(),cam_imageUri));
        mGiftInsertImgAsyncTask.execute(Common.insertGiftImg_image, String.valueOf(cam_imageUri),giftContent);

        //------------------------------上傳禮物資料
        giftInsertImg_giftAsyncTask mgiftInsertAsyncTask = new giftInsertImg_giftAsyncTask(new giftInsertImg_giftAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        mgiftInsertAsyncTask.execute(Common.insertGift, giftContent, dateTime ,giftName ,owner, giftType);

    }

}