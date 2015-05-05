package model;

/** EnemyPath *************************************************************
 * Description: Defines a path by defining the goal position and applies a route
 * from EnemyRoute.
 * @params - none
 * @modifies EnemyRoute route: The route to take (intialized)
 * goalPosX: x position of the goal
 * goalPosY: y position of the goal 
 * @precond There are enemies in the wave that need to find their goal.
 * @postcond A path is created for the enemies.
 * ***********************************************************************/

public class EnemyPath {
	
	public static EnemyRoute route;
	
	public static EnemyPathMove movePath; //The path to be traversed
	
	public static int goalPosX; //X coordinate of the goal square for enemies
	public static int goalPosY; //Y coordinate of the goal square for enemies
	
	public int ID;
	
	public EnemyPath(Map map) {
		
		route = new EnemyRoute(map); //Initialize route
		
		goalPosX = route.goal.xPos;
		goalPosY = route.goal.yPos;
		
		movePath = new EnemyPathMove(0);
		
	}
	
	public EnemyPath (int ID) {
		this.ID = ID;
	}
	
}
