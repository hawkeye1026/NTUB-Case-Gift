package com.ntubcase.gift;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.GiftListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftListFragment extends Fragment {

    private SearchView mSearchView;
    private ListView mListView;
    private GiftListAdapter giftListAdapter;
    private List<List<String>> mGiftsList; //禮物清單

    public GiftListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_list, container, false);

        //-----新增按鈕----
        FloatingActionButton fab = view.findViewById(R.id.newGift);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "新增禮物", Toast.LENGTH_SHORT).show();
            }
        });

        mListView = (ListView) view.findViewById(R.id.giftList);
        mSearchView = (SearchView) view.findViewById(R.id.mSearch);

        String[][] mGifts = {       //禮物清單內容
                {"1","小明生日賀卡"},
                {"2","結婚紀念日"},
                {"3","跑腿兌換券"},
                {"1","禮物1"},
                {"3","禮物2"},
                {"2","禮物3"},
                {"4","禮物4"}
        };

        mGiftsList = new ArrayList<List<String>>();
        for(int i=0; i<mGifts.length; i++){     //將禮物內容放進禮物清單
            List<String> aListData = new ArrayList<String>();
            aListData.add(mGifts[i][0]);
            aListData.add(mGifts[i][1]);
            mGiftsList.add(aListData);
        }

        giftListAdapter = new GiftListAdapter(getActivity(), mGiftsList);
        mListView.setAdapter(giftListAdapter);
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

}
