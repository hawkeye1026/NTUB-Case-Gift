package com.ntubcase.gift.login_model;

import android.net.Uri;

public class userData {

    //使用者id
    private static String user_id;
    //使用者名稱
    private static String user_name;
    //使用者MAIL
    private static String user_mail;
    //使用者照片位置
    private static Uri user_photo_uri;
    //登入途徑
    private static String loginPortal;

    public userData(String user_name, String user_mail, Uri user_photo_uri, String loginPortal){
        userData.user_name = user_name;
        userData.user_mail = user_mail;
        userData.user_photo_uri = user_photo_uri;
        userData.loginPortal = loginPortal;
    }

    public static void setUserID(String userid){
        user_id = userid;
    }

    public static String getUserID(){
        return user_id;
    }
    public static String getUserName(){
        return user_name;
    }
    public static String getUserMail(){
        return user_mail;
    }
    public static Uri getUserPhotoUri(){
        return user_photo_uri;
    }
    public static String getLoginPortal(){
        return userData.loginPortal;
    }


}
