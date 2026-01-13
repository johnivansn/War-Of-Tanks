package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import math.Vector2D;
import graphics.Assets;
import graphics.Loader;
import graphics.Text;
import input.Constants;

public class LoadingState extends State {

	private Thread loadingThread;
	private Font font;
	private BufferedImage loading;

	public LoadingState(Thread loadingThread) {
		this.loadingThread = loadingThread;
		this.loadingThread.start();
		font = Loader.loadFont("/fonts/futureFont.ttf", 38);
		loading = Loader.imageLoader("/theme/mainMenu.jpg");
	}

	@Override
	public void update(float dt) {

		if (Assets.loaded) {
			State.changeState(new MenuState());
			try {
				loadingThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public void draw(Graphics g) { // ventana de cargado

		g.drawImage(loading, 0, 0, null);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.DARK_GRAY);
		g2d.fillRect(
				Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2, // dibuja la barra
				(int) (Constants.HEIGHT / 2 + Constants.LOADING_BAR_HEIGHT * 3.4), 
				Constants.LOADING_BAR_WIDTH,
				Constants.LOADING_BAR_HEIGHT);

		GradientPaint gp = new GradientPaint( // barra de cargando..
				Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2,
				Constants.HEIGHT / 2 - Constants.LOADING_BAR_HEIGHT / 2, 
				Color.LIGHT_GRAY,
				Constants.WIDTH / 2 + Constants.LOADING_BAR_WIDTH / 2,
				Constants.HEIGHT / 2 + Constants.LOADING_BAR_HEIGHT / 2, 
				Color.BLACK);

		g2d.setPaint(gp);

		float percentage = (Assets.count / Assets.MAX_COUNT);

		g2d.fillRect(
				Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2, // dibuja la barra
				(int) (Constants.HEIGHT / 2 + Constants.LOADING_BAR_HEIGHT * 3.4),
				(int) (Constants.LOADING_BAR_WIDTH * percentage), 
				Constants.LOADING_BAR_HEIGHT);

		g2d.drawRect(
				Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2, // dibuja los bordes
				(int) (Constants.HEIGHT / 2 + Constants.LOADING_BAR_HEIGHT * 3.4), 
				Constants.LOADING_BAR_WIDTH,
				Constants.LOADING_BAR_HEIGHT);

		// Text.drawText(g2d, "WAR OF TANKS", new Vector2D(Constants.WIDTH /2,
		// Constants.HEIGHT/2 -50),
		// true, Color.WHITE, fontBig);

		Text.drawText(
				g2d, 
				"LOADING...", 
				new Vector2D(
						Constants.WIDTH / 2 - Constants.LOADING_BAR_WIDTH / 2 + Constants.LOADING_BAR_WIDTH / 2,
						(int) (Constants.HEIGHT / 2 + (Constants.LOADING_BAR_HEIGHT * 3.4) + Constants.LOADING_BAR_HEIGHT * 1.1)),
				true, 
				Color.WHITE,
				font);
	}

}
