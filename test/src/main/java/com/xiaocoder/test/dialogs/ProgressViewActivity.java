package com.xiaocoder.test.dialogs;

import android.os.Bundle;

import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.view.SXProgressView;
import com.xiaocoder.test.R;

/**
 * Created by xiaocoder on 2015/9/14.
 */
public class ProgressViewActivity extends XCBaseActivity {

    SXProgressView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_view_progress);
        super.onCreate(savedInstanceState);
    }

    // ������ʱ,�����Ļ��ص��ķ���
    @Override
    public void onNetRefresh() {
    }

    // ��ʼ���ؼ�
    @Override
    public void initWidgets() {
        view = getViewById(R.id.sx_id_progress_view);
        showProgressView();
    }

    // ���ü���
    @Override
    public void listeners() {

    }

    private void showProgressView() {
        setViewVisible(true, view);
        XCApplication.getBase_cache_threadpool().execute(new Runnable() {
            @Override
            public void run() {

                while (view.getProgress() < 100) {
                    try {
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    XCApplication.getBase_handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setProgress(view.getProgress() + 2);
                        }
                    });
                }
            }
        });
    }

}
