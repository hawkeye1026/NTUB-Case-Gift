package com.ntubcase.gift;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class checkReceivedTime {

    public static boolean checkReceivedTime(String date_s){
        //欲轉換的日期字串

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date deadline_d = null;

        try {
            deadline_d = sdf.parse(date_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.v("testtime111", deadline_d + "");
        Date now =new Date();
        //deadline_d < now 為true 否則為false
        if(deadline_d.before(now)){
            Log.v("testtime1", deadline_d + "");
            return true;
        }
        else {
            Log.v("testtime2", deadline_d + "");
            return false;
        }
    }
}
