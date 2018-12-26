package com.jusfoun.jusfouninquire.net.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.jusfoun.jusfouninquire.OffsetSharePrerence;
import com.jusfoun.jusfouninquire.net.model.BaseModel;
import com.jusfoun.jusfouninquire.net.util.GetParamsUtil;
import com.jusfoun.jusfouninquire.ui.util.LogUtil;
import com.jusfoun.jusfouninquire.ui.util.PhoneUtil;
import com.jusfoun.jusfouninquire.ui.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import netlib.util.AppUtil;


/**
 * @author zhaoyapeng
 * @version create time:15-1-20下午12:15
 * @Email zhaoyp@witmob.com
 * @Description ${自定义 request}
 */
public class VolleyPostRequest<T extends BaseModel> extends Request<T> {

    private static final int DEFAULT_NET_TYPE = 0;
    private final Response.Listener<T> mListener;
    private Class<T> modelClass;
    private Context mContext;

    /**
     * 重新定义缓存key
     *
     * @return
     */
    @Override
    public String getCacheKey() {
        String params = "";
        try {
            params = getParams().toString();
        } catch (Exception e) {
        }
        return getUrl() + params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headerParams = new HashMap<String, String>();

        headerParams.put("Version", AppUtil.getVersionName(mContext));
        headerParams.put("VersionCode", AppUtil.getVersionCode(mContext) + "");
        headerParams.put("AppType", "0");
        headerParams.put("Channel", StringUtil.getChannelName(mContext));
        headerParams.put("DeviceId", PhoneUtil.getIMEI(mContext));
        headerParams.put("Deviceid", PhoneUtil.getIMEI(mContext));
        return headerParams;
    }

    /**
     * null 正常逻辑
     * 可以重写此方法，返回json数据
     * TODO
     *
     * @return
     */
    public String getDemoRespose() {
        return null;
    }

    public VolleyPostRequest(String url, Class<T> modelClass, Response.Listener<T> listener,
                             Response.ErrorListener errorListener, Context mContext) {
        this(Method.POST, url, modelClass, listener, errorListener, mContext);
    }

    public VolleyPostRequest(int method, String url, Class<T> modelClass, Response.Listener<T> listener,
                             Response.ErrorListener errorListener, Context mContext) {
        super(method, url, errorListener);
        mListener = listener;
        this.modelClass = modelClass;
        this.mContext = mContext;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30*1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(retryPolicy);

        try {
            LogUtil.e("tag", "post-url=" + GetParamsUtil.getParmas(url, getPostParams()));
        } catch (Exception e) {

        }
    }


    /**
     * 解析 response
     *
     * @param response
     * @return
     */
    private Response<T> responseWrapper;

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            if(response.headers!=null){
                OffsetSharePrerence.getComputeOffsetTime(mContext, response.headers.get("Date"));
            }

            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers,"UTF-8"));
            LogUtil.e("tag", "网络请求结果=" + parsed);
            responseWrapper = Response.success(new Gson().fromJson(parsed, modelClass), VolleyHttpHeaderParser.volleyParseCacheHeaders(response));
            return responseWrapper;
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }


}