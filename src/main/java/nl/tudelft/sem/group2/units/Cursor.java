package main.java.nl.tudelft.sem.group2.units;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Cursor extends LineTraveller {

    private KeyCode currentMove = null;

    public Cursor(int x, int y, int width, int height, Image[] sprite) {
        super(x, y, width, height, sprite);
    }

    @Override
    public void move(){
        if (currentMove != null) {
            if (currentMove.equals(KeyCode.LEFT)) {
                x--;
            } else if (currentMove.equals(KeyCode.RIGHT)) {
                x++;
            } else if (currentMove.equals(KeyCode.UP)) {
                y--;
            } else if (currentMove.equals(KeyCode.DOWN)) {
                y++;
            }
        }
    }
    public KeyCode getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(KeyCode currentMove) {
        this.currentMove = currentMove;
    }
    
    public String toString() {
    	return "Cursor";
    }

}