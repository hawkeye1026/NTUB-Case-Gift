package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.giftReceivedNewAsyncTask;
import com.ntubcase.gift.DateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class getGiftReceived {

    private  static String[] planid     ;
    private  static String[] senderid   ;
    private  static String[] nickname   ;
    private  static String[] planName   ;
    private  static String[] sendPlanDate;
    private  static String[] type       ;
    private static int giftLength = 0 ;

    public static void getJSON() {

        giftReceivedNewAsyncTask myAsyncTask = new giftReceivedNewAsyncTask(new giftReceivedNewAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);

                    JSONArray jsonArray = object.getJSONArray("result");

                    Log.v("length",jsonArray.length()+"");

                    giftLength = jsonArray.length();

                    planid       = new String[giftLength];
                    senderid     = new String[giftLength];
                    nickname     = new String[giftLength];
                    planName     = new String[giftLength];
                    sendPlanDate = new String[giftLength];
                    type         = new String[giftLength];

                    for (int i = 0 ; i <jsonArray.length() ; i++){
//                        Log.v("abc","10000");
                        planid[i] = jsonArray.getJSONObject(i).getString("planid");
                        senderid[i] = jsonArray.getJSONObject(i).getString("senderid");
                        nickname[i] = jsonArray.getJSONObject(i).getString("nickname");
                        planName[i] = jsonArray.getJSONObject(i).getString("planName");
                        sendPlanDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("sendPlanDate"));
                        type[i] = jsonArray.getJSONObject(i).getString("type");

                        switch(type[i]){
                            case "1":
                                type[i] = "驚喜式";
                                Log.v("驚喜式",
                                        type[i]);
                                break;
                            case "2":
                                type[i] = "期間式";
                                Log.v("期間式",
                                        type[i]);
                                break;
                            case "3":
                                type[i] = "問答式";
                                Log.v("問答式",
                                        type[i]);
                                break;
                            default:
                                type[i] = "驚喜式";
                        }

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
        return planid[i];
    }
    public static String getSenderid(int i){
        return senderid[i];
    }
    public static String getNickname(int i){
        return nickname[i];
    }
    public static String getPlanName(int i){
        return planName[i];
    }
    public static String getSendPlanDate(int i){
        return sendPlanDate[i];
    }
    public static String getType(int i){
        return type[i];
    }
}
