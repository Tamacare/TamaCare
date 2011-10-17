package tama.care.spel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class options extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optionsscreen);
		
		//Resume background sound
		myMenu.mpBackgroundSound.start();
		
		Button soundBon = (Button) findViewById(R.id.soundBon);
		Button soundBoff = (Button) findViewById(R.id.soundBoff);
		
		Button usBslow = (Button) findViewById(R.id.updatespeedBslow);
		Button usBnormal = (Button) findViewById(R.id.updatespeedBnormal);
		
		
		soundBon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//sets the viewing page
				myMenu.muteOn = false;
				isOtherMpSoundsOnMute();
			}
		});

		
		soundBoff.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//sets the viewing page
				myMenu.muteOn = true;
				isOtherMpSoundsOnMute();
			}
		});

		
		usBslow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//sets the viewing page
				myMenu.updateSpeed = 3600;				
								
			}
		});
		
		usBnormal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//sets the viewing page
				myMenu.updateSpeed = 60;
			}
		});
		
	}
	
	public static void isMpActionOnMute(int playIt){
		if(myMenu.muteOn){
			myGame.mpAction.setVolume(0.0f, 0.0f);
		}
		else{
			myGame.mpAction.setVolume(1.0f, 1.0f);
		}
		if(playIt == 1){
			myGame.mpAction.start();
		}
	}
	
	public static void isOtherMpSoundsOnMute(){
		if(myMenu.muteOn){
			//Set volume off (0) 
			myMenu.mpBackgroundSound.setVolume(0.0f, 0.0f);
			myMenu.mpbutton.setVolume(0.0f, 0.0f);
		}
		else{
			//Set volume on
			myMenu.mpBackgroundSound.setVolume(0.5f, 0.5f);
			myMenu.mpbutton.setVolume(1.0f, 1.0f);
		}
	}
}
