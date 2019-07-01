package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.R;

import java.util.List;
import java.util.Map;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder>{
    private Context context;
    private List<Map<String, Object>> mFriendList;

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

        holder.iv_photo.setImageResource(R.drawable.ic_gift_camera);
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
}
