package com.xiaocoder.android.fw.general.helper;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.xiaocoder.android.fw.general.io.XCIO;

/**
 * @author xiaocoder
 * @Description: 加载重复图片的时候不要用setResource与setBitmap, 很耗性能; 如果是加载同一张图片, 如默认图片,
 *               应该使用setDrawable, 且这个drawable应该是同一个实例;如果是加载不同图片的时候可以用setBitmap
 */
public class XCDrawableHelper {

	private DefaultDrawableListener listener;

	public void setOnDefaultDrawableListener(DefaultDrawableListener listener) {
		this.listener = listener;
	}

	public interface DefaultDrawableListener {
		void getDefaultDrawable(Drawable default_drawable);
	}

	// 如果默认图片不大的话,可在主线程中加载
	public Drawable getDrawableByMain(Context context, int default_image_id) {
		Drawable drawable = new BitmapDrawable(context.getResources(), BitmapFactory.decodeResource(context.getResources(), default_image_id));
		return drawable;
	}

	// 如果默认图片很大的话,在子线程中加载
	public void getDrawableByAsyn(final Context context, final int default_image_id) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				InputStream in = context.getResources().openRawResource(default_image_id);
				byte[] data = XCIO.toBytesByInputStream(in);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				options.inDither = false;
				options.inPurgeable = true;
				options.inInputShareable = true;
				// 重置压缩尺寸
				int ratio = (int) Math.round(Math.sqrt(data.length / 102400));// 一百kb
				if (ratio < 1) {
					options.inSampleSize = 1;// 保持原有大小
				} else {
					options.inSampleSize = ratio;
				}
				Bitmap default_bitmap = null;
				try {
					default_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
				} catch (Exception e) {
					e.printStackTrace();
					default_bitmap = null;
					return;
				} finally {
					try {
						if (in != null) {
							in.close();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (listener != null && default_bitmap != null) {
					Drawable default_drawable = new BitmapDrawable(context.getResources(), default_bitmap);
					listener.getDefaultDrawable(default_drawable);
				}
			}
		}.start();
	}
}
