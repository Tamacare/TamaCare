package tama.care.spel;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;


public class credits extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creditsscreen);
		
		//Resume background sound
		myMenu.mpBackgroundSound.start();
	}

	//Fixes the bug restarting activity when screen rotates
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	//BRA ATT HA NÄR MAN TRYCKER PÅ TILLBAKA KNAPPEN!
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//pause background sound
		myMenu.mpBackgroundSound.pause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myMenu.mpBackgroundSound.start();
	}
}
