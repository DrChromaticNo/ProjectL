package standard;

public class DescServer {

	/**
	 * This class will provide the descriptions for the various cards
	 * @author Chris
	 *
	 */
	final static String PARROT =  "Day: The parrot card is discarded " +
			"and replaced by a card from your hand (in the appropriate place)"+
			"\nDusk: N/A" +
			"\nNight: N/A" +
		 	"\nEnd: N/A\n";
	
	final static String MONKEY = "Day: Transfer all cursed relics from your den to the " +
			"den of the player to your left" +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: N/A" +
			"\nEnd: N/A\n";
	
	final static String BEGGAR = "Day: The player with the card of highest " +
			"rank on the ship gives you 3 gold" +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: N/A" +
			"\nEnd: N/A\n";
	
	final static String RECRUITER = "Day: Recruit 1 card from the den to your hand" +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: N/A" +
			"\nEnd: N/A\n";
	
	final static String CABIN_BOY = "Day: N/A" +
			"\nDusk: N/A" + 
			"\nNight: N/A" +
			"\nEnd: N/A\n";
	
	final static String PREACHER = "Day: Lose all treasure except for 1" +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: N/A" +
			"\nEnd: Score 5 gold\n";
	
	final static String BARKEEP = "Day: N/A" +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: Gain 1 gold" +
			"\nEnd: N/A\n";
	
	final static String WAITRESS = "Day: N/A" +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: If you have at least one treasure map, you can sell it for 3 gold" +
			"\nEnd: N/A\n";
	
	final static String CARPENTER = "Day: Lose half your gold" +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: N/A" +
			"\nEnd: Score 10 gold\n";
	
	final static String FRENCH_OFFICER = "Day: Gain 5 gold if you have less than 9 gold" +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: N/A" +
			"\nEnd: N/A\n";
	
	
	/**
	 * Returns the description for a given card value
	 * @param value the value of the card to get the description for
	 * @return the description for this value of card
	 */
	public String getCardDesc(int value)
	{
		switch(value){
			case 1: return PARROT;
			case 2: return MONKEY;
			case 3: return BEGGAR;
			case 4: return RECRUITER;
			case 5: return CABIN_BOY;
			case 6: return PREACHER;
			case 7: return BARKEEP;
			case 8: return WAITRESS;
			case 9: return CARPENTER;
			case 10: return FRENCH_OFFICER;
			}
			return "";
	}
	
	/**
	 * Returns the name for a given card value
	 * @param value the value of the card to get the name for
	 * @return the name for this value of card
	 */
	public String getCardName(int value)
	{
		switch(value){
		case 1: return "Parrot";
		case 2: return "Monkey";
		case 3: return "Beggar";
		case 4: return "Recruiter";
		case 5: return "Cabin Boy";
		case 6: return "Preacher";
		case 7: return "Barkeep";
		case 8: return "Waitress";
		case 9: return "Carpenter";
		case 10: return "French Officer";
		}
		return "";
	}
	
}
