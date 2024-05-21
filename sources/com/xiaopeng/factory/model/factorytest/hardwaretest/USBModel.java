package com.xiaopeng.factory.model.factorytest.hardwaretest;

import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmlconfig.Support;
/* loaded from: classes.dex */
public class USBModel {
    private static final String DEVICE_PRODUCT_NAME = "product";
    public static final int INDEX_USB_HUB_1_1 = 1;
    public static final int INDEX_USB_HUB_1_2 = 2;
    public static final int INDEX_USB_HUB_1_3 = 3;
    public static final int INDEX_USB_HUB_2_1 = 4;
    private static final String TAG = "USBModel";
    public static final int TBOX_PRODUCT_ID = 1077;
    public static final int TBOX_VENDOR_ID = 11388;
    private static final String BASE_DIR = Support.Path.getFilePath(Support.Path.USB_DEVICES);
    private static final String USB_HUB_1_1 = BASE_DIR + "1-1.3/";
    private static final String USB_HUB_1_2 = BASE_DIR + "1-1.4/";
    private static final String USB_HUB_1_3 = BASE_DIR + "1-1.2/";
    private static final String USB_HUB_2_1 = BASE_DIR + "3-1/";

    public String getDeviceProductName(int usbIndex) {
        String productName = null;
        String path = getPath(usbIndex) + DEVICE_PRODUCT_NAME;
        if (path != null) {
            productName = FileUtil.read(path);
        }
        LogUtils.i(TAG, "getDeviceProductName productName(" + usbIndex + ") " + productName);
        return productName;
    }

    public boolean isInserted(int usbIndex) {
        String path = getPath(usbIndex);
        boolean isinserted = FileUtil.isExistFilePath(path);
        LogUtils.i(TAG, path + "isInserted " + isinserted);
        return isinserted;
    }

    private String getPath(int usbIndex) {
        if (usbIndex == 1) {
            String path = USB_HUB_1_1;
            return path;
        } else if (usbIndex == 2) {
            String path2 = USB_HUB_1_2;
            return path2;
        } else if (usbIndex == 3) {
            String path3 = USB_HUB_1_3;
            return path3;
        } else if (usbIndex != 4) {
            return null;
        } else {
            String path4 = USB_HUB_2_1;
            return path4;
        }
    }

    public boolean isTboxAttached() {
        return FileUtil.isUsbDeviceAttached(MyApplication.getContext(), 11388, 1077);
    }
}
