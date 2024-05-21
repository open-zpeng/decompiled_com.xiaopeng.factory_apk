package com.xpeng.upso.proxy;

import androidx.annotation.Keep;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.XpengPsoClientType;
import com.xpeng.upso.XpengSecretType;
@Keep
/* loaded from: classes2.dex */
public class ProxyParamWrapper {
    private XpengCarModel carModel;
    private XpengCarChipModel chipModel;
    private XpengPsoClientType clientType;
    private int proxyServerPort = ProxyConstants.SERVER_PORT;
    private XpengSecretType secretType;

    public ProxyParamWrapper() {
    }

    public ProxyParamWrapper(XpengCarModel carModel, XpengCarChipModel chipModel, XpengPsoClientType clientType, XpengSecretType secretType) {
        this.carModel = carModel;
        this.chipModel = chipModel;
        this.clientType = clientType;
        this.secretType = secretType;
    }

    public XpengCarModel getCarModel() {
        return this.carModel;
    }

    public void setCarModel(XpengCarModel carModel) {
        this.carModel = carModel;
    }

    public XpengCarChipModel getChipModel() {
        return this.chipModel;
    }

    public void setChipModel(XpengCarChipModel chipModel) {
        this.chipModel = chipModel;
    }

    public XpengPsoClientType getClientType() {
        return this.clientType;
    }

    public void setClientType(XpengPsoClientType clientType) {
        this.clientType = clientType;
    }

    public XpengSecretType getSecretType() {
        return this.secretType;
    }

    public void setSecretType(XpengSecretType secretType) {
        this.secretType = secretType;
    }

    public void setProxyServerPort(int port) {
        this.proxyServerPort = port;
    }

    public int getProxyServerPort() {
        return this.proxyServerPort;
    }
}
