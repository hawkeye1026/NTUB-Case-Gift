package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntubcase.gift.GiftReceivedActivity;
import com.ntubcase.gift.R;
import com.ntubcase.gift.ReceivedListActivity;
import com.ntubcase.gift.ReceivedMultipleActivity;
import com.ntubcase.gift.ReceivedSingleActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftReceivedProcessAdapter extends RecyclerView.Adapter<GiftReceivedProcessAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Map<String, Object>> re_giftList;
    private List<Map<String, Object>> originalitem;
    private List<Map<String, Object>> selectedTypeitem;
    private ArrayList<String> plansType; //所有計畫種類
    public static String selectedType; //spinner所選取的種類

    public GiftReceivedProcessAdapter(Context context, List<Map<String, Object>> re_giftList){
        this.context = context;
        this.re_giftList = re_giftList;

        //---從strings取得所有計畫種類---
        Resources res = context.getResources();
        String[] mPlanStrings = res.getStringArray(R.array.plan_type);

        plansType = new ArrayList<String>();
        for (int i=0; i<mPlanStrings.length; i++){
            plansType.add(mPlanStrings[i]);
        }
    }

    @Override
    public GiftReceivedProcessAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.received_process_gift_layout, parent, false);
        return new GiftReceivedProcessAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GiftReceivedProcessAdapter.ViewHolder holder, final int position) {
        holder.image.setImageResource(R.drawable.opengift);
        holder.giftName.setText(re_giftList.get(position).get("title").toString());
        holder.sender.setText(re_giftList.get(position).get("sender").toString());
        holder.date.setText(re_giftList.get(position).get("date").toString());
        String type = re_giftList.get(position).get("type").toString();
        switch (type){
            case "單日送禮" :
                holder.image.setImageResource(R.drawable.s_received_mid);
                break;
            case "多日規劃" :
                holder.image.setImageResource(R.drawable.c_received_mid);
                break;
            case "任務清單" :
                holder.image.setImageResource(R.drawable.l_received_mid);
                break;
        }
        //--------------item點擊監聽器-------------
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                String planID = re_giftList.get(position).get("planID").toString();
                String type = re_giftList.get(position).get("type").toString();
                switch (type){
                    case "單日送禮" :
                        intent = new Intent(context, ReceivedSingleActivity.class);
                        break;
                    case "多日規劃" :
                        intent = new Intent(context, ReceivedMultipleActivity.class);
                        break;
                    case "任務清單" :
                        intent = new Intent(context, ReceivedListActivity.class);
                        break;
                }

                bundle.putString("planID", planID);
                bundle.putString("from", "GiftReceivedProcess");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return re_giftList.size();
    }

    //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView giftName,sender,date;
        ImageView image;
        ViewHolder(View itemView) {
            super(itemView);
            giftName = (TextView) itemView.findViewById(R.id.tv_giftname);
            sender=(TextView) itemView.findViewById(R.id.tv_sender);
            date=(TextView) itemView.findViewById(R.id.tv_date);
            image = (ImageView) itemView.findViewById(R.id.iv_photo);
        }
    }

    @Override
    public Filter getFilter() { //過濾器
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString();
                FilterResults result = new FilterResults();

                if(originalitem == null){
                    synchronized (this){
                        originalitem = new ArrayList<Map<String, Object>>(re_giftList);
                    }
                }

                if(plansType.contains(selectedType)){ //篩選選取的type
                    selectedTypeitem = new ArrayList<Map<String, Object>>();
                    for(int i=0;i<originalitem.size();i++) {
                        String type = originalitem.get(i).get("type").toString();
                        String title = originalitem.get(i).get("title").toString();
                        String sender = originalitem.get(i).get("sender").toString();
                        String date = originalitem.get(i).get("date").toString();
                        String planID = originalitem.get(i).get("planID").toString();

                        if(type.equals(selectedType)){
                            Map<String, Object> itemContent = new HashMap<String, Object>();
                            itemContent.put("type", type);
                            itemContent.put("title", title);
                            itemContent.put("sender", sender);
                            itemContent.put("date", date);
                            itemContent.put("planID", planID);
                            selectedTypeitem.add(itemContent);
                        }
                    }
                }else if(!(plansType.contains(selectedType))){  //選取"全部"種類
                    synchronized (this){
                        selectedTypeitem = new ArrayList<Map<String, Object>>(originalitem);
                        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(originalitem);
                        result.values = list;
                        result.count = list.size();
                    }
                }

                if(constraint != null && constraint.toString().length()>0){
                    List<Map<String, Object>> filteredItem = new ArrayList<Map<String, Object>>();
                    for(int i=0;i<selectedTypeitem.size();i++){
                        String type = selectedTypeitem.get(i).get("type").toString();
                        String title = selectedTypeitem.get(i).get("title").toString();
                        String sender = selectedTypeitem.get(i).get("sender").toString();
                        String date = selectedTypeitem.get(i).get("date").toString();
                        String planID = selectedTypeitem.get(i).get("planID").toString();

                        if(sender.contains(constraint)){
                            Map<String, Object> filteredItemContent = new HashMap<String, Object>();
                            filteredItemContent.put("type", type);
                            filteredItemContent.put("title", title);
                            filteredItemContent.put("sender", sender);
                            filteredItemContent.put("date", date);
                            filteredItemContent.put("planID", planID);
                            filteredItem.add(filteredItemContent);
                        }
                    }
                    result.count = filteredItem.size();
                    result.values = filteredItem;
                }else{
                    synchronized (this){
                        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(selectedTypeitem);
                        result.values = list;
                        result.count = list.size();

                    }
                }

                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                re_giftList = (List<Map<String, Object>>)results.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }
}
