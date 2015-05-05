package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TextField;
import java.awt.Toolkit;

import javax.swing.*;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.TextListener;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
//import javax.swing.Timer;














import javax.swing.event.DocumentListener;

import model.Bullet;
import model.Enemy;
import model.EnemyMotion;
import model.EnemyPath;
import model.Highscore;
import model.Ice;
import model.Map;
import model.MapFile;
import model.Player;
import model.PoisonDart;
import model.Profile;
import model.Rocket;
import model.Shell;
import model.Tower;
import model.Wave;

/** BattleGrid *************************************************************
 * Description: BattleGrid is the class that maintains and updates the 
 * visual game elements that the user sees. This includes the drawing of the map
 * and arena, the placing of towers, and the actions of enemies as they move.
 * @params - none
 * @modifies Various
 * ***********************************************************************/

public class BattleGrid extends JPanel implements Runnable, ActionListener {
	


	private static final long serialVersionUID = 1L;

	Thread thread = new Thread(this);
	 
	 GameView view;
	
	 Player player;
	 
	 Map map;
	 MapFile mapFile;
	 
	 Wave wave;
	 public static EnemyPath enemyPath;
	 
	 int selXPos;
	 int selYPos;

	 private JOptionPane name;
	 String Name;
	 
	 int b;
	 public boolean running = false; //Is the game running?
	 public boolean paused = false; //Is the game paused?
	 
	 public static double towerWidth = 1;
	 public static double towerHeight = 1;
	 public Tower selectedTower;
	 List<String> scores = new ArrayList<String>();
	 
	 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //get the current computer's resolution
	 double widthRes = screenSize.getWidth();								
	 double heightRes = screenSize.getHeight();
	 int xPos;
	 int yPos;
	 int messWidth;
	 
	// TextListener l;
	
	 public int[][] arena = new int[17][10]; //The play surface
	 public Tower[][] towerMap = new Tower[17][10]; //Map of towers that sit on top of the arena
	 public Image[] terrain = new Image[100]; //Arbitrarily large array for terrain cells
	 
	 private Image playButton = new ImageIcon("res/play.png").getImage();
	 
	 public EnemyMotion[] enemyMap = new EnemyMotion[10]; //Array of enemies in the game - bounded by arbitrarily large amount
	 
	 public static Rocket[] rockets = new Rocket[20]; //Array of rocket projectiles in game
     public static Shell[] shells = new Shell[20];
	 public static PoisonDart[] darts = new PoisonDart[20];
	 public static Bullet[] bullets = new Bullet[200];
	 public static Ice[] allice = new Ice[20];
	 
	 private String packageName = "model";
	 //private int fps = 0; //The Frames Per Second (FPS) of the view
	 
	 public int scene = 0; //The view's state (initially 0 for menu)
	 
	 public int pointer = 0; //Pending result of previous click
	 public int pointerXPos = 0; //Mouse's x-position
	 public int pointerYPos = 0; //Mouse's y-position
	 
	//ActionListener update = new ActionListener() { 
		//@Override public void actionPerformed(ActionEvent ae) { repaint(); }
   //};
		
   //Timer timer = new Timer(1, update); 
		
	public BattleGrid (GameView view) {
		
		this.view = view;
		
		this.view.addKeyListener(new KeyEventHandler(this));
		this.addMouseListener(new MouseEventHandler(this));
		this.addMouseMotionListener(new MouseEventHandler(this));
		
		towerWidth = 50; 
		towerHeight = 50;
		
		//timer.start();
		thread.start();
		
	}
	
	public void paintComponent (Graphics g) {

		g.clearRect(0, 0, this.view.getWidth() ,this.view.getHeight()); //Make sure painting can proceed as prev. layer is done painting

/********MAIN MENU BACKGROUND*********/		
		if (scene == 0) { //Scene 0 = The pregame state.
			MainMenu(g); //Set up the main menu
		}
		
/**********GAME BACKGROUND*********/		
		else if (scene == 1) { //Scene 1 = The active game playing state.
			g.setColor(Color.green); //During game backdrop
			g.fillRect(0, 0, this.view.getWidth(), this.view.getHeight());
			
/**********GRID**************/
			g.setColor(Color.GRAY);
			for (int x = 0; x < 17; x++) { //Grid the space NOTE [IT2]: May change to better suit all displays as percentage of screen width/height
				for (int y = 0; y < 10; y++) {
				  g.drawImage(terrain[arena[x][y]], (int) ((view.getWidth() / widthRes) * (towerWidth + (x * (int) towerWidth))) ,(int) ((view.getHeight() / heightRes) * (towerHeight + (y * (int)towerHeight))), (int) (towerWidth * (view.getWidth() / widthRes)), (int) (towerHeight * (view.getHeight() / heightRes)), null);	
			      g.drawRect((int)((view.getWidth() / widthRes) * (towerWidth + (x*(int)towerWidth))), (int)((view.getHeight() / heightRes) * (towerHeight + (y*(int)towerHeight))), (int)((view.getWidth() / widthRes) * towerWidth), (int)((view.getHeight() / heightRes) * towerHeight));
				}  
			}
/**********ENEMIES**********/			
			for (int i = 0; i < enemyMap.length; i++){
				if (enemyMap[i] != null) {
					g.drawImage(enemyMap[i].enemy.texture, (int)((view.getWidth() / widthRes) * (enemyMap[i].xPos + (int) towerWidth)), (int)((view.getHeight() / heightRes) * ( enemyMap[i].yPos + (int) towerHeight)), (int) ((view.getWidth() / widthRes)* towerWidth), (int) ((view.getHeight() / heightRes)*towerHeight), null);
				}
			}
	
/*********UPGRADE MENU******/
				g.setColor(Color.BLUE);
				g.drawRect((int)((view.getWidth() / widthRes) * 1165), (int)((view.getHeight() / heightRes) * 250), (int)((view.getWidth() / widthRes) * 250), (int)((view.getHeight() / heightRes) * 500));
				g.drawRect((int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes)* (250 + (int) towerHeight/2)), (int)((view.getWidth() / widthRes) * towerWidth*3), (int) ((view.getHeight() / heightRes) * (towerHeight*3)));
				if ((selectedTower != null)) { //If a tower is selected, display it in the upgrade window
					g.drawImage(selectedTower.texture, (int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes)*(250 + (int) towerHeight/2)), (int)((view.getWidth() / widthRes) * towerWidth*3), (int) ((view.getHeight() / heightRes) * towerHeight*3), null);
					g.drawRect((int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes)* (250 + (int) towerHeight/2)), (int)((view.getWidth() / widthRes) * towerWidth*3), (int) ((view.getHeight() / heightRes) * (towerHeight*3)));
					g.drawString("ATK STRATEGY: " + selectedTower.attackStrategy, (int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes) * 270));
					g.drawString("COST: " + selectedTower.cost, (int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes) * (420 + (int) towerHeight/2)));
					g.drawString("DAMAGE: " + selectedTower.damage, (int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)),(int)((view.getHeight() / heightRes) * (440 + towerHeight/2)));
					g.drawString("RANGE: " + selectedTower.towerRange, (int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes) * (460 + (int) towerHeight/2)));
					g.drawString("LEVEL: " + selectedTower.level, (int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes) * (480 + (int) towerHeight/2)));
					g.drawString("RATE OF FIRE: " + (int) (10000/(2 * selectedTower.maxAttackDelay)), (int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes) * (500 + (int) towerHeight/2)));
					
					if (selectedTower.level != Tower.maxLevel) {
						g.drawString("UPGRADE COST: " + selectedTower.cost * (selectedTower.level + 1.5), (int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes) * (520 + (int) towerHeight/2)));
					}
				
					else {
						g.drawString("UPGRADE COST: " + "----", 1165 + (int)((view.getWidth() / widthRes) * ( towerWidth/2)), (int)((view.getHeight() / heightRes) * (520 + (int) towerHeight/2)));	
					}
				}
					
