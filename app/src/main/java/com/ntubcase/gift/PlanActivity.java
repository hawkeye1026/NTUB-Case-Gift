package com.ntubcase.gift;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.ntubcase.gift.Adapter.GiftListAdapter;
import com.ntubcase.gift.Adapter.PlanListAdapter;
import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.data.getPlanList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanActivity extends AppCompatActivity {

    private FloatingActionButton fab_surprise, fab_calendar, fab_qa;
    private FloatingActionMenu newPlan;
    private ListView mListView;
    private List<Map<String, Object>> mPlansList; //計畫清單
    private Spinner mSpinner;
    private SearchView mSearchView;
    private PlanListAdapter planListAdapter;
    private ArrayAdapter spinnerAdapter;
    private String[][] mPlansData = new String[getPlanList.planLength()][20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        setTitle(R.string.mPlan);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //---------------------ListView--------------------------------
        mListView = (ListView) findViewById(R.id.planList);
        mSearchView = (SearchView) findViewById(R.id.mSearch);

        mPlansList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mPlans;
        /*
        //------------範例資料格式(計畫種類,計畫名稱,日期)----------
        String[][] mPlansData = {       //計畫清單內容
                {"驚喜式","小明生日","2019-04-04"},
                {"期間式","結婚紀念日","2019-04-05"},
                {"問答式","猜燈謎","2019-04-06"},
                {"驚喜式","爸爸生日","2019-04-07"},
                {"驚喜式","計畫1","2019-04-08"},
                {"期間式","計畫2","2019-04-09"},
                {"問答式","計畫3","2019-04-10"}
        };
        */
        for(int i = 0 ;i < getPlanList.planLength(); i++){
            mPlansData[i][0]= getPlanList.getPlanType(i);
            mPlansData[i][1]= getPlanList.getSpPlanName(i);
            mPlansData[i][2]= getPlanList.getSpCreateDate(i);
        }
        for(int i=0;i<getPlanList.planLength();i++) {
            mPlans = new HashMap<String, Object>();
            mPlans.put("type", mPlansData[i][0]);
            mPlans.put("title", mPlansData[i][1]);
            mPlans.put("date", mPlansData[i][2]);
            mPlansList.add(mPlans);
        }
        planListAdapter = new PlanListAdapter(this, mPlansList);

        mListView.setAdapter(planListAdapter);
        mListView.setTextFilterEnabled(true);

        setmListViewListener(); //設定ListView的監聽
        setSearch_function(); // 設定searchView的文字輸入監聽

        //-----------------------------spinner----------------------
        mSpinner = (Spinner) findViewById(R.id.mSpinner);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_plan_type, R.layout.spinner_layout);
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


        //------------------------------FAB_newGift----------------------
        newPlan = (FloatingActionMenu) findViewById(R.id.newPlan);
        fab_surprise = (FloatingActionButton) findViewById(R.id.fab_surprise);
        fab_calendar = (FloatingActionButton) findViewById(R.id.fab_calendar);
        fab_qa = (FloatingActionButton) findViewById(R.id.fab_qa);
        fab_surprise.setOnClickListener(fabClickListener);
        fab_calendar.setOnClickListener(fabClickListener);
        fab_qa.setOnClickListener(fabClickListener);
    }

    public void onResume(){

        getGiftList.getJSON();

        mListView = (ListView) findViewById(R.id.planList);
        mSearchView = (SearchView) findViewById(R.id.mSearch);

        mPlansList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mPlans;
        /*
        //------------範例資料格式(計畫種類,計畫名稱,日期)----------
        String[][] mPlansData = {       //計畫清單內容
                {"驚喜式","小明生日","2019-04-04"},
                {"期間式","結婚紀念日","2019-04-05"},
                {"問答式","猜燈謎","2019-04-06"},
                {"驚喜式","爸爸生日","2019-04-07"},
                {"驚喜式","計畫1","2019-04-08"},
                {"期間式","計畫2","2019-04-09"},
                {"問答式","計畫3","2019-04-10"}
        };
        */
        for(int i = 0 ;i < getPlanList.planLength(); i++){
            mPlansData[i][0]= getPlanList.getPlanType(i);
            mPlansData[i][1]= getPlanList.getGiftName(i);
            mPlansData[i][2]= getPlanList.getSpCreateDate(i);
        }
        for(int i=0;i<getPlanList.planLength();i++) {
            mPlans = new HashMap<String, Object>();
            mPlans.put("type", mPlansData[i][0]);
            mPlans.put("title", mPlansData[i][1]);
            mPlans.put("date", mPlansData[i][2]);
            mPlansList.add(mPlans);
        }
        planListAdapter = new PlanListAdapter(this, mPlansList);

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
                intent = new Intent(PlanActivity.this, PlanDetailActivity.class);
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


    // ----------------設定FAB的點擊監聽---------------
    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.fab_surprise:
                    intent = new Intent(PlanActivity.this, MakePlansActivity.class);
                    intent.putExtra("planType", fab_surprise.getLabelText());
                    break;
                case R.id.fab_calendar:
                    intent = new Intent(PlanActivity.this, MakePlansActivity.class);
                    intent.putExtra("planType", fab_calendar.getLabelText());
                    break;
                case R.id.fab_qa:
                    intent = new Intent(PlanActivity.this, MakePlansActivity.class);
                    intent.putExtra("planType", fab_qa.getLabelText());
                    break;
            }

            newPlan.close(true);
            startActivity(intent);
        }
    };



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
