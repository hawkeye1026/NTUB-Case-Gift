package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ntubcase.gift.R;

import java.util.List;
import java.util.Map;

public class PlanMultiAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> selectDates;
    private int numColumns = 3; //總列數
    private int lines; //總行數
    private int[] rowsMaxHeight; //每行最高高度

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

            //---計算總行數---
            if (lines < 1){
                if(getCount() % numColumns > 0) lines = getCount() / numColumns  + 1;
                else lines = getCount() / numColumns;
                rowsMaxHeight = new int[lines];
            }

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_date.setText(selectDates.get(position).get("date").toString());
        holder.tv_content.setText(selectDates.get(position).get("message").toString()+"\n"+selectDates.get(position).get("time").toString());

        return convertView;
    }

    //----------------設定grid高度-------------------------
    public void setItemHeight(final View convertView, final int position, final ViewGroup parent, final GridView gridView){
        convertView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int positionRow = position / numColumns;  //此position的所在行數
                        int height = convertView.getHeight(); //此view高度

                        if(height < rowsMaxHeight[positionRow]){    //高度小於同行最高
                            convertView.setLayoutParams(new GridView.LayoutParams(
                                    GridView.LayoutParams.MATCH_PARENT,
                                    rowsMaxHeight[positionRow]));
                        } else if(height > rowsMaxHeight[positionRow]){ //高度大於同行最高
                            rowsMaxHeight[positionRow] = height;

                            for (int i = positionRow * numColumns; i < ((positionRow+1) * numColumns) && i< getCount(); i++){
                                View view = parent.getChildAt(i- gridView.getFirstVisiblePosition());
                                if (view.getHeight() != rowsMaxHeight[positionRow]){
                                    view.setLayoutParams(new GridView.LayoutParams(
                                            GridView.LayoutParams.MATCH_PARENT,
                                            rowsMaxHeight[positionRow]));
                                }
                            }
                        }

                        Log.e("***","position: " + position +"; height: " +  height + "; maxHeight: " +  rowsMaxHeight[positionRow]);
                        for (int j=0; j<rowsMaxHeight.length; j++) Log.e("***", j+ ": " + rowsMaxHeight[j]);

                        convertView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    //------刷新單一item-----
    public void refreshOneView(GridView gridView, int position) {
        int start = gridView.getFirstVisiblePosition();
        int last = gridView.getLastVisiblePosition();
        for (int i = start, j = last; i <= j; i++) {
            if (position == i) {
                View convertView = gridView.getChildAt(position - start);
                if (convertView != null) {
                    getView(position, convertView, gridView);
                    break;
                }
            }
        }
    }

}
