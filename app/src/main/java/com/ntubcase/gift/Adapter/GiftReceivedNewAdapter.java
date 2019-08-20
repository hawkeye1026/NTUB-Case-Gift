package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

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


public class GiftReceivedNewAdapter extends RecyclerView.Adapter<GiftReceivedNewAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Map<String, Object>> re_giftList;
    private List<Map<String, Object>> item;
    private List<Map<String, Object>> originalitem;
    private List<Map<String, Object>> selectedTypeitem;
    private ArrayList<String> plansType; //所有計畫種類
    public static String selectedType; //spinner所選取的種類

    public GiftReceivedNewAdapter(Context context, List<Map<String, Object>> re_giftList){
        this.context = context;
        this.re_giftList = re_giftList;
    }

    @Override
    public GiftReceivedNewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.received_new_gift_layout, parent, false);
        return new GiftReceivedNewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GiftReceivedNewAdapter.ViewHolder holder, int position) {
        holder.image.setImageResource(R.drawable.newgift);
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
                        originalitem = new ArrayList<Map<String, Object>>(item);
                    }
                }

                if(plansType.contains(selectedType)){ //篩選選取的type
                    selectedTypeitem = new ArrayList<Map<String, Object>>();
                    for(int i=0;i<originalitem.size();i++) {
                        String type = originalitem.get(i).get("type").toString();
                        String title = originalitem.get(i).get("title").toString();
                        String sender = originalitem.get(i).get("sender").toString();
                        String date = originalitem.get(i).get("date").toString();
                        if(type.equals(selectedType)){
                            Map<String, Object> itemContent = new HashMap<String, Object>();
                            itemContent.put("type", type);
                            itemContent.put("title", title);
                            itemContent.put("sender", sender);
                            itemContent.put("date", date);
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
                        if(title.contains(constraint)){
                            Map<String, Object> filteredItemContent = new HashMap<String, Object>();
                            filteredItemContent.put("type", type);
                            filteredItemContent.put("title", title);
                            filteredItemContent.put("sender", sender);
                            filteredItemContent.put("date", date);
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
                item = (List<Map<String, Object>>)results.values;
                if(results.count>0) notifyDataSetChanged();
            }
        };

        return filter;
    }
}
