package model;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

import view.BattleGrid;

/** Tower *************************************************************
 * Description: Tower constructs a new tower, grabs its texture file, 
 * and implements a clone to ensure all towers attack independently.
 * @params - none
 * @modifies towerMap[i]: The map of towers on top of the arena is updated
 * by a new tower from the towerList at the specified location (Code 1)
 * @precond No tower currently shares the same ID of the tower to be created.
 * @postcond An enemy is created and added to the towerMap (done once per call)
 * Note: This function does not DRAW the tower. That is handled by TOWERS/TOWER GRID in
 * paintComponent.
 * ***********************************************************************/

public abstract class Tower implements Cloneable{
	
	public String imageFile = "";
	public Image texture;
	
	public static final Tower[] towerList = new Tower[20]; //Array of all the towers the user has placed. (Arbitrary large limit set)
	public static final Tower rocketTower = new RocketTower(0, 250, 3, 15, 200, 200, 50, 0, 0).getImageSource("scottrocketTower");
	//public static final Tower laserTower = new LaserTower(, 350, 2, 10, 350, 350).getImageSource("scottlaserTower");
	public static final Tower artilleryTower = new ArtilleryTower(1, 500, 10, 40, 100, 600, 50, 0, 50).getImageSource("Artillery");
	public static final Tower poisonTower = new PoisonTower(2, 300, 4, 1, 100, 200, 50, 6000, 0).getImageSource("Poison");
	public static final Tower machineGunTower = new MachineGunTower(3, 300, 2, 2, 100, 1, 50, 0, 0).getImageSource("MachineGun");
	public static final Tower freezeTower = new FreezeTower(4, 300, 2, 1, 100, 200, 50, 0, 450).getImageSource("Freeze");
	
	/*****TOWER VARIABLES******/
	public int ID; //Number of tower
	public int level = 0; //Level of the tower (for upgrades)
	public static final int maxLevel = 3; //Maximum level of the tower
	public int cost; //Cost to buy tower
	public int towerRange; //The firing range of the tower
	public int damage; //Damage the tower does (multiplier)
	public EnemyMotion target; //The tower's desired target
	public double attackTime; //The time of persistence of a weapon (if any)
	public double attackDelay; //The length of pause between attacks (re-charging, reloading, etc.)
	public double maxAttackTime;
	public double maxAttackDelay;
	public double health;
	public int venom;
	public int stun;
	
	/*****STRATEGIES*****/
	
	public String first = "FIRST"; //Attack the enemy closest to the goal
	public String rand = "RANDOM"; //Attack a random enemy within the range of the tower
	public String weak = "WEAKEST"; //Attack the weakest enemy in range
	
	public String attackStrategy = "RANDOM"; //Which enemy should we attack? (What strategy to use?)
	
	public Tower(int ID, int cost, int range, int damage, int maxAttackTime, int maxAttackDelay, double health, int venom, int stun) { //Tower constructor
        if (towerList[ID] != null){
			System.out.println("ERROR! Two towers with same ID! Conflict at:" + ID);
		}
        
        else{
        	towerList[ID] = this;
        	this.ID = ID;
        	this.cost = cost;
        	this.towerRange = range;
        	
        	this.damage = damage;
        	this.maxAttackTime = maxAttackTime;
        	this.maxAttackDelay = maxAttackDelay;
        	
        	this.attackTime = 0;
        	this.attackDelay = 0;
        	this.health = health;
        	this.venom = venom;
        	this.stun = stun;
        	
        }
	}
	
