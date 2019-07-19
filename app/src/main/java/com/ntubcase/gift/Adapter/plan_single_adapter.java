package com.ntubcase.gift.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import com.ntubcase.gift.R;


import java.util.List;



public class plan_single_adapter extends RecyclerView.Adapter<plan_single_adapter.ViewHolder>implements Filterable{
    private List<String> mData;
    private OnItemClickListener mOnItemClickListener;

    public plan_single_adapter(List<String> data) {
        mData = data;
    }

    @Override
    public Filter getFilter() {
        return null;
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

//            // 點擊項目時
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(view.getContext(),
//                            "click " +getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                }
//            });

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
        holder.txtItem.setText(mData.get(position));
    }





    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 新增項目
    public void addItem(String text) {
        int a;
        a = mData.size();
        if(a==0){
            mData.add(0,text);
            notifyItemInserted(0);
        }else{
            mData.add(a,text);
            notifyItemInserted(a);
        }

    }

    // 刪除項目
    public void removeItem(int position){
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
