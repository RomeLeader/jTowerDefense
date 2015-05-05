package model;

import view.BattleGrid;

public class PoisonTower extends Tower{

	public PoisonTower(int ID, int cost, int range, int damage, int maxAttackTime, int maxAttackDelay, double health, int venom, int stun) {
		super(ID, cost, range, damage, maxAttackTime, maxAttackDelay, health, venom, stun);
	}

	public void towerAttack(int x, int y, EnemyMotion enemy) {
		for (int i = 0; i < BattleGrid.darts.length; i++){
			
		  if (BattleGrid.darts[i] == null) { //No dart for this ID!
		  BattleGrid.darts[i] = new PoisonDart((int) (2*BattleGrid.towerWidth + x*BattleGrid.towerWidth), (int) (3*BattleGrid.towerHeight + y*BattleGrid.towerHeight), 2, damage, enemy, venom, stun); //Set dart
		  break;
		  }
		  
		}
		
	}

}
