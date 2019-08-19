package com.ntubcase.gift;

import android.app.ProgressDialog;
import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.giftInsertImg_giftAsyncTask;
import com.ntubcase.gift.data.getGiftList;

import java.text.SimpleDateFormat;
import java.util.Date;

public class uploadGift {

    public uploadGift(String giftContent, String giftName, String owner, String giftType){
        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateTime = sdFormat.format(date);
//        Log.v("gift",giftContent+"||"+giftName+"||"+owner+"||"+giftType);
        //------------------------------上傳禮物資料
        giftInsertImg_giftAsyncTask mgiftInsertAsyncTask = new giftInsertImg_giftAsyncTask(new giftInsertImg_giftAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        mgiftInsertAsyncTask.execute(Common.insertGift, giftContent, dateTime, giftName, owner, giftType);
    }
}
