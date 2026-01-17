package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import graphics.Assets;
import input.Constants;
import input.KeyBoard;
import input.MouseInput;
import states.LoadingState;
import states.State;

public class Window extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private Canvas canvas;
	private Thread thread;// un miniPrograma para no sobrecargar a Jframe
	private boolean running = false;

	private BufferStrategy bs;
	private Graphics g;

	private final int FPS = 60; // cantidad de fotogramas
	private double TARGET_TIME = 1000000000 / FPS; // tiempo para pasar fotogramas
	private double delta = 0; // amacena temporalmente el tiempo
	private int AVERAGE_FPS = FPS; // muestra a cuanto va los FPS

	private KeyBoard keyBoard;
	private MouseInput mouseInput;

	public Window() { // propiedades de la ventana

		setTitle("War of tank");
		setSize(Constants.WIDTH + 16, Constants.HEIGHT + 39);// 37 es el tamaño del lugar del titulo
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null); // establecemos la ventana en el centro
		// setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/tanks/tank.png")));
		
		canvas = new Canvas();
		keyBoard = new KeyBoard();
		mouseInput = new MouseInput();

		canvas.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		canvas.setMaximumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		canvas.setMinimumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));

		canvas.setFocusable(true); // permite recibir entradas del teclado

		add(canvas);
		canvas.addKeyListener(keyBoard);
		canvas.addMouseListener(mouseInput);
		canvas.addMouseMotionListener(mouseInput);
		setVisible(true);

	}

	public static void main(String[] args) {
		new Window().start(); // comienza todo el programa
	}

	private void update(float dt) {
		keyBoard.update();
		State.getCurrentState().update(dt);

	}

	private void draw() {
		bs = canvas.getBufferStrategy();

		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}

		g = bs.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);

		State.getCurrentState().draw(g);

		g.setColor(Color.WHITE);
		g.drawString("" + AVERAGE_FPS, 10, 20);

		g.dispose();
		bs.show();
	}

	private void init() {
		Thread loadingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Assets.init();
			}
		});
		State.changeState(new LoadingState(loadingThread));
	}

	@Override
	public void run() {
		long now = 0;
		long lastTime = System.nanoTime();
		int frames = 0;
		long time = 0;

		init();

		while (running) {
			try {
				now = System.nanoTime();
				delta += (now - lastTime) / TARGET_TIME;
				time += (now - lastTime);
				lastTime = now;

				if (delta >= 1) {
					try {
						update((float) (delta * TARGET_TIME * 0.000001f));
					} catch (Exception e) {
						System.err.println("Error en update(): " + e.getMessage());
						e.printStackTrace();
					}
					try {
						draw();
					} catch (Exception e) {
						System.err.println("Error en draw(): " + e.getMessage());
						e.printStackTrace();
					}
					delta--;
					frames++;
				}
				if (time >= 1000000000) {
					AVERAGE_FPS = frames;
					frames = 0;
					time = 0;
				}
			} catch (Exception e) {
				System.err.println("Error crítico en game loop: " + e.getMessage());
				e.printStackTrace();

				int choice = JOptionPane.showConfirmDialog(
						this,
						"Error crítico en el juego. ¿Intentar continuar?",
						"Error",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.ERROR_MESSAGE);

				if (choice != JOptionPane.YES_OPTION) {
					running = false;
				}
			}
		}
		stop(); // detiene el programa
	}

	public void start() { // para iniciar el programa
		thread = new Thread(this); // inicia el miniPrograma
		thread.start(); // llama al metodo +run()
		running = true;
	}

	private void stop() { // para detener el programa
		try {
			thread.join(); //
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
}
