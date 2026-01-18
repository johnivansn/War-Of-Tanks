package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import math.Vector2D;
import graphics.Animation;
import graphics.Assets;
import graphics.Resize;
import graphics.Sound;
import input.Constants;
import input.KeyBoard;
import states.GameState;

public class Player extends MovingObject {

	private Vector2D heading;
	private Vector2D acceleration;
	private long fireRate;

	private boolean spawning, visible;

	private long spawnTime, flickerTime, multi;

	private Sound shoot, loose;

	private boolean multiOn, activa;

	private Animation shieldEffect;

	private long fireSpeed;

	private final PowerUpState powerUpState;

	public Player(
			Vector2D position,
			Vector2D velocity,
			double maxVel,
			BufferedImage texture,
			GameState gameState) {
		super(position,
				velocity,
				maxVel,
				texture,
				gameState);

		heading = new Vector2D(0, 1);
		acceleration = new Vector2D();
		fireRate = 0;
		spawnTime = 0;
		flickerTime = 0;
		multi = 0;

		shoot = new Sound(Assets.enemyShoot);
		loose = new Sound(Assets.playerLoose);

		shieldEffect = new Animation(
				Assets.shieldEffect,
				80,
				null);
		powerUpState = new PowerUpState();
		visible = true;
	}

	@Override
	public void update(float dt) {
		updatePowerUpTimers(dt);
		updateSpawningState(dt);
		handleShooting(dt);
		handleMovement(dt);
		clampToScreen();
		updateVisualEffects(dt);
	}

	private void updatePowerUpTimers(float dt) {
		fireRate += dt;

		if (activa && multiOn) {
			multi += dt;
		}

		powerUpState.update(dt);

		fireSpeed = powerUpState.isActive(PowerUpTypes.FASTER_FIRE)
				? Constants.FIRE_RATE / 2
				: Constants.FIRE_RATE;

		if (multi > Constants.MULTI_FIRE_DURATION) {
			multiOn = false;
			multi = 0;
		}
	}

	private void updateSpawningState(float dt) {
		if (!spawning)
			return;

		flickerTime += dt;
		spawnTime += dt;

		if (flickerTime > Constants.FLICKER_TIME) {
			visible = !visible;
			flickerTime = 0;
		}

		if (spawnTime > Constants.SPAWNING_TIME) {
			spawning = false;
			visible = true;
		}
	}

	private void handleShooting(float dt) {
		if (!KeyBoard.SHOOT || fireRate <= fireSpeed || spawning)
			return;

		if (isDoubleGunOn()) {
			shootDoubleGun();
		} else if (multiOn) {
			shootMulti();
		} else {
			shootSingle();
		}

		fireRate = 0;
		shoot.play();

		if (shoot.getFramePosition() > Constants.SOUND_STOP_THRESHOLD) {
			shoot.stop();
		}
	}

	private void shootDoubleGun() {
		Vector2D leftGun = getCenter();
		Vector2D rightGun = getCenter();

		Vector2D temp = new Vector2D(heading);
		temp.normalize();
		temp = temp.setDirection(angle - 1.35f);// 1.3
		temp = temp.scale(width);
		rightGun = rightGun.add(temp);

		temp = temp.setDirection(angle - 1.8f);// 1.9
		leftGun = leftGun.add(temp);

		gameState.getMovingObjects().add(0, gameState.acquireFire(
				leftGun,
				heading,
				angle,
				Assets.fire));

		gameState.getMovingObjects().add(0, gameState.acquireFire(
				rightGun,
				heading,
				angle,
				Assets.fire));
	}

