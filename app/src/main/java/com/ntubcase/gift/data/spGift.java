package com.ntubcase.gift.data;

public class spGift extends getGiftList{
    //here
    String giftid;
    String giftName;
    String type;

    public spGift(String giftid, String giftName, String type){
        this.giftid=giftid;
        this.giftName=giftName;
        this.type=type;
    }

    public String getGiftid(){return this.giftid;}
    public String getGiftName(){return this.giftName;}
    public String getType(){return this.type;}
}
