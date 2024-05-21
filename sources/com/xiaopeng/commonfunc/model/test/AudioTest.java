package com.xiaopeng.commonfunc.model.test;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import java.io.IOException;
/* loaded from: classes.dex */
public class AudioTest {
    private static final String TAG = "AudioTest";
    private AudioManager mAudioManager;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private Ringtone mRingtone;

    public AudioTest(Context context, Uri uri) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mRingtone = RingtoneManager.getRingtone(this.mContext, uri);
        Ringtone ringtone = this.mRingtone;
        if (ringtone != null) {
            ringtone.setStreamType(3);
        }
    }

    public AudioTest(Context context) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
    }

    public int getCurrentMediaVolume() {
        return this.mAudioManager.getStreamVolume(3);
    }

    public int getMediaMaxVolume() {
        return this.mAudioManager.getStreamMaxVolume(3);
    }

    public void setMediaVolume(int volume) {
        this.mAudioManager.setStreamVolume(3, volume, 0);
    }

    public void onVolumeChanged(int progress, int minVol) {
        this.mAudioManager.setStreamVolume(3, progress + minVol, 0);
        startPlay();
    }

    private void startPlay() {
        Ringtone ringtone;
        Ringtone ringtone2 = this.mRingtone;
        if (ringtone2 != null && !ringtone2.isPlaying() && (ringtone = this.mRingtone) != null) {
            ringtone.play();
        }
    }

    public void testTrack(Uri uri) {
        if (this.mMediaPlayer == null) {
            Log.d(TAG, "testTrack mMediaPlayer == null ");
            this.mMediaPlayer = new MediaPlayer();
            this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.xiaopeng.commonfunc.model.test.AudioTest.1
                @Override // android.media.MediaPlayer.OnPreparedListener
                public void onPrepared(MediaPlayer mp) {
                    Log.d(AudioTest.TAG, " mMediaPlayer onPrepared");
                    mp.start();
                }
            });
            this.mMediaPlayer.setLooping(true);
        } else {
            Log.d(TAG, "testTrack mMediaPlayer != null ");
            this.mMediaPlayer.reset();
        }
        try {
            this.mMediaPlayer.setDataSource(this.mContext, uri);
            this.mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void destroy(int preVol) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                this.mMediaPlayer.stop();
            }
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        this.mAudioManager.setStreamVolume(3, preVol, 0);
    }
}