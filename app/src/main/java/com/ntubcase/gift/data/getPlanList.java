package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.giftListAsyncTask;
import com.ntubcase.gift.MyAsyncTask.planListAsyncTask;
import com.ntubcase.gift.dateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class getPlanList {

    private static String[] spid          = new String[100];
    private static String[] giftid         = new String[100];
    private static String[] sendGiftDate   = new String[100];
    private static String[] spPlanName     = new String[100];
    private static String[] spCreateDate   = new String[100];
    private static String[] message        = new String[100];
    private static String[] gift           = new String[100];
    private static String[] giftName       = new String[100];
    private static String[] giftCreateDate = new String[100];
    private static String[] ownerid        = new String[100];
    private static String[] type           = new String[100];
    private static String[] planType        = new String[100];

    private static int planLength = 0 ;

    public static void getJSON() {

        planListAsyncTask myAsyncTask = new planListAsyncTask(new planListAsyncTask.TaskListener() {

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
                        sendGiftDate[i]   = dateFormat.dateFormat(jsonArray.getJSONObject(i).getString("sendGiftDate"));
                        spPlanName[i]     = jsonArray.getJSONObject(i).getString("spPlanName");
                        spCreateDate[i]   = dateFormat.dateFormat(jsonArray.getJSONObject(i).getString("spCreateDate"));
                        message[i]        = jsonArray.getJSONObject(i).getString("message");
                        gift[i]           = jsonArray.getJSONObject(i).getString("gift");
                        giftName[i]       = jsonArray.getJSONObject(i).getString("giftName");
                        giftCreateDate[i] = dateFormat.dateFormat(jsonArray.getJSONObject(i).getString("giftCreateDate"));
                        ownerid[i]        = jsonArray.getJSONObject(i).getString("ownerid");
                        type[i]           = jsonArray.getJSONObject(i).getString("type");
                        planType[i]       = "驚喜式";
                        Log.v("pdata",sendGiftDate[i]);
                        Log.v("pdata",spPlanName[i]);

                        switch(type[i]){
                            case "1":
                                type[i] = "照片";
                                break;
                            case "2":
                                type[i] = "影片";
                                break;
                            case "3":
                                type[i] = "兌換券";
                                break;
                        }
                    }

                } catch (Exception e) {
                }
            }
        });
        myAsyncTask.execute(Common.planList);
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
    public static String getMessage(int i){
        return  message[i];
    }
    public static String getGift(int i){
        return  gift[i];
    }
    public static String getGiftName(int i){
        return giftName[i];
    }
    public static String getGiftCreateDate(int i){
        return giftCreateDate[i];
    }
    public static String getOwnerid(){
        return ownerid[0];
    }
    public static String getType(int i){
        return type[i];
    }
    public static String getPlanType(int i){
        return planType[i];
    }
}
