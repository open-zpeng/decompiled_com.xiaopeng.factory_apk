package com.xpeng.dolbypresetpso;

import android.content.Context;
import android.pso.XpPsoException;
import androidx.annotation.Keep;
import com.xpeng.dolbypresetpso.http.PresetAsyncTask;
import com.xpeng.dolbypresetpso.http.PresetResponse;
import com.xpeng.upso.UpsoConfig;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.XpengKeyStore;
import com.xpeng.upso.sentry.SentryReporter;
import com.xpeng.upso.utils.Base64Util;
import com.xpeng.upso.utils.HexUtils;
import com.xpeng.upso.utils.KeyUtils;
import com.xpeng.upso.utils.LogUtils;
import com.xpeng.upso.utils.SysPropUtils;
@Keep
/* loaded from: classes2.dex */
public class DolbyPresetImpl extends XpengKeyStore implements IDolbyPreset {
    private static final String TAG = "Upso-DolbyPresetImpl";
    private String alias;
    private DolbyErrorCode dolbyErrorCode;
    private IDolbyPresetResultCallback dolbyPresetResultCallback;
    private Context mContext;
    private long taskStartTime;
    private XpengCarChipModel xpengCarChipModel;
    private XpengCarModel xpengCarModel;

    /* loaded from: classes2.dex */
    public class a implements Runnable {
        public final /* synthetic */ DolbyErrorCode a;

