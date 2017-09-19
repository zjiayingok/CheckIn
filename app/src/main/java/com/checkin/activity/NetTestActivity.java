package com.checkin.activity;

import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.checkin.R;
import com.checkin.base.BaseActivity;
import com.checkin.net.listener.impl.UIProgressListener;
import com.checkin.utils.Logger;

import butterknife.BindView;

/**
 * Created by zhangjiaying on 2017/9/20.
 */

public class NetTestActivity extends BaseActivity{
    @BindView(R.id.upload_progress)
    ProgressBar upload_progress;
    @BindView(R.id.content_length)
    TextView content_length;
    @BindView(R.id.content_spaced)
    TextView content_spaced;
    @Override
    public int getInflateLayout() {
        return R.layout.net_test_activity;
    }

    @Override
    public void initView() {

    }

    //这个是ui线程回调，可直接操作UI
    Handler handler =  new Handler();
    final UIProgressListener uiProgressRequestListener = new UIProgressListener() {
        @Override
        public void onUIProgress(long bytesWrite, long contentLength, boolean done) {
            Logger.e("TAG", "bytesWrite:" + bytesWrite);
            Logger.e("TAG", "contentLength" + contentLength);
            Logger.e("TAG", (100 * bytesWrite) / contentLength + " % done ");
            Logger.e("TAG", "done:" + done);
            Logger.e("TAG", "================================");
            //ui层回调
            Logger.e("上传进度%%%%%%%%%%-----------",((100 * bytesWrite) / contentLength)+"");
            final long percent = (100 * bytesWrite) / contentLength;

            handler.post(new Runnable() {
                @Override
                public void run() {
                    upload_progress.setProgress((int) percent);
                    content_spaced.setText("上传速度:"+percent+"%");
                }
            });

        }

        @Override
        public void onUIStart(long bytesWrite,final long contentLength, boolean done) {
            super.onUIStart(bytesWrite, contentLength, done);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    content_length.setText("文件大小:"+contentLength/1024);
//                    ll_progress.setVisibility(View.VISIBLE);
//                    Toast.showToast(WorkTrackActivity.this,"start");
                }
            });

        }

        @Override
        public void onUIFinish(long bytesWrite, long contentLength, boolean done) {
            super.onUIFinish(bytesWrite, contentLength, done);
            handler.post(new Runnable() {
                @Override
                public void run() {

//                    ll_progress.setVisibility(View.GONE);
//                    Toast.showToast(WorkTrackActivity.this,"end");
                }
            });

        }
    };

}
