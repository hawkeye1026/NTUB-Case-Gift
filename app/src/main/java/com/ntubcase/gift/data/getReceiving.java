package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.DateFormat;
import com.ntubcase.gift.MyAsyncTask.receive.receiveNewAsyncTask;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONArray;
import org.json.JSONObject;

public class getReceiving {
    private static String[] ownerid;
    private static String[] receiverid;
    private static String[] nickname;
    private static String[] planid;
    private static String[] planType;
    private static String[] planName;
    private static String[] sendPlanDate;
    //------已完成的planid
    private static String[] comPlaid;
    //------是否完成
    private static String[] complete;

    private static String[] misComid;
    private static String[] misComplete;

    private static int receiveOpenLength = 0 ;
    private static int completeLength = 0 ;
    private static int misComLength = 0 ;

    public static void getJSON() {

        receiveNewAsyncTask myAsyncTask = new receiveNewAsyncTask(new receiveNewAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("result");

                    //Log.v("plength",jsonArray.length()+"");
                    receiveOpenLength = jsonArray.length();

                    ownerid = new String[receiveOpenLength];
                    receiverid = new String[receiveOpenLength];
                    nickname = new String[receiveOpenLength];
                    planid = new String[receiveOpenLength];
                    planType = new String[receiveOpenLength];
                    planName = new String[receiveOpenLength];
                    sendPlanDate = new String[receiveOpenLength];

                    for (int i = 0 ; i <receiveOpenLength ; i++){
                        //Log.v("abc", "10000");
                        ownerid[i] = jsonArray.getJSONObject(i).getString("ownerid");
                        receiverid[i] = jsonArray.getJSONObject(i).getString("receiverid");
                        nickname[i] = jsonArray.getJSONObject(i).getString("nickname");
                        planid[i] = jsonArray.getJSONObject(i).getString("planid");
                        planType[i] = jsonArray.getJSONObject(i).getString("planType");
                        planName[i] = jsonArray.getJSONObject(i).getString("planName");
                        sendPlanDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("sendPlanDate"));


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
                    jsonArray = object.getJSONArray("misCom");
                    misComLength = jsonArray.length();

                    misComid = new String[misComLength];
                    misComplete = new String[misComLength];

                    for (int i = 0 ; i <misComLength ; i++) {
                        //Log.v("abc", "10000");
                        misComid[i] = jsonArray.getJSONObject(i).getString("misid");
                        misComplete[i] = jsonArray.getJSONObject(i).getString("complete");

                    }
                    //--------取得禮物是否能夠點選收禮完成
                    jsonArray = object.getJSONArray("deadline");

                    completeLength = jsonArray.length();

                    comPlaid = new String[completeLength];
                    complete = new String[completeLength];

                    for (int i = 0 ; i <completeLength ; i++) {
                        //Log.v("abc", "10000");

                        comPlaid[i] = jsonArray.getJSONObject(i).getString("planid");
                        complete[i] = jsonArray.getJSONObject(i).getString("complete");
                        Log.v("checkcomplete",comPlaid[i]);
                        Log.v("checkcomplete",complete[i]);
                        for(int j = 0 ; j < misComLength; j++){
                            if(misComid[j].equals(comPlaid[i]) && misComplete[j].equals("1")){
                                complete[i] = "1";
                            }else{
                                complete[i] = jsonArray.getJSONObject(i).getString("complete");
                            }
                        }
                    }




                } catch (Exception e) {
                }
            }
        });
        myAsyncTask.execute(Common.receiving, userData.getUserID());
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
    public static int getReceivingLength(){
        return receiveOpenLength;
    }
    //---------------
    public static String getComPlanid(int i){
        return comPlaid[i];
    }
    public static String getComplete(int i){
        return complete[i];
    }
    public static int getComLength(){
        return completeLength;
    }
}
