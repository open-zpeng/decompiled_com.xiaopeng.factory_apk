package com.xpeng.upso.utils.cert;
/* loaded from: classes2.dex */
public class CertInfo {
    private String cert;
    private Integer index;
    private String key;

    protected boolean canEqual(final Object other) {
        return other instanceof CertInfo;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CertInfo) {
            CertInfo other = (CertInfo) o;
            if (other.canEqual(this)) {
                Object this$cert = getCert();
                Object other$cert = other.getCert();
                if (this$cert != null ? this$cert.equals(other$cert) : other$cert == null) {
                    Object this$key = getKey();
                    Object other$key = other.getKey();
                    if (this$key != null ? this$key.equals(other$key) : other$key == null) {
                        Object this$index = getIndex();
                        Object other$index = other.getIndex();
                        return this$index != null ? this$index.equals(other$index) : other$index == null;
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
        Object $cert = getCert();
        int result = (1 * 59) + ($cert == null ? 43 : $cert.hashCode());
        Object $key = getKey();
        int result2 = (result * 59) + ($key == null ? 43 : $key.hashCode());
        Object $index = getIndex();
        return (result2 * 59) + ($index != null ? $index.hashCode() : 43);
    }

    public void setCert(final String cert) {
        this.cert = cert;
    }

    public void setIndex(final Integer index) {
        this.index = index;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String toString() {
        return "CertInfo(cert=" + getCert() + ", key=" + getKey() + ", index=" + getIndex() + ")";
    }

    public String getCert() {
        return this.cert;
    }

    public String getKey() {
        return this.key;
    }

    public Integer getIndex() {
        return this.index;
    }
}
