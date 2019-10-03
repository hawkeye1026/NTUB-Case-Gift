package com.ntubcase.gift;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.insert.giftInsertCodeAsyncTask;
import com.ntubcase.gift.MyAsyncTask.gift.insert.giftUpdateCodeAsyncTask;
import com.ntubcase.gift.MyAsyncTask.gift.update.giftUpdateAsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class updateGiftCode {

    public updateGiftCode(String decodeid , String rowNumber, String maincode_array, String matchcode_array){

        //--------上傳code
        giftUpdateCodeAsyncTask giftUpdateCodeAsyncTask = new giftUpdateCodeAsyncTask(new giftUpdateCodeAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {

            }
        });
        // Log.v("Mupgiftid",uploadGift.getLastGiftid());
        giftUpdateCodeAsyncTask.execute(Common.updateGiftCode,decodeid, rowNumber, maincode_array , matchcode_array);
    }
}
