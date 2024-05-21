package ch.ethz.ssh2.sftp;
/* loaded from: classes.dex */
public class ErrorCodes {
    public static final int SSH_FX_BAD_MESSAGE = 5;
    public static final int SSH_FX_BYTE_RANGE_LOCK_CONFLICT = 25;
    public static final int SSH_FX_BYTE_RANGE_LOCK_REFUSED = 26;
    public static final int SSH_FX_CANNOT_DELETE = 22;
    public static final int SSH_FX_CONNECTION_LOST = 7;
    public static final int SSH_FX_DELETE_PENDING = 27;
    public static final int SSH_FX_DIR_NOT_EMPTY = 18;
    public static final int SSH_FX_EOF = 1;
    public static final int SSH_FX_FAILURE = 4;
    public static final int SSH_FX_FILE_ALREADY_EXISTS = 11;
    public static final int SSH_FX_FILE_CORRUPT = 28;
    public static final int SSH_FX_FILE_IS_A_DIRECTORY = 24;
    public static final int SSH_FX_GROUP_INVALID = 30;
    public static final int SSH_FX_INVALID_FILENAME = 20;
    public static final int SSH_FX_INVALID_HANDLE = 9;
    public static final int SSH_FX_INVALID_PARAMETER = 23;
    public static final int SSH_FX_LINK_LOOP = 21;
    public static final int SSH_FX_LOCK_CONFLICT = 17;
    public static final int SSH_FX_NOT_A_DIRECTORY = 19;
    public static final int SSH_FX_NO_CONNECTION = 6;
    public static final int SSH_FX_NO_MATCHING_BYTE_RANGE_LOCK = 31;
    public static final int SSH_FX_NO_MEDIA = 13;
    public static final int SSH_FX_NO_SPACE_ON_FILESYSTEM = 14;
    public static final int SSH_FX_NO_SUCH_FILE = 2;
    public static final int SSH_FX_NO_SUCH_PATH = 10;
    public static final int SSH_FX_OK = 0;
    public static final int SSH_FX_OP_UNSUPPORTED = 8;
    public static final int SSH_FX_OWNER_INVALID = 29;
    public static final int SSH_FX_PERMISSION_DENIED = 3;
    public static final int SSH_FX_QUOTA_EXCEEDED = 15;
    public static final int SSH_FX_UNKNOWN_PRINCIPAL = 16;
    public static final int SSH_FX_WRITE_PROTECT = 12;
    private static final String[][] messages = {new String[]{"SSH_FX_OK", "Indicates successful completion of the operation."}, new String[]{"SSH_FX_EOF", "An attempt to read past the end-of-file was made; or, there are no more directory entries to return."}, new String[]{"SSH_FX_NO_SUCH_FILE", "A reference was made to a file which does not exist."}, new String[]{"SSH_FX_PERMISSION_DENIED", "The user does not have sufficient permissions to perform the operation."}, new String[]{"SSH_FX_FAILURE", "An error occurred, but no specific error code exists to describe the failure."}, new String[]{"SSH_FX_BAD_MESSAGE", "A badly formatted packet or other SFTP protocol incompatibility was detected."}, new String[]{"SSH_FX_NO_CONNECTION", "There is no connection to the server."}, new String[]{"SSH_FX_CONNECTION_LOST", "The connection to the server was lost."}, new String[]{"SSH_FX_OP_UNSUPPORTED", "An attempted operation could not be completed by the server because the server does not support the operation."}, new String[]{"SSH_FX_INVALID_HANDLE", "The handle value was invalid."}, new String[]{"SSH_FX_NO_SUCH_PATH", "The file path does not exist or is invalid."}, new String[]{"SSH_FX_FILE_ALREADY_EXISTS", "The file already exists."}, new String[]{"SSH_FX_WRITE_PROTECT", "The file is on read-only media, or the media is write protected."}, new String[]{"SSH_FX_NO_MEDIA", "The requested operation cannot be completed because there is no media available in the drive."}, new String[]{"SSH_FX_NO_SPACE_ON_FILESYSTEM", "The requested operation cannot be completed because there is insufficient free space on the filesystem."}, new String[]{"SSH_FX_QUOTA_EXCEEDED", "The operation cannot be completed because it would exceed the user's storage quota."}, new String[]{"SSH_FX_UNKNOWN_PRINCIPAL", "A principal referenced by the request (either the 'owner', 'group', or 'who' field of an ACL), was unknown. The error specific data contains the problematic names."}, new String[]{"SSH_FX_LOCK_CONFLICT", "The file could not be opened because it is locked by another process."}, new String[]{"SSH_FX_DIR_NOT_EMPTY", "The directory is not empty."}, new String[]{"SSH_FX_NOT_A_DIRECTORY", "The specified file is not a directory."}, new String[]{"SSH_FX_INVALID_FILENAME", "The filename is not valid."}, new String[]{"SSH_FX_LINK_LOOP", "Too many symbolic links encountered or, an SSH_FXF_NOFOLLOW open encountered a symbolic link as the final component."}, new String[]{"SSH_FX_CANNOT_DELETE", "The file cannot be deleted. One possible reason is that the advisory READONLY attribute-bit is set."}, new String[]{"SSH_FX_INVALID_PARAMETER", "One of the parameters was out of range, or the parameters specified cannot be used together."}, new String[]{"SSH_FX_FILE_IS_A_DIRECTORY", "The specified file was a directory in a context where a directory cannot be used."}, new String[]{"SSH_FX_BYTE_RANGE_LOCK_CONFLICT", " A read or write operation failed because another process's mandatory byte-range lock overlaps with the request."}, new String[]{"SSH_FX_BYTE_RANGE_LOCK_REFUSED", "A request for a byte range lock was refused."}, new String[]{"SSH_FX_DELETE_PENDING", "An operation was attempted on a file for which a delete operation is pending."}, new String[]{"SSH_FX_FILE_CORRUPT", "The file is corrupt; an filesystem integrity check should be run."}, new String[]{"SSH_FX_OWNER_INVALID", "The principal specified can not be assigned as an owner of a file."}, new String[]{"SSH_FX_GROUP_INVALID", "The principal specified can not be assigned as the primary group of a file."}, new String[]{"SSH_FX_NO_MATCHING_BYTE_RANGE_LOCK", "The requested operation could not be completed because the\tspecifed byte range lock has not been granted."}};

    public static final String[] getDescription(int errorCode) {
        if (errorCode >= 0) {
            String[][] strArr = messages;
            if (errorCode >= strArr.length) {
                return null;
            }
            return strArr[errorCode];
        }
        return null;
    }
}
