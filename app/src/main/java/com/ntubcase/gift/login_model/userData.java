package com.ntubcase.gift.login_model;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class userData {

    //使用者id
    private static String user_id;
    //使用者名稱
    private static String user_name;
    //使用者生日
    private static String user_birthday;
    //使用者MAIL
    private static String user_mail;
    //使用者照片位置
    private static Uri user_photo_uri;
    //登入途徑
    private static String loginProtal;

    public userData(String user_name, String user_birthday, String user_mail, Uri user_photo_uri, String loginProtal){
        userData.user_name = user_name;
        userData.user_birthday = user_birthday;
        userData.user_mail = user_mail;
        userData.user_photo_uri = user_photo_uri;
        userData.loginProtal = loginProtal;
    }

    public static void setUserID(String userid){
        user_id = userid;
    }
    public static String getUserID(){
        return user_id;
    }

    //-----FB用-----
    public static void setUserData(String user_name, String user_birthday, Uri user_photo_uri, String loginProtal){
        userData.user_name = user_name;
        userData.user_birthday = user_birthday;
        userData.user_photo_uri = user_photo_uri;
        userData.loginProtal = loginProtal;
    }

    public static void setUserMail(String user_mail){
        userData.user_mail = user_mail;
    }
    //-----FB用-----

    public static String getUserName(){
        return user_name;
    }
    public static String getUserBirthday(){
        return user_birthday;
    }
    public static String getUserMail(){
        return user_mail;
    }
    public static Uri getUserPhotoUri(){
        return user_photo_uri;
    }
    public static String getLoginProtal(){
        return userData.loginProtal;
    }


}
