package tama.care.spel;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class myHelp extends Activity{
	
	TextView showhelpText1;
	TextView showhelpText2;
	TextView showhelpText3;
	TextView showhelpText4;
	ImageView showPic1;
	ImageView showPic2;
	int page;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helpscreen);
		
		//Resume background sound
		myMenu.mpBackgroundSound.start();
			
		showhelpText1 = (TextView) findViewById(R.id.tvHelp1);
		showhelpText2 = (TextView) findViewById(R.id.tvHelp2);
		showhelpText3 = (TextView) findViewById(R.id.tvHelp3);
		showhelpText4 = (TextView) findViewById(R.id.tvHelp4);
		showPic1 = (ImageView) findViewById(R.id.ivHelp1);
		showPic2 = (ImageView) findViewById(R.id.ivHelp2);
		
		final Button nextB = (Button) findViewById(R.id.bNext);
		final Button backB = (Button) findViewById(R.id.bBack);
		
		page = 1;
		changePage();

		nextB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//sets the viewing page
				page++;
				//changing page with this method
				changePage();
				backB.setVisibility(0);
				//sets the next button to invisible
				if(page==3){
					nextB.setVisibility(4);
				}
			}
		});
		
		backB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				page--;
				changePage();
				if(page==1){
					backB.setVisibility(4);
				}
				if(page>=2){
					nextB.setVisibility(0);
				}
			}
		});
	}
	
	public void changePage(){
		// own method that changes the page, by using visibility
		switch(page){
			case 1:
				showhelpText1.setVisibility(0);
				showhelpText1.setText(R.string.helpText1);
				showPic1.setImageResource(R.drawable.neutralfluffy);
				showPic2.setVisibility(8);
				showhelpText2.setVisibility(8);
				showhelpText3.setVisibility(8);
				showhelpText4.setVisibility(8);
				break;
			case 2:
				
				showPic2.setVisibility(0);
				showhelpText2.setVisibility(0);
				showhelpText3.setVisibility(0);
				showhelpText2.setText(R.string.helpText2);
				showPic1.setImageResource(R.drawable.feedicon);
				showhelpText3.setText(R.string.helpText3);
				showPic2.setImageResource(R.drawable.bar60);
				showhelpText4.setVisibility(8);
				showhelpText1.setVisibility(8);
				break;
			case 3:
				showhelpText4.setVisibility(0);
				showhelpText4.setText(R.string.helpText4);
				showPic1.setImageResource(R.drawable.deadfluffy);
				showhelpText1.setVisibility(8);
				showhelpText2.setVisibility(8);
				showhelpText3.setVisibility(8);
				showPic2.setVisibility(8);
				break;
		}
	}
	
	//Fixes the bug restarting activity when screen rotates
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
