package com.xpeng.upso.utils.cert;
/* loaded from: classes2.dex */
public class CertResp {
    private String pkcs12;

    protected boolean canEqual(final Object other) {
        return other instanceof CertResp;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof CertResp) {
            CertResp other = (CertResp) o;
            if (other.canEqual(this)) {
                Object this$pkcs12 = getPkcs12();
                Object other$pkcs12 = other.getPkcs12();
                return this$pkcs12 != null ? this$pkcs12.equals(other$pkcs12) : other$pkcs12 == null;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        Object $pkcs12 = getPkcs12();
        int result = (1 * 59) + ($pkcs12 == null ? 43 : $pkcs12.hashCode());
        return result;
    }

    public void setPkcs12(final String pkcs12) {
        this.pkcs12 = pkcs12;
    }

    public String toString() {
        return "CertResp(pkcs12=" + getPkcs12() + ")";
    }

    public String getPkcs12() {
        return this.pkcs12;
    }
}
