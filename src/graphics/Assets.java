package graphics;

import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

public class Assets {

	public static boolean loaded = false;
	public static float count = 0;
	public static float MAX_COUNT = 160;

	public static BufferedImage player;
	public static BufferedImage doubleGunPlayer;

	public static BufferedImage fire;

	public static BufferedImage[] shieldEffect = new BufferedImage[3];

	public static BufferedImage[] exp1 = new BufferedImage[71];
	public static BufferedImage[] exp2 = new BufferedImage[17];
	public static BufferedImage[] exp3 = new BufferedImage[8];
	public static BufferedImage enemy, enemyA, enemyR;
	public static BufferedImage canyon, canyonA, canyonR;

	public static BufferedImage missile;
	public static BufferedImage rocks;

	public static BufferedImage[] num = new BufferedImage[11];
	public static BufferedImage life;

	public static BufferedImage blue_button;
	public static BufferedImage grey_button, grey_buttonBig, grey_left, grey_right;
	public static BufferedImage red_button;
	public static BufferedImage green_left, green_right;
	public static BufferedImage yellow_buttonBig;
	public static BufferedImage dump;

	public static BufferedImage muro;
	public static BufferedImage verde;
	public static BufferedImage piedra;

	public static BufferedImage menu, lvlMenu, editMenu;
	public static BufferedImage area, miniCampo;
	public static BufferedImage score;

	public static BufferedImage Item1, Item2, Item3;

	public static Font fontBig;
	public static Font fontMed;
	public static Font fontMed05;

	public static Clip backgroundMusic, theme, explosion, playerLoose, playerShoot, poderShoot, enemyShoot, powerUp,
			backEdit;

	public static BufferedImage orb, doubleScore, doubleGun, fastFire, shield, star;

	public static void init() {
		player = loadImage("/tanks/tank.png");
		doubleGunPlayer = loadImage("/tanks/tank2.png");

		fire = loadImage("/fire/fire.png");

		for (int i = 0; i < 3; i++)
			shieldEffect[i] = loadImage("/shield/shield" + (i + 1) + ".png");

		for (int i = 0; i < exp1.length; i++)
			exp1[i] = loadImage("/explosions/explosion1/" + i + ".png");

		for (int i = 0; i < exp2.length; i++)
			exp2[i] = loadImage("/explosions/explosion2/" + i + ".png");

		for (int i = 0; i < exp3.length; i++)
			exp3[i] = loadImage("/explosions/explosion3/" + i + ".png");

		for (int i = 0; i < num.length; i++)
			num[i] = loadImage("/num/" + i + ".png");

		enemy = loadImage("/enemy/enemy.png");
		enemyA = loadImage("/enemy/enemyA.png");
		enemyR = loadImage("/enemy/enemyR.png");

		canyon = loadImage("/enemy/canyon.png");
		canyonA = loadImage("/enemy/canyonA.png");
		canyonR = loadImage("/enemy/canyonR.png");

		muro = loadImage("/enemy/muro.png");
		verde = loadImage("/enemy/verde.png");
		piedra = loadImage("/enemy/piedra.png");

		missile = loadImage("/fire/missile.png");

		rocks = loadImage("/enemy/Rocks1.png");

		life = loadImage("/tanks/life.png");

		fontBig = loadFont("/fonts/futureFont.ttf", 42);

		fontMed = loadFont("/fonts/futureFont.ttf", 20);

		fontMed05 = loadFont("/fonts/futureFont.ttf", (int)(20*1.05));

		backgroundMusic = loadSound("/sounds/backgroundMusic.wav");
		theme = loadSound("/sounds/theme.wav");
		explosion = loadSound("/sounds/explosion.wav");
		playerLoose = loadSound("/sounds/playerShoot.wav");
		playerShoot = loadSound("/sounds/playerShoot.wav");
		poderShoot = loadSound("/sounds/poderShoot.wav");
		enemyShoot = loadSound("/sounds/enemyShoot.wav");
		powerUp = loadSound("/sounds/powerUp.wav");
		backEdit = loadSound("/sounds/backEdit.wav");

		blue_button = loadImage("/ui/blue_button01.png");

		red_button = loadImage("/ui/red_button01.png");

		yellow_buttonBig = loadImage("/ui/yellow_buttonBig.png");

		green_left = loadImage("/ui/green_sliderLeft.png");
		green_right = loadImage("/ui/green_sliderRight.png");

		grey_button = loadImage("/ui/grey_button01.png");
		grey_buttonBig = loadImage("/ui/grey_buttonBig.png");
		grey_left = loadImage("/ui/grey_sliderLeft.png");
		grey_right = loadImage("/ui/grey_sliderRight.png");

		dump = loadImage("/ui/dump.png");

		orb = loadImage("/powers/orb.png");
		doubleScore = loadImage("/powers/doubleScore.png");
		doubleGun = loadImage("/powers/doubleGun.png");
		fastFire = loadImage("/powers/fastFire.png");
		star = loadImage("/powers/star.png");
		shield = loadImage("/powers/shield.png");

		menu = loadImage("/theme/mainMenu.jpg");
		lvlMenu = loadImage("/theme/menulvl.jpg");
		area = loadImage("/theme/campo.jpg");
		score = loadImage("/theme/Scoreboard.jpg");
		editMenu = loadImage("/theme/EditMenu.jpg");
		miniCampo = loadImage("/theme/minicampo.jpg");

		Item1 = loadImage("/tanks/Item1.png");
		Item2 = loadImage("/tanks/Item4.png");
		Item3 = loadImage("/tanks/Item3.png");
		// ===================================================

		loaded = true;

	}

	public static BufferedImage loadImage(String path) {
		count++;
		return Loader.imageLoader(path);
	}

	public static Font loadFont(String path, int size) {
		count++;
		return Loader.loadFont(path, size);
	}

	public static Clip loadSound(String path) {
		count++;
		return Loader.loadSound(path);
	}

}
