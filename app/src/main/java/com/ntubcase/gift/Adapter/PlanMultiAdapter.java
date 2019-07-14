package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ntubcase.gift.R;

import java.util.List;
import java.util.Map;

public class PlanMultiAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> selectDates;

    public PlanMultiAdapter(Context context, List<Map<String, Object>> selectDates) {
        this.context = context;
        this.selectDates = selectDates;
    }

    @Override
    public int getCount() {
        return selectDates.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        public TextView tv_date;
        public TextView tv_content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_plan_multi, null);

            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_date.setText(selectDates.get(position).get("date").toString());
        holder.tv_content.setText(selectDates.get(position).get("message").toString()+"\n"+selectDates.get(position).get("time").toString());

        return convertView;
    }
}
