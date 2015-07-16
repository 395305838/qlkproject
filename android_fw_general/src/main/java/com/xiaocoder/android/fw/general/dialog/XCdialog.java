package com.xiaocoder.android.fw.general.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaocoder.android.fw.general.base.XCBaseMainActivity;
import com.xiaocoder.android.fw.general.http.XCHttpAsyn;
import com.xiaocoder.android_fw_general.R;

import java.util.ArrayList;
import java.util.List;

/*
 * dialogs 说明
 * 
 * 自定义菜单:
 onCreateOptionMenu方法用于创建选项菜单,在显示选项菜单之前,系统会调用onMenuOpened方法,如果该方法返回false,则在onCreateOptionMenu中创建的
 选项菜单将不会显示,因此,在onMenuOpened中弹出用于显示自定义菜单的创建

 dialog.getWindow().getAttributes().dimAount设置透明度  0为完全透明
 dialog.setContentView()
 点击菜单键后对话框如何显示在屏幕的最底部:
 dialog.getWindow.setGravity(Gravity.Bottom | Gravity.LEFT)

 点菜单键对话框如何dismiss
 dialog.setkeyListener监听中,判断是否是按了菜单键

 点击非对话框区域消失,且点击回退键可以消失
 d.setCancledOnTouchOutSide(false) 

 按返回键都无法取消dialog
 dialog.setCancelable(false); 


 对话框弹出来后,按钮(如button)还可以点的设置
 window.setFlags(WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW, WindowManager.LayoutParams.LAST_SYSTEM_WINDOW);
 -------------------------------------------------------------------------------------------------
 <style name="mydialog" parent="@android:style/Theme.Dialog">
 <item name="android:windowFrame">@null</item><!-- 无边界 ,否则会出现框框的线条-->
 <item name="android:windowIsFloating">true</item><!-- 悬浮在activity上 -->
 <item name="android:windowIsTranslucent">true</item>
 <item name="android:windowNoTitle">true</item> <!-- 默认都是有title -->
 <item name="android:windowBackground">@drawable/out</item>//全透明
 </style>
 ---------------------------------------------------------------------------------------------------
 //style中已经配置了,默认的对话框都是有title的  ,必须在setContentView之前设置
 Dialog dialog = new Dialog(this , R.style.mydialog);
 dialog.setContentView(R.layout.dialog_layout);
 //		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
 //		dialog.setCanceledOnTouchOutside(true);
 //		dialog.setCancelable(false); //按返回键都无法取消dialog
 //		dialog.setOnDismissListener(listener);
 //		dialog.setOnShowListener(listener);
 //		dialog.setOnCancelListener(listener);
 //     dialog.setOnKeyListener(new OnKeyListener() {
 @Override
 public boolean onKey(DialogInterface dialog, int keyCode,
 KeyEvent event) {
 if(keyCode == KeyEvent.KEYCODE_BACK){
 dialog.dismiss();
 }
 return false;
 }
 });

 Window window = dialog.getWindow();
 WindowManager.LayoutParams lp = window.getAttributes();
 //		window .setGravity(Gravity.LEFT | Gravity.TOP);
 //		lp.x = 100; // 新位置X坐标
 //		lp.y = 100; // 新位置Y坐标
 //		lp.width = 300; // 宽度
 //		lp.height = 300; // 高度
 lp.alpha = 0.7f; // 这个是dialog的透明度,1是原本的显示状态
 lp.dimAmount = 0.0f; //这个是整个窗口(全屏)的透明度,1.0是黑的,0.0是亮的
 window.setAttributes(lp);
 //window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
 //这句代码的作用是,按屏幕无法取消dialog(但是后面的按钮可以工作,即无模式窗口),但是只有按返回键才可以取消dialog(解决方案可以在dispatchTouchEvent中拦截,只要触摸了,dismiss())
 window.setFlags(WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW, WindowManager.LayoutParams.LAST_SYSTEM_WINDOW);
 dialog.show();

 总结:当dialog出现时,它背后的view(activity)是无法获得焦点的;但是如果设置了无模式则背后的view可以获取焦点,但是dialog会无法获取焦点,所以如果需要让背后的view也可以点击,就用popupwindow
 */

//代码设置帧动画
/*
 animationDrawable=new AnimationDrawable();
 animationDrawable.setOneShot(false);//是否执行一次动画
 //duration: 帧切换的时间，毫秒
 for(int i=0;i<11;i++){
 Drawable frame=getResources().getDrawable(R.drawable.girl_1+i);
 animationDrawable.addFrame(frame, 200);
 }

 //开始或者暂停
 public void start(View v){
 if(animationDrawable.isRunning()){
 animationDrawable.stop();
 }else{
 animationDrawable.start();
 }	
 */

