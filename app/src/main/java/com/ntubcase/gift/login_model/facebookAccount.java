package com.ntubcase.gift.login_model;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;


public class facebookAccount {

    //使用者名稱
    private static String user_name;
    //使用者MAIL
    private static String user_mail;
    //使用者生日
    private static String user_birthday;

    private static String user_id;

    //使用者照片位置
    private static Uri user_photo_uri;


    public facebookAccount(String user_name,String user_birthday , String user_mail, String user_id){
        facebookAccount.user_name = user_name;
        facebookAccount.user_birthday = user_birthday;
        facebookAccount.user_mail = user_mail;
        facebookAccount.user_id = user_id;

        facebookAccount.user_photo_uri = Uri.parse("http://graph.facebook.com/"+user_id+"/picture?type=large");
    }
    public static String getUserName(){
        return user_name;
    }

    public static String getUserMail(){
        return user_mail;
    }

    public static String getUserBirthday(){
        return user_birthday;
    }

    public static String getUseruserid(){
        return user_id;
    }

    public static Uri getUserPhotoUri(){
        return user_photo_uri;
    }
}
