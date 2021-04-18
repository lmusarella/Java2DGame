package gioco.musarella.progetto;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CaricatoreImmagini {

	BufferedImage image;
	
	public BufferedImage caricatoreImmagini(String path) {
		try {
			image = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}
