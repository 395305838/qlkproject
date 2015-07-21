package com.xiaocoder.android.fw.general.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xiaocoder.android_fw_general.R;

/**
 * Created by xiaocoder on 2015/7/21.
 */

public class XCBaseDialog extends Dialog {

    public static int TRAN_STYLE = R.style.xc_s_dialog;

    /*
     * 如果这里使用getLayoutInflater(),则获取不到双圈的dialog，用LayoutInflater.from可以
     */
    public LayoutInflater dialogInflater;

    public ViewGroup dialogLayout;

    public Context mContext;

    public XCBaseDialog(Context context, int theme) {
        super(context, theme);
        dialogInflater = LayoutInflater.from(context);
        mContext = context;
    }

}


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
