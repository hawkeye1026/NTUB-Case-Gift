package com.ntubcase.gift.login_model;


import com.ntubcase.gift.LoginActivity;


public class revokeAccess {
    public revokeAccess() {
        googleAccount.getmGoogleSignInClient().revokeAccess();
    }
//---------- 移除訪問權限範本
//                new revokeAccess();
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this , LoginActivity.class);
//                startActivity(intent);
}
