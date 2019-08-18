package com.ntubcase.gift;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.ntubcase.gift.Adapter.GiftReceivedAdapter;
import com.ntubcase.gift.data.getReceiveNew;
import com.ntubcase.gift.data.getReceiveOpen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftReceivedDoneFragment extends Fragment {
    Context context;

    private SearchView mSearchView;
    private ListView mListView;
    private GiftReceivedAdapter giftReceivedAdapter;
    private List<Map<String, Object>> rGiftsList; //禮物清單
    private Spinner mSpinner;
    private ArrayAdapter spinnerAdapter;


    public GiftReceivedDoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gift_received_done, container, false);
    }
/*   --執行會失敗，先註解掉。
    //-----------------
    public void onResume(){

        getReceiveOpen.getJSON();

        String[][] rGiftsData = new String[getReceiveOpen.getReceiveOpenLength()][20];

        Log.v("res_length",getReceiveOpen.getReceiveOpenLength()+"");

        for(int i = 0 ;i < getReceiveOpen.getReceiveOpenLength(); i++){
            rGiftsData[i][0]= getReceiveOpen.getPlanType(i);
            rGiftsData[i][1]= getReceiveOpen.getPlanName(i);
            rGiftsData[i][2]= getReceiveOpen.getNickname(i);
            rGiftsData[i][3]= getReceiveOpen.getSendPlanDate(i);
            rGiftsData[i][4]= getReceiveOpen.getPlanid(i);
        }
        rGiftsList.clear();

        rGiftsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> rGifts;

        for(int i=0;i<getReceiveOpen.getReceiveOpenLength();i++) {
            rGifts = new HashMap<String, Object>();
            rGifts.put("type", rGiftsData[i][0]);
            rGifts.put("title", rGiftsData[i][1]);
            rGifts.put("sender", rGiftsData[i][2]);
            rGifts.put("date", rGiftsData[i][3]);
            rGifts.put("planid", rGiftsData[i][4]);
            rGiftsList.add(rGifts);
        }
        giftReceivedAdapter = new GiftReceivedAdapter(getActivity(), rGiftsList);

        mListView.setAdapter(giftReceivedAdapter);
        mListView.setTextFilterEnabled(true);

        setmListViewListener(); //設定ListView的監聽
        setSearch_function(); // 設定searchView的文字輸入監聽
        super.onResume();


    }
*/
}
