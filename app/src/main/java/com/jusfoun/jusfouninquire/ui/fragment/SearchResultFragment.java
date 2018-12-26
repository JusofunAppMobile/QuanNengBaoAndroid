package com.jusfoun.jusfouninquire.ui.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jusfoun.jusfouninquire.R;
import com.jusfoun.jusfouninquire.TimeOut;
import com.jusfoun.jusfouninquire.net.callback.NetWorkCallBack;
import com.jusfoun.jusfouninquire.net.model.HomeDataItemModel;
import com.jusfoun.jusfouninquire.net.model.SearchListModel;
import com.jusfoun.jusfouninquire.net.route.SearchRoute;
import com.jusfoun.jusfouninquire.net.util.AppUtil;
import com.jusfoun.jusfouninquire.sharedpreference.LoginSharePreference;
import com.jusfoun.jusfouninquire.ui.activity.CompanyDetailActivity;
import com.jusfoun.jusfouninquire.ui.activity.SearchResultActivity;
import com.jusfoun.jusfouninquire.ui.activity.WebActivity;
import com.jusfoun.jusfouninquire.ui.adapter.SearchResultAdapter;
import com.jusfoun.jusfouninquire.ui.view.XListView;

import java.util.HashMap;

import netlib.util.EventUtils;

import static com.jusfoun.jusfouninquire.R.id.register_time_sort;

/**
 * SearchResultFragment
 *
 * @author : albert
 * @Email : liubinhou007@163.com
 * @date : 16/8/10
 * @Description :搜索结果页面，配合抽屉显示的碎片
 */
public class SearchResultFragment extends BaseInquireFragment implements XListView.IXListViewListener {

    public static final String SEARCH_RESULT = "search_result";
    private SearchListModel mData;
    //    private View mView;
    private XListView mResult;
    private SearchResultAdapter mAdapter;
    private RelativeLayout mFocusLayout;
    private ViewGroup mFundLayout, mTimeLayout;
    private LinearLayout mLooanywhere;
    private ImageView mFocusUp, mFocusDown;
    private TextView mFocusText, mFundText, mTimeText;
//    private Button footWhere;

    //是否是升序排序
    private boolean mIsFocusUp, mIsFundUp, mIsTimeUp;


    private String mCurrentType;
    private String mSearchKey;
//    private View footView;


    private String mSequence = "2";
    private String mCity = "", mProvince = "", mCapital = "", mTime = "", mIndustry = "";

    private int pageIndex = 1;
    private int pageSize = 20;
    private boolean isHasFoot = false;
    private String search_city = "全国";
    private View vArrowFund, vArrowTime;

    private TextView tvCount;
    private LinearLayout headerLayout;


    @Override
    protected void initData() {
        if (getArguments() != null && getArguments().getSerializable(SEARCH_RESULT) != null) {
            mData = (SearchListModel) getArguments().getSerializable(SEARCH_RESULT);
        }

        if (getArguments() != null) {
            mCurrentType = getArguments().getString(SearchResultActivity.SEARCH_TYPE);
            mSearchKey = getArguments().getString(SearchResultActivity.SEARCH_KEY);
        }

        mAdapter = new SearchResultAdapter(mContext);
        mIsFundUp = true;
        mIsTimeUp = true;
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        mFocusLayout = (RelativeLayout) view.findViewById(R.id.focus_sort_layout);
//        mView = view.findViewById(R.id.myself);
        mLooanywhere = (LinearLayout) view.findViewById(R.id.look_any);
//        mLookanywhere = (Button) view.findViewById(R.id.look_anywhere);
        mFundLayout = (ViewGroup) view.findViewById(R.id.register_fund_sort_layout);
        mTimeLayout = (LinearLayout) view.findViewById(R.id.register_time_sort_layout);
        mFocusUp = (ImageView) view.findViewById(R.id.focus_sort_up_image);
        mFocusDown = (ImageView) view.findViewById(R.id.focus_sort_down_image);
//        mFundUp = (ImageView) view.findViewById(R.id.register_fund_sort_up_image);
//        mFundDown = (ImageView) view.findViewById(R.id.register_fund_sort_down_image);
//        mTimeUp = (ImageView) view.findViewById(R.id.register_time_sort_up_image);
//        mTimeDown = (ImageView) view.findViewById(R.id.register_time_sort_down_image);
        mResult = (XListView) view.findViewById(R.id.search_result_listview);
        mFocusText = (TextView) view.findViewById(R.id.focus_sort);
        mFundText = (TextView) view.findViewById(R.id.register_fund_sort);
        mTimeText = (TextView) view.findViewById(register_time_sort);

//        footView = LayoutInflater.from(mContext).inflate(R.layout.layout_foot_search_result, null);
//        footWhere = (Button) footView.findViewById(R.id.look_anywhere);

        vArrowFund = view.findViewById(R.id.vArrowFund);
        vArrowTime = view.findViewById(R.id.vArrowTime);

        tvCount = (TextView) view.findViewById(R.id.tvCount);
        headerLayout = (LinearLayout) view.findViewById(R.id.vHeader);

        if (mData != null)
            tvCount.setText(String.valueOf(mData.getCount()));

        return view;
    }

