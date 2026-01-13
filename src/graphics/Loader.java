package graphics;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Loader {

	public static BufferedImage imageLoader(String path) {
		try {
			var url = Loader.class.getResource("/resources" + path);
			if (url == null) {
				throw new RuntimeException("Imagen no encontrada: " + path);
			}
			return ImageIO.read(url);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Font loadFont(String path, int size) {
		try (InputStream is = Loader.class.getResourceAsStream("/resources" + path)) {
			if (is == null) {
				throw new RuntimeException("Fuente no encontrada: " + path);
			}
			return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, size);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Clip loadSound(String path) {
		try {
			var url = Loader.class.getResource("/resources" + path);
			if (url == null) {
				throw new RuntimeException("Sonido no encontrado: " + path);
			}
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			return clip;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
