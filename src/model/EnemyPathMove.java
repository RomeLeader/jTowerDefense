package model;

import view.BattleGrid;

/** EnemyPathMove *************************************************************
 * Description: EnemyPathMove comprises the path-finding algorithm for the enemies.
 * It extends EnemyPath to note the path a. 
 * @params - none
 * @modifies enemy.xPos: The current x-position of an enemy in motion
 * enemy.yPos: The current y-position of an enemy in motion.
 * @precond Enemy is created and alive, on its path towards the goal.
 * @postcond The enemy's position is gradually updated to get it towards
 * the goal square.
 * ***********************************************************************/


public class EnemyPathMove extends EnemyPath{
	
	private int enemyAttackDelay = 1001; //Have a pause between enemy attacks

	public EnemyPathMove(int ID) {
		super(ID);
	}
	
	/** move *************************************************************
	 * Description: The main path finding/position updating function
	 * for enemies in motion.
	 * @params - EnemyMotion enemy: An enemy currently in motion
	 * @modifies enemy.xPos: The current x-position of an enemy in motion
	 * enemy.yPos: The current y-position of an enemy in motion.
	 * enemy.pathPosX: The intermediate "goal" of a moving enemy - the next square
	 * on its path (X coordinate of this square).
	 * enemy.pathPosY: The intermediate "goal" of a moving enemy - the next square
	 * on its path (Y coordinate of this square).
	 * @precond Enemy is spawned.
	 * @postcond The enemy's position is gradually updated to get it towards
	 * the goal square.
	 * ***********************************************************************/
	
	public void move(EnemyMotion enemy) {

		if(enemy.frozen > 0 && enemy.frozen%2000000000 != 0){
			enemy.frozen--;
		}
		else{
			if (enemy.xPos % (int) BattleGrid.towerWidth == 0 && enemy.yPos % (int) BattleGrid.towerHeight == 0 && enemy.pathPosX == enemy.xPos / (int) BattleGrid.towerWidth && enemy.pathPosY == enemy.yPos / (int) BattleGrid.towerHeight) {
				if (enemy.pathPosX == goalPosX && enemy.pathPosY == goalPosY) { //If enemy is right before the base
					enemy.attack = true; //Now we can attack!
					if (enemyAttackDelay > 1000) {
						attackPlayer(enemy);
						enemyAttackDelay = 0;
					} else {
						enemyAttackDelay += enemy.enemy.attackRate;
					}
				} 
			else {
				if(route.route[enemy.pathPosX][enemy.pathPosY] == route.UP) { //If the next route square is UP...
					enemy.pathPosY --; // Move UP
				} 
						
				else {
					if(route.route[enemy.pathPosX][enemy.pathPosY] == route.DOWN) { //If the next route square is DOWN...
						enemy.pathPosY ++; // Move DOWN
					} 
					else {
						if(route.route[enemy.pathPosX][enemy.pathPosY] == route.RIGHT) { //If the next route square is RIGHT...
							enemy.pathPosX ++; // Move RIGHT
						} 
						else {
							if(route.route[enemy.pathPosX][enemy.pathPosY] == route.LEFT) { //If the next route square is LEFT...
								enemy.pathPosX --; //Move LEFT
							} 
							else {
								routeNotFound();
							}
						}
					} 
				}
			}
		}
		else {
			double xPos = enemy.xPos / BattleGrid.towerWidth;
			double yPos = enemy.yPos / BattleGrid.towerHeight;
			
			if (xPos > enemy.pathPosX) {enemy.xPos -= Math.ceil(enemy.enemy.speed);} //RIGHT
			if (xPos < enemy.pathPosX) {enemy.xPos += Math.ceil(enemy.enemy.speed);} //LEFT	
			if (yPos > enemy.pathPosY) {enemy.yPos -= Math.ceil(enemy.enemy.speed);}//DOWN	
			if (yPos < enemy.pathPosY) {enemy.yPos += Math.ceil(enemy.enemy.speed);}//UP
			
		}
	}	
}
	/*
	public void attackTower(EnemyMotion enemy, Tower tower){
		tower.health -= enemy.enemy.damage;
	}
	*/
	
	public void attackPlayer(EnemyMotion enemy) { //Attack the player after reaching the goal
		
		Profile.lives -= enemy.enemy.damage; //Attack
	
	}

	public void routeNotFound() { //Throw error and close if map is in error (no path from spawn to goal)
		System.out.println("Route not found! Map is in error!");
		System.exit(0);
	}

}
