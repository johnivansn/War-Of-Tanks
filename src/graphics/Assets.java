package graphics;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

public class Assets {

	public static boolean loaded = false;
	public static float count = 0;
	public static float MAX_COUNT = 160;

	public static BufferedImage player;
	public static BufferedImage doubleGunPlayer;
	public static BufferedImage fire;
	public static BufferedImage[] shieldEffect;
	public static BufferedImage[] exp1;
	public static BufferedImage[] exp2;
	public static BufferedImage[] exp3;
	public static BufferedImage enemy, enemyA, enemyR;
	public static BufferedImage canyon, canyonA, canyonR;
	public static BufferedImage missile;
	public static BufferedImage rocks;
	public static BufferedImage[] num;
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
	public static Clip backgroundMusic, theme, explosion, playerLoose, playerShoot, poderShoot, enemyShoot, powerUp, backEdit;
	public static BufferedImage orb, doubleScore, doubleGun, fastFire, shield, star;

	public static void init() {
		player = img("/tanks/tank.png");
		doubleGunPlayer = img("/tanks/tank2.png");
		fire = img("/fire/fire.png");

		shieldEffect = anim("/shield/shield");
		exp1 = anim("/explosions/explosion1/");
		exp2 = anim("/explosions/explosion2/");
		exp3 = anim("/explosions/explosion3/");
		num = anim("/num/");

		enemy = img("/enemy/enemy.png");
		enemyA = img("/enemy/enemyA.png");
		enemyR = img("/enemy/enemyR.png");
		canyon = img("/enemy/canyon.png");
		canyonA = img("/enemy/canyonA.png");
		canyonR = img("/enemy/canyonR.png");
		muro = img("/enemy/muro.png");
		verde = img("/enemy/verde.png");
		piedra = img("/enemy/piedra.png");
		missile = img("/fire/missile.png");
		rocks = img("/enemy/Rocks1.png");
		life = img("/tanks/life.png");

		fontBig = fnt("/fonts/futureFont.ttf", 42);
		fontMed = fnt("/fonts/futureFont.ttf", 20);
		fontMed05 = fnt("/fonts/futureFont.ttf", 21);

		backgroundMusic = snd("/sounds/backgroundMusic.wav");
		theme = snd("/sounds/theme.wav");
		explosion = snd("/sounds/explosion.wav");
		playerLoose = snd("/sounds/playerShoot.wav");
		playerShoot = snd("/sounds/playerShoot.wav");
		poderShoot = snd("/sounds/poderShoot.wav");
		enemyShoot = snd("/sounds/enemyShoot.wav");
		powerUp = snd("/sounds/powerUp.wav");
		backEdit = snd("/sounds/backEdit.wav");

		blue_button = img("/ui/blue_button01.png");
		red_button = img("/ui/red_button01.png");
		yellow_buttonBig = img("/ui/yellow_buttonBig.png");
		green_left = img("/ui/green_sliderLeft.png");
		green_right = img("/ui/green_sliderRight.png");
		grey_button = img("/ui/grey_button01.png");
		grey_buttonBig = img("/ui/grey_buttonBig.png");
		grey_left = img("/ui/grey_sliderLeft.png");
		grey_right = img("/ui/grey_sliderRight.png");
		dump = img("/ui/dump.png");

		orb = img("/powers/orb.png");
		doubleScore = img("/powers/doubleScore.png");
		doubleGun = img("/powers/doubleGun.png");
		fastFire = img("/powers/fastFire.png");
		star = img("/powers/star.png");
		shield = img("/powers/shield.png");

		menu = img("/theme/mainMenu.jpg");
		lvlMenu = img("/theme/menulvl.jpg");
		area = img("/theme/campo.jpg");
		score = img("/theme/Scoreboard.jpg");
		editMenu = img("/theme/EditMenu.jpg");
		miniCampo = img("/theme/minicampo.jpg");

		Item1 = img("/tanks/Item1.png");
		Item2 = img("/tanks/Item4.png");
		Item3 = img("/tanks/Item3.png");

		loaded = true;
	}

	private static BufferedImage img(String path) {
		count++;
		return Loader.imageLoader(path);
	}

	private static BufferedImage[] anim(String path) {
		return anim(path, -1, -1);
	}

	private static BufferedImage[] anim(String path, int count) {
		return anim(path, 0, count - 1);
	}

	private static BufferedImage[] anim(String path, int start, int end) {
		List<BufferedImage> frames = new ArrayList<>();

		if (end == -1) {
			int i = path.endsWith("/") ? 0 : 1;
			while (true) {
				String framePath = path.endsWith("/")
					? path + i + ".png"
					: path + i + ".png";

				URL url = Assets.class.getResource("/resources" + framePath);
				if (url == null) break;

				frames.add(img(framePath));
				i++;
			}
		} else {
			for (int i = start; i <= end; i++) {
				String framePath = path.endsWith("/")
					? path + i + ".png"
					: path + i + ".png";
				frames.add(img(framePath));
			}
		}

		return frames.toArray(new BufferedImage[0]);
	}

	private static Clip snd(String path) {
		count++;
		return Loader.loadSound(path);
	}

	private static Font fnt(String path, int size) {
		count++;
		return Loader.loadFont(path, size);
	}

	public static BufferedImage loadImage(String path) {
		return img(path);
	}

	public static Font loadFont(String path, int size) {
		return fnt(path, size);
	}

	public static Clip loadSound(String path) {
		return snd(path);
	}
}
