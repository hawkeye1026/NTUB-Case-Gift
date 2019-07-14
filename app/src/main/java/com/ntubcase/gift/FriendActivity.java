package com.ntubcase.gift;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.FriendListAdapter;
import com.ntubcase.gift.data.getFriendList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Map<String, Object>> mFriendsList; //好友清單

    private FloatingActionButton fab_add;
    private SearchView mSearchView;
    private FriendListAdapter friendListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        setTitle(R.string.mFriend);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //-----------------------------------------------------
        mSearchView = (SearchView) findViewById(R.id.mSearch);
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
/*
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
*/
        getFriendList.getJSON();
        //---------------------ListView倒入資料--------------------------------
        String[][] mFriendsData = new String[getFriendList.getFriendLength()][20];

        Log.e("res_length",getFriendList.getFriendLength()+"");
        for(int i = 0 ;i < getFriendList.getFriendLength(); i++){
            mFriendsData[i][0]= getFriendList.getFriendid(i);
            mFriendsData[i][1]= getFriendList.getFriendName(i);
            mFriendsData[i][2]= getFriendList.getFriendMail(i);
        }

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
        friendListAdapter = new FriendListAdapter(this, mFriendsList);
        mRecyclerView.setAdapter(friendListAdapter);

        setSearch_function(); // 設定searchView的文字輸入監聽
        super.onResume();
    }

    // ----------------設定searchView的文字輸入監聽---------------
    private void setSearch_function(){
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                friendListAdapter.getFilter().filter(query);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                friendListAdapter.getFilter().filter(newText);
                return true;
            }
        });

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    mSearchView.clearFocus();
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
