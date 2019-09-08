package com.ntubcase.gift;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
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

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ntubcase.gift.Adapter.GiftListAdapter;
import com.ntubcase.gift.data.getGiftList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private ListView mListView;
    private GiftListAdapter giftListAdapter;
    private List<Map<String, Object>> mGiftsList; //禮物清單
    private FloatingActionButton fabPhoto, fabVideo, fabMessage, fabTicket, fabCode;
    private FloatingActionMenu newGift;
    private Spinner mSpinner;
    private ArrayAdapter spinnerAdapter;
    private static int jslen = 0 ;
    private SwipeRefreshLayout swipeRefreshLayout;
    //--------------------------------------------
    private mMultiChoiceListener multiChoiceListener;  //list多選模式監聽器
    private View actionBarView;  //多選模式中的action bar
    private TextView selectedNum;  //顯示選中個項目個數
    int ItemCheckedStateChangedCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        setTitle(R.string.mGift);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------ListView--------------------------------
        mListView = (ListView) findViewById(R.id.giftList);
        mSearchView = (SearchView) findViewById(R.id.mSearch);

        //-----------------------------spinner----------------------
        mSpinner = (Spinner) findViewById(R.id.mSpinner);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_gift_type, R.layout.spinner_layout);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_itm_layout);
        mSpinner.setAdapter(spinnerAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();

                //清除已選取的Item
                mListView.clearChoices();
                if( ItemCheckedStateChangedCount > 0){
                    selectedNum.setText("" + 0);
                }

                if (str.equals(getString(R.string.giftPhoto))) str="1";
                else if(str.equals(getString(R.string.giftVideo))) str="2";
                else if(str.equals(getString(R.string.giftMessage))) str="3";
                else if(str.equals(getString(R.string.giftTicket))) str="4";
                else if(str.equals(getString(R.string.giftCode))) str="5";

                giftListAdapter.selectedType=str;
                String query = mSearchView.getQuery().toString();
                giftListAdapter.getFilter().filter(query);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //---------------------SwipeRefreshLayout--------------------------------
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getGiftData(); //更新資料
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //------------------------------FAB_newGift----------------------
        newGift = (FloatingActionMenu) findViewById(R.id.newGift);
        fabPhoto = (FloatingActionButton) findViewById(R.id.fabPhoto);
        fabVideo = (FloatingActionButton) findViewById(R.id.fabVideo);
        fabMessage = (FloatingActionButton) findViewById(R.id.fabMessage);
        fabTicket = (FloatingActionButton) findViewById(R.id.fabTicket);
        fabCode = (FloatingActionButton) findViewById(R.id.fabCode);
        fabPhoto.setOnClickListener(fabClickListener);
        fabVideo.setOnClickListener(fabClickListener);
        fabMessage.setOnClickListener(fabClickListener);
        fabTicket.setOnClickListener(fabClickListener);
        fabCode.setOnClickListener(fabClickListener);
    }

    //-----------------取得禮物資料-----------------
    private void getGiftData(){
        //---------------------ListView倒入資料--------------------------------
        getGiftList.getJSON();
        String[][] mGiftsData = new String[getGiftList.getGiftLength()][20];

        //Log.e("res_length",getGiftList.getGiftLength()+"");
        for(int i = 0 ;i < getGiftList.getGiftLength(); i++){
            mGiftsData[i][0]= getGiftList.getType(i);
            mGiftsData[i][1]= getGiftList.getGiftName(i);
            mGiftsData[i][2]= getGiftList.getGiftCreateDate(i);
            mGiftsData[i][3]= getGiftList.getGiftid(i);
        }

        mGiftsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> mGifts;

        for(int i=0;i<getGiftList.getGiftLength();i++) {
            mGifts = new HashMap<String, Object>();
            mGifts.put("type", mGiftsData[i][0]);
            mGifts.put("title", mGiftsData[i][1]);
            mGifts.put("date", mGiftsData[i][2]);
            mGifts.put("giftid", mGiftsData[i][3]);
            mGiftsList.add(mGifts);
        }
        giftListAdapter = new GiftListAdapter(this, mGiftsList);

        multiChoiceListener = new mMultiChoiceListener();
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(multiChoiceListener);

        mListView.setAdapter(giftListAdapter);
        mListView.setTextFilterEnabled(true);

        mSpinner.setSelection(0); //spinner預設為全部
        setmListViewListener(); //設定ListView的監聽
        setSearch_function(); // 設定searchView的文字輸入監聽
    }

    @Override
    public void onResume(){
        getGiftData();
        super.onResume();
    }


    // ----------------設定ListView的監聽---------------
    private void setmListViewListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent() ;
                Bundle bundle = new Bundle();
                String type =  giftListAdapter.getItem().get(position).get("type").toString();
                int giftid =  Integer.valueOf(giftListAdapter.getItem().get(position).get("giftid").toString());
