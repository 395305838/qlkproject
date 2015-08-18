package reference;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;
import com.xiaocoder.buffer.QlkActivity;

/**
 * Created by xiaocoder on 2015/7/15.
 */
public abstract class UmengUpdateDemo extends QlkActivity {

    // 双击两次返回键退出应用
    long back_quit_time;

    @Override
    public void onBackPressed() {
        long this_quit_time = System.currentTimeMillis();
        if (this_quit_time - back_quit_time <= 1000) {
            getXCApplication().AppExit(base_context);
        } else {
            back_quit_time = this_quit_time;
            shortToast("快速再按一次退出");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        umengUpdate();
    }

    // 会弹出确认框，需要点击确认是否更新
    public void umengUpdate() {
        umengUpdateConfig();
        UmengUpdateAgent.update(this);
    }

    // 如 主动点击设置中的检测更新
    public void umengCheckUpdate() {
        umengUpdateConfig();
        UmengUpdateAgent.forceUpdate(this);
    }

    public void umengSlientUdate() {
        umengUpdateConfig();
        UmengUpdateAgent.silentUpdate(this);
    }

    public void umengUpdateConfig() {
        // 重置状态，如果每次的参数是不一样的，则很有必要调用这个方法
        UmengUpdateAgent.setDefault();
        // false为非wifi下也会更新，以及针对机顶盒等可能不支持或者没有无线网络的设备
        // 该方法对于手动更新和静默更新无效
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        // 可以提示umeng更新的配置是否正确
        UmengUpdateAgent.setUpdateCheckConfig(true);
        // true为增量更新,false为全量更新
        UmengUpdateAgent.setDeltaUpdate(true);
        // 通知栏显示高级样式，api14以上
        UmengUpdateAgent.setRichNotification(true);
        // 是否显示日志
        UpdateConfig.setDebug(true);
    }

//    UmengUpdateAgent.setAppkey(null); //如果使用该方法设置，优先使用这里设置的值，如果为null则从AndroidManifest.xml里读取
//    UmengUpdateAgent.setChannel(null);//如果使用该方法设置，优先使用这里设置的值，如果为null则从AndroidManifest.xml里读取。
//    UmengUpdateAgent.setUpdateOnlyWifi(true);//针对机顶盒等可能不支持或者没有无线网络的设备，请同样将该参数设为false。 注意：该参数仅针对自动更新接口update(context)，对于手动更新接口（无视网络环境）和静默下载更新接口（仅WIFI）无效。
//    UmengUpdateAgent.setDeltaUpdate(true);
//    UmengUpdateAgent.setUpdateAutoPopup(true);//对于自动更新和手动更新接口，该参数控制检测到更新后自动弹出更新提示；对于静默下载更新，该参数控制检测到更新后如果未下载自动下载，如果已下载，自动弹出更新提示。
//    UmengUpdateAgent.setRichNotification(true);//richNotification 布尔值true(默认)Android4.1及以上系统中通知栏显示暂停/取消按钮，false下载通知栏使用默认样式。
//    UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_DIALOG);//更新提示的样式UpdateStatus.STYLE_DIALOG(默认)使用对话框进行更新提示，UpdateStatus.STYLE_NOTIFICATION使用通知栏进行更新提示。

//    UmengUpdateAgent.setUpdateListener(null); // 监听是否是wifi是否有更新的监听
//    UmengUpdateAgent.setDialogListener(null); // 监听dialog的按钮点击，返回键为用户选择以后再说，关闭对话框
//    UmengUpdateAgent.setDownloadListener(null); // 下载进度的监听

}
