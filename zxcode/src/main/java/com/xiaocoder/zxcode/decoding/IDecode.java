package com.xiaocoder.zxcode.decoding;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;

import com.google.zxing.Result;
import com.xiaocoder.zxcode.view.ViewfinderView;

public interface IDecode {

    void drawViewfinder();

    void handleDecode(Result result, Bitmap barcode);

    ViewfinderView getViewfinderView();

    Handler getHandler();

}