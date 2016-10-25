package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.collisions.CollisionInterface;

import static nl.tudelft.sem.group2.scenes.GameScene.gridToCanvas;

/**
 * A unit which can travel over outerborders and innerborders.
 */
public abstract class LineTraveller extends Unit implements CollisionInterface {

    private Image[] sprite;
    private int spriteIndex = 0;

    /**
     * Create a new LineTraveller.
     *
     * @param x           x coord
     * @param y           y coord
     * @param width       width, used for collision
     * @param height      height, used for collision
     * @param areaTracker used for calculating areas
     */
    public LineTraveller(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height, areaTracker);
    }

    /**
     * @param x x coord
     * @param y y coord
     * @return true if the tile at (x,y) has an UNCOVERED AreaState
     */
    public boolean uncoveredOn(int x, int y) {
        return getAreaTracker().getBoardGrid()[x][y].equals(AreaState.UNCOVERED);
    }

    /**
     * @param x x coord
     * @param y y coord
     * @return true if the tile at (x,y) has an INNERBORDER AreaState
     */
    public boolean innerBorderOn(int x, int y) {
        return getAreaTracker().getBoardGrid()[x][y].equals(AreaState.INNERBORDER);
    }

    /**
     * @param x x coord
     * @param y y coord
     * @return true if the tile at (x,y) has an OUTERBORDER AreaState
     */
    public boolean outerBorderOn(int x, int y) {
        return getAreaTracker().getBoardGrid()[x][y].equals(AreaState.OUTERBORDER);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.getGraphicsContext2D().drawImage(
                sprite[getSpriteIndex()],
                gridToCanvas(getX()) - getWidth() / 2,
                gridToCanvas(getY()) - getHeight() / 2,
                getWidth(),
                getHeight()
        );
        incrementSpriteIndex();
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

    private void incrementSpriteIndex() {
        setSpriteIndex((spriteIndex + 1) % sprite.length);
    }
}
