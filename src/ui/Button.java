package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import math.Vector2D;
import graphics.Assets;
import graphics.Resize;
import graphics.Text;
import input.MouseInput;

public class Button {

	private BufferedImage mouseOutImg;
	private BufferedImage mouseInImg;
	private BufferedImage mouseInImgScaled;
	public boolean mouseIn, resize;
	private Rectangle boundingBox;
	private Action action;
	private String text;
	private int x, y;
	private static final double HOVER_SCALE = 1.05;

	public Button(
			BufferedImage mouseOutImg,
			BufferedImage mouseInImg,
			int x,
			int y,
			String text,
			Action action) {
		this.mouseInImg = mouseInImg;
		this.mouseOutImg = mouseOutImg;
		this.text = text;
		this.x = x;
		this.y = y;

		boundingBox = new Rectangle(
				x,
				y,
				mouseInImg.getWidth(),
				mouseInImg.getHeight());

		this.action = action;
		this.mouseInImgScaled = Resize.getResize(mouseInImg, HOVER_SCALE);
	}

	public void update() {
		if (boundingBox.contains(MouseInput.X, MouseInput.Y)) {
			mouseIn = true;
		} else {
			mouseIn = false;
		}

		if (mouseIn && MouseInput.MLB) {
			action.doAction();
		}
	}

	public void draw(Graphics g) {
		if (mouseIn) {
			int offsetX = (mouseInImgScaled.getWidth() - boundingBox.width) / 2;
			int offsetY = (mouseInImgScaled.getHeight() - boundingBox.height) / 2;

			g.drawImage(
					mouseInImgScaled,
					boundingBox.x - offsetX,
					boundingBox.y - offsetY,
					null);

			// Texto con fuente ligeramente más grande para hover
			Text.drawText(
					g,
					text,
					new Vector2D(
							boundingBox.getX() + boundingBox.getWidth() / 2,
							boundingBox.getY() + boundingBox.getHeight() / 1.2),
					true,
					Color.BLACK,
					Assets.fontMed05);

			// Borde opcional si resize está activo
			if (resize) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setStroke(new BasicStroke(5));
				g.setColor(Color.BLACK);
				g.drawRect(
						x,
						y,
						mouseInImgScaled.getWidth(),
						mouseInImgScaled.getHeight());
			}

		} else {
			// Estado normal
			g.drawImage(
					mouseOutImg,
					boundingBox.x,
					boundingBox.y,
					null);

			Text.drawText(
					g,
					text,
					new Vector2D(
							boundingBox.getX() + boundingBox.getWidth() / 2,
							boundingBox.getY() + boundingBox.getHeight() / 1.2),
					true,
					Color.BLACK,
					Assets.fontMed);
		}
	}

	public void dispose() {
		mouseInImgScaled = null;
	}
}
