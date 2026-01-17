package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import graphics.Resize;
import input.Constants;
import input.MouseInput;
import math.Vector2D;

public class Obj {

	private BufferedImage texture, textureR, textureO;
	public boolean mouseIn, vis, m, limit;
	private Vector2D position;
	private Color color;
	public Obj(
			BufferedImage texture,
			Color color,
			Vector2D position,
			boolean resize,
			boolean posR) {

		textureO = textureR = texture;

		if (resize) {
			// Ridemeciona la imagen
			textureR = Resize.getResize(texture, 1 / Constants.EDITOR_SCALE);
			//
		}
		this.texture = textureR;
		this.color = color;
		this.position = position;

		if(posR) {
		this.position = new Vector2D(
				900 / 2 - this.texture.getWidth() / 2,
				(Constants.HEIGHT / 1.375) / 2 + (Constants.HEIGHT - Constants.HEIGHT / 1.375 - 50) - this.texture.getHeight() / 2);
		}
	}

	public void update() {

		new Rectangle(
				(int) (position.getX()),
				(int) (position.getY()),
				texture.getWidth(),
				texture.getHeight());

		/* if (!m) { // borrar ??
			if (boundingBox.contains(MouseInput.X, MouseInput.Y)) {
				mouseIn = true;
			} else {
				mouseIn = false;
			}
		} */
		if (!vis) {
			if (mouseIn && MouseInput.MLB) {
				position.setX(MouseInput.X - texture.getWidth() / 2);
				position.setY(MouseInput.Y - texture.getHeight() / 2);
			}
		}
		if (limit) {
			if (position.getX() < 50) {
				position.setX(50);
			}
			if (position.getY() < Constants.HEIGHT - Constants.HEIGHT / 1.375 - 50) {
				position.setY(Constants.HEIGHT - Constants.HEIGHT / 1.375 - 50);
			}
			if (position.getX() > Constants.WIDTH / 1.375 + 50 - texture.getWidth()) {
				position.setX(Constants.WIDTH / 1.375 + 50 - texture.getWidth());
			}
			if (position.getY() > 600 - texture.getHeight()) {
				position.setY(600 - texture.getHeight());
			}
			/* position.setX(Math.max(50, position.getX()));
			position.setY(Math.max(Constants.HEIGHT - Constants.HEIGHT / 1.375 - 50, position.getY()));
			position.setX(Math.min(Constants.WIDTH / 1.375 + 50 - texture.getWidth(), position.getX()));
			position.setY(Math.min(600 - texture.getHeight(), position.getY())); */
		}
	}

	public void draw(Graphics g) {
		//Graphics2D g2d = (Graphics2D) g;

		g.drawImage(
				texture,
				(int) (position.getX()),
				(int) (position.getY()),
				null);

		if (mouseIn) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(3));
			g.setColor(color);
			g.drawRect(
				(int) (position.getX()),
				(int) (position.getY()),
				texture.getWidth(),
				texture.getHeight());
		}
	}
	public BufferedImage getTexture() {
		return textureO;
	}

	public Vector2D getPosition() {
		return position;
	}
}
