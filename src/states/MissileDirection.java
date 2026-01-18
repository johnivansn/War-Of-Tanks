package states;

import input.Constants;
import math.Vector2D;

public enum MissileDirection {
	TOP_LEFT_TO_BOTTOM_RIGHT(0, Math.PI / 2),
	BOTTOM_RIGHT_TO_TOP_LEFT(Math.PI, Math.PI / 2),
	TOP_RIGHT_TO_BOTTOM_LEFT(Math.PI / 2, Math.PI / 2),
	BOTTOM_LEFT_TO_TOP_RIGHT(-Math.PI / 2, Math.PI / 2);

	private final double baseAngle;
	private final double missileAngleOffset;

	MissileDirection(double baseAngle, double missileAngleOffset) {
		this.baseAngle = baseAngle;
		this.missileAngleOffset = missileAngleOffset;
	}

	public Vector2D getHeading() {
		return new Vector2D(0, 1).setDirection(Math.random() + baseAngle);
	}

	public double getMissileAngle(Vector2D heading) {
		return heading.getAngle() + missileAngleOffset;
	}

	public Vector2D getSpawnPosition(int index) {
		boolean alternate = index % 2 == 0;

		return switch (this) {
			case TOP_LEFT_TO_BOTTOM_RIGHT -> alternate
				? new Vector2D(Math.random() * Constants.WIDTH - 100, 0)
				: new Vector2D(0, Math.random() * Constants.HEIGHT - 100);

			case BOTTOM_RIGHT_TO_TOP_LEFT -> alternate
				? new Vector2D(Math.random() * Constants.WIDTH - 100, Constants.HEIGHT - 10)
				: new Vector2D(Constants.WIDTH - 10, Math.random() * Constants.HEIGHT - 100);

			case TOP_RIGHT_TO_BOTTOM_LEFT -> alternate
				? new Vector2D(Math.random() * Constants.WIDTH - 100, 0)
				: new Vector2D(Constants.WIDTH - 10, Math.random() * Constants.HEIGHT - 100);

			case BOTTOM_LEFT_TO_TOP_RIGHT -> alternate
				? new Vector2D(Math.random() * Constants.WIDTH - 100, Constants.HEIGHT - 10)
				: new Vector2D(0, Math.random() * Constants.HEIGHT - 100);
		};
	}

	public static MissileDirection random() {
		MissileDirection[] values = values();
		return values[(int)(Math.random() * values.length)];
	}
}