// 平移动画
/*
 如:TranslateAnimation an = new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue)
 fromXTpye: 水平方向的x的值的类型
 Absolut( 相对于原点的位置-20,30)  
 relative_to_self（相对于自己x方向的宽度的比例）
 relative_to_parent（相对于父控件x方向的宽度的比例,如果父控件的宽度是全屏,那么就是移动全屏的长度）  
 对于0 值 ，就设为Absolut类型
 //假如0.2为开始的起点,则开始时直接跳到0.2开始;如果起点大于结束点,则倒着走的效果
 //反转:决定0-1 1-0   还是 0-1 0-1

 public void translate(View v){
 TranslateAnimation animation=new TranslateAnimation(
 Animation.ABSOLUTE, 0, 
 Animation.ABSOLUTE, 0, 
 Animation.RELATIVE_TO_SELF, 2,
 Animation.RELATIVE_TO_SELF, 0.8f);
 animation.setDuration(3000);
 animation.setFillAfter(true);
 animation.setInterpolator(new BounceInterpolator());//速率改变器（弹簧效果）
 animation.setRepeatCount(2);
 animation.setRepeatMode(Animation.REVERSE);
 tween.startAnimation(animation);
 }
 */

//旋转动画
/* fromDegrees： 开始的度数  0  为从当前的位置开始旋转,顺时针转90度,即从左向右转
 * toDegrees： 终止点的度数  90
 * pivotX： 参考点的x位置  如 50%
 * pivotY： 参考点的y位置 如 50%
 float pivotValue = 0.5f;    // SUPPRESS CHECKSTYLE
 float toDegree = 720.0f;    // SUPPRESS CHECKSTYLE
 mRotateAnimation = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue,
 Animation.RELATIVE_TO_SELF, pivotValue);

 RotateAnimation animation=new RotateAnimation(0, 360, 30, 30);
 animation.setDuration(3000);
 animation.setFillAfter(true);
 animation.setInterpolator(new BounceInterpolator());//速率改变器（弹簧效果）
 animation.setRepeatCount(2);
 animation.setRepeatMode(Animation.REVERSE);
 tween.startAnimation(animation);
 */

//透明度动画
/*
 fromAlpha： 起始点的透明度值  0（完全透明）to 1 （完全不透明）  ，它是float值
 toAlpha：    终止点的透明度值   0 to 1
 AlphaAnimation alphaAnimation=new AlphaAnimation(0, 0.8f);
 alphaAnimation.setDuration(000);//动画的持续时间
 alphaAnimation.setFillAfter(true);//设置动画最后的填充效果，设置终点的效果为当前结束时的效果
 alphaAnimation.setRepeatCount(2);//设置动画重复的次数
 alphaAnimation.setRepeatMode(Animation.REVERSE);//设置重复模式 REVERSE 反转,即第二次是从什么状态开始
 //alphaAnimation.setRepeatMode(Animation.RESTART);
 //对动画设置监听
 alphaAnimation.setAnimationListener(new AnimationListener() {
 //动画开始时
 @Override
 public void onAnimationStart(Animation animation) {
 }
 //动画重复时
 @Override
 public void onAnimationRepeat(Animation animation) {
 }
 //动画结束
 @Override
 public void onAnimationEnd(Animation animation) {
 Toast.makeText(TweenActivity.this, "动画结束", Toast.LENGTH_LONG).show();
 }
 });
 //对View控件设置动画
 tween.startAnimation(alphaAnimation);
 */

//缩放动画scale 
/*缩放 0.5,2,1,1  从x轴的一半开始,y轴不变-->x轴最后是原始的两倍,y轴不变
 * fromX ：x方向的起点缩放因子，也就是比例
 * toX：　　ｘ方向的终点的缩放因子
 ScaleAnimation animation=new ScaleAnimation(0.5f, 2, 1, 2);
 animation.setDuration(3000);
 animation.setFillAfter(true);
 animation.setInterpolator(new BounceInterpolator());//速率改变器（弹簧效果）
 animation.setRepeatCount(2);
 animation.setRepeatMode(Animation.REVERSE);
 tween.startAnimation(animation);
 */

//集合动画
/*
 AnimationSet set=new AnimationSet(true);
 ScaleAnimation animation=new ScaleAnimation(0.5f, 2, 1, 2);
 animation.setDuration(3000);
 animation.setFillAfter(true);
 animation.setInterpolator(new BounceInterpolator());//速率改变器（弹簧效果）
 animation.setRepeatCount(2);
 animation.setRepeatMode(Animation.REVERSE);
 set.addAnimation(animation);//往动画集合中添加动画
 */

