package com.xiaocoder.android.fw.general.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaocoder.android_fw_general.R;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public class XCQueryDialog extends XCBaseDialog {

    TextView title_textview;

    public TextView getContent_textview() {
        return content_textview;
    }

    TextView content_textview;
    Button cancle;
    Button confirm;
    View title_line;
    View button_between_line;
    View body_line;

    String title_content;
    String content;
    String[] decide;
    boolean isCanceledOnTouchOutside;

    public interface OnDecideListener {
        void confirm();

        void cancle();
    }

    public void setOnDecideListener(OnDecideListener listener) {
        onDecideListener = listener;
    }

    public OnDecideListener onDecideListener;


    public XCQueryDialog(Context context, int style, String title_content, String content, String[] decide, boolean isCanceledOnTouchOutside) {
        super(context, style);
        initDialog(title_content, content, decide, isCanceledOnTouchOutside);
    }

    public void initDialog(String title_content, String content, String[] decide, boolean isCanceledOnTouchOutside) {

        dialogLayout = (ViewGroup) dialogInflater.inflate(R.layout.xc_l_dialog_query, null);

        title_textview = (TextView) dialogLayout.findViewById(R.id.xc_id_dialog_query_title);
        content_textview = (TextView) dialogLayout.findViewById(R.id.xc_id_dialog_query_content);
        cancle = (Button) dialogLayout.findViewById(R.id.xc_id_dialog_query_cancle);
        confirm = (Button) dialogLayout.findViewById(R.id.xc_id_dialog_query_confirm);
        title_line = (View) dialogLayout.findViewById(R.id.xc_id_dialog_query_title_line);
        button_between_line = (View) dialogLayout.findViewById(R.id.xc_id_dialog_query_button_between_line);
        body_line = (View) dialogLayout.findViewById(R.id.xc_id_dialog_query_body_line);

        setParams(title_content, content, decide, isCanceledOnTouchOutside);
        check();

        setContentView(dialogLayout);
        setWindowLayoutStyleAttr();
    }

    public void check() {

        if (title_content != null) {
            title_textview.setText(title_content);
        } else {
            title_textview.setVisibility(View.GONE);
            title_line.setVisibility(View.GONE);
        }

        if (decide != null) {
            if (decide.length == 1) {
                cancle.setVisibility(View.GONE);
                button_between_line.setVisibility(View.GONE);
                confirm.setText(decide[0]);
            } else {
                cancle.setText(decide[0]);
                confirm.setText(decide[1]);
            }
        } else {
            cancle.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            button_between_line.setVisibility(View.GONE);
            body_line.setVisibility(View.GONE);
        }

        if (content != null) {
            content_textview.setText(content);
        }

        if (cancle != null) {
            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDecideListener != null) {
                        onDecideListener.cancle();
                    }
                }
            });
        }

        if (confirm != null) {
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDecideListener != null) {
                        onDecideListener.confirm();
                    }
                }
            });
        }
    }

    public void setWindowLayoutStyleAttr() {
        setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.95f;
        lp.dimAmount = 0.3f;
        window.setAttributes(lp);
    }

    public void setParams(String title, String content, String[] decide, boolean isCanceledOnTouchOutside) {
        this.title_content = title;
        this.content = content;
        this.decide = decide;
        this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
    }


}



