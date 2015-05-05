package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameView extends JFrame{
	private static final long serialVersionUID = 1L;

	public static void main (String [] args) {
	
	   new GameView();
	   
	}
	
	public GameView() {
		//new JFrame();
		//dispose();
		//this.setUndecorated(true);
		this.setVisible(true);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //get the current computer's resolution
		double width = screenSize.getWidth();								
		double height = screenSize.getHeight();
		this.setSize((int)width, (int)height); //Launch Fullscreen
		//this.setSize((int)(width/2), (int)(height/2)); //Launch Windowed
		this.setTitle("jTowerDefense");
		//this.setExtendedState(MAXIMIZED_BOTH); // Fullscreen launch option
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		
		this.setLayout(new BorderLayout());
		BattleGrid grid = new BattleGrid(this);
		this.add(grid);
		
	}

}
