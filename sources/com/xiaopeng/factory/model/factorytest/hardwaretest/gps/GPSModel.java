package com.xiaopeng.factory.model.factorytest.hardwaretest.gps;

import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.xiaopeng.commonfunc.utils.TimeUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.factory.model.factorytest.hardwaretest.gps.UDPClient;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.gps.GPSPresenter;
import com.xiaopeng.lib.utils.LogUtils;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class GPSModel implements GeocodeSearch.OnGeocodeSearchListener {
    public static final int SENSOR_TYPE_GPS_ACC = 1;
    public static final int SENSOR_TYPE_GPS_GYRO = 4;
    private static final String TAG = "GPSModel";
    private int currentMode;
    private SimpleDateFormat format;
    private GeocodeSearch geocoderSearch;
    private GPSLooperThread gpsLooperThread;
    private final Context mContext;
    private final List<GpsSatellite> mGPSCNRGpsSatellites;
    private GPSPresenter mGPSPresenter;
    private MyGpsStatusListener mGpsStatusListener;
    private final StringBuilder mLocBuilder;
    private Location mLocation;
    private MyLocationListener mLocationListener;
    private final LocationManager mLocationManager;
    private UDPClient mUDPClient;
    private final UDPClient.UDPClientListener mUDPClientListener;

    public GPSModel(GPSPresenter presenter) {
        this.currentMode = -1;
        this.mLocBuilder = new StringBuilder();
        this.mUDPClientListener = new UDPClient.UDPClientListener() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.gps.GPSModel.1
            @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.gps.UDPClient.UDPClientListener
            public void onSensorChanged(int sensorType, float[] values, long timestamp, float temperature) {
                GPSModel.this.mGPSPresenter.onSensorChanged(sensorType, values, timestamp, temperature);
            }
        };
        this.mContext = MyApplication.getContext();
        this.mGPSPresenter = presenter;
        this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.mLocationManager = (LocationManager) this.mContext.getSystemService(RequestParameters.SUBRESOURCE_LOCATION);
        this.geocoderSearch = new GeocodeSearch(this.mContext);
        this.geocoderSearch.setOnGeocodeSearchListener(this);
        this.currentMode = getCurrentGPSmode();
        setGPSmode(3);
        getCurrentGPSmode();
        this.mGPSCNRGpsSatellites = new ArrayList();
        this.mLocationListener = new MyLocationListener(this.mGPSPresenter);
        this.mLocationManager.requestLocationUpdates(GeocodeSearch.GPS, 1000L, 1.0f, this.mLocationListener);
        this.mGpsStatusListener = new MyGpsStatusListener(this.mGPSPresenter);
        this.mLocationManager.addGpsStatusListener(this.mGpsStatusListener);
        this.mUDPClient = UDPClient.getInstance();
    }

    public GPSModel() {
        this.currentMode = -1;
        this.mLocBuilder = new StringBuilder();
        this.mUDPClientListener = new UDPClient.UDPClientListener() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.gps.GPSModel.1
            @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.gps.UDPClient.UDPClientListener
            public void onSensorChanged(int sensorType, float[] values, long timestamp, float temperature) {
                GPSModel.this.mGPSPresenter.onSensorChanged(sensorType, values, timestamp, temperature);
            }
        };
        this.mContext = MyApplication.getContext();
        this.mLocationManager = (LocationManager) this.mContext.getSystemService(RequestParameters.SUBRESOURCE_LOCATION);
        this.currentMode = getCurrentGPSmode();
        this.mGPSCNRGpsSatellites = new ArrayList();
        if (this.gpsLooperThread == null) {
            this.gpsLooperThread = new GPSLooperThread();
            this.gpsLooperThread.start();
        }
    }

    public void setGPSEnable() {
        LogUtils.i(TAG, "setGPSEnable ");
        if (!this.mLocationManager.isProviderEnabled(GeocodeSearch.GPS) && hasGPSFeature() && this.currentMode != 3) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), "location_mode", 3);
        }
    }

    public boolean setGPSmode(int mode) {
        LogUtils.i(TAG, "setGPSmode mode = " + mode);
        if (mode != -1 && mode != getCurrentGPSmode()) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), "location_mode", mode);
            return true;
        }
        return false;
    }

    public String getGPSCNR() {
        int satellitesSize = this.mGPSCNRGpsSatellites.size();
        String CNRvalue = "" + satellitesSize;
        for (int i = 0; i < satellitesSize; i++) {
            CNRvalue = (CNRvalue + "," + this.mGPSCNRGpsSatellites.get(i).getPrn()) + "_" + this.mGPSCNRGpsSatellites.get(i).getSnr();
        }
        LogUtils.i(TAG, "getGPSCNR CNRvalue = " + CNRvalue);
        return CNRvalue;
    }

    public int getCurrentGPSmode() {
        int mode = 0;
        try {
            mode = Settings.Secure.getInt(this.mContext.getContentResolver(), "location_mode");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        LogUtils.i(TAG, "getCurrentGPSmode mode = " + mode);
        return mode;
    }

    public Location getLastKnownLocation() {
        Location location = this.mLocationManager.getLastKnownLocation(GeocodeSearch.GPS);
        return location;
    }

    public void startSearchLocation(Location location) {
        if (location != null) {
            this.mLocation = location;
            startSearchDetailAddr(location);
        }
    }

    public void destory() {
        LogUtils.i(TAG, "destory , currentMode = " + this.currentMode);
        if (this.currentMode != -1) {
            Settings.Secure.putInt(this.mContext.getContentResolver(), "location_mode", this.currentMode);
        }
        MyLocationListener myLocationListener = this.mLocationListener;
        if (myLocationListener != null) {
            this.mLocationManager.removeUpdates(myLocationListener);
        }
        GPSLooperThread gPSLooperThread = this.gpsLooperThread;
        if (gPSLooperThread != null) {
            gPSLooperThread.interrupt();
            this.gpsLooperThread = null;
        }
    }

    public boolean isGPSEnabled() {
        boolean isGpsEnabled = false;
        if (hasGPSFeature()) {
            isGpsEnabled = this.mLocationManager.isProviderEnabled(GeocodeSearch.GPS);
        }
        LogUtils.i(TAG, "isGPSEnabled = " + isGpsEnabled);
        return isGpsEnabled;
    }

    public boolean hasGPSFeature() {
        boolean withFeature = this.mContext.getPackageManager().hasSystemFeature("android.hardware.location.gps");
        LogUtils.i(TAG, "hasGPSFeature withFeature = " + withFeature);
        return withFeature;
    }

    private void startSearchDetailAddr(Location location) {
        LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200.0f, GeocodeSearch.GPS);
        this.geocoderSearch.getFromLocationAsyn(query);
    }

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(2);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        criteria.setBearingRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setPowerRequirement(1);
        return criteria;
    }

    public void openUDPClient() {
        this.mUDPClient.open();
    }

    public void closeUDPClient() {
        this.mUDPClient.close();
    }

    public void setUDPClientListener() {
        this.mUDPClient.setUDPClientListener(this.mUDPClientListener);
    }

    @Override // com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        this.mLocBuilder.setLength(0);
        Location location = this.mLocation;
        if (location != null) {
            location.getTime();
            String locationTime = TimeUtil.getDate("yyyy-MM-dd HH:mm:ss");
            StringBuilder sb = this.mLocBuilder;
            sb.append("经度: ");
            sb.append(this.mLocation.getLongitude());
            sb.append("\n");
            sb.append("纬度: ");
            sb.append(this.mLocation.getLatitude());
            sb.append("\n");
            sb.append("海拔高度: ");
            sb.append(this.mLocation.getAltitude());
            sb.append("\n");
            sb.append("时间: ");
            sb.append(locationTime);
            sb.append("\n");
        }
        if (i == 1000 && regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
            String addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress();
            if (!TextUtils.isEmpty(addressName)) {
                StringBuilder sb2 = this.mLocBuilder;
                sb2.append("详细地址: ");
                sb2.append(addressName);
            }
        }
        this.mGPSPresenter.updateLocation(this.mLocBuilder.toString());
    }

    @Override // com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class GPSLooperThread extends Thread {
        private GPSLooperThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            GPSModel gPSModel = GPSModel.this;
            gPSModel.mGpsStatusListener = new MyGpsStatusListener();
            GPSModel.this.mLocationManager.addGpsStatusListener(GPSModel.this.mGpsStatusListener);
            Looper.loop();
        }
    }

    /* loaded from: classes.dex */
    private final class MyGpsStatusListener implements GpsStatus.Listener {
        private WeakReference<GPSPresenter> mWeakReference;

        public MyGpsStatusListener(GPSPresenter presenter) {
            this.mWeakReference = new WeakReference<>(presenter);
        }

        public MyGpsStatusListener() {
        }

        @Override // android.location.GpsStatus.Listener
        public void onGpsStatusChanged(int event) {
            GpsStatus status = GPSModel.this.mLocationManager.getGpsStatus(null);
            updateGpsStatus(event, status);
        }

        private void updateGpsStatus(int event, GpsStatus status) {
            if (event == 4) {
                int maxSatellites = status.getMaxSatellites();
                Iterator<GpsSatellite> it = status.getSatellites().iterator();
                GPSModel.this.mGPSCNRGpsSatellites.clear();
                int count = 0;
                while (it.hasNext() && count <= maxSatellites) {
                    GpsSatellite s = it.next();
                    GPSModel.this.mGPSCNRGpsSatellites.add(s);
                    count++;
                }
                WeakReference<GPSPresenter> weakReference = this.mWeakReference;
                if (weakReference != null && weakReference.get() != null) {
                    this.mWeakReference.get().updateGPSStatus(count, GPSModel.this.mGPSCNRGpsSatellites);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class MyLocationListener implements LocationListener {
        private WeakReference<GPSPresenter> mWeakReference;

        public MyLocationListener(GPSPresenter presenter) {
            this.mWeakReference = new WeakReference<>(presenter);
        }

        public MyLocationListener() {
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            GPSModel.this.startSearchLocation(location);
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String provider) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String provider) {
            if (this.mWeakReference.get() != null) {
                this.mWeakReference.get().updateOnOffInfo();
                Location location = GPSModel.this.mLocationManager.getLastKnownLocation(provider);
                GPSModel.this.startSearchLocation(location);
            }
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
