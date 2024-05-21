package com.xpeng.upso.proxy;

import androidx.annotation.Keep;
import cn.hutool.core.text.CharPool;
import java.util.Arrays;
@Keep
/* loaded from: classes2.dex */
public class TeeEncrpytedContent {
    private byte[] content;
    private String deviceId;
    private byte[] iv;
    private Integer keyIndex;
    private String magicNumber;
    private Integer nonceLength;
    private Long timestamp;
    private Integer version;

    public String getMagicNumber() {
        return this.magicNumber;
    }

    public void setMagicNumber(String magicNumber) {
        this.magicNumber = magicNumber;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getKeyIndex() {
        return this.keyIndex;
    }

    public void setKeyIndex(Integer keyIndex) {
        this.keyIndex = keyIndex;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getNonceLength() {
        return this.nonceLength;
    }

    public void setNonceLength(Integer nonceLength) {
        this.nonceLength = nonceLength;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getIv() {
        return this.iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String toString() {
        return "TeeEncrpytedContent{magicNumber='" + this.magicNumber + CharPool.SINGLE_QUOTE + ", version=" + this.version + ", keyIndex=" + this.keyIndex + ", deviceId='" + this.deviceId + CharPool.SINGLE_QUOTE + ", nonceLength=" + this.nonceLength + ", timestamp=" + this.timestamp + ", iv=" + Arrays.toString(this.iv) + ", content=" + Arrays.toString(this.content) + '}';
    }
}
