package xp.hardware.dab.common;
/* loaded from: classes2.dex */
public interface DabConstants {
    public static final int AM_FREQ_MAX = 1620;
    public static final int AM_FREQ_MIN = 522;
    public static final int DAB_FREQ_MAX = 239200;
    public static final int DAB_FREQ_MIN = 174928;
    public static final int FM_FREQ_MAX = 108000;
    public static final int FM_FREQ_MIN = 87500;
    public static final String[] PTY_NAME_ARRAY = {"NONE", "NEWS", "AFFAIRS", "INFO", "SPORT", "EDUCATE", "DRAMA", "CULTURE", "SCIENCE", "VARIED", "POP M", "ROCK M", "EASY M", "LIGHT M", "CLASSICS", "OTHER M", "WEATHER", "FINANCE", "CHILDREN", "SOCIAL", "RELIGION", "PHONE IN", "TRAVEL", "LEISURE", "JAZZ", "COUNTRY", "NATION M", "OLDIES", "FOLK M", "DOCUMENT", "TEST", "ALARM!", "TRAFFIC"};
    public static final String RADIO_SERVICE_NAME = "ts_radiomanager";

    /* loaded from: classes2.dex */
    public static final class AnnouncementType {
        public static final int TYPE_ALARM = 0;
        public static final int TYPE_ALARM_FM = 2;
        public static final int TYPE_TRAFFIC = 1;
        public static final int TYPE_TRAFFIC_FM = 3;
    }

    /* loaded from: classes2.dex */
    public static final class RadioSourceType {
        public static final int TYPE_AM = 1;
        public static final int TYPE_DAB = 2;
        public static final int TYPE_FM = 0;
        public static final int TYPE_NONE = -1;
    }

    /* loaded from: classes2.dex */
    public static final class WorkStatus {
        public static final int NORMAL = 0;
        public static final int SCAN = 2;
        public static final int SCAN_HOLD = 3;
        public static final int SEEK = 1;
    }
}
