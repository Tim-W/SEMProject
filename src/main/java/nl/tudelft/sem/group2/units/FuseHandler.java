package nl.tudelft.sem.group2.units;

import nl.tudelft.sem.group2.global.Globals;

import java.awt.Point;

/**
 * Created by dennis on 28-10-16.
 */
public class FuseHandler {

    private Fuse fuse;
    private Cursor cursor;

    /**
     * Creates a fuse handler for the cursor.
     *
     * @param cursor the cursor
     */
    public FuseHandler(Cursor cursor) {
        this.cursor = cursor;
    }

    /**
     * handles making fuse and makes it start moving.
     */
    public void handleFuse() {
        if (cursor.getStix().contains(new Point(cursor.getX(), cursor.getY()))) {
            if (fuse == null) {
                fuse =
                        new Fuse((int) cursor.getStix().getStixCoordinates().getFirst().getX(),
                                (int) cursor.getStix().getStixCoordinates().getFirst().getY(),
                                Globals.FUSE_WIDTH,
                                Globals.FUSE_HEIGHT, cursor.getStix());
            } else {
                fuse.moving();
            }
            cursor.setCurrentMove(null);
        }
    }

    /**
     * If there is a Fuse on the screen, remove it.
     */
    public void removeFuse() {
        fuse = null;
    }

    /**
     * @return the fuse if there is a fuse, otherwise null.
     */
    public Fuse getFuse() {
        return fuse;
    }

    /**
     * only for testing.
     *
     * @param fuse setter for fuse
     */
    public void setFuse(Fuse fuse) {
        this.fuse = fuse;
    }
}
