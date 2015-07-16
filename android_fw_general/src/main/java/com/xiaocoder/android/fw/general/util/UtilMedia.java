package com.xiaocoder.android.fw.general.util;

import android.media.MediaPlayer;

public class UtilMedia {

	// 结束播放
	public static void finishPlaying(MediaPlayer mediaplayer) {
		if (mediaplayer != null) {
			mediaplayer.stop();
			mediaplayer.release();
		}
	}

	// 开始播放
	public static MediaPlayer openVoice(String url) {
		try {
			final MediaPlayer mp = new MediaPlayer();
			// 置为初始状态
			mp.reset();
			// 设置文件路径
			mp.setDataSource(url);
			// 准备(缓冲) ,但是不知道什么时候缓冲完成
			mp.prepare();
			// 设置缓冲完成监听(当缓冲完成的时候,调用该监听器)
			mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					mp.start();
				}
			});
			mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mediaPlayer) {
					finishPlaying(mp);
				}
			});
			return mp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}


