package model;

import view.BattleGrid;

public class FreezeTower extends Tower{

	public FreezeTower(int ID, int cost, int range, int damage, int maxAttackTime, int maxAttackDelay, double health, int venom, int stun) {
		super(ID, cost, range, damage, maxAttackTime, maxAttackDelay, health, venom, stun);
	}

	public void towerAttack(int x, int y, EnemyMotion enemy) {
		for (int i = 0; i < BattleGrid.allice.length; i++){
			
		  if (BattleGrid.allice[i] == null) { //No ice for this ID!
		  BattleGrid.allice[i] = new Ice((int) (2*BattleGrid.towerWidth + x*BattleGrid.towerWidth), (int) (3*BattleGrid.towerHeight + y*BattleGrid.towerHeight), 5, damage, enemy, venom, stun); //Set ice
		  break;
		  }
		  
		}
		
	}

}
