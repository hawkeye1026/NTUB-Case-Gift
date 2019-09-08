package com.ntubcase.gift;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.giftRecordInsertAsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class updateGiftRecord {
    String dateTime, dateOnly;

    public updateGiftRecord(String senderid, String receiverid, String planid, String store, String planType){
        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date = new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTime = sdFormat.format(date);
        SimpleDateFormat _sdFormat = new SimpleDateFormat("yyyy-MM-dd ");
        dateOnly = _sdFormat.format(date);

        SimpleDateFormat sdFormat_giftContent = new SimpleDateFormat("yyyyMMddHHmmss");
        planid = "mis_" + sdFormat_giftContent.format(date);
        //------------------------------上傳禮物資料
        giftRecordInsertAsyncTask giftRecordInsertAsyncTask = new giftRecordInsertAsyncTask(new giftRecordInsertAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        //giftRecordInsertAsyncTask.execute(Common.insertMisPlan, sender, selectFriendIds.get(i), planid, store, planType);
    }

}
