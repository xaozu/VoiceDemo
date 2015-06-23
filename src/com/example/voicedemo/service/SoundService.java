package com.example.voicedemo.service;

import com.example.voicedemo.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class SoundService extends Service {
	    private MediaPlayer mp;
	 
	    @Override
	    public void onCreate() {
	        super.onCreate();
	        mp = MediaPlayer.create(this, R.raw.bird);
	    }
	 
	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        mp.release();
	        stopSelf();
	    }
	 
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	        boolean playing = intent.getBooleanExtra("playing", false);
	        if (playing) {
	        	Toast.makeText(SoundService.this, "开始播放！", Toast.LENGTH_SHORT).show();
	            mp.start();
	        } else {
	        	Toast.makeText(SoundService.this, "暂停播放！", Toast.LENGTH_SHORT).show();
	            mp.pause();
	        }
	        mp.getDuration();//获得时长
//	        mp.seekTo(msec)
	        return super.onStartCommand(intent, flags, startId);
	    }
	 
	    @Override
	    public IBinder onBind(Intent intent) {
	        return null;
	    }
	 
	}

