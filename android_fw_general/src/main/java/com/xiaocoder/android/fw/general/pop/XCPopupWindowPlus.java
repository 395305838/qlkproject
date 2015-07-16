package com.xiaocoder.android.fw.general.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xiaocoder.android_fw_general.R;

/**
 * Created by Administrator on 14-1-21. 多了一个从我的处方单中获取的button
 */
public class XCPopupWindowPlus extends XCBasePopupWindow implements View.OnClickListener {

	private TextView xc_id_pop_photoUpload;
	private TextView xc_id_pop_localAlbum;
	private TextView xc_id_pop_cancel;
	private TextView xc_id_pop_net_prescrition;
	private onPhotoPopupItemClickListener mOnPhotoPopupItemClickListener;

	public XCPopupWindowPlus(Context context, int width, int height) {
		super(LayoutInflater.from(context).inflate(R.layout.xc_l_pop_window_photo, null), width, height);
		setAnimationStyle(R.style.xc_s_pop_from_down_to_up);
	}

	@Override
	public void initViews() {
		xc_id_pop_photoUpload = (TextView) findViewById(R.id.xc_id_pop_photoUpload);
		xc_id_pop_localAlbum = (TextView) findViewById(R.id.xc_id_pop_localAlbum);
		xc_id_pop_cancel = (TextView) findViewById(R.id.xc_id_pop_cancel);
		xc_id_pop_net_prescrition = (TextView) findViewById(R.id.xc_id_pop_net_prescrition);
	}

	@Override
	public void initEvents() {
		xc_id_pop_photoUpload.setOnClickListener(this);
		xc_id_pop_localAlbum.setOnClickListener(this);
		xc_id_pop_cancel.setOnClickListener(this);
		xc_id_pop_net_prescrition.setOnClickListener(this);
	}

	@Override
	public void init() {

	}

	public void setTvPopNetPrescriptionVisiable(boolean show) {
		if (show) {
			xc_id_pop_net_prescrition.setVisibility(View.VISIBLE);
		} else {
			xc_id_pop_net_prescrition.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.xc_id_pop_photoUpload) {
			if (mOnPhotoPopupItemClickListener != null) {
				mOnPhotoPopupItemClickListener.onPhotoUpload();
			}
		} else if (id == R.id.xc_id_pop_localAlbum) {
			if (mOnPhotoPopupItemClickListener != null) {
				mOnPhotoPopupItemClickListener.onLocalAlbum();
			}
		} else if (id == R.id.xc_id_pop_cancel) {
			if (mOnPhotoPopupItemClickListener != null) {
				mOnPhotoPopupItemClickListener.onCancel();
			}
		} else if (id == R.id.xc_id_pop_net_prescrition) {
			if (mOnPhotoPopupItemClickListener != null) {
				mOnPhotoPopupItemClickListener.onNetPrescription();
			}
		}
		dismiss();
	}

	public void setOnPhotoPopupItemClickListener(onPhotoPopupItemClickListener mOnPhotoPopupItemClickListener) {
		this.mOnPhotoPopupItemClickListener = mOnPhotoPopupItemClickListener;
	}

	public interface onPhotoPopupItemClickListener {
		void onPhotoUpload();

		void onLocalAlbum();

		void onCancel();

		void onNetPrescription();
	}
}
