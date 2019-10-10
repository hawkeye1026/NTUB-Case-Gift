package com.ntubcase.gift.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ntubcase.gift.R;


import java.util.ArrayList;
import java.util.List;


public class plan_list_adapter extends RecyclerView.Adapter<plan_list_adapter.ViewHolder>{
    private List<String> mData;

    private OnItemClickListener mOnItemClickListener;
    public static boolean isFromMake = true;

    public plan_list_adapter(List<String> data) {
        mData = data;
    }

    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        TextView textView;
        Button btnRemove;

        ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.Textview);
            btnRemove= (Button) itemView.findViewById(R.id.btn_delete_item);

            if (isFromMake){ //製作計畫
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 移除項目，getAdapterPosition為點擊的項目位置
                        mData.remove(getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
            }else { //已預送
                btnRemove.setVisibility(View.INVISIBLE); //若是唯讀狀態則不顯示刪除按鈕
            }

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 連結項目布局檔list_item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final plan_list_adapter.ViewHolder holder, final int position) {
        // 設置txtItem要顯示的內容
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() { //----------item點擊事件----------
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        String mission = mData.get(position); //任務清單內容
        holder.textView.setText(mission);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(plan_list_adapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
