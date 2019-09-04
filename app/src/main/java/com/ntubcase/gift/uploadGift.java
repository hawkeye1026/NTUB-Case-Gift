package com.ntubcase.gift;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.insert.giftInsertAsyncTask;

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
        giftInsertAsyncTask mgiftInsertAsyncTask = new giftInsertAsyncTask(new giftInsertAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        mgiftInsertAsyncTask.execute(Common.insertGift, giftContent, dateTime, giftName, owner, giftType);
    }
}