        public a(DolbyErrorCode dolbyErrorCode) {
            this.a = dolbyErrorCode;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                SentryReporter.ReportBuilder().setCDUID(SysPropUtils.get(SysPropUtils.SYS_PROP_CDUID, "")).setUiCarType(DolbyPresetImpl.this.xpengCarModel.toString()).setOption(getClass().getSimpleName()).setErrorDisc(this.a.toString()).setEventTime(System.currentTimeMillis()).setLibUpsoVer("2.3.7").setLibDolbyPresetVer("2.3.7").setLocalLog(LogUtils.getLog()).setDuration(System.currentTimeMillis() - DolbyPresetImpl.this.taskStartTime).report(new DolbyPresetException());
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(DolbyPresetImpl.TAG, e.toString());
            }
        }
    }

    /* loaded from: classes2.dex */
    public class b implements Runnable {
        public b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                SentryReporter.ReportBuilder().setCDUID(SysPropUtils.get(SysPropUtils.SYS_PROP_CDUID, "")).setUiCarType(DolbyPresetImpl.this.xpengCarModel.toString()).setOption(getClass().getSimpleName()).setEventTime(System.currentTimeMillis()).setLibUpsoVer("2.3.7").setLibDolbyPresetVer("2.3.7").setDuration(System.currentTimeMillis() - DolbyPresetImpl.this.taskStartTime).report(new DolbyPresetSucceed(), false);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(DolbyPresetImpl.TAG, e.toString());
            }
        }
    }

    public DolbyPresetImpl(boolean z, Context context) {
        super(z);
        this.mContext = context;
        this.taskStartTime = System.currentTimeMillis();
        this.dolbyErrorCode = DolbyErrorCode.ERR_OK;
        SentryReporter.init(this.mContext);
        LogUtils.i(TAG, "new DolbyPresetImpl,ver=2.3.7");
    }

    private void onDolbyPresetDoneReport() {
        LogUtils.i(TAG, "onDolbyPresetDoneReport");
        new Thread(new b()).start();
    }

    private void onDolbyPresetFailedReport(DolbyErrorCode dolbyErrorCode) {
        LogUtils.e(TAG, "onDolbyPresetFailedReport");
        new Thread(new a(dolbyErrorCode)).start();
    }

    @Override // com.xpeng.dolbypresetpso.IDolbyPreset
    public void enableSensitiveLog(boolean z) {
        UpsoConfig.setLogEnabled(z);
    }

    @Override // com.xpeng.dolbypresetpso.IDolbyPreset
    public String genAlias(XpengCarModel xpengCarModel, XpengCarChipModel xpengCarChipModel) {
        String str = this.alias;
        if (str != null) {
            return str;
        }
        return KeyUtils.genAlias(xpengCarModel.toString() + "_" + xpengCarChipModel.toString() + "_doooolby");
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x0099 -> B:40:0x00b3). Please submit an issue!!! */
    public /* synthetic */ void lambda$presetAesKeyFromNetwork$0$DolbyPresetImpl(PresetResponse presetResponse) {
        try {
            if (presetResponse == null) {
                this.dolbyErrorCode = DolbyErrorCode.ERR_NO_NETWORK_RESPONSE;
                LogUtils.e(TAG, "response is null");
            } else {
                String str = presetResponse.code;
                if (str == null) {
                    this.dolbyErrorCode = DolbyErrorCode.ERR_HTTP_RESPONSE_CODE;
                    LogUtils.e(TAG, "response code is null");
                } else if (!str.equals("200")) {
                    this.dolbyErrorCode = DolbyErrorCode.ERR_HTTP_RESPONSE_CODE;
                    LogUtils.e(TAG, "response code is " + presetResponse.code);
                } else if (presetResponse.data == null) {
                    this.dolbyErrorCode = DolbyErrorCode.ERR_NO_DATA;
                    LogUtils.e(TAG, "response data is null");
                } else {
                    LogUtils.d(TAG, "response is OK");
                    byte[] decode = DolbyAesCbcPkcs5Cryptor.decode(Base64Util.decode(presetResponse.data.getBytes()));
                    if (decode == null) {
                        this.dolbyErrorCode = DolbyErrorCode.ERR_KEY_DECODE;
                    } else {
                        byte[] hexStringToByte = HexUtils.hexStringToByte(new String(decode));
                        LogUtils.d(TAG, "presetAesKeyFromNetwork, key len = " + hexStringToByte.length);
                        try {
                            if (!presetAesKeyByData(hexStringToByte)) {
                                this.dolbyErrorCode = DolbyErrorCode.ERR_PRESET_FAILED;
                            } else {
                                this.dolbyErrorCode = DolbyErrorCode.ERR_OK;
                            }
                        } catch (XpPsoException e) {
                            e.printStackTrace();
                            LogUtils.e(TAG, e.toString());
                            this.dolbyErrorCode = DolbyErrorCode.ERR_PRESET_FAILED;
                        }
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            LogUtils.e(TAG, e2.toString());
        }
        DolbyErrorCode dolbyErrorCode = this.dolbyErrorCode;
        DolbyErrorCode dolbyErrorCode2 = DolbyErrorCode.ERR_OK;
        if (dolbyErrorCode != dolbyErrorCode2) {
            onDolbyPresetFailedReport(dolbyErrorCode);
        } else {
            onDolbyPresetDoneReport();
        }
        IDolbyPresetResultCallback iDolbyPresetResultCallback = this.dolbyPresetResultCallback;
        if (iDolbyPresetResultCallback != null) {
            DolbyErrorCode dolbyErrorCode3 = this.dolbyErrorCode;
            iDolbyPresetResultCallback.onPresetResult(dolbyErrorCode3 == dolbyErrorCode2, dolbyErrorCode3);
        }
    }

    @Override // com.xpeng.dolbypresetpso.IDolbyPreset
    public boolean presetAesKeyByData(byte[] bArr) throws XpPsoException {
        XpengCarModel xpengCarModel;
        XpengCarChipModel xpengCarChipModel = this.xpengCarChipModel;
        if (xpengCarChipModel != null && (xpengCarModel = this.xpengCarModel) != null) {
            String genAlias = genAlias(xpengCarModel, xpengCarChipModel);
            if (genAlias != null) {
                if (UpsoConfig.isLogEnabled()) {
                    LogUtils.d(TAG, "presetAesKeyByData, alias = " + genAlias);
                }
                boolean saveAesKeyWithCbcPkcs7 = saveAesKeyWithCbcPkcs7(genAlias, bArr);
                LogUtils.d(TAG, "preset, result = " + saveAesKeyWithCbcPkcs7);
                return true;
            }
            throw new XpPsoException("SecretKey env error occurred");
        }
        throw new XpPsoException("MUST set param first");
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0058 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v0, types: [long] */
    @Override // com.xpeng.dolbypresetpso.IDolbyPreset
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean presetAesKeyByFile(java.lang.String r6) throws android.pso.XpPsoException {
        /*
            r5 = this;
            java.io.File r0 = new java.io.File
            r0.<init>(r6)
            boolean r6 = r0.exists()
            if (r6 != 0) goto L5e
            long r1 = r0.length()
            r3 = 0
            int r6 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r6 <= 0) goto L5e
            r6 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L33 java.io.IOException -> L35
            r1.<init>(r0)     // Catch: java.lang.Throwable -> L33 java.io.IOException -> L35
            long r2 = r0.length()     // Catch: java.io.IOException -> L31 java.lang.Throwable -> L53
            int r6 = (int) r2     // Catch: java.io.IOException -> L31 java.lang.Throwable -> L53
            int r6 = r6 + 1
            byte[] r6 = new byte[r6]     // Catch: java.io.IOException -> L31 java.lang.Throwable -> L53
            r1.read(r6)     // Catch: java.io.IOException -> L31 java.lang.Throwable -> L53
            boolean r6 = r5.presetAesKeyByData(r6)     // Catch: java.io.IOException -> L31 java.lang.Throwable -> L53
            r1.close()     // Catch: java.io.IOException -> L2f
            goto L30
        L2f:
            r0 = move-exception
        L30:
            return r6
        L31:
            r6 = move-exception
            goto L38
        L33:
            r0 = move-exception
            goto L56
        L35:
            r0 = move-exception
            r1 = r6
            r6 = r0
        L38:
            android.pso.XpPsoException r0 = new android.pso.XpPsoException     // Catch: java.lang.Throwable -> L53
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L53
            r2.<init>()     // Catch: java.lang.Throwable -> L53
            java.lang.String r3 = "Read file, IO Exception "
            r2.append(r3)     // Catch: java.lang.Throwable -> L53
            java.lang.String r6 = r6.getMessage()     // Catch: java.lang.Throwable -> L53
            r2.append(r6)     // Catch: java.lang.Throwable -> L53
            java.lang.String r6 = r2.toString()     // Catch: java.lang.Throwable -> L53
            r0.<init>(r6)     // Catch: java.lang.Throwable -> L53
            throw r0     // Catch: java.lang.Throwable -> L53
        L53:
            r6 = move-exception
            r0 = r6
            r6 = r1
        L56:
            if (r6 == 0) goto L5d
            r6.close()     // Catch: java.io.IOException -> L5c
            goto L5d
        L5c:
            r6 = move-exception
        L5d:
            throw r0
        L5e:
            android.pso.XpPsoException r6 = new android.pso.XpPsoException
            java.lang.String r0 = "File not exists"
            r6.<init>(r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xpeng.dolbypresetpso.DolbyPresetImpl.presetAesKeyByFile(java.lang.String):boolean");
    }

    @Override // com.xpeng.dolbypresetpso.IDolbyPreset
    public void presetAesKeyFromNetwork(IDolbyPresetResultCallback iDolbyPresetResultCallback) {
        this.dolbyPresetResultCallback = iDolbyPresetResultCallback;
        this.dolbyErrorCode = DolbyErrorCode.ERR_OK;
        DolbyPresetParam dolbyPresetParam = new DolbyPresetParam();
        dolbyPresetParam.carModel = this.xpengCarModel;
        dolbyPresetParam.chipModel = this.xpengCarChipModel;
        PresetAsyncTask presetAsyncTask = new PresetAsyncTask(dolbyPresetParam);
        presetAsyncTask.setOnPresetCallback(new PresetAsyncTask.a() { // from class: com.xpeng.dolbypresetpso.-$$Lambda$DolbyPresetImpl$Xxq5oYfMQpPfVjV4jFq6cE6_XoI
            @Override // com.xpeng.dolbypresetpso.http.PresetAsyncTask.a
            public final void a(PresetResponse presetResponse) {
                DolbyPresetImpl.this.lambda$presetAesKeyFromNetwork$0$DolbyPresetImpl(presetResponse);
            }
        });
        presetAsyncTask.execute(new DolbyPresetParam[0]);
    }

    @Override // com.xpeng.dolbypresetpso.IDolbyPreset
    public void setParameters(DolbyPresetParam dolbyPresetParam) {
        setParameters(dolbyPresetParam.carModel, dolbyPresetParam.chipModel);
    }

    @Override // com.xpeng.dolbypresetpso.IDolbyPreset
    public void setParameters(XpengCarModel xpengCarModel, XpengCarChipModel xpengCarChipModel) {
        this.xpengCarModel = xpengCarModel;
        this.xpengCarChipModel = xpengCarChipModel;
    }
}
