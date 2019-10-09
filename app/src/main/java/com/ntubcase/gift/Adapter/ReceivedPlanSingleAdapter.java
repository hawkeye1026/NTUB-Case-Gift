package com.ntubcase.gift.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ntubcase.gift.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReceivedPlanSingleAdapter extends RecyclerView.Adapter<ReceivedPlanSingleAdapter.ViewHolder> {
    private List<Map<String, Object>> mData;
    private ReceivedPlanSingleAdapter.OnItemClickListener mOnItemClickListener;
    public static boolean isFromMake = true;
    private SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm");
    public ReceivedPlanSingleAdapter(List<Map<String, Object>> data) {
        mData = data;
    }
    private Button btn_Received;
    private TextView txtSentTime;


    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private TextView txtTime,txtGift,txtMessage;

        ViewHolder(View itemView) {
            super(itemView);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtGift = (TextView) itemView.findViewById(R.id.txtGift);
            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            btn_Received = (Button) itemView.findViewById(R.id.btnReceived);
            txtSentTime = (TextView) itemView.findViewById(R.id.sentTime);
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
        // 設置txtItem要顯示的內容
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() { //----------item點擊事件----------
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        String giftName = mData.get(position).get("giftName").toString();
        String sentTime = mData.get(position).get("sentTime").toString();
        String message = mData.get(position).get("message").toString();
        String type = mData.get(position).get("type").toString();

        if(IsGtTenHours(sentTime)){
            switch (type) {
                case "1":
                    holder.itemView.setBackgroundResource(R.drawable.image_video);
                    break;
                case "2":
                    holder.itemView.setBackgroundResource(R.drawable.image_video);
                    break;
                case "3":
                    holder.itemView.setBackgroundResource(R.drawable.whisper);
                    break;
                case "4":
                    holder.itemView.setBackgroundResource(R.drawable.ticket);
                    break;
                case "5":
                    holder.itemView.setBackgroundResource(R.drawable.code);
                    break;
            }
        }else{

            switch (type) {
                case "1":
                    holder.itemView.setBackgroundResource(R.drawable.lock_image_video);
                    break;
                case "2":
                    holder.itemView.setBackgroundResource(R.drawable.lock_image_video);
                    break;
                case "3":
                    holder.itemView.setBackgroundResource(R.drawable.lock_whisper);
                    break;
                case "4":
                    holder.itemView.setBackgroundResource(R.drawable.lock_ticket);
                    break;
                case "5":
                    holder.itemView.setBackgroundResource(R.drawable.lock_code);
                    break;
            }
            this.btn_Received.setVisibility(View.GONE);
            this.txtSentTime.setVisibility(View.VISIBLE);
        }

        holder.txtTime.setText(sentTime);
        holder.txtGift.setText(giftName);
        holder.txtMessage.setText(message);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void setOnItemClickListener(ReceivedPlanSingleAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public boolean IsGtTenHours(String sentTime) {
        Calendar t = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date ten = null;
        Date now = null;
        String nowTime = t.get(Calendar.HOUR_OF_DAY)+":"+ t.get(Calendar.MINUTE);
        try {
            now = format.parse(nowTime);
            Log.e("now", String.valueOf(now));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try{
            ten = format.parse(sentTime);
            Log.e("ten", String.valueOf(ten));
        } catch(ParseException e){
            e.printStackTrace();
        }


        if(ten.before(now)){
            return true;
        }
        return false;
    }



}