	private void shootMulti() {
		Vector2D center = getCenter();
		// delanteIzq
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading.setDirection(angle - Math.PI / 2 + Math.PI / 16),
				angle + Math.PI / 16,
				Assets.fire));
		// delanteM
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading,
				angle,
				Assets.fire));
		// delanteDer
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading.setDirection(angle - Math.PI / 2 - Math.PI / 16 + Math.PI),
				angle - Math.PI / 16 + Math.PI,
				Assets.fire));
		// -------------------------------------------------
		// atrasIzq
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading.setDirection(angle - Math.PI / 2 + Math.PI / 16 + Math.PI),
				angle + Math.PI / 16 + Math.PI,
				Assets.fire));
		// atrasM
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading.setDirection(angle - Math.PI / 2 - Math.PI),
				angle + Math.PI,
				Assets.fire));
		// atrasDer
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading.setDirection(angle - Math.PI / 2 - Math.PI / 16),
				angle - Math.PI / 16,
				Assets.fire));
		// -------------------------------------------------
		// arribaIzq
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading.setDirection(angle - Math.PI / 2 + Math.PI / 16 - Math.PI / 2),
				angle + Math.PI / 16 - Math.PI / 2,
				Assets.fire));
		// arribaM
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading.setDirection(angle - Math.PI / 2 - Math.PI / 2),
				angle - Math.PI / 2,
				Assets.fire));
		// arribaDer
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading.setDirection(angle - Math.PI / 2 - Math.PI / 16 - Math.PI / 2),
				angle - Math.PI / 16 - Math.PI / 2,
				Assets.fire));
		// -------------------------------------------------
		// abajoIzq
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading.setDirection(angle - Math.PI / 2 + Math.PI / 16 + Math.PI / 2),
				angle + Math.PI / 16 + Math.PI / 2,
				Assets.fire));
		// abajoM
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading.setDirection(angle - Math.PI / 2 + Math.PI / 2),
				angle + Math.PI / 2,
				Assets.fire));
		// abajoDer
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				center,
				heading.setDirection(angle - Math.PI / 2 - Math.PI / 16 + Math.PI / 2),
				angle - Math.PI / 16 + Math.PI / 2,
				Assets.fire));
	}

	private void shootSingle() {
		gameState.getMovingObjects().add(0, gameState.acquireFire(
				getCenter().add(heading.scale(width)),
				heading,
				angle,
				Assets.fire));
	}

	private void handleMovement(float dt) {
		// teclado
		if (KeyBoard.RIGHT)
			angle += Constants.DELTA_ANGLE;
		if (KeyBoard.LEFT)
			angle -= Constants.DELTA_ANGLE;

		if (KeyBoard.UP) {
			acceleration = heading.scale(Constants.ACC);
		} else {
			if (velocity.getMagnitude() != 0)
				acceleration = velocity.scale(-1).normalize().scale(Constants.ACC / 2);
		}

		velocity = velocity.add(acceleration);
		velocity = velocity.limit(maxVel);
		heading = heading.setDirection(angle - Math.PI / 2);
		position = position.add(velocity);
	}

	private void clampToScreen() {
		// limitador de pantalla para el jugador
		if (position.getX() < 0) {
			position.setX(0);
		}
		if (position.getY() < 0) {
			position.setY(0);
		}
		if (position.getX() > Constants.WIDTH - width) {
			position.setX(Constants.WIDTH - width);
		}
		if (position.getY() > Constants.HEIGHT - height) {
			position.setY(Constants.HEIGHT - height);
		}
	}

	private void updateVisualEffects(float dt) {
		if (isShieldOn())
			shieldEffect.update(dt);
	}

	public void setShield() {
		powerUpState.activate(PowerUpTypes.SHIELD, Constants.SHIELD_TIME);
		multiOn = false;
	}

	public void setDoubleScore() {
		powerUpState.activate(PowerUpTypes.SCORE_X2, Constants.DOUBLE_SCORE_TIME);
	}

	public void setFastFire() {
		powerUpState.activate(PowerUpTypes.FASTER_FIRE, Constants.FAST_FIRE_TIME);
	}

	public void setDoubleGun() {
		powerUpState.activate(PowerUpTypes.DOUBLE_GUN, Constants.DOUBLE_GUN_TIME);
	}

	public void setMulti() {
		if (multiOn)
			multi = 0;
		multiOn = true;
		powerUpState.activate(PowerUpTypes.SHIELD, 0);
	}

	@Override
	public void Destroy() {
		spawning = true;

		gameState.playExplosion1(getCenter());
		spawnTime = 0;
		loose.play();
		if (!gameState.subtractLife(position)) {
			gameState.gameOver();
			super.Destroy();
		}
		resetValues();
	}

	private void resetValues() {
		angle = 0;
		velocity = new Vector2D();
		position = GameState.PLAYER_START_POSITION;
	}

	@Override
	public void draw(Graphics g) {
		if (!visible)
			return;

		Graphics2D g2d = (Graphics2D) g;

		if (isShieldOn()) {
			BufferedImage currentFrame = shieldEffect.getCurrentFrame();
			AffineTransform at3 = AffineTransform.getTranslateInstance(
					position.getX() - currentFrame.getWidth() / 2 + width / 2,
					position.getY() - currentFrame.getHeight() / 2 + height / 2);

			at3.rotate(
					angle,
					currentFrame.getWidth() / 2,
					currentFrame.getHeight() / 2);

			g2d.drawImage(
					shieldEffect.getCurrentFrame(),
					at3,
					null);
		}

		at = AffineTransform.getTranslateInstance(position.getX(), position.getY());

		at.rotate(
				angle,
				width / 2,
				height / 2);

		if (isDoubleGunOn()) {
			g2d.drawImage(Assets.doubleGunPlayer, at, null);
		} else if (multiOn) {
			g2d.drawImage(Resize.getResize(Assets.doubleGunPlayer, 1 / 1.15), at, null);
		} else {
			g2d.drawImage(texture, at, null);
		}
	}

	public boolean isSpawning() {
		return spawning;
	}

	public boolean isShieldOn() {
		return powerUpState.isActive(PowerUpTypes.SHIELD);
	}

	public boolean isDoubleScoreOn() {
		return powerUpState.isActive(PowerUpTypes.SCORE_X2);
	}

	public boolean isDoubleGunOn() {
		return powerUpState.isActive(PowerUpTypes.DOUBLE_GUN);
	}

	public boolean isMulti() {
		return multiOn;
	}
}
