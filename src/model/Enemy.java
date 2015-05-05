package model;

import java.awt.Image;

import javax.swing.ImageIcon;

/** Enemy *************************************************************
 * Description: Enemy constructs a new enemy and grabs its texture file.
 * @params - none
 * @modifies enemyMap[i]: The map of enemies on top of the arena is updated
 * by a new enemy from the enemyList at the specified spawnPoint (Code 2)
 * @precond No enemy currently shares the same ID of the enemy to be created.
 * @postcond An enemy is created and added to the enemyMap (done once per call)
 * Note: This function does not DRAW the enemy. That is handled by ENEMIES in
 * paintComponent.
 * ***********************************************************************/

public class Enemy {
	
	public static final Enemy[] enemyList = new Enemy[10];
	
	public static final Enemy blob = new EnemyBlob(0, 10, 10, 0.2, 1, 3, 10, 1).getTextureFile("Phantom.png");
	public static final Enemy toast = new EnemyBlob(1, 20, 20, 1, 1, 4, 20, 2).getTextureFile("SuperToast.png");
	public static final Enemy cat = new EnemyCat(2, 30, 25, 2, 1, 2, 15, 3).getTextureFile("Black Cat.png");
	public static final Enemy skeleton = new EnemySkeleton(3, 75, 30, 1.5, 1, 5, 40 , 4).getTextureFile("Skeleton.png");
	public static final Enemy minidevil = new EnemyMiniDevil(4, 100, 100, 1.5, 1, 7, 75, 4).getTextureFile("Mini Devil.png");
	public static final Enemy devil = new EnemyDevil(5, 666, 660, 0.2, 1, 10, 200, 5).getTextureFile("Devil.png");
	
	static {enemyList[0] = blob;} //Assign the blob to enemyList[0]
	static {enemyList[1] = toast;} //Assign the toast to enemyList[1]
	static {enemyList[2] = cat;} //Assign the cat to enemyList[2]
	static {enemyList[3] = skeleton;} //Assign the skeleton to enemyList[3]
	static {enemyList[4] = minidevil;} //Assign the minidevil to enemyList[4]
	static {enemyList[5] = devil;} //Assign the devil to enemyList[5]
	
	public String textureFile = ""; //The file to use for the enemy's texture
	public Image texture = null; //The current texture of the enemy
	
	public int ID; //The enemy's unique ID
	public int score; //The score the user gets for killing an enemy
	public int value; //The amount of money the user gets for killing the enemy
	public double speed; //Speed of the enemy
	public double attackRate; //The enemy's attack speed
	public double damage; //The enemy's attack damage
	public int health; //The enemy's health
	public int spawnScore; //A number to decide the spawn groups of enemies. Wave must exceed or exceed this value to spawn.
	
	public int poisoned = 0;
	public int frozen = 0;
	//public int enemyAttackDelay;
	
	public Enemy(int ID, int score, int value, double speed, double attackRate, int damage, int health, int spawnScore) {
		
		if(enemyList[ID] != null) {
			System.out.println("ERROR! Enemy with this ID already exists!");
		} 
		
		else {
			this.ID = ID;
			this.value = value;
			this.speed = speed;
			if (50 % speed != 0) {
			this.speed = 50 % speed; //Make sure the speed evenly divides 50 for the path finding algorithm
			}
			this.health = health;
			this.attackRate = attackRate;
			this.damage = damage;
			this.score = score;
			this.spawnScore = spawnScore;
	//		this.enemyAttackDelay = 1001;
		}
		
	}
	
	public Enemy getTextureFile(String str) {
		
		this.textureFile = str;
		this.texture = new ImageIcon("res/enemy/" + this.textureFile).getImage();
		
		return this; //Return the created sprited enemy
	}

}
