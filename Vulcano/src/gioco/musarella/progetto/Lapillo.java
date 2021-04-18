package gioco.musarella.progetto;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Lapillo extends Thread {
	
	//private Gioco main;
	private int x;
	private int y;
	private int larghezza;
	private int altezza;
	private int velocità;
	BufferedImage imaLapillo;
	private boolean attivo;
	
	
	public Lapillo(BufferedImage imaLapillo, int x, int y, int larghezza, int altezza, int velocità) {
		this.x = x;
		this.y = y;
		this.larghezza = larghezza;
		this.altezza = altezza;
		this.imaLapillo = imaLapillo;
		this.velocità = velocità;
		attivo = true;
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
		y += velocità;
		if(y + altezza > 600) {
			this.setAttivo(false);	
		}
		
	}
	
	public void disegna(Graphics g) {
		g.drawImage(imaLapillo, x, y, larghezza, altezza, null);
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
