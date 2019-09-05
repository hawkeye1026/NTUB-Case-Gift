package com.ntubcase.gift;

import android.content.Context;
import android.net.Uri;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.insert.giftInsertImg_imageAsyncTask;
import com.ntubcase.gift.MyAsyncTask.gift.insert.giftInsertImg_infoAsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class uploadGiftFile {

    public uploadGiftFile(Context context, Uri cam_imageUri, String giftContent, String old_giftContent, String owner,String fileType,String crud){
        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
//        Log.v("gift",giftContent+"||"+giftName+"||"+owner+"||"+giftType);
        //------------------------------上傳禮物檔案
        giftInsertImg_imageAsyncTask mGiftInsertImgAsyncTask = new giftInsertImg_imageAsyncTask(new giftInsertImg_imageAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        }, ImageFilePath.getPath(context, cam_imageUri));
        mGiftInsertImgAsyncTask.execute(Common.insertGiftUploadGiftFile,giftContent,fileType);

        //------------------------------上傳禮物資訊
        giftInsertImg_infoAsyncTask mGiftInsertImg_infoAsyncTask = new giftInsertImg_infoAsyncTask(new giftInsertImg_infoAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        mGiftInsertImg_infoAsyncTask.execute(Common.insertGiftUploadGiftFile, giftContent, old_giftContent,owner,fileType,crud);
    }
}
