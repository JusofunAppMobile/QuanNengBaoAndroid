package com.jusfoun.jusfouninquire.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jusfoun.jusfouninquire.R;
import com.jusfoun.jusfouninquire.net.model.HomeDataItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/18.
 * Description
 */
public class HotCompanyListAdapter extends BaseAdapter {
    private Context mContext;
    private List<HomeDataItemModel> list;
    private boolean isFromHome = false;

    public HotCompanyListAdapter(Context mContext) {
        this(mContext, false);
    }

    public HotCompanyListAdapter(Context mContext, boolean isFromHome) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
        this.isFromHome = isFromHome;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_hotcompany, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.update(list.get(position), position);
        return convertView;
    }

    public void refersh(List<HomeDataItemModel> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<HomeDataItemModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder {
        private TextView mHotCompanyName, mHotCompanyFollow, mAddress, mCountMoneny, mAttention;
        public HomeDataItemModel data;
        private TextView legalText, dateText;
        private ImageView collectImg;
        private View vDivider, v1, v2;

        public ViewHolder(View view) {
            mHotCompanyFollow = (TextView) view.findViewById(R.id.hot_company_follow);
            mHotCompanyName = (TextView) view.findViewById(R.id.hot_company_name);
            mAddress = (TextView) view.findViewById(R.id.address);
            mCountMoneny = (TextView) view.findViewById(R.id.count_moneny);
            mAttention = (TextView) view.findViewById(R.id.company_attention);
//            mHotRating= (RatingBar) view.findViewById(R.id.hot_company_rating);
            legalText = (TextView) view.findViewById(R.id.company_legal);
            dateText = (TextView) view.findViewById(R.id.company_date);
            collectImg = (ImageView) view.findViewById(R.id.img_collect);
            vDivider = view.findViewById(R.id.vDivider);
            v1 = view.findViewById(R.id.v1);
            v2 = view.findViewById(R.id.v2);

        }

        public HomeDataItemModel getData() {
            return data;
        }

        public void update(HomeDataItemModel model, int position) {
            this.data = model;
            if (TextUtils.isEmpty(model.getLocation())) {
                mAddress.setText("未公布");
            } else {
                mAddress.setText(model.getLocation());
            }
            mHotCompanyName.setText(model.getCompanyname());

            vDivider.setVisibility(isFromHome ? View.GONE : View.VISIBLE);
            v1.setVisibility(isFromHome ? View.GONE : View.VISIBLE);
            v2.setVisibility(isFromHome ? View.GONE : View.VISIBLE);

            if (TextUtils.isEmpty(model.getIndustry())) {
                mHotCompanyFollow.setText("未公布");
            } else {
                mHotCompanyFollow.setText(model.getIndustry());
            }
            if (TextUtils.isEmpty(model.getFunds())) {
                mCountMoneny.setText("未公布");
            } else {
                mCountMoneny.setText(model.getFunds());
            }
//            mAttention.setText(model.getAttentioncount()+"关注");
//            if (!TextUtils.isEmpty(model.getRatings())){
//                mHotRating.setRating(Integer.parseInt(model.getRatings()));
//            }
//            if(position+1==getCount()){
//                mLine_first.setVisibility(View.GONE);
//            }else{
//                mLine_first.setVisibility(View.VISIBLE);
//            }

            if (TextUtils.isEmpty(model.legalPerson)) {
                legalText.setText("未公布");
            } else {
                legalText.setText(model.legalPerson);
            }

            if (TextUtils.isEmpty(model.PublishTime)) {
                dateText.setText("未公布");
            } else {
                dateText.setText(model.PublishTime);
            }

            if (model.isFav) {
                collectImg.setImageResource(R.drawable.icon_home_collect);
            } else {
                collectImg.setImageResource(R.drawable.icon_home_collect_no);
            }
        }

//        private SpannableString setTextViewColor(String string){
//            SpannableString spannableString=null;
//            spannableString = new SpannableString(string + mContext.getString(R.string.people_follow));
//            spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_text_yellow))
//                    , 0,string.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            return spannableString;
//        }

    }
}
