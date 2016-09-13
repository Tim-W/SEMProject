package nl.tudelft.sem.group2.units;

import java.awt.Rectangle;

import javafx.scene.canvas.Canvas;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.scenes.GameScene;

public abstract class Unit {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected AreaTracker areaTracker;

    Unit(int x, int y){
        this.x = x;
        this.y = y;
        this.width = 1;
        this.height = 1;
        this.areaTracker = GameScene.getAreaTracker();
    }
    Unit(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.areaTracker = GameScene.getAreaTracker();
    }
    
    
    public abstract void move();
    public abstract void draw(Canvas canvas);
    public int getX(){
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
	
    public boolean intersect (Unit collidee){
		Rectangle colliderR = new Rectangle(this.getX(),this.getY(), this.getWidth()/2, this.getHeight()/2);
		//subtract one from width&height to make collisions look more real
		Rectangle collideeR = new Rectangle(collidee.getX(),collidee.getY(), collidee.getWidth()/2-1, collidee.getHeight()/2-1);
		if(colliderR.intersects(collideeR)){
			return true;
		}
		return false;
    }
}