    @Override
    protected void initWeightActions() {
        headerLayout.setVisibility(View.VISIBLE);
        mResult.setAdapter(mAdapter);
        mAdapter.setSearchType(mCurrentType);
        if (mData != null) {
            mAdapter.refresh(mData.getBusinesslist());
        }

        if (mData == null
                || mData.getBusinesslist() == null
                || mData.getBusinesslist().size() == 0) {
            mLooanywhere.setVisibility(View.VISIBLE);
        } else {
            mLooanywhere.setVisibility(View.GONE);
        }

        mResult.setXListViewListener(this);
        if (mData != null)
            mResult.setPullLoadEnable(mData.getCount() > pageSize);
        if (mResult.isEnablePullLoad()) {
            if (isHasFoot) {
                isHasFoot = false;
//                mResult.removeFooterView(footView);
            }
        } else {
            if (!isHasFoot && mData != null && mData.getCount() > 0) {
                isHasFoot = true;
//                mResult.addFooterView(footView);
            }
        }
        mResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (view.getTag() instanceof SearchResultAdapter.ViewHolder) {
                    SearchResultAdapter.ViewHolder holder = (SearchResultAdapter.ViewHolder) view.getTag();
                    if (holder == null) {
                        return;
                    }


                    EventUtils.event(mContext, EventUtils.SEARCH46);
                    HomeDataItemModel model = holder.getData();
                    if (model != null) {
                        Intent intent = new Intent(mContext, CompanyDetailActivity.class);
                        intent.putExtra(CompanyDetailActivity.COMPANY_ID, model.getCompanyid());
                        intent.putExtra(CompanyDetailActivity.COMPANY_NAME, model.getCompanyname());
                        startActivity(intent);
                    }

                }
            }
        });


//        mFocusText.setTextColor(getResources().getColor(R.color.search_name_color));
        setSelect(mFocusText, true);


        mFocusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventUtils.event(mContext, EventUtils.SEARCH54);
                mIsFocusUp = !mIsFocusUp;
                mFocusUp.setImageResource(mIsFocusUp ? R.mipmap.sort_up_selected : R.mipmap.sort_up_unselected);
                mFocusDown.setImageResource(mIsFocusUp ? R.mipmap.sort_down_unselected : R.mipmap.sort_down_selected);
                mSequence = "2";
//                mFocusText.setTextColor(getResources().getColor(R.color.search_name_color));
                setSelect(mFocusText, true);

                vArrowFund.setSelected(false);

//                mFundUp.setImageResource(R.mipmap.sort_up_unselected);
//                mFundDown.setImageResource(R.mipmap.sort_down_unselected);
                mIsFundUp = true;
//                mFundText.setTextColor(getResources().getColor(R.color.black));
                setSelect(mFundText, false);

//                mTimeUp.setImageResource(R.mipmap.sort_up_unselected);
//                mTimeDown.setImageResource(R.mipmap.sort_down_unselected);
                vArrowTime.setSelected(false);
                mIsTimeUp = true;
//                mTimeText.setTextColor(getResources().getColor(R.color.black));
                setSelect(mTimeText, false);

                doSearch();

            }
        });

//        mLookanywhere.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchWholeNet();
//            }
//        });

//        footWhere.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchWholeNet();
//            }
//        });

        mFundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventUtils.event(mContext, EventUtils.SEARCH55);
                mIsFundUp = !mIsFundUp;

                vArrowFund.setSelected(mIsFundUp);
//                mFundUp.setImageResource(mIsFundUp ? R.mipmap.sort_up_selected : R.mipmap.sort_up_unselected);
//                mFundDown.setImageResource(mIsFundUp ? R.mipmap.sort_down_unselected : R.mipmap.sort_down_selected);
                mSequence = mIsFundUp ? "3" : "4";
