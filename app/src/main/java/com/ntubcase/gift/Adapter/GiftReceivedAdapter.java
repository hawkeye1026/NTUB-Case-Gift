package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import com.ntubcase.gift.GiftReceivedNewFragment;
import com.ntubcase.gift.R;

import com.ntubcase.gift.SurpriseCardviewGiftItem;
import com.ntubcase.gift.re_NewgiftCardviewItem;


import java.util.List;
import java.util.Map;


public class GiftReceivedAdapter extends RecyclerView.Adapter<GiftReceivedAdapter.ViewHolder> {
    private Context context;
    private List<re_NewgiftCardviewItem> re_giftList;


    public GiftReceivedAdapter(GiftReceivedNewFragment context, List<SurpriseCardviewGiftItem> re_giftList){
        this.re_giftList = re_giftList;
    }
    @Override
    public GiftReceivedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.newgiftlist_layout, parent, false);
        return new GiftReceivedAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(GiftReceivedAdapter.ViewHolder holder, int position) {
        final re_NewgiftCardviewItem re_NewgiftCardviewItem = re_giftList.get(position);
        holder.image.setImageResource(re_NewgiftCardviewItem.getImage());
        holder.giftName.setText(re_NewgiftCardviewItem.getGiftname());
        holder.sender.setText(re_NewgiftCardviewItem.getSender());
        holder.date.setText(re_NewgiftCardviewItem.getSender());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //觸發事件
            }
        });
    }
    @Override
    public int getItemCount() {
        return re_giftList.size();
    }

    //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardview;
        TextView giftName,sender,date;
        ImageView image;
        ViewHolder(View itemView) {
            super(itemView);
            cardview= (CardView)itemView;
            giftName = (TextView) itemView.findViewById(R.id.tv_giftname);
            sender=(TextView) itemView.findViewById(R.id.tv_sender);
            date=(TextView) itemView.findViewById(R.id.tv_date);
            image = (ImageView) itemView.findViewById(R.id.iv_photo);
        }
    }

}
