package tama.care.spel;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import tama.care.spel.newGame;

public class myGame extends Activity{
	
	TextView showName;
	TextView showAge;
	static String nameIt = "";
	static int charRace;
	//character age
	static float charAge;
	ImageButton feedB, cleanB, playB, slapB;
	int bAction=0, bActionOnUse=0;
	static int bar[]={0,5,5,5,2};	//bar[0] isn't used, bar[1] is HungryBar, bar[2] is HygienBar, bar[3] is LoyaltyBar, bar[4] is MoodBar
	ImageView statusBar, myChar;
	Handler handler = new Handler();
	boolean onlyOneClick = true;
	int counter=0;
	int temp=0; //FIXES THE BUG SWITCHING FROM TIMERTASK TO BUTTON ACTION
	int pukeOn3, forceIt;
	boolean timeToPuke;
	int inactiveCount; //inactiveCount is the counter for sleeping switch image
	boolean isCharNeutral; //checks if neutral image is already set
	boolean gameIsPaused; //See if game is paused
	boolean gameOver;
	
	LinearLayout ln; //Needed for background change
	//variables to handle with time
	Date dt = new Date();
	static int hourOld, dayOld, monthOld, yearOld;
	
	int delay = 0; // delay for 0 sec.
	int period = 1000; // repeat every 60 sec.
	TimerTask updateThis; 
	Timer timerThis = new Timer();
	
	//autoBarIsOn is use to deactivate button action until update is completed
	boolean updateBarIsOn = false;
	
	//MediaPlayer mpAction;
	static MediaPlayer mpAction;
	
