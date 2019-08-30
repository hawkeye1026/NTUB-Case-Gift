package com.ntubcase.gift;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.insert.giftInsertImg_giftAsyncTask;
import com.ntubcase.gift.MyAsyncTask.gift.update.giftUpdateAsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class updateGift {

    public updateGift(String giftid,String giftContent, String giftName, String owner, String giftType){
        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateTime = sdFormat.format(date);
//        Log.v("gift",giftContent+"||"+giftName+"||"+owner+"||"+giftType);
        //------------------------------上傳禮物資料
        giftUpdateAsyncTask mgiftUpdateAsyncTask = new giftUpdateAsyncTask(new giftUpdateAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        mgiftUpdateAsyncTask.execute(Common.updateGift,giftid, giftContent, dateTime, giftName, owner, giftType);
    }
}
