package com.xpeng.upso.sentry;

import android.content.Context;
import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import com.xpeng.upso.utils.Base64Util;
import com.xpeng.upso.utils.DateTimeUtil;
import com.xpeng.upso.utils.LogUtils;
import io.sentry.Breadcrumb;
import io.sentry.Sentry;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.android.core.SentryAndroid;
import io.sentry.android.core.SentryAndroidOptions;
import io.sentry.protocol.Message;
import io.sentry.protocol.SentryId;
import java.nio.charset.StandardCharsets;
@Keep
/* loaded from: classes2.dex */
public class SentryReporter {
    private static final String CDUID = "cduid";
    private static final int CDU_DOLBY_LOG_SIZE_LIMITED = 5;
    private static final int CDU_PROXY_LOG_SIZE_LIMITED = 9;
    private static final String CLIENT_CAR_TYPE = "clientCarType";
    private static final String CLIENT_LOG = "clientLog";
    private static final String CLIENT_TYPE = "clientType";
    private static final String DURATION = "duration";
    private static final String ECU_ID = "clientId";
    private static final String ECU_IS_USER_VER = "ecuIsUserVer";
    private static final int ECU_LOG_SIZE_LIMITED = 4;
    private static final String ECU_ROM_VER = "ecuRomVer";
    private static final String ERROR_CODE = "errorCode";
    private static final String ERROR_DISC = "errorDisc";
    private static final String EVENT_TIME = "eventTime";
    private static final String EVENT_TIMESTAMP = "eventTimeStamp";
    private static final String LIB_DOLBY_PRESET_PSO_VER = "libDolbyPresetPsoVer";
    private static final String LIB_DOLBY_VERIFY_PSO_VER = "libDolbyVerifyPsoVer";
    private static final String LIB_SERVER_PROXY_VER = "libServerProxyVer";
    private static final String LIB_UPSO_VER = "libUpsoVer";
    private static final String LOCAL_LOG = "localLog";
    private static final int LOG_BLOCK_SIZE = 750;
    private static final String OPTION = "option";
    private static final String PROXY_LOG_TAG = "proxyLog";
    private static final String TAG = "SentryUtils";
    private static final String UI_CAR_TYPE = "UiCarType";
    private static final String UI_CLIENT_TYPE = "UiClientType";
    private static Context mContext;
    private String UiCarType;
    private String UiClientType;
    private String cduid;
    private String clientCarType;
    private String clientLog;
    private String clientType;
    boolean debugEnable = false;
    private Float duration;
    private String ecuId;
    private String ecuRomVer;
    private Integer errorCode;
    private String errorDisc;
    private String eventTime;
    private Long eventTimeStamp;
    private Boolean isUserVer;
    private String libDolbyPresetPso_ver;
    private String libDolbyVerifyPso_ver;
    private String libServerProxy_ver;
    private String libUpso_ver;
    private String local_log;
    private String option;
    private String proxyLog;
    private static Boolean inited = new Boolean(false);
    static int sw = 0;

    public static SentryReporter ReportBuilder() {
        return new SentryReporter();
    }

    public static void SentryAndroidInit(final Context context) {
        new Thread(new Runnable() { // from class: com.xpeng.upso.sentry.-$$Lambda$SentryReporter$aklFQoeVFHaoydZvWjxCQeLF3Q8
            @Override // java.lang.Runnable
            public final void run() {
                SentryReporter.lambda$SentryAndroidInit$3(context);
            }
        }).start();
    }

