package ch.ethz.ssh2;
/* loaded from: classes.dex */
public class SFTPv3FileAttributes {
    public Long size = null;
    public Integer uid = null;
    public Integer gid = null;
    public Integer permissions = null;
    public Integer atime = null;
    public Integer mtime = null;

    public boolean isDirectory() {
        Integer num = this.permissions;
        return (num == null || (num.intValue() & 16384) == 0) ? false : true;
    }

    public boolean isRegularFile() {
        Integer num = this.permissions;
        return (num == null || (num.intValue() & 32768) == 0) ? false : true;
    }

    public boolean isSymlink() {
        Integer num = this.permissions;
        return (num == null || (num.intValue() & 40960) == 0) ? false : true;
    }

    public String getOctalPermissions() {
        Integer num = this.permissions;
        if (num == null) {
            return null;
        }
        String res = Integer.toString(num.intValue() & 65535, 8);
        StringBuffer sb = new StringBuffer();
        for (int leadingZeros = 7 - res.length(); leadingZeros > 0; leadingZeros--) {
            sb.append('0');
        }
        sb.append(res);
        return sb.toString();
    }
}
