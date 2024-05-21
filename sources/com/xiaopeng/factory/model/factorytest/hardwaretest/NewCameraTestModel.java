package com.xiaopeng.factory.model.factorytest.hardwaretest;

import android.hardware.Camera;
import android.view.SurfaceHolder;
import com.xiaopeng.commonfunc.utils.CameraUtils;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmlconfig.Support;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
/* loaded from: classes.dex */
public class NewCameraTestModel {
    private static final String TAG = "NewCameraTestModel";
    private static Camera mCamera;
    private static int mCameraPreviewFps;
    private static final int mOrientation = 0;
    private final Object mSwitchCamera;

    private NewCameraTestModel() {
        this.mSwitchCamera = new Object();
    }

    public static NewCameraTestModel getInstance() {
        return NewCameraSingle.INSTANCE;
    }

    public static void openCamera(int cameraID) {
        if (mCamera != null) {
            throw new RuntimeException("camera already initialized!");
        }
        mCamera = Camera.open(cameraID);
        Camera camera = mCamera;
        if (camera == null) {
            throw new RuntimeException("Unable to open camera");
        }
        Camera.Parameters parameters = camera.getParameters();
        parameters.setRecordingHint(false);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(0);
    }

    public void changeCameraToTop() {
        switchCameraType(0);
    }

    public void changeCameraTo360() {
        switchCameraType(1);
    }

    public void changeCameraToBack() {
        switchCameraType(1);
    }

    private void switchCameraType(final int type) {
        new Thread(new Runnable() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.NewCameraTestModel.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (NewCameraTestModel.this.mSwitchCamera) {
                    OutputStreamWriter writer = null;
                    try {
                        writer = new OutputStreamWriter(new FileOutputStream(Support.Path.getFilePath(Support.Path.CAMERA_CHANNEL)));
                        writer.write(type + "");
                        writer.flush();
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e = e;
                            e.printStackTrace();
                        }
                    } catch (Exception e2) {
                        LogUtils.e(NewCameraTestModel.TAG, e2.getMessage() + "");
                        if (writer != null) {
                            try {
                                writer.close();
                            } catch (IOException e3) {
                                e = e3;
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public synchronized void startPreviewDisplay(final SurfaceHolder holder) {
        new Thread(new Runnable() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.NewCameraTestModel.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (NewCameraTestModel.this.mSwitchCamera) {
                    if (NewCameraTestModel.mCamera != null) {
                        try {
                            NewCameraTestModel.mCamera.setPreviewDisplay(holder);
                            NewCameraTestModel.mCamera.startPreview();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    LogUtils.e(NewCameraTestModel.TAG, "Camera must be set when start preview");
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Camera.Parameters getCameraParameters(Camera camera) {
        LogUtils.i(TAG, "getCameraParameters");
        try {
            Camera.Parameters parameters = camera.getParameters();
            return parameters;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("getCameraParameters", "initCameraPreview getParameters error, msg=" + e.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initCameraPreview(Camera camera, int cameraType) {
        LogUtils.i(TAG, "initCameraPreview cameraType = " + cameraType);
        Camera.Parameters parameters = getCameraParameters(camera);
        if (parameters == null) {
            return;
        }
        int width = parameters.getPreviewSize().width;
        int height = parameters.getPreviewSize().height;
        if (cameraType == 0) {
            width = CameraUtils.HIGH_WIDTH;
            height = CameraUtils.HIGH_HEIGHT;
        } else if (1 == cameraType) {
            width = CameraUtils.DEFAULT_WIDTH;
            height = CameraUtils.DEFAULT_HEIGHT;
        }
        LogUtils.i("initCameraPreview", "initCameraPreview width=" + width + "; height=" + height);
        parameters.setPreviewSize(width, height);
        parameters.setPictureSize(width, height);
        parameters.setPictureFormat(256);
        camera.setParameters(parameters);
    }

    public synchronized void resetCameraParameters(final int cameraType) {
        LogUtils.i(TAG, "resetCameraParameters cameraType = " + cameraType);
        new Thread(new Runnable() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.NewCameraTestModel.3
            @Override // java.lang.Runnable
            public void run() {
                synchronized (NewCameraTestModel.this.mSwitchCamera) {
                    if (NewCameraTestModel.mCamera != null) {
                        Camera.Parameters parameters = NewCameraTestModel.this.getCameraParameters(NewCameraTestModel.mCamera);
                        if (parameters != null) {
                            int width = parameters.getPreviewSize().width;
                            int height = parameters.getPreviewSize().height;
                            int targetWidth = 0;
                            int targetHeight = 0;
                            if (cameraType == 0) {
                                targetWidth = CameraUtils.HIGH_WIDTH;
                                targetHeight = CameraUtils.HIGH_HEIGHT;
                            } else if (1 == cameraType) {
                                targetWidth = CameraUtils.DEFAULT_WIDTH;
                                targetHeight = CameraUtils.DEFAULT_HEIGHT;
                            }
                            if (width != targetWidth || height != targetHeight) {
                                NewCameraTestModel.mCamera.stopPreview();
                                NewCameraTestModel.this.initCameraPreview(NewCameraTestModel.mCamera, cameraType);
                                NewCameraTestModel.mCamera.startPreview();
                            } else {
                                LogUtils.i("resetCameraParameters", "initCameraPreview cancel for same size.");
                            }
                        }
                    } else {
                        LogUtils.i("resetCameraParameters", "camera is null");
                    }
                }
            }
        }).start();
    }

    public synchronized void releaseCamera() {
        LogUtils.i(TAG, "releaseCamera");
        new Thread(new Runnable() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.NewCameraTestModel.4
            @Override // java.lang.Runnable
            public void run() {
                synchronized (NewCameraTestModel.this.mSwitchCamera) {
                    if (NewCameraTestModel.mCamera != null) {
                        NewCameraTestModel.mCamera.stopPreview();
                        NewCameraTestModel.mCamera.release();
                        Camera unused = NewCameraTestModel.mCamera = null;
                    }
                }
            }
        }).start();
    }

    /* loaded from: classes.dex */
    private static class NewCameraSingle {
        private static final NewCameraTestModel INSTANCE = new NewCameraTestModel();

        private NewCameraSingle() {
        }
    }
}