// xml动画
/*
 //    	Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
 //    	Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
 //    	Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate);
 //    	Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
 //    	Animation animation = AnimationUtils.loadAnimation(this, R.anim.set);
 */

/*
 Interpolator 定义了动画的变化速度，可以实现匀速、正加速、负加速、无规则变加速等；
 AccelerateDecelerateInterpolator，延迟减速，在动作执行到中间的时候才执行该特效。
 AccelerateInterpolator, 会使慢慢以(float)的参数降低速度。
 LinearInterpolator，平稳不变的
 DecelerateInterpolator，在中间加速,两头慢
 CycleInterpolator，曲线运动特效，要传递float型的参数。
 BounceInterpolator 弹簧效果
 */

/**
 * @author xiaocoder
 * @date 2014-10-17 下午1:52:37
 */
public class XCdialog {
	private Context mContext;
	private Dialog dialog;
	private ViewGroup dialog_layout;
	private Button cancle;
	private Button confirm;
	private TextView title;
	private TextView content_textview;
	private EditText content_edittext;
	private ImageView anim_imageview;
	private TextView anim_textview;
	private List<Button> list;
	// 两个button之间的线
	private View button_between_line;
	// 标题下的线
	private View title_line;
	private View body_line;

	// 一个带有透明背景的空dialog
	public static int NAKED_DIALOG = 0;
	// 带有edittext输入的且含有取消和确定按钮的dialog
	public static int INPUT_DIALOG = 1;
	// 带有询问信息的且含有有取消和确定按钮的dialog
	public static int QUERY_DIALOG = 2;
	// 类似菜单的dialog
	public static int ITEMS_DIALOG = 3;
	// 只有一个系统的圈圈
	public static int SYSTEM_CIRCLE_DIALOG_V = 4;
	public static int SYSTEM_CIRCLE_DIALOG_H = 7;
	// 左边是一个imageview,右边文字
	public static int ANIMATION_DIALOG_H = 5;
	// 上面是一个imageview,下面文字
	public static int ANIMATION_DIALOG_V = 6;

	private static XCdialog instance = new XCdialog();

	private DialogCallBack callback;

	public interface DialogCallBack {
		public void confirm();

		public void cancle();
	}

	public void setDialogCallBack(DialogCallBack callback) {
		this.callback = callback;
	}

	private XCdialog() {
	}

	public static XCdialog getXCDialogInstance() {
		return instance;
	}

	public Dialog getDefineDialog(Context context, int MODE_TYPE, String head, String[] items, String[] decide) {
		return getDefineDialog(context, MODE_TYPE, head, items, decide, true, null, null);
	}

	public Dialog getDefineDialog(Context context, int MODE_TYPE, String head, String[] items, String[] decide, boolean isCanceledOnTouchOutside) {
		return getDefineDialog(context, MODE_TYPE, head, items, decide, isCanceledOnTouchOutside, null, null);
	}

