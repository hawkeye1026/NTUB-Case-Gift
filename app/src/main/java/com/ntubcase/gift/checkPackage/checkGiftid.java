package com.ntubcase.gift.checkPackage;

import com.ntubcase.gift.data.getGiftList;

public class checkGiftid {
    public static int checkGiftid(int giftid){
        for (int i = 0; i < getGiftList.getGiftLength(); i++){
            //-----回傳false拒絕存取
            if(getGiftList.getGiftid(i).equals(String.valueOf(giftid)))return i;
        }
        //-----回傳true拒絕存取
        return -1;
    }
}
