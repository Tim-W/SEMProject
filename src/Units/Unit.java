package Units;
/**
 * Created by gijs on 8-9-2016.
 */
import javafx.scene.image.Image;

abstract class Unit {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    private Image[] sprite;
    Unit(int x, int y, int width, int height, Image[] sprite){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }
    public abstract void draw();

    public int getX(){
        return this.x;
    };

    public int getY() {
        return this.y;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}


