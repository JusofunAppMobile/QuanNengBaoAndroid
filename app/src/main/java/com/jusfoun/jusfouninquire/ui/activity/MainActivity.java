package com.jusfoun.jusfouninquire.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jusfoun.jusfouninquire.R;
import com.jusfoun.jusfouninquire.service.event.FlipperAnimEvent;
import com.jusfoun.jusfouninquire.service.event.HomeLeadEvent;
import com.jusfoun.jusfouninquire.service.event.IEvent;
import com.jusfoun.jusfouninquire.service.event.LoginOutIconEvent;
import com.jusfoun.jusfouninquire.service.event.TestEvent;
import com.jusfoun.jusfouninquire.sharedpreference.FirstStartAppSharePreference;
import com.jusfoun.jusfouninquire.sharedpreference.LoginSharePreference;
import com.jusfoun.jusfouninquire.ui.adapter.HomeAdapter;
import com.jusfoun.jusfouninquire.ui.util.AlphaAnimUtil;
import com.jusfoun.jusfouninquire.ui.util.KeyBoardUtil;
import com.jusfoun.jusfouninquire.ui.util.ThirdLoginUtil;
import com.jusfoun.jusfouninquire.ui.util.ZoomOutPageTransformer;
import com.jusfoun.jusfouninquire.ui.view.HomeTopView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseInquireActivity {


    private ViewPager viewpager;

    private HomeAdapter adapter;
    private HomeTopView homeTopView;
    private LinearLayout mHeadLead;
    private AlphaAnimUtil alphaAnimUtil;
    private final int ADVERTISE_CODE = 1001;
//    private Toolbar mToolbar;
    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG, "registrationid:" + JPushInterface.getRegistrationID(mContext));


    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        homeTopView = (HomeTopView) findViewById(R.id.home_top_view);
        mHeadLead = (LinearLayout) findViewById(R.id.home_lead);
        rootLayout = (RelativeLayout) findViewById(R.id.layout_root);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public boolean isBarDark() {
        return false;
    }

    @Override
    public boolean isSetStatusBar() {
        return false;
    }

    @Override
    protected void initWidgetActions() {


//      mSwipeBackLayout.setEnableGesture(false);
//        downlaodAdvertise();

//        handler.sendEmptyMessageDelayed(ADVERTISE_CODE, 1000);


        if (LoginSharePreference.getUserInfo(mContext) != null
                && !TextUtils.isEmpty(LoginSharePreference.getUserInfo(mContext).getUserid())) {
            homeTopView.setLoginOutIconState(true);

        } else {
            homeTopView.setLoginOutIconState(false);
        }


//        setSupportActionBar(mToolbar);
        initSystemBar();

    }


    @Override
    public void onEvent(IEvent event) {
        super.onEvent(event);
        if (event instanceof TestEvent) {
            showToast("activity Event");
        } else if (event instanceof HomeLeadEvent) {
            if (FirstStartAppSharePreference.isHomeFirstStart(mContext)) {
                //FirstStartAppSharePreference.saveHomeFirstStart(mContext);
                mHeadLead.setPadding(0, ((HomeLeadEvent) event).getPadding(), 0, 0);
                alphaAnimUtil = new AlphaAnimUtil(mHeadLead);
                alphaAnimUtil.start();
            }
        } else if (event instanceof LoginOutIconEvent) {
            homeTopView.setLoginOutIconState(((LoginOutIconEvent) event).isLogin());
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initSystemBar() {
        int padding = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(android.R.color.transparent);
            tintManager.setStatusBarTintResource(0);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            padding = config.getStatusBarHeight();
            homeTopView.setPadding(padding);

//            mHeadLead.setPadding(0, padding, 0, 0);


//            Log.e("tag", "底部虚拟=" + config.getPixelInsetBottom());
        }
        adapter = new HomeAdapter(getSupportFragmentManager(), padding);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(3);
        homeTopView.setViewPager(viewpager);
        viewpager.setCurrentItem(1);
        viewpager.setPageTransformer(true, new ZoomOutPageTransformer());
    }



    @Override
    protected void onResume() {
        super.onResume();
        KeyBoardUtil.hideSoftKeyboard(this);
        if (viewpager.getCurrentItem() == 1) {
            FlipperAnimEvent event = new FlipperAnimEvent();
            event.isStart = true;
            EventBus.getDefault().post(event);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (viewpager.getCurrentItem() == 1) {
            FlipperAnimEvent event = new FlipperAnimEvent();
            event.isStart = false;
            EventBus.getDefault().post(event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ThirdLoginUtil.getThirdLoginUtil(mContext).onActivityResult(requestCode, resultCode, data);
    }

    private long lastBackTime = 0;

    @Override
    public void onBackPressed() {
        if(viewpager.getCurrentItem() != 1){
            viewpager.setCurrentItem(1);
        }else{
            if(System.currentTimeMillis() - lastBackTime <= 3000){
                finish();
            }else{
                showToast("再按一次退出程序");
            }
            lastBackTime = System.currentTimeMillis();
        }
    }
}
