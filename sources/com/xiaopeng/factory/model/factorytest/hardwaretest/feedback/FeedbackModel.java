package com.xiaopeng.factory.model.factorytest.hardwaretest.feedback;

import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.commonfunc.bean.factorytest.TestResultItem;
import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import java.io.FileOutputStream;
/* loaded from: classes.dex */
public class FeedbackModel {
    public static final int AUDIO_SAMPLE_RATE = 44100;
    public static final int MAX_LENGTH = 600000;
    private static final String TAG = "FeedbackModel";
    private AudioRecord mAudioRecord;
    private MediaPlayer mMediaPlayer;
    private MediaRecorder mMediaRecorder;
    private final String PARAMETER_LOOPBACK = "ftmtest=loopback";
    private final String PARAMETER_LOOPBACK_START = "action=start";
    private final String PARAMETER_LOOPBACK_STOP = "action=stop";
    private final String PARAMETER_LOOPBACK_SRC = "source=";
    private final String PARAMETER_LOOPBACK_DEST = "dest=";
    private int mBufferSizeInBytes = 0;
    private boolean mIsRecording = false;
    private final AudioManager mAudioManager = (AudioManager) MyApplication.getContext().getSystemService("audio");

    public void startFeedback(int src, int dest) {
        AudioManager audioManager = this.mAudioManager;
        audioManager.setParameters("ftmtest=loopback;action=start;source=" + src + Constant.SEMICOLON_STRING + "dest=" + dest);
    }

    public void stopFeedback() {
        this.mAudioManager.setParameters("ftmtest=loopback;action=stop");
    }

    public void play(int resid) throws Exception {
        Uri uri = Uri.parse("android.resource://" + MyApplication.getContext().getPackageName() + "/" + resid);
        if (this.mMediaPlayer == null) {
            LogUtils.i(TAG, "play mMediaPlayer == null ");
            this.mMediaPlayer = new MediaPlayer();
            this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.feedback.-$$Lambda$FeedbackModel$3j6dg1a-FloV9KalLHfG0NGRnGM
                @Override // android.media.MediaPlayer.OnPreparedListener
                public final void onPrepared(MediaPlayer mediaPlayer) {
                    FeedbackModel.lambda$play$0(mediaPlayer);
                }
            });
            this.mMediaPlayer.setLooping(true);
        } else {
            LogUtils.i(TAG, "play mMediaPlayer != null");
            this.mMediaPlayer.reset();
        }
        this.mAudioManager.setStreamVolume(3, 27, 0);
        this.mMediaPlayer.setDataSource(MyApplication.getContext(), uri);
        this.mMediaPlayer.prepare();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$play$0(MediaPlayer mp) {
        LogUtils.i(TAG, " mMediaPlayer onPrepared sound 1khz");
        mp.start();
    }

    public void play(final String path) throws Exception {
        if (this.mMediaPlayer == null) {
            LogUtils.i(TAG, "play mMediaPlayer == null ");
            this.mMediaPlayer = new MediaPlayer();
            this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.feedback.-$$Lambda$FeedbackModel$BJiS4h8IiQD_Vu3kuokmi4eBET8
                @Override // android.media.MediaPlayer.OnPreparedListener
                public final void onPrepared(MediaPlayer mediaPlayer) {
                    FeedbackModel.lambda$play$1(path, mediaPlayer);
                }
            });
            this.mMediaPlayer.setLooping(true);
        } else {
            LogUtils.i(TAG, "play mMediaPlayer != null ");
            this.mMediaPlayer.reset();
        }
        this.mAudioManager.setStreamVolume(3, 27, 0);
        this.mMediaPlayer.setDataSource(path);
        this.mMediaPlayer.prepare();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$play$1(String path, MediaPlayer mp) {
        LogUtils.i(TAG, " mMediaPlayer onPrepared path:" + path);
        mp.start();
    }

    public void stopPlay() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                this.mMediaPlayer.stop();
            }
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    public void startRecord(final String path) {
        if (this.mAudioRecord == null) {
            this.mBufferSizeInBytes = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE, 12, 2);
            this.mAudioRecord = new AudioRecord(1, AUDIO_SAMPLE_RATE, 12, 2, this.mBufferSizeInBytes);
        }
        this.mAudioRecord.startRecording();
        this.mIsRecording = true;
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.feedback.-$$Lambda$FeedbackModel$VyAw54UElMXq9PR7TxpoyGV1b4U
            @Override // java.lang.Runnable
            public final void run() {
                FeedbackModel.this.lambda$startRecord$2$FeedbackModel(path);
            }
        });
    }

    public void stopRecord() {
        if (this.mAudioRecord != null) {
            LogUtils.i(TAG, "");
            this.mIsRecording = false;
            this.mAudioRecord.stop();
            this.mAudioRecord.release();
            this.mAudioRecord = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: saveWavFile */
    public void lambda$startRecord$2$FeedbackModel(String path) {
        byte[] audiodata = new byte[this.mBufferSizeInBytes];
        FileOutputStream fos = FileUtil.createFileOS(path);
        long totalsize = 0;
        byte[] emptyheader = new byte[44];
        FileUtil.writeFileOS(fos, emptyheader);
        while (this.mIsRecording) {
            long readsize = this.mAudioRecord.read(audiodata, 0, this.mBufferSizeInBytes);
            if (-3 != readsize && fos != null && FileUtil.writeFileOS(fos, audiodata)) {
                totalsize += readsize;
            }
        }
        FileUtil.closeFileOS(fos);
        writeWaveHeader(path, totalsize);
    }

    private void writeWaveHeader(String path, long totalAudioLen) {
        long totalDataLen = totalAudioLen + 36;
        long byteRate = (705600 * 2) / 8;
        byte[] header = {82, 73, TestResultItem.RESULT_FAIL, TestResultItem.RESULT_FAIL, (byte) (totalDataLen & 255), (byte) ((totalDataLen >> 8) & 255), (byte) ((totalDataLen >> 16) & 255), (byte) ((totalDataLen >> 24) & 255), 87, 65, 86, TestResultItem.RESULT_ENTER, 102, 109, 116, WlanPresenter.WL_RMMOD_INSMOD, 16, 0, 0, 0, 1, 0, (byte) 2, 0, (byte) (44100 & 255), (byte) ((44100 >> 8) & 255), (byte) ((44100 >> 16) & 255), (byte) ((44100 >> 24) & 255), (byte) (byteRate & 255), (byte) ((byteRate >> 8) & 255), (byte) ((byteRate >> 16) & 255), (byte) ((byteRate >> 24) & 255), 4, 0, 16, 0, 100, 97, 116, 97, (byte) (totalAudioLen & 255), (byte) ((totalAudioLen >> 8) & 255), (byte) ((totalAudioLen >> 16) & 255), (byte) ((totalAudioLen >> 24) & 255)};
        FileUtil.coverToFile(path, 0, header);
    }

    public void releaseAll() {
        stopRecord();
        stopPlay();
    }
}
