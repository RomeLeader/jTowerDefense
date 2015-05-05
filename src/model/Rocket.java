package model;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Rocket extends Weapon {

	public Image texture = new ImageIcon("res/tower/weapon/Rocket.png").getImage();	
	
	public Rocket(double x, double y, double speed, double damage, EnemyMotion target, int venom, int stun) {
		super(x, y, speed, damage, target, venom, stun);
	}
}
