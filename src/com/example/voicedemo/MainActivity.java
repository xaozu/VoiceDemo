package com.example.voicedemo;

import com.example.voicedemo.service.SoundService;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


/**
 * 延迟30秒自动播放音乐
 * @author xaozu
 *
 */
public class MainActivity extends ActionBarActivity {

	public Handler mHandler=new Handler();
	private static final long POST_PLAY=30*1000;//延迟30秒播放音乐
	private static final long POST_STOP=60*10000;//播放1分钟停止
	private TextView text_time;
	private int time=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        text_time=(TextView)findViewById(R.id.text_time);
        
        mHandler.postDelayed(timeRunnable, 1000);//计时
        mHandler.postDelayed(palyRunnable, 30*1000);//播放
        mHandler.postDelayed(stopRunnable, POST_PLAY+POST_STOP);//暂停
        
    }
    /**
     * 更新时间
     */
    Handler viewHandler=new Handler(){
    	public void handleMessage(Message msg) {
    			text_time.setText(time+"秒");
    	};
    };
    
    /**
     * 在线程中开启播放音乐服务
     */
    Runnable timeRunnable=new Runnable() {
		
		@Override
		public void run() {
			
			time++;
			if(time==POST_PLAY+POST_STOP)
				mHandler.removeCallbacks(timeRunnable);
			viewHandler.handleMessage(new Message());
			mHandler.postDelayed(timeRunnable, 1000);
		}
	};
    
    
    /**
     * 在线程中开启播放音乐服务
     */
    Runnable palyRunnable=new Runnable() {
		
		@Override
		public void run() {
			Intent intent = new Intent(MainActivity.this,SoundService.class);
            intent.putExtra("playing", true);
            startService(intent);
		}
	};
	
	 /**
     * 在线程中开关闭播放音乐服务
     */
    Runnable stopRunnable=new Runnable() {
		
		@Override
		public void run() {
			Intent intent = new Intent(MainActivity.this,SoundService.class);
            intent.putExtra("playing", false);
            startService(intent);
		}
	};
    
	
}
