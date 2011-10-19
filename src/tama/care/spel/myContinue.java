package tama.care.spel;


public class myContinue{
	
	static float newAge = 0.0f;
	static int newHungryBar = 0;
	static int newHygienBar = 0;
	static int newLoyaltyBar = 0;
	static int newMoodBar = 0;
	static int diffHour = 0;
	static int diffDay = 0;
	static int diffMonth = 0;
	static int diffYear = 0;
	static int diffTotalHours = 0;
	static boolean somethingIsNegativeOrZero;
	static int monthWithTotalDay[] = {0,31,28,31,30,31,30,31,31,30,31,30,31};
	static int dBOANM = 0;
	
	//Calculate hour difference between last time active and recent time
	public static void calculateTimeDiff(){
		//New is current time and saved is the saved time from last time.
		diffHour = myMenu.hourNew - myMenu.savedHour;
		diffDay = myMenu.dayNew - myMenu.savedDay;
		diffMonth = myMenu.monthNew - myMenu.savedMonth;
		diffYear = myMenu.yearNew - myMenu.savedYear;
		
		//Check if any is negative or 0, then put true or false
		if((diffHour<=0) || (diffDay<=0) || (diffMonth<=0)){
			somethingIsNegativeOrZero = true;
		}
		
		//if no one is negative or 0, then there is no need to do following if equations.
		if(somethingIsNegativeOrZero){
			//sub "higher" value to "lower" value. Year = highest value (can't become negative) and hour = lowest value.
			//check if diffMonth is negative or 0.
			if(diffMonth <= 0){
				if(diffYear > 0){
					diffYear--;
					diffMonth += 12;
				}
			}
			
			//check if diffDay is negative or 0.
			if(diffDay <= 0){
				if(diffMonth != 0){
					diffMonth--;
					diffDay += dayAddition();
					myMenu.savedMonth++;
				}
			}
			
			//check if diffHour is negative or 0.
			if(diffHour <= 0){
				if(diffDay != 0){
					diffDay--;
					diffHour += 24;
				}
			}
		}
		//add all together and get hour diff.
		//different for when month= 0 to avoid time bug
		if(diffMonth == 0){
			diffTotalHours = diffHour + 24*diffDay + 24*365*diffYear;
		}
		else{
			diffTotalHours = diffHour + 24*diffDay + 24*daysBetweenOldAndNewMonth() + 24*365*diffYear;
		}
	}
	//Method that returns how many days is in that month.
	public static int dayAddition(){
		for(int i = 1; i<=12; i++){
			if(myMenu.savedMonth == i){
				return monthWithTotalDay[i];
			}
		}
		return 0;
	}
	//Calculates how many days between last times month and current month
	public static int daysBetweenOldAndNewMonth(){
		//reset dBOANM
		dBOANM = 0;
		//Following if:s is to avoid bugs such as ex. new month = march old= october
		//do this if, when old is a lower month than new month in same year
		if(myMenu.savedMonth < myMenu.monthNew){
			for(int j = (myMenu.savedMonth); j<myMenu.monthNew; j++){
				dBOANM += monthWithTotalDay[j];
			}
			return dBOANM;
		}
		//do this if, when old is a larger month (nr) than new (ex. this may happen when old year is 2010 and new year is 2011)
		else if(myMenu.savedMonth > myMenu.monthNew){
			for(int j = (myMenu.monthNew-1); j<myMenu.savedMonth; j--){
				dBOANM += monthWithTotalDay[j];
				if(j == 1){
					break;
				}
			}
			for(int j = (myMenu.savedMonth); j<=12; j++){
				dBOANM += monthWithTotalDay[j];
			}
			return dBOANM;
		}
		//do this if its exactly 1 year ago
		else{
			return 365;
		}
	}
	//this method calculates how much bars will decrease since last this, and how much age will increase
	public static void calculateAgeAndBarValue(){
		newAge = myMenu.savedAge + diffTotalHours/24;
		//slow speed
		//-1 / 5hours for bars
		//-1 / 10 hours for mood bar
		if(myMenu.updateSpeed == 3600){
			newHungryBar = myMenu.savedHungryBar + (-1*diffTotalHours/5);
			newHygienBar = myMenu.savedHygienBar + (-1*diffTotalHours/5);
			newLoyaltyBar = myMenu.savedLoyaltyBar + (-1*diffTotalHours/5);
			newMoodBar = myMenu.savedMoodBar + (-1*diffTotalHours/10);
		}
		//normal speed
		//-1 / hour
		//-1 / 5 hours for mood bar
		if(myMenu.updateSpeed == 60){
			newHungryBar = myMenu.savedHungryBar + (-1*diffTotalHours);
			newHygienBar = myMenu.savedHygienBar + (-1*diffTotalHours);
			newLoyaltyBar = myMenu.savedLoyaltyBar + (-1*diffTotalHours);
			newMoodBar = myMenu.savedMoodBar + (-1*diffTotalHours/5);
		}
	}
	//this method apply the new changes
	public static void applyChanges(){
		myGame.nameIt = myMenu.savedName;
		myGame.charRace = myMenu.savedRace;
		myGame.charAge = newAge;
		myGame.bar[1] = newHungryBar;
		myGame.bar[2] = newHygienBar;
		myGame.bar[3] = newLoyaltyBar;
		myGame.bar[4] = newMoodBar;
		
		//don't decrease more than 0. Only matters for hygiene bar.
		for(int a=1; a<=4; a++){
			if(myGame.bar[a] < 0){
				myGame.bar[a] = 0;
			}
		}
	}
}
