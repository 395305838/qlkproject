package com.xiaocoder.android.fw.general.scan;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.xiaocoder.android.fw.general.application.XCApplication;
import com.xiaocoder.android.fw.general.base.XCBaseActivity;
import com.xiaocoder.android.fw.general.dialog.XCdialog;
import com.xiaocoder.android.fw.general.dialog.XCdialog.DialogCallBack;
import com.xiaocoder.android.fw.general.fragment.XCTitleCommonFragment;
import com.xiaocoder.android.fw.general.fragment.XCTitleCommonFragment.RightListener;
import com.xiaocoder.android.fw.general.scan.camare.CameraManager;
import com.xiaocoder.android.fw.general.scan.decoding.CaptureActivityHandler;
import com.xiaocoder.android.fw.general.scan.decoding.InactivityTimer;
import com.xiaocoder.android.fw.general.scan.image.RGBLuminanceSource;
import com.xiaocoder.android.fw.general.scan.view.ViewfinderView;
import com.xiaocoder.android_fw_general.R;

// 二维码扫描的类 ， 直接调用
public class XCScanActivity extends XCBaseActivity implements Callback, View.OnClickListener {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	/** 自己加的,用于开启关闭闪光灯,闪光灯的方法也是自己加的,以及修改了dialog,自定义了dialog */
	private CameraManager manager;
	private boolean isFlashOpen;
	private Dialog dialog;// 自定义弹出的dialog
	private TextView result_textview;
	private String result;
	/********************** 以上 *********************/

