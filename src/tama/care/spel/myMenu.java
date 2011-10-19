package tama.care.spel;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class myMenu extends Activity {
	
	static boolean muteOn = false;
	static int updateSpeed = 60;
	
	static MediaPlayer mpbutton;
	static MediaPlayer mpBackgroundSound;
	//int variables to load in value to
	static boolean saveGameExist;
	static String savedName = "";
	static int savedRace = 0;
	static float savedAge = 0.0f;
	static int savedHungryBar = 0;
	static int savedHygienBar = 0;
	static int savedLoyaltyBar = 0;
	static int savedMoodBar = 0;
	static int savedMinute = 0;
	static int savedHour = 0;
	static int savedDay = 0;
	static int savedMonth = 0;
	static int savedYear = 0;
	//variables to handle with new time
	Date dt = new Date();
	static int hourNew, dayNew, monthNew, yearNew;
	
	Button bContinue;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuscreen);
		
		//button sound
		mpbutton = MediaPlayer.create(this, R.raw.button);
		//Background sound
		mpBackgroundSound = MediaPlayer.create(this, R.raw.bgsound);
		options.isOtherMpSoundsOnMute();
		mpBackgroundSound.start();
		mpBackgroundSound.setLooping(true);
		
		bContinue = (Button) findViewById(R.id.continue2);
		
		bContinue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getNewTimeAndDate();
				loadGame();
				myContinue.calculateTimeDiff();
				myContinue.calculateAgeAndBarValue();
				myContinue.applyChanges();
				startActivity(new Intent("tama.care.spel.THEGAME"));
				mpbutton.start();
			}
		});
		
		Button bNewGame = (Button) findViewById(R.id.newgame1);
		
		bNewGame.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//is there is already a saved game, ask if user wants to delete it and create new game
				if(saveGameExist){
					startActivity(new Intent("tama.care.spel.Over"));
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
	//get current time (new)
	public void getNewTimeAndDate(){
		hourNew = dt.getHours();
		dayNew = dt.getDate();
		monthNew = dt.getMonth()+1;
		yearNew = dt.getYear();
	}
	//this method checks if there is a saved game
	public void checkIfGameSaved(){
		myGame.gameFile = getSharedPreferences(myGame.FILENAME, 0);
		saveGameExist = myGame.gameFile.getBoolean("isSaveGame", false);
		//CHECK IF SAVE GAME EXIST
		if(saveGameExist){
			//set the continue button visible (is not visible for the first time since there is no saved game)
			bContinue.setVisibility(0);
		}else{
			//else set invisible
			bContinue.setVisibility(4);
		}
	}
	//Retrieve saved game
	public void loadGame(){
		savedName = myGame.gameFile.getString("characerName", "DEFAULT");
		savedRace = myGame.gameFile.getInt("characterRace", 0);
		savedAge = myGame.gameFile.getFloat("characterAge", -1);;
		savedHungryBar = myGame.gameFile.getInt("barHungry", -1);
		savedHygienBar = myGame.gameFile.getInt("barHygien", -1);
		savedLoyaltyBar = myGame.gameFile.getInt("barLoyalty", -1);
		savedMoodBar = myGame.gameFile.getInt("barMood", -1);
		savedMinute = myGame.gameFile.getInt("minute", -1);
		savedHour = myGame.gameFile.getInt("hour", -1);
		savedDay = myGame.gameFile.getInt("day", -1);
		savedMonth = myGame.gameFile.getInt("month", -1);
		savedYear = myGame.gameFile.getInt("year", -1);
		updateSpeed = myGame.gameFile.getInt("updateSpeed", 60);
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
		myGame.saveOptValues();
		finish();
	}
}
