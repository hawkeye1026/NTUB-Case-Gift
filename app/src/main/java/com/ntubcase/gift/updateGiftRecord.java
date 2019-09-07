package com.ntubcase.gift;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.giftRecordInsertAsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class updateGiftRecord {

    public updateGiftRecord(String giftid, String giftContent, String giftName, String owner, String giftType){
        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateTime = sdFormat.format(date);
//        Log.v("gift",giftContent+"||"+giftName+"||"+owner+"||"+giftType);
        //------------------------------上傳禮物資料
        giftRecordInsertAsyncTask giftRecordInsertAsyncTask = new giftRecordInsertAsyncTask(new giftRecordInsertAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        giftRecordInsertAsyncTask.execute(Common.updateGift,giftid, giftContent, dateTime, giftName, owner, giftType);
    }

}
