package ch.ethz.ssh2;

import ch.ethz.ssh2.sftp.ErrorCodes;
import java.io.IOException;
/* loaded from: classes.dex */
public class SFTPException extends IOException {
    private static final long serialVersionUID = 578654644222421811L;
    private final int sftpErrorCode;
    private final String sftpErrorMessage;

    private static String constructMessage(String s, int errorCode) {
        String[] detail = ErrorCodes.getDescription(errorCode);
        if (detail == null) {
            StringBuffer stringBuffer = new StringBuffer(String.valueOf(s));
            stringBuffer.append(" (UNKNOW SFTP ERROR CODE)");
            return stringBuffer.toString();
        }
        StringBuffer stringBuffer2 = new StringBuffer(String.valueOf(s));
        stringBuffer2.append(" (");
        stringBuffer2.append(detail[0]);
        stringBuffer2.append(": ");
        stringBuffer2.append(detail[1]);
        stringBuffer2.append(")");
        return stringBuffer2.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SFTPException(String msg, int errorCode) {
        super(constructMessage(msg, errorCode));
        this.sftpErrorMessage = msg;
        this.sftpErrorCode = errorCode;
    }

    public String getServerErrorMessage() {
        return this.sftpErrorMessage;
    }

    public int getServerErrorCode() {
        return this.sftpErrorCode;
    }

    public String getServerErrorCodeSymbol() {
        String[] detail = ErrorCodes.getDescription(this.sftpErrorCode);
        if (detail == null) {
            StringBuffer stringBuffer = new StringBuffer("UNKNOW SFTP ERROR CODE ");
            stringBuffer.append(this.sftpErrorCode);
            return stringBuffer.toString();
        }
        return detail[0];
    }

    public String getServerErrorCodeVerbose() {
        String[] detail = ErrorCodes.getDescription(this.sftpErrorCode);
        if (detail == null) {
            StringBuffer stringBuffer = new StringBuffer("The error code ");
            stringBuffer.append(this.sftpErrorCode);
            stringBuffer.append(" is unknown.");
            return stringBuffer.toString();
        }
        return detail[1];
    }
}
