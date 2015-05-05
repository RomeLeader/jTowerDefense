package model;

import view.BattleGrid;

public class ArtilleryTower extends Tower{

	public ArtilleryTower(int ID, int cost, int range, int damage, int maxAttackTime, int maxAttackDelay, double health, int venom, int stun) {
		super(ID, cost, range, damage, maxAttackTime, maxAttackDelay, health, venom, stun);
	}

	public void towerAttack(int x, int y, EnemyMotion enemy) {
		for (int i = 0; i < BattleGrid.shells.length; i++){
			
		  if (BattleGrid.shells[i] == null) { //No shell for this ID!
		  BattleGrid.shells[i] = new Shell((int) (2*BattleGrid.towerWidth + x*BattleGrid.towerWidth), (int) (3*BattleGrid.towerHeight + y*BattleGrid.towerHeight), 1, damage, enemy, venom, stun); //Set shell
		  break;
		  }
		  
		}
		
	}

}