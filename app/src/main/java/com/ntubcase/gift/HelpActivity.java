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
    private static int[] giftPics = { R.drawable.help_gift_1,R.drawable.help_gift_2,R.drawable.help_gift_3};

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
            case "SettingActivity":

                break;

            case "GiftActivity":
                for (int i=0; i<giftPics.length; i++){
                    ImageView iv = new ImageView(this);
                    iv.setLayoutParams(mParams);
                    iv.setImageResource(giftPics[i]);
                    iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                    mViews.add(iv);
                }
                break;
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
