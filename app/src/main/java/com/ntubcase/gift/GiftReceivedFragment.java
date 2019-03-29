package com.ntubcase.gift;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.GiftReceivedAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftReceivedFragment extends Fragment {

    private SearchView mSearchView;
    private ListView mListView;
    private GiftReceivedAdapter giftReceivedAdapter;
    private List<List<String>> rGiftsList; //禮物清單

    public GiftReceivedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_received, container, false);

        mListView = (ListView) view.findViewById(R.id.giftList);
        mSearchView = (SearchView) view.findViewById(R.id.mSearch);

        String[][] rGifts = {       //禮物清單內容
                {"1","生日賀卡","林同學"},
                {"1","結婚紀念照","老婆"},
                {"2","健身計畫","陳同事"},
                {"1","按摩兌換券","兒子"},
                {"3","考考你","好友1"},
                {"1","禮物3","好友2"}
        };

        rGiftsList = new ArrayList<List<String>>();
        for(int i=0; i<rGifts.length; i++){     //將禮物內容放進禮物清單
            List<String> aListData = new ArrayList<String>();
            aListData.add(rGifts[i][0]);
            aListData.add(rGifts[i][1]);
            aListData.add(rGifts[i][2]);
            rGiftsList.add(aListData);
        }

        giftReceivedAdapter = new GiftReceivedAdapter(getActivity(), rGiftsList);
        mListView.setAdapter(giftReceivedAdapter);
        mListView.setTextFilterEnabled(true);

        setmListViewListener(); //設定ListView的監聽
        setSearch_function(); // 設定searchView的文字輸入監聽

        return view;
    }

    // ----------------設定ListView的監聽---------------
    private void setmListViewListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "You Choose "+ position+" listItem", Toast.LENGTH_SHORT).show();
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
