package com.xpeng.upso.utils.cert;

import java.util.Arrays;
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

    protected boolean canEqual(final Object other) {
        return other instanceof TeeEncrpytedContent;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof TeeEncrpytedContent) {
            TeeEncrpytedContent other = (TeeEncrpytedContent) o;
            if (other.canEqual(this)) {
                Object this$magicNumber = getMagicNumber();
                Object other$magicNumber = other.getMagicNumber();
                if (this$magicNumber != null ? this$magicNumber.equals(other$magicNumber) : other$magicNumber == null) {
                    Object this$version = getVersion();
                    Object other$version = other.getVersion();
                    if (this$version != null ? this$version.equals(other$version) : other$version == null) {
                        Object this$keyIndex = getKeyIndex();
                        Object other$keyIndex = other.getKeyIndex();
                        if (this$keyIndex != null ? this$keyIndex.equals(other$keyIndex) : other$keyIndex == null) {
                            Object this$deviceId = getDeviceId();
                            Object other$deviceId = other.getDeviceId();
                            if (this$deviceId != null ? this$deviceId.equals(other$deviceId) : other$deviceId == null) {
                                Object this$nonceLength = getNonceLength();
                                Object other$nonceLength = other.getNonceLength();
                                if (this$nonceLength != null ? this$nonceLength.equals(other$nonceLength) : other$nonceLength == null) {
                                    Object this$timestamp = getTimestamp();
                                    Object other$timestamp = other.getTimestamp();
                                    if (this$timestamp != null ? this$timestamp.equals(other$timestamp) : other$timestamp == null) {
                                        return Arrays.equals(getIv(), other.getIv()) && Arrays.equals(getContent(), other.getContent());
                                    }
                                    return false;
                                }
                                return false;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        Object $magicNumber = getMagicNumber();
        int result = (1 * 59) + ($magicNumber == null ? 43 : $magicNumber.hashCode());
        Object $version = getVersion();
        int result2 = (result * 59) + ($version == null ? 43 : $version.hashCode());
        Object $keyIndex = getKeyIndex();
        int result3 = (result2 * 59) + ($keyIndex == null ? 43 : $keyIndex.hashCode());
        Object $deviceId = getDeviceId();
        int result4 = (result3 * 59) + ($deviceId == null ? 43 : $deviceId.hashCode());
        Object $nonceLength = getNonceLength();
        int result5 = (result4 * 59) + ($nonceLength == null ? 43 : $nonceLength.hashCode());
        Object $timestamp = getTimestamp();
        return (((((result5 * 59) + ($timestamp != null ? $timestamp.hashCode() : 43)) * 59) + Arrays.hashCode(getIv())) * 59) + Arrays.hashCode(getContent());
    }

    public void setContent(final byte[] content) {
        this.content = content;
    }

    public void setDeviceId(final String deviceId) {
        this.deviceId = deviceId;
    }

    public void setIv(final byte[] iv) {
        this.iv = iv;
    }

    public void setKeyIndex(final Integer keyIndex) {
        this.keyIndex = keyIndex;
    }

    public void setMagicNumber(final String magicNumber) {
        this.magicNumber = magicNumber;
    }

    public void setNonceLength(final Integer nonceLength) {
        this.nonceLength = nonceLength;
    }

    public void setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

    public String toString() {
        return "TeeEncrpytedContent(magicNumber=" + getMagicNumber() + ", version=" + getVersion() + ", keyIndex=" + getKeyIndex() + ", deviceId=" + getDeviceId() + ", nonceLength=" + getNonceLength() + ", timestamp=" + getTimestamp() + ", iv=" + Arrays.toString(getIv()) + ", content=" + Arrays.toString(getContent()) + ")";
    }

    public String getMagicNumber() {
        return this.magicNumber;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Integer getKeyIndex() {
        return this.keyIndex;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public Integer getNonceLength() {
        return this.nonceLength;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public byte[] getIv() {
        return this.iv;
    }

    public byte[] getContent() {
        return this.content;
    }
}
