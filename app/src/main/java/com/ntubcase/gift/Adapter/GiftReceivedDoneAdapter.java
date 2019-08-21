package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntubcase.gift.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftReceivedDoneAdapter extends RecyclerView.Adapter<com.ntubcase.gift.Adapter.GiftReceivedDoneAdapter.ViewHolder> implements Filterable {
        private Context context;
        private List<Map<String, Object>> re_giftList;
        private List<Map<String, Object>> originalitem;
        private List<Map<String, Object>> selectedTypeitem;
        private ArrayList<String> plansType; //所有計畫種類
        public static String selectedType; //spinner所選取的種類

        public GiftReceivedDoneAdapter(Context context, List<Map<String, Object>> re_giftList){
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
        public com.ntubcase.gift.Adapter.GiftReceivedDoneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.received_done_gift_layout, parent, false);
            return new com.ntubcase.gift.Adapter.GiftReceivedDoneAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(com.ntubcase.gift.Adapter.GiftReceivedDoneAdapter.ViewHolder holder, int position) {
            holder.image.setImageResource(R.drawable.opengift);
            holder.giftName.setText(re_giftList.get(position).get("title").toString());
            holder.sender.setText(re_giftList.get(position).get("sender").toString());
            holder.date.setText(re_giftList.get(position).get("date").toString());
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
