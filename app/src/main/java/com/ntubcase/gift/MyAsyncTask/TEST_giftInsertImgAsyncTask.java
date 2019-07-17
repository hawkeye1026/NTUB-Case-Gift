package com.ntubcase.gift.MyAsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class TEST_giftInsertImgAsyncTask extends AsyncTask<String, Integer, String> {


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
    private String sourceFileUri;
    private int serverResponseCode;
    Runtime lRuntime = Runtime.getRuntime();


    //---------------------------------------
    // 建構元, 傳入context及接收回傳值的物件
    //---------------------------------------
    public TEST_giftInsertImgAsyncTask(TaskListener taskListener,String sourceFileUri) {
        this.taskListener = taskListener;
        this.sourceFileUri = sourceFileUri;
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
        try {
//            sourceFileUri = "/mnt/sdcard/abc.png";

            HttpURLConnection conn = null;
            //DataOutputStream writer = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            long freeM;
            int bytesRead = 0 ,bufferSize = 0;
            char[] buffer = new char[0];
            //檔案大小限制
            int maxBufferSize = 1000 * 1024 * 1024;
            Log.v("fileUri1",String.valueOf(sourceFileUri));
            File sourceFile = new File(sourceFileUri.toString());
            Log.v("fileUri2",String.valueOf(sourceFileUri));

            if (!sourceFile.isFile()) {
                Log.v("uploadFile", "Source File not exist :");
            }
            else{
                try {

                    // open a URL connection to the Servlet
                    FileReader FileReader = new FileReader(
                            sourceFile);
                    URL url = new URL(params[0]);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE",
                            "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    //將圖片名稱寫入標頭
                    conn.setRequestProperty("image", params[2]);

                    //conn.connect();
                    OutputStream os = conn.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));

                    //將標頭寫入，name=image > php 用 $_POST["image"] 接資料 filename為檔案名稱
                    writer.write(twoHyphens + boundary + lineEnd);
                    writer.write("Content-Disposition:form-data; name=\"image\";filename=\""
                            + params[2] + "\"" + lineEnd);

                    writer.write(lineEnd);

                    // create a buffer of maximum size
                    freeM = lRuntime.freeMemory();

                    bufferSize = (int) Math.min(freeM, maxBufferSize);

                    buffer = new char[bufferSize];

                    // read file and write it into form...
                    bytesRead = FileReader.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {

                        writer.write(buffer, 0, bufferSize);
                        freeM = lRuntime.freeMemory();

                        bufferSize = (int) Math.min(freeM, maxBufferSize);
                        bytesRead = FileReader.read(buffer, 0,
                                bufferSize);

                    }
//                    //將資料寫入
//                    writer.write(buffer, 0, maxBufferSize);

                    // send multipart form data necesssary after file
                    // data...1
                    writer.write(lineEnd);
                    writer.write(twoHyphens + boundary + twoHyphens
                            + lineEnd);
//
//                    // Responses from the server (code and message)
//                    serverResponseCode = conn.getResponseCode();
//                    String serverResponseMessage = conn
//                            .getResponseMessage();
//                    Log.v("resCode",serverResponseCode + "");
//                    if (serverResponseCode == 200) {
//
//                        // messageText.setText(msg);
//                        //Toast.makeText(ctx, "File Upload Complete.",
//                        //      Toast.LENGTH_SHORT).show();
//
//                        // recursiveDelete(mDirectory1);
//
//                    }

                    // close the streams //
                    FileReader.close();
                    writer.flush();
                    writer.close();

                } catch (Exception e) {

                    // dialog.dismiss();
                    e.printStackTrace();

                }
                // dialog.dismiss();

            } // End else block


        } catch (Exception ex) {
            // dialog.dismiss();

            ex.printStackTrace();
        }
        return "Executed";
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