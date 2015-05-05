package model;

/** EnemyRoute *************************************************************
 * Description: EnemyRoute tracks the enemy's progress on its route as it
 * moves, and updates it accordingly.
 * @params - none
 * @modifies int [][] route: The current location, in tiles, of an enemy
 * in a route
 * @precond Enemy is created and alive, moving on its path towards the goal.
 * @postcond The enemy's position is noted and updated
 * ***********************************************************************/

public class EnemyRoute {
	
	Map map;
	
	int[][] route = new int[17][10];
	int[][] routeValue = new int[17][10]; //The value of a route square for the targeting algorithm
	
	int lastPos = -1; //The stored value of the last checked square in the route - starts at undef. (-1)
	
	int RIGHT = 1;
	int DOWN = 2;
	int LEFT = 3;
	int UP = 4;
	
	int xPos;
	int yPos;
	
	int goalTile = 3;
	Goal goal;
	
	public EnemyRoute (Map map) {
		
		this.map = map;
		this.xPos = this.map.spawnPoint.getX(); //Set to x coordinate of enemy spawn
		this.yPos = this.map.spawnPoint.getY(); //Set to y coordinate of enemy spawn
		
		calculateRoute();
		
	}
	
	private void calculateRoute() {
		
		while (goal == null) { //As long as we haven't located the goal yet
			
			calculateNextPos(); //Find the goal
			
		}
		
	}
	
	private void calculateNextPos() {
		
		for (int i = 1; i < 5; i++) {
			
			if (i != lastPos) { //Do not recalculate the square we are on if we have made it to the goal.
				if (yPos > 0 && i == UP) { //UP direction check
					if (map.arena[xPos][yPos-1] == 1) {
						
						setRouteValue();
						
						lastPos = DOWN;
						route[xPos][yPos] = UP;
						
						yPos--;
						break;
					}
					
					else if(map.arena[xPos][yPos-1] == 3) {
						goal = new Goal(xPos, yPos);
						break;
					}
					
				}
				
				if (xPos < 16 && i == RIGHT) { //RIGHT direction check
					if (map.arena[xPos+1][yPos] == 1) {
						
						setRouteValue();
						
						lastPos = LEFT;
						route[xPos][yPos] = RIGHT;
						
						xPos++;
						break;
					}
					
					else if(map.arena[xPos+1][yPos] == 3) {
						goal = new Goal(xPos, yPos);
						break;
					}
					
				}
				
				if (xPos > 0 && i == LEFT) { //LEFT direction check
					if (map.arena[xPos-1][yPos] == 1) {
						
						setRouteValue();
						
						lastPos = RIGHT;
						route[xPos][yPos] = LEFT;
						xPos--;
						break;
					}
					
					else if(map.arena[xPos-1][yPos] == 3) {
						goal = new Goal(xPos, yPos);
						break;
					}
					
				}
				
				if (yPos < 9 && i == DOWN) { //DOWN direction check
					if (map.arena[xPos][yPos+1] == 1) {
						
						setRouteValue();
						
						lastPos = UP;
						route[xPos][yPos] = DOWN;
						yPos++;
						break;
					}
					
					else if (map.arena[xPos][yPos+1] == 3) {
						goal = new Goal(xPos, yPos);
						break;
					}
					
				}
				
				
			}
			
		}
		
	}
	
   private void setRouteValue () {
	   
	 if (lastPos == UP) {
		routeValue[xPos][yPos] = routeValue[xPos][yPos-1] + 1; 
	 }
	 
	 if (lastPos == DOWN) {
		routeValue[xPos][yPos] = routeValue[xPos][yPos+1] + 1; 
	 }
	 
	 if (lastPos == RIGHT) {
		routeValue[xPos][yPos] = routeValue[xPos+1][yPos] + 1; 
     }
	 
	 if (lastPos == LEFT) {
		routeValue[xPos][yPos] = routeValue[xPos-1][yPos] + 1; 
	 }
	 
	 if (lastPos == -1) {
		 routeValue[xPos][yPos] = 1; 
	 }
	 
   }
   
   public int getRouteValue (int x, int y) {
	   
	  return routeValue[x][y];
	   
   }

}
