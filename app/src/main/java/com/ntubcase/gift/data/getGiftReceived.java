package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.getReceivedAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class getGiftReceived {
    public  static String[] planid = new String[100];
    public  static String[] senterid = new String[100];
    public  static String[] planName= new String[100];
    public  static String[] sendPlanDate= new String[100];
    //public  static String[] type= new String[100];
    private static int giftLength = 0 ;

    public static void getJSON() {

        getReceivedAsyncTask myAsyncTask = new getReceivedAsyncTask(new getReceivedAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);

                    JSONArray jsonArray = object.getJSONArray("result");

                    Log.v("length",jsonArray.length()+"");

                    giftLength = jsonArray.length();

                    for (int i = 0 ; i <jsonArray.length() ; i++){
                        Log.v("abc",
                                "10000");
                        planid[i] = jsonArray.getJSONObject(i).getString("planid");
                        senterid[i] = jsonArray.getJSONObject(i).getString("senterid");
                        planName[i] = jsonArray.getJSONObject(i).getString("planName");
                        sendPlanDate[i] = jsonArray.getJSONObject(i).getString("sendPlanDate");
                        //type[i] = jsonArray.getJSONObject(i).getString("type");
/*
                        switch(type[i]){
                            case "1":
                                type[i] = "照片";
                            case "2":
                                type[i] = "影片";
                            case "3":
                                type[i] = "兌換券";
                        }
*/
                    }
                } catch (Exception e) {
                }
            }
        });
        myAsyncTask.execute(Common.giftReceived);
    }

    public static int getGiftLength(){
        return giftLength;
    }
    public static String getPlanid(int i){
        return  planid[i];
    }
    public static String getSenterid(int i){
        return senterid[i];
    }
    public static String getPlanName(int i){
        return planName[i];
    }
    public static String getSendPlanDate(int i){
        return sendPlanDate[i];
    }
    /*
    public static String getOwnerid(){
        return ownerid[0];
    }
    */
}
