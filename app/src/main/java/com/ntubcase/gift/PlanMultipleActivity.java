package com.ntubcase.gift;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.Adapter.PlanMultiAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlanMultipleActivity extends AppCompatActivity {

    private GridView gridView;
    private PlanMultiAdapter planMultiAdapter;
    private TextView tv_receiveFriend;

    private String planName, receiveFriend, startDate, endDate, message; //------bundle傳遞的資料
    private Date dateStart, dateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_multiple);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //啟用返回建

        //-----------------------------------------------------------------------------------------
        Bundle bundle = getIntent().getExtras();
        planName = bundle.getString("planName");
        receiveFriend = bundle.getString("receiveFriend");
        startDate = bundle.getString("startDate");
        endDate = bundle.getString("endDate");
        message = bundle.getString("message");

        setTitle(planName); //-----標題為計畫名稱-----
        tv_receiveFriend = (TextView) findViewById(R.id.tv_receiveFriend); //-----顯示收禮人-----
        tv_receiveFriend.setText("To. " + receiveFriend);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateStart = sdf.parse(startDate);
            dateEnd = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<String> selectDates = findDates(dateStart, dateEnd); //取得兩個日期間所有日期

        //---------------------------------GridView---------------------------------------------
        gridView = (GridView) findViewById(R.id.gridView);
        planMultiAdapter = new PlanMultiAdapter(this, selectDates);
        gridView.setAdapter(planMultiAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PlanMultipleActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //--------------取得兩個日期間所有日期------------------
    public List<String> findDates(Date dBegin, Date dEnd){
        List<String> lDate = new ArrayList<String>();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        lDate.add(sd.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }
        return lDate;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){ //toolbar返回建
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
