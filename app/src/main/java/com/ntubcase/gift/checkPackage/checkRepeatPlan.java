package com.ntubcase.gift.checkPackage;

import com.ntubcase.gift.data.getPlanGot;

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
