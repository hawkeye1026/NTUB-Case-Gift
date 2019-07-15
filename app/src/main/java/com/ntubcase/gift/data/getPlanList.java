package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.giftListAsyncTask;
import com.ntubcase.gift.MyAsyncTask.planListAsyncTask;
import com.ntubcase.gift.dateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class getPlanList {

    private static String[] spid          ;
    private static String[] giftid        ;
    private static String[] sendGiftDate  ;
    private static String[] spPlanName    ;
    private static String[] spCreateDate  ;
    private static String[] message       ;
    private static String[] gift          ;
    private static String[] giftName      ;
    private static String[] giftCreateDate;
    private static String[] ownerid       ;
    private static String[] type          ;
    private static String[] planType      ;

    private static int planLength = 0 ;

    public static void getJSON() {

        planListAsyncTask myAsyncTask = new planListAsyncTask(new planListAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);

                    JSONArray jsonArray = object.getJSONArray("result");

//                    Log.v("plength",jsonArray.length()+"");
                    planLength = jsonArray.length();

                    spid           = new String[planLength];
                    giftid         = new String[planLength];
                    sendGiftDate   = new String[planLength];
                    spPlanName     = new String[planLength];
                    spCreateDate   = new String[planLength];
                    message        = new String[planLength];
                    gift           = new String[planLength];
                    giftName       = new String[planLength];
                    giftCreateDate = new String[planLength];
                    ownerid        = new String[planLength];
                    type           = new String[planLength];
                    planType       = new String[planLength];


                    for (int i = 0 ; i <jsonArray.length() ; i++){
                        //Log.v("abc", "10000");
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
                        //Log.v("pdata",sendGiftDate[i]);
                        //Log.v("pdata",spPlanName[i]);

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

    public static int getPlanLength(){
        return planLength;
    }
    public static String getSpPlanid(int i){
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
