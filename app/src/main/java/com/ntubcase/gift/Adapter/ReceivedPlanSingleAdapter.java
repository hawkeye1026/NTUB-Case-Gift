package com.ntubcase.gift.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ntubcase.gift.R;
import com.ntubcase.gift.ReceivedShowGiftActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReceivedPlanSingleAdapter extends RecyclerView.Adapter<ReceivedPlanSingleAdapter.ViewHolder> {
    private Context context;
    private List<Map<String, Object>> mData;
    public static boolean isFromMake = true;
    private SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm");

    public ReceivedPlanSingleAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        mData = data;
    }

    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        TextView txtTime, txtMessage;

        ViewHolder(View itemView) {
            super(itemView);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
        }
    }

    @Override
    public ReceivedPlanSingleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 連結項目布局檔list_item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.re_single_item, parent, false);
        return new ReceivedPlanSingleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String sentTime = mData.get(position).get("sentTime").toString();
        String message = mData.get(position).get("message").toString();
        final List<String> giftContent = (List<String>)mData.get(position).get("giftContent");
        final List<String> giftType = (List<String>)mData.get(position).get("giftType");

        if(IsGtTenHours(sentTime)){ //已過領取時間
            holder.itemView.setOnClickListener(new View.OnClickListener() { //領取禮物
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReceivedShowGiftActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("giftContent", (Serializable) giftContent);
                    bundle.putSerializable("giftType", (Serializable) giftType);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            
        }else{
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"領取時間還沒到喔",Toast.LENGTH_SHORT).show();
                }
            });
        }



        holder.txtTime.setText(sentTime);
        holder.txtMessage.setText(message);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public boolean IsGtTenHours(String sentTime) {
        Calendar t = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date ten = null;
        Date now = null;
        String nowTime = t.get(Calendar.HOUR_OF_DAY)+":"+ t.get(Calendar.MINUTE);
        try {
            now = format.parse(nowTime);
            //Log.e("now", String.valueOf(now));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try{
            ten = format.parse(sentTime);
            //Log.e("ten", String.valueOf(ten));
        } catch(ParseException e){
            e.printStackTrace();
        }

        if(ten.before(now)){
            return true;
        }
        return false;
    }

}
