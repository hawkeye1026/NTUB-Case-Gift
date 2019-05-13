package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.giftDownloadAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class getPlanList {

    public  static String[] spid          = new String[100];
    public  static String[] giftid        = new String[100];
    public  static String[] sendGiftDate  = new String[100];
    public  static String[] spPlanName    = new String[100];
    public  static String[] spCreateDate  = new String[100];
    public  static String[] message       = new String[100];
    public  static String[] gift          = new String[100];
    public  static String[] giftName      = new String[100];
    public  static String[] giftCreateDate= new String[100];
    public  static String[] ownerid       = new String[100];
    public  static String[] type          = new String[100];
    private static int planLength = 0 ;

    public static void getJSON() {

        giftDownloadAsyncTask myAsyncTask = new giftDownloadAsyncTask(new giftDownloadAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);

                    JSONArray jsonArray = object.getJSONArray("result");

                    Log.v("plength",jsonArray.length()+"");

                    planLength = jsonArray.length();

                    for (int i = 0 ; i <jsonArray.length() ; i++){
                        Log.v("abc",
                                "10000");
                        spid[i]           = jsonArray.getJSONObject(i).getString("spid");
                        giftid[i]         = jsonArray.getJSONObject(i).getString("giftid");
                        sendGiftDate[i]   = jsonArray.getJSONObject(i).getString("sendGiftDate");
                        spPlanName[i]     = jsonArray.getJSONObject(i).getString("spPlanName");
                        spCreateDate[i]   = jsonArray.getJSONObject(i).getString("spCreateDate");
                        message[i]        = jsonArray.getJSONObject(i).getString("message");
                        gift[i]           = jsonArray.getJSONObject(i).getString("gift");
                        giftName[i]       = jsonArray.getJSONObject(i).getString("giftName");
                        giftCreateDate[i] = jsonArray.getJSONObject(i).getString("giftCreateDate");
                        ownerid[i]        = jsonArray.getJSONObject(i).getString("ownerid");
                        type[i]           = jsonArray.getJSONObject(i).getString("type");

                        switch(type[i]){
                            case "1":
                                type[i] = "照片";
                            case "2":
                                type[i] = "影片";
                            case "3":
                                type[i] = "兌換券";
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
        myAsyncTask.execute(Common.giftList);
    }

    public static int planLength(){
        return planLength;
    }
    public static String getspid(int i){
        return  spid[i];
    }
    /*
    spid[i]           = jsonArray.getJSONObject(i).getString("spid");
    giftid[i]         = jsonArray.getJSONObject(i).getString("giftid");
    sendGiftDate[i]   = jsonArray.getJSONObject(i).getString("sendGiftDate");
    spPlanName[i]     = jsonArray.getJSONObject(i).getString("spPlanName");
    spCreateDate[i]   = jsonArray.getJSONObject(i).getString("spCreateDate");
    message[i]        = jsonArray.getJSONObject(i).getString("message");
    gift[i]           = jsonArray.getJSONObject(i).getString("gift");
    giftName[i]       = jsonArray.getJSONObject(i).getString("giftName");
    giftCreateDate[i] = jsonArray.getJSONObject(i).getString("giftCreateDate");
    ownerid[i]        = jsonArray.getJSONObject(i).getString("ownerid");
    type[i]           = jsonArray.getJSONObject(i).getString("type");
    */
    public static String getGiftid(int i){
        return  giftid[i];
    }
    public static String getSendGiftDate(int i){
        return  sendGiftDate[i];
    }
    public static String getSpPlanName(int i){
        return  spPlanName[i];
    }
    public static String getSpCreateDate(int i){
        return  spCreateDate[i];
    }
    public static String getmessage(int i){
        return  message[i];
    }
    public static String getGift(int i){
        return  gift[i];
    }
    public static String getGiftName(int i){
        return giftName[i];
    }
    public static String getgiftCreateDate(int i){
        return giftCreateDate[i];
    }
    public static String getOwnerid(){
        return ownerid[0];
    }
    public static String getType(int i){
        return type[i];
    }
}
