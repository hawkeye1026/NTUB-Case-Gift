package com.ntubcase.gift;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.GiftReceivedAdapter;
//import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.data.getGiftReceived;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftReceivedNewFragment extends Fragment {
    Context context;

    private SearchView mSearchView;
    private ListView mListView;
    private GiftReceivedAdapter giftReceivedAdapter;
    private List<Map<String, Object>> rGiftsList; //禮物清單
    private Spinner mSpinner;
    private ArrayAdapter spinnerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_received_new, container, false);

        //------------------ListView------------------
        mListView = (ListView) view.findViewById(R.id.giftList);
        mSearchView = (SearchView) view.findViewById(R.id.mSearch);

        rGiftsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> rGifts;

        //-----
        String[][] rGiftsData = new String[getGiftReceived.getGiftLength()][20];

        //------------範例資料格式(計畫種類,計畫名稱,送禮人,日期)----------
/*
        String[][] rGiftsData = {       //禮物清單內容
                {"驚喜式","生日賀卡","林同學","2019-02-02"},
                {"驚喜式","結婚紀念照","老婆","2019-02-03"},
                {"問答式","考考你","好友1","2019-02-06"},
                {"期間式","健身計畫","陳同事","2019-02-04"},
                {"驚喜式","按摩兌換券","兒子","2019-02-05"},
                {"驚喜式","禮物3","好友2","2019-02-07"}
        };

        getGiftReceived.getJSON();

        for(int i = 0; i < getGiftReceived.getGiftLength(); i++){
            rGiftsData[i][0]= getGiftReceived.getType(i);
            rGiftsData[i][1]= getGiftReceived.getPlanName(i);
            rGiftsData[i][2]= getGiftReceived.getNickname(i);
            rGiftsData[i][3]= getGiftReceived.getSendPlanDate(i);
        }

        for(int i=0;i<rGiftsData.length;i++) {
            rGifts = new HashMap<String, Object>();
            rGifts.put("type", rGiftsData[i][0]);
            rGifts.put("title", rGiftsData[i][1]);
            rGifts.put("sender", rGiftsData[i][2]);
            rGifts.put("date", rGiftsData[i][3]);
            rGiftsList.add(rGifts);
        }

        giftReceivedAdapter = new GiftReceivedAdapter(getActivity(), rGiftsList);

        mListView.setAdapter(giftReceivedAdapter);
        mListView.setTextFilterEnabled(true);

        setmListViewListener(); //設定ListView的監聽
        setSearch_function(); // 設定searchView的文字輸入監聽

*/
        //-----------------------------spinner----------------------
        mSpinner = (Spinner) view.findViewById(R.id.mSpinner);
        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.spinner_plan_type, R.layout.spinner_layout);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_itm_layout);
        mSpinner.setAdapter(spinnerAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();

                giftReceivedAdapter.selectedType=str;
                String query = mSearchView.getQuery().toString();
                giftReceivedAdapter.getFilter().filter(query);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return view;
    }

    //-----------------
    public void onResume(){

        getGiftReceived.getJSON();

        String[][] rGiftsData = new String[getGiftReceived.getGiftLength()][20];

        Log.v("res_length",getGiftReceived.getGiftLength()+"");

        for(int i = 0 ;i < getGiftReceived.getGiftLength(); i++){

            rGiftsData[i][0]= getGiftReceived.getType(i);
            rGiftsData[i][1]= getGiftReceived.getPlanName(i);
            rGiftsData[i][2]= getGiftReceived.getNickname(i);
            rGiftsData[i][3]= getGiftReceived.getSendPlanDate(i);
        }
        rGiftsList.clear();

        rGiftsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> rGifts;

        for(int i=0;i<getGiftReceived.getGiftLength();i++) {
            rGifts = new HashMap<String, Object>();
            rGifts.put("type", rGiftsData[i][0]);
            rGifts.put("title", rGiftsData[i][1]);
            rGifts.put("sender", rGiftsData[i][2]);
            rGifts.put("date", rGiftsData[i][3]);
            rGiftsList.add(rGifts);
        }
        giftReceivedAdapter = new GiftReceivedAdapter(getActivity(), rGiftsList);

        mListView.setAdapter(giftReceivedAdapter);
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
                //Toast.makeText(getActivity(), "You Choose "+ ((TextView)view.findViewById(R.id.tv_giftTitle)).getText().toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent = new Intent(getActivity(), GiftReceivedDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("sender",((TextView)view.findViewById(R.id.tv_sender)).getText().toString());
                bundle.putString("title",((TextView)view.findViewById(R.id.tv_giftTitle)).getText().toString());
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
                giftReceivedAdapter.getFilter().filter(query);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                giftReceivedAdapter.getFilter().filter(newText);

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


}
