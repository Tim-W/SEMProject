package main.java.nl.tudelft.sem.group2.units;

import javafx.scene.image.Image;

/**
 * Created by gijs on 8-9-2016.
 */
abstract class Unit {
    public int x;
    public int y;
    public int width;
    public int height;
    private Image[] sprite;
    Unit(int x, int y, int width, int height, Image[] sprite){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }
    public abstract void draw();
}


