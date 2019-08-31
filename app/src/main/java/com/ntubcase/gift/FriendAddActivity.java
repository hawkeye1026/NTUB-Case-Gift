package com.ntubcase.gift;

import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.friend.friendSearchAsyncTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ntubcase.gift.MakePlanSingleActivity.edt_single_friend;

public class FriendAddActivity extends AppCompatActivity {
    EditText edt_sEmail;
    ImageView imgFriend;
    TextView txtFriend;
    Button btnAdd,btnSearch;

    //---
    private String userid="1";

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
        imgFriend = (ImageView) findViewById(R.id.img_Friend);//好友照片
        txtFriend = (TextView) findViewById(R.id.txt_Friend);//好友帳號
        btnSearch = (Button) findViewById(R.id.btnSearch);//加入按鈕
        btnAdd = (Button) findViewById(R.id.btnAdd);//加入按鈕

        //-----製作計畫按鈕-----
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                            String nickname =jsonArray.getJSONObject(0).getString("nickname");
                            String mail =jsonArray.getJSONObject(0).getString("mail");
                            String image =jsonArray.getJSONObject(0).getString("image");

                            //-------圖片網址 getGift(n) 取得第n筆資料的禮物資料
                            Uri imageURI = Uri.parse(image);
                            if (imageURI!=null){
                                Picasso.get().load(imageURI)
                                        .transform(new CircleTransform())
                                        .into(imgFriend);
                            }

                            txtFriend.setText(nickname);

                        } catch (Exception e) {
                            Toast.makeText(FriendAddActivity.this, "連線失敗!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Log.v("mail", edt_sEmail.getText().toString());
                friendSearchAsyncTask.execute(Common.friendQuery , userid, edt_sEmail.getText().toString());

            }
        });
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
