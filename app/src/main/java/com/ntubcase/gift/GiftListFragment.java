package com.ntubcase.gift;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ntubcase.gift.Adapter.GiftListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftListFragment extends Fragment {

    private SearchView mSearchView;
    private ListView mListView;
    private GiftListAdapter giftListAdapter;
    private List<Map<String, Object>> mGiftsList; //禮物清單
    private FloatingActionButton fab1, fab2, fab3;
    private FloatingActionMenu newGift;
    private Spinner mSpinner;
    private ArrayAdapter spinnerAdapter;

    public GiftListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_list, container, false);

        //---------------------ListView--------------------------------
        mListView = (ListView) view.findViewById(R.id.giftList);
        mSearchView = (SearchView) view.findViewById(R.id.mSearch);

        mGiftsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mGifts;

        String[][] mGiftsData = {       //禮物清單內容
                {"照片","小明生日賀卡"},
                {"影片","結婚紀念日"},
                {"兌換券","跑腿兌換券"},
                {"照片","禮物1"},
                {"兌換券","禮物2"},
                {"影片","禮物3"},
                {"兌換券","禮物4"}
        };

        for(int i=0;i<mGiftsData.length;i++) {
            mGifts = new HashMap<String, Object>();
            mGifts.put("type", mGiftsData[i][0]);
            mGifts.put("title", mGiftsData[i][1]);
            mGiftsList.add(mGifts);
        }
        giftListAdapter = new GiftListAdapter(getActivity(), mGiftsList);

        mListView.setAdapter(giftListAdapter);
        mListView.setTextFilterEnabled(true);

        setmListViewListener(); //設定ListView的監聽
        setSearch_function(); // 設定searchView的文字輸入監聽

        //-----------------------------spinner----------------------
        mSpinner = (Spinner) view.findViewById(R.id.mSpinner);
        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.gift_type, R.layout.spinner_layout);
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
        newGift = (FloatingActionMenu) view.findViewById(R.id.newGift);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);
        fab1.setOnClickListener(fabClickListener);
        fab2.setOnClickListener(fabClickListener);
        fab3.setOnClickListener(fabClickListener);


        return view;
    }

    // ----------------設定ListView的監聽---------------
    private void setmListViewListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "You Choose "+ ((TextView)view.findViewById(R.id.tv_giftTitle)).getText().toString() , Toast.LENGTH_SHORT).show();
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
                    intent = new Intent(getActivity(), MakeGiftsActivity.class);
                    break;
                case R.id.fab2:
                    intent = new Intent(getActivity(), MakeGiftsActivity.class);
                    break;
                case R.id.fab3:
                    intent = new Intent(getActivity(), MakeGiftsActivity.class);
                    break;
            }

            newGift.close(true);
            startActivity(intent);
        }
    };


}
