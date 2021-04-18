package gioco.musarella.progetto;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Proiettile extends Thread {
	
	private int altezza;
	private int larghezza;
	private int x;
	private int y;
	private int velocità;
	private boolean attivo;
	BufferedImage imgProiettile;
	
	
	public Proiettile(BufferedImage img, int larghezza, int altezza, int x, int y, int velocità) {
		
		this.altezza = altezza;
		this.larghezza = larghezza;
		this.x = x;
		this.y = y;
		this.imgProiettile = img;
		this.velocità = velocità;
		start();
	}
	
	@Override
	public void run() {
		attivo = true;
		while(attivo) {
			aggiorna();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void aggiorna() {
		y -= velocità;
		if(y + altezza < 0) {
			this.setAttivo(false);	
		}
		
	}
	
	public void disegna(Graphics g) {
		g.drawImage(imgProiettile, x, y, larghezza, altezza, null);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Rectangle getBordi() {
		return new Rectangle(x,y, larghezza, altezza);
	}

	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}
	
	

	
	

}
