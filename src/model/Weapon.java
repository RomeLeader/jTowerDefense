package model;

import view.BattleGrid;

/** Weapon *************************************************************
 * Description: Weapon constructs a new projectile for attacking towers,
 * and manages its direction, speed, and any special effects such as
 * poison or stunning/freezing.
 * @params - none
 * @modifies double direction: The direction of a particular projectile
 * double x: The x coordinate of the projectile's current position
 * double y: The y coordinate of the projectile's current position
 * @precond No projectile currently shares the same ID of the weapon to be created.
 * @postcond An projectile is created and added to the appropriate array for its
 * type (rockets, darts, etc.). It's direction and position are updated
 * repeatedly as it is repainted.
 * Note: This function does not DRAW the projectile. That is handled by  
 * various specific weapon drawing sections in paintComponent.
 * ***********************************************************************/
public class Weapon {

	public double x; //x position of the shell
	public double y; //y position of the shell
	public double direction; //Direction of the shell [Units: Radians]
	public double speed; //Speed of the shell
	public double damage; //Damage of shell
	public int stun;
	public int venom;
	
	public EnemyMotion target; //The shell's target

	public Weapon(double x, double y, double speed, double damage, EnemyMotion target, int venom, int stun) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.damage = damage;
		this.venom = venom;
		this.stun = stun;
		this.target = target;
		
		updateDirection();
		Math.toDegrees(direction); //Radians to degrees conversion for the direction
	}


	public void update() {
		
		updateDirection(); //Recalculate direction
		
		this.x += speed * Math.cos(direction); //Update x-position (cosine value)
		this.y += speed * Math.sin(direction); //Update y-position (sin value)
		
		checkTarget(); //Find target
		
	}
	
	public void checkTarget() {
		int deltaX = (int) (BattleGrid.towerWidth*2 + this.target.xPos - BattleGrid.towerWidth/2 - this.x - 5);
		int deltaY = (int) (BattleGrid.towerHeight*3 + this.target.yPos - BattleGrid.towerHeight/2 - this.y - 15);
		
		int deltaRadius = 4; //The enemy "hitbox"
		
		if((deltaX*deltaX + deltaY*deltaY) < (deltaRadius*deltaRadius)) {
			this.target.poisoned = Math.max(this.venom, this.target.poisoned);
			this.target.frozen = Math.max(this.stun, this.target.frozen);
			this.target.health -= this.damage;//Target attacked
			this.target = null; //Re-update target after hit
		}
		
	}

	public void updateDirection() {
		
		this.direction = Math.atan2(BattleGrid.towerHeight*3 + this.target.yPos - BattleGrid.towerHeight/2 - this.y - 15, BattleGrid.towerWidth*2 + this.target.xPos - BattleGrid.towerWidth/2 - this.x - 5); //Target the middle of the enemy
		
	}
}