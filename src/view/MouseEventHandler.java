package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseEventHandler implements MouseListener, MouseMotionListener {
	
	private BattleGrid grid;
	private BattleGrid.MouseClicked mouseclicked;
	private BattleGrid.MouseHeld mouseHeld;
	public MouseEventHandler (BattleGrid grid) {
		
		this.grid = grid;
		this.mouseHeld = this.grid.new MouseHeld();
		this.mouseclicked = this.grid.new MouseClicked();
	}

	public void mouseDragged(MouseEvent e) {
		
		
	}

	public void mouseMoved(MouseEvent e) {
	
		this.mouseclicked.mouseMoved(e);
	}

	public void mouseClicked(MouseEvent e) {
		this.mouseclicked.menuClick(e);
		this.mouseclicked.endClick(e);
		this.mouseclicked.selTower(e);
	}

	public void mousePressed(MouseEvent e) {
		this.mouseHeld.mouseDown(e);
	}

	public void mouseReleased(MouseEvent e) {
	//	this.mouseHeld.mouseDown(e);
		this.mouseHeld.mouseUp(e);
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

}
