package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.giftListAsyncTask;
import com.ntubcase.gift.dateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class getGiftList {

    private static String[] giftid = new String[100];
    private static String[] gift = new String[100];
    private static String[] giftCreateDate = new String[100];
    private static String[] giftName= new String[100];
    private static String[] ownerid= new String[100];
    private static String[] type= new String[100];
    private static int giftLength = 0 ;

    public static void getJSON() {

        giftListAsyncTask myAsyncTask = new giftListAsyncTask(new giftListAsyncTask.TaskListener() {

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
                        giftid[i] = jsonArray.getJSONObject(i).getString("giftid");
                        gift[i] = jsonArray.getJSONObject(i).getString("gift");
                        giftCreateDate[i] = dateFormat.dateFormat(jsonArray.getJSONObject(i).getString("giftCreateDate"));
                        giftName[i] = jsonArray.getJSONObject(i).getString("giftName");
                        ownerid[i] = jsonArray.getJSONObject(i).getString("ownerid");
                        type[i] = jsonArray.getJSONObject(i).getString("type");
                        Log.v("giftid",giftid[i]);
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
                    Log.v("strArr", Arrays.toString(getGiftList.getGiftNmaeArr()));
                } catch (Exception e) {
                }
            }
        });
        myAsyncTask.execute(Common.giftList);
    }

    public static int getGiftLength(){
        return giftLength;
    }
    public static String getGiftid(int i){
        return  giftid[i];
    }
    public static String getGift(int i){
        return  gift[i];
    }
    public static String getType(int i){
        return type[i];
    }
    public static String getGiftName(int i){
        return giftName[i];
    }
    public static String getGiftCreateDate(int i){
        return giftCreateDate[i];
    }
    public static String getOwnerid(int i){
        return ownerid[0];
    }
    public static String[] getGiftNmaeArr(){
        return giftName;
    }
}
