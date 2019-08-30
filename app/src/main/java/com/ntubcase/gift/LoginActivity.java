package com.ntubcase.gift;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.login.loginAsyncTask;
import com.ntubcase.gift.login_model.facebookAccount;

import com.ntubcase.gift.login_model.googleAccount;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.ntubcase.gift.login_model.userData;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.login.LoginManager.getInstance;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener  {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private CallbackManager callbackManager;

    private static GoogleSignInClient mGoogleSignInClient;

    private Button btn_main;
    private static String user_birthday;

    //--------fb logiin
    private String userid;
    //--------
    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //--------取得目前時間：yyyy/MM/dd hh:mm:ss
        Date date =new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        user_birthday = sdFormat.format(date);

        //-------------登入按鈕
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);

        FBLogin();

        googleLogin();

        //---------------------------------------------------------------------------------------------------直接進入
        Button btn_main = (Button) findViewById(R.id.btn_main);
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //更新UI，
        updateUI(account);
        //Toast.makeText(this,account.getEmail(),Toast.LENGTH_SHORT);

    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // [END onActivityResult]

    // 確認連線是否成功
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.v("login","success");
            //改變UI
            updateUI(account);
        } catch (ApiException e) {
            //登入失敗，failed code 要參閱: https://developers.google.com/android/reference/com/google/android/gms/common/api/CommonStatusCodes#SIGN_IN_REQUIRED
            Log.v("login","fail");

            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    // [END handleSignInResult]

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            //建立google帳戶的物件: googleAccount(使用者名稱,使用者,使用者頭像)
            String user_mail = account.getEmail();
            String user_name = account.getDisplayName();
            String user_birthday = "1998/05/12";
            String user_photoUri;

            if(account.getPhotoUrl() == null){
                user_photoUri = "";
            }else{
                user_photoUri = account.getPhotoUrl().toString();
            }

            new googleAccount(user_name, user_birthday, user_mail, Uri.parse(user_photoUri));
            loginAsyncTask loginAsyncTask = new loginAsyncTask(new loginAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            loginAsyncTask.execute(Common.login , userData.getUserMail(), userData.getUserName() ,userData.getUserBirthday() ,userData.getUserPhotoUri().toString());
            //若確認已登入，直接進入首頁
            Intent intent;
            intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

//
//            Toast.makeText(this,account.getDisplayName(),Toast.LENGTH_SHORT);
//
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
    }

    //綁定各按鈕的作用
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                //登入
                signIn();
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

    public void FBLogin(){
        try {
            Button loginButton = (Button) findViewById(R.id.login_button);

            //--------------facebook 登入
            final AccessToken accessToken = AccessToken.getCurrentAccessToken();
            boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

            //宣告callback Manager
            callbackManager = CallbackManager.Factory.create();
            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(getApplication());

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getInstance()
                            .logInWithReadPermissions(LoginActivity.this,
                                    Arrays.asList("public_profile","user_friends","email"));

                }
            });

            // If using in a fragment
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    //建立google帳戶的物件: googleAccount(使用者名稱,使用者,使用者頭像)
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    //-------------讀取Dialog-----------
                                    barProgressDialog = ProgressDialog.show(LoginActivity.this,
                                            "讀取中", "請等待...",true);
                                    new Thread(new Runnable(){
                                        @Override
                                        public void run() {
                                            try{
                                                Thread.sleep(1000);
                                            }
                                            catch(Exception e){
                                                e.printStackTrace();
                                            }
                                            finally{
                                                barProgressDialog.dismiss();
                                                finish();
                                            }
                                        }
                                    }).start();
                                    //-------------結束Dialog-----------

                                    try {
                                        String user_mail = object.getString("email");
                                        String user_name = object.getString("name");
                                        String user_birthday = "1998/05/12";
                                        String user_id = object.getString("id");

                                        //Log.v("abc","10000");
                                        new facebookAccount(user_name, user_birthday, user_mail, user_id);
                                        loginAsyncTask loginAsyncTask = new loginAsyncTask(new loginAsyncTask.TaskListener() {
                                            @Override
                                            public void onFinished(String result) {

                                            }
                                        });
                                        loginAsyncTask.execute(Common.login , userData.getUserMail(), userData.getUserName() ,userData.getUserBirthday() ,userData.getUserPhotoUri().toString());
                                        //若確認已登入，直接進入首頁
                                        Intent intent;
                                        intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, name, birthday , email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    exception.printStackTrace();
                }
            });

            //--------------facebook 登入結束
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}