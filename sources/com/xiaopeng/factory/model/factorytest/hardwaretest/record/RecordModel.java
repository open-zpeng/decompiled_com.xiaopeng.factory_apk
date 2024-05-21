package com.xiaopeng.factory.model.factorytest.hardwaretest.record;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;
/* loaded from: classes.dex */
public class RecordModel implements IRecordModel {
    public static final int MAX_LENGTH = 600000;
    Context mContext;
    File mFile;
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mPlayer = null;
    private static final String TAG = RecordModel.class.getSimpleName();
    private static final String BASE_PATH = Environment.getExternalStorageDirectory() + "/record/xpTest.amr";
    private static final String mFilePath = Environment.getExternalStorageDirectory() + "/record/";

    public RecordModel(Context context) {
        this.mContext = null;
        this.mFile = null;
        this.mContext = context;
        this.mFile = new File(mFilePath);
        if (!this.mFile.exists()) {
            this.mFile.mkdirs();
        }
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.record.IRecordModel
    public void startRecord() {
        if (this.mMediaRecorder == null) {
            this.mMediaRecorder = new MediaRecorder();
        }
        try {
            this.mMediaRecorder.setAudioSource(0);
            this.mMediaRecorder.setOutputFormat(4);
            this.mMediaRecorder.setAudioEncoder(2);
            this.mMediaRecorder.setOutputFile(BASE_PATH);
            this.mMediaRecorder.setMaxDuration(600000);
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
        } catch (IOException e) {
            String str = TAG;
            Log.i(str, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IllegalStateException e2) {
            String str2 = TAG;
            Log.i(str2, "call startAmr(File mRecAudioFile) failed!" + e2.getMessage());
        }
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.record.IRecordModel
    public void stopRecord() {
        MediaRecorder mediaRecorder = this.mMediaRecorder;
        if (mediaRecorder == null) {
            return;
        }
        mediaRecorder.stop();
        this.mMediaRecorder.reset();
        this.mMediaRecorder.release();
        this.mMediaRecorder = null;
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.record.IRecordModel
    public void play() {
        if (this.mPlayer == null) {
            this.mPlayer = new MediaPlayer();
            try {
                this.mPlayer.setDataSource(BASE_PATH);
                this.mPlayer.prepare();
                this.mPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.record.IRecordModel
    public void stopPlay() {
        MediaPlayer mediaPlayer = this.mPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mPlayer.release();
            this.mPlayer = null;
        }
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.record.IRecordModel
    public void releaseAll() {
        MediaRecorder mediaRecorder = this.mMediaRecorder;
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            this.mMediaRecorder.release();
        }
        MediaPlayer mediaPlayer = this.mPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mPlayer.release();
        }
    }
}
