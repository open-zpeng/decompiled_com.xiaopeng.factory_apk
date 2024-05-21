package com.xiaopeng.factory.model.factorytest.hardwaretest.camera;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.commonfunc.utils.TimeUtil;
import com.xiaopeng.lib.utils.LogUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
/* loaded from: classes.dex */
public class CameraModel {
    public static final String PATH_PICTURE_DIR = "Pictures/";
    private static final String PATH_RECORDER_DIR = "VideoRecorderTest/";
    public static final String SUFFIX_PICTURE_FORMAT = ".jpg";
    private static final String SUFFIX_VIDEO_MP4_FORMAT = ".mp4";
    private static final String TAG = "CameraModel";
    private static CameraModel sCameraModel = null;
    private static final int sChipAddress1 = 61;
    private static final int sChipAddress2 = 48;
    private static final String sChipId1 = "0x7a";
    private static final String sChipId2 = "0x60";
    private static final int sDataAddress1 = 0;
    private static final int sDataAddress2 = 0;
    private static final int sI2cBus = 8;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private SurfaceHolder mSurfaceHolder;
    private final Object mSwitchCamera = new Object();
    private int mCurrentState = -1;

    private CameraModel() {
    }

    public static CameraModel getInstance() {
        if (sCameraModel == null) {
            LogUtils.i(TAG, "create CameraModel Instance");
            sCameraModel = new CameraModel();
        }
        return sCameraModel;
    }

    public static int getNumberOfCameras() {
        return Camera.getNumberOfCameras();
    }

    public String getI2cCmd1() {
        return "8 61 0";
    }

    public String getI2cCmd2() {
        return "8 48 0";
    }

    public String getChipId1() {
        return sChipId1;
    }

    public String getChipId2() {
        return sChipId2;
    }

    public void setSurfaceHolder(SurfaceHolder holder) {
        this.mSurfaceHolder = holder;
    }

    public void openCamera(int state) {
        synchronized (this.mSwitchCamera) {
            LogUtils.i(TAG, "openCamera state = " + state);
            this.mCurrentState = state;
            if (this.mCamera == null) {
                this.mCamera = Camera.open(state);
            }
        }
    }

    public void openPreView() {
        synchronized (this.mSwitchCamera) {
            try {
                if (this.mCamera != null && this.mSurfaceHolder.getSurface() != null) {
                    LogUtils.i(TAG, "setPreviewSurface before");
                    this.mCamera.setPreviewSurface(this.mSurfaceHolder.getSurface());
                    LogUtils.i(TAG, "setPreviewSurface after");
                    this.mCamera.startPreview();
                    LogUtils.i(TAG, "startPreview after");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeCamera() {
        synchronized (this.mSwitchCamera) {
            if (this.mCamera != null) {
                LogUtils.i(TAG, "stopPreview before");
                this.mCamera.stopPreview();
                LogUtils.i(TAG, "stopPreview after");
                this.mCamera.release();
                LogUtils.i(TAG, "release after");
                this.mCamera = null;
                this.mCurrentState = -1;
            }
        }
    }

    public boolean startRecording() {
        LogUtils.i(TAG, "startRecording");
        if (prepareMediaRecorder()) {
            this.mMediaRecorder.start();
            return true;
        }
        releaseMediaRecorder();
        return false;
    }

    public void stopRecording() {
        LogUtils.i(TAG, "stopRecording");
        MediaRecorder mediaRecorder = this.mMediaRecorder;
        if (mediaRecorder != null) {
            mediaRecorder.stop();
        }
        releaseMediaRecorder();
    }

    public boolean isRecording() {
        boolean isRecording = this.mMediaRecorder != null;
        LogUtils.i(TAG, "isRecording = " + isRecording);
        return isRecording;
    }

    private boolean prepareMediaRecorder() {
        boolean ret = false;
        synchronized (this.mSwitchCamera) {
            LogUtils.i(TAG, "prepareMediaRecorder");
            try {
                if (this.mCamera != null) {
                    this.mMediaRecorder = new MediaRecorder();
                    List<Camera.Size> sizes = getCameraParameters().getSupportedVideoSizes();
                    this.mCamera.unlock();
                    this.mMediaRecorder.setCamera(this.mCamera);
                    this.mMediaRecorder.setVideoSource(1);
                    LogUtils.i(TAG, "prepareMediaRecorder mCurrentState = " + this.mCurrentState);
                    CamcorderProfile ccp = CamcorderProfile.get(this.mCurrentState, 5);
                    this.mMediaRecorder.setProfile(ccp);
                    LogUtils.i(TAG, "prepareMediaRecorder width:" + sizes.get(0).width + ", height:" + sizes.get(0).height);
                    this.mMediaRecorder.setVideoSize(sizes.get(0).width, sizes.get(0).height);
                    this.mMediaRecorder.setPreviewDisplay(this.mSurfaceHolder.getSurface());
                    String path = FileUtil.getSDPath();
                    if (path != null) {
                        String dir = path + File.separator + PATH_RECORDER_DIR;
                        FileUtil.mkDir(dir);
                        String path2 = dir + TimeUtil.getDate() + SUFFIX_VIDEO_MP4_FORMAT;
                        LogUtils.i(TAG, "prepareMediaRecorder path = " + path2);
                        this.mMediaRecorder.setOutputFile(path2);
                        this.mMediaRecorder.prepare();
                        ret = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                ret = false;
            }
        }
        LogUtils.i(TAG, "prepareMediaRecorder ret:" + ret);
        return ret;
    }

    private Camera.Parameters getCameraParameters() {
        LogUtils.i(TAG, "getCameraParameters");
        try {
            Camera.Parameters parameters = this.mCamera.getParameters();
            return parameters;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void releaseMediaRecorder() {
        synchronized (this.mSwitchCamera) {
            LogUtils.i(TAG, "releaseMediaRecorder");
            if (this.mMediaRecorder != null) {
                this.mMediaRecorder.reset();
                this.mMediaRecorder.release();
                this.mMediaRecorder = null;
                this.mCamera.lock();
            }
        }
    }

    public void takePicture(Camera.PictureCallback callback) {
        synchronized (this.mSwitchCamera) {
            LogUtils.i(TAG, "takePicture");
            if (this.mCamera != null) {
                this.mCamera.takePicture(null, null, callback);
            }
        }
    }
}
