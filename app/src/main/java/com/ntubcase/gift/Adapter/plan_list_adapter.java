package com.ntubcase.gift.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.ntubcase.gift.R;


import java.util.List;
import java.util.Map;


public class plan_list_adapter extends RecyclerView.Adapter<plan_list_adapter.ViewHolder>{
    //private List<Map<String, Object>> mData;
    private List<String> mData;

    public plan_list_adapter(List<String> data) {
        mData = data;
    }


    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private EditText editText;
        private Button btnRemove ,btnCheck;
        private CheckBox checkBox;
        String message;

        ViewHolder(View itemView) {
            super(itemView);
            editText = (EditText) itemView.findViewById(R.id.editText);
            btnRemove= (Button) itemView.findViewById(R.id.btn_delete_item);
            btnCheck= (Button) itemView.findViewById(R.id.btn_check_item);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);

            // 點擊項目中的Button時
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 移除項目，getAdapterPosition為點擊的項目位置
                    mData.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

            btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   mData.set(getAdapterPosition(),editText.getText().toString());
                   notifyDataSetChanged();
                }
            });

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

        holder.editText.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
