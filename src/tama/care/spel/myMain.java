package tama.care.spel;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;

public class myMain extends Activity {
    /** Called when the activity is first created. */
	
	MediaPlayer mpSplash;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        
        mpSplash = MediaPlayer.create(this, R.raw.splash);
        mpSplash.start();
        
        
        Thread logoTimer = new Thread(){
        	public void run(){
        		try{
        			int logoTimer = 0;
        			while(logoTimer<50){
        				sleep(100);
        				logoTimer++;
        		}
        		startActivity(new Intent("tama.care.spel.CLEARSCREEN"));
        		}
        		catch (InterruptedException e) {
        			e.printStackTrace();
        		}
        		
        		finally{
        			mpSplash.stop();
        			finish();
        		}
        	}
        };
        
        logoTimer.start();
        
        
    }
    
    //Fixes the bug restarting activity when screen rotates
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
    
    
}