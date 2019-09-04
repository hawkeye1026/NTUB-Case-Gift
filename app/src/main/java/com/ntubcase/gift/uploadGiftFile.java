package com.ntubcase.gift;

import android.content.Context;
import android.net.Uri;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.insert.giftInsertImg_imageAsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class uploadGiftFile {

    public uploadGiftFile(Context context, Uri cam_imageUri, String giftContent, String old_giftContent, String owner,String fileType,String crud){
        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateTime = sdFormat.format(date);
//        Log.v("gift",giftContent+"||"+giftName+"||"+owner+"||"+giftType);
        //------------------------------上傳禮物資料
        giftInsertImg_imageAsyncTask mGiftInsertImgAsyncTask = new giftInsertImg_imageAsyncTask(new giftInsertImg_imageAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        }, ImageFilePath.getPath(context, cam_imageUri));
        mGiftInsertImgAsyncTask.execute(Common.insertGiftUploadGiftFile, giftContent, old_giftContent,owner,fileType,crud);
    }
}
