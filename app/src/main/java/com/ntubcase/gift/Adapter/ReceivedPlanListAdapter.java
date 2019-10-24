package com.ntubcase.gift.Adapter;

import android.support.v7.widget.RecyclerView;
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
import java.util.Map;


public class ReceivedPlanListAdapter extends RecyclerView.Adapter<ReceivedPlanListAdapter.ViewHolder>{
    private List<Map<String, String>> missionData = new ArrayList<Map<String, String>>(); //任務清單資料
    private List<Boolean> missionCheck = new ArrayList<Boolean>();

    private OnItemClickListener mOnItemClickListener;

    public ReceivedPlanListAdapter(List<Map<String, String>> data) {
        missionData = data;
    }

    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        TextView textView;
        Button btnRemove;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.Textview);
            btnRemove= (Button) itemView.findViewById(R.id.btn_delete_item);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_mission);
            checkBox.setVisibility(View.VISIBLE);
            btnRemove.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.GONE);
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
    public void onBindViewHolder(final ReceivedPlanListAdapter.ViewHolder holder, final int position) {
        // 設置txtItem要顯示的內容
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() { //----------item點擊事件----------
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        //顯示任務清單內容
        String mission = missionData.get(position).get("itemContent");
        holder.checkBox.setText(mission);

        //設定任務是否勾選
        String isCheck = missionData.get(position).get("itemChecked");
        if (isCheck.equals("")){
            holder.checkBox.setChecked(false);
            missionCheck.add(false);
        }else {
            holder.checkBox.setChecked(true);
            missionCheck.add(true);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //點選checkbox
                if (isChecked) missionCheck.set(position, true);
                else missionCheck.set(position, false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return missionData.size();
    }

    public void setOnItemClickListener(ReceivedPlanListAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //-----檢查任務清單是否都完成---
    public boolean isMissionComplete(){
        int count=0;
        for (int i=0; i<missionCheck.size(); i++){
            if (missionCheck.get(i)) count++;
        }
        
        if (count == missionCheck.size()) return true;
        return false;
    }

    public List<Boolean> getMissionCheck(){
        return missionCheck;
    }
}