//                try {
//                    Log.v("test", GiftListAdapter.getItem().get(position).get("type").toString());
//                    Log.v("test", GiftListAdapter.getItem().get(position).get("giftid").toString());
//                    Log.v("test", position + "");
//                }catch (Exception e){
//                    e.printStackTrace();
//                }

                switch (type) {
                    case "1":
                        //------------照片
                        intent = new Intent(GiftActivity.this, MakeGiftImageActivity.class);
                        break;
                    case "2":
                        //------------影片
                        intent = new Intent(GiftActivity.this, MakeGiftVideoActivity.class);
                        break;
                    case "3":
                        //------------悄悄話
                        intent = new Intent(GiftActivity.this, MakeGiftMessageActivity.class);
                        break;
                    case "4":
                        //------------兌換券
                        intent = new Intent(GiftActivity.this, MakeGiftTicketActivity.class);
                        break;
                    case "5":
                        //------------密碼表
                        intent = new Intent(GiftActivity.this, MakeGiftCodeActivity.class);
                        break;
                }

                bundle.putInt("giftid", giftid);
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
                giftListAdapter.getFilter().filter(query);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //清除已選取的Item
                mListView.clearChoices();

                if( ItemCheckedStateChangedCount > 0){
                    selectedNum.setText("" + 0);
                }

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
            Bundle bundle = new Bundle();

            switch (v.getId()) {
                case R.id.fabPhoto:
                    intent = new Intent(GiftActivity.this, MakeGiftImageActivity.class);
                    break;
                case R.id.fabVideo:
                    intent = new Intent(GiftActivity.this, MakeGiftVideoActivity.class);
                    break;
                case R.id.fabMessage:
                    intent = new Intent(GiftActivity.this, MakeGiftMessageActivity.class);
                    break;
                case R.id.fabTicket:
                    intent = new Intent(GiftActivity.this, MakeGiftTicketActivity.class);
                    break;
                case R.id.fabCode:
                    intent = new Intent(GiftActivity.this, MakeGiftCodeActivity.class);
                    break;
            }

            newGift.close(true);
            bundle.putInt("giftid", -1);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar返回建
                finish();
                return true;
            case R.id.action_enter_delete:  //刪除鈕，進入多選模式
                mListView.setItemChecked(0, true);
                mListView.clearChoices();
                multiChoiceListener.updateSelectedCount();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //----------------------刪除，多選模式的監聽器----------------------
    class mMultiChoiceListener implements AbsListView.MultiChoiceModeListener {

        //-----item狀態改變-----
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            updateSelectedCount();
            int giftid =  Integer.valueOf(giftListAdapter.getItem().get(position).get("giftid").toString());
            Log.v("giftid ",giftid + "");
            giftListAdapter.notifyDataSetChanged();
        }

        //-----顯示目前選中的項目個數-----
        public void updateSelectedCount() {
            ItemCheckedStateChangedCount = mListView.getCheckedItemCount();
            selectedNum.setText("" + ItemCheckedStateChangedCount);
        }

        //-----初始化ActionBar-----
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_multi_choice, menu);
            if (actionBarView == null) {
                actionBarView = LayoutInflater.from(GiftActivity.this).inflate(R.layout.delete_actionbar_layout, null);
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
                    new AlertDialog.Builder(GiftActivity.this)
                            .setTitle("確定要刪除選取的禮物嗎?")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //-----刪除禮物-----
                                    giftListAdapter.deleteItems();
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