/********UPGRADE/SELL BUTTONS****/			
			g.drawRect((int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes) * (550 + (int) towerHeight/2)),(int) ((view.getWidth() / widthRes) * towerWidth*3), ((int) ((view.getHeight() / heightRes) * (towerHeight*2 + (int) towerHeight)/4))); //Sell button
			g.drawString("SELL TOWER",(int)((view.getWidth() / widthRes)*( 1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes) * (570 + (int) towerHeight/2)));
			g.drawRect((int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes) * (600 + (int) towerHeight/2)),(int) ((view.getWidth() / widthRes) * towerWidth*3), ((int) ((view.getHeight() / heightRes) * (towerHeight*2 + (int) towerHeight)/4))); //Upgrade button
			g.drawString("UPGRADE TOWER",(int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes) * (620 + (int) towerHeight/2)));
			g.drawRect((int)((view.getWidth() / widthRes)*(1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes)*(650 + (int) towerHeight/2)),(int) ((view.getWidth() / widthRes)*towerWidth*3), ((int) ((view.getHeight() / heightRes) * (towerHeight*2 + (int) towerHeight)/4)));
			g.drawString("DOWNGRADE TOWER",(int)((view.getWidth() / widthRes) * (1165 + (int) towerWidth/2)), (int)((view.getHeight() / heightRes) * (670 + (int) towerHeight/2)));

/*******OPTIONS*********/			
			g.drawRect((int)((view.getWidth() / widthRes) * (1000 + towerWidth/2)), (int)((view.getHeight() / heightRes) *(700 + towerHeight/2)), (int) ((view.getWidth() / widthRes) * towerWidth), (int)((view.getHeight() / heightRes) * towerHeight)); //Play button/Pause button
			g.drawImage(playButton, (int)((view.getWidth() / widthRes) * (1000 + towerWidth/2)), (int)((view.getHeight() / heightRes) *(700 + towerHeight/2)),(int) ((view.getWidth() / widthRes) * towerWidth), (int)((view.getHeight() / heightRes) * towerHeight), null);
			g.drawRect((int)((view.getWidth() / widthRes) *(1050 + towerWidth/2)), (int)((view.getHeight() / heightRes) * (700 + towerHeight/2)), (int) ((view.getWidth() / widthRes) * towerWidth), (int)((view.getHeight() / heightRes) * towerHeight)); //Speed button


/******USER HUD*******/		
		g.setColor(Color.RED);
		g.drawRect((int)((1165*(view.getWidth() / widthRes))), 0, (int) ((view.getWidth() / widthRes)* 200), (int) ((view.getHeight() / heightRes) * 70)); //Money display
		g.drawString("Money: " + player.profile.money, (int) ((view.getWidth() / widthRes) * 1165), (int) ((view.getHeight() / heightRes) * 20));
		//g.drawString((pointerXPos + "," + pointerYPos), (int) ((view.getWidth() / widthRes) * 1165), (int) ((view.getHeight() / heightRes) * 40));
		g.drawRect((int) ((view.getWidth() / widthRes)* 1165),(int) ((view.getHeight() / heightRes) * 80), (int) ((view.getWidth() / widthRes) * 200), (int) ((view.getHeight() / heightRes) * 60)); //Health display
		g.drawString("Health: " + player.profile.lives, (int) ((view.getWidth() / widthRes) * 1165), (int) ((view.getHeight() / heightRes) * 130));
		g.drawRect((int) ((view.getWidth() / widthRes) * 1165), (int) ((view.getHeight() / heightRes) * 160), (int) ((view.getWidth() / widthRes) * 200), (int) ((view.getHeight() / heightRes) * 80)); //Modify buttons/score container
		g.drawString("Score:" + player.profile.score ,(int) ((view.getWidth() / widthRes) * 1165), (int) ((view.getHeight() / heightRes) * 180));
		g.drawString("Current Wave: " + wave.waveNumber + "/" + wave.maxWaveNumber, (int) ((view.getWidth() / widthRes) * 1165), (int) ((view.getHeight() / heightRes)* 200));
		g.drawString("Wave score remaining: " + ((5*(Math.pow((wave.waveNumber),2)) + 5*(wave.waveNumber)) - Profile.killCount), (int) ((view.getWidth() / widthRes) * 1165), (int) ((view.getHeight() / heightRes) * 220));
		
/*****TOWER LIST*****/		
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 1; y++) {
				
			if (Tower.towerList[x] != null) { 
				g.drawImage(Tower.towerList[x].texture, (int)((view.getWidth() / widthRes) * (160 + (x * (int) towerWidth))), (int)((view.getHeight() / heightRes) * (580 + (y * (int) towerHeight))), (int) ((view.getWidth() / widthRes) * towerWidth), (int) ((view.getHeight() / heightRes) * towerHeight), null);
				
				if (Tower.towerList[x].cost > this.player.profile.money){ //If the player can't afford the purchase...
				  g.setColor(new Color(255, 0, 0, 100)); //Mask an unaffordable tower	
				  g.fillRect((int)((view.getWidth() / widthRes) * (160 + (x*50))), (int)((view.getHeight() / heightRes) * 580), (int)((view.getWidth() / widthRes) * towerWidth), (int)((view.getHeight() / heightRes) * towerHeight));	
				}
			
			}
			
			g.setColor(Color.BLACK);
			g.drawRect((int)((view.getWidth() / widthRes) * (160 + (x * (int) towerWidth))), (int)((view.getHeight() / heightRes) * (580 + (y * (int) towerHeight))), (int)((view.getWidth() / widthRes) * 50), (int)((view.getHeight() / heightRes) * 50));	
		    }
		}
/******TOWERS ON GRID*******/
		
		for(int x = 0; x < 17; x++) {
			for(int y = 0; y < 10; y++){
				if(towerMap[x][y] != null) {
					if(selectedTower == towerMap[x][y]){
						g.setColor(Color.GRAY);
						g.drawOval((int)((view.getWidth() / widthRes) * (towerWidth + (x * (int) towerWidth) - (towerMap[x][y].towerRange * 2 * (int) towerWidth + (int) towerWidth)/2 + (int) towerWidth / 2)), (int)((view.getHeight() / heightRes) * (towerHeight + (y * (int) towerHeight) - (towerMap[x][y].towerRange * 2 * (int) towerHeight + (int) towerHeight) / 2 + (int) towerHeight/2)), (int)((view.getWidth() / widthRes) * (towerMap[x][y].towerRange * 2 * (int) towerWidth + (int) towerWidth)), (int)((view.getHeight() / heightRes) * (towerMap[x][y].towerRange * 2 * (int) towerHeight + (int) towerHeight))); //Draw in the correct place [Grid Params]
					    g.setColor(new Color(64, 64, 64, 64));
					    g.fillOval((int)((view.getWidth() / widthRes) * (towerWidth + (x * (int) towerWidth) - (towerMap[x][y].towerRange * 2 * (int) towerWidth + (int) towerWidth)/2 + (int) towerWidth / 2)), (int)((view.getHeight() / heightRes) * (towerHeight + (y * (int) towerHeight) - (towerMap[x][y].towerRange * 2 * (int) towerHeight + (int) towerHeight) / 2 + (int) towerHeight/2)), (int)((view.getWidth() / widthRes) * (towerMap[x][y].towerRange * 2 * (int) towerWidth + (int) towerWidth)), (int)((view.getHeight() / heightRes) * (towerMap[x][y].towerRange * 2 * (int) towerHeight + (int) towerHeight)));
					}
				g.drawImage(Tower.towerList[towerMap[x][y].ID].texture, (int)((view.getWidth() / widthRes) * ( towerWidth + (x * (int) towerWidth))), (int)((view.getHeight() / heightRes) * (towerHeight + (y * (int) towerHeight))), (int)((view.getWidth() / widthRes) * towerWidth), (int)((view.getHeight() / heightRes) * towerHeight), null);
					
				}	
			}
		}
		
