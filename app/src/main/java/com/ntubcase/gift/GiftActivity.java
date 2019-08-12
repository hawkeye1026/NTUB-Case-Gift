package com.ntubcase.gift;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ntubcase.gift.Adapter.GiftListAdapter;
import com.ntubcase.gift.data.getGiftList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private ListView mListView;
    private GiftListAdapter giftListAdapter;
    private List<Map<String, Object>> mGiftsList; //禮物清單
    private FloatingActionButton fabPhoto, fabVideo, fabMessage, fabTicket, fabCode;
    private FloatingActionMenu newGift;
    private Spinner mSpinner;
    private ArrayAdapter spinnerAdapter;
    private static int jslen = 0 ;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        setTitle(R.string.mGift);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------ListView--------------------------------
        mListView = (ListView) findViewById(R.id.giftList);
        mSearchView = (SearchView) findViewById(R.id.mSearch);

        //-----------------------------spinner----------------------
        mSpinner = (Spinner) findViewById(R.id.mSpinner);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_gift_type, R.layout.spinner_layout);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_itm_layout);
        mSpinner.setAdapter(spinnerAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();

                if (str.equals(getString(R.string.giftPhoto))) str="1";
                else if(str.equals(getString(R.string.giftVideo))) str="2";
                else if(str.equals(getString(R.string.giftMessage))) str="3";
                else if(str.equals(getString(R.string.giftTicket))) str="4";
                else if(str.equals(getString(R.string.giftCode))) str="5";

                giftListAdapter.selectedType=str;
                String query = mSearchView.getQuery().toString();
                giftListAdapter.getFilter().filter(query);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //---------------------SwipeRefreshLayout--------------------------------
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                onResume();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //------------------------------FAB_newGift----------------------
        newGift = (FloatingActionMenu) findViewById(R.id.newGift);
        fabPhoto = (FloatingActionButton) findViewById(R.id.fabPhoto);
        fabVideo = (FloatingActionButton) findViewById(R.id.fabVideo);
        fabMessage = (FloatingActionButton) findViewById(R.id.fabMessage);
        fabTicket = (FloatingActionButton) findViewById(R.id.fabTicket);
        fabCode = (FloatingActionButton) findViewById(R.id.fabCode);
        fabPhoto.setOnClickListener(fabClickListener);
        fabVideo.setOnClickListener(fabClickListener);
        fabMessage.setOnClickListener(fabClickListener);
        fabTicket.setOnClickListener(fabClickListener);
        fabCode.setOnClickListener(fabClickListener);
    }

    //-----------------
    public void onResume(){

        getGiftList.getJSON();
        //---------------------ListView倒入資料--------------------------------
        String[][] mGiftsData = new String[getGiftList.getGiftLength()][20];

        Log.e("res_length",getGiftList.getGiftLength()+"");
        for(int i = 0 ;i < getGiftList.getGiftLength(); i++){
            mGiftsData[i][0]= getGiftList.getType(i);
            mGiftsData[i][1]= getGiftList.getGiftName(i);
            mGiftsData[i][2]= getGiftList.getGiftCreateDate(i);
            mGiftsData[i][3]= getGiftList.getGiftid(i);
        }

        mGiftsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mGifts;

        for(int i=0;i<getGiftList.getGiftLength();i++) {
            mGifts = new HashMap<String, Object>();
            mGifts.put("type", mGiftsData[i][0]);
            mGifts.put("title", mGiftsData[i][1]);
            mGifts.put("date", mGiftsData[i][2]);
            mGifts.put("giftid", mGiftsData[i][3]);
            mGiftsList.add(mGifts);
        }
        giftListAdapter = new GiftListAdapter(this, mGiftsList);

        mListView.setAdapter(giftListAdapter);
        mListView.setTextFilterEnabled(true);

        setmListViewListener(); //設定ListView的監聽
        setSearch_function(); // 設定searchView的文字輸入監聽
        super.onResume();
    }


    // ----------------設定ListView的監聽---------------
    private void setmListViewListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent() ;
                Bundle bundle = new Bundle();
                Log.v("test",mGiftsList.get(position).get("type").toString());
                Log.v("test",position+"");

                switch (mGiftsList.get(position).get("type").toString()) {
                    case "1":
                        //------------照片
                        intent = new Intent(GiftActivity.this, MakeGiftImageActivity.class);
                        bundle.putInt("position", position);
                        break;
                    case "2":
                        //------------影片
                        intent = new Intent(GiftActivity.this, MakeGiftVideoActivity.class);
                        bundle.putInt("position", position);
                        break;
                    case "3":
                        //------------悄悄話
                        intent = new Intent(GiftActivity.this, MakeGiftMessageActivity.class);
                        bundle.putInt("position", position);
                        break;
                    case "4":
                        //------------兌換券
                        intent = new Intent(GiftActivity.this, MakeGiftTicketActivity.class);
                        bundle.putInt("position", position);
                        break;
                    case "5":
                        //------------密碼表
                        intent = new Intent(GiftActivity.this, MakeGiftCodeActivity.class);
                        bundle.putInt("position", position);
                        break;
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mSearchView.clearFocus();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    // ----------------設定searchView的文字輸入監聽---------------
    private void setSearch_function(){
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                giftListAdapter.getFilter().filter(query);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                giftListAdapter.getFilter().filter(newText);
                return true;
            }
        });

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    /*InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);*/
                    mSearchView.clearFocus();
                }
            }
        });
    }

    // ----------------設定FAB的點擊監聽---------------
    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            switch (v.getId()) {
                case R.id.fabPhoto:
                    intent = new Intent(GiftActivity.this, MakeGiftImageActivity.class);
                    break;
                case R.id.fabVideo:
                    intent = new Intent(GiftActivity.this, MakeGiftVideoActivity.class);
                    break;
                case R.id.fabMessage:
                    intent = new Intent(GiftActivity.this, MakeGiftMessageActivity.class);
                    break;
                case R.id.fabTicket:
                    intent = new Intent(GiftActivity.this, MakeGiftTicketActivity.class);
                    break;
                case R.id.fabCode:
                    intent = new Intent(GiftActivity.this, MakeGiftCodeActivity.class);
                    break;

            }

            newGift.close(true);
            bundle.putInt("position", -1);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
