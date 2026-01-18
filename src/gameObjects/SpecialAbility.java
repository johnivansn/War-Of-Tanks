package gameObjects;

public class SpecialAbility {
	private final int unlockScore;
	private final int cooldown;
	private boolean unlocked;
	private boolean available;
	private boolean justUnlockedFlag;
	private long cooldownTimer;

	public SpecialAbility(int unlockScore, int cooldown) {
		this.unlockScore = unlockScore;
		this.cooldown = cooldown;
		this.unlocked = false;
		this.available = false;
		this.justUnlockedFlag = false;
	}

	public void checkUnlock(int currentScore) {
		if (!unlocked && currentScore >= unlockScore) {
			unlocked = true;
			available = true;
			justUnlockedFlag = true;
		}
	}

	public boolean canUse() {
		return unlocked && available;
	}

	public void use() {
		if (!canUse()) return;

		available = false;
		if (cooldown > 0) {
			cooldownTimer = cooldown;
		}
	}

	public void update(float dt) {
		if (cooldown > 0 && cooldownTimer > 0) {
			cooldownTimer -= dt;
			if (cooldownTimer <= 0) {
				available = true;
			}
		}
	}

	public boolean justUnlocked() {
		if (justUnlockedFlag) {
			justUnlockedFlag = false;
			return true;
		}
		return false;
	}

	public boolean isUnlocked() {
		return unlocked;
	}

	public float getCooldownProgress() {
		if (cooldown == 0) return available ? 1.0f : 0.0f;
		return 1.0f - (cooldownTimer / (float) cooldown);
	}
}
