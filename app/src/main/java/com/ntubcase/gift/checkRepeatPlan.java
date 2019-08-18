package com.ntubcase.gift;

import com.ntubcase.gift.data.getGiftList;
import com.ntubcase.gift.data.getPlanGot;
import com.ntubcase.gift.data.getPlanList;

public class checkRepeatPlan {

    public static boolean checkRepeatPlan(String planName){
        for (int i = 0; i < getPlanGot.getPlangotgLength(); i++){
            //-----回傳false拒絕存取
            if(getPlanGot.getPlanName(i).equals(planName))return false;
        }
        //-----回傳true拒絕存取
        return true;
    }
}