    public static void init(Context context) {
        mContext = context;
        try {
            LogUtils.init(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SentryAndroidInit(mContext);
    }

    public static boolean isInited() {
        return inited.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ SentryEvent lambda$SentryAndroidInit$0(SentryEvent sentryEvent, Object obj) {
        SentryLevel.DEBUG.equals(sentryEvent.getLevel());
        return sentryEvent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Breadcrumb lambda$SentryAndroidInit$1(Breadcrumb breadcrumb, Object obj) {
        return breadcrumb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$SentryAndroidInit$2(SentryAndroidOptions sentryAndroidOptions) {
        LogUtils.i(TAG, "SentryAndroid.init");
        sentryAndroidOptions.setDebug(Boolean.FALSE);
        sentryAndroidOptions.setDsn("https://ef1a9ea0bfe442f8929dbc9da17482dc@front-monitor.xiaopeng.com/29");
        sentryAndroidOptions.setBeforeSend(new SentryOptions.BeforeSendCallback() { // from class: com.xpeng.upso.sentry.-$$Lambda$SentryReporter$gJdZQQfQgSJxIyvHFeSCiYnCvLA
            @Override // io.sentry.SentryOptions.BeforeSendCallback
            public final SentryEvent execute(SentryEvent sentryEvent, Object obj) {
                return SentryReporter.lambda$SentryAndroidInit$0(sentryEvent, obj);
            }
        });
        sentryAndroidOptions.setBeforeBreadcrumb(new SentryOptions.BeforeBreadcrumbCallback() { // from class: com.xpeng.upso.sentry.-$$Lambda$SentryReporter$iogJnVu-FrovB7xlXhsYcjj4ArU
            @Override // io.sentry.SentryOptions.BeforeBreadcrumbCallback
            public final Breadcrumb execute(Breadcrumb breadcrumb, Object obj) {
                return SentryReporter.lambda$SentryAndroidInit$1(breadcrumb, obj);
            }
        });
        sentryAndroidOptions.setEnvironment("production");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$SentryAndroidInit$3(Context context) {
        synchronized (SentryReporter.class) {
            if (isInited()) {
                return;
            }
            try {
                SentryAndroid.init(context, new Sentry.OptionsConfiguration() { // from class: com.xpeng.upso.sentry.-$$Lambda$SentryReporter$EUdfCMaqF6eC3FgzNgmmTuZy5qU
                    @Override // io.sentry.Sentry.OptionsConfiguration
                    public final void configure(SentryOptions sentryOptions) {
                        SentryReporter.lambda$SentryAndroidInit$2((SentryAndroidOptions) sentryOptions);
                    }
                });
                inited = Boolean.TRUE;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(TAG, e.toString());
            }
        }
    }

    public static void test() {
        try {
            int i = sw ^ 1;
            sw = i;
            if (i == 1) {
                SentryEvent sentryEvent = new SentryEvent();
                Message message = new Message();
                message.setMessage("onFailed status 1");
                sentryEvent.setMessage(message);
                sentryEvent.setLevel(SentryLevel.FATAL);
                SentryId sentryId = new SentryId();
                Sentry.addBreadcrumb("breadcrumb ,t=" + System.currentTimeMillis());
                sentryEvent.setEventId(sentryId);
                Sentry.captureEvent(sentryEvent);
                Sentry.clearBreadcrumbs();
                LogUtils.d(TAG, "Sentry id=" + sentryId.toString() + ",Message:" + sentryEvent.getMessage().getMessage());
                SentryEvent sentryEvent2 = new SentryEvent();
                Message message2 = new Message();
                message2.setMessage("onDone status 2");
                sentryEvent2.setMessage(message2);
                sentryEvent2.setLevel(SentryLevel.FATAL);
                SentryId sentryId2 = new SentryId();
                Sentry.addBreadcrumb("breadcrumb ,t=" + System.currentTimeMillis());
                sentryEvent2.setEventId(sentryId2);
                Sentry.captureEvent(sentryEvent2);
                Sentry.clearBreadcrumbs();
                LogUtils.d(TAG, "Sentry id=" + sentryId2.toString() + ",Message:" + sentryEvent2.getMessage().getMessage());
                return;
            }
            try {
                int i2 = 1 / 0;
            } catch (Exception e) {
                e.printStackTrace();
                Sentry.captureException(e);
            }
            try {
                throw new Exception("test1");
            } catch (Exception e2) {
                e2.printStackTrace();
                Sentry.captureException(e2);
                try {
                    throw new RuntimeException("test2");
                } catch (Exception e3) {
                    e3.printStackTrace();
                    Sentry.captureException(e3);
                }
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    public void addBreadcrumb2SentryEvent(SentryEvent sentryEvent, int i, String str, String str2) {
        try {
            int length = str.length();
            LogUtils.i(TAG, "add log len=" + str.length());
            int i2 = length - (i * LOG_BLOCK_SIZE);
            int i3 = 0;
            if (i2 < 0) {
                i2 = 0;
            }
            while (i3 < i) {
                Breadcrumb breadcrumb = new Breadcrumb();
                int i4 = i3 + 1;
                int i5 = (i4 * LOG_BLOCK_SIZE) + i2;
                if (i5 < str.length()) {
                    breadcrumb.setData(str2 + i3, Base64Util.encodeToString(str.substring((i3 * LOG_BLOCK_SIZE) + i2, i5).getBytes(StandardCharsets.UTF_8)));
                } else {
                    int i6 = (i3 * LOG_BLOCK_SIZE) + i2;
                    if (i6 < str.length()) {
                        breadcrumb.setData(str2 + i3, Base64Util.encodeToString(str.substring(i6, str.length() - 1).getBytes(StandardCharsets.UTF_8)));
                    }
                }
                sentryEvent.addBreadcrumb(breadcrumb);
                if (i5 >= str.length()) {
                    return;
                }
                i3 = i4;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void report(@Nullable Throwable th) {
        report(th, true);
    }

    public SentryReporter setCDUID(String str) {
        this.cduid = str;
        return this;
    }

    public SentryReporter setClientCarType(String str) {
        this.clientCarType = str;
        return this;
    }

    public SentryReporter setClientLog(String str) {
        this.clientLog = str;
        return this;
    }

    public SentryReporter setClientType(String str) {
        this.clientType = str;
        return this;
    }

    public void setDebug(boolean z) {
        this.debugEnable = z;
    }

    public SentryReporter setDuration(long j) {
        this.duration = Float.valueOf(((float) j) / 1000.0f);
        return this;
    }

    public SentryReporter setEcuId(String str) {
        this.ecuId = str;
        return this;
    }

    public SentryReporter setEcuIsUserVer(boolean z) {
        this.isUserVer = Boolean.valueOf(z);
        return this;
    }

    public SentryReporter setEcuRomVer(String str) {
        this.ecuRomVer = str;
        return this;
    }

    public SentryReporter setErrorCode(int i) {
        this.errorCode = Integer.valueOf(i);
        return this;
    }

    public SentryReporter setErrorDisc(String str) {
        this.errorDisc = str;
        return this;
    }

    public SentryReporter setEventTime(long j) {
        Long valueOf = Long.valueOf(j);
        this.eventTimeStamp = valueOf;
        setEventTime(DateTimeUtil.time2String(valueOf));
        return this;
    }

    public SentryReporter setLibDolbyPresetVer(String str) {
        this.libDolbyPresetPso_ver = str;
        return this;
    }

    public SentryReporter setLibDolbyVerifyVer(String str) {
        this.libDolbyVerifyPso_ver = str;
        return this;
    }

    public SentryReporter setLibServerProxyVer(String str) {
        this.libServerProxy_ver = str;
        return this;
    }

    public SentryReporter setLibUpsoVer(String str) {
        this.libUpso_ver = str;
        return this;
    }

    public SentryReporter setLocalLog(String str) {
        this.local_log = str;
        return this;
    }

    public SentryReporter setOption(String str) {
        this.option = str;
        return this;
    }

    public SentryReporter setProxyLog(String str) {
        this.proxyLog = str;
        return this;
    }

    public SentryReporter setUiCarType(String str) {
        this.UiCarType = str;
        return this;
    }

    public SentryReporter setUiClientType(String str) {
        this.UiClientType = str;
        return this;
    }

    public void report(@Nullable Throwable th, boolean z) {
        synchronized (SentryReporter.class) {
            if (mContext != null) {
                inited = Boolean.FALSE;
                SentryAndroidInit(mContext);
            }
            SentryEvent sentryEvent = new SentryEvent();
            sentryEvent.setLevel(z ? SentryLevel.FATAL : SentryLevel.INFO);
            Message message = new Message();
            message.setMessage(this.clientCarType + "_" + this.clientType + "_" + this.option + "isError=" + z);
            sentryEvent.setMessage(message);
            String str = this.cduid;
            if (str != null) {
                sentryEvent.setTag(CDUID, str);
            }
            String str2 = this.clientType;
            if (str2 != null) {
                sentryEvent.setTag(CLIENT_TYPE, str2);
            }
            String str3 = this.clientCarType;
            if (str3 != null) {
                sentryEvent.setTag(CLIENT_CAR_TYPE, str3);
            }
            String str4 = this.UiCarType;
            if (str4 != null) {
                sentryEvent.setTag(UI_CAR_TYPE, str4);
            }
            String str5 = this.UiClientType;
            if (str5 != null) {
                sentryEvent.setTag(UI_CLIENT_TYPE, str5);
            }
            String str6 = this.ecuId;
            if (str6 != null) {
                sentryEvent.setTag(ECU_ID, str6);
            }
            if (this.isUserVer != null) {
                sentryEvent.setTag(ECU_IS_USER_VER, "" + this.isUserVer);
            }
            String str7 = this.ecuRomVer;
            if (str7 != null) {
                sentryEvent.setTag(ECU_ROM_VER, str7);
            }
            String str8 = this.option;
            if (str8 != null) {
                sentryEvent.setTag(OPTION, str8);
            }
            if (this.errorCode != null) {
                sentryEvent.setTag(ERROR_CODE, "" + this.errorCode);
            }
            String str9 = this.errorDisc;
            if (str9 != null) {
                sentryEvent.setTag(ERROR_DISC, str9);
            }
            String str10 = this.eventTime;
            if (str10 != null) {
                sentryEvent.setTag(EVENT_TIME, str10);
            }
            Long l = this.eventTimeStamp;
            if (l != null) {
                sentryEvent.setTag(EVENT_TIMESTAMP, l.toString());
            }
            Float f = this.duration;
            if (f != null) {
                sentryEvent.setTag(DURATION, String.format("%.1f", f));
            }
            String str11 = this.libUpso_ver;
            if (str11 != null) {
                sentryEvent.setTag(LIB_UPSO_VER, str11);
            }
            String str12 = this.libServerProxy_ver;
            if (str12 != null) {
                sentryEvent.setTag(LIB_SERVER_PROXY_VER, str12);
            }
            String str13 = this.libDolbyPresetPso_ver;
            if (str13 != null) {
                sentryEvent.setTag(LIB_DOLBY_PRESET_PSO_VER, str13);
            }
            String str14 = this.libDolbyVerifyPso_ver;
            if (str14 != null) {
                sentryEvent.setTag(LIB_DOLBY_VERIFY_PSO_VER, str14);
            }
            try {
                if (sentryEvent.getBreadcrumbs() != null) {
                    sentryEvent.getBreadcrumbs().clear();
                }
                String str15 = this.local_log;
                if (str15 != null) {
                    addBreadcrumb2SentryEvent(sentryEvent, 5, str15, LOCAL_LOG);
                } else {
                    String str16 = this.proxyLog;
                    if (str16 != null) {
                        addBreadcrumb2SentryEvent(sentryEvent, 9, str16, PROXY_LOG_TAG);
                    }
                    String str17 = this.clientLog;
                    if (str17 != null) {
                        addBreadcrumb2SentryEvent(sentryEvent, 4, str17, CLIENT_LOG);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            sentryEvent.setThrowable(th);
            SentryId captureEvent = Sentry.captureEvent(sentryEvent);
            LogUtils.e(TAG, "sentryId =" + captureEvent);
        }
    }

    private SentryReporter setEventTime(String str) {
        this.eventTime = str;
        return this;
    }
}
