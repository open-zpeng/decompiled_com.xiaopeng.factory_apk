package com.xiaopeng.factory.presenter.factorytest.hardwaretest.gps;

import android.location.GpsSatellite;
import android.location.Location;
import com.xiaopeng.factory.model.factorytest.hardwaretest.gps.GPSModel;
import com.xiaopeng.factory.view.factorytest.hardwaretest.gps.IGPSView;
import java.util.List;
/* loaded from: classes2.dex */
public class GPSPresenter {
    private final GPSModel mGPSModel;
    private IGPSView mGPSView;

    public GPSPresenter(IGPSView view) {
        this.mGPSModel = new GPSModel(this);
        this.mGPSView = view;
    }

    public GPSPresenter() {
        this.mGPSModel = new GPSModel();
    }

    public void setGPSEnable() {
        this.mGPSModel.setGPSEnable();
    }

    public boolean setGPSmode(int mode) {
        return this.mGPSModel.setGPSmode(mode);
    }

    public int getCurrentGPSmode() {
        return this.mGPSModel.getCurrentGPSmode();
    }

    public String getGPSCNR() {
        return this.mGPSModel.getGPSCNR();
    }

    public Location getLastKnownLocation() {
        return this.mGPSModel.getLastKnownLocation();
    }

    public void startSearchLocation(Location location) {
        this.mGPSModel.startSearchLocation(location);
    }

    public boolean isGPSEnabled() {
        return this.mGPSModel.isGPSEnabled();
    }

    public boolean hasGPSFeature() {
        return this.mGPSModel.hasGPSFeature();
    }

    public void updateOnOffInfo() {
        this.mGPSView.updateOnOffInfo();
    }

    public void updateLocation(String location) {
        this.mGPSView.updateLocation(location);
    }

    public void destory() {
        this.mGPSModel.destory();
    }

    public void updateGPSStatus(int counts, List<GpsSatellite> gpsSatellites) {
        this.mGPSView.updateGPSStatus(counts, gpsSatellites);
    }

    public void openUDPClient() {
        this.mGPSModel.openUDPClient();
    }

    public void closeUDPClient() {
        this.mGPSModel.closeUDPClient();
    }

    public void setUDPClientListener() {
        this.mGPSModel.setUDPClientListener();
    }

    public void onSensorChanged(int sensorType, float[] values, long timestamp, float temperature) {
        this.mGPSView.onSensorChanged(sensorType, values, timestamp, temperature);
    }
}
