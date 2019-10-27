package com.ntubcase.gift;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ntubcase.gift.Adapter.ReceivedPagerAdapter;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private String from;
    private Button btn_close;
    private ViewPager mViewPager;
    private ArrayList<View> mViews; //傳至adapter
    private ViewGroup mViewGroup;
    private ImageView[] dots;

    //------------說明圖------------
    //禮物 清單
    private static int[] giftPics = { R.drawable.help_gift_1,R.drawable.help_gift_2,R.drawable.help_gift_3};
//    //禮物 直接送禮
//    private static int[] giftDirectPics = { R.drawable.help_gift_direct_1,R.drawable.help_gift_direct_2};
    //計畫 清單
    private static int[] planPics = { R.drawable.help_plan_1,R.drawable.help_plan_2,R.drawable.help_plan_3,R.drawable.help_plan_4};
    //計畫 單日
    private static int[] planSinPics = { R.drawable.help_plan_sin_1,R.drawable.help_plan_sin_2};
    //計畫 多日
    private static int[] planMulPics = { R.drawable.help_plan_mul_1,R.drawable.help_plan_mul_2,R.drawable.help_plan_mul_3};
    //計畫 任務
    private static int[] planMisPics = { R.drawable.help_plan_mis_1,R.drawable.help_plan_mis_2};
//    //收禮
//    private static int[] receivedPics = { R.drawable.help_received_1,R.drawable.help_received_2};
//    //好友
//    private static int[] friendPics = { R.drawable.help_friend_1,R.drawable.help_friend_2};
//    //設定
//    private static int[] settingPics = { R.drawable.help_setting_1,R.drawable.help_setting_2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        //---------------------------------------------------------------------
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mViews = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        Bundle bundle =getIntent().getExtras();
        if (bundle!=null) from = bundle.getString("from");

        switch (from){
            case "GiftActivity": //禮物 清單
                for (int i=0; i<giftPics.length; i++){
                    ImageView iv = new ImageView(this);
                    iv.setLayoutParams(mParams);
                    iv.setImageResource(giftPics[i]);
                    iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                    mViews.add(iv);
                }
                break;
//            case "SendGiftDirectlyActivity": //禮物 直接送禮
//                for (int i=0; i<giftDirectPics.length; i++){
//                    ImageView iv = new ImageView(this);
//                    iv.setLayoutParams(mParams);
//                    iv.setImageResource(giftDirectPics[i]);
//                    iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//
//                    mViews.add(iv);
//                }
//                break;
            case "PlanActivity":  //計畫 清單
                for (int i=0; i<planPics.length; i++){
                    ImageView iv = new ImageView(this);
                    iv.setLayoutParams(mParams);
                    iv.setImageResource(planPics[i]);
                    iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                    mViews.add(iv);
                }
                break;
            case "PlanSingleActivity":  //計畫 單日
                for (int i=0; i<planSinPics.length; i++){
                    ImageView iv = new ImageView(this);
                    iv.setLayoutParams(mParams);
                    iv.setImageResource(planSinPics[i]);
                    iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                    mViews.add(iv);
                }
                break;
            case "PlanMultiActivity":  //計畫 多日
                for (int i=0; i<planMulPics.length; i++){
                    ImageView iv = new ImageView(this);
                    iv.setLayoutParams(mParams);
                    iv.setImageResource(planMulPics[i]);
                    iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                    mViews.add(iv);
                }
                break;
            case "PlanListActivity":  //計畫 任務
                for (int i=0; i<planMisPics.length; i++){
                    ImageView iv = new ImageView(this);
                    iv.setLayoutParams(mParams);
                    iv.setImageResource(planMisPics[i]);
                    iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                    mViews.add(iv);
                }
                break;
//            case "GiftReceivedActivity":  //收禮
//                for (int i=0; i<receivedPics.length; i++){
//                    ImageView iv = new ImageView(this);
//                    iv.setLayoutParams(mParams);
//                    iv.setImageResource(receivedPics[i]);
//                    iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//
//                    mViews.add(iv);
//                }
//                break;
//            case "FriendActivity":  //好友
//                for (int i=0; i<friendPics.length; i++){
//                    ImageView iv = new ImageView(this);
//                    iv.setLayoutParams(mParams);
//                    iv.setImageResource(friendPics[i]);
//                    iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//
//                    mViews.add(iv);
//                }
//                break;
//            case "SettingActivity":  //設定
//                for (int i=0; i<settingPics.length; i++){
//                    ImageView iv = new ImageView(this);
//                    iv.setLayoutParams(mParams);
//                    iv.setImageResource(settingPics[i]);
//                    iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//
//                    mViews.add(iv);
//                }
//                break;
        }

        mViewPager.setAdapter(new ReceivedPagerAdapter(mViews));
        mViewPager.addOnPageChangeListener(this);

        //-----加入下方圓點 ------
        mViewGroup = (ViewGroup) findViewById(R.id.mViewGroup);
        dots = new ImageView[mViews.size()];
        for(int i =0;i<mViews.size();i++){
            ImageView imageView = new ImageView(HelpActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
            layoutParams.setMargins(10,0,10,0);
            imageView.setLayoutParams(layoutParams);

            dots[i] = imageView;

            if (i == 0) { //預設第一張圖顯示為選中狀態
                dots[i].setBackgroundResource(R.drawable.ic_dot_red);
            } else {
                dots[i].setBackgroundResource(R.drawable.ic_dot_gray);
            }

            mViewGroup.addView(dots[i]);
        }

        //---------------------------------------------------------------------
        btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

}
