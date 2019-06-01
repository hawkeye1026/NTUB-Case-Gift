package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntubcase.gift.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftListAdapter extends BaseAdapter implements Filterable {

    private List<Map<String, Object>> item;
    private List<Map<String, Object>> originalitem;
    private List<Map<String, Object>> selectedTypeitem;
    private LayoutInflater mLayout;
    private ArrayList<String> giftsType; //所有禮物種類
    public static String selectedType; //spinner所選取的種類

    public GiftListAdapter(Context context, List<Map<String, Object>> mList){
        mLayout = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.item = mList;

        //---從strings取得所有禮物種類---
        Resources res = context.getResources();
        String[] mGiftStrings = res.getStringArray(R.array.gift_type);

        giftsType = new ArrayList<String>();
        for (int i=0; i<mGiftStrings.length; i++){
            giftsType.add(mGiftStrings[i]);
        }

    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        public TextView tvTitle;
        public ImageView ivGiftIcon;
        public TextView tvDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            //自定義的list布局
            convertView = mLayout.inflate(R.layout.giftlist_layout, parent,false);

            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_giftTitle);
            viewHolder.ivGiftIcon = (ImageView) convertView.findViewById(R.id.iv_giftIcon);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);

            convertView.setTag(viewHolder); //設置好的布局保存到緩存中，並將其設置在tag裡
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(item.get(position).get("title").toString()); //設定禮物名稱
        viewHolder.tvDate.setText(item.get(position).get("date").toString()); //設定日期

        String a =  item.get(position).get("type").toString();
        if(a.equals(giftsType.get(0))){
            viewHolder.ivGiftIcon.setImageResource(R.drawable.ic_gift_camera);
        }else if(a.equals(giftsType.get(1))){
            viewHolder.ivGiftIcon.setImageResource(R.drawable.ic_gift_video);
        }else if(a.equals(giftsType.get(2))){
            viewHolder.ivGiftIcon.setImageResource(R.drawable.ic_gift_message);
        }else if(a.equals(giftsType.get(3))){
            viewHolder.ivGiftIcon.setImageResource(R.drawable.ic_gift_ticket);
        }

        return convertView;
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

                if(giftsType.contains(selectedType)){ //篩選選取的type
                    selectedTypeitem = new ArrayList<Map<String, Object>>();
                    for(int i=0;i<originalitem.size();i++) {
                        String type = originalitem.get(i).get("type").toString();
                        String title = originalitem.get(i).get("title").toString();
                        String date = originalitem.get(i).get("date").toString();
                        if(type.equals(selectedType)){
                            Map<String, Object> itemContent = new HashMap<String, Object>();
                            itemContent.put("type", type);
                            itemContent.put("title", title);
                            itemContent.put("date", date);
                            selectedTypeitem.add(itemContent);
                        }
                    }
                }else if(!(giftsType.contains(selectedType))){  //選取"全部"種類
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
                        String date = selectedTypeitem.get(i).get("date").toString();
                        if(title.contains(constraint)){
                            Map<String, Object> filteredItemContent = new HashMap<String, Object>();
                            filteredItemContent.put("type", type);
                            filteredItemContent.put("title", title);
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
                if(results.count>0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }
}