//                mFundText.setTextColor(getResources().getColor(R.color.search_name_color));
                setSelect(mFundText, true);


                mFocusUp.setImageResource(R.mipmap.sort_up_unselected);
                mFocusDown.setImageResource(R.mipmap.sort_down_unselected);
                mIsFocusUp = true;
//                mFocusText.setTextColor(getResources().getColor(R.color.black));
                setSelect(mFocusText, false);


//                mTimeUp.setImageResource(R.mipmap.sort_up_unselected);
//                mTimeDown.setImageResource(R.mipmap.sort_down_unselected);
                vArrowTime.setSelected(false);
                mIsTimeUp = true;
//                mTimeText.setTextColor(getResources().getColor(R.color.black));
                setSelect(mTimeText, false);

                doSearch();

            }
        });

        mTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventUtils.event(mContext, EventUtils.SEARCH56);
                mIsTimeUp = !mIsTimeUp;
//                mTimeUp.setImageResource(mIsTimeUp ? R.mipmap.sort_up_selected : R.mipmap.sort_up_unselected);
//                mTimeDown.setImageResource(mIsTimeUp ? R.mipmap.sort_down_unselected : R.mipmap.sort_down_selected);
                vArrowTime.setSelected(mIsTimeUp);
                mSequence = mIsTimeUp ? "5" : "6";
//                mTimeText.setTextColor(getResources().getColor(R.color.search_name_color));
                setSelect(mTimeText, true);

                mFocusUp.setImageResource(R.mipmap.sort_up_unselected);
//                mFundDown.setImageResource(R.mipmap.sort_down_unselected);
                mIsFocusUp = true;
//                mFocusText.setTextColor(getResources().getColor(R.color.black));
                setSelect(mFocusText, false);

//                mFundUp.setImageResource(R.mipmap.sort_up_unselected);
//                mFundDown.setImageResource(R.mipmap.sort_down_unselected);
                vArrowFund.setSelected(false);

                mIsFundUp = true;
