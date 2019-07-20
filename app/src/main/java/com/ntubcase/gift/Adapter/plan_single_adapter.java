package com.ntubcase.gift.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.ntubcase.gift.R;


import java.util.List;
import java.util.Map;


public class plan_single_adapter extends RecyclerView.Adapter<plan_single_adapter.ViewHolder>{
    private List<Map<String, Object>> mData;
    private OnItemClickListener mOnItemClickListener;

    public plan_single_adapter(List<Map<String, Object>> data) {
        mData = data;
    }


    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private TextView txtItem;
        private Button btnRemove;
        ViewHolder(View itemView) {
            super(itemView);
            txtItem = (TextView) itemView.findViewById(R.id.txtItem);
            btnRemove= (Button) itemView.findViewById(R.id.btnRemove);

            // 點擊項目中的Button時
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 移除項目，getAdapterPosition為點擊的項目位置
                    removeItem(getAdapterPosition());
                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 連結項目布局檔list_item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final plan_single_adapter.ViewHolder holder, final int position) {
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
        holder.txtItem.setText(giftName + "; " + sentTime + "; " + message);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 刪除項目
    public void removeItem(int position){
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
