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

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class PlanMultiAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> selectDates;
    private int numColumns = 3; //總列數
    private int lines; //總行數
    private int[] rowsMaxHeight; //每行最高高度

    private static int defaultHeight = 0;
    private static int maxHeight = 0 ;
    private boolean[][] isRowHaveData;

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
                isRowHaveData = new boolean[lines][numColumns];
            }

            setDefaultHeight(convertView,position); //取得預設高度

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_date.setText(selectDates.get(position).get("date").toString());
        holder.tv_content.setText(selectDates.get(position).get("message").toString()+"\n"+selectDates.get(position).get("time").toString());

        setItemHeight(convertView,position);  //設定高度

//        Log.e("getView","position: " + position +"; height: " +  convertView.getHeight());

        return convertView;
    }

    //----------------設定預設高度-------------------------
    private void setDefaultHeight(final View convertView, final int position){
        convertView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int positionRow = position / numColumns;  //此position的所在行數
                        int height = convertView.getHeight(); //此view高度

                        if (rowsMaxHeight[positionRow] == 0 && height>0){
                            for (int i=0; i<rowsMaxHeight.length; i++){
                                rowsMaxHeight[i] = height;
                            }
                            if (defaultHeight == 0){
                                defaultHeight = height;
                                maxHeight = defaultHeight * 2;
                            }
                        }
                        convertView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    //----------------設定grid高度-------------------------
    private void setItemHeight(final View convertView, final int position){
        int positionRow = position / numColumns;  //此position的所在行數
        int height = convertView.getHeight(); //此view高度

        if(height != rowsMaxHeight[positionRow]){  //高度不等於行高
            convertView.setLayoutParams(new GridView.LayoutParams(
                    GridView.LayoutParams.MATCH_PARENT,
                    rowsMaxHeight[positionRow]));
            Log.e("setItemHeight","position: " + position +" ; " + rowsMaxHeight[positionRow]);
        }
    }

//    //----------------設定grid高度-------------------------
//    private void setItemHeight(final View convertView, final int position){
//        convertView.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        int positionRow = position / numColumns;  //此position的所在行數
//                        int height = convertView.getHeight(); //此view高度
//
//                        if(height != rowsMaxHeight[positionRow]){  //高度不等於行高
//                            convertView.setLayoutParams(new GridView.LayoutParams(
//                                    GridView.LayoutParams.MATCH_PARENT,
//                                    rowsMaxHeight[positionRow]));
//                            //Log.e("setItemHeight","之前position: " + position +"; height: " +  height);
//                        }
//
//                        convertView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
////                        Log.e("setItemHeight","position: " + position +"; height: " +  height);
//                        Log.e("setItemHeight","setItemHeight");
//                    }
//                });
//    }

    public void checkMaxHeight(boolean isHaveData, int position){
        int positionRow = position / numColumns;  //此position的所在行數
        int positionColumn = position % numColumns; //此position的所在欄數

        if (isHaveData && (!isRowHaveData[positionRow][positionColumn])){  //無資料至有資料
            isRowHaveData[positionRow][positionColumn] = true;
            rowsMaxHeight[positionRow] = maxHeight;
            Log.e("checkMaxHeight", "無資料至有資料 "+rowsMaxHeight[positionRow]);
        }else if((!isHaveData) && isRowHaveData[positionRow][positionColumn]){  //有資料至無資料
            isRowHaveData[positionRow][positionColumn] = false;
            rowsMaxHeight[positionRow] = defaultHeight;

            for (boolean i : isRowHaveData[positionRow]){
                if (i) rowsMaxHeight[positionRow] = maxHeight;  //檢查同行中是否有資料
            }
            Log.e("checkMaxHeight", "有資料至無資料 "+rowsMaxHeight[positionRow]);
        }

        for (int j=0; j<rowsMaxHeight.length; j++) Log.e("***", j+ ": " + rowsMaxHeight[j]);
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
