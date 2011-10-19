package tama.care.spel;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class newGame extends Activity implements OnCheckedChangeListener{

	RadioGroup charColor;
	ImageView charPic;
	static String sn;
	EditText writeName;
	static int cRace;
	TextView showErrorMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createscreen);
		
		myMenu.mpBackgroundSound.start();
		
		cRace = 0;
		sn = "";
		
		charColor = (RadioGroup) findViewById(R.id.rgRace);
		charColor.setOnCheckedChangeListener(this);
		charPic = (ImageView) findViewById(R.id.ivSelectChar);
		
		showErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
		
		writeName = (EditText) findViewById(R.id.etName);
		
		Button bCan = (Button) findViewById(R.id.bCancel);
		bCan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//if cancel pressed return to menu screen
				onDestroy();
			}
		});
		
		Button bCre = (Button) findViewById(R.id.bCreate);
		bCre.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//get entered name from edit box and put it in string sn
				sn = writeName.getText().toString();
				//Don't create new game unless user has entered both name and race
				if(sn.equalsIgnoreCase("") && cRace == 0){
					showErrorMsg.setText(R.string.errorNameRace);
				}
				else{
					if(!sn.equalsIgnoreCase("")){
						if(cRace != 0){
							startActivity(new Intent("tama.care.spel.THEGAME"));
						}
						else{
							showErrorMsg.setText(R.string.errorRace);
						}
					}
					else{
						showErrorMsg.setText(R.string.errorName);
					}
				}
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		//show the char image accordingly to pick at race
		switch (arg1){
			case R.id.rbFluffy:
				charPic.setImageResource(R.drawable.neutralfluffy);
				cRace=1;
				break;
			case R.id.rbSpiky:
				charPic.setImageResource(R.drawable.neutralspiky);
				cRace=2;
				break;
			case R.id.rbDrako:
				charPic.setImageResource(R.drawable.neutraldrako);
				cRace=3;
				break;
		}
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
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}
	
}
