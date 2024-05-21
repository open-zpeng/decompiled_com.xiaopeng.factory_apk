package com.xiaopeng.factory.model.factorytest.hardwaretest.bluetooth;

import com.xiaopeng.commonfunc.bean.factorytest.ScanDevice;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ScanDeviceLab {
    private static ScanDeviceLab itemLab;
    private final List<ScanDevice> items = new ArrayList();

    private ScanDeviceLab() {
    }

    public static ScanDeviceLab getDeviceItemLab() {
        if (itemLab == null) {
            itemLab = new ScanDeviceLab();
        }
        return itemLab;
    }

    public void addItems(ScanDevice item) {
        this.items.add(item);
    }

    public List<ScanDevice> getItems() {
        return this.items;
    }

    public ScanDevice getAvailableDeviceItem(String address) {
        ScanDevice availableDeviceItem = null;
        for (ScanDevice item : this.items) {
            if (item.getAddress().equals(address)) {
                availableDeviceItem = item;
            }
        }
        return availableDeviceItem;
    }

    public void deleteDeviceItem(String address) {
        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i).getAddress().contains(address)) {
                this.items.remove(i);
            }
        }
    }

    public void deleteAll() {
        this.items.clear();
    }
}
