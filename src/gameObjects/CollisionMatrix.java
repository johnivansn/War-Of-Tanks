package gameObjects;

import java.util.HashMap;
import java.util.Map;

public class CollisionMatrix {

	public enum CollisionResult {
		BOTH_DESTROYED,
		NONE,
		POWER_UP_CONSUMED
	}

	private final Map<String, CollisionResult> matrix = new HashMap<>();

	public CollisionMatrix() {
		addRule("FireE-FireE", CollisionResult.NONE);
		addRule("FireE-PowerUp", CollisionResult.NONE);
		addRule("Enemy-Missile", CollisionResult.NONE);
		addRule("Enemy2-Missile", CollisionResult.NONE);
		addRule("Enemy3-Missile", CollisionResult.NONE);
		addRule("Enemy-FireE", CollisionResult.NONE);
		addRule("Enemy2-FireE", CollisionResult.NONE);
		addRule("Enemy3-FireE", CollisionResult.NONE);
		addRule("Fire-Player", CollisionResult.NONE);
		addRule("Fire-Fire", CollisionResult.NONE);
		addRule("Player-FireE", CollisionResult.BOTH_DESTROYED);
		addRule("Player-Missile", CollisionResult.BOTH_DESTROYED);
		addRule("Player-PowerUp", CollisionResult.POWER_UP_CONSUMED);
	}

	private void addRule(String key, CollisionResult result) {
		matrix.put(key, result);
	}

	public CollisionResult getResult(MovingObject a, MovingObject b) {
		String typeA = a.getClass().getSimpleName();
		String typeB = b.getClass().getSimpleName();

		String key1 = typeA + "-" + typeB;
		String key2 = typeB + "-" + typeA;

		CollisionResult result = matrix.get(key1);
		if (result == null) result = matrix.get(key2);
		if (result == null) result = CollisionResult.BOTH_DESTROYED;

		return result;
	}
}
