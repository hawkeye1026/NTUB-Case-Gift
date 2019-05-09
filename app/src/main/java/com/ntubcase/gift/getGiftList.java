package com.ntubcase.gift;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.getterAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class getGiftList {

    public  static String[] gift = new String[100];
    public  static String[] date = new String[100];
    public  static String[] giftName= new String[100];
    public  static String[] ownerid= new String[100];
    public  static String[] type= new String[100];
    private static int giftLength = 0 ;

    public static void getJSON() {

        getterAsyncTask myAsyncTask = new getterAsyncTask(new getterAsyncTask.TaskListener() {

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
                        gift[i] = jsonArray.getJSONObject(i).getString("gift");
                        date[i] = jsonArray.getJSONObject(i).getString("date");
                        giftName[i] = jsonArray.getJSONObject(i).getString("giftName");
                        ownerid[i] = jsonArray.getJSONObject(i).getString("ownerid");
                        type[i] = jsonArray.getJSONObject(i).getString("type");

                        switch(type[i]){
                            case "0":
                                type[i] = "照片";
                            case "1":
                                type[i] = "影片";
                            case "2":
                                type[i] = "兌換券";
                        }

                    }
                } catch (Exception e) {
                }
            }
        });
        myAsyncTask.execute(Common.giftList);
    }

    public static int getGiftLength(){
        return giftLength;
    }
    public static String getType(int i){
        Log.v("abc",
                type[i]);
        return type[i];
    }
    public static String getGiftName(int i){
        return giftName[i];
    }
}
