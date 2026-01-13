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

	private long spawnTime, flickerTime, shieldTime, doubleScoreTime, fastFireTime, doubleGunTime, multi;

	private Sound shoot, loose;

	private boolean shieldOn, doubleScoreOn, fastFireOn, doubleGunOn, multiOn, activa;

	private Animation shieldEffect;

	private long fireSpeed;

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
		shieldTime = 0;
		fastFireTime = 0;
		doubleGunTime = 0;
		multi = 0;

		shoot = new Sound(Assets.enemyShoot);
		loose = new Sound(Assets.playerLoose);

		shieldEffect = new Animation(
				Assets.shieldEffect,
				80,
				null);

		visible = true;
	}

	@Override
	public void update(float dt) { // movimiento

		fireRate += dt;
		/* if (!activa) {
			if (multiOn) {
				multi += dt;
			}
		} */
		if (activa && multiOn) {
			multi += dt;
		}

		if (shieldOn) {
			shieldTime += dt;
		}

		if (doubleScoreOn)
			doubleScoreTime += dt;

		if (fastFireOn) {
			fireSpeed = Constants.FIRE_RATE / 2;
			fastFireTime += dt;
		} else {
			fireSpeed = Constants.FIRE_RATE;
		}

		if (doubleGunOn)
			doubleGunTime += dt;

		if (shieldTime > Constants.SHIELD_TIME) {
			shieldTime = 0;
			shieldOn = false;
		}

		if (doubleScoreTime > Constants.DOUBLE_SCORE_TIME) {
			doubleScoreOn = false;
			doubleScoreTime = 0;
		}

		if (fastFireTime > Constants.FAST_FIRE_TIME) {
			fastFireOn = false;
			fastFireTime = 0;
		}

		if (doubleGunTime > Constants.DOUBLE_GUN_TIME) {
			doubleGunOn = false;
			doubleGunTime = 0;
		}
		if (multi > 15000) {
			multiOn = false;
			multi = 0;
		}
		if (spawning) {
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

		if (KeyBoard.SHOOT && fireRate > fireSpeed && !spawning) { // velocidad de disparo

			if (doubleGunOn) {
				Vector2D leftGun = getCenter();
				Vector2D rightGun = getCenter();

				Vector2D temp = new Vector2D(heading);
				temp.normalize();
				temp = temp.setDirection(angle - 1.35f);// 1.3
				temp = temp.scale(width);
				rightGun = rightGun.add(temp);

				temp = temp.setDirection(angle - 1.8f);// 1.9
				leftGun = leftGun.add(temp);

				Fire l = new Fire(
						leftGun,
						heading,
						Constants.FIRE_VEL,
						angle,
						Assets.fire,
						gameState);

				Fire r = new Fire(
						rightGun,
						heading,
						Constants.FIRE_VEL,
						angle,
						Assets.fire,
						gameState);

				gameState.getMovingObjects().add(0, l);
				gameState.getMovingObjects().add(0, r);

			} else if (multiOn) {

				Fire delanteIzq = new Fire(
						getCenter(),
						heading.setDirection(angle - Math.PI / 2 + Math.PI / 16),
						Constants.FIRE_VEL,
						angle + Math.PI / 16,
						Assets.fire,
						gameState);

				Fire delanteM = new Fire(
						getCenter(),
						heading,
						Constants.FIRE_VEL,
						angle,
						Assets.fire,
						gameState);

				Fire delanteDer = new Fire(
						getCenter(),
						heading.setDirection(angle - Math.PI / 2 - Math.PI / 16 + Math.PI),
						Constants.FIRE_VEL,
						angle - Math.PI / 16 + Math.PI,
						Assets.fire,
						gameState);
				// -------------------------------------------------
				Fire atrasIzq = new Fire(
						getCenter(),
						heading.setDirection(angle - Math.PI / 2 + Math.PI / 16 + Math.PI),
						Constants.FIRE_VEL,
						angle + Math.PI / 16 + Math.PI,
						Assets.fire,
						gameState);

				Fire atrasM = new Fire(
						getCenter(),
						heading.setDirection(angle - Math.PI / 2 - Math.PI),
						Constants.FIRE_VEL,
						angle + Math.PI,
						Assets.fire,
						gameState);

				Fire atrasDer = new Fire(
						getCenter(),
						heading.setDirection(angle - Math.PI / 2 - Math.PI / 16),
						Constants.FIRE_VEL,
						angle - Math.PI / 16,
						Assets.fire,
						gameState);
				// -------------------------------------------------
				Fire arribaIzq = new Fire(
						getCenter(),
						heading.setDirection(angle - Math.PI / 2 + Math.PI / 16 - Math.PI / 2),
						Constants.FIRE_VEL,
						angle + Math.PI / 16 - Math.PI / 2,
						Assets.fire,
						gameState);

				Fire arribaM = new Fire(
						getCenter(),
						heading.setDirection(angle - Math.PI / 2 - Math.PI / 2),
						Constants.FIRE_VEL,
						angle - Math.PI / 2,
						Assets.fire,
						gameState);

				Fire arribaDer = new Fire(
						getCenter(),
						heading.setDirection(angle - Math.PI / 2 - Math.PI / 16 - Math.PI / 2),
						Constants.FIRE_VEL,
						angle - Math.PI / 16 - Math.PI / 2,
						Assets.fire,
						gameState);
				// -------------------------------------------------
				Fire abajoIzq = new Fire(
						getCenter(),
						heading.setDirection(angle - Math.PI / 2 + Math.PI / 16 + Math.PI / 2),
						Constants.FIRE_VEL,
						angle + Math.PI / 16 + Math.PI / 2,
						Assets.fire,
						gameState);

				Fire abajoM = new Fire(
						getCenter(),
						heading.setDirection(angle - Math.PI / 2 + Math.PI / 2),
						Constants.FIRE_VEL,
						angle + Math.PI / 2,
						Assets.fire,
						gameState);

				Fire abajoDer = new Fire(
						getCenter(),
						heading.setDirection(angle - Math.PI / 2 - Math.PI / 16 + Math.PI / 2),
						Constants.FIRE_VEL,
						angle - Math.PI / 16 + Math.PI / 2,
						Assets.fire,
						gameState);
				// -------------------------------------------------

				gameState.getMovingObjects().add(0, delanteIzq);
				gameState.getMovingObjects().add(0, delanteM);
				gameState.getMovingObjects().add(0, delanteDer);

				gameState.getMovingObjects().add(0, atrasIzq);
				gameState.getMovingObjects().add(0, atrasM);
				gameState.getMovingObjects().add(0, atrasDer);

				gameState.getMovingObjects().add(0, arribaIzq);
				gameState.getMovingObjects().add(0, arribaM);
				gameState.getMovingObjects().add(0, arribaDer);

				gameState.getMovingObjects().add(0, abajoIzq);
				gameState.getMovingObjects().add(0, abajoM);
				gameState.getMovingObjects().add(0, abajoDer);
			} else {
				gameState.getMovingObjects().add(0, new Fire(
						getCenter().add(heading.scale(width)),
						heading,
						Constants.FIRE_VEL,
						angle,
						Assets.fire,
						gameState));
			}

			fireRate = 0;
			shoot.play();

		}

		if (shoot.getFramePosition() > 8500) {
			shoot.stop();
		}

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
		//
		if (shieldOn)
			shieldEffect.update(dt);
		collidesWith();
	}

	public void setShield() {
		if (shieldOn)
			shieldTime = 0;
		shieldOn = true;
		multiOn = false;
	}

	public void setDoubleScore() {
		if (doubleScoreOn)
			doubleScoreTime = 0;
		doubleScoreOn = true;
	}

	public void setFastFire() {
		if (fastFireOn)
			fastFireTime = 0;
		fastFireOn = true;
	}

	public void setDoubleGun() {
		if (doubleGunOn)
			doubleGunTime = 0;
		doubleGunOn = true;
	}

	public void setMulti() {
		if (multiOn)
			multi = 0;
		multiOn = true;
		shieldOn = false;
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

		if (shieldOn) {
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

		if (doubleGunOn) {
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
		return shieldOn;
	}

	public boolean isDoubleScoreOn() {
		return doubleScoreOn;
	}

	public boolean isMulti() {
		return multiOn;
	}
}