package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.giftListAsyncTask;
import com.ntubcase.gift.DateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

public class  getGiftList {
    //-------禮物
    private static String[] giftid        ;
    private static String[] gift          ;
    private static String[] giftCreateDate;
    private static String[] giftName      ;
    private static String[] ownerid       ;
    private static String[] type          ;
    //-------解碼內容
    private static String[] decodeGift          ;
    private static String[] decodeMainCode          ;
    private static String[] decodeMatchCode          ;
    private static int giftLength = 0 ;
    private static int decodeLength = 0 ;

    public static void getJSON() {

        giftListAsyncTask myAsyncTask = new giftListAsyncTask(new giftListAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    //取得禮物資料
                    JSONArray jsonArray = object.getJSONArray("result");

                    Log.v("length",jsonArray.length()+"");

                    giftLength = jsonArray.length();

                    giftid         = new String[giftLength];
                    gift           = new String[giftLength];
                    giftCreateDate = new String[giftLength];
                    giftName       = new String[giftLength];
                    ownerid        = new String[giftLength];
                    type           = new String[giftLength];

                    for (int i = 0 ; i <jsonArray.length() ; i++){
                        //Log.v("abc","10000");
                        giftid[i] = jsonArray.getJSONObject(i).getString("giftid");
                        gift[i] = jsonArray.getJSONObject(i).getString("gift");
                        giftCreateDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("giftCreateDate"));
                        giftName[i] = jsonArray.getJSONObject(i).getString("giftName");
                        ownerid[i] = jsonArray.getJSONObject(i).getString("ownerid");
                        type[i] = jsonArray.getJSONObject(i).getString("type");
                    }

                    //取得解碼表資料
                    jsonArray = object.getJSONArray("decode");
                    decodeLength = jsonArray.length();

                    decodeGift      = new String[giftLength];
                    decodeMainCode  = new String[giftLength];
                    decodeMatchCode = new String[giftLength];
                    Log.v("strArr", decodeLength+"");
                    for (int i = 0 ; i < decodeLength ; i++){
                        //Log.v("abc","10000");
                        decodeGift[i] = jsonArray.getJSONObject(i).getString("gift");
                        decodeMainCode[i] = jsonArray.getJSONObject(i).getString("mainCode");
                        decodeMatchCode[i] = jsonArray.getJSONObject(i).getString("matchCode");
                        Log.v("strArr", decodeMainCode[i]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        myAsyncTask.execute(Common.giftList);
    }
    //---------禮物資料
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
    //---------解碼表資料
    public static String getDecodeGift(int i){
        return  gift[i];
    }
    public static String getDecodeMaincode(int i){
        return decodeMainCode[i];
    }
    public static String getDecodeMatchCode(int i){
        return decodeMatchCode[i];
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
