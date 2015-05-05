package model;

import view.BattleGrid;

public class MachineGunTower extends Tower{

	public MachineGunTower(int ID, int cost, int range, int damage, int maxAttackTime, int maxAttackDelay, double health, int venom, int stun) {
		super(ID, cost, range, damage, maxAttackTime, maxAttackDelay, health, venom, stun);
	}

	public void towerAttack(int x, int y, EnemyMotion enemy) {
		for (int i = 0; i < BattleGrid.bullets.length; i++){
			
		  if (BattleGrid.bullets[i] == null) { //No bullet for this ID!
		  BattleGrid.bullets[i] = new Bullet((int) (2*BattleGrid.towerWidth + x*BattleGrid.towerWidth), (int) (3*BattleGrid.towerHeight + y*BattleGrid.towerHeight), 5, damage, enemy, venom, stun); //Set bullet
		  break;
		  }
		  
		}
		
	}

}
