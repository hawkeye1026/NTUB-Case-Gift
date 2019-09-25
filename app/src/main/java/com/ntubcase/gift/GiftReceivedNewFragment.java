package com.ntubcase.gift;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.ntubcase.gift.Adapter.GiftReceivedNewAdapter;
import com.ntubcase.gift.data.getReceiveNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftReceivedNewFragment extends Fragment {
    private RecyclerView recyclerView;
    private GiftReceivedNewAdapter giftReceivedAdapter;
    private List<Map<String, Object>> rGiftsList; //禮物清單
    private View view;
    private SearchView mSearchView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gift_received_new, container, false);
        rGiftsList = new ArrayList<Map<String, Object>>();
        recyclerView = view.findViewById(R.id.recyclerView);
        mSearchView = (SearchView) view.findViewById(R.id.mSearch);

        //---------------------SwipeRefreshLayout--------------------------------
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getNewReceivedGiftData(); //更新資料
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    //-----------------取得收禮箱 新禮物資料-----------------
    private void getNewReceivedGiftData() {
        getReceiveNew.getJSON();

        String[][] rGiftsData = new String[getReceiveNew.getReceiveNewLength()][20];
        for (int i = 0; i < getReceiveNew.getReceiveNewLength(); i++) {
            rGiftsData[i][0] = getReceiveNew.getPlanType(i);
            rGiftsData[i][1] = getReceiveNew.getPlanName(i);
            rGiftsData[i][2] = getReceiveNew.getNickname(i);
            rGiftsData[i][3] = getReceiveNew.getSendPlanDate(i);
            rGiftsData[i][4] = getReceiveNew.getPlanid(i);
        }

        rGiftsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> rGifts;

        for (int i = 0; i < getReceiveNew.getReceiveNewLength(); i++) {
            rGifts = new HashMap<String, Object>();
            rGifts.put("type", rGiftsData[i][0]);
            rGifts.put("title", rGiftsData[i][1]);
            rGifts.put("sender", rGiftsData[i][2]);
            rGifts.put("date", rGiftsData[i][3]);
            rGifts.put("planID", rGiftsData[i][4]);
            rGiftsList.add(rGifts);
        }

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        giftReceivedAdapter = new GiftReceivedNewAdapter(getActivity(), rGiftsList);
        recyclerView.setAdapter(giftReceivedAdapter);
        setSearch_function(); // 設定searchView的文字輸入監聽
    }

    @Override
    public void onResume(){
        getNewReceivedGiftData();
        super.onResume();
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
