package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.friendListAsyncTask;
import com.ntubcase.gift.MyAsyncTask.giftListAsyncTask;
import com.ntubcase.gift.dateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class getFriendList {

    private static String[] friendid   ;
    private static String[] friendName ;
    private static String[] friendMail ;
    private static int friendLength = 0 ;

    public static void getJSON() {

        friendListAsyncTask myAsyncTask = new friendListAsyncTask(new friendListAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);

                    JSONArray jsonArray = object.getJSONArray("result");

                    Log.v("Flength",jsonArray.length()+"");

                    friendLength = jsonArray.length();
                    friendid   = new String[friendLength];
                    friendName = new String[friendLength];
                    friendMail = new String[friendLength];

                    for (int i = 0 ; i <friendLength ; i++){
                        friendid[i] = jsonArray.getJSONObject(i).getString("friendid");
                        friendName[i] = jsonArray.getJSONObject(i).getString("nickname");
                        friendMail[i] = jsonArray.getJSONObject(i).getString("email");
                    }
                } catch (Exception e) {
                }
            }
        });
        myAsyncTask.execute(Common.friendList,"1");
    }

    public static int getFriendLength(){
        return friendLength;
    }
    public static String getFriendName(int i){
        return friendName[i];
    }
    public static String getFriendid(int i){
        return friendid[i];
    }
    public static String getFriendMail(int i){
        return friendMail[i];
    }
}
