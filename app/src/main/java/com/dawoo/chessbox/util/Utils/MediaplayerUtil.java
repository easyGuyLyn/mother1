package com.dawoo.chessbox.util.Utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import com.dawoo.chessbox.BoxApplication;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.util.SharePreferenceUtil;

import java.util.List;


public class MediaplayerUtil {
    private Context mContext = BoxApplication.getContext();
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private boolean isOpen = true;
    public AudioManager.OnAudioFocusChangeListener mListener = null;
    private static final MediaplayerUtil ourInstance = new MediaplayerUtil();

    public static MediaplayerUtil newInstance() {
        return ourInstance;
    }

    private MediaplayerUtil() {
        create();
    }

    private void create() {
        if (mContext==null){
           mContext = BoxApplication.getContext();
        }
        mediaPlayer = MediaPlayer.create(mContext, R.raw.bgm);
        if (mediaPlayer==null){
            mediaPlayer = MediaPlayer.create(mContext, R.raw.bgm);
        }
        mediaPlayer.setLooping(true);
        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        setOnListener();
        audioManager.requestAudioFocus(mListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

    //播放
    public void play() {
        if (!isOpen){
            return;
        }
        if (mediaPlayer == null) {
            create();
        }
        float saveVoice = SharePreferenceUtil.getVoiceBGSatus(mContext);
        setVoice(saveVoice);
        if (isAppForeground(mContext)) {
            mediaPlayer.start();
        }
    }

    public void release() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
           releaseMusic();
        }
    }

    public void releaseMusic(){
        if (!isOpen){
            return;
        }
        mediaPlayer.stop();
        mediaPlayer.release();
        audioManager.abandonAudioFocus(mListener);
        mediaPlayer = null;
    }

    public void releaseWebView(){
      if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
          releaseMusic();
      }
    }

    //设置音量
    public void setVoice(float voice) {
        if (mediaPlayer!=null){
            mediaPlayer.setVolume(voice, voice);
        }
    }

    public void open() {
        isOpen = true;
        SharePreferenceUtil.saveBgVoiceStatus(mContext, true);
    }

    public void close() {
        isOpen = false;
        SharePreferenceUtil.saveBgVoiceStatus(mContext, false);
    }


    private static boolean isAppForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList == null) {
            Log.d("MediaplayerService", "runningAppProcessInfoList is null!");
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfoList) {
            if (processInfo.processName.equals(context.getPackageName())
                    && (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)) {
                return true;
            }
        }
        return false;
    }

    public void setOnListener(){
        //因为AudioManager.OnAudioFocusChangeListener在SDK8版本开始才有。
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.ECLAIR_MR1){
            mListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    //失去焦点
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS){
                        if (mediaPlayer!=null){
     //                       mediaPlayer.pause();
                        }
                    }

                    //获取焦点
                    if (focusChange == AudioManager.AUDIOFOCUS_GAIN){
                        if (!mediaPlayer.isPlaying()&&mediaPlayer!=null){
                            play();
                        }
                    }

                }
            };
        }
    }


}
