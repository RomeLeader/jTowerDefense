package model;

/** SpawnPoint ************************************************************************************
 * Description: Simple class to return the position of the spawn point.
 * @params none
 * @modifies none
 * @precond Spawn point exists
 * @postcond Spawn located and x and y location returned.
 * ****************************************************************************************/

public class SpawnPoint {
	
	private int x; //X coordinate of the spawn point
	private int y; //Y coordinate of the spawn point
	
	public SpawnPoint(int x, int y) {
		this.x = x;
		this.y = y;
		
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

}
