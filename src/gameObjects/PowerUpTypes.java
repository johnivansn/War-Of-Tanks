package gameObjects;

import java.awt.image.BufferedImage;

import graphics.Assets;

public enum PowerUpTypes {
	SHIELD("SHIELD", Assets.shield, PowerUpCategory.DEFENSE),
	FIRES("multi", Assets.orb, PowerUpCategory.WEAPON),
	LIFE("+1 LIFE", Assets.life, PowerUpCategory.INSTANT),
	SCORE_X2("SCORE x2", Assets.doubleScore, PowerUpCategory.SCORE),
	FASTER_FIRE("FAST FIRE", Assets.fastFire, PowerUpCategory.SCORE),
	SCORE_STACK("+1000 SCORE", Assets.star, PowerUpCategory.INSTANT),
	DOUBLE_GUN("DOUBLE GUN", Assets.doubleGun, PowerUpCategory.WEAPON);

	public String text;
	public BufferedImage texture;
	public PowerUpCategory category;

	private PowerUpTypes(String text, BufferedImage texture, PowerUpCategory category) {
		this.text = text;
		this.texture = texture;
		this.category = category;
	}
}
