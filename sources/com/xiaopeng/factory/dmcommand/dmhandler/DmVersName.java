package com.xiaopeng.factory.dmcommand.dmhandler;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.text.TextUtils;
import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.FactoryTestPresenter;
import com.xiaopeng.factory.presenter.navi.NaviPresenter;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.SystemPropertyUtil;
import com.xiaopeng.xmlconfig.Support;
/* loaded from: classes.dex */
public class DmVersName extends DmCommandHandler {
    private final NaviPresenter mNaviPresenter;

    public DmVersName(Context context, DmResponseWriter dmResponseWriter) {
        super(context);
        this.CMD_NAME = DmUtil.VersName.CMD_NAME;
        this.CLASS_NAME = "DmVersName";
        this.mNaviPresenter = new NaviPresenter(dmResponseWriter);
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public synchronized byte[] handleCommand(byte[] argu) {
        byte[] resData;
        if (checkArgu(argu, new byte[]{1, 0})) {
            String softwareId = SystemPropertyUtil.getSoftwareId();
            String str = this.CLASS_NAME;
            LogUtils.i(str, "softwareId = " + softwareId);
            if (softwareId != null) {
                String softwareId2 = softwareId.substring(0, softwareId.indexOf("_REV"));
                String str2 = this.CLASS_NAME;
                LogUtils.i(str2, "after substring softwareId = " + softwareId2);
                resData = responseWithValue(argu, softwareId2.getBytes());
            } else {
                resData = responseNG(argu);
            }
        } else {
            byte[] resData2 = {1, 3};
            if (checkArgu(argu, resData2)) {
                String mapVersion = this.mNaviPresenter.readVmapVer();
                String str3 = this.CLASS_NAME;
                LogUtils.i(str3, "mapVersion = " + mapVersion);
                if (mapVersion != null) {
                    resData = responseWithValue(argu, mapVersion.getBytes());
                } else {
                    resData = responseNG(argu);
                }
            } else {
                byte[] resData3 = {1, 4};
                if (checkArgu(argu, resData3)) {
                    String hwVersion = SystemPropertyUtil.getHwVersion();
                    String str4 = this.CLASS_NAME;
                    LogUtils.i(str4, "hwVersion = " + hwVersion);
                    if (hwVersion != null) {
                        resData = responseWithValue(argu, hwVersion.getBytes());
                    } else {
                        resData = responseNG(argu);
                    }
                } else {
                    byte[] resData4 = {1, 5};
                    if (checkArgu(argu, resData4)) {
                        String dtsVer = new FactoryTestPresenter().getDtsVer();
                        String str5 = this.CLASS_NAME;
                        LogUtils.i(str5, "dtsVer = " + dtsVer);
                        if (dtsVer != null) {
                            resData = responseWithValue(argu, dtsVer.getBytes());
                        } else {
                            resData = responseNG(argu);
                        }
                    } else {
                        byte[] resData5 = {1, 6};
                        if (checkArgu(argu, resData5)) {
                            boolean sha1Result = new FactoryTestPresenter().checkRomSha1Sum();
                            String str6 = this.CLASS_NAME;
                            LogUtils.i(str6, "sha1Result = " + sha1Result);
                            resData = sha1Result ? responseOK(argu) : responseNG(argu);
                        } else {
                            byte[] resData6 = {1, 7};
                            if (checkArgu(argu, resData6)) {
                                String socEepromVersion = com.xiaopeng.commonfunc.utils.SystemPropertyUtil.getSocEepromVersion();
                                String str7 = this.CLASS_NAME;
                                LogUtils.i(str7, "socEepromVersion = " + socEepromVersion);
                                if (socEepromVersion != null) {
                                    resData = responseWithValue(argu, socEepromVersion.getBytes());
                                } else {
                                    resData = responseNG(argu);
                                }
                            } else {
                                byte[] resData7 = {1, 8};
                                if (checkArgu(argu, resData7)) {
                                    String md5 = FileUtil.getFileMd5(Constant.PATH_ROM_SHA1SUM_FILE_FLASH);
                                    String str8 = this.CLASS_NAME;
                                    LogUtils.d(str8, "PATH_BLOCK_SHA1_SUM = " + md5);
                                    resData = md5 == null ? responseNG(argu) : responseWithValue(argu, md5.getBytes());
                                } else if (checkArgu(argu, new byte[]{1, 10})) {
                                    if (Support.Case.getEnabled(Support.Case.USB_BLUETOOTH_VER_INFO)) {
                                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                        String UsbBtVer = bluetoothAdapter.getModuleVersion();
                                        resData = TextUtils.isEmpty(UsbBtVer) ? responseNG(argu) : responseWithValue(argu, DataHelp.strToByteArray(UsbBtVer));
                                    } else {
                                        resData = responseNA(argu);
                                    }
                                } else {
                                    resData = responseNA(argu);
                                }
                            }
                        }
                    }
                }
            }
        }
        return resData;
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public void destroy() {
        super.destroy();
    }
}
