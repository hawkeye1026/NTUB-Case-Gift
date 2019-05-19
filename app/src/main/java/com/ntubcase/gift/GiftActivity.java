package com.ntubcase.gift;

import android.content.Intent;
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
    private FloatingActionButton fab1, fab2, fab3;
    private FloatingActionMenu newGift;
    private Spinner mSpinner;
    private ArrayAdapter spinnerAdapter;
    private static int jslen = 0 ;

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

                giftListAdapter.selectedType=str;
                String query = mSearchView.getQuery().toString();
                giftListAdapter.getFilter().filter(query);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //------------------------------FAB_newGift----------------------
        newGift = (FloatingActionMenu) findViewById(R.id.newGift);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab1.setOnClickListener(fabClickListener);
        fab2.setOnClickListener(fabClickListener);
        fab3.setOnClickListener(fabClickListener);
    }

    //-----------------
    public void onResume(){
        //---------------------ListView倒入資料--------------------------------
        getGiftList.getJSON();

        String[][] mGiftsData = new String[getGiftList.getGiftLength()][20];

        Log.v("res_length",getGiftList.getGiftLength()+"");
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
                //Toast.makeText(getActivity(), "You Choose "+ ((TextView)view.findViewById(R.id.tv_giftTitle)).getText().toString() , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent = new Intent(GiftActivity.this, GiftDetailActivity.class);
                Bundle bundle = new Bundle();
                //bundle.putString("name",((TextView)view.findViewById(R.id.tv_giftTitle)).getText().toString());
                //bundle.putString("content","禮物內容");
                bundle.putString("giftid",mGiftsList.get(position).get("giftid").toString());
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
            switch (v.getId()) {
                case R.id.fab1:
                    intent = new Intent(GiftActivity.this, MakeGiftsActivity.class);
                    intent.putExtra("giftType", fab1.getLabelText());
                    break;
                case R.id.fab2:
                    intent = new Intent(GiftActivity.this, MakeGiftsActivity.class);
                    intent.putExtra("giftType", fab2.getLabelText());
                    break;
                case R.id.fab3:
                    intent = new Intent(GiftActivity.this, MakeGiftsActivity.class);
                    intent.putExtra("giftType", fab3.getLabelText());
                    break;
            }

            newGift.close(true);
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
