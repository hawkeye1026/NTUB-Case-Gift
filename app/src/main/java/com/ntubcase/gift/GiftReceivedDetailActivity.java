package com.ntubcase.gift;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;

import com.ntubcase.gift.Adapter.reSurpriseGiftAdapter;

import java.util.ArrayList;
import java.util.List;

public class GiftReceivedDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_received_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建
        setTitle(R.string.giftReceivedDetail);

        List<surpriseCardviewGiftItem> giftList = new ArrayList<>();
        giftList.add(new surpriseCardviewGiftItem("白沙屯海灘1"));
        giftList.add(new surpriseCardviewGiftItem("白沙屯海灘2"));
        giftList.add(new surpriseCardviewGiftItem("白沙屯海灘3"));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
// MemberAdapter 會在步驟7建立
        recyclerView.setAdapter(new reSurpriseGiftAdapter(this, giftList));

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
