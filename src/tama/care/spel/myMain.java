package tama.care.spel;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;

public class myMain extends Activity {
    /** Called when the activity is first created. */
	//made to avoid problems when exiting before splash screen done
	boolean itsOnPause;
	MediaPlayer mpSplash;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        
        itsOnPause = false;
        //Start splash sound
        mpSplash = MediaPlayer.create(this, R.raw.splash);
        checkIfMuteOn();
        mpSplash.start();
        //create new thread to be able to do while
        Thread logoTimer = new Thread(){
        	public void run(){
        		try{
        			int logoTimer = 0;
        			while(logoTimer<50){
        				sleep(100);
        				logoTimer++;
        			}
        			if(!itsOnPause){
        				startActivity(new Intent("tama.care.spel.CLEARSCREEN"));
        			}
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
    //this method checks if sound was saved
    public void checkIfMuteOn(){
    	//check if mute is on, if there is no saved filed mute is off
		myGame.gameFile = getSharedPreferences(myGame.FILENAME, 0);
		myMenu.muteOn = myGame.gameFile.getBoolean("isMuteOn", false);
		//if its on mute, mute the splash screen sound
		if(myMenu.muteOn){
			mpSplash.setVolume(0.0f, 0.0f);
		}else{
			mpSplash.setVolume(1.0f, 1.0f);
		}
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
		itsOnPause = true;
		mpSplash.stop();
		finish();
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