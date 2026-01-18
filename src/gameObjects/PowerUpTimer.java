package gameObjects;

public class PowerUpTimer {
	private final PowerUpTypes type;
	private final long duration;
	private long elapsed;
	private boolean expired;

	public PowerUpTimer(PowerUpTypes type, long duration) {
		this.type = type;
		this.duration = duration;
		this.elapsed = 0;
		this.expired = false;
	}

	public void update(float dt) {
		if (expired) return;

		elapsed += dt;
		if (elapsed >= duration) {
			expired = true;
		}
	}

	public void reset() {
		elapsed = 0;
		expired = false;
	}

	public PowerUpTypes getType() {
		return type;
	}

	public boolean isExpired() {
		return expired;
	}

	public float getProgress() {
		return 1.0f - (elapsed / (float) duration);
	}
}