	// 这里仅仅是获取,并没有show
	public Dialog getDefineDialog(Context context, int MODE_TYPE, String head, String[] items, String[] decide, boolean isCanceledOnTouchOutside, Float the_apha_of_dialog, Float the_dim_of_screen) {
		if (context == null) {
			return null;
		}

		// 这里没有复用
		dismiss();
		mContext = context;
		dialog = new Dialog(mContext, R.style.xc_s_dialog);

		LayoutInflater inflater = LayoutInflater.from(context);
		if (MODE_TYPE == INPUT_DIALOG) {
			dialog_layout = (ViewGroup) inflater.inflate(R.layout.xc_l_dialog_input, null);
			title = (TextView) dialog_layout.findViewById(R.id.xc_id_dialog_input_title);
			content_edittext = (EditText) dialog_layout.findViewById(R.id.xc_id_dialog_input_edittext);
			cancle = (Button) dialog_layout.findViewById(R.id.xc_id_dialog_input_cancle);
			confirm = (Button) dialog_layout.findViewById(R.id.xc_id_dialog_input_confirm);
			title_line = (View) dialog_layout.findViewById(R.id.xc_id_dialog_input_title_line);
			button_between_line = (View) dialog_layout.findViewById(R.id.xc_id_dialog_input_button_between_line);
			body_line = (View) dialog_layout.findViewById(R.id.xc_id_dialog_input_body_line);
		} else if (MODE_TYPE == QUERY_DIALOG) {
			dialog_layout = (ViewGroup) inflater.inflate(R.layout.xc_l_dialog_query, null);
			title = (TextView) dialog_layout.findViewById(R.id.xc_id_dialog_query_title);
			content_textview = (TextView) dialog_layout.findViewById(R.id.xc_id_dialog_query_content);
			cancle = (Button) dialog_layout.findViewById(R.id.xc_id_dialog_query_cancle);
			confirm = (Button) dialog_layout.findViewById(R.id.xc_id_dialog_query_confirm);
			title_line = (View) dialog_layout.findViewById(R.id.xc_id_dialog_query_title_line);
			button_between_line = (View) dialog_layout.findViewById(R.id.xc_id_dialog_query_button_between_line);
			body_line = (View) dialog_layout.findViewById(R.id.xc_id_dialog_query_body_line);
		} else if (MODE_TYPE == ITEMS_DIALOG) {
			// 这是一个包含title的布局, 该布局中,代码add item
			dialog_layout = (ViewGroup) inflater.inflate(R.layout.xc_l_dialog_items, null);
			title = (TextView) dialog_layout.findViewById(R.id.xc_id_dialog_items_title);
			if (items != null) {
				list = new ArrayList<Button>();
				for (String item : items) {
					ViewGroup viewgroup = (ViewGroup) inflater.inflate(R.layout.xc_l_dialog_item_of_items, null);
					Button button = ((Button) viewgroup.getChildAt(1));
					button.setText(item);
					list.add(button);
					dialog_layout.addView(viewgroup);
				}
			}
		} else if (NAKED_DIALOG == MODE_TYPE) {
			return dialog;
		} else {
			return null;
		}

		// 标题title
		if (head != null) {
			title.setText(head);
		} else {
			title.setVisibility(View.GONE);
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
			if (MODE_TYPE == INPUT_DIALOG || MODE_TYPE == QUERY_DIALOG) {
				cancle.setVisibility(View.GONE);
				confirm.setVisibility(View.GONE);
				button_between_line.setVisibility(View.GONE);
				body_line.setVisibility(View.GONE);
			}
		}

		if (cancle != null) {
			cancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (callback != null) {
						callback.cancle();
					}
				}
			});
		}

		if (confirm != null) {
			confirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (callback != null) {
						callback.confirm();
					}
				}
			});
		}

        if(NAKED_DIALOG != MODE_TYPE){
            dialog.setContentView(dialog_layout);
        }

		dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		// window .setGravity(Gravity.LEFT | Gravity.TOP);
		// lp.x = 100; // 新位置X坐标
		// lp.y = 100; // 新位置Y坐标
		// lp.width = 300; // 宽度
		// lp.height = 300; // 高度
		if (the_apha_of_dialog != null) {
			lp.alpha = the_apha_of_dialog; // 这个是dialog的透明度,1是原本的显示状态,之前设置的是0.7f
		} else {
			lp.alpha = 1.0f;
		}
		if (the_dim_of_screen != null) {
			lp.dimAmount = the_dim_of_screen; // 这个是整个窗口的透明度,1.0是黑的,0.0是亮的,之前设置的是0.3f
		} else {
			lp.dimAmount = 0.3f;
		}
		window.setAttributes(lp);
		return dialog;
	}

	// 这里仅仅是获取,并没有show
	public Dialog getWaitingDialog(Context context, int MODE_TYPE, String desc, boolean isCanceledOnTouchOutside, Float the_apha_of_dialog, Float the_dim_of_screen) {
		if (context == null) {
			return null;
		}

		dismiss();
		mContext = context;
		dialog = new Dialog(mContext, R.style.xc_s_dialog);
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					XCHttpAsyn.httpFinish();
					if (!(mContext instanceof XCBaseMainActivity)) {
						XCHttpAsyn.isNeting = false;
						((Activity) mContext).finish();
					}
				}
				return false;
			}
		});

		LayoutInflater inflater = LayoutInflater.from(context);
		if (MODE_TYPE == SYSTEM_CIRCLE_DIALOG_V) {
			// 只有圈圈的
			dialog_layout = (ViewGroup) inflater.inflate(R.layout.xc_l_dialog_system_circle_v, null);
			anim_textview = (TextView) dialog_layout.getChildAt(1);
		} else if (MODE_TYPE == SYSTEM_CIRCLE_DIALOG_H) {
			dialog_layout = (ViewGroup) inflater.inflate(R.layout.xc_l_dialog_system_circle_h, null);
			anim_textview = (TextView) dialog_layout.getChildAt(1);
		} else if (MODE_TYPE == ANIMATION_DIALOG_H) {
			dialog_layout = (ViewGroup) inflater.inflate(R.layout.xc_l_dialog_animation_h, null);
			anim_imageview = (ImageView) dialog_layout.getChildAt(0);
			anim_textview = (TextView) dialog_layout.getChildAt(1);
		} else if (MODE_TYPE == ANIMATION_DIALOG_V) {
			dialog_layout = (ViewGroup) inflater.inflate(R.layout.xc_l_dialog_animation_v, null);
			anim_imageview = (ImageView) dialog_layout.getChildAt(0);
			anim_textview = (TextView) dialog_layout.getChildAt(1);
		} else {
			return null;
		}

		if (anim_textview != null) {
			if (desc != null) {
				anim_textview.setText(desc);
			} else {
				anim_textview.setVisibility(View.GONE);
			}
		}

		dialog.setContentView(dialog_layout);
		dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		// window .setGravity(Gravity.LEFT | Gravity.TOP);
		// lp.x = 100; // 新位置X坐标
		// lp.y = 100; // 新位置Y坐标
		// lp.width = 300; // 宽度
		// lp.height = 300; // 高度
		if (the_apha_of_dialog != null) {
			lp.alpha = the_apha_of_dialog; // 这个是dialog的透明度,1是原本的显示状态,之前设置的是0.7f
		} else {
			lp.alpha = 1.0f;
		}
		if (the_dim_of_screen != null) {
			lp.dimAmount = the_dim_of_screen; // 这个是整个窗口的透明度,1.0是黑的,0.0是亮的,之前设置的是0.3f
		} else {
			lp.dimAmount = 0.3f;
		}
		window.setAttributes(lp);
		return dialog;
	}

	// 可以获取到然后设置背景等
	public ViewGroup getDialogLayout() {
		return dialog_layout;
	}

	public Button getCancle() {
		return cancle;
	}

	public Button getConfirm() {
		return confirm;
	}

	public TextView getTitle() {
		return title;
	}

	public TextView getContent_textview() {
		return content_textview;
	}

	public EditText getContent_edittext() {
		return content_edittext;
	}

	public List<Button> getItemList() {
		return list;
	}

	public ImageView getAnim_imageview() {
		return anim_imageview;
	}

	public TextView getAnim_textview() {
		return anim_textview;
	}

	public Dialog getCurrentDialog() {
		return dialog;
	}

	public void show() {
		if (dialog != null && !dialog.isShowing()) {
			dialog.show();
		}
	}

	public void show(boolean isBackAlpha) {
		if (dialog != null && !dialog.isShowing()) {
			if (isBackAlpha) {
				dialog_layout.setBackgroundColor(0x00000000);
			}
			dialog.show();
		}
	}

	public void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog = null;
	}

	/**
	 *
	 * @date 2014-10-20 下午1:15:38
	 * 
	 *       base_dialog = base_dialogsHelper.getWaitingDialog(base_context,
	 *       DialogsHelper.ANIMATION_DIALOG_V,
	 *       base_resource.getString(R.string.map_refresh_location)
	 * 
	 *       设置帧动画的drawable ,
	 *       base_dialogsHelper.showFrameAnimDialog(base_resource
	 *       .getDrawable(R.drawable.xc_dd_dialog_frame), true, true);
	 */
	public void showFrameAnimDialog(Drawable drawable, boolean isAnim, boolean isBackAlpha) {
		if (isBackAlpha) {
			dialog_layout.setBackgroundColor(0x00000000);
		}
		if (isAnim) {
			// 如果是帧动画, 则设置帧动画
			anim_imageview.setImageDrawable((AnimationDrawable) drawable);
			((AnimationDrawable) drawable).start();
			// ((AnimationDrawable) anim_imageview.getBackground()).start(); //
			// 可以在onstart()方法中判断 isRunning 与stop
		} else {
			// 如果不是帧动画 , 则仅设置图片
			anim_imageview.setImageDrawable(drawable);
		}
		// 如果之前并没有获取dialog,则这里无法显示
		show();
	}

	/**
	 * @date 2015-2-27 上午10:41:18
	 * 
	 *       1 需要先获取dialog
	 * 
	 *       2 设置普通drawable 和 动画
	 */
	public void showRotateAnimDialog(Animation anim, Drawable drawable, boolean isBackAlpha) {
		if (isBackAlpha) {
			dialog_layout.setBackgroundColor(0x00000000);
		}
		anim_imageview.setImageDrawable(drawable);
		anim_imageview.startAnimation(anim);
		show();
	}

}
