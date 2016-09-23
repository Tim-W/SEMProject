package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import nl.tudelft.sem.group2.AreaState;

import static nl.tudelft.sem.group2.game.Board.gridToCanvas;

/**
 * A unit which can travel over outerborders and innerborders.
 */
public abstract class LineTraveller extends Unit {

    private Image[] sprite;
    private int spriteIndex = 0;
    private int width;
    private int height;

    /**
     * Create a new LineTraveller.
     *
     * @param x      x coord
     * @param y      y coord
     * @param width  width, used for collision
     * @param height height, used for collision
     */
    public LineTraveller(int x, int y, int width, int height) {
        super(x, y,width,height);
    }

    /**
     * @param x x coord
     * @param y y coord
     * @return true if the tile at (x,y) has an UNCOVERED AreaState
     * TODO implement
     */
    public boolean uncoveredOn(int x, int y) {
        return getAreaTracker().getBoardGrid()[x][y].equals(AreaState.UNCOVERED);
    }

    /**
     * @param x x coord
     * @param y y coord
     * @return true if the tile at (x,y) has an INNERBORDER AreaState
     */
    protected boolean innerBorderOn(int x, int y) {
        return getAreaTracker().getBoardGrid()[x][y].equals(AreaState.INNERBORDER);
    }

    /**
     * @param x x coord
     * @param y y coord
     * @return true if the tile at (x,y) has an OUTERBORDER AreaState
     */
    protected boolean outerBorderOn(int x, int y) {
        return getAreaTracker().getBoardGrid()[x][y].equals(AreaState.OUTERBORDER);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.getGraphicsContext2D().drawImage(
                sprite[spriteIndex],
                gridToCanvas(getX()) - width / 2,
                gridToCanvas(getY()) - height / 2,
                width,
                height
        );
        spriteIndex = (spriteIndex + 1) % sprite.length;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public Image[] getSprite() {
        return sprite;
    }

    public void setSprite(Image[] sprite) {
        this.sprite = sprite;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }

    public void setSpriteIndex(int spriteIndex) {
        this.spriteIndex = spriteIndex;
    }
}
