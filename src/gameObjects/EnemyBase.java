package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.Assets;
import graphics.Sound;
import input.Constants;
import math.Vector2D;
import states.GameState;

public abstract class EnemyBase extends MovingObject {

	protected ArrayList<Vector2D> path;
	protected Vector2D currentNode, toPlayer;
	protected int index;
	protected boolean following, fireE;
	protected long fireRate, fireRateM, p;
	protected Sound shoot;
	protected double angleD, currentAngleD, angleF, d;
	protected BufferedImage cannonTexture;

	public EnemyBase(
			Vector2D position,
			Vector2D velocity,
			double maxVel,
			BufferedImage texture,
			BufferedImage cannonTexture,
			ArrayList<Vector2D> path,
			GameState gameState) {
		super(position, velocity, maxVel, texture, gameState);
		this.path = path;
		this.cannonTexture = cannonTexture;
		index = 0;
		following = true;
		fireE = false;
		angleD = currentAngleD = d = 0;
		fireRate = fireRateM = p = 0;
		angle = angleF = Math.PI / 2;
		shoot = new Sound(Assets.enemyShoot);
	}

	protected Vector2D pathFollowing() {
		currentNode = path.get(index);

		double distanceToNode = currentNode.subtract(getCenter()).getMagnitude();

		if (distanceToNode < Constants.NODE_RADIUS) {
			index++;
			if (index >= path.size()) {
				following = false;
			}
		}
		return seekForce(currentNode);
	}

	protected Vector2D seekForce(Vector2D target) {
		Vector2D desiredVelocity = target.subtract(getCenter());
		return desiredVelocity.subtract(velocity);
	}

	protected void updateRotation(Vector2D pathFollowing) {
		if (index == path.size())
			return;

		angleD = pathFollowing.getAngle();
		angleD += Math.PI / 2;

		if (index == 1) {
			angle = angleD;
		} else {
			pathFollowing = currentNode.subtract(
				getCenter()).
				normalize().
				scale(maxVel).
				scale(1 / Constants.ENEMY_MASS * Constants.SEEK_FORCE_MULTIPLIER);

			angle += (angleD - angle) * Constants.ROTATION_SMOOTHING_FACTOR;
		}
	}

	protected void updateCannonRotation() {
		if (fireRateM == 0) {
			calculateTargetAngle();
		}

		if (p == 0) {
			calculateTargetAngle();
			p++;
			angleF = d;
		}

		angleF = Math.round(angleF * 10.0) / 10.0;
		d = Math.round(d * 10.0) / 10.0;

		if (angleF != d) {
			if (angleF < d) {
				angleF += 0.1;
			} else if (angleF > d) {
				angleF -= 0.1;
			}
			fireE = false;
			fireRateM += 1;
		} else {
			angleF = d;
		}
	}

	protected void handleShooting(float dt) {
		fireRate += dt;

		if (fireRate > Constants.ENEMY_FIRE_RATE) {
			fireE = true;
			fireRate = 0;
			fireRateM = 0;
		}

		if (fireE) {
			fireProjectile();
			fireE = false;
			shoot.play();
		}

		if (shoot.getFramePosition() > Constants.SOUND_STOP_THRESHOLD) {
			shoot.stop();
		}
	}

	protected void fireProjectile() {
		FireE fire = new FireE(
				getCenter().add(toPlayer.scale(width)),
				toPlayer,
				Constants.FIRE_VEL,
				d,
				Assets.fire,
				gameState);

		gameState.getMovingObjects().add(0, fire);
	}

	protected void handleShield() {
		Vector2D playerPos = new Vector2D(gameState.getPlayer().getCenter());
		int distanceToPlayer = (int) playerPos.subtract(getCenter()).getMagnitude();

		if (distanceToPlayer < Constants.SHIELD_DISTANCE / 2 + width / 2) {
			if (gameState.getPlayer().isShieldOn()) {
				Destroy();
			}
		}
	}

	protected void calculateTargetAngle() {
		toPlayer = gameState.getPlayer().getCenter().subtract(getCenter()).normalize();
		currentAngleD = toPlayer.getAngle();
		currentAngleD += Math.random() * Constants.ENEMY_ANGLE_RANGE - Constants.ENEMY_ANGLE_RANGE / 2;

		toPlayer = toPlayer.setDirection(currentAngleD);
		d = currentAngleD + Math.PI / 2;
	}

	protected abstract void handleSpecialBehavior(float dt);

	@Override
	public void update(float dt) {
		handleShooting(dt);

		Vector2D pathFollowing = following ? pathFollowing() : new Vector2D();

		updateRotation(pathFollowing);
		handleSpecialBehavior(dt);

		velocity = velocity.add(pathFollowing).limit(maxVel);
		position = position.add(velocity);

		if (position.getX() > Constants.WIDTH ||
				position.getY() > Constants.HEIGHT ||
				position.getX() < -width ||
				position.getY() < -height) {
			Destroy();
		}

		updateCannonRotation();
		handleShield();
	}

	@Override
	public void Destroy() {
		if (position.getX() > Constants.WIDTH || position.getY() > Constants.HEIGHT) {
			super.Destroy();
		} else {
			gameState.playExplosion1(getCenter());
			gameState.addScore(Constants.ENEMY_SCORE, position);
			super.Destroy();
		}
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		AffineTransform at1 = AffineTransform.getTranslateInstance(position.getX(), position.getY());

		at.rotate(angle, width / 2, height / 2);
		g2d.drawImage(texture, at, null);

		at1.rotate(angleF + Math.PI, width / 2, height / 2);
		g2d.drawImage(cannonTexture, at1, null);

		// nodos de seguimiento
		g.setColor(Color.RED);
		for (int i = 1; i < path.size(); i++) {// prueba
			g.fillOval(
					(int) path.get(i).getX(),
					(int) path.get(i).getY(),
					10,
					10);
		}
		// nodo inicio
		g.setColor(Color.GREEN);
		g.fillOval(
				(int) path.get(0).getX(),
				(int) path.get(0).getY(),
				10,
				10);
	}
}
