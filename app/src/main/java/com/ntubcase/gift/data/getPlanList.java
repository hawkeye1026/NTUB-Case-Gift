package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.DateFormat;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class getPlanList {

    //--giftRecord
    private static String[] recordid;
    private static String[] senderid;
    private static String[] receiverid;
    private static String[] planid;
    private static String[] store;
    private static String[] feedback;
    private static String[] openDate;
    private static String[] planType;
    private static String[] nickname;
    //--singlePlan
    private static String[] sinPlanid;
    private static String[] sinPlanName;
    private static String[] sinCreateDate;
    private static String[] sinSendPlanDate;
    //--singleList
    private static String[] sinListid;
    private static String[] sinGiftid;
    private static String[] sinSendGiftDate;
    private static String[] sinMessage;
    //--multiplePlan
    private static String[] mulPlanid;
    private static String[] mulPlanName;
    private static String[] mulCreateDate;
    private static String[] mulStartDate;
    private static String[] mulEndDate;
    private static String[] mulMessage;
    //--multipleList
    private static String[] mulListid;
    private static String[] mulGiftid;
    private static String[] mulSendGiftDate;
    private static String[] mulGoal;
    //--missionPlan
    private static String[] misPlanid;
    private static String[] misPlanName;
    private static String[] misCreateDate;
    private static String[] misSendPlanDate;
    private static String[] misDeadline;
    private static String[] misGetGiftDate;
    //--missionList
    private static String[] misListid;
    private static String[] misGiftid;
    //--missionItem
    private static String[] misItemid;
    private static String[] misContent;
    //--gift
    private static String[] giftid;
    private static String[] gift;
    private static String[] giftCreateDate;
    private static String[] giftName;
    private static String[] ownerid;
    private static String[] type;

    private static int giftLength = 0 ;
    private static int recordLength = 0 ;
    private static int sinPlanLength = 0 ;
    private static int sinListLength = 0 ;
    private static int mulPlanLength = 0 ;
    private static int mulListLength = 0 ;
    private static int misPlanLength = 0 ;
    private static int misListLength = 0 ;
    private static int misItemLength = 0 ;

    public static void getJSON(String userid, String idPlan) {

        planDetailAsyncTask myAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {

            @Override
            public void onFinished(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    Log.v("result", result);

                    //取得禮物資料
                    JSONArray jsonArray = object.getJSONArray("result");
                    giftLength = jsonArray.length();
                    Log.v("giftLength", String.valueOf(giftLength));

                    giftid = new String[giftLength];
                    gift = new String[giftLength];
                    giftCreateDate = new String[giftLength];
                    giftName = new String[giftLength];
                    ownerid = new String[giftLength];
                    type = new String[giftLength];

                    for (int i = 0 ; i <jsonArray.length() ; i++){
                        //Log.v("abc", "10000");
                        giftid[i] = jsonArray.getJSONObject(i).getString("giftid");
                        gift[i] = jsonArray.getJSONObject(i).getString("gift");
                        giftCreateDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("giftCreateDate"));
                        giftName[i] = jsonArray.getJSONObject(i).getString("giftName");
                        ownerid[i] = jsonArray.getJSONObject(i).getString("ownerid");
                        type[i] = jsonArray.getJSONObject(i).getString("type");

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

                    //取得禮物紀錄
                    jsonArray = object.getJSONArray("record");
                    recordLength = jsonArray.length();
                    Log.v("recordLength", String.valueOf(recordLength));

                    senderid      = new String[recordLength];
                    receiverid  = new String[recordLength];
                    planid = new String[recordLength];
                    store = new String[recordLength];
                    feedback = new String[recordLength];
                    openDate = new String[recordLength];
                    planType = new String[recordLength];
                    nickname = new String[recordLength];

                    for (int i = 0 ; i < recordLength ; i++){
                        //Log.v("abc","10000");
                        senderid[i] = jsonArray.getJSONObject(i).getString("senderid");
                        receiverid[i] = jsonArray.getJSONObject(i).getString("receiverid");
                        planid[i] = jsonArray.getJSONObject(i).getString("planid");
                        store[i] = jsonArray.getJSONObject(i).getString("store");
                        feedback[i] = jsonArray.getJSONObject(i).getString("feedback");
                        openDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("openDate"));
                        planType[i] = jsonArray.getJSONObject(i).getString("planType");
                        nickname[i] = jsonArray.getJSONObject(i).getString("nickname");
                    }

                    //取得單日計畫
                    jsonArray = object.getJSONArray("sinPlan");
                    sinPlanLength = jsonArray.length();
                    Log.v("sinPlanLength", String.valueOf(sinPlanLength));

                    sinPlanid = new String[sinPlanLength];
                    sinPlanName = new String[sinPlanLength];
                    sinCreateDate = new String[sinPlanLength];
                    sinSendPlanDate = new String[sinPlanLength];

                    for (int i = 0 ; i < sinPlanLength ; i++){
                        //Log.v("abc","10000");
                        sinPlanid[i] = jsonArray.getJSONObject(i).getString("sinid");
                        sinPlanName[i] = jsonArray.getJSONObject(i).getString("sinPlanName");
                        sinCreateDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("createDate"));
                        sinSendPlanDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("sendPlanDate"));
                    }

                    //取得單日禮物清單
                    jsonArray = object.getJSONArray("sinList");
                    sinListLength = jsonArray.length();
                    Log.v("sinListLength", String.valueOf(sinListLength));

                    sinListid = new String[sinListLength];
                    sinGiftid = new String[sinListLength];
                    sinSendGiftDate = new String[sinListLength];
                    sinMessage = new String[sinListLength];

                    for (int i = 0 ; i < sinListLength ; i++){
                        //Log.v("abc","10000");
                        sinListid[i] = jsonArray.getJSONObject(i).getString("sinid");
                        sinGiftid[i] = jsonArray.getJSONObject(i).getString("giftid");
                        sinSendGiftDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("sendGiftDate"));
                        sinMessage[i] = jsonArray.getJSONObject(i).getString("message");
                    }


                    //取得多日計畫
                    jsonArray = object.getJSONArray("mulPlan");
                    mulPlanLength = jsonArray.length();

                    mulPlanid = new String[mulPlanLength];
                    mulPlanName = new String[mulPlanLength];
                    mulCreateDate = new String[mulPlanLength];
                    mulStartDate = new String[mulPlanLength];
                    mulEndDate = new String[mulPlanLength];
                    mulMessage = new String[mulPlanLength];

                    for (int i = 0 ; i < mulPlanLength ; i++){
                        //Log.v("abc","10000");
                        mulPlanid[i] = jsonArray.getJSONObject(i).getString("mulid");
                        mulPlanName[i] = jsonArray.getJSONObject(i).getString("mulPlanName");
                        mulCreateDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("createDate"));
                        mulStartDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("startDate"));
                        mulEndDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("endDate"));
                        mulMessage[i] = jsonArray.getJSONObject(i).getString("message");
                    }

                    //取得多日禮物清單
                    jsonArray = object.getJSONArray("mulList");
                    mulListLength = jsonArray.length();

                    mulListid = new String[mulListLength];
                    mulGiftid = new String[mulListLength];
                    mulSendGiftDate = new String[mulListLength];
                    mulGoal = new String[mulListLength];

                    for (int i = 0 ; i < sinListLength ; i++){
                        //Log.v("abc","10000");
                        mulListid[i] = jsonArray.getJSONObject(i).getString("mulid");
                        mulGiftid[i] = jsonArray.getJSONObject(i).getString("giftid");
                        mulSendGiftDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("sendGiftDate"));
                        mulGoal[i] = jsonArray.getJSONObject(i).getString("goal");
                    }

                    //取得任務計畫
                    jsonArray = object.getJSONArray("misPlan");
                    misPlanLength = jsonArray.length();

                    misPlanid = new String[misPlanLength];
                    misPlanName = new String[misPlanLength];
                    misCreateDate = new String[misPlanLength];
                    misSendPlanDate = new String[misPlanLength];
                    misDeadline = new String[misPlanLength];
                    misGetGiftDate = new String[misPlanLength];

                    for (int i = 0 ; i < mulPlanLength ; i++){
                        //Log.v("abc","10000");
                        misPlanid[i] = jsonArray.getJSONObject(i).getString("misid");
                        misPlanName[i] = jsonArray.getJSONObject(i).getString("misPlanName");
                        misCreateDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("createDate"));
                        misSendPlanDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("sendPlanDate"));
                        misDeadline[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("deadline"));
                        misGetGiftDate[i] = DateFormat.dateFormat(jsonArray.getJSONObject(i).getString("getGiftDate"));
                    }

                    //取得任務禮物清單
                    jsonArray = object.getJSONArray("misList");
                    misListLength = jsonArray.length();

                    misListid = new String[misListLength];
                    misGiftid = new String[misListLength];

                    for (int i = 0 ; i < sinListLength ; i++){
                        //Log.v("abc","10000");
                        mulListid[i] = jsonArray.getJSONObject(i).getString("misid");
                        misGiftid[i] = jsonArray.getJSONObject(i).getString("giftid");
                    }

                    //取得任務清單項目
                    jsonArray = object.getJSONArray("misItem");
                    misItemLength = jsonArray.length();

                    misItemid = new String[misListLength];
                    misContent = new String[misListLength];

                    for (int i = 0 ; i < sinListLength ; i++){
                        //Log.v("abc","10000");
                        misItemid[i] = jsonArray.getJSONObject(i).getString("misid");
                        misContent[i] = jsonArray.getJSONObject(i).getString("content");
                    }

                } catch (Exception e) {
                }
            }
        });
        Log.v("userid", userid);
        Log.v("idPlan", idPlan);
        myAsyncTask.execute(Common.planList, userid, idPlan);
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
    public static String getGiftCreateDate(int i){
        return giftCreateDate[i];
    }
    public static String getGiftName(int i){
        return  giftName[i];
    }
    public static String getOwnerid(int i){
        return  ownerid[i];
    }
    public static String getType(int i){
        return type[i];
    }
    //---------禮物紀錄
    public static int getRecordLength(){
        return giftLength;
    }
    public static String getSenderid(int i){
        return  senderid[i];
    }
    public static String getReceiverid(int i){
        return  receiverid[i];
    }
    public static String getPlanid(int i){
        return planid[i];
    }
    public static String getStore(int i){
        return store[i];
    }
    public static String getFeedback(int i){
        return  feedback[i];
    }
    public static String getOpenDate(int i){
        return openDate[i];
    }
    public static String getPlanType(int i){
        return planType[i];
    }
    public static String getNickname(int i){
        return nickname[i];
    }
    //---------單日計畫
    public static int getSinPlanLength(){
        return giftLength;
    }
    public static String getSinPlanid(int i){
        return  sinPlanid[i];
    }
    public static String getSinPlanName(int i){
        return  sinPlanName[i];
    }
    public static String getSinCreateDate(int i){
        return sinCreateDate[i];
    }
    public static String getSinSendPlanDate(int i){
        return sinSendPlanDate[i];
    }
    //---------單日禮物清單
    public static int getSinListLength(){
        return giftLength;
    }
    public static String getSinListid(int i){
        return  sinListid[i];
    }
    public static String getSinGiftid(int i){
        return  sinGiftid[i];
    }
    public static String getSinSendGiftDate(int i){
        return sinSendGiftDate[i];
    }
    public static String getSinMessage(int i){
        return sinMessage[i];
    }
    //---------多日計畫
    private static int getMulPlanLength(){
        return giftLength;
    }
    public static String getMulPlanid(int i){
        return  mulPlanid[i];
    }
    public static String getMulPlanName(int i){
        return  mulPlanName[i];
    }
    public static String getMulCreateDate(int i){
        return mulCreateDate[i];
    }
    public static String getMulStartDate(int i){
        return  mulStartDate[i];
    }
    public static String getMulEndDate(int i){
        return  mulEndDate[i];
    }
    public static String getMulMessage(int i){
        return mulMessage[i];
    }
    //---------多日禮物清單
    private static int getMulListLength(){
        return giftLength;
    }
    public static String getMulListid(int i){
        return  mulListid[i];
    }
    public static String getMulGiftid(int i){
        return  mulGiftid[i];
    }
    public static String getMulSendGiftDate(int i){
        return mulSendGiftDate[i];
    }
    public static String getMulGoal(int i){
        return  mulGoal[i];
    }
    //---------任務計畫
    private static int getMisPlanLength(){
        return giftLength;
    }
    public static String getMisPlanid(int i){
        return  misPlanid[i];
    }
    public static String getMisPlanName(int i){
        return  misPlanName[i];
    }
    public static String getMisCreateDate(int i){
        return misCreateDate[i];
    }
    public static String getMisSendPlanDate(int i){
        return  misSendPlanDate[i];
    }
    public static String getMisDeadline(int i){
        return  misDeadline[i];
    }
    public static String getMisGetGiftDate(int i){
        return misGetGiftDate[i];
    }
    //---------任務禮物清單
    private static int getMisListLength(){
        return giftLength;
    }
    public static String getMisListid(int i){
        return  misListid[i];
    }
    public static String getMisGiftid(int i){
        return misGiftid[i];
    }
    //---------任務項目
    private static int getMisItemLength(){
        return giftLength;
    }
    public static String getMisItemid(int i){
        return  misItemid[i];
    }
    public static String getMisContent(int i){
        return misContent[i];
    }

}