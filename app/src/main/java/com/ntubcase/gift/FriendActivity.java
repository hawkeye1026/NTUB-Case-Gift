package com.ntubcase.gift;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.FriendListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Map<String, Object>> mFriendsList; //好友清單

    private FloatingActionButton fab_add;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        setTitle(R.string.mFriend);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //-----------------------------------------------------
        //mSearchView = (SearchView) findViewById(R.id.mSearch);
        mRecyclerView = (RecyclerView) findViewById(R.id.friendList);

        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FriendActivity.this, "新增好友", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //-----------------
    public void onResume(){

        //---------------------取得好友資料--------------------------------
        String[][] mFriendsData ={      //--------------測試用資料-----------------
                {"0","小明","abcd@gmail.com"},
                {"0","媽媽","mother@gmail.com"},
                {"0","爸爸","fater@gmail.com"},
                {"0","ruby","ruby@gmail.com"},
                {"0","wen","wen@gmail.com"},
                {"0","tzu","tzu@gmail.com"},
                {"0","mmm","mmm@gmail.com"}
        };

        mFriendsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mFriends;

        for(int i=0; i<mFriendsData.length; i++) {
            mFriends = new HashMap<String, Object>();
            mFriends.put("photo", mFriendsData[i][0]);
            mFriends.put("nickname", mFriendsData[i][1]);
            mFriends.put("email", mFriendsData[i][2]);
            mFriendsList.add(mFriends);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        FriendListAdapter friendListAdapter = new FriendListAdapter(this, mFriendsList);
        mRecyclerView.setAdapter(friendListAdapter);

        super.onResume();
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
