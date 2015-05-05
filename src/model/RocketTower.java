package model;

import view.BattleGrid;

/** rocketTower *************************************************************
 * Description: A specific type of tower (Rocket Tower) that generalizes tower
 * with its own specific icon (texture) and projectile (rocket)
 * @params - none
 * @modifies none
 * @precond A user legally places a rocket tower on a build square, and so it needs 
 * to be instantiated.
 * @postcond A rocket tower is created and is able to be drawn to the screen
 * by the BattleGrid view.
 * ***********************************************************************/

public class RocketTower extends Tower{

	public RocketTower(int ID, int cost, int range, int damage, int maxAttackTime, int maxAttackDelay, double health, int venom, int stun) {
		super(ID, cost, range, damage, maxAttackTime, maxAttackDelay, health, venom, stun);
	}
	
/** towerAttack *************************************************************
* Description: The implementation for the abstract method in Tower. 
* @params - int x: x location of the tower (rocket's origin)
* int y: y location of the tower (rocket's origin)
* EnemyMotion enemy: The enemy targeted by the rocket.
* @modifies int rockets[i]: The battle grid's array holding the rocket
* images on the screen. Update with each new rocket created by the tower.
* @precond A rocket needs to be fired at an enemy in the tower's range
* @postcond A rocket is fired and added to the master list
* (BattleGrid.rocket[i)
* ***********************************************************************/

	public void towerAttack(int x, int y, EnemyMotion enemy) {
		for (int i = 0; i < BattleGrid.rockets.length; i++){
			
		  if (BattleGrid.rockets[i] == null) { //No rocket for this ID!
		  BattleGrid.rockets[i] = new Rocket((int) (2*BattleGrid.towerWidth + x*BattleGrid.towerWidth), (int) (3*BattleGrid.towerHeight + y*BattleGrid.towerHeight), 2, 6, enemy, venom, stun); //Make and record rocket
		  break;
		  }
		  
		}
		
	}

}
