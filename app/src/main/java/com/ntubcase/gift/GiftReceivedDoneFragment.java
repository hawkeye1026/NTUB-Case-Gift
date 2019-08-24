package com.ntubcase.gift;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.ntubcase.gift.Adapter.GiftReceivedDoneAdapter;
import com.ntubcase.gift.data.getGiftReceived;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftReceivedDoneFragment extends Fragment {
    RecyclerView recyclerView;
    private GiftReceivedDoneAdapter giftReceivedDoneAdapter;
    private List<Map<String, Object>> rGiftsList; //禮物清單
    private View view;
    private SearchView mSearchView;
    private Spinner mSpinner;
    private ArrayAdapter spinnerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public GiftReceivedDoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gift_received_done, container, false);
        rGiftsList = new ArrayList<Map<String, Object>>();
        recyclerView = view.findViewById(R.id.recyclerView);
        mSearchView = (SearchView) view.findViewById(R.id.mSearch);

        //-----------------------------spinner----------------------
        mSpinner = (Spinner) view.findViewById(R.id.mSpinner);
        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.spinner_plan_type, R.layout.spinner_layout);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_itm_layout);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();

                giftReceivedDoneAdapter.selectedType=str;
                String query = mSearchView.getQuery().toString();
                giftReceivedDoneAdapter.getFilter().filter(query);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //---------------------SwipeRefreshLayout--------------------------------
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getDoneReceivedGiftData(); //更新資料
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    //-----------------取得收禮箱 已接收禮物資料-----------------
    private void getDoneReceivedGiftData() {
        getGiftReceived.getJSON();

        String[][] rGiftsData = new String[getGiftReceived.getGiftLength()][20];
        for (int i = 0; i < getGiftReceived.getGiftLength(); i++) {
            rGiftsData[i][0] = getGiftReceived.getType(i);
            rGiftsData[i][1] = getGiftReceived.getPlanName(i);
            rGiftsData[i][2] = getGiftReceived.getNickname(i);
            rGiftsData[i][3] = getGiftReceived.getSendPlanDate(i);
            rGiftsData[i][4] = getGiftReceived.getPlanid(i);
        }

        rGiftsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> rGifts;

        for (int i = 0; i < getGiftReceived.getGiftLength(); i++) {
            rGifts = new HashMap<String, Object>();
            rGifts.put("type", rGiftsData[i][0]);
            rGifts.put("title", rGiftsData[i][1]);
            rGifts.put("sender", rGiftsData[i][2]);
            rGifts.put("date", rGiftsData[i][3]);
            rGifts.put("planID", rGiftsData[i][4]);
            rGiftsList.add(rGifts);
        }

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        giftReceivedDoneAdapter = new GiftReceivedDoneAdapter(getActivity(), rGiftsList);
        recyclerView.setAdapter(giftReceivedDoneAdapter);

        mSpinner.setSelection(0); //spinner預設為全部
        setSearch_function(); // 設定searchView的文字輸入監聽
    }

    @Override
    public void onResume() {
        getDoneReceivedGiftData();
        super.onResume();
    }

    // ----------------設定searchView的文字輸入監聽---------------
    private void setSearch_function(){
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                giftReceivedDoneAdapter.getFilter().filter(query);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                giftReceivedDoneAdapter.getFilter().filter(newText);
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
