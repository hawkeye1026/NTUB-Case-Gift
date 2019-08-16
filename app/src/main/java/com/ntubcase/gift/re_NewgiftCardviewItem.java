package com.ntubcase.gift;

public class re_NewgiftCardviewItem {
        private int image;
        private String giftname,sender,date;

        public re_NewgiftCardviewItem() {
            super();
        }

        public re_NewgiftCardviewItem(int image, String giftname,String sender,String date) {
            super();
            this.image = image;
            this.giftname = giftname;
            this.sender = sender;
            this.date = date;
        }


    //------------------------------------
        public int getImage() {
            return image;
        }
        public void setImage(int image) {
            this.image = image;
        }
        //------------------------------------
        public String getGiftname() {
            return giftname;
        }
        public void setGiftname(String giftname) {
            this.giftname = giftname;
        }
        //------------------------------------
        public String getSender() {
            return sender;
        }
        public void setSender(String sender) {
            this.sender = sender;
        }
        //------------------------------------
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }


}

