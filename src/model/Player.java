package model;

import view.BattleGrid;

/** Player ************************************************************************************
 * Description: Player creates a user by making a profile, and holds the default values
 * of the player variables of money, lives and score.
 * @params none
 * @modifies none
 * @precond Game not begun - no profile exists
 * @postcond Profile created
 * ****************************************************************************************/

public class Player {
	
	private BattleGrid grid;
	public Profile profile;
	public static double difficulty;
	
	int startingMoney = 1000; //Initial money on startup
	int startingLives = 100; //Initial lives on startup
	int startingScore = 0; //Player's score starts at zero
	int startingKills = 0; //Player's initial kill count

	public Player(BattleGrid grid) {
		
		this.grid = grid;
		this.grid.scene = 0; //Starting the view at the main menu screen
		
	}
	
	public void createProfile() {
		this.profile = new Profile(this);
	}

}