/********ROCKETS*********/
		Graphics2D rocket2D = (Graphics2D) g; //Casting the graphics object to Graphics2D for ease of rotation
		
		for (int i = 0; i < rockets.length; i++) {
			if (rockets[i] != null) {
				rocket2D.rotate(rockets[i].direction + Math.toRadians(90), (int) ((view.getWidth() / widthRes) * rockets[i].x), (int) ((view.getHeight() / heightRes) * rockets[i].y)); //Rotate CCW
				g.drawImage(rockets[i].texture, (int)((view.getWidth() / widthRes) * rockets[i].x), (int)((view.getHeight() / heightRes) * rockets[i].y), (int)((view.getWidth() / widthRes) * 10), (int)((view.getHeight() / heightRes) * 30), null); //Draw the rockets (last two params double width and height of rocket image)
			    rocket2D.rotate(-rockets[i].direction + Math.toRadians(-90), (int) ((view.getWidth() / widthRes) * rockets[i].x), (int) ((view.getHeight() / heightRes) * rockets[i].y)); //Rotate CW
			}
		}
		
/*******SHELLS*******/
		Graphics2D shell2D = (Graphics2D) g; //Casting the graphics object to Graphics2D for ease of rotation
				
				for (int i = 0; i < shells.length; i++) {
					if (shells[i] != null) {
						shell2D.rotate(shells[i].direction + Math.toRadians(90), (int) ((view.getWidth() / widthRes) * shells[i].x), (int) ((view.getHeight() / heightRes) * shells[i].y)); //Rotate CCW
						g.drawImage(shells[i].texture, (int)((view.getWidth() / widthRes) * shells[i].x), (int)((view.getHeight() / heightRes) * shells[i].y), (int)((view.getWidth() / widthRes) * 10), (int)((view.getHeight() / heightRes) * 30), null); //Draw the shells (last two params double width and height ofshell image)
					   shell2D.rotate(-shells[i].direction + Math.toRadians(-90), (int) ((view.getWidth() / widthRes) * shells[i].x), (int) ((view.getHeight() / heightRes) * shells[i].y)); //Rotate CW
					}
				}
				
				
/*******DARTS*******/
		Graphics2D dart2D = (Graphics2D) g; //Casting the graphics object to Graphics2D for ease of rotation
				
				for (int i = 0; i < darts.length; i++) {
					if (darts[i] != null) {
						dart2D.rotate(darts[i].direction + Math.toRadians(90), (int) ((view.getWidth() / widthRes) * darts[i].x), (int) ((view.getHeight() / heightRes) * darts[i].y)); //Rotate CCW
						g.drawImage(darts[i].texture, (int)((view.getWidth() / widthRes) * darts[i].x), (int)((view.getHeight() / heightRes) * darts[i].y), (int)((view.getWidth() / widthRes) * 10), (int)((view.getHeight() / heightRes) * 30), null); //Draw the darts (last two params double width and height of dart image)
					   dart2D.rotate(-darts[i].direction + Math.toRadians(-90), (int) ((view.getWidth() / widthRes) * darts[i].x), (int) ((view.getHeight() / heightRes) * darts[i].y)); //Rotate CW
					}
				}
					
/*******BULLETS*******/
		Graphics2D bullet2D = (Graphics2D) g; //Casting the graphics object to Graphics2D for ease of rotation
				
				for (int i = 0; i < bullets.length; i++) {
					if (bullets[i] != null) {
						bullet2D.rotate(bullets[i].direction + Math.toRadians(90), (int) ((view.getWidth() / widthRes) * bullets[i].x), (int) ((view.getHeight() / heightRes) * bullets[i].y)); //Rotate CCW
						g.drawImage(bullets[i].texture, (int)((view.getWidth() / widthRes) * bullets[i].x), (int)((view.getHeight() / heightRes) * bullets[i].y), (int)((view.getWidth() / widthRes) * 10), (int)((view.getHeight() / heightRes) * 30), null); //Draw the bullets (last two params double width and height of bullet image)
					   bullet2D.rotate(-bullets[i].direction + Math.toRadians(-90), (int) ((view.getWidth() / widthRes) * bullets[i].x), (int) ((view.getHeight() / heightRes) * bullets[i].y)); //Rotate CW
					}
				}		
				
				
/*******ICE*******/
		Graphics2D ice2D = (Graphics2D) g; //Casting the graphics object to Graphics2D for ease of rotation
				
				for (int i = 0; i < allice.length; i++) {
					if (allice[i] != null) {
						ice2D.rotate(allice[i].direction + Math.toRadians(90), (int) ((view.getWidth() / widthRes) * allice[i].x), (int) ((view.getHeight() / heightRes) * allice[i].y)); //Rotate CCW
						g.drawImage(allice[i].texture, (int)((view.getWidth() / widthRes) * allice[i].x), (int)((view.getHeight() / heightRes) * allice[i].y), (int)((view.getWidth() / widthRes) * 10), (int)((view.getHeight() / heightRes) * 30), null); //Draw the ice (last two params double width and height of ice image)
					   ice2D.rotate(-allice[i].direction + Math.toRadians(-90), (int) ((view.getWidth() / widthRes) * allice[i].x), (int) ((view.getHeight() / heightRes) * allice[i].y)); //Rotate CW
					}
				}	
	}
		
/********YOU WON BACKGROUND**********/		
	
		else if (scene == 2) {
			for(int i = 0; i < 17; i++){
				for(int j = 0; j < 10; j++){
					towerMap[i][j] = null;
				}
			}
			
			for(int i = 0; i < enemyMap.length; i++){
				enemyMap[i] = null;
			}
			b++;
			String winnerMessage = "YOU WON! Great game!";
			
			g.setColor(Color.YELLOW);
			g.fillRect(0, 0, this.view.getWidth(), this.view.getHeight());
			FontMetrics fm = getFontMetrics(new Font(Font.SANS_SERIF, Font.BOLD, (int)( view.getWidth() / widthRes * 50)));
			int messWidth = (fm.stringWidth(winnerMessage));
			int xPos = 0 + (this.view.getWidth() - messWidth)/2;
			int yPos = this.view.getHeight()/6;
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, (int)( view.getWidth() / widthRes * 50)));
			g.setColor(Color.BLACK);
			g.drawString(winnerMessage, xPos, yPos);
			g.drawString("Your score was: " + Profile.score, xPos, yPos * 2);
			messWidth = fm.stringWidth("Submit Score");
			g.drawString("Click Anywhere to Submit Score", xPos, yPos * 4);

			if(b==1){
				this.name = new JOptionPane();
				this.add(name);
				Name = name.showInputDialog("Enter Name: ");
			}
			
			//this.name = new TextField(20);
			//this.name.addTextListener(l);
			//this.add(name, BorderLayout.CENTER);
			//String Name = name.getText();	
		}
		