//                mFundText.setTextColor(getResources().getColor(R.color.black));
                setSelect(mFundText, false);

                doSearch();
            }
        });

        if (mData == null) {
            doSearch();
        }
    }

    private void setSelect(TextView textView, boolean select) {
        textView.setSelected(select);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, select ? 16 : 14);
        textView.setTypeface(select ? Typeface.defaultFromStyle(Typeface.BOLD) : Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    /**
     * 跳转到全网搜索
     */
    private void searchWholeNet() {
        String url = String.format(mContext.getString(R.string.search_whole_net), search_city, mSearchKey, AppUtil.getVersionName(mContext));
        Intent intent = new Intent(mContext, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, "企业查询");
        bundle.putString(WebActivity.URL, mContext.getString(R.string.req_url) + url);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 网络搜索
     */
    private void doSearch() {
        pageIndex = 1;
        final TimeOut timeOut = new TimeOut(mContext);
        HashMap<String, String> params = new HashMap<>();
        if (LoginSharePreference.getUserInfo(mContext) != null
                && !TextUtils.isEmpty(LoginSharePreference.getUserInfo(mContext).getUserid())) {
            params.put("userid", LoginSharePreference.getUserInfo(mContext).getUserid());
        } else {
            params.put("userid", "");
        }

        if (!TextUtils.isEmpty(mCity)) {
            search_city = mCity;
        } else if (!TextUtils.isEmpty(mProvince)) {
            search_city = mProvince;
        } else {
            search_city = "全国";
        }

        params.put("t", timeOut.getParamTimeMollis() + "");
        params.put("m", timeOut.MD5time() + "");

        params.put("searchkey", mSearchKey);
        params.put("type", mCurrentType);
        params.put("pageSize", pageSize + "");
        params.put("pageIndex", pageIndex + "");
        params.put("sequence", mSequence);
        params.put("city", mCity);
        params.put("province", mProvince);
        params.put("registeredcapital", mCapital);
        params.put("regtime", mTime);
        params.put("industryid", mIndustry);
        showLoading();
        SearchRoute.searchNet(mContext, params, getActivity().getLocalClassName().toString(), new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                hideLoadDialog();
                mResult.stopRefresh();
                if (data instanceof SearchListModel) {
                    SearchListModel model = (SearchListModel) data;
                    tvCount.setText(String.valueOf(model.getCount()));
                    if (model.getResult() == 0) {
                        if (model.getBusinesslist() != null && model.getBusinesslist().size() > 0) {
                            mAdapter.refresh(model.getBusinesslist());
                            mResult.setPullLoadEnable(model.getCount() > pageSize);
                            mResult.setVisibility(View.VISIBLE);
                            mLooanywhere.setVisibility(View.GONE);


                        } else {
                            mAdapter.refresh(model.getBusinesslist());
                            mResult.setPullLoadEnable(false);
//                            showToast(mContext.getString(R.string.search_result_none));
                            mResult.setVisibility(View.GONE);
                            mLooanywhere.setVisibility(View.VISIBLE);
//                            mView.setVisibility(View.GONE);
                        }
                        if (mResult.isEnablePullLoad()) {
                            if (isHasFoot) {
                                isHasFoot = false;
//                                mResult.removeFooterView(footView);
                            }
                        } else {
                            if (!isHasFoot) {
                                isHasFoot = true;
//                                mResult.addFooterView(footView);
                            }
                        }
                    } else {
                        if (model.getResult() != -1) {
                            if (!TextUtils.isEmpty(model.getMsg())) {
                                showToast(model.getMsg());
                            } else {
                                showToast("网络不给力，请稍后重试");
                            }
                        }

                    }
                }
            }

            @Override
            public void onFail(String error) {
                mResult.stopRefresh();
                showToast("网络不给力，请稍后重试");
                hideLoadDialog();
            }
        });
    }

    /**
     * 根据筛选条件进行重新搜索
     *
     * @param params
     */
    public void doFilterSearch(HashMap<String, String> params) {
        if (params.get("city") != null) {
            mCity = params.get("city").toString();
        }
        if (params.get("province") != null) {
            mProvince = params.get("province").toString();
        }

        if (params.get("registeredcapital") != null) {
            mCapital = params.get("registeredcapital").toString();
        }

        if (params.get("isHavWebSite") != null) {
            mCapital = params.get("isHavWebSite").toString();
        }

        if (params.get("regtime") != null) {
            mTime = params.get("regtime").toString();
        }

        if (params.get("industryid") != null) {
            mIndustry = params.get("industryid").toString();
        }

        doSearch();
    }

    /**
     * 加载更多网络请求
     */
    private void doLoadMore() {
        TimeOut timeOut = new TimeOut(mContext);
        HashMap<String, String> params = new HashMap<>();
        if (LoginSharePreference.getUserInfo(mContext) != null
                && !TextUtils.isEmpty(LoginSharePreference.getUserInfo(mContext).getUserid())) {
            params.put("userid", LoginSharePreference.getUserInfo(mContext).getUserid());
        } else {
            params.put("userid", "");
        }

        params.put("t", timeOut.getParamTimeMollis() + "");
        params.put("m", timeOut.MD5time() + "");

        params.put("searchkey", mSearchKey);
        params.put("type", mCurrentType);
        params.put("pageSize", pageSize + "");//业务逻辑是每次上拉加载10条
        params.put("pageIndex", pageIndex + 1 + "");
        params.put("sequence", mSequence);
        params.put("city", mCity);
        params.put("province", mProvince);
        params.put("registeredcapital", mCapital);
        params.put("regtime", mTime);
        params.put("industryid", mIndustry);

        SearchRoute.searchNet(mContext, params, getActivity().getLocalClassName().toString(), new NetWorkCallBack() {
            @Override
            public void onSuccess(Object data) {
                mResult.stopLoadMore();
                if (data instanceof SearchListModel) {
                    SearchListModel model = (SearchListModel) data;
                    if (model.getResult() == 0) {
                        pageIndex = pageIndex + 1;
                        mResult.setPullLoadEnable(model.getCount() > (pageSize * pageIndex));
                        if (mResult.isEnablePullLoad()) {
                            if (isHasFoot) {
                                isHasFoot = false;
//                                mResult.removeFooterView(footView);
                            }
                        } else {
                            if (!isHasFoot) {
                                isHasFoot = true;
//                                mResult.addFooterView(footView);
                            }
                        }
                        if (model.getBusinesslist() != null && model.getBusinesslist().size() > 0) {
                            mAdapter.addData(model.getBusinesslist());
                        } else {

                        }
                    } else {
                        if (model.getResult() != -1) {
                            if (!TextUtils.isEmpty(model.getMsg())) {
                                showToast(model.getMsg());
                            } else {
                                showToast("网络不给力，请稍后重试");
                            }
                        }
                    }
                }
            }

            @Override
            public void onFail(String error) {
                mResult.stopLoadMore();
                showToast("网络不给力，请稍后重试");

            }
        });
    }

    @Override
    public void onRefresh() {
        doSearch();
    }

    @Override
    public void onLoadMore() {
        doLoadMore();
    }


}
