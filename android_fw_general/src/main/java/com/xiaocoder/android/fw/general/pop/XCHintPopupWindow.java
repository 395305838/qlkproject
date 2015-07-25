package com.xiaocoder.android.fw.general.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaocoder.android_fw_general.R;


public class XCHintPopupWindow extends XCBasePopupWindow implements View.OnClickListener {

    private TextView xc_id_patient_1;
    private TextView xc_id_patient_2;
    private TextView xc_id_patient_3;

    public XCHintPopupWindow(Context context, int width, int height) {
        super((ViewGroup) LayoutInflater.from(context).inflate(R.layout.xc_l_view_pop_hint, null), width, height);
    }

    @Override
    public void initWidgets() {
        xc_id_patient_1 = (TextView) popFindViewById(R.id.xc_id_patient_1);
        xc_id_patient_2 = (TextView) popFindViewById(R.id.xc_id_patient_2);
        xc_id_patient_3 = (TextView) popFindViewById(R.id.xc_id_patient_3);

    }

    @Override
    public void listener() {
        xc_id_patient_1.setOnClickListener(this);
        xc_id_patient_2.setOnClickListener(this);
        xc_id_patient_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        resetAllTextColor();
        int i = v.getId();
        if (i == R.id.xc_id_patient_1) {
            xc_id_patient_1.setTextColor(0xffe60044);
            if (onHintPopupItemClickListener != null) {
                onHintPopupItemClickListener.hint1(xc_id_patient_1);
                return;
            }

        } else if (i == R.id.xc_id_patient_2) {
            xc_id_patient_2.setTextColor(0xffe60044);
            if (onHintPopupItemClickListener != null) {
                onHintPopupItemClickListener.hint2(xc_id_patient_2);
                return;
            }
        } else if (i == R.id.xc_id_patient_3) {
            xc_id_patient_3.setTextColor(0xffe60044);
            if (onHintPopupItemClickListener != null) {
                onHintPopupItemClickListener.hint3(xc_id_patient_3);
                return;
            }
        }
        dismiss();
    }

    public void resetAllTextColor() {
        xc_id_patient_1.setTextColor(0xff666666);
        xc_id_patient_2.setTextColor(0xff666666);
        xc_id_patient_3.setTextColor(0xff666666);
    }

    public void setOnHintPopupItemClickListener(OnHintPopupItemClickListener onHintPopupItemClickListener) {
        this.onHintPopupItemClickListener = onHintPopupItemClickListener;
    }

    public interface OnHintPopupItemClickListener {

        void hint1(TextView textview);

        void hint2(TextView textview);

        void hint3(TextView textview);

    }

    private OnHintPopupItemClickListener onHintPopupItemClickListener;

}
