package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
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
				System.err.println("ADVERTENCIA: Imagen no encontrada: " + path);
				return createPlaceholderImage(path);
			}
			return ImageIO.read(url);
		} catch (IOException e) {
			System.err.println("ERROR al cargar imagen: " + path + " - " + e.getMessage());
			return createPlaceholderImage(path);
		}
	}

	private static BufferedImage createPlaceholderImage(String path) {
		BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();

		g.setColor(Color.MAGENTA);
		g.fillRect(0, 0, 64, 64);

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 63, 63);

		g.drawLine(0, 0, 63, 63);
		g.drawLine(63, 0, 0, 63);

		g.dispose();
		return img;
	}

	public static Font loadFont(String path, int size) {
		try (InputStream is = Loader.class.getResourceAsStream("/resources" + path)) {
			if (is == null) {
				System.err.println("ADVERTENCIA: Fuente no encontrada: " + path + " - Usando fuente por defecto");
				return new Font("SansSerif", Font.PLAIN, size);
			}
			return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, size);
		} catch (Exception e) {
			System.err.println("ERROR al cargar fuente: " + path + " - " + e.getMessage());
			return new Font("SansSerif", Font.PLAIN, size);
		}
	}

	public static Clip loadSound(String path) {
		try {
			var url = Loader.class.getResource("/resources" + path);
			if (url == null) {
				System.err.println("ADVERTENCIA: Sonido no encontrado: " + path + " - Usando clip silencioso");
				return createSilentClip();
			}
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(url));
			return clip;
		} catch (Exception e) {
			System.err.println("ERROR al cargar sonido: " + path + " - " + e.getMessage());
			return createSilentClip();
		}
	}

	private static Clip createSilentClip() {
		try {
			Clip clip = AudioSystem.getClip();
			byte[] silentAudio = new byte[1];
			javax.sound.sampled.AudioFormat format = new javax.sound.sampled.AudioFormat(8000.0f, 8, 1, true, false);
			javax.sound.sampled.AudioInputStream stream = new javax.sound.sampled.AudioInputStream(
					new java.io.ByteArrayInputStream(silentAudio),
					format,
					1);
			clip.open(stream);
			return clip;
		} catch (Exception e) {
			System.err.println("ERROR CRÍTICO: No se pudo crear clip silencioso - " + e.getMessage());
			return null;
		}
	}
}
