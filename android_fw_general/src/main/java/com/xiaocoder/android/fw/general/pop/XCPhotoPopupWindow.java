package com.xiaocoder.android.fw.general.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaocoder.android_fw_general.R;

/**
 * Created by xiaocoder on 14-1-21
 */
public class XCPhotoPopupWindow extends XCBasePopupWindow implements View.OnClickListener {

    private TextView xc_id_pop_photoUpload;
    private TextView xc_id_pop_localAlbum;
    private TextView xc_id_pop_cancel;
    private TextView xc_id_pop_net_prescrition;
    private onPhotoPopupItemClickListener mOnPhotoPopupItemClickListener;

    public XCPhotoPopupWindow(Context context, int width, int height) {
        super((ViewGroup) LayoutInflater.from(context).inflate(R.layout.xc_l_pop_window_photo, null), width, height);

        setAnimationStyle(R.style.xc_s_pop_from_down_to_up);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

    }

    @Override
    public void initWidgets() {
        xc_id_pop_photoUpload = (TextView) popFindViewById(R.id.xc_id_pop_photoUpload);
        xc_id_pop_localAlbum = (TextView) popFindViewById(R.id.xc_id_pop_localAlbum);
        xc_id_pop_cancel = (TextView) popFindViewById(R.id.xc_id_pop_cancel);
        xc_id_pop_net_prescrition = (TextView) popFindViewById(R.id.xc_id_pop_net_prescrition);
    }

    @Override
    public void listener() {
        xc_id_pop_photoUpload.setOnClickListener(this);
        xc_id_pop_localAlbum.setOnClickListener(this);
        xc_id_pop_cancel.setOnClickListener(this);
        xc_id_pop_net_prescrition.setOnClickListener(this);
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