/********GAME OVER BACKGROUND*********/		
		
		else if (scene == 3) {//Scene 3 = You lost!
			for(int i = 0; i < 17; i++){
				for(int j = 0; j < 10; j++){
					towerMap[i][j] = null;
				}
			}
			
			for(int i = 0; i < enemyMap.length; i++){
				enemyMap[i] = null;
			}
			b++;
			String loserMessage = "YOU LOST! Try again?";
			g.setColor(Color.RED);
			g.fillRect(0, 0, this.view.getWidth(), this.view.getHeight());
			FontMetrics fm = getFontMetrics(new Font(Font.SANS_SERIF, Font.BOLD, (int)( view.getWidth() / widthRes * 50)));
			int messWidth = (fm.stringWidth(loserMessage));
			int xPos = 0 + (this.view.getWidth() - messWidth)/2;
			int yPos = this.view.getHeight()/6;
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, (int)( view.getWidth() / widthRes * 50)));
			g.setColor(Color.BLACK);
			g.drawString(loserMessage, xPos, yPos);
			g.drawString("Your score was: " + Profile.score, xPos, yPos * 2);
			g.drawString("Click Anywhere to Submit Score", xPos, yPos * 4);
			if(b==1){
				this.name = new JOptionPane();
				this.add(name);
				Name = name.showInputDialog("Enter Name: ");
			}
			
			//	this.name = new TextField(20);
			//this.name.addTextListener(l);
			//this.add(name);
			//name.setBounds(getWidth()/3, getHeight() / 6, getWidth()/3, getHeight()/40);
			//String Name = name.getText();	
			
		
		}
		
		else if(scene == 7){

			g.setColor(Color.BLACK);
			g.setFont(new Font(null, Font.PLAIN, 30));
			g.drawString("HIGH SCORES", (getWidth()/3), getWidth() / 20);
			g.setFont(new Font(null, Font.PLAIN, 20));
			for(int i = 0; i < scores.size()/2; i++){
				g.drawString(scores.get(i), (getWidth()/3), (i+2)*getHeight()/ 15);
				g.drawString(scores.get(i + scores.size()/2), 2 * getWidth() / 3, (i+2)*getHeight()/ 15);
			}
			
			g.drawString("Press BACKSPACE to go back to the title", (getWidth()/3), 13* getHeight() / 15);
		}
		
		else {
			g.setColor(Color.WHITE); //Anywhere else (Fault checker)
			g.fillRect(0, 0, this.view.getWidth(), this.view.getHeight());
		}
		
		//g.drawString(fps + "", 10, 10); //Print the FPS on screen
		
	}
	
	/** MainMenu *************************************************************
	 * Description: The Main Menu interface the user sees on startup.
	 * A message is displayed, along with instructions to start.
	 * @params - Graphics g: A graphics object we can use for drawing
	 * @modifies Sets the initial GUI to a menu with text and blue background
	 * @precond scene = 0 (Game just started up)
	 * @postcond Menu displayed to the user
	 * ***********************************************************************/
	
	private void MainMenu(Graphics g) {
			
		g.setColor(Color.BLUE); //Pre-game menu holder
		g.fillRect(0, 0, this.view.getWidth(), this.view.getHeight());
		g.setColor(Color.ORANGE);
		g.setFont(new Font(Font.SERIF, Font.PLAIN, (int)((view.getWidth() / widthRes) * 14)));
	//	g.drawString((pointerXPos + "," + pointerYPos), (int) ((view.getWidth() / widthRes) * 1165), (int) ((view.getHeight() / heightRes) * 40));
		String startMessage = "Welcome to jTowerDefense!";// Press Space to Begin!";
		
		FontMetrics fm = getFontMetrics(new Font(Font.SERIF, Font.BOLD, (int)( view.getWidth() / widthRes * 50)));
		messWidth = (fm.stringWidth("Start Normal"));
		xPos = 0 + (this.view.getWidth())/3;
		int yPos = this.view.getHeight()/10;
		g.setFont(new Font(Font.SERIF, Font.BOLD, (int)( view.getWidth() / widthRes * 50)));
		g.setColor(Color.GREEN);
		g.fillRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)((this.view.getHeight() / 5) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		g.fillRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)(3 * (this.view.getHeight() / 10) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		g.fillRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)(2 * (this.view.getHeight() / 5) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		g.fillRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)((this.view.getHeight() / 2) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		g.setColor(Color.YELLOW);
		g.fillRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)(3 * (this.view.getHeight() / 5) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		g.setColor(Color.RED);
		g.fillRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)(7 * (this.view.getHeight() / 10) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		g.setColor(Color.BLACK);
		g.drawRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)((this.view.getHeight() / 5) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		g.drawRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)(3 * (this.view.getHeight() / 10) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		g.drawRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)(2 * (this.view.getHeight() / 5) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		g.drawRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)((this.view.getHeight() / 2) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		g.drawRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)(3 * (this.view.getHeight() / 5) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		g.drawRect((int)(xPos - ((this.view.getWidth()/widthRes) * messWidth /2)),(int)(7 * (this.view.getHeight() / 10) + (this.view.getHeight() * .05)),(int) (this.view.getWidth()/widthRes) * messWidth * 2,(int)(.06 * this.view.getHeight()));
		
		g.drawString(startMessage, xPos - (int)((this.view.getWidth()/widthRes) * (messWidth / 2)), yPos);
		g.drawString("Start Easy", xPos, (int)((this.view.getHeight() / 5) + .1 * this.view.getHeight()));
		g.drawString("Start Normal", xPos, (int)(3 * (this.view.getHeight() / 10) + .1 * this.view.getHeight()));
		g.drawString("Start Hard", xPos, (int)(2 * (this.view.getHeight() / 5) + .1 * this.view.getHeight()));
		g.drawString("Start Insane", xPos, (int)((this.view.getHeight() / 2) + .1 * this.view.getHeight()));
		g.drawString("High Scores", xPos, (int)(3 * (this.view.getHeight() / 5) + .1 * this.view.getHeight()));
		g.drawString("Quit", xPos, (int)(7 * (this.view.getHeight() / 10) + .1 * this.view.getHeight()));
		
	}

	
	public void actionPerformed(ActionEvent e) {
		if ("start".equals(e.getActionCommand())){ //Start the game
		} else {
			System.exit(0);//Close game
		}
	} 
	
	/** createGame *************************************************************
	 * Description: createGame makes instances of all necessary components of the game,
	 * including a player, the map file and terrain and waves. It also sets running
	 * to true so the game can start and update itself.
	 * @params - none
	 * @modifies Player, mapFile, wave instantiated. Terrain image file
	 * parsed for individual blocks
	 * @precond No game has been started
	 * @postcond The game's data and components are created and the terrain
	 * initially painted.
	 * ***********************************************************************/

	public void createGame() {
		
		player = new Player(this);
		mapFile = new MapFile();
		wave = new Wave(this);
		
		ClassLoader cl = this.getClass().getClassLoader();
		
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				terrain[x + (y*10)] = new ImageIcon(cl.getResource(packageName + "/terrain.png")).getImage();
				terrain[x + (y*10)] = createImage(new FilteredImageSource(terrain[x + (y*10)].getSource(), new CropImageFilter(x*25, y*25, 25, 25)));
			}
		}
		
		running = true;
		
	}
	
	/** beginGame *************************************************************
	 * Description: beginGame initializes the game (assigns the player and map created by createGame)
	 * and locates the spawnPoint from which enemies stream.
	 * @params - player: The user of the game, with a starting health and money amount
	 * 			 map: The array of numbers that correspond to the play surface
	 * @modifies scene: Changes to 1 (game playing)
	 *           wave.waveNumer: Starts at Wave0
	 * @precond Game has not been previously created.
	 * @postcond The game is initialized and ready for the user to play.
	 * ***********************************************************************/
	
	public void beginGame(Player player, String map, double difficulty) {
		
		player.createProfile();		
		b = 0;
		this.map = mapFile.getMap(map);
		this.map.findSpawnPoint();
		this.arena = this.map.arena;
		this.enemyPath = new EnemyPath(this.map);
		this.scene = 1; //Game playing state
		this.wave.waveNumber = 0; //Start at Wave0
		this.wave.maxWaveNumber = (int)(15 * difficulty);
	}
	
	/** run *************************************************************
	 * Description: The game's runtime function. It updates the view by
	 * calling repaint as long as the game is running, and exits otherwise.
	 * @params - none
	 * @modifies none
	 * @precond The user has started the game
	 * @postcond GUI/view updated periodically according to thread parameter.
	 * ***********************************************************************/
	
	public void run() {
		
		//long lastView = System.currentTimeMillis();
		//int frames = 0;
		
		createGame(); //Initiate game creation
		
		while (running) {
			
			repaint();
		
			
				
			//frames++;
			
			/*if (System.currentTimeMillis() - 1000 >= lastView) { //At 1000 milliseconds (1 second), set the fps
				//fps = frames;
				frames = 0;
				lastView = System.currentTimeMillis();
			}*/
			
			if(paused == false && scene == 1){
				update(); //Send the enemies onto the screen
			}
			
			/*double time = (double)System.currentTimeMillis() / 1000;
			int timeMilliSeconds = (int) Math.round(time - (int) time) * 1000;
			
			if (timeMilliSeconds > synchronized_fps * 1000/25) { //Manage the frames per second of the screen to reduce lag
				
			 synchronized_fps++;
			 update(); //Send the enemies onto the screen
			 
			 if (synchronized_fps == 1000/25) { //One second has passed
				synchronized_fps = 0;
			 }
			
			}
			
			if (timeMilliSeconds + 1000/25 < synchronized_fps * 1000/25) {
				synchronized_fps = 0;
			}*/
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			  }
			
		}
		
		System.exit(0);
		
	}
	
	/** enemyUpdate *************************************************************
	 * Description: Updates the image of an enemy. This includes movement and death.
	 * @params - none
	 * @modifies enemyMap[i]: The map of enemies on top of the arena is updated
	 * when the enemy dies by being assigned to null.
	 * @precond For movement, enemy must exist. Player must also still have lives for
	 * enemies to update, otherwise the game is over.
	 * @postcond A dead enemy is deleted from the screen visually. If the player is
	 * dead, the scene is advanced to the game over screen. Enemies move to their
	 * new square by running the movePath algorithm, provided they have not reached
	 * the penultimate square to the goal.
	 * **************************************************************************/

	public void enemyUpdate() {
		for(int i = 0 ; i < enemyMap.length; i++) {
			if(enemyMap[i] != null) {
				//if(!enemyMap[i].attack) { //If this particular enemy can't attack (haven't reached the goal)
					EnemyPath.movePath.move(enemyMap[i]); // Move the enemy!
				//}
				
				enemyMap[i] = enemyMap[i].update(); //If enemy is dead, update the enemy map and obliterate them! (null assigned)
				
				if (Profile.lives <= 0) {
					scene = 3;
				}
				
               if (wave.waveNumber == wave.maxWaveNumber && Profile.killCount == (5*(Math.pow((wave.maxWaveNumber),2)) + 5*(wave.maxWaveNumber))) { //Win condition: Final round and total kill count = 5s^2 + 5s
					scene = 2;
				}
			}
		}
	}
	
	/** towerUpdate *************************************************************
	 * Description: Tries an attack if a tower exists.
	 * @params - none
	 * @modifies none
	 * @precond There is a tower placed on the map.
	 * @postcond towerAttack is called (tried).
	 * ***********************************************************************/
	
	
	public void towerUpdate() {
		for (int x = 0; x < 17; x++) { //Loop to locate all towers
			for (int y = 0; y < 10; y++) {
				if (towerMap[x][y] != null) { //If there is a tower
					towerAttack(x,y); //Try attacking
				//	clickSellTower(x, y);
				}
			}
		}
	}
	
	/** towerAttack *************************************************************
	 * Description: towerAttack attacks and targets an enemy in range of a tower.
	 * @params - int x: The x-coordinate of a tower (in tiles) that is laid on the map.
	 * int y: The y-coordinate of a tower (in tiles) that is laid on the map.
	 * @modifies towerMap[x][y].target: Updated to null after we fire on an enemy.
	 * currentEnemy.health: Reduced according to the damage of a tower's attack.
	 * towerMap[x][y].attackTime: Reset after making an attack.
	 * towerMap[x][y].attackDelay: Reset after making an attack.
	 * @precond For attack, an enemy must be in range. For targeting, we must
	 * not have a current target.
	 * @postcond Enemies are damaged by the tower that hit them. A new target
	 * is acquired if there is still an enemy in the tower's range. All
	 * tower timers are updated.
	 * ***********************************************************************/
	
	public void towerAttack(int x, int y) {
		
		if(this.towerMap[x][y].target == null) { //If tower does not have an enemy targeted
			//Find a target!
			if (this.towerMap[x][y].attackDelay > this.towerMap[x][y].maxAttackDelay) {
				EnemyMotion currentEnemy = this.towerMap[x][y].calculateEnemy(enemyMap, x, y);
				
				if (currentEnemy != null) { //If the enemy isn't dead yet
					this.towerMap[x][y].towerAttack(x, y, currentEnemy);
					
					this.towerMap[x][y].target = currentEnemy; //Target is the current enemy
					this.towerMap[x][y].attackTime = 0; 
					this.towerMap[x][y].attackDelay = 0;
					
					//System.out.println("GOTCHA" + currentEnemy.health);
					
				}
				
			} else {
				this.towerMap[x][y].attackDelay += 0.5; //Increase the current attack delay
			}
		} else { //If we are targeting an enemy
			if (this.towerMap[x][y].attackTime < this.towerMap[x][y].maxAttackTime) { //If the attack time is less than the max
				this.towerMap[x][y].attackTime += 1; //Update the attack time
				
			}
			else {
				this.towerMap[x][y].target = null; //After we are done firing, relocate a target
			}
		}
		
	}
	
	/** rocketUpdate *****************************************************************************
	 * Description: Updates the rockets on the screen by calling rockets[i].update() which
	 * has them track a target, and deletes the rocket when it kills an enemy (target is null)
	 * @params - none
	 * @modifies Rockets deleted from screen if target hit. Otherwise, follow target while in range
	 * @precond A rocket has been fired by a rocket tower.
	 * @postcond The GUI is updated according to the necessary state of the rocket. If it hits an
	 * enemy, it is deleted. If it needs to follow an enemy, it does.
	 * ******************************************************************************************/
	
	public void rocketUpdate () {
		for (int i = 0; i< rockets.length; i++) {
			if(rockets[i] != null) {
				rockets[i].update();
				
				if(rockets[i].target == null) {
					rockets[i] = null; //Remove fired rockets that have hit
				}
				
			}
		}
	}
	
	/** shellUpdate *****************************************************************************
	 * Description: Updates the shells on the screen by calling shells[i].update() which
	 * has them track a target, and deletes the shell when it kills an enemy (target is null)
	 * @params - none
	 * @modifies Shells deleted from screen if target hit. Otherwise, follow target while in range
	 * @precond A shell has been fired by an artillery tower.
	 * @postcond The GUI is updated according to the necessary state of the shell. If it hits an
	 * enemy, it is deleted. If it needs to follow an enemy, it does.
	 * ******************************************************************************************/
	
	public void shellUpdate () {
		for (int i = 0; i< shells.length; i++) {
			if(shells[i] != null) {
				shells[i].update();
				
				if(shells[i].target == null) {
					shells[i] = null; //Remove fired shells that have hit
				}
				
			}
		}
	}
	
	/** dartUpdate *****************************************************************************
	 * Description: Updates the darts on the screen by calling darts[i].update() which
	 * has them track a target, and deletes the dart when it kills an enemy (target is null)
	 * @params - none
	 * @modifies darts deleted from screen if target hit. Otherwise, follow target while in range
	 * @precond A dart has been fired by a poison tower.
	 * @postcond The GUI is updated according to the necessary state of the dart. If it hits an
	 * enemy, it is deleted. If it needs to follow an enemy, it does.
	 * ******************************************************************************************/
	
	public void dartUpdate () {
		for (int i = 0; i< darts.length; i++) {
			if(darts[i] != null) {
				darts[i].update();
				
				if(darts[i].target == null) {
					darts[i] = null; //Remove fired darts that have hit
				}
				
			}
		}
	}
	
	/** bulletUpdate *****************************************************************************
	 * Description: Updates the bullets on the screen by calling bullets[i].update() which
	 * has them track a target, and deletes the bullet when it kills an enemy (target is null)
	 * @params - none
	 * @modifies bullets deleted from screen if target hit. Otherwise, follow target while in range
	 * @precond A bullet has been fired by a machine gun tower.
	 * @postcond The GUI is updated according to the necessary state of the bullet. If it hits an
	 * enemy, it is deleted. If it needs to follow an enemy, it does.
	 * ******************************************************************************************/
	
	public void bulletUpdate () {
		for (int i = 0; i< bullets.length; i++) {
			if(bullets[i] != null) {
				bullets[i].update();
				
				if(bullets[i].target == null) {
					bullets[i] = null; //Remove fired bullets that have hit
				}
				
			}
		}
	}
	
	/** iceUpdate *****************************************************************************
	 * Description: Updates the ice on the screen by calling allice[i].update() which
	 * has them track a target, and deletes the ice when it kills an enemy (target is null)
	 * @params - none
	 * @modifies ice deleted from screen if target hit. Otherwise, follow target while in range
	 * @precond ice has been fired by a freeze tower.
	 * @postcond The GUI is updated according to the necessary state of the ice. If it hits an
	 * enemy, it is deleted. If it needs to follow an enemy, it does.
	 * ******************************************************************************************/
	
	public void iceUpdate () {
		for (int i = 0; i< allice.length; i++) {
			if(allice[i] != null) {
				allice[i].update();
				
				if(allice[i].target == null) {
					allice[i] = null; //Remove fired ice that have hit
				}
				
			}
		}
	}
	
	/** update *************************************************************************
	 * Description: update updates the GUI for various changes such as enemy death,
	 * tower placement, firing projectiles, and wave sending.
	 * @params - none
	 * @modifies none
	 * @precond An update is required. If a wave is to be sent, waveSpawning must be true
	 * @postcond The GUI is updated according to the necessary state of enemies and towers.
	 * If a wave should be sent, it is.
	 * ********************************************************************************/
	
	public void update() {
		
		enemyUpdate();
		towerUpdate();
		rocketUpdate();
		shellUpdate();
		dartUpdate();
		bulletUpdate();
		iceUpdate();
		
		if (wave.waveSpawning) { //If we are ready to spawn a wave, do it
			wave.sendWave();
		}
		
	}
	
	/** spawnEnemy *************************************************************
	 * Description: spawnEnemy creates a new enemy object in the enemy list at the spawn point.
	 * @params - none
	 * @modifies enemyMap[i]: The map of enemies on top of the arena is updated
	 * by a new enemy from the enemyList at the specified spawnPoint (Code 2)
	 * @precond No enemy currently shares the same ID of the enemy to be created.
	 * @postcond An enemy is created and added to the enemyMap (done once per call)
	 * Note: This function does not DRAW the enemy. That is handled by ENEMIES in
	 * paintComponent.
	 * ***********************************************************************/
	
	public void spawnEnemy(int enemyID) {
		
		for (int i = 0; i < enemyMap.length; i++) {
			
			if (enemyMap[i] == null) { //Make sure the enemy spawned is unique
				enemyMap[i] = new EnemyMotion(Enemy.enemyList[enemyID], map.spawnPoint);
				break;
			}
			
		}
		
	}
	
	/** placeTower *************************************************************
	 * Description: placeTower adds a tower to the towerMap laid over the arena.
	 * @params - x: The x coordinate of the tower placement.
	 *           y: The y coordinate of the tower placement.
	 * @modifies towerMap[xPos][yPos]: The map of towers on top of the arena is updated
	 * by a new tower from the towerList at the specified point (Code 0)
	 * @precond No tower has been created or drawn where we want to place our tower.
	 * @postcond A tower is created and added to the towerMap.
	 * 			 The tower's cost is deducted from the user's money.
	 * Note: This function does not DRAW the tower. That is handled by TOWERS ON GRID in
	 * paintComponent.
	 * ***********************************************************************/
	
	public void placeTower(int x, int y) {
		int xPos = (int)((x / (view.getWidth() / widthRes)) / (int)towerWidth);
		int yPos = (int)(((y / (view.getHeight() / heightRes)) / (int)towerHeight));
		
		if(xPos > 0 && xPos<= 17 && yPos > 0 && yPos <= 10) { //Ensure we are on the grid before proceeding to placing operation
			
			xPos -= 1;
			yPos -= 1;
			
			if(towerMap[xPos][yPos] == null && arena[xPos][yPos] == 0) { //Making sure there is not a tower where we intend to place
				
				player.profile.money -= Tower.towerList[pointer-1].cost; //Deduct the cost from the player's funds
				towerMap[xPos][yPos] = (Tower) Tower.towerList[pointer-1].towerClone(); //Actually place the tower on the tower map
				selectedTower = towerMap[xPos][yPos];	
			}	
		}
	}
	
	public void selectTower(int x, int y){
		selXPos = (int)((x / (view.getWidth() / widthRes)) / (int)towerWidth);
		selYPos = (int)(((y / (view.getHeight() / heightRes)) / (int)towerHeight));
		
		if (selXPos > 0 && selXPos<= 17 && selYPos > 0 && selYPos <= 10) { //Ensure we are on the grid before proceeding to placing operation
			
			selXPos -= 1;
			selYPos -= 1;
			selectedTower = towerMap[selXPos][selYPos];
		}	
		
		else if (x > (getWidth()/widthRes) *(1165 + (int) towerWidth/2) && x < (getWidth()/widthRes) * (1165 + towerWidth/2 + (int) towerWidth*3) && ((y > (view.getHeight() / heightRes) * (600 + (int) towerHeight/2) && y < (view.getHeight() / heightRes) * ((600 + (int) towerHeight/2) + (towerHeight*2 + towerHeight)/4)) || (y > (view.getHeight() / heightRes)*(650 + (int) towerHeight/2) && y < (view.getHeight() / heightRes)*((650 + (int) towerHeight/2) + (towerHeight*2 + (int) towerHeight)/4)))){
			selectedTower = towerMap[selXPos][selYPos];
				
		}
		else {
			selectedTower = null;
		}
	}
	
	
