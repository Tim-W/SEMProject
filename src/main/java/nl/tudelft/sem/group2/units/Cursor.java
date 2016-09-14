package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.util.LinkedList;

import static nl.tudelft.sem.group2.game.Board.gridToCanvas;

public class Cursor extends LineTraveller {
    private KeyCode currentMove = null;
    private int loops = 0;
    private int speed = 2;
    private LinkedList<double[][]> oldLines = new LinkedList<double[][]>();

    private int animationSpeed = 30;
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    private boolean isDrawing = false;

    //todo set stix to false when implementation is done
    private boolean stix = true;

    public Cursor(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.sprite = new Image[1];
        this.sprite[0] = new Image("/images/cursor.png");
    }

    @Override
    public void move() {
        for (int i = 0; i < speed; i++) {
            if (currentMove != null) {
                switch (currentMove){
                    case LEFT: {
                        if (x > 0) {
                            if (!checkLine(x - 1, y) && isDrawing) {
                                x--;
                                areaTracker.addToStix(new Point(x, y));
                            } else if(checkLine(x - 1, y)) {
                                x--;
                            }
                        }
                        break;
                    }
                    case RIGHT: {
                        if (x < 150) {
                            if (!checkLine(x + 1, y) && isDrawing) {
                                x++;
                                areaTracker.addToStix(new Point(x, y));
                            } else if(checkLine(x + 1, y)) {
                                x++;
                            }
                        }
                        break;
                    }
                    case UP: {
                        if (y > 0) {
                            if (!checkLine(x, y - 1) && isDrawing) {
                                y--;
                                areaTracker.addToStix(new Point(x, y));
                            } else if(checkLine(x, y - 1)) {
                                y--;
                            }
                        }
                        break;
                    }
                    case DOWN: {
                        if (y < 150) {
                            if (!checkLine(x, y + 1) && isDrawing) {
                                y++;
                                areaTracker.addToStix(new Point(x, y));
                            } else if(checkLine(x, y +1)) {
                                y++;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public KeyCode getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(KeyCode currentMove) {
        this.currentMove = currentMove;
    }

    @Override
    public void draw(Canvas canvas) {
        int drawX = gridToCanvas(x);
        int drawY = gridToCanvas(y);
        if (loops < animationSpeed) {
            double height = canvas.getHeight();
            double heightVar = height / animationSpeed * loops;
            double width = canvas.getWidth();
            double widthVar = width / animationSpeed * loops;
            double lineSize = 80.0;
            double lineSizeVar = (lineSize / animationSpeed) * loops;
            double[][] line = new double[4][4];
            line[0][0]=width - widthVar + drawX - (lineSize - lineSizeVar);
            line[0][1]=-(height - heightVar) + drawY;
            line[0][2]=width - widthVar + drawX;
            line[0][3]=-(height - heightVar) + drawY + (lineSize - lineSizeVar);
            line[1][0]=width - widthVar + drawX - (lineSize - lineSizeVar);
            line[1][1]=height - heightVar + drawY;
            line[1][2]=width - widthVar + drawX;
            line[1][3]=height - heightVar + drawY - (lineSize - lineSizeVar);
            line[2][0]=-(width - widthVar) + drawX + (lineSize - lineSizeVar);
            line[2][1]=-(height - heightVar) + drawY;
            line[2][2]=-(width - widthVar) + drawX;
            line[2][3]=-(height - heightVar) + drawY + (lineSize - lineSizeVar);
            line[3][0]=-(width - widthVar) + drawX + (lineSize - lineSizeVar);
            line[3][1]=height - heightVar + drawY;
            line[3][2]=-(width - widthVar) + drawX;
            line[3][3]=height - heightVar + drawY - (lineSize - lineSizeVar);
            oldLines.addFirst(line);
        }
        if(loops<animationSpeed+10){
            if(oldLines.size()>10||oldLines.size()>animationSpeed-loops){
                oldLines.removeLast();
            }
            GraphicsContext gC = canvas.getGraphicsContext2D();
            gC.setStroke(javafx.scene.paint.Color.WHITE);
            for (int j = 0; j < oldLines.size(); j++) {
                double[][] l = oldLines.get(j);
                gC.beginPath();
                for (int i = 0; i < 4; i++) {
                    gC.moveTo(l[i][0],l[i][1] );
                    gC.lineTo(l[i][2],l[i][3] );

                }
                gC.stroke();
            }
            loops++;
        }
        canvas.getGraphicsContext2D().drawImage(sprite[spriteIndex], drawX - width / 2 + 1, drawY - height / 2 + 1, width, height);
        spriteIndex = (spriteIndex + 1) % sprite.length;
    }

    public boolean isDrawing() {
        return isDrawing;
    }

    public void setDrawing(boolean drawing) {
        isDrawing = drawing;
    }

    public String toString() {
        return "Cursor";
    }
}