package gameObjects;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import math.Vector2D;
import graphics.Assets;
import graphics.Sound;
import states.GameState;
import gameObjects.CollisionMatrix.CollisionResult;

public abstract class MovingObject extends GameObject {

	protected Vector2D velocity;
	protected AffineTransform at;
	protected double angle;
	protected double maxVel;
	protected int width;
	protected int height;
	protected GameState gameState;

	private Sound explosion;
	private static final CollisionMatrix collisionMatrix = new CollisionMatrix();

	protected boolean Dead;

	public MovingObject(
			Vector2D position,
			Vector2D velocity,
			double maxVel,
			BufferedImage texture,
			GameState gameState) {
		super(position, texture);
		this.velocity = velocity;
		this.maxVel = maxVel;
		this.gameState = gameState;
		width = texture.getWidth();
		height = texture.getHeight();
		angle = 0;
		explosion = new Sound(Assets.explosion);
		Dead = false;
	}

	public void handleCollision(MovingObject other) {

		Player p = null;

		if (this instanceof Player)
			p = (Player) this;
		else if (other instanceof Player)
			p = (Player) other;

		if (p != null && p.isSpawning())
			return;

		CollisionResult result = collisionMatrix.getResult(this, other);

		switch (result) {
			case BOTH_DESTROYED:
				this.Destroy();
				other.Destroy();
				break;
			case NONE:
				break;
			case POWER_UP_CONSUMED:
				if (p != null) {
					if (this instanceof Player) {
						((PowerUp) other).executeAction();
						other.Destroy();
					} else if (other instanceof Player) {
						((PowerUp) this).executeAction();
						this.Destroy();
					}
				}
				break;
		}
	}

	public void Destroy() {
		Dead = true;
		if (!(this instanceof Fire) && !(this instanceof PowerUp)) {
			explosion.play();
			explosion.changeVolume(-15.0f);
		}
	}

	public Vector2D getCenter() {
		return new Vector2D(position.getX() + width / 2, position.getY() + height / 2);
	}

	public boolean isDead() {
		return Dead;
	}

	public void handleCollisionWithRocks(MovingObject other) {

		Player p = null;

		if (this instanceof Player)
			p = (Player) this;
		else if (other instanceof Player)
			p = (Player) other;

		if (p != null && p.isSpawning())
			return;

		if ((other instanceof Rocks && this instanceof Enemy3)
				|| (other instanceof Rocks && this instanceof Enemy2)
				|| (other instanceof Rocks && this instanceof Enemy)
				|| (other instanceof Rocks && this instanceof FireE)
				|| (other instanceof Rocks && this instanceof Fire)
				|| (other instanceof Rocks && this instanceof Player)
				|| (other instanceof Rocks && this instanceof Missile)

		)
			return;

		if (!(this instanceof PowerUp || other instanceof PowerUp)) {
			other.DestroyRocks();
			return;
		}
	}

	public void DestroyRocks() {
		Dead = true;
	}

	public double getWidth() {
		return width;
	}
}
