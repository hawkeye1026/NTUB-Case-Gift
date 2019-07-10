package com.ntubcase.gift.login_model;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;


public class googleAccount {

    //使用者名稱
    private static String user_name;
    //使用者MAIL
    private static String user_mail;
    //使用者照片位置
    private static Uri user_photo_uri;

    private static GoogleSignInClient mGoogleSignInClient;

    public googleAccount(String user_name,String user_mail, Uri user_photo_uri){
        googleAccount.user_name = user_name;
        googleAccount.user_mail = user_mail;
        googleAccount.user_photo_uri = user_photo_uri;
    }

    public static String getUserName(){
        return user_name;
    }

    public static String getUserMail(){
        return user_mail;
    }

    public static Uri getPhotoUrl(){
        return user_photo_uri;
    }

    public static GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public static void setGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        googleAccount.mGoogleSignInClient = mGoogleSignInClient;
    }
}
