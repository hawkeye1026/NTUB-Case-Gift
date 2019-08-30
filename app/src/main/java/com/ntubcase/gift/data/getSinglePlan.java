package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.DateFormat;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.planningListAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class getSinglePlan {
    private static String[] senderid;
    private static String[] planid;
    private static String[] receiverid;

    private static int singlePlanLength = 0 ;

    public static void getJSON() {

        planDetailAsyncTask myAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("result");

                    Log.v("4.getSinglePlan",result);

                    //Log.v("plength",jsonArray.length()+"");
                    singlePlanLength = jsonArray.length();

                    senderid = new String[singlePlanLength];
                    planid = new String[singlePlanLength];
                    receiverid = new String[singlePlanLength];

                    for (int i = 0 ; i <jsonArray.length() ; i++){
                        //Log.v("abc", "10000");
                        senderid[i] = jsonArray.getJSONObject(i).getString("senderid");
                        planid[i] = jsonArray.getJSONObject(i).getString("planid");
                        receiverid[i] = jsonArray.getJSONObject(i).getString("receiverid");
                        //sendPlanDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("sendPlanDate"));

                        Log.v("6.receiverid",receiverid[i]);
                        //Log.v("pdata",spPlanName[i]);
                    }

                } catch (Exception e) {
                }
            }
        });
        Log.v("5.","sin_20190825005416");
        myAsyncTask.execute(Common.singlePlan, "1", "sin_20190825005416");
    }

    public static String getSenderid(int i){
        return senderid[i];
    }
    public static String getPlanid(int i){
        return planid[i];
    }
    public static String getReceiverid(int i){
        return receiverid[i];
    }
    public static int getsinglePlanLength(){
        return singlePlanLength;
    }
}
