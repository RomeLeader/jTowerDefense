package model;

/** Profile ************************************************************************************
 * Description: Profile keeps the current values of the player's health, money and score.
 * @params none
 * @modifies money: The player's money in the game (initialized to startingMoney)
 * lives: The player's life in the game (initialized to startingLives)
 * @precond A Profile has been created by the Player class
 * @postcond money and lives assigned accordingly on creation
 * ****************************************************************************************/

public class Profile {
	
	public static double lives; //The player's current lives (chances) in the game
	public static int money; //The player's current money on hand in the game
	public static int score; //The player's score for killing enemies
	public static int killCount; //The enemies defeated by the player (for winning purposes)

	public Profile(Player player) { //Initialize the players stats and money
		money = (int)(player.startingMoney / player.difficulty);
		if(money < 1000){//case for Insane
			money = 1000;
		}
		lives = (int)(player.startingLives / player.difficulty);
		if(lives < 100){
			lives = 100;
		}
		score = player.startingScore;
		killCount = player.startingKills;
	}

}
