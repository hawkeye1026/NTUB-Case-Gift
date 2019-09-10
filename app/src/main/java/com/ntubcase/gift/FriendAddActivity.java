package com.ntubcase.gift;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.friend.friendInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.friend.friendSearchAsyncTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ntubcase.gift.data.getFriendList;


public class FriendAddActivity extends AppCompatActivity {
    private EditText edt_sEmail;
    private ImageView imgFriend;
    private TextView txtFriend;
    private Button btnAdd, btnSearch, btn_scan;
    private LinearLayout find_friend;
    //---
    private String userid="1", friendid;
//    private String userid="1", friendid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);
        setTitle("新增好友");
        //getWindow().getDecorView().setBackgroundResource(R.drawable.bg_friend);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        //-----------------------------------------------------

        edt_sEmail = (EditText) findViewById(R.id.edt_sEmail); //搜尋的email
        btn_scan = (Button) findViewById(R.id.iv_scanner); //QRcode掃描器
        find_friend =(LinearLayout) findViewById(R.id.find_friend);
        imgFriend = (ImageView) findViewById(R.id.img_Friend);//好友照片
        txtFriend = (TextView) findViewById(R.id.txt_Friend);//好友帳號
        btnSearch = (Button) findViewById(R.id.btnSearch);//搜尋按鈕
        btnAdd = (Button) findViewById(R.id.btn_add);//加入按鈕


        //-----搜尋-----
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_sEmail.getText().toString().equals("")){
                    Toast.makeText(FriendAddActivity.this,"輸入好友信箱!", Toast.LENGTH_SHORT).show();
                }else {
                    searchFriend(edt_sEmail.getText().toString()); //搜尋好友
                    //關閉鍵盤
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        //-----QRcode掃描器-----
        btn_scan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                IntentIntegrator integrator = new IntentIntegrator(FriendAddActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("請掃描好友的QRcode");
                integrator.setOrientationLocked(false);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        //-----加入-----
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(friendid == null)){
                    friendInsertAsyncTask friendInsertAsyncTask = new friendInsertAsyncTask(new friendInsertAsyncTask.TaskListener() {
                        @Override
                        public void onFinished(String result) {
                            Toast.makeText(FriendAddActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.v("friendid", friendid);
                    friendInsertAsyncTask.execute(Common.insertFriend , userid, friendid);
                }else {
                    Toast.makeText(FriendAddActivity.this, "無資料", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //----------------------搜尋好友----------------------
    private void searchFriend(final String friendEmail){
        friendSearchAsyncTask friendSearchAsyncTask = new friendSearchAsyncTask(new friendSearchAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(FriendAddActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);

                    //取得禮物紀錄
                    JSONArray jsonArray = object.getJSONArray("result");
                    int resultLength = jsonArray.length();
                    Log.v("resultLength", String.valueOf(resultLength));

                    friendid =jsonArray.getJSONObject(0).getString("userid");
                    String nickname =jsonArray.getJSONObject(0).getString("nickname");
                    String mail =jsonArray.getJSONObject(0).getString("mail");
                    String image =jsonArray.getJSONObject(0).getString("image");

                    //-------圖片網址---------
                    Uri imageURI = Uri.parse(image);
                    if (imageURI!=null && !imageURI.toString().equals("")){
                        Picasso.get().load(imageURI)
                                .transform(new CircleTransform())
                                .into(imgFriend);
                    }

                    txtFriend.setText(nickname);
                    find_friend.setVisibility(View.VISIBLE); //顯示好友資訊區塊

                    //-----檢查好友是否加過-----
                    int i;
                    for (i=0; i<getFriendList.getFriendLength(); i++){
                        if(friendid.equals(getFriendList.getFriendid(i))) break;
                    }
                    if (friendid.equals(userid)) {  //自己
                        btnAdd.setText("您本人");
                        btnAdd.setEnabled(false);
                    }else if (i==getFriendList.getFriendLength()){ //尚未加過
                        btnAdd.setText("加入");
                        btnAdd.setEnabled(true);
                    }else{  //已加過
                        btnAdd.setText("已加入");
                        btnAdd.setEnabled(false);
                    }

                } catch (Exception e) {
                    find_friend.setVisibility(View.INVISIBLE);
                }
            }
        });
        friendSearchAsyncTask.execute(Common.friendQuery , userid, friendEmail);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result!= null) {
            if (result.getContents()==null) {
                Toast.makeText(this, "取消掃描", Toast.LENGTH_SHORT).show();
            }else {
                String contents = data.getStringExtra("SCAN_RESULT");   //取得QR Code內容
                searchFriend("linchengway000@gmail.com"); //***測試資料***
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    //-------------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
