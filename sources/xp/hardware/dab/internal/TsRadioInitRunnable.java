package xp.hardware.dab.internal;

import android.content.Context;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
/* loaded from: classes2.dex */
public class TsRadioInitRunnable implements Runnable {
    private static final String TAG = "TsRadioInitRunnable";
    private Context mContext;

    public TsRadioInitRunnable(Context ctx) {
        this.mContext = ctx;
    }

    @Override // java.lang.Runnable
    public void run() {
        Log.d(TAG, "run TsRadioInitRunnable ");
        createServer(Loader.getRadioServiceJar(this.mContext), Loader.RADIO_SERVER_CLASS_NAME);
    }

    private void createServer(Loader loader, String className) {
        Log.d(TAG, "createServer");
        Class serviceClass = loader.loadClass(className);
        try {
            Constructor serviceConst = serviceClass.getDeclaredConstructor(Context.class);
            serviceConst.setAccessible(true);
            serviceConst.newInstance(this.mContext);
            serviceConst.setAccessible(false);
        } catch (IllegalAccessException ex) {
            Log.e(TAG, "raise exception", ex);
        } catch (InstantiationException ex2) {
            Log.e(TAG, "raise exception", ex2);
        } catch (NoSuchMethodException ex3) {
            Log.e(TAG, "raise exception", ex3);
        } catch (InvocationTargetException ex4) {
            Log.e(TAG, "raise exception", ex4);
        }
    }
}
