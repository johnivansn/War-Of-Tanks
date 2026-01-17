package input;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {

	// frame dimensions

	public static final int WIDTH = 1100;
	public static final int HEIGHT = 650;
	public static int SELECT = 1;

	// player properties

	public static final int FIRE_RATE = 300;
	public static final double DELTA_ANGLE = 0.1;
	public static final double ACC = 0.2;
	public static final double PLAYER_MAX_VEL = 4.0;
	public static final long FLICKER_TIME = 200;
	public static final long SPAWNING_TIME = 3000;
	public static final long GAME_OVER_TIME = 3000;

	// fire properties

	public static final double FIRE_VEL = 15.0;

	// missile properties

	public static final double MISSILE_VEL = 2.0;
	public static final int MISSILE_SCORE = 20;
	public static final double MISSILE_MAX_VEL = 6.0;
	public static final int SHIELD_DISTANCE = 150;

	// Enemy properties

	public static final int NODE_RADIUS = 160;
	public static final double ENEMY_MASS = 60.0;
	public static final int ENEMY_MAX_VEL = 3;
	public static final long ENEMY_FIRE_RATE = 1000;
	public static final double ENEMY_ANGLE_RANGE = Math.PI / 2;
	public static final int ENEMY_SCORE = 40;
	public static final long ENEMY_SPAWN_RATE = 10000;

	public static final String PLAY = "PLAY";
	public static final String EXIT = "EXIT";
	public static final String FACIL = "FACIL";
	public static final String NORMAL = "NORMAL";
	public static final String AVANZADO = "AVANZADO";

	public static final int LOADING_BAR_WIDTH = (int) (500 * 1.2);
	public static final int LOADING_BAR_HEIGHT = (int) (50 * 1.2);

	public static final String RETURN = "RETURN";
	public static final String HIGH_SCORES = "SCORE BOARD";

	public static final String SCORE = "SCORE";
	public static final String DATE = "DATE";
	public static final String NICKNAME = "NICKNAME";

	// Paths multiplataforma
	private static final String USER_HOME = System.getProperty("user.home");
	private static final String APP_DIR = "War_Of_Tanks";

	private static Path getAppDirectory() {
		return Paths.get(USER_HOME, "Documents", APP_DIR);
	}

	public static final String SCORE_PATH = getAppDirectory()
			.resolve("dates")
			.resolve("data.json")
			.toString();

	public static final String SAVE_CARP = getAppDirectory()
			.resolve("edits")
			.toString();

	public static final String BIN = getAppDirectory()
			.resolve("BIN")
			.toString();

	public static final long POWER_UP_DURATION = 10000;
	public static final long POWER_UP_SPAWN_TIME = 8000;

	public static final long SHIELD_TIME = 12000;
	public static final long DOUBLE_SCORE_TIME = 10000;
	public static final long FAST_FIRE_TIME = 14000;
	public static final long DOUBLE_GUN_TIME = 12000;

	public static final int SCORE_STACK = 1000;
}