public void clickSellTower(int x, int y) {
		
		if (selectedTower != null && x > (view.getWidth() / widthRes) * (1165 + (int) towerWidth/2) && x < (view.getWidth() / widthRes) * ((1165 + (int) towerWidth/2) + (int) towerWidth*3) && y >((view.getHeight() / heightRes) * (550 + (int) towerHeight/2)) && y < (view.getHeight() / heightRes) * ((550 + (int) towerHeight/2) + (towerHeight*2 + (int) towerHeight)/4)) {
		
			towerMap[selXPos][selYPos] = null;
			Profile.money += (selectedTower.cost + (selectedTower.level/2) * selectedTower.cost); //Sell the tower for a cost that is a percentage of its worth
			
		}
	}


/** upgradeTower *************************************************************
 * Description: upgradeTower allows the user to upgrade their tower, increasing
 * its damage and range, until a certain max upgrade level.
 * @params - x: The x coordinate of the desired tower.
 *           y: The y coordinate of the desired tower.
 * @modifies Tower selectedTower.*: Attributes of the tower such as its
 * damage and range
 * @precond A tower exists to be upgraded, and has not reached its max level
 * @postcond The tower is upgraded, and its attributes reflect the operation.
 * ***********************************************************************/
public void upgradeTower(int x, int y) {
	
	if (selectedTower != null && x > (view.getWidth() / widthRes) * (1165 + (int) towerWidth/2) && x < (view.getWidth() / widthRes) * ((1165 + (int) towerWidth/2) + (int) towerWidth*3) && y > (view.getHeight() / heightRes) * (600 + (int) towerHeight/2) && y < (view.getHeight() / heightRes) * ((600 + (int) towerHeight/2) + (towerHeight*2 + towerHeight)/4)) {
		if (Profile.money >= (selectedTower.cost * (selectedTower.level + 1.5)) && selectedTower.level < Tower.maxLevel) { //Can't upgrade with no money or when level is max!
		selectedTower.towerRange += Math.round(0.5*selectedTower.towerRange); //Range upgrade	
		selectedTower.towerRange = Math.min(selectedTower.towerRange, 10); //Make sure the range doesn't exceed a certain max value
		selectedTower.damage += Math.ceil(0.5*selectedTower.damage); //Damage Upgrade
		selectedTower.maxAttackDelay = 0.9*selectedTower.maxAttackDelay; //Firing delay reduction
		selectedTower.stun = (int) (selectedTower.stun * 1.5);
		selectedTower.venom = (int)(selectedTower.venom * 1.5);
		Profile.money -= (selectedTower.cost * (selectedTower.level + 1.5));
		selectedTower.level++;
		selectedTower = towerMap[selXPos][selYPos];
		
		}
		
	}
}

