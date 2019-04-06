package com.ntubcase.gift.Adapter;

import android.content.Context;
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
    private LayoutInflater mLayout;

    public GiftListAdapter(Context context, List<Map<String, Object>> mList){
        mLayout = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.item = mList;
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

            convertView.setTag(viewHolder); //設置好的布局保存到緩存中，並將其設置在tag裡
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(item.get(position).get("title").toString());
        switch (item.get(position).get("type").toString()){
            case "1":
                viewHolder.ivGiftIcon.setImageResource(R.drawable.ic_gift_camera);
                break;
            case "2":
                viewHolder.ivGiftIcon.setImageResource(R.drawable.ic_gift_video);
                break;
            case "3":
                viewHolder.ivGiftIcon.setImageResource(R.drawable.ic_gift_ticket);
                break;
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
                if(constraint != null && constraint.toString().length()>0){
                    List<Map<String, Object>> filteredItem = new ArrayList<Map<String, Object>>();
                    for(int i=0;i<originalitem.size();i++){
                        String type = originalitem.get(i).get("type").toString();
                        String title = originalitem.get(i).get("title").toString();
                        if(title.contains(constraint)){
                            Map<String, Object> filteredItemContent = new HashMap<String, Object>();
                            filteredItemContent.put("type", type);
                            filteredItemContent.put("title", title);
                            filteredItem.add(filteredItemContent);
                        }
                    }
                    result.count = filteredItem.size();
                    result.values = filteredItem;
                }else{
                    synchronized (this){
                        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(originalitem);
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
