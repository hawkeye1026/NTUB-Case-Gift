package com.ntubcase.gift;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.insert.giftInsertAsyncTask;
import com.ntubcase.gift.MyAsyncTask.gift.insert.giftInsertCodeAsyncTask;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class uploadGift {

    private static String lastGiftID ="0";

    public uploadGift(String giftContent, String giftName, String owner, String giftType){
        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateTime = sdFormat.format(date);
        //------------------------------上傳禮物資料
        giftInsertAsyncTask mgiftInsertAsyncTask = new giftInsertAsyncTask(new giftInsertAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result){
                try{
                    if(result==null){ //伺服器連線失敗跳維修頁
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("result");

                    for (int i = 0 ; i <jsonArray.length() ; i++) {
                        //Log.v("abc","10000");
                        //取得禮物資料
                        lastGiftID = jsonArray.getJSONObject(i).getString("lastID");

                        Log.v("uplastGiftID",lastGiftID);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        mgiftInsertAsyncTask.execute(Common.insertGift, giftContent, dateTime, giftName, owner, giftType);
    }
    public uploadGift(String giftName, String owner, String giftType, final String rowNumber, final String maincode_array, final String matchcode_array){
        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateTime = sdFormat.format(date);
        //------------------------------上傳禮物資料
        giftInsertAsyncTask mgiftInsertAsyncTask = new giftInsertAsyncTask(new giftInsertAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result){
                try{
                    if(result==null){ //伺服器連線失敗跳維修頁
                        return;
                    }
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("result");

                    for (int i = 0 ; i <jsonArray.length() ; i++) {
                        //Log.v("abc","10000");
                        //取得禮物資料
                        lastGiftID = jsonArray.getJSONObject(i).getString("lastID");

                        Log.v("uplastGiftID",lastGiftID);
                    }

                    //--------上傳code
                    giftInsertCodeAsyncTask mgiftInsertCodAsyncTask = new giftInsertCodeAsyncTask(new giftInsertCodeAsyncTask.TaskListener() {
                        @Override
                        public void onFinished(String result) {

                        }
                    });
                    // Log.v("MuplastGiftID",uploadGift.getLastGiftid());
                    mgiftInsertCodAsyncTask.execute(Common.insertGiftCode,lastGiftID, rowNumber, maincode_array , matchcode_array);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        mgiftInsertAsyncTask.execute(Common.insertGift, "", dateTime, giftName, owner, giftType);

    }

    public static String getLastGiftID(){
        return lastGiftID;
    }
}
