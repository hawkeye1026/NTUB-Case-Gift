package com.ntubcase.gift;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ntubcase.gift.Adapter.PlanListAdapter;
import com.ntubcase.gift.data.getPlanningList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlanNewFragment extends Fragment {

    private FloatingActionButton fab_single, fab_multi, fab_list;
    private FloatingActionMenu newPlan;
    private ListView mListView;
    private List<Map<String, Object>> mPlansList; //計畫清單
    private Spinner mSpinner;
    private SearchView mSearchView;
    private PlanListAdapter planListAdapter;
    private ArrayAdapter spinnerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    //--------------------------------------------
    private mMultiChoiceListener multiChoiceListener;  //list多選模式監聽器
    private View actionBarView;  //多選模式中的action bar
    private TextView selectedNum;  //顯示選中個項目個數
    private ActionMode mActionMode;

    public PlanNewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_new, container, false);

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
                if (mActionMode!=null) mActionMode.finish(); //---關閉多選模式---

                String str = parent.getItemAtPosition(position).toString();
                planListAdapter.selectedType=str;

                String query = mSearchView.getQuery().toString();
                planListAdapter.getFilter().filter(query);
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
                getPlanningData(); //更新資料
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //------------------------------FAB_newGift----------------------
        newPlan = (FloatingActionMenu) view.findViewById(R.id.newPlan);
        fab_single = (FloatingActionButton) view.findViewById(R.id.fab_single);
        fab_multi = (FloatingActionButton) view.findViewById(R.id.fab_multi);
        fab_list = (FloatingActionButton) view.findViewById(R.id.fab_list);
        fab_single.setOnClickListener(fabClickListener);
        fab_multi.setOnClickListener(fabClickListener);
        fab_list.setOnClickListener(fabClickListener);

        return view;
    }

    //-----------------取得未送出計畫資料-----------------
    private void getPlanningData(){

        getPlanningList.getJSON();

        //---------------------ListView倒入資料--------------------------------
        String[][] mPlansData = new String[getPlanningList.getPlanningLength()][20];

        for(int i = 0 ;i < getPlanningList.getPlanningLength(); i++){
            mPlansData[i][0]= getPlanningList.getPlanid(i);
            mPlansData[i][1]= getPlanningList.getPlanType(i);
            mPlansData[i][2]= getPlanningList.getPlanName(i);
            mPlansData[i][3]= getPlanningList.getCreateDate(i);
            mPlansData[i][4]= getPlanningList.getSendPlanDate(i);
        }

        mPlansList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mPlans;

        for(int i=0;i<getPlanningList.getPlanningLength();i++) {
            mPlans = new HashMap<String, Object>();
            mPlans.put("planID", mPlansData[i][0]);
            mPlans.put("type", mPlansData[i][1]);
            mPlans.put("title", mPlansData[i][2]);
            mPlans.put("date", mPlansData[i][3]);
            mPlans.put("sendPlanDate", mPlansData[i][4]);
            mPlansList.add(mPlans);
        }
        planListAdapter = new PlanListAdapter(getContext(), mPlansList);

        multiChoiceListener = new mMultiChoiceListener();
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(multiChoiceListener);

        mListView.setAdapter(planListAdapter);
        mListView.setTextFilterEnabled(true);

        mSpinner.setSelection(0); //spinner預設為全部
        setmListViewListener(); //設定ListView的監聽
        setSearch_function(); // 設定searchView的文字輸入監聽
    }

    @Override
    public void onResume(){
        getPlanningData();
        super.onResume();
    }

    @Override
    public void onDestroyView(){
        if (mActionMode!=null) mActionMode.finish(); //---關閉多選模式---
        super.onDestroyView();
    }


    // ----------------設定ListView的監聽---------------
    private void setmListViewListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                String type = planListAdapter.getItem().get(position).get("type").toString();
                String planID = planListAdapter.getItem().get(position).get("planID").toString();

                switch (type){
                    case "單日送禮" :
                        intent = new Intent(getActivity(), MakePlanSingleActivity.class);
                        break;
                    case "多日規劃" :
                        intent = new Intent(getActivity(), MakePlanMultipleActivity.class);
                        break;
                    case "任務清單" :
                        intent = new Intent(getActivity(), MakePlanListActivity.class);
                        break;
                }

                bundle.putString("planID", planID);
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
                if (mActionMode!=null) mActionMode.finish(); //---關閉多選模式---
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
                case R.id.fab_single:
                    intent = new Intent(getActivity(), MakePlanSingleActivity.class);
                    break;
                case R.id.fab_multi:
                    intent = new Intent(getActivity(), MakePlanMultipleActivity.class);
                    break;
                case R.id.fab_list:
                    intent = new Intent(getActivity(), MakePlanListActivity.class);
                    break;
            }

            newPlan.close(true);
            startActivity(intent);
        }
    };

    //----------------------刪除，多選模式的監聽器----------------------
    class mMultiChoiceListener implements AbsListView.MultiChoiceModeListener {

        //-----item狀態改變-----
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            updateSelectedCount();
            planListAdapter.notifyDataSetChanged();
        }

        //-----顯示目前選中的項目個數-----
        public void updateSelectedCount() {
            int count = mListView.getCheckedItemCount();
            selectedNum.setText("" + count);
        }

        //-----初始化ActionBar-----
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getActivity().getMenuInflater().inflate(R.menu.menu_multi_choice, menu);
            mActionMode = mode;

            if (actionBarView == null) {
                actionBarView = LayoutInflater.from(getActivity()).inflate(R.layout.delete_actionbar_layout, null);
                selectedNum = (TextView) actionBarView.findViewById(R.id.selected_num);
            }
            mode.setCustomView(actionBarView);
            return true;
        }

        //-----點選ActionBar的item-----
        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_delete:  //刪除鈕
                    new AlertDialog.Builder(getActivity())
                            .setTitle("確定要刪除選取的計畫嗎?")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //-----刪除計畫-----
                                    planListAdapter.deleteItems();
                                    mode.finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();

                    break;
                default:
                    break;

            }
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        //-----退出多選模式-----
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mListView.clearChoices();
        }
    }
}
