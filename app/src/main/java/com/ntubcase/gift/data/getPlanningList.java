package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planningListAsyncTask;
import com.ntubcase.gift.dateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class getPlanningList {

    private static String[] senderid;
    private static String[] planid;
    private static String[] planType;
    private static String[] planName;
    private static String[] createDate;
    private static String[] sendPlanDate;

    private static int planningLength = 0 ;

    public static void getJSON() {

        planningListAsyncTask myAsyncTask = new planningListAsyncTask(new planningListAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("result");

                    //Log.v("plength",jsonArray.length()+"");
                    planningLength = jsonArray.length();

                    senderid = new String[planningLength];
                    planid = new String[planningLength];
                    planType = new String[planningLength];
                    planName = new String[planningLength];
                    createDate = new String[planningLength];
                    sendPlanDate = new String[planningLength];

                    for (int i = 0 ; i <jsonArray.length() ; i++){
                        //Log.v("abc", "10000");
                        senderid[i] = jsonArray.getJSONObject(i).getString("senderid");
                        planid[i] = jsonArray.getJSONObject(i).getString("planid");
                        planType[i] = jsonArray.getJSONObject(i).getString("planType");
                        planName[i] = jsonArray.getJSONObject(i).getString("planName");
                        createDate[i] = dateFormat.dateFormat(jsonArray.getJSONObject(i).getString("createDate"));
                        sendPlanDate[i] = dateFormat.dateFormat(jsonArray.getJSONObject(i).getString("sendPlanDate"));

                        //Log.v("pdata",sendGiftDate[i]);
                        //Log.v("pdata",spPlanName[i]);

                        switch(planType[i]){
                            case "1":
                                planType[i] = "單日送禮";
                                break;
                            case "2":
                                planType[i] = "多日規劃";
                                break;
                            case "3":
                                planType[i] = "任務清單";
                                break;
                        }
                    }

                } catch (Exception e) {
                }
            }
        });
        myAsyncTask.execute(Common.planningList, "1");
    }

    public static String getSenderid(int i){
        return senderid[i];
    }
    public static String getPlanid(int i){
        return planid[i];
    }
    public static String getPlanType(int i){
        return planType[i];
    }
    public static String getPlanName(int i){
        return planName[i];
    }
    public static String getCreateDate(int i){
        return createDate[i];
    }
    public static String getSendPlanDate(int i){
        return sendPlanDate[i];
    }
    public static int getPlanningLength(){
        return planningLength;
    }
}
