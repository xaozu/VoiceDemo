package com.example.voicedemo;

import com.example.voicedemo.service.SoundService;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 延迟30秒自动播放音乐
 * 
 * @author xaozu
 * 
 */
public class MainActivity extends Activity {

	public Handler mHandler = new Handler();
	private static final long POST_PLAY = 30 * 1000;// 延迟30秒播放音乐
	private static final long POST_STOP = 60 * 10000;// 播放1分钟停止
	private TextView text_time;
	private int time = 0;
	private MediaPlayer mp;
	private EditText edit;
	private Button btn;
	private static int curTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mp = MediaPlayer.create(this, R.raw.music_1);
		text_time = (TextView) findViewById(R.id.text_time);
		edit = (EditText) findViewById(R.id.edit);
		btn = (Button) findViewById(R.id.btn);
		mHandler.postDelayed(timeRunnable, 1000);// 计时
		// mHandler.postDelayed(palyRunnable, 30*1000);//播放
		// mHandler.postDelayed(stopRunnable, POST_PLAY+POST_STOP);//暂停
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				time = 0;
				int seek = Integer.parseInt(edit.getText().toString());
				
				mp.seekTo(seek * 1000);
				mp.start();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mp != null && curTime!=0) {
			Toast.makeText(this, "开始在" + mp.getCurrentPosition() / 1000 + "秒",
					Toast.LENGTH_SHORT).show();
			mp.seekTo(curTime);
			mp.start();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		
		if (mp != null) {
			curTime = mp.getCurrentPosition();
			Toast.makeText(this, "暂停在" + mp.getCurrentPosition() / 1000 + "秒",
					Toast.LENGTH_SHORT).show();
			mp.pause();
		}

	}

	/**
	 * 更新时间
	 */
	Handler viewHandler = new Handler() {
		public void handleMessage(Message msg) {
			text_time.setText("歌曲时长：" + mp.getDuration() / 1000 + "秒\n当前时间："
					+ time + "秒");
		};
	};

	/**
	 * 在线程中开启播放音乐服务
	 */
	Runnable timeRunnable = new Runnable() {

		@Override
		public void run() {

			time++;
			if (time == POST_PLAY + POST_STOP)
				mHandler.removeCallbacks(timeRunnable);
			viewHandler.handleMessage(new Message());
			mHandler.postDelayed(timeRunnable, 1000);
		}
	};

	/**
	 * 在线程中开启播放音乐服务
	 */
	Runnable palyRunnable = new Runnable() {

		@Override
		public void run() {
			Intent intent = new Intent(MainActivity.this, SoundService.class);
			intent.putExtra("playing", true);
			startService(intent);
		}
	};

	/**
	 * 在线程中开关闭播放音乐服务
	 */
	Runnable stopRunnable = new Runnable() {

		@Override
		public void run() {
			Intent intent = new Intent(MainActivity.this, SoundService.class);
			intent.putExtra("playing", false);
			startService(intent);
		}
	};

}
