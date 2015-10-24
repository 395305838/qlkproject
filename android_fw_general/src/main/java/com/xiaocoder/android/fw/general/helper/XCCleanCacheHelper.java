package com.xiaocoder.android.fw.general.helper;

import android.app.Dialog;

import com.xiaocoder.android.fw.general.application.XCApp;

import java.io.File;

/**
 * 删除缓存 ，removeFileAsyn（文件或文件夹）
 */
public class XCCleanCacheHelper {

    public boolean isGoOnDeleting;

    Dialog mDeletingDialog;

    public XCCleanCacheHelper(Dialog deletingDialog) {

        mDeletingDialog = deletingDialog;
        // 如果是实际中, 可能存在正在删除, 而页面此时退出的情况
        isGoOnDeleting = true;

    }

    private void removeDir(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                // 如果目录是系统级文件夹，java没有访问权限，那么会返回null数组
                for (File file : files) {
                    if (isGoOnDeleting) {
                        if (file.isDirectory()) {
                            removeDir(file);
                        } else {
                            file.delete();
                        }
                    }
                }
            }
            // dir.delete();
        }
    }

    /**
     * 子线程中删除
     */
    public void removeFileAsyn(final File file) {
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
                XCApp.getBase_handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mDeletingDialog != null) {
                            mDeletingDialog.cancel();
                        }
                    }
                }, 2000);
            }
        });
    }

    public void quit() {
        isGoOnDeleting = false;
        mDeletingDialog = null;
    }
}
