package com.ntubcase.gift;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

import com.ntubcase.gift.Adapter.FriendListAdapter;
import com.ntubcase.gift.Adapter.GiftReceivedAdapter;
//import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.Adapter.reSurpriseGiftAdapter;
import com.ntubcase.gift.data.getGiftReceived;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ntubcase.gift.Common.Common.giftList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftReceivedNewFragment extends Fragment {
    RecyclerView recyclerView;
    private GiftReceivedAdapter giftReceivedAdapter;
    private List<Map<String, Object>> rGiftsList; //禮物清單
    private View view;
    private SearchView mSearchView;
    private ListView mListView;
    private Spinner mSpinner;
    private ArrayAdapter spinnerAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gift_received_new, container, false);
        rGiftsList = new ArrayList<Map<String, Object>>();
        mListView = (ListView) view.findViewById(R.id.giftList);
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
    public void onResume() {

        getGiftReceived.getJSON();
        String[][] rGiftsData = new String[getGiftReceived.getGiftLength()][20];
        rGiftsList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < getGiftReceived.getGiftLength(); i++) {
            rGiftsData[i][0] = getGiftReceived.getType(i);
            rGiftsData[i][1] = getGiftReceived.getPlanName(i);
            rGiftsData[i][2] = getGiftReceived.getNickname(i);
            rGiftsData[i][3] = getGiftReceived.getSendPlanDate(i);
            rGiftsData[i][4] = getGiftReceived.getPlanid(i);
        }
        rGiftsList.clear();
        rGiftsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> rGifts;

        for (int i = 0; i < getGiftReceived.getGiftLength(); i++) {
            rGifts = new HashMap<String, Object>();
            rGifts.put("type", rGiftsData[i][0]);
            rGifts.put("title", rGiftsData[i][1]);
            rGifts.put("sender", rGiftsData[i][2]);
            rGifts.put("date", rGiftsData[i][3]);
            rGifts.put("planid", rGiftsData[i][4]);
            rGiftsList.add(rGifts);
        }
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        giftReceivedAdapter = new GiftReceivedAdapter(this.rGiftsList);
        recyclerView.setAdapter(giftReceivedAdapter);
        setSearch_function(); // 設定searchView的文字輸入監聽
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
