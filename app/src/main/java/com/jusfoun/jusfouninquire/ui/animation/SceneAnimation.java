package com.jusfoun.jusfouninquire.ui.animation;

import android.text.TextUtils;
import android.widget.ImageView;

import com.jusfoun.jusfouninquire.InquireApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:2015-6-2上午10:55:47
 * @Email zyp@jusfoun.com
 * @Description TODO
 */

public class SceneAnimation {
    private ImageView mImageView;
    private int[] mDurations;
    private int mDuration;

    private int mLastFrameNo;
    private long mBreakDelay;
    private boolean isStop = true;

    private List<Integer> resList = new ArrayList<>();
//    private int[] mFrameRess = {R.mipmap.frame_1, R.mipmap.frame_2, R.mipmap.frame_3, R.mipmap.frame_4,
//            R.mipmap.frame_5, R.mipmap.frame_6, R.mipmap.frame_7, R.mipmap.frame_8, R.mipmap.frame_9,
//            R.mipmap.frame_10, R.mipmap.frame_11, R.mipmap.frame_12, R.mipmap.frame_13, R.mipmap.frame_14,
//            R.mipmap.frame_15, R.mipmap.frame_16, R.mipmap.frame_17, R.mipmap.frame_18, R.mipmap.frame_19,
//            R.mipmap.frame_20, R.mipmap.frame_21,R.mipmap.frame_22,R.mipmap.frame_23,R.mipmap.frame_24,R.mipmap.frame_23,
//            R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,
//            R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,
//            R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,
//            R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,
//            R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,R.mipmap.frame_23,};

    public SceneAnimation(ImageView pImageView, int pDuration) {
        this.mImageView = pImageView;
//        this.mDuration = pDuration;
//        this.mDuration = pDuration;
        this.mDuration = 41;

        for(int i = 0 ; i <= 88;i ++)
            resList.add(getResourceId("frame_"+i));

        this.mLastFrameNo = resList.size() - 1;
    }

    public static int getResourceId(String imageName) {
        if (!TextUtils.isEmpty(imageName))
            return InquireApplication.application.getResources().getIdentifier(imageName, "mipmap", InquireApplication.application.getPackageName());
        return 0;
    }

    private void playConstant(final int pFrameNo) {
        if (!isStop) {
            mImageView.postDelayed(new Runnable() {
                public void run() {
                    mImageView.setBackgroundResource(resList.get(pFrameNo));

                    if (pFrameNo == mLastFrameNo) {
//                        playConstant(0);
                        playConstant(45);
                    }
                    else
                        playConstant(pFrameNo + 1);
                }
            }, pFrameNo == mLastFrameNo && mBreakDelay > 0 ? mBreakDelay : mDuration);

        }
    }

    public void start() {
        isStop = false;
        mImageView.setBackgroundResource(resList.get(0));
        playConstant(1);
    }

    public void stop() {
        isStop = true;
    }

    // 判断该当前的状态
    public boolean getIsStop() {
        return isStop;
    }
}
