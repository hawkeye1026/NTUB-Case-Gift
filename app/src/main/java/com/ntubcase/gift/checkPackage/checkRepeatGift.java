package com.ntubcase.gift.checkPackage;

import com.ntubcase.gift.data.getGiftList;

public class checkRepeatGift {

    public static boolean checkRepeatGift(String giftName){
        for (int i = 0 ; i <getGiftList.getGiftLength(); i++){
            //-----回傳false拒絕存取
            if(getGiftList.getGiftName(i).equals(giftName))return false;
        }
        //-----回傳true拒絕存取
        return true;
    }
}
