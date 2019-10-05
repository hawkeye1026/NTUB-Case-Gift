package com.ntubcase.gift.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ntubcase.gift.R;
import com.ntubcase.gift.ReceivedListActivity;
import com.ntubcase.gift.ReceivedMultipleActivity;
import com.ntubcase.gift.ReceivedSingleActivity;

import java.util.List;
import java.util.Map;

public class re_plan_single_adapter extends RecyclerView.Adapter<re_plan_single_adapter.ViewHolder> {
    private List<Map<String, Object>> mData;
    private re_plan_single_adapter.OnItemClickListener mOnItemClickListener;
    public static boolean isFromMake = true;

    public re_plan_single_adapter(List<Map<String, Object>> data) {
        mData = data;
    }


    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private TextView txtTime,txtGift,txtMessage;

        ViewHolder(View itemView) {
            super(itemView);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtGift = (TextView) itemView.findViewById(R.id.txtGift);
            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
        }
    }

    @Override
    public re_plan_single_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 連結項目布局檔list_item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.re_single_item, parent, false);
        return new re_plan_single_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final re_plan_single_adapter.ViewHolder holder, final int position) {
        // 設置txtItem要顯示的內容
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() { //----------item點擊事件----------
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        String giftName = mData.get(position).get("giftName").toString();
        String sentTime = mData.get(position).get("sentTime").toString();
        String message = mData.get(position).get("message").toString();
        String type = mData.get(position).get("type").toString();

        switch (type) {
            case "照片":
                holder.itemView.setBackgroundResource(R.drawable.image_video);
                break;
            case "影片":
                holder.itemView.setBackgroundResource(R.drawable.image_video);
                break;
            case "悄悄話":
                holder.itemView.setBackgroundResource(R.drawable.whisper);
                break;
            case "兌換卷":
                holder.itemView.setBackgroundResource(R.drawable.ticket);
                break;
            case "解碼表":
                holder.itemView.setBackgroundResource(R.drawable.code);
                break;
        }

        holder.txtTime.setText(sentTime);
        holder.txtGift.setText(giftName);
        holder.txtMessage.setText(message);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void setOnItemClickListener(re_plan_single_adapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
