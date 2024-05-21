package com.xiaopeng.factory.presenter.navi;

import com.xiaopeng.commonfunc.callback.NaviCallback;
import com.xiaopeng.commonfunc.model.NaviModel;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.factory.R;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.view.navi.IUuidView;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xui.app.XToast;
/* loaded from: classes2.dex */
public class NaviPresenter {
    private static final String TAG = "NaviPresenter";
    private DmResponseWriter mDmResponseWriter;
    private NaviCallback mNaviCallback = new NaviCallback() { // from class: com.xiaopeng.factory.presenter.navi.NaviPresenter.1
        @Override // com.xiaopeng.commonfunc.callback.NaviCallback
        public void updateUuid() {
            NaviPresenter naviPresenter = NaviPresenter.this;
            naviPresenter.responseDm(naviPresenter.mNaviModel.isUuidValid(NaviPresenter.this.mNaviModel.readUuid()));
            NaviPresenter.this.updateUuidView();
        }
    };
    private NaviModel mNaviModel = new NaviModel(this.mNaviCallback, MyApplication.getContext());
    private IUuidView mUuidView;

    public NaviPresenter(IUuidView uuidView) {
        this.mUuidView = uuidView;
    }

    public NaviPresenter(DmResponseWriter writer) {
        this.mDmResponseWriter = writer;
    }

    public void genUuid() {
        LogUtils.i(TAG, "genUuid");
        if (!this.mNaviModel.isNeedUuid()) {
            XToast.showLong((int) R.string.tips_unsupport_get_uuid);
            responseDm(false);
            return;
        }
        this.mNaviModel.genUuid();
    }

    public String readUuid() {
        return this.mNaviModel.readUuid();
    }

    public String readVmapVer() {
        return this.mNaviModel.readVmapVer();
    }

    public String readVmapVendor() {
        return this.mNaviModel.readVmapVendor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void responseDm(boolean result) {
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(result ? DmUtil.responseOK(DmUtil.NaviTest.CMD_NAME, DmUtil.ARGU_00_00) : DmUtil.responseNG(DmUtil.NaviTest.CMD_NAME, DmUtil.ARGU_00_00));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUuidView() {
        IUuidView iUuidView = this.mUuidView;
        if (iUuidView != null) {
            iUuidView.updateUuid();
        }
    }
}
