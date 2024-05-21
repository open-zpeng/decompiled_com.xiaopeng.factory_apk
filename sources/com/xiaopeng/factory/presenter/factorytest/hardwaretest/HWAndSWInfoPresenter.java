package com.xiaopeng.factory.presenter.factorytest.hardwaretest;

import android.content.Context;
import android.graphics.Bitmap;
import com.xiaopeng.commonfunc.callback.HWAndSWInfoCallBack;
import com.xiaopeng.commonfunc.model.factorytest.hardwaretest.HWAndSWInfoModel;
import com.xiaopeng.commonfunc.model.factorytest.hardwaretest.IHWAndSWInfoModel;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.view.factorytest.hardwaretest.IHWAndSWView;
import com.xiaopeng.lib.http.ICallback;
/* loaded from: classes2.dex */
public class HWAndSWInfoPresenter implements IHWAndSWInfoPresenter {
    private static final String KEY_SN = "SN:";
    private DmResponseWriter mDmResponseWriter;
    private HWAndSWInfoCallBack mHWAndSWInfoCallBack;
    private final IHWAndSWInfoModel mIHWAndSWInfoModel;
    private IHWAndSWView mView;

    public HWAndSWInfoPresenter(IHWAndSWView view) {
        this.mHWAndSWInfoCallBack = new HWAndSWInfoCallBack() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.HWAndSWInfoPresenter.1
            @Override // com.xiaopeng.commonfunc.callback.HWAndSWInfoCallBack
            public void updateDeviceCodeBitmap(Bitmap bitmap) {
                HWAndSWInfoPresenter.this.setDeviceCodeBitmap(bitmap);
            }

            @Override // com.xiaopeng.commonfunc.callback.HWAndSWInfoCallBack
            public void updateDeviceNumBitmap(Bitmap bitmap) {
                HWAndSWInfoPresenter.this.setDeviceNumBitmap(bitmap);
            }

            @Override // com.xiaopeng.commonfunc.callback.HWAndSWInfoCallBack
            public void updateCarCodeBitmap(Bitmap bitmap) {
                HWAndSWInfoPresenter.this.setCarCodeBitmap(bitmap);
            }
        };
        this.mIHWAndSWInfoModel = new HWAndSWInfoModel(this.mHWAndSWInfoCallBack);
        this.mView = view;
    }

    public HWAndSWInfoPresenter(DmResponseWriter responseWriter) {
        this.mHWAndSWInfoCallBack = new HWAndSWInfoCallBack() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.HWAndSWInfoPresenter.1
            @Override // com.xiaopeng.commonfunc.callback.HWAndSWInfoCallBack
            public void updateDeviceCodeBitmap(Bitmap bitmap) {
                HWAndSWInfoPresenter.this.setDeviceCodeBitmap(bitmap);
            }

            @Override // com.xiaopeng.commonfunc.callback.HWAndSWInfoCallBack
            public void updateDeviceNumBitmap(Bitmap bitmap) {
                HWAndSWInfoPresenter.this.setDeviceNumBitmap(bitmap);
            }

            @Override // com.xiaopeng.commonfunc.callback.HWAndSWInfoCallBack
            public void updateCarCodeBitmap(Bitmap bitmap) {
                HWAndSWInfoPresenter.this.setCarCodeBitmap(bitmap);
            }
        };
        this.mIHWAndSWInfoModel = new HWAndSWInfoModel();
        this.mDmResponseWriter = responseWriter;
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public void init(Context context) {
        this.mIHWAndSWInfoModel.init(context);
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public String getHWInfo() {
        return this.mIHWAndSWInfoModel.getHWInfo();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public String getSWInfo() {
        return this.mIHWAndSWInfoModel.getSWInfo();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public String getDeviceCode() {
        return this.mIHWAndSWInfoModel.getDeviceCode();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public void generateCodeBitmap(int width) {
        this.mIHWAndSWInfoModel.generateCodeBitmap(width);
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public void generateNumBitmap(int width) {
        this.mIHWAndSWInfoModel.generateNumBitmap(width);
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public void generateCarCodeBitmap(int width) {
        this.mIHWAndSWInfoModel.generateCarCodeBitmap(width);
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public void getPsuVersion() {
        this.mIHWAndSWInfoModel.getPsuVersion();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public String getVehicleId() {
        return this.mIHWAndSWInfoModel.getVehicleId();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public void setDeviceCodeBitmap(Bitmap bitmap) {
        IHWAndSWView iHWAndSWView = this.mView;
        if (iHWAndSWView != null) {
            iHWAndSWView.setDeviceCodeBitmap(bitmap);
        }
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public void setDeviceNumBitmap(Bitmap bitmap) {
        IHWAndSWView iHWAndSWView = this.mView;
        if (iHWAndSWView != null) {
            iHWAndSWView.setDeviceNumBitmap(bitmap);
        }
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public void setCarCodeBitmap(Bitmap bitmap) {
        IHWAndSWView iHWAndSWView = this.mView;
        if (iHWAndSWView != null) {
            iHWAndSWView.setCarCodeBitmap(bitmap);
        }
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public void setPsuVersionTextView(String str) {
        IHWAndSWView iHWAndSWView = this.mView;
        if (iHWAndSWView != null) {
            iHWAndSWView.setPsuVersionTextView(str);
        }
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public void requestCarCode() {
        this.mIHWAndSWInfoModel.requestCarCode(new ICallback<String, String>() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.HWAndSWInfoPresenter.2
            @Override // com.xiaopeng.lib.http.ICallback
            public void onSuccess(String s) {
                String buf = s.substring(s.indexOf(HWAndSWInfoPresenter.KEY_SN) + HWAndSWInfoPresenter.KEY_SN.length(), s.indexOf("\n", s.indexOf(HWAndSWInfoPresenter.KEY_SN)));
                if (HWAndSWInfoPresenter.this.mDmResponseWriter != null) {
                    HWAndSWInfoPresenter.this.mDmResponseWriter.write(DmUtil.responseWithValue(DmUtil.HwParam.CMD_NAME, DmUtil.ARGU_02_00, DataHelp.strToByteArray(buf)));
                }
            }

            @Override // com.xiaopeng.lib.http.ICallback
            public void onError(String s) {
                if (HWAndSWInfoPresenter.this.mDmResponseWriter != null) {
                    HWAndSWInfoPresenter.this.mDmResponseWriter.write(DmUtil.responseNG(DmUtil.HwParam.CMD_NAME, DmUtil.ARGU_02_00));
                }
            }
        });
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.IHWAndSWInfoPresenter
    public void onDestroy() {
        this.mView = null;
        this.mIHWAndSWInfoModel.onDestroy();
    }
}
