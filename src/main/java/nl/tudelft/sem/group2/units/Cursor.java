package nl.tudelft.sem.group2.units;

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
            if (currentMove.equals(KeyCode.LEFT) && x>1) {
                x--;
            } else if (currentMove.equals(KeyCode.RIGHT) && x<150) {
                x++;
            } else if (currentMove.equals(KeyCode.UP) && y > 0) {
                y--;
            } else if (currentMove.equals(KeyCode.DOWN) && y<150) {
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