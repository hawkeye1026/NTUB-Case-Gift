package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.MyAsyncTask.gift.delete.giftDeleteAsyncTask;
import com.ntubcase.gift.MyAsyncTask.plan.planDetailAsyncTask;
import com.ntubcase.gift.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanListAdapter extends BaseAdapter implements Filterable{

    private LayoutInflater mLayout;
    private List<Map<String, Object>> item;
    private List<Map<String, Object>> originalitem;
    private List<Map<String, Object>> selectedTypeitem;
    private ArrayList<String> plansType; //所有計畫種類
    public static String selectedType; //spinner所選取的種類
    //------------------------------------
    private boolean isCachedBackground = false;
    private Drawable mBackground;

    private ListView mListView;

    public PlanListAdapter(Context context, List<Map<String, Object>> mList){
        mLayout = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.item = mList;

        //---從strings取得所有計畫種類---
        Resources res = context.getResources();
        String[] mPlanStrings = res.getStringArray(R.array.plan_type);

        plansType = new ArrayList<String>();
        for (int i=0; i<mPlanStrings.length; i++){
            plansType.add(mPlanStrings[i]);
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
        public ImageView ivPlanIcon;
        public TextView tvDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlanListAdapter.ViewHolder viewHolder;
        if(convertView == null){
            mListView=(ListView)parent;
            viewHolder = new PlanListAdapter.ViewHolder();
            //自定義的list布局
            convertView = mLayout.inflate(R.layout.planlist_layout, parent,false);

            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_planTitle);
            viewHolder.ivPlanIcon = (ImageView) convertView.findViewById(R.id.iv_planIcon);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);

            convertView.setTag(viewHolder); //設置好的布局保存到緩存中，並將其設置在tag裡
        }else{
            viewHolder = (PlanListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(item.get(position).get("title").toString());
        viewHolder.tvDate.setText(item.get(position).get("date").toString());

        String a =  item.get(position).get("type").toString();
        if(a.equals(plansType.get(0))){
            viewHolder.ivPlanIcon.setImageResource(R.drawable.ic_plan_surprise);
        }else if(a.equals(plansType.get(1))){
            viewHolder.ivPlanIcon.setImageResource(R.drawable.ic_plan_calendar);
        }else if(a.equals(plansType.get(2))){
            viewHolder.ivPlanIcon.setImageResource(R.drawable.ic_plan_list);
        }

        //---緩存原本的background
        if (!isCachedBackground) {
            isCachedBackground = true;
            mBackground = convertView.getBackground();
        }

        updateBackground(position, convertView); //設定背景

        return convertView;
    }

    //-----設定背景-----
    private void updateBackground(int position,View convertView) {
        if(mListView.isItemChecked(position)) {
            convertView.setBackgroundColor(0xFFDFDFDF);
        } else {
            convertView.setBackgroundDrawable(mBackground);
        }
    }

    @Override
    public boolean hasStableIds() {
        return true ;
    }

    //-----刪除item-----
    public void deleteItems(){
        long[] checkedItems=mListView.getCheckedItemIds(); //取得勾選的項目
        for (int i=checkedItems.length-1; i>=0; i--){

            planDetailAsyncTask planDetailAsyncTask = new planDetailAsyncTask(new planDetailAsyncTask.TaskListener() {
                @Override
                public void onFinished(String result) {

                }
            });
            //planDetailAsyncTask.execute(Common.deletedPlan, userid, planid);

            item.remove((int)checkedItems[i]);
        }
        notifyDataSetChanged();
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
                        String planID = originalitem.get(i).get("planID").toString();
                        String type = originalitem.get(i).get("type").toString();
                        String title = originalitem.get(i).get("title").toString();
                        String date = originalitem.get(i).get("date").toString();
                        if(type.equals(selectedType)){
                            Map<String, Object> itemContent = new HashMap<String, Object>();
                            itemContent.put("planID", planID);
                            itemContent.put("type", type);
                            itemContent.put("title", title);
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
                        String planID = selectedTypeitem.get(i).get("planID").toString();
                        String type = selectedTypeitem.get(i).get("type").toString();
                        String title = selectedTypeitem.get(i).get("title").toString();
                        String date = selectedTypeitem.get(i).get("date").toString();
                        if(title.contains(constraint)){
                            Map<String, Object> filteredItemContent = new HashMap<String, Object>();
                            filteredItemContent.put("planID", planID);
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

    public List<Map<String, Object>> getItem(){
        return item;
    }
}
