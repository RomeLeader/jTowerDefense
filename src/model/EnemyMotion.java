package model;

import view.BattleGrid;

/** EnemyMotion *************************************************************
 * Description: Initializes the parameters the enemy will need while in motion.
 * An enemy is passed in, and given a starting position on the grid equal to
 * the spawn point when created. The x-position and y-position of the enemy
 * are then initialized, which will be updated frequently as it seeks the goal. 
 * The enemy attacks the player i.f.f it reaches the square before the goal 
 * and stops moving, and can be killed before getting there. To this end,
 * the enemies health is continually tracked as it is in motion. If it dies,
 * the user is credited with the appropriate value and cash reward.
 * @params - none
 * @modifies int xPos: Initialized to the spawn point's x-coordinate
 * int yPos: Initialized to the spawn point's y-coordinate
 * @precond An enemy is constructed and a wave has been sent.
 * @postcond An enemy's starting position and coordinates are initialized.
 * ***********************************************************************/

public class EnemyMotion {
	
	public Enemy enemy;
	
	public double xPos; //Current position of the enemy [X coordinate]
	public double yPos; //Current position of the enemy [Y coordinate]
	
	boolean attack; //[IT2] Is the enemy attacking?
	
	int pathPosX; //X coordinate of their current position on their route
	int pathPosY; //Y coordinate of their current position on their route
	
	int health; //The health of an enemy
	int score; //The score a player gets for killing an enemy
	int value; //The cash reward for killing an enemy
	int spawnScore; //The enemy's spawn score
	int poisoned;
	int frozen;
	
	public EnemyMotion(Enemy enemy, SpawnPoint spawnPoint) {
		
		this.enemy = enemy;
		
		this.pathPosX = spawnPoint.getX(); //Set the path of the enemy wave [X coordinate]
		this.pathPosY = spawnPoint.getY(); //Set the path of the enemy wave [Y coordinate]
		
		this.xPos = spawnPoint.getX() * (int) BattleGrid.towerWidth; //Where the enemy is on the path [X coordinate]
		this.yPos = spawnPoint.getY() * (int) BattleGrid.towerHeight; //Where the enemy is on the path [Y coordinate]
		
		this.attack = false; //Can't attack until you reach the base [IT2] Attack whenever tower in range
		this.health = enemy.health;
		this.score = enemy.score;
		this.value = enemy.value;
		this.spawnScore = enemy.spawnScore;
		this.poisoned = enemy.poisoned;
		this.frozen = enemy.frozen;
		
	}
	
	/** update *************************************************************
	 * Description: An enemy's health is tracked. If it dies, the user is credited
	 * with the point value and cash reward for the enemy. (Note: Only the view
	 * can remove the enemy's sprite from the screen (in enemyUpdate))
	 * @params - none
	 * @modifies int Profile.score: The player's score
	 * int Profile.money: The player's money
	 * @precond An enemy is alive and in motion along its path.
	 * @postcond A dead enemy credits the user with score and money. 
	 * returns: currentEnemy: The current moving enemy and its alive/dead state
	 * ***********************************************************************/
	
	public EnemyMotion update() {
		
		EnemyMotion currentEnemy = this;
		
		if(currentEnemy.poisoned > 0 ){
			currentEnemy.poisoned--;
			if(currentEnemy.poisoned%300 == 0){
				currentEnemy.health -= 1;
			}
		}
		
		if(currentEnemy.health <= 0) { //If enemy is dead
			Profile.score += currentEnemy.score; //Update the score if the enemy is defeated
			Profile.money += currentEnemy.value; //Update the player's money when enemy is defeated
			Profile.killCount += currentEnemy.spawnScore; //Update the kill count by spawn score when the enemy dies
			
			//System.out.println(Profile.killCount);
			
			return null; //Handle the enemy's death
			
		}
		
		return currentEnemy;
	}

}
