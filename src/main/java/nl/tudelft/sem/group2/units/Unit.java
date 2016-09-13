package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.scenes.GameScene;

public abstract class Unit {
    protected int x;
    protected int y;
    protected AreaTracker areaTracker;
    Unit(int x, int y){
        this.x = x;
        this.y = y;
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
}


