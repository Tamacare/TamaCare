package tama.care.spel;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class myMenu extends Activity {
	
	MediaPlayer mpbutton;
	static MediaPlayer mpBackgroundSound;
	static boolean saveGameExist;
	static String savedName = "";
	static int savedRace = 0;
	Button bContinue;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuscreen);
		
		mpbutton = MediaPlayer.create(this, R.raw.button);
		//Background sound
		mpBackgroundSound = MediaPlayer.create(this, R.raw.bgsound);
		mpBackgroundSound.start();
		mpBackgroundSound.setLooping(true);
		
		bContinue = (Button) findViewById(R.id.continue2);
		
		bContinue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadGame();
				startActivity(new Intent("tama.care.spel.THEGAME"));
				mpbutton.start();
			}
		});
		
		Button bNewGame = (Button) findViewById(R.id.newgame1);
		
		bNewGame.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(saveGameExist){
					myGame.deleteSaveGame();
					checkIfGameSaved();
					//startActivity(new Intent("tama.care.spel.STARTOVER"));
				}
				else{
					startActivity(new Intent("tama.care.spel.NewGame"));
				}
				mpbutton.start();
			}
		});
				
		Button bOptions = (Button) findViewById(R.id.options3);
		
		bOptions.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("tama.care.spel.Options"));
				mpbutton.start();
			}
		});
				
		Button bHelp = (Button) findViewById(R.id.help4);
		
		bHelp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("tama.care.spel.Help"));
				mpbutton.start();
			}
		});
				
		Button bCredits = (Button) findViewById(R.id.credits5);
		
		
		bCredits.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("tama.care.spel.Credits"));
				mpbutton.start();
			}
		});
				
		Button bExit = (Button) findViewById(R.id.exit6);
		
		bExit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mpbutton.start();
				onDestroy();
			}
		});
		
	}
	
	public void checkIfGameSaved(){
		myGame.gameFile = getSharedPreferences(myGame.FILENAME, 0);
		saveGameExist = myGame.gameFile.getBoolean("isSaveGame", false);
		//CHECK IF SAVE GAME EXIST
		if(saveGameExist){
			bContinue.setVisibility(0);
		}else{
			bContinue.setVisibility(4);
		}
	}
	
	public void loadGame(){
		savedName = myGame.gameFile.getString("characerName", "DEFAULT");
		savedRace = myGame.gameFile.getInt("characterRace", 0);
	}
	
	//Fixes the bug restarting activity when screen rotates
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
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
		checkIfGameSaved();
		myMenu.mpBackgroundSound.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mpBackgroundSound.stop();
		finish();
	}
	
}
