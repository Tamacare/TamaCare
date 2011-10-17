package tama.care.spel;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class myStartoverscreen extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startoverscreen);
		
		//Resume background sound
		myMenu.mpBackgroundSound.start();
		
		Button SYes = (Button) findViewById(R.id.bYes);
		Button SNo = (Button) findViewById(R.id.bNo);
			
		SYes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//sets the viewing page
				myGame.deleteSaveGame();
				myMenu.saveGameExist = false;
				startActivity(new Intent("tama.care.spel.NewGame"));
				finish();
			}
		});
			
		SNo.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//sets the viewing page
				finish();			
			}
		});
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
		myMenu.mpBackgroundSound.pause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myMenu.mpBackgroundSound.start();
	}
}


