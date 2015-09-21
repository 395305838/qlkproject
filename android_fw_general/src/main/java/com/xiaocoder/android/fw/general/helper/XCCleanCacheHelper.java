package com.xiaocoder.android.fw.general.helper;

import android.app.Dialog;

import com.xiaocoder.android.fw.general.application.XCApp;

import java.io.File;

/**
 * 删除缓存
 */
public class XCCleanCacheHelper {

    // public boolean isGoOnDeleting = false;

    Dialog mDeletingDialog;

    public XCCleanCacheHelper(Dialog deletingDialog) {

        mDeletingDialog = deletingDialog;

    }

    /**
     * 删除文件夹
     */
    public void removeDir(File dir) {
        // isGoOnDeleting = true; // 如果是实际中, 可能存在正在删除, 而应用此时退出的情况
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                // 如果目录是系统级文件夹，java没有访问权限，那么会返回null数组
                for (File file : files) {
                    // if (isGoOnDeleting) {
                    if (file.isDirectory()) {
                        removeDir(file);
                    } else {
                        file.delete();
                    }
                }
            }
            // dir.delete();
        }
    }


    public void removeFileAsyn(final File file) {
        /**
         * 子线程中删除
         */
        XCApp.getBase_cache_threadpool().execute(new Runnable() {
            @Override
            public void run() {
                // 如果文件不存在
                if (!file.exists()) {
                    return;
                }
                // 文件存在，则开始转圈
                XCApp.getBase_handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDeletingDialog != null) {
                            mDeletingDialog.show();
                        }
                    }
                });

                if (file.isDirectory()) {
                    removeDir(file);
                } else {
                    file.delete();
                }
                XCApp.getBase_handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (mDeletingDialog != null) {
                            mDeletingDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    //@Override
    //public void onDestroy() {
    //    super.onDestroy();
    //    isGoOnDeleting = false;
    //}

}
