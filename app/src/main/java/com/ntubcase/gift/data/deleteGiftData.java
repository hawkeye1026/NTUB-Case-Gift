package com.ntubcase.gift.data;

import android.util.Log;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.delete.giftDeleteAsyncTask;
import com.ntubcase.gift.MyAsyncTask.gift.update.giftUpdateAsyncTask;

public class deleteGiftData {
    static int count = 0;
    static String[]  deleteGift = new String[getGiftList.getGiftLength()];

    public static void setDeleteGiftData (String userid){
        deleteGift[count] = userid;
        Log.v("Del_count 1:" , count + "");
        try {
            //判斷傳入的user是否與刪除陣列重複，
            for (int i = 0; i < count; i++) {
                Log.v("Del_count 2:" , count + "");
                if (deleteGift[i].equals(userid)) {

                    deleteGift[count] = "";
                    count--;
                    Log.v("Del_count 3:" , count + "");
                    return;
                }

            }
            count++;
        }catch (Exception e){

        }
    }

    public static void setCountDefault(){
        count = 0;
    }

    public static void deleteGift(){
        Log.v("Del_length:" , count + "");
        for(int i = 0 ; i < count; i++ ){
            //------------------------------上傳禮物資料
            giftDeleteAsyncTask mgiftDeleteAsyncTask = new giftDeleteAsyncTask(new giftDeleteAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });

            mgiftDeleteAsyncTask.execute(Common.deleteGift,deleteGift[i]);
        }
    }
}
