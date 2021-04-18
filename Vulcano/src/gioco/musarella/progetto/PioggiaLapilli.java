package gioco.musarella.progetto;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class PioggiaLapilli extends Thread {
	
	private final int maxVel = 5;
	
	private int numeroLapilli;
	private int attesa;
	private boolean piove = false;
	BufferedImage imgLapillo;
	private CopyOnWriteArrayList<Lapillo> lapilli;
	Random random;
	
	public PioggiaLapilli(int numeroLapilli, int attesa, BufferedImage imgLapillo) {
		this.numeroLapilli = numeroLapilli;
		this.attesa = attesa;
		this.imgLapillo = imgLapillo;
		piove = true;
		lapilli = new CopyOnWriteArrayList<>();
		random = new Random();
	}
	
	@Override
	public void run() {
		piove = true;
		while(piove) {
			for(int i = 0; i < numeroLapilli; i++) {
				lapilli.add(new Lapillo(imgLapillo, random.nextInt(1000), -50, 30, 30, random.nextInt(maxVel + 2)));
			}
			
			try {
				Thread.sleep(attesa);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void disegna(Graphics g) {
		for(int i = 0; i<lapilli.size(); i++) {
			Lapillo lapillo = lapilli.get(i);
			lapillo.disegna(g);
		}
	}
	 public CopyOnWriteArrayList<Lapillo> getLapilli(){
		 cancellaLapilli();
		 return lapilli;
	 }

	public int getNumeroLapilli() {
		return numeroLapilli;
	}

	public void setNumeroLapilli(int numeroLapilli) {
		this.numeroLapilli = numeroLapilli;
	}
	
	public void cancellaLapilli() {
		if(!lapilli.isEmpty()) {
			Iterator<Lapillo> iter = lapilli.iterator();
			while(iter.hasNext()){
				Lapillo item = iter.next();
				if(!item.isAttivo())
					lapilli.remove(item);		
			}
		}
	}
	 
	 
	

}
