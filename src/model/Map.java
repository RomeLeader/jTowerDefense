package model;

/** Map ************************************************************************************
 * Description: Map is a simple class with one function that traverses the game arena matrix
 * and sets the spawn point when it finds it.
 * @params none
 * @modifies spawnPoint: Sets the spawn point to the appropriate arena x & y coordinates
 * as defined by the user using value '2'.
 * @precond Spawn not initialized
 * @postcond Spawn located and assigned to an arena square.
 * ****************************************************************************************/

public class Map {
	
	public int[][] arena; //Actual game surface
	
	public SpawnPoint spawnPoint;
	
	public void findSpawnPoint(){
		for (int x = 0; x < arena.length; x++){
			for (int y = 0; y < arena[0].length; y++) {
				if (arena[x][y] == 2) {
					spawnPoint = new SpawnPoint(x, y);
				}
			}
		}
	}

}
