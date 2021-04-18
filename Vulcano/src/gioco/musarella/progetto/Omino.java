package gioco.musarella.progetto;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


public class Omino extends Thread {
	
	private int xGame;
	private int yGame;
	//coordinate
	private int x;
	private int y;
	//dimensioni
	private int larghezza;
	private int altezza;
	
	boolean destra, sinistra, su, giu;
	public int delay = 10;
	public int vita;
	public int score;
	
	private int numberoProiettili;
	
	private boolean attivo;
	Random random;
	
	private CopyOnWriteArrayList<Proiettile> munizioni = new CopyOnWriteArrayList<>();
	
	BufferedImage imgOmino;
	BufferedImage imgProiettile;
	
	
	public Omino(BufferedImage image, BufferedImage imgProiettile, int larghezza, int altezza, int x, int y, int xGame, int yGame) {
		this.x = x;
		this.y = y;
		this.xGame = xGame;
		this.yGame = yGame;
		this.imgOmino = image;
		this.larghezza = larghezza;
		this.altezza = altezza;
		this.imgProiettile = imgProiettile;
		attivo = true;
		vita = 100;
		score = 0;
		numberoProiettili = 1000;
		random = new Random();
	}
	
	public void run() {
		attivo = true;
		while(attivo) {
			
			aggiorna();
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public CopyOnWriteArrayList<Proiettile> getMunizioni() {
		return munizioni;
	}

	public void setMunizioni(CopyOnWriteArrayList<Proiettile> munizioni) {
		this.munizioni = munizioni;
	}

	private void aggiorna() {
		
		if (destra && this.getX() < (this.xGame - this.larghezza))
			x += 5;
		if (sinistra && this.getX() > 0)
			x += -5;
		if (su && this.getY() > 0)
			y += -5;
		if (giu && this.getY() < (this.yGame - 2*this.altezza))
			y += 5;
	}
	
	public void disegna(Graphics g) {
		g.drawImage(imgOmino, x, y, larghezza, altezza, null);
	}
	
	public void animazioneOmino(BufferedImage image) {
		imgOmino = image;
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
	
	
	public void spara() {	
		this.munizioni.add(new Proiettile(imgProiettile, 25, 25, x+larghezza/2, y, 15));
	}
	
	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	public void cancellaMunizioni() {
		if(!munizioni.isEmpty()) {
			Iterator<Proiettile> iter = munizioni.iterator();
			while(iter.hasNext()){
				Proiettile item = iter.next();
				if(!item.isAttivo())
					munizioni.remove(item);
				
			}
		}
	}
	
	public void zunami() {
		for(int i = 0; i < numberoProiettili; i++) {
			this.munizioni.add(new Proiettile(imgProiettile, 25, 25, random.nextInt(1000), 700, 15));
		}
	}


}
