package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.giftListAsyncTask;
import com.ntubcase.gift.dateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class getFriendList {

    private static String[] friendName = new String[1000];
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
                        friendName[i] = jsonArray.getJSONObject(i).getString("friendName");
                        Log.v("friName",friendName[i]);
                    }
                } catch (Exception e) {
                }
            }
        });
        myAsyncTask.execute(Common.friendList,"1");
    }

    public static int getGiftLength(){
        return giftLength;
    }
    public static String getGift(int i){
        return  friendName[i];
    }
}
