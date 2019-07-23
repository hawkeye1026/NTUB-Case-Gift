package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.FriendActivity;
import com.ntubcase.gift.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Map<String, Object>> mFriendList;
    private List<Map<String, Object>> originalitem;

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
    public void onBindViewHolder(final FriendListAdapter.ViewHolder holder, int position) {
        //getBitmapFromURL(mFriendList.get(position).get("photo").toString());
        //if(mFriendList.get(position).get("photo").equals(null)){
        //    holder.iv_photo.setImageResource(R.drawable.ic_gift_camera);
        //}else {
        //    holder.iv_photo.setImageBitmap((Bitmap) mFriendList.get(position).get("photo"));
        //}
        //holder.iv_photo.setImageResource(R.drawable.ic_gift_camera);

        holder.iv_photo.setImageBitmap(getBitmapFromURL(mFriendList.get(position).get("photo").toString()));
        holder.tv_nickname.setText(mFriendList.get(position).get("nickname").toString());
        holder.tv_email.setText(mFriendList.get(position).get("email").toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() { //----------item點擊事件----------
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), holder.tv_nickname.getText(), Toast.LENGTH_SHORT).show();
            }
        });
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

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;

        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
            return null;
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
                        String photo = originalitem.get(i).get("photo").toString();
                        String nickname = originalitem.get(i).get("nickname").toString();
                        String email = originalitem.get(i).get("email").toString();
                        if(nickname.contains(constraint)){
                            Map<String, Object> filteredItemContent = new HashMap<String, Object>();
                            filteredItemContent.put("photo", photo);
                            filteredItemContent.put("nickname", nickname);
                            filteredItemContent.put("email", email);
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
                if(results.count>0) notifyDataSetChanged();
            }
        };

        return filter;
    }
}
