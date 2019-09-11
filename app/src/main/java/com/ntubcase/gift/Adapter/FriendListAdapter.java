package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.CircleTransform;
import com.ntubcase.gift.Common.Common;
import com.ntubcase.gift.FriendActivity;
import com.ntubcase.gift.FriendAddActivity;
import com.ntubcase.gift.MyAsyncTask.friend.friendInsertAsyncTask;
import com.ntubcase.gift.R;
import com.ntubcase.gift.data.getFriendList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Map<String, Object>> mFriendList;
    private List<Map<String, Object>> originalitem;
    //----------------------------------------------------------------------------
    private OnItemClickListener mOnItemClickListener;
    private View actionBarView;  //多選模式中的action bar
    private TextView selectedNum;  //顯示選中個項目個數
    private boolean isCachedBackground = false;
    private ColorStateList mBackground;

    public FriendListAdapter(Context context, List<Map<String, Object>> mFriendList) {
        this.context = context;
        this.mFriendList = mFriendList;
    }

    @Override
    public FriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friendlist_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FriendListAdapter.ViewHolder holder, final int position) {

        //-------圖片網址 getGift(n) 取得第n筆資料的禮物資料
        Uri imageURI = Uri.parse(mFriendList.get(position).get("imgURL").toString());
        if (imageURI!=null && !imageURI.toString().equals("")){
            Picasso.get().load(imageURI)
                    .transform(new CircleTransform())
                    .into(holder.iv_photo);
        }else{
            holder.iv_photo.setImageResource(R.drawable.ic_user);
        }

        holder.tv_nickname.setText(mFriendList.get(position).get("nickname").toString());
        holder.tv_email.setText(mFriendList.get(position).get("email").toString());

        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() { //----------item點擊事件----------
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

        if (!multiSelect){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { //-----長按事件(刪除多選模式)-----
                @Override
                public boolean onLongClick(View v) {
                    multiSelect = true; //開啟多選模式
                    ((AppCompatActivity)v.getContext()).startSupportActionMode(actionModeCallbacks);
                    selectItem(position);
                    notifyDataSetChanged();
                    return true;
                }
            });
        }else{
            holder.itemView.setOnLongClickListener(null);
        }

        //---緩存原本的background
        if (!isCachedBackground) {
            isCachedBackground = true;
            mBackground = ((CardView)holder.itemView).getCardBackgroundColor();
        }
        updateBackground(position, holder.itemView); //設定背景

    }

    //-----設定背景-----
    public void updateBackground(int position, View convertView) {
        CardView cardView = (CardView)convertView;
        if(selectedItems.contains(position)) {
            cardView.setCardBackgroundColor(0xFF7CA2C2);
        } else {
            cardView.setCardBackgroundColor(mBackground);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_photo;
        TextView tv_nickname, tv_email;

        ViewHolder(View itemView) {
            super(itemView);
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_photo);
            tv_nickname = (TextView) itemView.findViewById(R.id.tv_nickname);
            tv_email = (TextView) itemView.findViewById(R.id.tv_email);
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
                        originalitem = new ArrayList<Map<String, Object>>(mFriendList);
                    }
                }

                if(constraint != null && constraint.toString().length()>0){
                    List<Map<String, Object>> filteredItem = new ArrayList<Map<String, Object>>();
                    for(int i=0;i<originalitem.size();i++){
                        String friendID = originalitem.get(i).get("friendID").toString();
                        String nickname = originalitem.get(i).get("nickname").toString();
                        String email = originalitem.get(i).get("email").toString();
                        String imgURL = originalitem.get(i).get("imgURL").toString();
                        if(nickname.contains(constraint)){
                            Map<String, Object> filteredItemContent = new HashMap<String, Object>();
                            filteredItemContent.put("friendID", friendID);
                            filteredItemContent.put("nickname", nickname);
                            filteredItemContent.put("email", email);
                            filteredItemContent.put("imgURL", imgURL);
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
                mFriendList = (List<Map<String, Object>>)results.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }

    //----------------------刪除，多選模式的監聽器----------------------
    public boolean multiSelect = false; //是否開啟多選模式
    private ArrayList<Integer> selectedItems = new ArrayList<Integer>(); //選取的項目

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        //-----初始化ActionBar-----
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            ((FriendActivity)context).getMenuInflater().inflate(R.menu.menu_multi_choice, menu);
            if (actionBarView == null) {
                actionBarView = LayoutInflater.from((FriendActivity)context).inflate(R.layout.delete_actionbar_layout, null);
                selectedNum = (TextView) actionBarView.findViewById(R.id.selected_num);
            }
            mode.setCustomView(actionBarView);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        //-----點選ActionBar的item-----
        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            new AlertDialog.Builder(context)
                    .setTitle("確定要刪除選取的好友嗎?")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //-----刪除禮物-----
                            Collections.sort(selectedItems); //按position排序

                            Log.v("mFriendList", String.valueOf(mFriendList));
                            for (int i=selectedItems.size()-1; i>=0; i--){
                                friendInsertAsyncTask friendInsertAsyncTask = new friendInsertAsyncTask(new friendInsertAsyncTask.TaskListener() {
                                    @Override
                                    public void onFinished(String result) {
                                    }
                                });
                                friendInsertAsyncTask.execute(Common.deleteFriend , "1", mFriendList.get(selectedItems.get(i)).get("friendID").toString());
                                mFriendList.remove((int)selectedItems.get(i));
                            }
                            mode.finish();
                            getFriendList.getJSON();
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            return true;
        }

        //-----退出多選模式-----
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };

    public void selectItem(Integer item) {
        if (multiSelect) {
            if (selectedItems.contains(item)) {
                selectedItems.remove(item);
            } else {
                selectedItems.add(item);
            }
            selectedNum.setText("" + selectedItems.size());
        }
    }

}
