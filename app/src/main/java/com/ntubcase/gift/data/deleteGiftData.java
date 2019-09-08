package com.ntubcase.gift.data;

public class deleteGiftData {
    static int count = 0;
    String[] deleteGift ;

    public deleteGiftData (String userid){
        count++;
        String[] tempGift = new String[count];
        deleteGift[count] = userid;

    }
}