/** downgradeTower *************************************************************
 * Description: downgradeTower allows the user to sell a previous tower upgrade.
 * @params - x: The x coordinate of the desired tower.
 *           y: The y coordinate of the desired tower.
 * @modifies Tower selectedTower.*: Attributes of the tower such as its
 * damage and range
 * @precond A tower exists to be upgraded, and has not reached its max level
 * @postcond The tower is downgraded, and its attributes reflect the operation.
 * ***********************************************************************/

public void downgradeTower(int x, int y) {
	//(int)((view.getHeight() / heightRes)*(650 + (int) towerHeight/2)),(int) ((view.getWidth() / widthRes)*towerWidth*3), ((int) ((view.getHeight() / heightRes) * (towerHeight*2 + (int) towerHeight)/4))
	if (selectedTower != null && x > (view.getWidth() / widthRes) * (1165 + (int) towerWidth/2) && x < (view.getWidth() / widthRes) * ((1165 + (int) towerWidth/2) + (int) towerWidth*3) && y > (view.getHeight() / heightRes)*(650 + (int) towerHeight/2) && y < (view.getHeight() / heightRes)*((650 + (int) towerHeight/2) + (towerHeight*2 + (int) towerHeight)/4)){
		
		if (selectedTower.level > 0) { //Can't upgrade with no money or when level is max!
		selectedTower.towerRange = (int) Math.floor(selectedTower.towerRange / 1.5); //Range downgrade	
		selectedTower.damage = (int) Math.floor((selectedTower.damage)/1.5); //Damage downgrade
		selectedTower.maxAttackDelay = selectedTower.maxAttackDelay / 0.9; //Delay downgrade
		
		selectedTower.level--; //Decrement level after downgrade
		Profile.money += selectedTower.cost * (selectedTower.level + 1.5); //Return money to user
		selectedTower = towerMap[selXPos][selYPos];
		
		}
		
	}
}

	public class MouseHeld {
		
		boolean mouseDown = false;
		
		public void mouseUp(MouseEvent e){
			selectTower(e.getX(), e.getY());
			updateMouse(e);
			mouseDown = false;
		}
		
		public void mouseDown(MouseEvent e) {
			
			mouseDown = true;
			
			if (pointer!= 0) { //If there is a pending tower placement
				
				/****PLACE TOWER*****/
				placeTower(e.getX(), e.getY());
				
				pointer = 0; //Tower placed - pending action completed and released
				pointerXPos = -25; //Remove annoying 'last click display' by placing the box outside the view
				pointerYPos = -25; // "" "" ""
				
			}
			
			else {
				clickSellTower(e.getX(), e.getY());
				upgradeTower(e.getX(), e.getY());
				downgradeTower(e.getX(), e.getY());
			}
			
			updateMouse(e); //Make sure the mouse gets the updated value of pointer		
		}
		
		public void updateMouse (MouseEvent e) {
			if (scene == 1) {//If we are playing the game currently
				if(pointer == 0){ //If we haven't clicked and nothing is pending
					
							//Rocket Tower
							if (e.getX() >= ((view.getWidth() / widthRes) * (160)) && e.getX() <= ((view.getWidth() / widthRes) * (210)) && e.getY() >= ((view.getHeight() / heightRes) * (580)) && e.getY() <= ((view.getHeight() / heightRes) * (630))){ //Make sure Rocket Tower is picked
								
								if (player.profile.money >= Tower.towerList[0].cost) {
								pointer = 1; //A tower placement is pending on the cursor
								
								selectedTower = Tower.towerList[0];
								}
						
						    }
							
							//Artillery Tower
							if (e.getX() >= ((view.getWidth() / widthRes) * (210)) && e.getX() <= ((view.getWidth() / widthRes) * (260)) && e.getY() >= ((view.getHeight() / heightRes) * (580)) && e.getY() <= ((view.getHeight() / heightRes) * (630))){ //Make sure Artillery Tower is picked
								
								if (player.profile.money >= Tower.towerList[1].cost){
									pointer = 2; //A tower placement is pending on the cursor
									
									selectedTower = Tower.towerList[1];
								}
						
						    }
							
							//Poison Tower
							if (e.getX() >= ((view.getWidth() / widthRes) * (260)) && e.getX() <= ((view.getWidth() / widthRes) * (310)) && e.getY() >= ((view.getHeight() / heightRes) * (580)) && e.getY() <= ((view.getHeight() / heightRes) * (630))){ //Make sure Poison Tower is picked
								
								if (player.profile.money >= Tower.towerList[2].cost){
									pointer = 3; //A tower placement is pending on the cursor
									
									selectedTower = Tower.towerList[2];
								}
						
						    }
							
							//MachineGun Tower
							
							if (e.getX() >= ((view.getWidth() / widthRes) * (310)) && e.getX() <= ((view.getWidth() / widthRes) * (360)) && e.getY() >= ((view.getHeight() / heightRes) * (580)) && e.getY() <= ((view.getHeight() / heightRes) * (630))){ //Make sure Machine Gun Tower is picked
								
								if (player.profile.money >= Tower.towerList[3].cost){
									pointer = 4; //A tower placement is pending on the cursor
									
									selectedTower = Tower.towerList[3];
								}
						
						    }
							
							//Freeze Tower
							
							if (e.getX() >= ((view.getWidth() / widthRes) * (360)) && e.getX() <= ((view.getWidth() / widthRes) * (410)) && e.getY() >= ((view.getHeight() / heightRes) * (580)) && e.getY() <= ((view.getHeight() / heightRes) * (630))){ //Make sure Freeze Tower is picked
								
								if (player.profile.money >= Tower.towerList[4].cost){
									pointer = 5; //A tower placement is pending on the cursor
								
									selectedTower = Tower.towerList[4];
								}
						
						    }	
				}
			}
		}

	}

	public class KeyPressed {
		
		public void KeyESC(){ //Close game on ESC key press
			running = false;
			
		}
		
		public void KeyENTER() { //Hotkey for wave advancement
			
			if (wave.waveSpawning == false && scene == 1) { //Make sure haven't yet sent a wave and don't overlap waves
			wave.nextWave();
			}
			
			if (scene == 3 || scene == 2) { //If game over, Enter also closes game
				running = false;
			}
			
		}

		public void KeySPACE() { //Hotkey for scene advancement
			
			if (scene == 0) { //If the game hasn't started
			
				Player.difficulty = .34;
			    beginGame(player, "Map0", (.34));
			
			}
		}
		
		public void KeyONE(){
			if(scene == 1){
				if(selectedTower != null){
					selectedTower.attackStrategy = "FIRST";
				}
			}
		}
		
		public void KeyTWO(){
			if(scene == 1){
				if(selectedTower != null){
					selectedTower.attackStrategy = "RANDOM";
				}
			}
		}
		
		public void KeyP(){
			if (scene == 1){
				if(paused == false){
					paused = true;
					return;
				}
				
				if(paused == true){
					paused = false;
					return;
				}
			}
		}

		public void KeyBACK() {
			if(scene == 7){
				scene = 0;
			}
			
		}
	}
	