	public static final String FILENAME = "TamaCareSaveGame";
	public static SharedPreferences gameFile;
	static SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamescreen);
		
		//REFERENCE TO SAVE FILE
		gameFile = getSharedPreferences(FILENAME, 0);
		
		//Resume background sound
		myMenu.mpBackgroundSound.start();
		
		//Fixes mpAction bug when going onPause directly after create
		mpAction = MediaPlayer.create(this, R.raw.button);
		
		gameIsPaused = false;
		pukeOn3 = 0;
		forceIt = 0;
		timeToPuke = false;
		inactiveCount = 0;
		counter=0;
		temp=0;
	    
		showName = (TextView) findViewById(R.id.tvName);
		myChar = (ImageView) findViewById(R.id.ivmyChar);
		feedB = (ImageButton) findViewById(R.id.ibfeed);
		cleanB = (ImageButton) findViewById(R.id.ibclean);
		playB = (ImageButton) findViewById(R.id.ibplay);
		slapB = (ImageButton) findViewById(R.id.ibslap);
		statusBar = (ImageView) findViewById(R.id.ivHungry);
		ln = (LinearLayout) findViewById(R.id.LLGameS);
		showAge = (TextView) findViewById(R.id.tvAge);
		
		//INITIATE THE GAME!
		initGame();
		
		//This is needed for real-time update (TIMER)
		updateThis = new TimerTask() { 
	        public void run() { 
	        	handler.post(new Runnable() { 
	        		public void run() { 
	        			adjustBackground();
	        			counter++;
	        			if(counter == myMenu.updateSpeed){
	        				//do once every x seconds(x = 60 for normal and 3600 for slow)
	        				updateTheGame();
	        				counter = 0;
	        			}
	        			//character sleeps if inactive for 30 sec
	        			inactiveCount++;
		        		if(inactiveCount == 30 && isCharNeutral && !gameIsPaused){
		        			switchCharImage(7);
		        			isCharNeutral = false;
		        		}
	                } 
	            }); 
	    }}; 
	    //activates the timer
	    timerThis.schedule(updateThis, delay, period);
	    //onClick for character image
		myChar.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//IF AUTO DECREASE IS ACTIVE CLICKING ON CHAR PIC WILL DO NOTHING
				if(!updateBarIsOn){
					if(bAction>0){
						//by setting to false, user can't perform another action while on is already active 
						myChar.setClickable(false);
						onlyOneClick = false;
					}
					bActionOnUse = bAction;
	        		bAction = 0;
	        		temp = bActionOnUse;
	        		//if character sleep image
	        		if(!isCharNeutral){
	        			int afterSleepDelay;
	        			if(bActionOnUse == 4){
	        				afterSleepDelay = 0;
	        			}
	        			else{
	        				afterSleepDelay = 300;
	        			}
	        			//Stop sleeping sound
	        			mpAction.reset();
	    				//If hygiene bar is 0 after wake up show not change back to stinky instead of neutral
	    				if(bar[2] == 0){
	    					checkEndAction();
	    				}else{
	    					//set back neutral character image
		        			setCharImage();
	    				}
	    				//Do the button action
	    				//if before char was sleep, delay 300ms
    				    handler.postDelayed(new Runnable() { 
    						public void run() {
    							OnCharClickOpt();
    						}
    					}, afterSleepDelay);
	        		}
	        		//Do the button action
    				//if char was neutral
	        		else{
	        			OnCharClickOpt();
	        		}
	        		inactiveCount = 0;
				}
			}
		});
			
		feedB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onlyOneClick){
					bAction=1;
				}
			}
		});
		
		cleanB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onlyOneClick){
					bAction=2;
				}
			}
		});
		
		playB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onlyOneClick){
					bAction=3;
				}
			}
		});
		
		slapB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onlyOneClick){
					bAction=4;
				}
			}
		});
		
	}
	//INIT the game w/o saved game
	public void initGame(){
		//game is started
		gameOver = false;
		//check if there is a save game, load it if there is one
		if(myMenu.saveGameExist){
			handler.postDelayed(new Runnable() { 
		    	public void run() {
		    		//Check endAction with the new values
		        	checkEndAction();
		        } 
		   }, 0);
		}
		else{
			//Prepare for a new game
			//SET NAME, RACE, BIRTH AGE
			nameIt = newGame.sn;
			charRace = newGame.cRace;
			charAge = 1.0f;
			//set bar
			bar[1] = 5;
			bar[2] = 5;
			bar[3] = 5;
			bar[4] = 2;
		}
		
		//SET NAME to screen
		showName.setText(nameIt);
		if(gameOver){
			//Show age last time it was alive
			showAge.setText("" + (int)myMenu.savedAge);
		}
		else{
			//Show birth/new age
			showAge.setText("" + (int)charAge);
		}
		//SET BACKGROUND IMAGE
		adjustBackground();
		//SET MOOD PICTURE
		moodChanger(0);
		//ADJUST THE BARS PICTURES
		for(int i=1; i<4; i++){
			bActionOnUse = i;
			changeBarImage();
    	}
		//SET CHARACTER IMAGE
		setCharImage();
	}
	
	public void updateTheGame() {
		// TODO Auto-generated method stub
		updateBarIsOn = true;
		//decrease bars value
		bar[1]--;
		if(bar[2] != 0){
			bar[2]--;
		}
		bar[3]--;
		//now change bar picture accordingly to value
		for(int i=1; i<4; i++){ 
			bActionOnUse = i;
			changeBarImage();
    	}
		bActionOnUse = temp; //this "fixes" the bug when u do button action exactly on update
		//reset pukeOn3
		pukeOn3 = 0;
		checkEndAction();
		updateBarIsOn = false;
	}
	//The button action method
	public void OnCharClickOpt(){
		//bactionOnUse is set to which button is pressed (activated)
		switch(bActionOnUse){
			case 1: //FEED
				if(bar[1] != 10){
					switchCharImage(bActionOnUse);
					moodChanger(1); //(=1 increase mood)
					sleepThenDo(); //the method that will change back char image, see method)
					forceIt = 0;
				}
				//Feed it when hungry bar = 10 only if forced (slap to force)
				else{
					if(forceIt == 1){
						//increase counter for puke
						pukeOn3++;
						if(pukeOn3 == 3){
		        			timeToPuke = true;
		        		}
						if(timeToPuke){
							//run the method to puke
							makeItPuke();
							//reset counter
							pukeOn3 = 0;
							//not forced any more
							forceIt = 0;
							timeToPuke = false;
						}
						//if not time to puke feed it more
						else{
							switchCharImage(bActionOnUse);
							moodChanger(2); //(=2 decrease mood)
							sleepThenDo();
							forceIt = 0;
						}
					}
					//if hungry bar full and not force deny to eat
					else{
						switchCharImage(5);
						moodChanger(2);
						sleepThenDo();
					}
				}
				break;
			case 2: //CLEAN
				forceIt = 0;
				//if hygiene bar not full clean it else deny
				if(bar[2] != 10){
					switchCharImage(bActionOnUse);
					moodChanger(1);
					sleepThenDo();
				}
				else{
					switchCharImage(5);
					moodChanger(2);
					sleepThenDo();
				}
				break;
			case 3: //PLAY
				forceIt = 0;
				//if loyalty bar not full (=10) play else deny
				if(bar[3] != 10){
					switchCharImage(bActionOnUse);
					moodChanger(1);
					sleepThenDo();
				}
				else{
					switchCharImage(5); //(=5 is for deny, see method)
					moodChanger(2);
					sleepThenDo();
				}
				break;
			case 4: //SLAP
				forceIt = 1;
				//simply slap it
				switchCharImage(bActionOnUse);
				moodChanger(2);
				sleepThenDo();
				break;
		}
	}
	
	public void sleepThenDo(){
		// SLEEP 2000 MILLISECONDS HERE ... 
	    handler.postDelayed(new Runnable() { 
	    	public void run() {
	    		//stop button(action) sound
	    		mpAction.reset();
	    		//set neutral char image
	    		setCharImage();
	    		//decrease loyalty bar if slapped
	        	if(bActionOnUse == 4){
	        		bar[3]--;
	        	}
	        	//else go to following method to see which bar to increase
	        	else{
	        		increaseBar();
	        	}
	        	//change bar image accordingly to bar value
	        	changeBarImage();
	        	//make char image clickable
	        	myChar.setClickable(true);
	        	onlyOneClick = true;
	        	checkEndAction();
	        } 
	   }, 2000);
	}
	
	public void setCharImage(){
		//Set standard character image accordingly to race
		switch (charRace){
			case 1:
				myChar.setImageResource(R.drawable.neutralfluffy);
				break;
			case 2:
				myChar.setImageResource(R.drawable.neutralspiky);
				break;
			case 3:
				myChar.setImageResource(R.drawable.neutraldrako);
				break;
		}
		isCharNeutral = true;
	}
	
	//method to adjust background image to current time
	public void adjustBackground(){
		hourOld = dt.getHours();
		
		if(hourOld>=4 && hourOld<=9){
	    	ln.setBackgroundResource(R.drawable.tmorning);
	    }
	    else if(hourOld>=10 && hourOld<=15){
	    	ln.setBackgroundResource(R.drawable.tnoon);
	    }
	    else if(hourOld>=16 && hourOld<=21){
	    	ln.setBackgroundResource(R.drawable.tafternoon);
	    }
	    if((hourOld>=22 && hourOld<=23) || (hourOld>=0 && hourOld<=3)){
	    	ln.setBackgroundResource(R.drawable.tnight);
	    }
	}

	public void switchCharImage(int actionNR){
	    //Switch the character image(eating, cleaning, playing,
	    //slapped) accordingly to which button is pressed
		//also accordingly to race
	    switch(actionNR){
	    	case 1: 
	    		//"EATING" image
	    		mpAction = MediaPlayer.create(this, R.raw.eatingsound);
	    		//check this method before playing
	    		//if game on mute there wont be any no sound
	    		options.isMpActionOnMute(1);
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.eatingfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.eatingspiky);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.eatingdrako);
	    				break;
	    		}
	    		break;
	    	case 2: 
	    		//"CLEANING" image
	    		mpAction = MediaPlayer.create(this, R.raw.cleaningsound);
	    		//check this method before playing
	    		//if game on mute there wont be any no sound
	    		options.isMpActionOnMute(1);
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.cleaningfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.cleaningspiky);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.cleaningdrako);
	    				break;
	    		}
	    		break;
	    	case 3:	
	    		//"PLAYING" image
	    		mpAction = MediaPlayer.create(this, R.raw.playingsound);
	    		//check this method before playing
	    		//if game on mute there wont be any no sound
	    		options.isMpActionOnMute(1);
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.playingfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.playingspiky);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.playingdrako);
	    				break;
	    		}
	    		break;
	    	case 4:	
	    		//"SLAPPED" image
	    		mpAction = MediaPlayer.create(this, R.raw.slapingsound);
	    		//check this method before playing
	    		//if game on mute there wont be any no sound
	    		options.isMpActionOnMute(1);
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.slappedfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.slappedspiky);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.slappeddrako);
	    				break;
	    		}
	    		break;
	    	case 5: 
	    		//"CHARACTER DENIES" image, same for all actions
	    		mpAction = MediaPlayer.create(this, R.raw.denysound);
	    		//check this method before playing
	    		//if game on mute there wont be any no sound
	    		options.isMpActionOnMute(1);
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.denyingfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.denyingspiky);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.denyingdrako);
	    				break;
	    		}
	    		break;
	    	case 6: 
	    		//"CHARACTER PUKES" image
	    		mpAction = MediaPlayer.create(this, R.raw.pukesound);
	    		//check this method before playing
	    		//if game on mute there wont be any no sound
	    		options.isMpActionOnMute(1);
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.pukingfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.pukingspiky);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.pukingdrako);
	    				break;
	    		}
	    		break;
	    	case 7:
	    		//"CHARACTER SLEEPS" image
	    		mpAction = MediaPlayer.create(this, R.raw.sleepingsound);
	    		//check this method before playing
	    		//if game on mute there wont be any no sound
	    		options.isMpActionOnMute(1);
	    		mpAction.setLooping(true);
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.sleepingfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.sleepingspiky);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.sleepingdrako);
	    				break;
	    		}
	    		isCharNeutral = false;
	    		break;
	    }
	}
	//handles the mood bar on game screen
	public void moodChanger(int mood){
		statusBar = (ImageView) findViewById(R.id.ivMood);
		if(mood == 1 && bar[4] >=0 && bar[4] <4){
			bar[4]++;
		}
		else if(mood == 2 && bar[4] <=4 && bar[4] >0){
			bar[4]--;
		}
		switch(bar[4]){
			case 0:
				statusBar.setImageResource(R.drawable.moodangry);
				break;
			case 1:
				statusBar.setImageResource(R.drawable.moodangry);
				break;
			case 2:
				statusBar.setImageResource(R.drawable.moodneutral);
				break;
			case 3:
				statusBar.setImageResource(R.drawable.moodneutral);
				break;
			case 4:
				statusBar.setImageResource(R.drawable.moodhappy);
				break;
		}
	}
	//the puke method, if char force to eat 3 time this method is called
	public void makeItPuke(){
		switchCharImage(6);
		//set mood to angry
		bar[4] = 0;
		//change bar picture accordingly to value
		moodChanger(0); //(=0 only adjust image)
		handler.postDelayed(new Runnable() { 
			public void run() {
				mpAction.reset();
				//If hygiene bar is 0 after wake up show not change back to stinky instead of neutral
				if(bar[2] == 0){
					//if char stinky then change back to stinky char image and not the neutral
					checkEndAction();
				}else{
					//set back neutral character image
        			setCharImage();
				}
				//decrease hungry bar to 4
		        bar[1] = 4;
		        changeBarImage();
		        
		    	myChar.setClickable(true);
		    	onlyOneClick = true;
			}
		}, 2000);
	}
	//the mthod that increases bar value according ly to action
	public void increaseBar(){
		switch(bar[bActionOnUse]){
			case 0:
				bar[bActionOnUse]+=1;
				break;
			case 1:
				bar[bActionOnUse]+=1;
				break;
			case 2:
				bar[bActionOnUse]+=1;
				break;
			case 3:
				bar[bActionOnUse]+=1;
				break;
			case 4:
				bar[bActionOnUse]+=1;
				break;
			case 5:
				bar[bActionOnUse]+=1;
				break;
			case 6:
				bar[bActionOnUse]+=1;
				break;
			case 7:
				bar[bActionOnUse]+=1;
				break;
			case 8:
				bar[bActionOnUse]+=1;
				break;
			case 9:
				bar[bActionOnUse]+=1;
				break;
		}
	}
	//the method that adjusts bar image (not mood bar)
	public void changeBarImage(){
		if(bActionOnUse == 4){
			bActionOnUse = 3;
		}
		switch(bActionOnUse){
			case 1:
				statusBar = (ImageView) findViewById(R.id.ivHungry);
				break;
			case 2:
				statusBar = (ImageView) findViewById(R.id.ivHygien);
				break;
			case 3:
				statusBar = (ImageView) findViewById(R.id.ivLoyalty);
				break;
		}
		switch(bar[bActionOnUse]){
			case 0:
				statusBar.setImageResource(R.drawable.bar0);
				break;
			case 1:
				statusBar.setImageResource(R.drawable.bar10);
				break;
			case 2:
				statusBar.setImageResource(R.drawable.bar20);
				break;
			case 3:
				statusBar.setImageResource(R.drawable.bar30);
				break;
			case 4:
				statusBar.setImageResource(R.drawable.bar40);
				break;
			case 5:
				statusBar.setImageResource(R.drawable.bar50);
				break;
			case 6:
				statusBar.setImageResource(R.drawable.bar60);
				break;
			case 7:
				statusBar.setImageResource(R.drawable.bar70);
				break;
			case 8:
				statusBar.setImageResource(R.drawable.bar80);
				break;
			case 9:
				statusBar.setImageResource(R.drawable.bar90);
				break;
			case 10:
				statusBar.setImageResource(R.drawable.bar100);
				break;
		}
	}
	//This method checks if character is dead,leaving or stinky
	public void checkEndAction(){
		if(bar[1]<=0){
			if(!isCharNeutral){
				mpAction.reset();
			}
			mpAction = MediaPlayer.create(this, R.raw.deadsound);
			myMenu.mpBackgroundSound.pause();
			//check this method before playing
			//if game on mute there wont be any no sound
    		options.isMpActionOnMute(1);
			switch(charRace){
				case 1:
					myChar.setImageResource(R.drawable.deadfluffy);
					break;
				case 2:
					myChar.setImageResource(R.drawable.deadspiky);
					break;
				case 3:
					myChar.setImageResource(R.drawable.deaddrako);
					break;
			}
			//if dead gameover
			gameOver = true;
		}
		else if(bar[2]<=0){
			if(!isCharNeutral){
				mpAction.reset();
			}
			//if stinky, game not over, just change char image
			//and continue with that stinky image until hygiene goes >0
			switch(charRace){
				case 1:
					myChar.setImageResource(R.drawable.stinkyfluffy);
					break;
				case 2:
					myChar.setImageResource(R.drawable.stinkyspiky);
					break;
				case 3:
					myChar.setImageResource(R.drawable.stinkydrako);
					break;
			}
		}
		else if(bar[3]<=0){
			if(!isCharNeutral){
				mpAction.reset();
			}
			mpAction = MediaPlayer.create(this, R.raw.leavingsound);
			myMenu.mpBackgroundSound.pause();
			//check this method before playing
			//if game on mute there wont be any no sound
    		options.isMpActionOnMute(1);
			switch(charRace){
				case 1:
					myChar.setImageResource(R.drawable.leavingfluffy);
					break;
				case 2:
					myChar.setImageResource(R.drawable.leavingspiky);
					break;
				case 3:
					myChar.setImageResource(R.drawable.leavingdrako);
					break;
			}
			//if leaving game over
			gameOver = true;
		}
		//if game over do this
		if(gameOver){
			myChar.setClickable(false);
			updateThis.cancel();
			//delete saved file
			deleteSaveGame();
			//delay for 10 sec to make the user realize its over
			handler.postDelayed(new Runnable() { 
				public void run() {
					mpAction.reset();
					myMenu.mpBackgroundSound.start();
					//finish the gamescreen, go to menu screen
					finish();
				}
			}, 10000);
		}
	}
	//get the current time
	public void getTimeAndDate(){
		hourOld = dt.getHours();
		dayOld = dt.getDate();
		monthOld = dt.getMonth()+1;
		yearOld = dt.getYear();
	}

	//Fixes the bug restarting activity when screen rotates
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	//save variables that is necessary to continue game
	public void saveGame(){
		editor = gameFile.edit();
		editor.putBoolean("isSaveGame", true);
		editor.putString("characerName", newGame.sn);
		editor.putInt("characterRace", newGame.cRace);
		editor.putFloat("characterAge", charAge);
		editor.putInt("barHungry", myGame.bar[1]);
		editor.putInt("barHygien", myGame.bar[2]);
		editor.putInt("barLoyalty", myGame.bar[3]);
		editor.putInt("barMood", myGame.bar[4]);
		editor.putInt("hour", hourOld);
		editor.putInt("day", dayOld);
		editor.putInt("month", monthOld);
		editor.putInt("year", yearOld);
		editor.commit();
	}
	//save opt values to file
	public static void saveOptValues(){
		editor = gameFile.edit();
		editor.putBoolean("isMuteOn", myMenu.muteOn);
		editor.putInt("updateSpeed", myMenu.updateSpeed);
		editor.commit();
	}
	//delete method
	public static void deleteSaveGame(){
		editor = myGame.gameFile.edit();
		editor.clear();
		editor.commit();
	}
	//onpause is called if u press back or home button on phone
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//stop sound
		mpAction.reset();
		super.onPause();
		gameIsPaused = true;
		//stop sound
		myMenu.mpBackgroundSound.pause();
		saveOptValues();
		//if game over and user didn't want to wait the 10 sec the game finishes.
		if(gameOver){
			myMenu.mpBackgroundSound.start();
			finish();
		}
		//else just get current time and save
		else{
			getTimeAndDate();
			saveGame();
			updateThis.cancel();
		}
	}
	//Is called when return the game screen
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		gameIsPaused = false;
		myMenu.mpBackgroundSound.start();
	}
	
}
