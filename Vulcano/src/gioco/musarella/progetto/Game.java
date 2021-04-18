package gioco.musarella.progetto;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class Game extends Canvas implements KeyListener, Runnable {
	
	/**
	 * Autore: Luca Musarella
	 */
	private static final long serialVersionUID = 1L;
	private static final int larghezza = 1000;
	private static final int altezza = 700;
	private static final String nome = "Vulcano";
	private static boolean primoAvvio = true;
	
	private boolean giocoAttivo = false;
	
	BufferedImage sfondo = null;
	BufferedImage ominoCentrale = null;
	BufferedImage ominoDestra = null;
	BufferedImage ominoSinistra = null;
	BufferedImage lapillo = null;
	BufferedImage bollaProiettile = null;
	private Omino stickMan;
	private PioggiaLapilli pioggia;
	
	public static final long delta = 10;
	public static String[] difficoltò = new String[10];
	private int livello = 0;
	private int energia = 0;
	private int percentualeEnergia = 0;
	public static long lastClick;
	public List<Proiettile> munizioni = new CopyOnWriteArrayList<Proiettile>();

	public Game() {
		caricaRisorse();
		iniziaGioco();
	};
	
	
	public static void main(String[] args) {
		Game gioco = new Game();
		JFrame finestra_gioco = new JFrame(nome);
		Dimension dimensioneGioco = new Dimension(larghezza, altezza);
		finestra_gioco.setPreferredSize(dimensioneGioco);
		finestra_gioco.setMaximumSize(dimensioneGioco);
		finestra_gioco.setResizable(false);
		finestra_gioco.add(gioco);
		finestra_gioco.addKeyListener(gioco);
		finestra_gioco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		finestra_gioco.pack();
		finestra_gioco.setLocationRelativeTo(finestra_gioco);
		finestra_gioco.setVisible(true);	
		Thread threadGioco = new Thread(gioco);
		threadGioco.run();
		threadGioco.setPriority(10);
	}
	
	//Inizializza i Thread secondari
	private void iniziaGioco() {
		stickMan = new Omino(ominoCentrale, bollaProiettile, 100, 75, 100, 525, this.getLarghezza(), this.getAltezza());
		stickMan.start();
		pioggia = new PioggiaLapilli(3, 2500, lapillo);
		pioggia.start();
	}
	
	private void caricaRisorse() {
		CaricatoreImmagini loader = new CaricatoreImmagini();
		sfondo = loader.caricatoreImmagini("/immagini/vulcano.jpg");
		ominoCentrale = loader.caricatoreImmagini("/immagini/ominoCentrale.png");
		ominoDestra = loader.caricatoreImmagini("/immagini/ominoDestra.png");
		ominoSinistra = loader.caricatoreImmagini("/immagini/ominoSinistra.png");
		lapillo = loader.caricatoreImmagini("/immagini/lapillo.png");
		bollaProiettile = loader.caricatoreImmagini("/immagini/gocciaProiettile.jpg");
		popolaArrayLivelli();
		System.out.println("Risorse caricate!");
	}
	
	private void popolaArrayLivelli() {
		difficoltò[0] = " Carbonella";
		difficoltò[1] = " Tizzone Ardente";
		difficoltò[2] = " Bracere";
		difficoltò[3] = " Lanciafiamme";
		difficoltò[4] = " Cascata di Lava";
		difficoltò[5] = " Muro di fuoco";
		difficoltò[6] = " Antò fa caldo!";
		difficoltò[7] = " Inferno";
		difficoltò[8] = " Highlander";
		difficoltò[9] = " Domatore di Lucifero";
	}


	private void disegna() {
		//BufferStrategy serve per gestire meglio la visualizzazione delle immagini le quali vengono ridisegnate ogni tot secondi, usiamo due pannelli dove nel primo viene visualizzata l'immagine e nel secondo viene già caricata l'immagine successiva (spostamento fluido)
		BufferStrategy buffer = this.getBufferStrategy();
		if(buffer == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics g = buffer.getDrawGraphics();
		g.drawImage(sfondo, 0, 0, larghezza, altezza, this);
		g.setColor(Color.GRAY);
		g.fillRect(0, 600, 1000, 200);
		
		
		if(primoAvvio) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Monospaced",Font.BOLD,38));
			g.drawString("Salva il mondo..." ,  larghezza/4, altezza/4);
			g.setFont(new Font("Monospaced",Font.BOLD,28));
			g.drawString("...combatti il VULCANO!" ,  larghezza/2, altezza/2);
			g.setFont(new Font("Monospaced",Font.BOLD,12));
			g.drawString("Usa la frecce per spostarti | Usa la SPACEBAR per sparare | Freccia in alto attiva lo ZUNAMI con 20% di ENERGIA" ,10 , 640);
			
		}
		if(giocoAttivo && !primoAvvio) {
			stickMan.disegna(g);
			stickMan.cancellaMunizioni();
			
			disegnaBarraEnergia(energia,stickMan.vita, g);
			if(!stickMan.getMunizioni().isEmpty()) {
				munizioni = stickMan.getMunizioni();	
				synchronized (munizioni) {
					Iterator<Proiettile> iter = munizioni.iterator();
					while(iter.hasNext()){
						Proiettile item = iter.next();
						if(item.isAttivo())
							item.disegna(g);	
					}
				}
			}
			pioggia.disegna(g);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Monospaced",Font.BOLD,18));
			g.drawString("Vita: " + stickMan.vita + " %" , 10, 625);
			g.drawString("Livello: " + (livello + 1) + difficoltò[livello] , 25, 25);
			g.drawString("Punti: " + stickMan.score , 750, 25);
			g.drawString("Energia: " + percentualeEnergia + " %" , 10, 660);
		} else if(!primoAvvio && !giocoAttivo){
 			g.setColor(Color.WHITE);
			g.setFont(new Font("Monospaced",Font.BOLD,18));
			g.drawString("Sei morto carbonizzato!" , larghezza/2, altezza/2);
			g.setFont(new Font("Monospaced",Font.BOLD,38));
			g.drawString("HAI PERSO" , larghezza/4, altezza/4);
		}
		g.dispose();
		buffer.show();
	}
	
	private void disegnaBarraEnergia(int energia,int vita, Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(0, 600, vita*10, 35);
		g.setColor(Color.BLUE);
		g.fillRect(0, 635, energia*100, 50);
		percentualeEnergia = energia*10;
	}


	public int getLarghezza() {
		return larghezza;
	}
	
	public int getAltezza() {
		return altezza;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		stickMan.setAttivo(true);
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (stickMan.sinistra) stickMan.sinistra = false;
			else stickMan.destra = true;	
			stickMan.animazioneOmino(ominoDestra);
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (stickMan.destra) stickMan.destra = false;
			else stickMan.sinistra = true;
			stickMan.animazioneOmino(ominoSinistra);
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(energia > 1) {
				stickMan.zunami();
				energia -= 2;
			}
			stickMan.animazioneOmino(ominoCentrale);
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			stickMan.animazioneOmino(ominoCentrale);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.isActionKey()) {
			stickMan.destra = false;
			stickMan.sinistra = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		primoAvvio = false;
		if(Calendar.getInstance().getTimeInMillis() - lastClick > delta) {
			stickMan.spara();
			lastClick = Calendar.getInstance().getTimeInMillis();
		}
	}

	private void aggiorna() {
		CopyOnWriteArrayList<Lapillo> lapilli  = this.pioggia.getLapilli();
		for(Lapillo lapillo: lapilli) {
			if(GestoreCollisioni.controllaCollisione(stickMan, lapillo)) {
				lapilli.remove(lapillo);
				stickMan.vita -= 5;
				break;
			}
		}
		Iterator<Proiettile> iter = stickMan.getMunizioni().iterator();
		while(iter.hasNext()){
			Proiettile item = iter.next();
			for(Lapillo lapillo: lapilli) {
				if(GestoreCollisioni.controllaCollisione(item, lapillo)) {
					munizioni.remove(item);
					lapilli.remove(lapillo);
					stickMan.score += 50;
					aumentaDifficoltà(stickMan.score);
					break;
				}
			}
		}
		if(controllaSconfitta()) {
			this.giocoAttivo = false;
			disegna();
		}
		
	}
	
	private boolean controllaSconfitta() {
		if(stickMan.vita <= 0) {
			return true;
		}else return false;
		
	}
	
	@Override
	public void run() {
		giocoAttivo = true;
		while(giocoAttivo) {
			aggiorna();
			disegna();
		}
	}
	
	public void aumentaDifficoltà(int score) {
		switch(score) {
		case 1000:
			pioggia.setNumeroLapilli(5);
			livello++;
			energia++;
			break;
		case 3000:
			pioggia.setNumeroLapilli(10);
			livello++;
			energia++;
			break;
		case 5000:
			pioggia.setNumeroLapilli(15);
			livello++;
			energia++;
			break;
		case 8000:
			pioggia.setNumeroLapilli(20);
			livello++;
			energia++;
			break;
		case 10000:
			pioggia.setNumeroLapilli(30);
			livello++;
			energia++;
			break;
		case 15000:
			pioggia.setNumeroLapilli(50);
			livello++;
			energia++;
			break;
		case 20000:
			pioggia.setNumeroLapilli(100);
			livello++;
			energia++;
			break;
		case 30000:
			pioggia.setNumeroLapilli(200);
			livello++;
			energia++;
			break;
		case 40000:
			pioggia.setNumeroLapilli(300);
			livello++;
			energia++;
			break;
		case 50000:
			pioggia.setNumeroLapilli(500);
			energia++;
			break;
		default:
			break;
		}
	}

}
