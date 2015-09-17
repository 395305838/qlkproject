package com.xiaocoder.buffer.function;

import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xiaocoder.android.fw.general.application.XCApp;
import com.xiaocoder.android.fw.general.util.UtilString;
import com.xiaocoder.buffer.QlkActivity;

/**
 * Created by xiaocoder on 2015/5/27.
 */
public abstract class QlkBaiduMapActivity extends QlkActivity {

    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02"; // bd09ll  , bd09
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;

    OnReceiverAddressListener addrListener;

    public interface OnReceiverAddressListener {
        void onReceiverAddressListener(String addr);
    }

    public void setOnReceiverAddressListener(OnReceiverAddressListener listener) {
        addrListener = listener;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 百度地图
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
        mLocationClient.start();

    }

    String address;

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
            }
            address = location.getAddrStr();
            XCApp.i(address);
            if (!UtilString.isBlank(address)) {
                mLocationClient.stop();
                if (addrListener != null) {
                    addrListener.onReceiverAddressListener(address);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        mLocationClient.stop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//设置定位模式
        option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
        int span = 1000;
        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(mMyLocationListener);
    }
}
