package com.jusfoun.jusfouninquire.net.route;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jusfoun.jusfouninquire.R;
import com.jusfoun.jusfouninquire.net.callback.NetWorkCallBack;
import com.jusfoun.jusfouninquire.net.constant.NetConstant;
import com.jusfoun.jusfouninquire.net.model.InvestOrBranchModel;
import com.jusfoun.jusfouninquire.net.util.GetParamsUtil;
import com.jusfoun.jusfouninquire.net.volley.VolleyErrorUtil;
import com.jusfoun.jusfouninquire.net.volley.VolleyGetRequest;
import com.jusfoun.jusfouninquire.ui.util.VolleyUtil;

import java.util.HashMap;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/16.
 * Description 获取对外投资或分支机构
 */
public class GetInvestOrBranch {

    private static final String getUrl="/api/entdetail/GetEntBranchOrInvesment";

    public static void getInvestOrBranch(Context context,HashMap<String,String> params,String tag, final NetWorkCallBack callBack){
        VolleyGetRequest<InvestOrBranchModel> getRequest=new VolleyGetRequest<>(GetParamsUtil.getParmas(context.getString(R.string.req_url)+getUrl, params)
                , InvestOrBranchModel.class
                , new Response.Listener<InvestOrBranchModel>() {
            @Override
            public void onResponse(InvestOrBranchModel response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFail(VolleyErrorUtil.disposeError(error));
            }
        },context);

        getRequest.setShouldCache(false);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(NetConstant.TIMEOUT, 1, 1.0f));
        getRequest.setTag(tag);
        VolleyUtil.getQueue(context).add(getRequest);
    }
}