	/** calculateEnemy *************************************************************
	 * Description: calculateEnemy determines if an enemy is in range and if so,
	 * determines how to attack the enemy through application of a strategy.
	 * @params - EnemyMotion[] enemies: An array of enemies in motion
	 * int x: x position of the tower
	 * int y: y position of the tower
	 * @modifies enemiesInRange[i]: A list of enemies in range of the tower
	 * enemyMap[i]: Updated by the tower's targeting
	 * @precond There exists a tower that may shoot an enemy
	 * @postcond Alive enemies in range are counted, and targeted by the tower
	 * @returns enemiesInRange[i]: The array of enemies in range of the tower.
	 * (Null if no enemy in range)
	 * ***********************************************************************/

	
	public EnemyMotion calculateEnemy(EnemyMotion[] enemies, int x, int y) {
		
		EnemyMotion[] enemiesInRange = new EnemyMotion[enemies.length]; //An array to hold all enemies in range of the tower
		
		int towerX = x; //The tower's x-position
		int towerY = y; //The tower's y-position
		
		int enemyX; //The enemy's x-position
		int enemyY; //The enemy's y-position
		
		int towerRadius = this.towerRange; //Tower's effective range
		int enemyRadius = 1;
		
		for (int i = 0; i < enemies.length; i++) {
			if (enemies[i] != null) {
				
				enemyX = (int) (enemies[i].xPos / BattleGrid.towerWidth); //Convert enemy x position to a position in tiles
				enemyY = (int) (enemies[i].yPos / BattleGrid.towerHeight); //Convert enemy y position to a position in tiles
				
				int dx = enemyX - towerX; //Max X-range of tower
				int dy = enemyY - towerY; //Max Y-range of tower
				
				int dRadius = towerRadius + enemyRadius; //Tower range radius
				
				if ((dx * dx) + (dy * dy) < (dRadius * dRadius)) { //Pythagorean Theorem to find enemies in radius range
					enemiesInRange[i] = enemies[i]; //Found one!
				} else {
					enemiesInRange[i] = null; //Otherwise, don't target that enemy!
				}
				
			}
			
		}
		
		 int totalEnemies = 0; //Total number of enemies in range	
		   
		   for (int i = 0; i < enemiesInRange.length; i++) {  //Loop through the enemies in range for all attack strategies
			   if (enemiesInRange[i] != null) {
			   totalEnemies++; //Count the enemies in range
			   }
		   }
		
		if (this.attackStrategy == "RANDOM") { //The RANDOM targeting strategy
		   
		   	if(totalEnemies > 0) {
		   		int enemy = new Random().nextInt(totalEnemies); //Pick a random enemy to attack from the list
		   		int enemiesTaken = 0; //Non-dead enemies counter
		   		int i = 0;
		   		
		   		while (true){ 
		   			
		   			if (enemiesTaken == enemy && enemiesInRange[i] != null) {
		   				
		   			return enemiesInRange[i];
		   			
		   			}
		   			
		   			if (enemiesInRange[i] != null) { //Still enemies in range
		   				enemiesTaken++; //Add them to the non-dead enemies list
		   			}
		   			
		   			i++; //Next enemy in range!
		   			
		   		}	
		   }   
		   
		}
		
		if (this.attackStrategy == "FIRST") {
			
		  EnemyMotion bestTarget = null; //The best target calculated by the algorithm 
		
			for (int i = 0; i < enemiesInRange.length; i++) {  //Loop through the enemies in range for all attack strategies
				   if (enemiesInRange[i] != null) {
					   
				     if (bestTarget == null) {
				    	 
				    	bestTarget = enemiesInRange[i]; 
				    	
				     }
				     
				     else {
				    	 
				        int tx = bestTarget.pathPosX;
				    	int ty = bestTarget.pathPosY;
				    	
				    	int tValue = BattleGrid.enemyPath.route.getRouteValue(tx, ty);
				    	
				    	if (BattleGrid.enemyPath.route.getRouteValue(enemiesInRange[i].pathPosX, enemiesInRange[i].pathPosY) > tValue) { //If the target can be improved, do so
				    	  bestTarget = enemiesInRange[i];
				    	}
				    	
				    	else if (BattleGrid.enemyPath.route.getRouteValue(enemiesInRange[i].pathPosX, enemiesInRange[i].pathPosY) == tValue) {
				    	//  System.out.println("Target not better! Sticking with old one!");
				    	}
				    	 
				     }
				     
				   }
		   }
			
		return bestTarget;
			
		}
		
		if (this.attackStrategy == "WEAKEST") {
			
			EnemyMotion weakestTarget = null;
			
			for (int i = 0; i < enemiesInRange.length; i++) {  //Loop through the enemies in range for all attack strategies
				   if (enemiesInRange[i] != null) {
				       if (enemiesInRange[i].health < weakestTarget.health) {
				    	   weakestTarget = enemiesInRange[i];
				       }
		
				       else {
				    	   
				       }
				       
		           }
				   
		   }
			
		  return weakestTarget;
			
		}
		
		return null; //Any other case, don't attack
	}
	
	public abstract void towerAttack(int x, int y, EnemyMotion enemy);
	
	/** towerClone *************************************************************
	 * Description: towerClone is a helper function that makes a clone each time
	 * a tower is created for attacking purposes. Without this function,
	 * every time an enemy is in range of any tower, the first tower placed
	 * will fire upon that enemy.
	 * ***********************************************************************/
		
	
	public Object towerClone () { //Clones the tower to make sure all towers attack independently
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			
			e.printStackTrace();
		}
		
		return null;
				
	}
	
	public Tower getImageSource (String src) { //Grabs the texture file for the tower
		this.imageFile = src;
		
		this.texture = new ImageIcon("res/tower/"+ this.imageFile + ".png").getImage();
		
		return null;
	}
}
