package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.R;

import java.util.List;

public class plan_list_adapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<String> data;
    private int resource;

    public plan_list_adapter(List<String> data, int resource) {
        this.data = data;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (context == null) {
            context = viewGroup.getContext();
        }

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, null);
            viewHolder = new ViewHolder();
            //viewHolder.tvItemContent = view.findViewById(R.id.tv_item_content);
            viewHolder.btnDeleteItem = view.findViewById(R.id.btn_delete_item);
            viewHolder.checkbox = view.findViewById(R.id.checkbox);
            viewHolder.edittext = view.findViewById(R.id.editText);
            view.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) view.getTag();

        viewHolder.btnDeleteItem.setOnClickListener(this);
        viewHolder.btnDeleteItem.setTag(i);

        //viewHolder.tvItemContent.setText(data.get(i));
        //viewHolder.tvItemContent.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_item_content:
//                String message = ((TextView) view).getText().toString();
//                Toast.makeText(context, message + " clicked", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.btn_delete_item:
                data.remove((int) view.getTag());
                notifyDataSetChanged();
                break;
        }
    }

    static class ViewHolder {
        public View checkbox;
        public View edittext;
        //TextView tvItemContent;
        Button btnDeleteItem;

    }

}

