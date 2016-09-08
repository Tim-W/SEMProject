package Units;

import javafx.scene.image.Image;

/**
 * Created by gijs on 8-9-2016.
 */
abstract public class LineTraveller extends Unit{

    LineTraveller(int x, int y, int width, int height, Image[] sprite) {
        super(x, y, width, height, sprite);
    }
    //Todo needs to be implemented
    //returns true if there is a line on the given coordinates.
    private boolean checkLine(int x, int y){
        return false;
    }
}
