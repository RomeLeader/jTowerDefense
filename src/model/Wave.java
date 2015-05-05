package model;

import java.util.Random;

import view.BattleGrid;

/** Wave *************************************************************
 * Description: Wave manages the enemy groups that are sent at the user,
 * tracking when waves should be sent, as well as actually sending them
 * when required.
 * @params - none
 * @modifies int waveNumber: The current 'round' of enemies
 * int enemiesThisRound: Number of enemies currently alive in the round
 * boolean waveSpawning: true if wave should be sent. (else false) 
 * @precond A wave needs to be sent based on user input (ENTER)
 * @postcond A wave is sent and enemies are dispensed appropriately.
 * ***********************************************************************/


public class Wave {
	
	BattleGrid grid;
	
	public int waveNumber = 0; //The wave number (within the current phase)
	public int maxWaveNumber; //Number of waves in the game
	public int scoreThisRound = 10; //Number of enemy score allotted to the current wave
	int currentSpawnScore;
	
	public boolean waveSpawning = false; //Is the wave currently spawning?
	
	public Wave(BattleGrid grid) {
		
		this.grid = grid;
		
	}
	
	/** nextWave *************************************************************
	 * Description: nextWave prepares for the sending of a round, ensuring
	 * all enemies from the previous round are cleared and that a wave
	 * is permitted to be sent.
	 * @params - none
	 * @modifies int waveNumber: The current 'round' of enemies
	 * boolean waveSpawning: true if wave should be sent. (else false) 
	 * @precond A wave needs to be sent based on user input (ENTER)
	 * @postcond A wave is prepared by counting the wave number and setting
	 * the flag for wave sending.
	 * ***********************************************************************/
	
	public void nextWave() {
		
		if (this.waveNumber < this.maxWaveNumber) {
			
		this.waveNumber++; //Count this wave
		this.waveSpawning = true; //Enemies can spawn to begin the wave
		
		}
		
		else {
			waveSpawning = false;
		}
		
		this.scoreThisRound = this.waveNumber * 10; //Set the wave point allocation
		this.currentSpawnScore = 0;
		
		for (int i = 0; i < this.grid.enemyMap.length; i++) {
			this.grid.enemyMap[i] = null; //Clear the map of enemies from the previous round
		}
		
	}
	
	private int currentDelay = 0;
	private int spawnRate = 300; //frames of delay in spawn to appear on grid
	
	/** sendWave *************************************************************
	 * Description: sendWave actually sends a call to spawnEnemy to generate
	 * enemies for the wave, subject to a delay spawnRate.
	 * @params - none
	 * @modifies int enemiesThisRound: Enemies currently alive in the round
	 * @precond waveSpawning = true (wave should be sent)
	 * @postcond A wave is prepared by counting the wave number and setting
	 * the flag for wave sending.
	 * ***********************************************************************/
	
	public void sendWave() {
		
		if (this.currentSpawnScore < this.scoreThisRound) { //Send the number of enemies according to the score allotment 
			
			if (currentDelay < spawnRate) { //Delay a bit until we should send an enemy
				currentDelay++;
			}
			
			else {
				
				currentDelay = 0; //Reset the delay
				
				int[] enemiesSpawnableID = new int[Enemy.enemyList.length]; //IDs of spawnable enemies
				int enemiesSpawnableSoFar = 0; //Running tally
				
				for (int i = 0; i < Enemy.enemyList.length; i++){
				  if (Enemy.enemyList[i] != null) {
					if (((Enemy.enemyList[i].spawnScore + currentSpawnScore) <= this.scoreThisRound) && Enemy.enemyList[i].spawnScore <= this.waveNumber) {
						
						enemiesSpawnableID[enemiesSpawnableSoFar] = Enemy.enemyList[i].ID; //Add the spawnable enemy
						enemiesSpawnableSoFar++; //Update the spawnable enemies tally
						
					}
				  }
				}
				
			int enemyID = new Random().nextInt(enemiesSpawnableSoFar);
			
			this.currentSpawnScore += Enemy.enemyList[enemyID].spawnScore;	
			this.grid.spawnEnemy(enemiesSpawnableID[enemyID]);
				
			}
		}
		
		else {
			this.waveSpawning = false; //Finish sending the wave and make sure no more are sent out of turn
		}
		
	}

}
