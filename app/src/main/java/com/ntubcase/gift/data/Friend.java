package com.ntubcase.gift.data;

import android.graphics.Bitmap;

public class Friend extends ImageObj{
    //here
    String friendId;
    String friendName;
    String friendMail;
    String imgURL;
    Bitmap img;

    public Friend(String friendId, String friendName, String friendMail, String imgURL, Bitmap img){
        this.friendId=friendId;
        this.friendName=friendName;
        this.friendMail=friendMail;
        this.imgURL=imgURL;
        this.img=img;
    }

    public String getFriendId(){return this.friendId;}
    public String getFriendName(){return this.friendName;}
    public String getFriendMail(){return this.friendMail;}
    public String getImgURL(){return this.imgURL;}
    public Bitmap getImg(){return img;}
}