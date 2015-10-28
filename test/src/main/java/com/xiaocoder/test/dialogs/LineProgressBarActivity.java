package com.xiaocoder.test.dialogs;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.xiaocoder.android.fw.general.view.XCLineProgressBar;
import com.xiaocoder.middle.MActivity;
import com.xiaocoder.test.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xiaocoder on 2015/8/18.
 */
public class LineProgressBarActivity extends MActivity implements XCLineProgressBar.OnProgressBarListener {

    private Timer timer;

    private XCLineProgressBar bnp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_line_progressbar);
        super.onCreate(savedInstanceState);

        bnp = (XCLineProgressBar) findViewById(R.id.numberbar1);
        bnp.setOnProgressBarListener(this);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bnp.incrementProgressBy(1);
                    }
                });
            }
        }, 1000, 100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onProgressChange(int current, int max) {
        if (current == max) {
            Toast.makeText(getApplicationContext(), "progressBar is finished", Toast.LENGTH_SHORT).show();
            bnp.setProgress(0);
        }
    }


    // 无网络时,点击屏幕后回调的方法
    @Override
    public void onNetRefresh() {

    }

    // 初始化控件
    @Override
    public void initWidgets() {

    }

    // 设置监听
    @Override
    public void listeners() {

    }

}
