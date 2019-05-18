package com.ntubcase.gift.Common;

public class Common {
    //--------------------------
    //(1) 若連結自己的主機
    //--------------------------
    // 確認並修改主機的ip位址
    // 若為192.168開頭的虛擬IP, 執行時模擬器與主機應使用同一分享器內之網路
    //public static String url="http://192.168.56.1:3000";

    //--------------------------
    //(2) 若連結現有測試主機
    //--------------------------
    //--------------------------------GET
    public static String giftList="http://140.131.114.156/NTUB_gift_server/giftList.php/";
    public static String planList="http://140.131.114.156/NTUB_gift_server/planList.php/";
    public static String giftReceived="http://140.131.114.156/NTUB_gift_server/giftReceived.php/";
    public static String giftReceivedDone="http://140.131.114.156/NTUB_gift_server/giftReceivedDone.php/";
    //--------------------------------POST
//    public static String test="http://140.131.114.156/NTUB_gift_server/test.php?";
    public static String friendList="http://140.131.114.156/NTUB_gift_server/friendList.php?";
    public static String insertGift="http://140.131.114.156/NTUB_gift_server/insertGift.php?";
    public static String insertPlan="http://140.131.114.156/NTUB_gift_server/insertSpPlan.php?";
    //public static String insertProtal="http://140.131.114.156/NTUB_gift_server/protal.php?";


}