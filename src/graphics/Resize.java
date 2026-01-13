package graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Resize {
	
	public static BufferedImage getResize(BufferedImage image, double integer) {
		int width = (int) (image.getWidth() * integer);
		int height = (int) (image.getHeight() * integer);
		BufferedImage newImage = new BufferedImage(
				width, 
				height,
				image.getType());
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, 
				width, 
				height, 
				null);

		g.dispose(); // necesario ??
		
		return newImage;
	}
}
