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

import com.ntubcase.gift.Adapter.PlanListAdapter;
import com.ntubcase.gift.data.getPlanList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlanDoneFragment extends Fragment {

    private ListView mListView;
    private List<Map<String, Object>> mPlansList; //計畫清單
    private Spinner mSpinner;
    private SearchView mSearchView;
    private PlanListAdapter planListAdapter;
    private ArrayAdapter spinnerAdapter;

    public PlanDoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_done, container, false);

        //---------------------ListView--------------------------------
        mListView = (ListView) view.findViewById(R.id.planList);
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

                planListAdapter.selectedType=str;
                String query = mSearchView.getQuery().toString();
                planListAdapter.getFilter().filter(query);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onResume(){

        getPlanList.getJSON();

        //---------------------ListView倒入資料--------------------------------

        String[][] mPlansData = new String[getPlanList.getPlanLength()][20];
        //Log.e("Plan","onResume: "+getPlanList.getPlanLength());
        for(int i = 0 ;i < getPlanList.getPlanLength(); i++){
            mPlansData[i][0]= getPlanList.getPlanType(i);
            mPlansData[i][1]= getPlanList.getSpPlanName(i);
            mPlansData[i][2]= getPlanList.getSpCreateDate(i);
            mPlansData[i][3]= getPlanList.getSpPlanid(i);
        }

        mPlansList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mPlans;

        for(int i=0;i<getPlanList.getPlanLength();i++) {
            mPlans = new HashMap<String, Object>();
            mPlans.put("type", mPlansData[i][0]);
            mPlans.put("title", mPlansData[i][1]);
            mPlans.put("date", mPlansData[i][2]);
            mPlans.put("planid", mPlansData[i][3]);
            mPlansList.add(mPlans);
        }
        planListAdapter = new PlanListAdapter(getContext(), mPlansList);

        mListView.setAdapter(planListAdapter);
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
                //Toast.makeText(getApplicationContext(), "You Choose "+ ((TextView)view.findViewById(R.id.tv_planTitle)).getText().toString() , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent = new Intent(getActivity(), PlanDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type",mPlansList.get(position).get("type").toString());
                bundle.putString("planid",mPlansList.get(position).get("planid").toString());
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
                planListAdapter.getFilter().filter(query);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                planListAdapter.getFilter().filter(newText);
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
