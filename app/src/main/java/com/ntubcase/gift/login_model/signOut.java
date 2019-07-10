package com.ntubcase.gift.login_model;

import com.ntubcase.gift.LoginActivity;


public class signOut {
    public signOut(){
        googleAccount.getmGoogleSignInClient().signOut();
    }
//---------- 移除訪問登出範本
//                new signOut();
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this , LoginActivity.class);
//                startActivity(intent);
}
