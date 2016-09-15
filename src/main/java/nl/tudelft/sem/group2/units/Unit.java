package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.scenes.GameScene;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Unit {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected AreaTracker areaTracker;

	Unit(int x, int y) {
		this.x = x;
		this.y = y;
		this.width = 1;
		this.height = 1;
		this.areaTracker = GameScene.getAreaTracker();
	}

	Unit(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.areaTracker = GameScene.getAreaTracker();
	}

	public abstract void move();

	public abstract void draw(Canvas canvas);

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean intersect(Unit collidee) {
		if (this instanceof Qix) {
			Qix qix = (Qix) this;
			Polygon colliderP = qix.toPolygon();

			// subtract one from width&height to make collisions look more real
			Rectangle collideeR = new Rectangle(collidee.getX(),
					collidee.getY(), collidee.getWidth() / 2 - 1,
					collidee.getHeight() / 2 - 1);
			return colliderP.intersects(collideeR);
		}

		if (collidee instanceof Qix) {
			Qix qix = (Qix) collidee;
			Polygon collideeP = qix.toPolygon();

			// subtract one from width&height to make collisions look more real
			Rectangle colliderR = new Rectangle(this.getX(), this.getY(),
					this.getWidth() / 2 - 1, this.getHeight() / 2 - 1);
			return collideeP.intersects(colliderR);
		}

		Rectangle colliderR = new Rectangle(this.getX(), this.getY(),2,2);
		// subtract one from width&height to make collisions look more real
		Rectangle collideeR = new Rectangle(collidee.getX(), collidee.getY(),
				2, 2);
		return colliderR.intersects(collideeR);
	}
}
