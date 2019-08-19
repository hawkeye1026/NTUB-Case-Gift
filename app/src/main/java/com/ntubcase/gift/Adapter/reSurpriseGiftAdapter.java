package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ntubcase.gift.GiftReceivedNewFragment;
import com.ntubcase.gift.R;
import com.ntubcase.gift.SurpriseCardviewGiftItem;

import java.util.List;


public class reSurpriseGiftAdapter extends RecyclerView.Adapter<reSurpriseGiftAdapter.ViewHolder>{

        private Context context;
        private List<SurpriseCardviewGiftItem> giftList;

    public reSurpriseGiftAdapter(Context context, List<SurpriseCardviewGiftItem> giftList) {
            this.context = context;
            this.giftList = giftList;
        }

    @Override
        public reSurpriseGiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.surprise_gift_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(reSurpriseGiftAdapter.ViewHolder holder, int position) {
            final SurpriseCardviewGiftItem surpriseCardviewGiftItem = giftList.get(position);

            holder.giftName.setText(surpriseCardviewGiftItem.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //觸發事件
                }
            });
        }

        @Override
        public int getItemCount() {
            return giftList.size();
        }

        //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
        class ViewHolder extends RecyclerView.ViewHolder{

            TextView  giftName;
            ViewHolder(View itemView) {
                super(itemView);
                giftName = (TextView) itemView.findViewById(R.id.giftName);
            }
        }

}
