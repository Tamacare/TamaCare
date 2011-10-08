package tama.care.spel;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
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
	String nameIt;
	ImageButton feedB, cleanB, playB, slapB;
	int bAction=0, bActionOnUse=0;
	int bar[]={0,8,8,8,2};	//bar[0] isn't used, bar[1] is HungryBar, bar[2] is HygienBar, bar[3] is LoyaltyBar, bar[4] is MoodBar
	ImageView statusBar, myChar;
	Handler handler = new Handler();
	int charRace = newGame.cRace;
	boolean onlyOneClick = true;
	//int gameOver=0;
	int barPointer;
	int counter=0;
	int temp=0; //FIXES THE BUG SWITCHING FROM TIMERTASK TO BUTTON ACTION
	int pukeOn3, forceIt;
	boolean timeToPuke;
	
	LinearLayout ln;
	Date dt = new Date();
	int hoursBefore, hoursNow, minutesBefore, minutesNow, timeDiff;
	
	int delay = 0; // delay for 0 sec.
	int period = 1000; // repeat every 60 sec.
	TimerTask updateThis; 
	Timer timerThis = new Timer();
	
	//autoBarIsOn is use to deactivate button action until update is completed
	boolean updateBarIsOn = false;
	
	MediaPlayer mpAction;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamescreen);
		
		//hoursBefore = dt.getHours();
	    //minutesBefore = dt.getMinutes();
		pukeOn3 = 0;
		forceIt = 0;
		timeToPuke = false;
	    
		showName = (TextView) findViewById(R.id.tvName);
		myChar = (ImageView) findViewById(R.id.ivmyChar);
		feedB = (ImageButton) findViewById(R.id.ibfeed);
		cleanB = (ImageButton) findViewById(R.id.ibclean);
		playB = (ImageButton) findViewById(R.id.ibplay);
		slapB = (ImageButton) findViewById(R.id.ibslap);
		statusBar = (ImageView) findViewById(R.id.ivHungry);
		ln = (LinearLayout) findViewById(R.id.LLGameS);
	    
		//INITIATE THE GAME!
		initGame();
		
		//This is needed for real-time update
		updateThis = new TimerTask() { 
	        public void run() { 
	        	handler.post(new Runnable() { 
	        		public void run() { 
	        			adjustBackground();
	        			counter++;
	        			if(counter == 60){
	        				//do once every 60 seconds
	        				updateTheGame();
	        				counter=0;
	        			}
	        			//TEST below
	        			//hoursBefore++;
	        		    //String curTime = hoursBefore + ":" + minutesBefore + ":" + bar[3];
	        		    //showName.setText(curTime);
	                } 
	            }); 
	    }}; 
	    //activates the update
	    timerThis.schedule(updateThis, delay, period);
	    
		myChar.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//IF AUTO DECREASE IS ACTIVE CLICKING ON CHAR PIC WILL DO NOTHING
				if(!updateBarIsOn){
					if(bAction>0){
						myChar.setClickable(false);
						onlyOneClick = false;
					}
					bActionOnUse = bAction;
	        		bAction = 0;
	        		temp = bActionOnUse;
					switch(bActionOnUse){
						case 1: //FEED
							if(bar[1] != 10){
								switchCharImage(bActionOnUse);
								moodChanger(1);
								sleepThenDo();
								forceIt = 0;
							}
							else{
								if(forceIt == 1){
									pukeOn3++;
									if(pukeOn3 == 3){
					        			timeToPuke = true;
					        		}
									if(timeToPuke){
										makeItPuke();
										pukeOn3 = 0;
										forceIt = 0;
										timeToPuke = false;
									}
									else{
										switchCharImage(bActionOnUse);
										moodChanger(2);
										sleepThenDo();
										forceIt = 0;
									}
								}
								else{
									switchCharImage(5);
									moodChanger(2);
									sleepThenDo();
								}
							}
							break;
						case 2: //CLEAN
							forceIt = 0;
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
							if(bar[3] != 10){
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
						case 4: //SLAP
							forceIt = 1;
							switchCharImage(bActionOnUse);
							moodChanger(2);
							sleepThenDo();
							break;
					}
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
	
	public void initGame(){
		//CEHCK IF GAME OVER
		checkEndAction();
		//SET NAME
		nameIt = newGame.sn;
		showName.setText(nameIt);
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
		//decrease bars
		bar[1] -= 2;
		bar[2] -= 2;
		bar[3] -= 2;
		//now change bar picture accordingly to value
		for(int i=1; i<4; i++){ 
			bActionOnUse = i;
			changeBarImage();
    	}
		bActionOnUse = temp; //this "fixes" the bug when u do button action exactly on update
		updateBarIsOn = false;
		checkEndAction();
	}
	
	public void sleepThenDo(){
		// SLEEP 2000 MILLISECONDS HERE ... 
	    handler.postDelayed(new Runnable() { 
	    	public void run() {
	    		setCharImage();
	        	if(bActionOnUse == 4){
	        		bar[3] -= 2;
	        	}
	        	else{
	        		increaseBar();
	        	}
	        	changeBarImage();
	        	myChar.setClickable(true);
	        	onlyOneClick = true;
	        	checkEndAction();
	        } 
	   }, 2000);
	}
	
	public void setCharImage(){
		//Set standard character image
		switch (charRace){
			case 1:
				myChar.setImageResource(R.drawable.neutralfluffy);
				break;
			case 2:
				myChar.setImageResource(R.drawable.redchar);
				break;
			case 3:
				myChar.setImageResource(R.drawable.yellowchar);
				break;
		}
	}
	
	public void adjustBackground(){
		
		hoursNow = dt.getHours();
		
		if(hoursNow>=6 && hoursNow<=12){
	    	ln.setBackgroundResource(R.drawable.tmorning);
	    }
	    else if(hoursNow>=12 && hoursNow<18){
	    	ln.setBackgroundResource(R.drawable.tnoon);
	    }
	    else if(hoursNow>=18 && hoursNow<24){
	    	ln.setBackgroundResource(R.drawable.tafternoon);
	    }
	    if(hoursNow>=0 && hoursNow<6){
	    	ln.setBackgroundResource(R.drawable.tnight);
	    }
	}

	public void switchCharImage(int actionNR){
	    //Switch the character image(eating, cleaning, playing,
	    //slapped) accordingly to which button is pressed
	    switch(actionNR){
	    	case 1: 
	    		//"EATING" image
	    		mpAction = MediaPlayer.create(this, R.raw.button);
	    		mpAction.start();
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.eatingfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    		}
	    		break;
	    	case 2: 
	    		//"CLEANING" image
	    		mpAction = MediaPlayer.create(this, R.raw.button);
	    		mpAction.start();
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.cleaningfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    		}
	    		break;
	    	case 3:	
	    		//"PLAYING" image
	    		mpAction = MediaPlayer.create(this, R.raw.button);
	    		mpAction.start();
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.playingfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    		}
	    		break;
	    	case 4:	
	    		//"SLAPPED" image
	    		mpAction = MediaPlayer.create(this, R.raw.button);
	    		mpAction.start();
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.slappedfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    		}
	    		break;
	    	case 5: 
	    		//"CHARACTER DENIES" image, same for all actions
	    		mpAction = MediaPlayer.create(this, R.raw.button);
	    		mpAction.start();
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.denyingfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    		}
	    		break;
	    	case 6: 
	    		//"CHARACTER PUKES" image
	    		mpAction = MediaPlayer.create(this, R.raw.button);
	    		mpAction.start();
	    		switch(charRace){
	    			case 1:
	    				myChar.setImageResource(R.drawable.pukingfluffy);
	    				break;
	    			case 2:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    			case 3:
	    				myChar.setImageResource(R.drawable.bluechar);
	    				break;
	    		}
	    		break;
	    }
	}
	
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
			case 2:
				statusBar.setImageResource(R.drawable.moodneutral);
				break;
			case 4:
				statusBar.setImageResource(R.drawable.moodhappy);
				break;
		}
	}
	
	public void makeItPuke(){
		switchCharImage(6);
		bar[4] = 0;
		moodChanger(0);
		handler.postDelayed(new Runnable() { 
			public void run() {
				setCharImage();
		        bar[1] = 4;
		        changeBarImage();
		        
		    	myChar.setClickable(true);
		    	onlyOneClick = true;
			}
		}, 2000);
	}
	
	public void increaseBar(){
		switch(bar[bActionOnUse]){
			case 0:
				bar[bActionOnUse]+=2;
				break;
			case 2:
				bar[bActionOnUse]+=2;
				break;
			case 4:
				bar[bActionOnUse]+=2;
				break;
			case 6:
				bar[bActionOnUse]+=2;
				break;
			case 8:
				bar[bActionOnUse]+=2;
				break;
		}
	}
	
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
			case 2:
				statusBar.setImageResource(R.drawable.bar20);
				break;
			case 4:
				statusBar.setImageResource(R.drawable.bar40);
				break;
			case 6:
				statusBar.setImageResource(R.drawable.bar60);
				break;
			case 8:
				statusBar.setImageResource(R.drawable.bar80);
				break;
			case 10:
				statusBar.setImageResource(R.drawable.bar100);
				break;
		}
	}
	
	public void checkEndAction(){
		if(bar[1]<=0){
			switch(charRace){
				case 1:
					myChar.setImageResource(R.drawable.deadfluffy);
					break;
				case 2:
					myChar.setImageResource(R.drawable.day1);
					break;
				case 3:
					myChar.setImageResource(R.drawable.day1);
					break;
			}
			//gameOver = 1;
			myChar.setClickable(false);
			updateThis.cancel();
		}
		else if(bar[2]<=0){
			switch(charRace){
				case 1:
					//SHOULD BE DIRTY HERE
					myChar.setImageResource(R.drawable.eatingfluffy);
					break;
				case 2:
					myChar.setImageResource(R.drawable.day1);
					break;
				case 3:
					myChar.setImageResource(R.drawable.day1);
					break;
			}
			//gameOver = 1;
			myChar.setClickable(false);
			updateThis.cancel();
		}
		else if(bar[3]<=0){
			switch(charRace){
				case 1:
					myChar.setImageResource(R.drawable.leavingfluffy);
					break;
				case 2:
					myChar.setImageResource(R.drawable.day1);
					break;
				case 3:
					myChar.setImageResource(R.drawable.day1);
					break;
			}
			//gameOver = 1;
			myChar.setClickable(false);
			updateThis.cancel();
		}
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
	}
	
}
