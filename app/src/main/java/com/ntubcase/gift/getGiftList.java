package com.ntubcase.gift;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.getterAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class getGiftList {

    public  static String gift[];
    public  static String date[];
    public  static String giftName[];
    public  static String ownerid[];
    public  static String type[];
    private static int jslen = 0 ;

    public static void getJSON() {

        getterAsyncTask myAsyncTask = new getterAsyncTask(new getterAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);

                    JSONArray jsonArray = object.getJSONArray("result");

                    Log.v("length",jsonArray.length()+"");

                    jslen = jsonArray.length();

                    for (int i = 0 ; i <jsonArray.length() ; i++){
                        Log.v("abc",
                                "10000");
                        gift[i] = jsonArray.getJSONObject(i).getString("gift");
                        date[i] = jsonArray.getJSONObject(i).getString("date");
                        giftName[i] = jsonArray.getJSONObject(i).getString("giftName");
                        ownerid[i] = jsonArray.getJSONObject(i).getString("ownerid");
                        type[i] = jsonArray.getJSONObject(i).getString("type");
                        Log.v("abc",
                                jsonArray.getJSONObject(i).getString("gift"));
                        Log.v("abc",
                                date[i]);
                        Log.v("abc",
                                giftName[i]);
                        Log.v("abc",
                                ownerid[i]);
                        Log.v("abc",
                                type[i]);

                    }
                } catch (Exception e) {
                }
            }
        });
        myAsyncTask.execute(Common.giftList);
    }
}
