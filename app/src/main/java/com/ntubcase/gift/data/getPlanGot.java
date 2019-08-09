package com.ntubcase.gift.data;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.plan.planningListAsyncTask;
import com.ntubcase.gift.dateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class getPlanGot {

    private static String[] senderid;
    private static String[] planid;
    private static String[] planType;
    private static String[] planName;
    private static String[] createDate;
    private static String[] sendPlanDate;

    private static int plangotLength = 0 ;

    public static void getJSON() {

        planningListAsyncTask myAsyncTask = new planningListAsyncTask(new planningListAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("result");

                    //Log.v("plength",jsonArray.length()+"");
                    plangotLength = jsonArray.length();

                    senderid = new String[plangotLength];
                    planid = new String[plangotLength];
                    planType = new String[plangotLength];
                    planName = new String[plangotLength];
                    createDate = new String[plangotLength];
                    sendPlanDate = new String[plangotLength];

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
        myAsyncTask.execute(Common.planGot, "1");
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
    public static int getPlangotgLength(){
        return plangotLength;
    }
}
