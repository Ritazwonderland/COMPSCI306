package com.example.a306_simulation2.services;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.example.a306_simulation2.R;
import com.example.a306_simulation2.utils.Constants;

public class MusicService {
    private HandlerThread musicThread;
    private Handler musicHandler;
    private MediaPlayer mediaPlayer;
    private Context context;
    private int currentMusic = Constants.MUSIC_SLOW;

    public MusicService(Context context) {
        this.context = context;
        musicThread = new HandlerThread("MusicThread");
        musicThread.start();
        musicHandler = new Handler(musicThread.getLooper());
    }

    public void start() {
        musicHandler.post(() -> {
            mediaPlayer = MediaPlayer.create(context, currentMusic);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        });
    }

    public void adjustMusicTempo(int stepsPerMinute) {
        musicHandler.post(() -> {
            if (mediaPlayer != null) {
                if (stepsPerMinute > 120 && currentMusic != Constants.MUSIC_FAST) {
                    switchMusic(Constants.MUSIC_FAST);
                } else if (stepsPerMinute <= 120 && currentMusic != Constants.MUSIC_SLOW) {
                    switchMusic(Constants.MUSIC_SLOW);
                }
            }
        });
    }

    private void switchMusic(int newMusic) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, newMusic);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        currentMusic = newMusic;
    }

    public void pause() {
        musicHandler.post(() -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        });
    }

    public void stop() {
        musicHandler.post(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            musicThread.quit();
        });
    }
}