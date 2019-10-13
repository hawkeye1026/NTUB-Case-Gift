package com.ntubcase.gift;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.VideoView;

import com.ntubcase.gift.Adapter.ReceivedPagerAdapter;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.receive.decodeTableAsyncTask;
import com.ntubcase.gift.data.getGiftList;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceivedShowGiftActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private List<String> giftContent, giftType; //bundle傳遞的資料
    private ViewPager mViewPager;
    private ArrayList<View> mViews; //傳至adapter
    private View viewCode, viewImage, viewMessage, viewTicket, viewVideo;
    private ViewGroup mViewGroup;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_show_gift);
        setTitle("收禮細節");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //---------------------------------上一頁資料-----------------------------------
        Bundle bundle = getIntent().getExtras();
        giftContent = (List<String>)bundle.getSerializable("giftContent");
        giftType = (List<String>)bundle.getSerializable("giftType");

        //---------------------------------PagerView-----------------------------------
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        //----- 將view加入mViews ------
        mViews = new ArrayList<View>();
        for (int i=0; i<giftContent.size(); i++){
            switch (giftType.get(i)){
                case "1": //照片
                    viewImage = getLayoutInflater().inflate(R.layout.received_gift_image,null);
                    setGiftContent("1", i); //設定禮物內容
                    mViews.add(viewImage);
                    break;
                case "2": //影片
                    viewVideo = getLayoutInflater().inflate(R.layout.received_gift_video,null);
                    setGiftContent("2", i); //設定禮物內容
                    mViews.add(viewVideo);
                    break;
                case "3": //悄悄話
                    viewMessage = getLayoutInflater().inflate(R.layout.received_gift_message,null);
                    setGiftContent("3", i); //設定禮物內容
                    mViews.add(viewMessage);
                    break;
                case "4": //兌換券
                    viewTicket = getLayoutInflater().inflate(R.layout.received_gift_ticket,null);
                    setGiftContent("4", i); //設定禮物內容
                    mViews.add(viewTicket);
                    break;
                case "5": //密碼表
                    viewCode = getLayoutInflater().inflate(R.layout.received_gift_code,null);
                    setGiftContent("5", i); //設定禮物內容
                    mViews.add(viewCode);
                    break;
            }
        }

        mViewPager.setAdapter(new ReceivedPagerAdapter(mViews));
        mViewPager.addOnPageChangeListener(this);

        //-----加入下方圓點 ------
        mViewGroup = (ViewGroup) findViewById(R.id.mViewGroup);
        dots = new ImageView[mViews.size()];
        for(int i =0;i<mViews.size();i++){
            ImageView imageView = new ImageView(ReceivedShowGiftActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
            layoutParams.setMargins(5,0,5,0);
            imageView.setLayoutParams(layoutParams);

            dots[i] = imageView;

            if (i == 0) { //預設第一張圖顯示為選中狀態
                dots[i].setBackgroundResource(R.drawable.ic_dot_red);
            } else {
                dots[i].setBackgroundResource(R.drawable.ic_dot_gray);
            }

            mViewGroup.addView(dots[i]);
        }
    }

    //----------------設定禮物內容----------------
    private void setGiftContent(String type, int position){
        ImageView iv_image; //照片
        VideoView vv_content; //影片
        EditText et_giftContent; //悄悄話,兌換券

        switch (type){
            case "1": //-----照片-----
                iv_image =(ImageView) viewImage.findViewById(R.id.iv_image);
                Uri imageURI = Uri.parse(Common.imgPath  + giftContent.get(position));
                Picasso.get().load(imageURI).into(iv_image);
                break;
            case "2": //-----影片-----
                vv_content = (VideoView) viewVideo.findViewById(R.id.vv_content);
                MediaController mc = new MediaController(this); // 設定影片控制台
                vv_content.setMediaController(mc);
                Uri viedoURI = Uri.parse(Common.vidPath + giftContent.get(position));
                vv_content.setVideoURI(viedoURI);
                break;
            case "3": //-----悄悄話-----
                et_giftContent = (EditText) viewMessage.findViewById(R.id.et_giftContent);
                et_giftContent.setKeyListener(null);
                et_giftContent.setText(giftContent.get(position));
                break;
            case "4": //-----兌換券-----
                et_giftContent = (EditText) viewTicket.findViewById(R.id.et_giftContent);
                et_giftContent.setKeyListener(null);
                et_giftContent.setText(giftContent.get(position));
                break;
            case "5": //-----密碼表-----
                decodeContent(giftContent.get(position)); //取得密碼表資料
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onPageSelected(int position) {
        dots[position].setBackgroundResource(R.drawable.ic_dot_red); //選中的view的圓點

        for(int i=0;i<mViews.size();i++){
            if (i != position) { //未選中的view的圓點
                dots[i].setBackgroundResource(R.drawable.ic_dot_gray);
            }
        }
    }

    //----------------------密碼表取得資料----------------------
    private void decodeContent(String gift){
        decodeTableAsyncTask decodeTableAsyncTask = new decodeTableAsyncTask(new decodeTableAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                try {
                    if (result == null) {
                        Toast.makeText(ReceivedShowGiftActivity.this,"無資料!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject object = new JSONObject(result);

                    //取得禮物紀錄
                    JSONArray jsonArray = object.getJSONArray("result");
                    int resultLength = jsonArray.length();

                    for (int i = 0 ; i < resultLength ; i++){
                        String decodeid =jsonArray.getJSONObject(i).getString("decodeid");
                        String rowNumber =jsonArray.getJSONObject(i).getString("rowNumber");
                        String mainCode =jsonArray.getJSONObject(i).getString("mainCode");
                        String matchCode =jsonArray.getJSONObject(i).getString("matchCode");

                        tableAddRow(mainCode, matchCode); //tableLayout新增行
                    }

                } catch (Exception e) {
                }
            }
        });
        decodeTableAsyncTask.execute(Common.decodeTable, gift);  //gift= gift.gift =decode.decodeid
    }

    //----------------------密碼表tableLayout新增行----------------------
    private void tableAddRow(String mainCode, String matchCode){
        TableLayout tableLayout = (TableLayout) viewCode.findViewById(R.id.tab_01);
        TableRow tabRow = new TableRow(viewCode.getContext());

        for (int col = 0 ; col< 2; col++){
            EditText editText = new EditText(viewCode.getContext());

            editText.setTextColor(Color.rgb(135,51,36));
            editText.setBackgroundResource(R.drawable.bg_text);
            editText.setGravity(Gravity.CENTER);
            editText.setTextSize(18);
            editText.setPadding(10,10,10,10);
            LinearLayout.LayoutParams lp = new TableRow.LayoutParams(50,100);
            lp.setMargins(3,0,3,3);
            editText.setLayoutParams(lp);
            editText.setKeyListener(null);

            if(col == 0) editText.setText(mainCode);  //設定mainCode文字
            else if(col == 1) editText.setText(matchCode);  //設定matchCode文字

            tabRow.addView(editText);
        }
        tableLayout.addView(tabRow);
    }

    //------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar返回建
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
