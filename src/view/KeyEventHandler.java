package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventHandler implements KeyListener {
	
	private BattleGrid grid;
	public BattleGrid.KeyPressed keyPressed;
	
	public KeyEventHandler(BattleGrid grid) {
		this.grid = grid;
		this.keyPressed = this.grid.new KeyPressed();
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyCode == 8){//BACKSPACE to go back to scene 1
			this.keyPressed.KeyBACK();
		}
		
		if(keyCode == 10){ //ENTER to advance wave
			this.keyPressed.KeyENTER();
			
		}
		if(keyCode == 27){ //ESC key to close game
			this.keyPressed.KeyESC();
		}
		
		if(keyCode == 32){ //Space to begin main game
			this.keyPressed.KeySPACE();
		}
		
		if(keyCode == 49){//1 to change Attack Strategy
			this.keyPressed.KeyONE();
		}
		
		if(keyCode == 50){//2 to change Attack Strategy
			this.keyPressed.KeyTWO();
		}
		
		
		if(keyCode == 80){//P to Pause
			this.keyPressed.KeyP();
		}
		
	 }
	
     public void keyReleased(KeyEvent e) {
		
		
	 }

	 public void keyTyped(KeyEvent e) {
		
		
	 }

}
