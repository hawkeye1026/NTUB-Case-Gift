package com.ntubcase.gift.MyAsyncTask.login;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class loginAsyncTask extends AsyncTask<String, Integer, String> {

    //----------------------------------------------------
    // 宣告一個TaskListener介面, 接收回傳值的物件必須實作它
    //----------------------------------------------------
    public interface TaskListener {
        void onFinished(String result);
    }

    //----------------------
    // 接收回傳值的物件參考
    //----------------------
    private final TaskListener taskListener;

    //---------------------------------------
    // 建構元, 傳入context及接收回傳值的物件
    //---------------------------------------
    public loginAsyncTask(TaskListener taskListener) {
        this.taskListener = taskListener;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    //=========================================================
    // 執行非同步工作, 建立一個HttpURLConnection, 讀取主機的資料.
    //=========================================================
    @Override
    protected String doInBackground(String... params) {
        String data=null;
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            URL url = new URL(params[0]); //params[0] 是myNavigationAsyncTask.execute(Common.updateUrl, getId);的第一個參數
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(150000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //----------------------------------------------
            //  傳給主機的參數(name, amount, deliverDate)
            //----------------------------------------------
            //params[1] 是myNavigationAsyncTask.execute(Common.updateUrl, getId);的第二個參數
            String args =
                    "email=" + URLEncoder.encode(params[1], "UTF-8")+
                    "&nickname=" + URLEncoder.encode(params[2], "UTF-8" )+
                    "&birthday=" + URLEncoder.encode(params[3], "UTF-8" )+
                    "&image=" + URLEncoder.encode(params[4], "UTF-8" );

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(args);
            writer.flush();
            writer.close();
            os.close();

            int statusCode = conn.getResponseCode();

            if (statusCode >= 200 && statusCode < 400) {
                // Create an InputStream in order to extract the response object
                inputStream = conn.getInputStream();
            }
            else {
                inputStream = conn.getErrorStream();
            }
            conn.connect();
            inputStream = conn.getInputStream();

            BufferedReader bufferedReader=new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));

            data=bufferedReader.readLine();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }


    //===========================
    // 執行完非同步工作之後執行
    //===========================
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        taskListener.onFinished(result);
    }

    @Override
    protected void onCancelled(String result) {
        super.onCancelled(result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}