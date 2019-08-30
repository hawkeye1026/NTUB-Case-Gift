package com.ntubcase.gift;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendAddActivity extends AppCompatActivity {
    EditText edt_sEmail;
    ImageView imgFriend;
    TextView txtFriend;
    Button btnAdd,btnSearch;
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
