package com.jusfoun.jusfouninquire.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jusfoun.jusfouninquire.R;
import com.jusfoun.jusfouninquire.net.model.HomeRecentModel;
import com.jusfoun.jusfouninquire.ui.activity.RecentChangeActivity;
import com.jusfoun.jusfouninquire.ui.view.RadarCountView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:18/2/2709:45
 * @Email zyp@jusfoun.com
 * @Description ${首页雷达adapter}
 */
public class RaderAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<HomeRecentModel.RecentBean> list;

    private LayoutInflater mLayoutInflater;

    private int[] colors = {0xffeb9720, 0xff4860bb, 0xff7060b8, 0xff0294ba, 0xff009370};

    public RaderAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RaderViewHolder(mLayoutInflater.inflate(R.layout.item_rader, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RaderViewHolder) holder).update(list.get(position % list.size()), position);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    public class RaderViewHolder extends RecyclerView.ViewHolder {


        private TextView des;

        private RadarCountView radarCountView;

        public RaderViewHolder(View itemView) {
            super(itemView);
            des = (TextView) itemView.findViewById(R.id.text_des);
            radarCountView = (RadarCountView) itemView.findViewById(R.id.view_radra);
        }

        public void update(final HomeRecentModel.RecentBean bean, int position) {
            if (list.size() < 5) {
                radarCountView.setIndex(position % list.size());
            } else {
                radarCountView.setIndex(position % 5);
            }

            radarCountView.setData(bean.count + "");
            des.setText("家企业进行了" + bean.title);

            if (list.size() < 5) {
                des.setTextColor(colors[position % list.size()]);
            } else {
                des.setTextColor(colors[position % 5]);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, RecentChangeActivity.class);
                    intent.putExtra("bean", new Gson().toJson(bean));
                    mContext.startActivity(intent);
                }
            });


        }

    }

    public void refresh(List<HomeRecentModel.RecentBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