public class MouseClicked {
	boolean mouseClicked = false;
		public void selTower(MouseEvent e){
			mouseClicked = true;
			if (pointer!= 0) { //If there is a pending tower placement
				
				/****PLACE TOWER*****/
				placeTower(e.getX(), e.getY());
				
				pointer = 0; //Tower placed - pending action completed and released\
				pointerXPos = -25; //Remove annoying 'last click display' by placing the box outside the view
				pointerYPos = -25; // "" "" ""
				
			}
	
			else{
				//clickSellTower(e.getX(), e.getY());
				//upgradeTower(e.getX(), e.getY());
				//downgradeTower(e.getX(), e.getY());
			}
			
			updateMouse(e); //Make sure the mouse gets the updated value of pointer
		}
		
		public void mouseMoved(MouseEvent e) {
			pointerXPos = e.getX();
			pointerYPos = e.getY();

		}
		
		public void endClick(MouseEvent e){
			if(scene == 2 || scene == 3){
				System.out.println(Name);
				if(e.getX() > 0 && e.getY() > 0){	
					try {
						Highscore.pushScore(Name, Profile.score);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						scores = Highscore.getHighscores();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					scene = 7;
					
				}
			}
		}
		
		public void menuClick(MouseEvent e){
			if(scene == 0){
				if(e.getX() > (xPos - ((view.getWidth()/widthRes) * messWidth /2)) && e.getX() < (xPos - ((view.getWidth()/widthRes) * messWidth /2)) + 2 * messWidth && e.getY() > ((view.getHeight() / 5) + (view.getHeight() * .05)) && e.getY() < ((view.getHeight() / 5) + (view.getHeight() * .05)) + (.06 * view.getHeight())){
					Player.difficulty = .3333;
					beginGame(player, "Map0", (.34));
				}
		
				if(e.getX() > (xPos - ((view.getWidth()/widthRes) * messWidth /2)) && e.getX() < (xPos - ((view.getWidth()/widthRes) * messWidth /2)) + 2 * messWidth && e.getY() > (3 * (view.getHeight() / 10) + (view.getHeight() * .05)) && e.getY() < ((3 * view.getHeight() / 10) + (view.getHeight() * .05)) + (.06 * view.getHeight())){
					Player.difficulty = .6666;
					beginGame(player, "Map1", (.67));
				}
		
				if(e.getX() > (xPos - ((view.getWidth()/widthRes) * messWidth /2)) && e.getX() < (xPos - ((view.getWidth()/widthRes) * messWidth /2)) + 2 * messWidth && e.getY() > ((2 * view.getHeight() / 5) + (view.getHeight() * .05)) && e.getY() < ((2 * view.getHeight() / 5) + (view.getHeight() * .05)) + (.06 * view.getHeight())){
					Player.difficulty = 1.0;
					beginGame(player, "Map2", 1.0);
				}
		
				
				if(e.getX() > (xPos - ((view.getWidth()/widthRes) * messWidth /2)) && e.getX() < (xPos - ((view.getWidth()/widthRes) * messWidth /2)) + 2 * messWidth && e.getY() > ((view.getHeight() / 2) + (view.getHeight() * .05)) && e.getY() < ((view.getHeight() / 2) + (view.getHeight() * .05)) + (.06 * view.getHeight())){
					Player.difficulty = 44.4;
					beginGame(player, "Map3", 44.4);
				}
				
				if(e.getX() > (xPos - ((view.getWidth()/widthRes) * messWidth /2)) && e.getX() < (xPos - ((view.getWidth()/widthRes) * messWidth /2)) + 2 * messWidth && e.getY() > ((3 * view.getHeight() / 5) + (view.getHeight() * .05)) && e.getY() < ((3 * view.getHeight() / 5)+ (view.getHeight() * .05)) + (.06 * view.getHeight())){
					try {
						scores = Highscore.getHighscores();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					scene = 7;
				}
				
				if(e.getX() > (xPos - ((view.getWidth()/widthRes) * messWidth /2)) && e.getX() < (xPos - ((view.getWidth()/widthRes) * messWidth /2)) + 2 * messWidth && e.getY() > ((7 * view.getHeight() / 10) + (view.getHeight() * .05)) && e.getY() < ((7 * view.getHeight() / 10)+ (view.getHeight() * .05)) + (.06 * view.getHeight())){
					running = false;
				}	
			}
		}
		
		public void updateMouse (MouseEvent e) {
			if (scene == 1) {//If we are playing the game currently
				if(pointer == 0){ //If we haven't clicked and nothing is pending
					
							//Rocket Tower
							if (e.getX() >= ((view.getWidth() / widthRes) * (160)) && e.getX() <= ((view.getWidth() / widthRes) * (210)) && e.getY() >= ((view.getHeight() / heightRes) * (580)) && e.getY() <= ((view.getHeight() / heightRes) * (630))){ //Make sure Rocket Tower is picked
								
								if (player.profile.money >= Tower.towerList[0].cost) {
								pointer = 1; //A tower placement is pending on the cursor
								
								selectedTower = Tower.towerList[0];
								}
						
						    }
							
							//Artillery Tower
							if (e.getX() >= ((view.getWidth() / widthRes) * (210)) && e.getX() <= ((view.getWidth() / widthRes) * (260)) && e.getY() >= ((view.getHeight() / heightRes) * (580)) && e.getY() <= ((view.getHeight() / heightRes) * (630))){ //Make sure Artillery Tower is picked
								
								if (player.profile.money >= Tower.towerList[1].cost){
									pointer = 2; //A tower placement is pending on the cursor
									
									selectedTower = Tower.towerList[1];
								}
						
						    }
							
							//Poison Tower
							if (e.getX() >= ((view.getWidth() / widthRes) * (260)) && e.getX() <= ((view.getWidth() / widthRes) * (310)) && e.getY() >= ((view.getHeight() / heightRes) * (580)) && e.getY() <= ((view.getHeight() / heightRes) * (630))){ //Make sure Poison Tower is picked
								
								if (player.profile.money >= Tower.towerList[2].cost){
									pointer = 3; //A tower placement is pending on the cursor
									
									selectedTower = Tower.towerList[2];
								}
						
						    }
							
							//MachineGun Tower
							
							if (e.getX() >= ((view.getWidth() / widthRes) * (310)) && e.getX() <= ((view.getWidth() / widthRes) * (360)) && e.getY() >= ((view.getHeight() / heightRes) * (580)) && e.getY() <= ((view.getHeight() / heightRes) * (630))){ //Make sure Machine Gun Tower is picked
								
								if (player.profile.money >= Tower.towerList[3].cost){
									pointer = 4; //A tower placement is pending on the cursor
									
									selectedTower = Tower.towerList[3];
								}
						
						    }
							
							//Freeze Tower
							
							if (e.getX() >= ((view.getWidth() / widthRes) * (360)) && e.getX() <= ((view.getWidth() / widthRes) * (410)) && e.getY() >= ((view.getHeight() / heightRes) * (580)) && e.getY() <= ((view.getHeight() / heightRes) * (630))){ //Make sure Freeze Tower is picked
								
								if (player.profile.money >= Tower.towerList[4].cost){
									pointer = 5; //A tower placement is pending on the cursor
								
									selectedTower = Tower.towerList[4];
								}
						
						    }	
				}
			}
		}

		public void mouseUp(MouseEvent e) {
			selectTower(e.getX(), e.getY());
			updateMouse(e);
			mouseClicked = false;
		}

	}
}