	private static final int REQUEST_CODE = 100;
	private static final int PARSE_BARCODE_SUC = 300;
	private static final int PARSE_BARCODE_FAIL = 303;
	private ProgressDialog mProgress;
	private String photo_path;
	private Bitmap scanBitmap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.xc_l_activity_scan_coder);
		super.onCreate(savedInstanceState);

		XCTitleCommonFragment title_fragment = new XCTitleCommonFragment();
		addFragment(R.id.xc_id_model_titlebar, title_fragment);
		title_fragment.setTitleCenter(true, "扫描二维码");
		title_fragment.setTitleRight(true, R.drawable.ic_launcher);
		title_fragment.setRight_listener(new RightListener() {

			@Override
			public void rightClick() {
				// 打开手机中的相册
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
				intent.setType("image/*");
				Intent wrapperIntent = Intent.createChooser(intent, "选择本地二维码图片");
				startActivityForResult(wrapperIntent, REQUEST_CODE);
			}
		});
		showTitleLayout(true);

		CameraManager.init(getApplication());
		manager = CameraManager.get();
		viewfinderView = (ViewfinderView) findViewById(R.id.capture_viewfinder);

		// 下面是自己加的
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		// 闪光灯按钮
		final ImageView xc_id_capture_flash_imageview = (ImageView) findViewById(R.id.xc_id_capture_flash_imageview);
		xc_id_capture_flash_imageview.setOnClickListener(this);

		// 自定义dialog
		dialog = XCApplication.getBase_dialog().getDefineDialog(this, XCdialog.QUERY_DIALOG, getResources().getString(R.string.dialog_query_title_scanresult), null,
				getResources().getStringArray(R.array.dialog_query_decide_scanresult), false, null, null);
		result_textview = (TextView) XCApplication.getBase_dialog().getContent_textview();

		XCApplication.getBase_dialog().setDialogCallBack(new DialogCallBack() {

			@Override
			public void confirm() {
				dialog.dismiss();
				// 关闪光灯
				if (isFlashOpen) {
					try {
						manager.closeLight();
						isFlashOpen = false;
						xc_id_capture_flash_imageview.setSelected(false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Uri content_url = Uri.parse(result);
				if (content_url.toString().startsWith("http")) {
					Intent intent = new Intent(); // 打开链接
					intent.setAction("android.intent.action.VIEW");
					intent.setData(content_url);
					startActivity(intent);
				} else {
					shortToast("该链接有误");
					onPause(); // 这个是自己加的,暂时实现连续扫描
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (handler != null) {
						handler.restartPreviewAndDecode();// 连续快速的扫描,会报获取焦点异常,这里用onPause()和onResume来暂时的解决这个问题
					}
					onResume(); // 这个是自己加的,暂时实现连续扫描
				}
			}

			@Override
			public void cancle() {
				dialog.dismiss();
				// 关闪光灯
				if (isFlashOpen) {
					try {
						manager.closeLight();
						isFlashOpen = false;
						xc_id_capture_flash_imageview.setSelected(false);
					} catch (Exception e) {

					} finally {

					}
				}
				onPause(); // 这个是自己加的,暂时实现连续扫描
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (handler != null) {
					handler.restartPreviewAndDecode();// 连续快速的扫描,会报获取焦点异常,这里用onPause()和onResume来暂时的解决这个问题
				}
				onResume(); // 这个是自己加的,暂时实现连续扫描
			}
		});
		showContentLayout();
	}

	private long start_timegap;
	private long end_timegap;

	@Override
	public void onClick(View v) {
		if (R.id.xc_id_capture_flash_imageview == v.getId()) {
			start_timegap = System.currentTimeMillis();
			if (start_timegap - end_timegap > 500) {
				if (!isFlashOpen) {
					try {
						manager.openLight();
						isFlashOpen = true;
						v.setSelected(true);
					} catch (Exception e) {

					} finally {

					}

				} else {
					try {
						manager.closeLight();
						isFlashOpen = false;
						v.setSelected(false);
					} catch (Exception e) {

					} finally {

					}

				}
				end_timegap = start_timegap;
			}
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			mProgress.dismiss();
			switch (msg.what) {
			case PARSE_BARCODE_SUC:
				onResultHandler((String) msg.obj, scanBitmap);
				break;
			case PARSE_BARCODE_FAIL:
				Toast.makeText(XCScanActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE:
				// 获取选中图片的路径
				Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
				if (cursor != null) {
					if (cursor.moveToFirst()) {
						photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					}
					cursor.close();

					mProgress = new ProgressDialog(XCScanActivity.this);
					mProgress.setMessage("正在扫描...");
					mProgress.setCancelable(false);
					mProgress.show();

					new Thread(new Runnable() {
						@Override
						public void run() {
							Result result = scanningImage(photo_path);
							if (result != null) {
								Message m = mHandler.obtainMessage();
								m.what = PARSE_BARCODE_SUC;
								m.obj = result.getText();
								mHandler.sendMessage(m);
							} else {
								Message m = mHandler.obtainMessage();
								m.what = PARSE_BARCODE_FAIL;
								m.obj = "解析数据有误";
								mHandler.sendMessage(m);
							}
						}
					}).start();
				} else {
					shortToast("解析数据有误");
				}
				break;
			}
		}
	}

	/**
	 * 扫描二维码图片的方法
	 * 
	 * @param path
	 * @return
	 */
	public Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); // 设置二维码内容的编码

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview_surfaceview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 处理扫描结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		onResultHandler(resultString, barcode);
	}

	/**
	 * 跳转到上一个页面
	 * 
	 * @param resultString
	 * @param bitmap
	 */
	private void onResultHandler(final String resultString, Bitmap bitmap) {
		if (TextUtils.isEmpty(resultString)) {
			Toast.makeText(XCScanActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			return;
		}
		/*
		 * Intent resultIntent = new Intent(); Bundle bundle = new Bundle();
		 * bundle.putString("result", resultString);
		 * bundle.putParcelable("bitmap", bitmap);
		 * resultIntent.putExtras(bundle); this.setResult(RESULT_OK,
		 * resultIntent); CaptureActivity.this.finish();
		 */
		result = resultString;
		result_textview.setText(resultString);
		dialog.show();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
			// CameraManager.get().openLight(); //开闪光灯
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onNetRefresh() {
	}

	@Override
	public void initWidgets() {

	}

	@Override
	public void listeners() {

	}

}