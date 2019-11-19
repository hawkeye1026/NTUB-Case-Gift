package com.ntubcase.gift;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.ntubcase.gift.Adapter.GiftReceivedProcessAdapter;
import com.ntubcase.gift.data.getReceiving;
import com.ntubcase.gift.data.getReceiving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftReceivedProcessFragment extends Fragment {
    RecyclerView recyclerView;
    private GiftReceivedProcessAdapter giftReceivedProcessAdapter;
    private List<Map<String, Object>> rGiftsList; //禮物清單
    private View view;
    private SearchView mSearchView;
    private Spinner mSpinner;
    private ArrayAdapter spinnerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public GiftReceivedProcessFragment() {
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

                giftReceivedProcessAdapter.selectedType=str;
                String query = mSearchView.getQuery().toString();
                giftReceivedProcessAdapter.getFilter().filter(query);
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
        getReceiving.getJSON();

        String[][] rGiftsData = new String[getReceiving.getReceivingLength()][20];
        for (int i = 0; i < getReceiving.getReceivingLength(); i++) {
            rGiftsData[i][0] = getReceiving.getPlanType(i);
            rGiftsData[i][1] = getReceiving.getPlanName(i);
            rGiftsData[i][2] = getReceiving.getNickname(i);
            rGiftsData[i][3] = getReceiving.getSendPlanDate(i);
            rGiftsData[i][4] = getReceiving.getPlanid(i);
            Log.v("testdata",rGiftsData[i][4]);
            if(checkComplete(getReceiving.getPlanid(i))){
                rGiftsData[i][5] = "1";
            }else{
                rGiftsData[i][5] = "0";
            }

            Log.v("testplanid",""+checkComplete(getReceiving.getPlanid(i)));
        }

        rGiftsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> rGifts;

        for (int i = 0; i < getReceiving.getReceivingLength(); i++) {
            rGifts = new HashMap<String, Object>();
            rGifts.put("type", rGiftsData[i][0]);
            rGifts.put("title", rGiftsData[i][1]);
            rGifts.put("sender", rGiftsData[i][2]);
            rGifts.put("date", rGiftsData[i][3]);
            rGifts.put("planID", rGiftsData[i][4]);
            rGifts.put("checked", rGiftsData[i][5]);

            rGiftsList.add(rGifts);
        }

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        giftReceivedProcessAdapter = new GiftReceivedProcessAdapter(getActivity(), rGiftsList);
        recyclerView.setAdapter(giftReceivedProcessAdapter);

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
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                giftReceivedProcessAdapter.getFilter().filter(query);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                giftReceivedProcessAdapter.getFilter().filter(newText);
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

    public boolean checkComplete(String planid){
        for(int i = 0 ; i < getReceiving.getComLength();i++){
            if(planid.equals(getReceiving.getComPlanid(i)) && getReceiving.getComplete(i).equals("1") ){
                return true;
            }
        }
        for(int i = 0 ; i < getReceiving.getMisComLength();i++){
            if(planid.equals(getReceiving.getMisComid(i)) && getReceiving.getMisCom(i).equals("1") ){
                return true;
            }
        }
        return false;
    }

}
