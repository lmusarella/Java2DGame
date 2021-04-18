package gioco.musarella.progetto;

public class GestoreCollisioni {
	
	//creando nuovi metodi possiamo gestire collisioni di più entità
	public static boolean controllaCollisione(Omino omino, Lapillo lapillo) {	
			return omino.getBordi().intersects(lapillo.getBordi());	
	}
	
	public static boolean controllaCollisione(Proiettile proiettile, Lapillo lapillo) {
		if(proiettile.isAttivo() && lapillo.isAttivo()) {
			return proiettile.getBordi().intersects(lapillo.getBordi());
		}else
			return false;
			
	}

}
