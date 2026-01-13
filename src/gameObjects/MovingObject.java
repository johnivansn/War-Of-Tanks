package gameObjects;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import math.Vector2D;
import graphics.Assets;
import graphics.Sound;
import states.GameState;

public abstract class MovingObject extends GameObject{

	protected Vector2D velocity;
	protected AffineTransform at;
	protected double angle;
	protected double maxVel;
	protected int width;
	protected int height;
	protected GameState gameState;

	private Sound explosion;

	protected boolean Dead;

	public MovingObject(
			Vector2D position, 
			Vector2D velocity, 
			double maxVel, 
			BufferedImage texture,
			GameState gameState) 
	{
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

	protected void collidesWith() {
		
		ArrayList<MovingObject> movingObjects = gameState.getMovingObjects();

		for (MovingObject m : movingObjects) {
			//MovingObject m = movingObjects.get(i);

			if (m.equals(this))
				continue;

			double distance = m.getCenter().subtract(getCenter()).getMagnitude();

			if (distance < m.width / 2 + width / 2 
				//&& movingObjects.contains(this) 
				&& !m.Dead 
				&& !Dead) 
			{
				objectCollision(m, this);
			}
		}
	}

	private void objectCollision(MovingObject a, MovingObject b) {

		Player p = null;

		if (a instanceof Player)
			p = (Player) a;
		else if (b instanceof Player)
			p = (Player) b;

		if (p != null && p.isSpawning())
			return;
		
		if ( // todo objeto collisiona excepto a - b y b - a:
				(a instanceof FireE && b instanceof FireE)

				|| (a instanceof Enemy && b instanceof Missile) 
				|| (a instanceof Missile && b instanceof Enemy)

				|| (a instanceof Enemy2 && b instanceof Missile) 
				|| (a instanceof Missile && b instanceof Enemy2)

				|| (a instanceof Enemy3 && b instanceof Missile) 
				|| (a instanceof Missile && b instanceof Enemy3)

				|| (a instanceof Enemy && b instanceof FireE) 
				|| (a instanceof FireE && b instanceof Enemy)
				
				|| (a instanceof Enemy2 && b instanceof FireE) 
				|| (a instanceof FireE && b instanceof Enemy2)
				
				|| (a instanceof Enemy3 && b instanceof FireE) 
				|| (a instanceof FireE && b instanceof Enemy3)
				
				|| (a instanceof Fire && b instanceof Player) 
				|| (a instanceof Player && b instanceof Fire)

				|| (a instanceof Fire && b instanceof Fire)
				
				|| (a instanceof Player && b instanceof FireE)	// x
				|| (a instanceof FireE && b instanceof Player)	// x

				|| (a instanceof Player && b instanceof Missile)	// x
				|| (a instanceof Missile && b instanceof Player)	// x
				
				/*|| (a instanceof Enemy3 && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof Enemy3)
				|| (a instanceof Enemy2 && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof Enemy2)
				|| (a instanceof Enemy && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof Enemy)
				|| (a instanceof FireE && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof FireE)
				|| (a instanceof Fire && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof Fire)
				|| (a instanceof Player && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof Player)
				|| (a instanceof Missile && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof Missile)*/
				
		)
			return;
					
		if (!(a instanceof PowerUp || b instanceof PowerUp)) {
			a.Destroy();
			b.Destroy();
			return;
		}

		if (p != null) {
			if (a instanceof Player) {
				((PowerUp) b).executeAction();
				b.Destroy();
			} else if (b instanceof Player) {
				((PowerUp) a).executeAction();
				a.Destroy();
			}
		}
	}
	
	public void Destroy() {
		Dead = true;
		if (!(this instanceof Fire) && !(this instanceof PowerUp)){
			explosion.play();
			explosion.changeVolume(-15.0f);
		}
	}

	protected Vector2D getCenter() {
		return new Vector2D(position.getX() + width / 2, position.getY() + height / 2);
	}

	public boolean isDead() {
		return Dead;
	}
	
	protected void collidesWithPiedra() {
		
		ArrayList<MovingObject> movingObjects = gameState.getMovingObjects();

		for (MovingObject m : movingObjects) {
			//MovingObject m = movingObjects.get(i);

			if (m.equals(this))
				continue;

			double distance = m.getCenter().subtract(getCenter()).getMagnitude();

			if (distance < m.width / 2 + width / 2 
				//&& movingObjects.contains(this) 
				&& !m.Dead 
				&& !Dead) 
			{
				objectCollisionPiedra(m, this);
			}
		}
	}

	private void objectCollisionPiedra(MovingObject a, MovingObject b) {

		Player p = null;

		if (a instanceof Player)
			p = (Player) a;
		else if (b instanceof Player)
			p = (Player) b;

		if (p != null && p.isSpawning())
			return;
		
		if ( // todo objeto collisiona excepto a - b y b - a:

				//(a instanceof Enemy3 && b instanceof Rocks) 
				 (a instanceof Rocks && b instanceof Enemy3)
				//|| (a instanceof Enemy2 && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof Enemy2)
				//|| (a instanceof Enemy && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof Enemy)
				//|| (a instanceof FireE && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof FireE)
				//|| (a instanceof Fire && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof Fire)
				//|| (a instanceof Player && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof Player)
				//|| (a instanceof Missile && b instanceof Rocks) 
				|| (a instanceof Rocks && b instanceof Missile)
				
		)
			return;
					
		if (!(a instanceof PowerUp || b instanceof PowerUp)) {
			//a.DestroyRocks();
			b.DestroyRocks();
			return;
		}
	}
	
	public void DestroyRocks() {
		Dead = true;
	}
}
