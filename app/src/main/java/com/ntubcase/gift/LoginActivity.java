package com.ntubcase.gift;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.login.loginAsyncTask;
import com.ntubcase.gift.data.getFriendList;
import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.data.getPlanGot;
import com.ntubcase.gift.data.getPlanSent;
import com.ntubcase.gift.data.getPlanningList;
import com.ntubcase.gift.data.getReceiveNew;
import com.ntubcase.gift.data.getReceiveOpen;
import com.ntubcase.gift.login_model.facebookAccount;

import com.ntubcase.gift.login_model.googleAccount;

import java.util.Arrays;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.login.LoginManager.getInstance;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener  {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private static GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;  //--------fb logiin

    private SharedPreferences mSharedPreferences; //儲存登入資訊

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //---------------判斷用戶是否登入過---------------
        mSharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        String user_name = mSharedPreferences.getString("user_name", "no data");
        String user_mail = mSharedPreferences.getString("user_mail", "no data");
        String user_photo_uri = mSharedPreferences.getString("user_photo_uri", "no data");
        String user_id = mSharedPreferences.getString("user_id", "no data");
        String login_portal = mSharedPreferences.getString("login_portal", "no data");

        if (login_portal.equals("google")) { //---已登入google---
            //-----建立userData-----
            new googleAccount(user_name, user_mail, Uri.parse(user_photo_uri));
            userData.setUserID(user_id);

            //-----setGoogleSignInClient-----
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            googleAccount.setGoogleSignInClient(mGoogleSignInClient);
            //----------取得禮物、計畫、收禮、好友資料
            getGiftList.getJSON();
            getPlanningList.getJSON();
            getPlanSent.getJSON();
            getPlanGot.getJSON();
            getFriendList.getJSON();
            getReceiveNew.getJSON();
            getReceiveOpen.getJSON();
            //-----進入首頁-----
            Intent intent;
            intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if (login_portal.equals("FB")) { //---已登入fb---
            //-----建立userData-----
            new facebookAccount(user_name, user_mail, Uri.parse(user_photo_uri));
            userData.setUserID(user_id);
            //----------取得禮物、計畫、收禮、好友資料
            getAllData();
            //-----進入首頁-----
            Intent intent;
            intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            googleLogin(); //---Google登入---
            FacebookLogin(); // -----Facebook登入-----
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) { //Google登入
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{ //FB登入
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 確認連線是否成功
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.v("login","success");

            updateUI(account); //改變UI
        } catch (ApiException e) {
            //登入失敗，failed code 要參閱: https://developers.google.com/android/reference/com/google/android/gms/common/api/CommonStatusCodes#SIGN_IN_REQUIRED
            Log.v("login","fail");
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            //建立google帳戶的物件: googleAccount(使用者名稱,使用者,使用者頭像)
            String user_mail = account.getEmail();
            String user_name = account.getDisplayName();
            String user_photoUri;

            if(account.getPhotoUrl() == null) user_photoUri = "";
            else user_photoUri = account.getPhotoUrl().toString();

            //-----在手機儲存登入的資訊-----
            mSharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
            mSharedPreferences.edit()
                    .putString("user_name", user_name)
                    .putString("user_mail", user_mail)
                    .putString("user_photo_uri", user_photoUri)
                    .putString("login_portal","google")
                    .commit();
            new googleAccount(user_name, user_mail, Uri.parse(user_photoUri));

            //---上傳資料---
            loginAsyncTask loginAsyncTask = new loginAsyncTask(new loginAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {
                    try{
                        if(result==null){
                            Log.v("aaaa",userData.getUserID());
                            return;
                        }
                        JSONObject object = new JSONObject(result);
                        JSONArray jsonArray = object.getJSONArray("result");

                        for (int i = 0 ; i <jsonArray.length() ; i++) {
                            //取得使用者ID
                            userData.setUserID(jsonArray.getJSONObject(i).getString("userid"));
                            mSharedPreferences.edit().putString("user_id", userData.getUserID()).commit();

                            Log.v("insertgiftid",userData.getUserID());
                        }
                        //----------取得禮物、計畫、收禮、好友資料
                        getAllData();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            loginAsyncTask.execute(Common.login , userData.getUserMail(), userData.getUserName(), userData.getUserPhotoUri().toString());

            //-----若確認已登入，直接進入首頁-----
            Intent intent;
            intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
    }

    //綁定各按鈕的作用
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn(); //登入
                break;
        }
    }

    public void googleLogin(){
        // 設置登入監聽器
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        // 結束設置登入監聽器

        //開啟google登入
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //[END configure_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleAccount.setGoogleSignInClient(mGoogleSignInClient);
        // [END build_client]
    }

    //----------Facebook登入----------
    private void FacebookLogin(){
        callbackManager = CallbackManager.Factory.create();

        LoginButton fbLoginButton = (LoginButton) findViewById(R.id.login_button);
        fbLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email")); //要取得的權限

        // Callback registration
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) { //登入成功
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try{
                                    //---取得資料---
                                    long user_id = object.getLong("id");
                                    String user_mail = object.getString("email");
                                    String user_name = object.getString("name");
                                    Uri user_photo_uri = Uri.parse("http://graph.facebook.com/"+user_id+"/picture?type=large");

                                    //-----在手機儲存登入的資訊-----
                                    mSharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
                                    mSharedPreferences.edit()
                                            .putString("user_name", user_name)
                                            .putString("user_mail", user_mail)
                                            .putString("user_photo_uri", "http://graph.facebook.com/"+user_id+"/picture?type=large")
                                            .putString("login_portal","FB")
                                            .commit();
                                    new facebookAccount(user_name, user_mail, user_photo_uri);

                                    //---上傳資料---
                                    loginAsyncTask loginAsyncTask = new loginAsyncTask(new loginAsyncTask.TaskListener() {
                                        @Override
                                        public void onFinished(String result) {
                                            try{
                                                if(result==null){ //伺服器連線失敗跳維修頁
                                                    Log.v("aaaa",userData.getUserID());
                                                    return;
                                                }
                                                JSONObject object = new JSONObject(result);
                                                JSONArray jsonArray = object.getJSONArray("result");

                                                for (int i = 0 ; i <jsonArray.length() ; i++) {
                                                    //取得使用者ID
                                                    userData.setUserID(jsonArray.getJSONObject(i).getString("userid"));
                                                    mSharedPreferences.edit().putString("user_id", userData.getUserID()).commit();

                                                    Log.v("insertgiftid",userData.getUserID());
                                                }
                                                getAllData();
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    loginAsyncTask.execute(Common.login , userData.getUserMail(), userData.getUserName(), userData.getUserPhotoUri().toString());


                                    //-----若確認已登入，直接進入首頁-----
                                    Intent intent;
                                    intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
            }
        });

    }

    public void getAllData(){
        //----------取得禮物、計畫、收禮、好友資料
        getGiftList.getJSON();
        getPlanningList.getJSON();
        getPlanSent.getJSON();
        getPlanGot.getJSON();
        getFriendList.getJSON();
        getReceiveNew.getJSON();
        getReceiveOpen.getJSON();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder ad=new AlertDialog.Builder(this);
            ad.setTitle("離開");
            ad.setMessage("確定要離開預送你嗎?");
            ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
                public void onClick(DialogInterface dialog, int i) {
                    finish();
                }
            });
            ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int i) {
                }
            });
            ad.show();//顯示對話框
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}