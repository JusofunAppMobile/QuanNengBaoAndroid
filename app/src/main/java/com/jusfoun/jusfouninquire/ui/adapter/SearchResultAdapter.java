package com.jusfoun.jusfouninquire.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jusfoun.jusfouninquire.R;
import com.jusfoun.jusfouninquire.net.model.HomeDataItemModel;

import java.util.ArrayList;
import java.util.List;

import static com.jusfoun.jusfouninquire.R.id.company_info;

/**
 * SearchResultAdapter
 *
 * @author : albert
 * @Email : liubinhou007@163.com
 * @date : 16/8/15
 * @Description :搜索结果适配器
 */
public class SearchResultAdapter extends BaseAdapter {
    private Context mContext;
    private List<HomeDataItemModel> mData;
    private String mSearchType;

    public SearchResultAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
        mSearchType = "";
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_result, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.update(position);
        return convertView;
    }

    public void setSearchType(String mSearchType) {
        this.mSearchType = mSearchType;
    }

    public void refresh(List<HomeDataItemModel> data) {
        mData.clear();
        if (data != null)
            mData.addAll(data);
        notifyDataSetChanged();

    }

    public void addData(List<HomeDataItemModel> data) {
        mData.addAll(data);
        notifyDataSetChanged();

    }


    public class ViewHolder {
        private TextView mCompanyName, mCompanyInfo, mCompanyState, tvLegal, tvEstablish, tvRelate;
        private LinearLayout mRelateLayout;
        private View vTop;
        //        private View mDivider;
        public HomeDataItemModel data;

        public ViewHolder(View view) {
            mCompanyName = (TextView) view.findViewById(R.id.company_name);
            mCompanyInfo = (TextView) view.findViewById(company_info);
            mCompanyState = (TextView) view.findViewById(R.id.company_state);
            tvLegal = (TextView) view.findViewById(R.id.tvLegal);
            tvEstablish = (TextView) view.findViewById(R.id.tvEstablish);
//            mRelated = (TextView) view.findViewById(R.id.related_text);
            mRelateLayout = (LinearLayout) view.findViewById(R.id.relate_layout);
            tvRelate = (TextView) view.findViewById(R.id.tvRelate);
            vTop = view.findViewById(R.id.vTop);
//            mDivider = view.findViewById(R.id.divider_view);

        }

        public HomeDataItemModel getData() {
            return data;
        }

        public void update(int position) {
            data = mData.get(position);
            tvLegal.setText(data.getLegal());
            mCompanyInfo.setText(data.getFunds());
            tvEstablish.setText(data.establish);
//            tvRelate.setText(data.relatednofont);

            if (!TextUtils.isEmpty(data.getRelated()))
                tvRelate.setText(Html.fromHtml(data.getRelated()));

            mRelateLayout.setVisibility(TextUtils.isEmpty(data.getRelated()) ? View.GONE : View.VISIBLE);

//            vTop.setBackgroundResource(mRelateLayout.getVisibility() == View.VISIBLE ? R.drawable.img_item_2 : R.drawable.img_item_bg);

            if (!TextUtils.isEmpty(data.getCompanylightname())) {
                mCompanyName.setText(Html.fromHtml(data.getCompanylightname()));
            } else {
                mCompanyName.setText(data.getCompanyname());
            }
//                mRelateLayout.setVisibility(TextUtils.isEmpty(data.getRelated()) ? View.GONE : View.VISIBLE);

//                try {
//                    mRelated.setText(Html.fromHtml(data.getRelated()));
//                } catch (Exception e) {
//
//                }

            //改版需求,搜索列表去除行业字段
            //mCompanyInfo.setText(data.getIndustry() + " | " + data.getLocation() + " | " + data.getFunds());
            //mCompanyInfo.setText(data.getLocation() + " | " + data.getFunds());
//                if ("未公布".equals(data.getLocation())) {
//                    if ("未公布".equals(data.getFunds())) {
//                        mCompanyInfo.setText("");
//                    } else {
//                        mCompanyInfo.setText(data.getFunds());
//                    }
//                } else {
//                    if ("未公布".equals(data.getFunds())) {
//                        mCompanyInfo.setText(data.getLocation());
//                    } else {
//                        mCompanyInfo.setText(data.getLocation() + " | " + data.getFunds());
//                    }
//                }

            mCompanyState.setText(data.getCompanystate());

//                mDivider.setVisibility(mSearchType.equals(SearchHistoryItemModel.SEARCH_COMMON) ? View.GONE : View.VISIBLE);
//                if (TextUtils.isEmpty(data.getSocialcredit()) || "null".equals(data.getSocialcredit())) {
//                    socialcreditText.setText("统一社会信用代码/注册号：");
//                } else {
//                    socialcreditText.setText("统一社会信用代码/注册号：" + data.getSocialcredit());
//                }
        }
    }
}
