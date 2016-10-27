package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import nl.tudelft.sem.group2.board.AreaState;
import nl.tudelft.sem.group2.board.AreaTracker;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.board.Coordinate;
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
     * @param grid used for calculating areas
     */
    public LineTraveller(int x, int y, int width, int height, BoardGrid grid) {
        super(new Coordinate(x, y, width, height), grid);
    }

    /**
     * @return true if this tile at (x,y) has an UNCOVERED AreaState
     */
    public boolean uncoveredOn() {
        return grid.isUncovered(this);
    }

    /**
     * @return true if this tile at (x,y) has an INNERBORDER AreaState
     */
    protected boolean innerBorderOn() {
        return grid.isInnerborder(this);
    }

    /**
     * @return true if this tile at (x,y) has an OUTERBORDER AreaState
     */
    protected boolean outerBorderOn() {
        return grid.isOuterborder(this);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.getGraphicsContext2D().drawImage(
                sprite[getSpriteIndex()],
                gridToCanvas(this.x) - getWidth() / 2,
                gridToCanvas(this.y) - getHeight() / 2,
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
