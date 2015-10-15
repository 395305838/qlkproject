package com.xiaocoder.android.fw.general.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;

public class XCAnimationHelper {
    /**
     * 透明度动画 fromAlpha： 起始点的透明度值 0（完全透明）to 1 （完全不透明） ，它是float值 toAlpha： 终止点的透明度值 0 to 1
     */
    public static Animation getAlphaAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 0.8f);
        alphaAnimation.setDuration(3000);// 动画的持续时间
        alphaAnimation.setFillAfter(true);// 设置动画最后的填充效果，设置终点的效果为当前结束时的效果
        alphaAnimation.setRepeatCount(2);// 设置动画重复的次数
        alphaAnimation.setRepeatMode(Animation.REVERSE);// Animation.RESTART;// REVERSE,反转,即第二次是从什么状态开始
        return alphaAnimation;
    }

    /**
     * 平移动画 TranslateAnimation an = new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue) Absolut( 相对于原点的位置-20,30) ,
     * relative_to_self（相对于自己x方向宽度的比例）, relative_to_parent（相对于父控件x方向的宽度的比例） 对于0 值 ，就设为Absolut类型 fromXType: 水平方向的x的值的类型
     */
    public static Animation getTranslateAnimation() {
        TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.RELATIVE_TO_SELF, 2, Animation.RELATIVE_TO_SELF, 0.8f);
        animation.setDuration(3000);
        animation.setFillAfter(true);
        // animation.setInterpolator(new BounceInterpolator());//速率改变器（弹簧效果）
        animation.setRepeatCount(2);
        // animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

    public static Animation getTranslateAnimationByAbsolute(int fromx, int tox, int fromy, int toy, long duration) {
        TranslateAnimation animation = new TranslateAnimation(fromx, tox, fromy, toy);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        animation.setInterpolator(new AccelerateInterpolator());
        return animation;
    }

    public static Animation getTranslateAnimationByRatio(int fromx, int tox, int fromy, int toy, long duration) {
        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, fromx, TranslateAnimation.RELATIVE_TO_SELF, tox, TranslateAnimation.RELATIVE_TO_SELF, fromy,
                TranslateAnimation.RELATIVE_TO_SELF, toy);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        return animation;
    }

    /**
     * 缩放动画 fromX ：x方向的起点缩放因子，也就是比例 toX：　　ｘ方向的终点的缩放因子
     */
    public static Animation getScaleAnimation(View v) {
        ScaleAnimation animation = new ScaleAnimation(0.5f, 2, 1, 2);
        animation.setDuration(3000);
        animation.setFillAfter(true);
        animation.setRepeatCount(2);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

    /**
     * 旋转动画 fromDegrees： 开始的度数 toDegrees： 终止点的度数 pivotX： 参考点的x位置 pivotY： 参考点的y位置
     */
    public static Animation getRatoteAnimation() {
        // RotateAnimation animation = new RotateAnimation(0, 360, 30, 30);
        RotateAnimation animation = new RotateAnimation(0.0f, 720.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(7000);
        animation.setFillAfter(true);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        return animation;
    }

	/*
     * //复合动画 public void set(View v){ //shareInterpolator :共享速率器 AnimationSet set=new AnimationSet(true); ScaleAnimation animation=new ScaleAnimation(0.5f, 2, 1, 2); animation.setDuration(3000);
	 * animation.setFillAfter(true); // animation.setInterpolator(new BounceInterpolator());//速率改变器（弹簧效果） animation.setRepeatCount(2); animation.setRepeatMode(Animation.REVERSE);
	 * set.addAnimation(animation);//往动画集合中添加动画 //从xml中导入动画序列 Animation rotateAnimation=AnimationUtils.loadAnimation(this, R.anim.rotate); set.addAnimation(rotateAnimation);
	 * tween.startAnimation(set);//开始集合动画
	 * }
	 */

    public static AnimationDrawable getAnimationDrawable(Context context, ArrayList<Integer> list, int gap) {
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.setOneShot(false);// 是否执行一次动画
        Resources resouces = context.getResources();
        for (Integer i : list) {
            Drawable frame = resouces.getDrawable(i);
            /*
             * 向帧动画中添加关键帧 frame： Drawable duration: 帧切换的时间，毫秒
             */
            animationDrawable.addFrame(frame, gap);
        }
        return animationDrawable;
    }

}


/**
 * 如:TranslateAnimation an = new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue)
 * fromXTpye: 水平方向的x的值的类型
 * Absolut( 相对于原点的位置-20,30)
 * relative_to_self（相对于自己x方向的宽度的比例）
 * relative_to_parent（相对于父控件x方向的宽度的比例,如果父控件的宽度是全屏,那么就是移动全屏的长度）
 * 对于0 值 ，就设为Absolut类型
 * 假如0.2为开始的起点,则开始时直接跳到0.2开始;如果起点大于结束点,则倒着走的效果
 * 反转:决定0-1 1-0   还是 0-1 0-1
 * <p/>
 * <p/>
 * 旋转动画
 * fromDegrees： 开始的度数  0  为从当前的位置开始旋转,顺时针转90度,即从左向右转
 * toDegrees： 终止点的度数  90
 * pivotX： 参考点的x位置  如 50%
 * pivotY： 参考点的y位置 如 50%
 * <p/>
 * <p/>
 * 透明度动画
 * fromAlpha： 起始点的透明度值  0（完全透明）to 1 （完全不透明）  ，它是float值
 * toAlpha：    终止点的透明度值   0 to 1
 * <p/>
 * scale :缩放 0.5,2,1,1  从x轴的一半开始,y轴不变-->x轴最后是原始的两倍,y轴不变
 * <p/>
 * Interpolator 定义了动画的变化速度，可以实现匀速、正加速、负加速、无规则变加速等； AccelerateDecelerateInterpolator，延迟减速，在动作执行到中间的时候才执行该特效。 AccelerateInterpolator, 会使慢慢以(float)的参数降低速度。 LinearInterpolator，平稳不变的
 * DecelerateInterpolator，在中间加速,两头慢 CycleInterpolator，曲线运动特效，要传递float型的参数。 BounceInterpolator 弹簧效果
 * <p/>
 * alphaAnimation.setAnimationListener();
 * Animation animation=AnimationUtils.loadAnimation(this, R.anim.rotate);
 */

/**
 * Interpolator 定义了动画的变化速度，可以实现匀速、正加速、负加速、无规则变加速等； AccelerateDecelerateInterpolator，延迟减速，在动作执行到中间的时候才执行该特效。 AccelerateInterpolator, 会使慢慢以(float)的参数降低速度。 LinearInterpolator，平稳不变的
 * DecelerateInterpolator，在中间加速,两头慢 CycleInterpolator，曲线运动特效，要传递float型的参数。 BounceInterpolator 弹簧效果
 * <p/>
 * alphaAnimation.setAnimationListener();
 * Animation animation=AnimationUtils.loadAnimation(this, R.anim.rotate);
 */

