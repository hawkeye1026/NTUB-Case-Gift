package com.ntubcase.gift.data;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.DateFormat;
import com.ntubcase.gift.MyAsyncTask.receive.receiveNewAsyncTask;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONArray;
import org.json.JSONObject;

public class getReceiveNew {
    private static String[] ownerid;
    private static String[] receiverid;
    private static String[] nickname;
    private static String[] planid;
    private static String[] planType;
    private static String[] planName;
    private static String[] sendPlanDate;

    private static int receiveNewLength = 0 ;

    public static void getJSON() {

        receiveNewAsyncTask myAsyncTask = new receiveNewAsyncTask(new receiveNewAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("result");

                    //Log.v("plength",jsonArray.length()+"");
                    receiveNewLength = jsonArray.length();

                    ownerid = new String[receiveNewLength];
                    receiverid = new String[receiveNewLength];
                    nickname = new String[receiveNewLength];
                    planid = new String[receiveNewLength];
                    planType = new String[receiveNewLength];
                    planName = new String[receiveNewLength];
                    sendPlanDate = new String[receiveNewLength];

                    for (int i = 0 ; i <jsonArray.length() ; i++){
                        //Log.v("abc", "10000");
                        ownerid[i] = jsonArray.getJSONObject(i).getString("ownerid");
                        receiverid[i] = jsonArray.getJSONObject(i).getString("receiverid");
                        nickname[i] = jsonArray.getJSONObject(i).getString("nickname");
                        planid[i] = jsonArray.getJSONObject(i).getString("planid");
                        planType[i] = jsonArray.getJSONObject(i).getString("planType");
                        planName[i] = jsonArray.getJSONObject(i).getString("planName");
                        sendPlanDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("sendPlanDate"));

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
        myAsyncTask.execute(Common.receiveNew, userData.getUserID());
    }

    public static String getOwnerid(int i){
        return ownerid[i];
    }
    public static String getReceiverid(int i){
        return receiverid[i];
    }
    public static String getNickname(int i){
        return nickname[i];
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
    public static String getSendPlanDate(int i){
        return sendPlanDate[i];
    }
    public static int getReceiveNewLength(){
        return receiveNewLength;
    }
}
