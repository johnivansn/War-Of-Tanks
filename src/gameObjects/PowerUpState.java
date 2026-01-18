package gameObjects;

import java.util.EnumMap;
import java.util.Map;

public class PowerUpState {
	private final Map<PowerUpCategory, PowerUpTimer> activeByCategory;

	public PowerUpState() {
		activeByCategory = new EnumMap<>(PowerUpCategory.class);
	}

	public void activate(PowerUpTypes type, long duration) {
		PowerUpCategory category = type.category;

		if (category == PowerUpCategory.INSTANT) {
			return;
		}

		PowerUpTimer existing = activeByCategory.get(category);
		if (existing != null) {
			existing.reset();
		} else {
			activeByCategory.put(category, new PowerUpTimer(type, duration));
		}
	}

	public void update(float dt) {
		activeByCategory.values().forEach(timer -> timer.update(dt));
		activeByCategory.entrySet().removeIf(entry -> entry.getValue().isExpired());
	}

	public boolean isActive(PowerUpTypes type) {
		PowerUpTimer timer = activeByCategory.get(type.category);
		return timer != null && timer.getType() == type;
	}
}
