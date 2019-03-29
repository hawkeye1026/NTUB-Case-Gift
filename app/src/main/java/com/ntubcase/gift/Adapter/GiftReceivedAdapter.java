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

import java.util.ArrayList;
import java.util.List;

public class GiftReceivedAdapter extends BaseAdapter implements Filterable {

    private List<List<String>> item;
    private List<List<String>> originalitem;
    private LayoutInflater mLayout;

    public GiftReceivedAdapter(Context context, List<List<String>> mList){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mLayout.inflate(R.layout.giftreceived_layout,parent,false);

        TextView sentBy = (TextView)v.findViewById(R.id.tv_sentBy);
        TextView title = (TextView)v.findViewById(R.id.tv_giftTitle);
        ImageView giftIcon = (ImageView) v.findViewById(R.id.iv_giftIcon);

        sentBy.setText("From : "+item.get(position).get(2).toString());
        title.setText(item.get(position).get(1).toString());

        switch (item.get(position).get(0).toString()){
            case "1":
                giftIcon.setImageResource(R.drawable.ic_surprise);
                break;
            case "2":
                giftIcon.setImageResource(R.drawable.ic_calendar);
                break;
            case "3":
                giftIcon.setImageResource(R.drawable.ic_qa);
                break;
        }

        return v;
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
                        originalitem = new ArrayList<List<String>>(item);
                    }
                }
                if(constraint != null && constraint.toString().length()>0){
                    List<List<String>> filteredItem = new ArrayList<List<String>>();
                    for(int i=0;i<originalitem.size();i++){
                        String type = originalitem.get(i).get(0).toString();
                        String title = originalitem.get(i).get(1).toString();
                        String sentby = originalitem.get(i).get(2).toString();
                        if(title.contains(constraint)){
                            List<String> filteredItemContent = new ArrayList<String>();
                            filteredItemContent.add(type);
                            filteredItemContent.add(title);
                            filteredItemContent.add(sentby);
                            filteredItem.add(filteredItemContent);
                        }
                    }
                    result.count = filteredItem.size();
                    result.values = filteredItem;
                }else{
                    synchronized (this){
                        List<List<String>> list = new ArrayList<List<String>>(originalitem);
                        result.values = list;
                        result.count = list.size();

                    }
                }

                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                item = (ArrayList<List<String>>)results.values;
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
