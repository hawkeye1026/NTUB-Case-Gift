package com.ntubcase.gift.data;

import android.net.Uri;


public class googleAccount {

    private static String user_name;
    private static String user_mail;
    private static Uri user_photo;

    public googleAccount(String user_name,String user_mail, Uri user_photo){
        this.user_name = user_name;
        this.user_mail = user_mail;
        this.user_photo = user_photo;
    }

    public static String getUserName(){
        return user_name;
    }

    public static String getUserMail(){
        return user_mail;
    }

    public static Uri getUserPhoto(){
        return user_photo;
    }


}
