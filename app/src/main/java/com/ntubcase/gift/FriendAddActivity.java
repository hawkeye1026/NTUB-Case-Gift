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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.friend.friendInsertAsyncTask;
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
    LinearLayout find_friend;

    //---
    private String userid="1", friendid;

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
        btnSearch = (Button) findViewById(R.id.btnSearch);//搜尋按鈕
        btnAdd = (Button) findViewById(R.id.btn_add);//加入按鈕
        find_friend =(LinearLayout) findViewById(R.id.find_friend);

        //--搜尋
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_sEmail.getText().toString().equals("")){
                    Toast.makeText(FriendAddActivity.this,"輸入好友信箱!", Toast.LENGTH_SHORT).show();
                }else {

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

                                //-------圖片網址 getGift(n) 取得第n筆資料的禮物資料
                                Uri imageURI = Uri.parse(image);
                                if (imageURI!=null && !imageURI.toString().equals("")){
                                    Picasso.get().load(imageURI)
                                            .transform(new CircleTransform())
                                            .into(imgFriend);
                                }

                                txtFriend.setText(nickname);
                                find_friend.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                Toast.makeText(FriendAddActivity.this, "沒找到此email的使用者!", Toast.LENGTH_SHORT).show();
                                find_friend.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                    Log.v("mail", edt_sEmail.getText().toString());
                    friendSearchAsyncTask.execute(Common.friendQuery , userid, edt_sEmail.getText().toString());

                }
            }
        });

        //--加入
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
