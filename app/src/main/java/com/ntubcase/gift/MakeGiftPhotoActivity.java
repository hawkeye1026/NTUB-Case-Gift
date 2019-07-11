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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.RequestHandler;
import com.ntubcase.gift.MyAsyncTask.giftInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.giftInsertImgAsyncTask;
import com.ntubcase.gift.data.getGiftList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MakeGiftPhotoActivity extends AppCompatActivity {

    private Button btn_save, btn_makePlan, btn_selectImage, btn_openCamera;
    private static EditText et_giftName;
    private ImageView iv_photo;
    private Uri cam_imageUri;
    private Bitmap bitmap;

    private static String giftName, giftContent;

    protected static Date date =new Date();
    protected static String owner = "wayne";
    protected static String dateTime, giftType;
    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_gift_photo);
        setTitle("製作照片");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------------------------------------------------------------------
        et_giftName = (EditText) findViewById(R.id.et_giftName);
        iv_photo =(ImageView) findViewById(R.id.iv_photo);
        btn_save = findViewById(R.id.btn_save);
        btn_makePlan = (Button) findViewById(R.id.btn_makePlan);
        btn_save.setOnClickListener(saveClickListener); //設置監聽器
        btn_makePlan.setOnClickListener(makePlanClickListener); //設置監聽器

        btn_selectImage = (Button) findViewById(R.id.btn_selectImage);
        btn_selectImage.setOnClickListener(selectImageClickListener); //設置監聽器
        btn_openCamera = (Button) findViewById(R.id.btn_openCamera);
        btn_openCamera.setOnClickListener(openCameraClickListener); //設置監聽器
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
                    MakeGiftPhotoActivity.this,
                    getPackageName() + ".fileprovider",
                    outputFile);

            Intent tTntent = new Intent("android.media.action.IMAGE_CAPTURE"); //照相
            tTntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_imageUri); //指定输出地址
            startActivityForResult(tTntent,OPEN_CAMERA); //啟動照相
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case SELECT_IMAGE:
                    decodeUri(data.getData());
                    break;
                case OPEN_CAMERA:
                    decodeUri(cam_imageUri);
                    delDefaultSavePic(); //刪除相機自動儲存的照片
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

            iv_photo.setImageBitmap(bitmap);

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

    //-------------------------------儲存按鈕 監聽器----------------------------------------
    private View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            giftName = et_giftName.getText().toString();    //取得使用者輸入的禮物名稱
            giftContent = "photo";    //取得使用者輸入的禮物內容
            giftType="1";
            //--------取得目前時間：yyyy/MM/dd hh:mm:ss
            Date date =new Date();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            dateTime = sdFormat.format(date);

            uploadImage();

            giftInsertAsyncTask mgiftInsertAsyncTask = new giftInsertAsyncTask(new giftInsertAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            mgiftInsertAsyncTask.execute(Common.insertGiftImg, giftContent, dateTime ,giftName ,owner, giftType);

            //-------------讀取Dialog-----------
            barProgressDialog = ProgressDialog.show(MakeGiftPhotoActivity.this,
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

            Toast.makeText(v.getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
        }
    };

    //-------------------------------製作計畫按鈕 監聽器----------------------------------------
    private View.OnClickListener makePlanClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            giftName = et_giftName.getText().toString();    //取得使用者輸入的禮物名稱
            giftContent = "照片";    //取得使用者輸入的禮物內容
            giftType="1";
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
            intent = new Intent(MakeGiftPhotoActivity.this, PlanActivity.class);
            startActivity(intent);
            finish();
        }
    };

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MakeGiftPhotoActivity.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put("image", uploadImage);
                data.put("name",getFileName(cam_imageUri));

                String result = rh.postRequest(Common.insertGiftImg,data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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
}