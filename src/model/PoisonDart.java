package model;

import java.awt.Image;

import javax.swing.ImageIcon;

public class PoisonDart extends Weapon {
    public Image texture = new ImageIcon("res/tower/weapon/Poison Dart.png").getImage();	
	
	public PoisonDart(double x, double y, double speed, double damage, EnemyMotion target, int venom, int stun) {
		super(x, y, speed, damage, target, venom, stun);
	}
	
}
