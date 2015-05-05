package model;

public class Goal {
	
	int yPos; //Y coordinate of the goal
	int xPos; //X coordinate of the goal
	
	int RIGHT = 1;
	int DOWN = 2;
	int LEFT = 3;
	int UP = 4;
	
	public Goal (int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}

}
