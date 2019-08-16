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
    private SearchView mSearchView;
    private ListView mListView;
    private GiftReceivedAdapter giftReceivedAdapter;
    private List<Map<String, Object>> rGiftsList; //禮物清單
    private Spinner mSpinner;
    private ArrayAdapter spinnerAdapter;

    public View onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_received_new, container, false);
        mSearchView = (SearchView) view.findViewById(R.id.mSearch);
        rGiftsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> rGifts;
        String[][] rGiftsData = new String[getGiftReceived.getGiftLength()][20];

        return view;
    }

    private void showData(View view){
        List<SurpriseCardviewGiftItem> re_giftList = new ArrayList<>();
        //cardview資料傳入---------------------------------
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new GiftReceivedAdapter(this, re_giftList));
    }


}
