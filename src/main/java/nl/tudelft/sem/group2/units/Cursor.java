package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.awt.*;

import static nl.tudelft.sem.group2.game.Board.gridToCanvas;

public class Cursor extends LineTraveller {
    private KeyCode currentMove = null;
    private int loops = 0;
    private int speed = 2;

    //todo set stix to false when implementation is done
    private boolean stix = true;
    private boolean isDrawing;
    private boolean isFast;
    public Cursor(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.sprite = new Image[1];
        this.sprite[0] = new Image("/images/cursor.png");
        isDrawing = false;
        isFast = true;
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
        int animationSpeed = 30;
        int drawX = gridToCanvas(x);
        int drawY = gridToCanvas(y);
        if (loops < animationSpeed) {
            GraphicsContext gC = canvas.getGraphicsContext2D();
            double height = canvas.getHeight();
            double heightVar = height / animationSpeed * loops;
            double width = canvas.getWidth();
            double widthVar = width / animationSpeed * loops;
            double lineSize = 40.0;
            double lineSizeVar = (lineSize / animationSpeed) * loops;
            gC.beginPath();
            gC.setStroke(Color.WHITE);
            //upRightCorner
            gC.moveTo(width - widthVar + drawX - (lineSize - lineSizeVar), -(height - heightVar) + drawY);
            gC.lineTo(width - widthVar + drawX, -(height - heightVar) + drawY + (lineSize - lineSizeVar));
            //downRightCorner
            gC.moveTo(width - widthVar + drawX - (lineSize - lineSizeVar), height - heightVar + drawY);
            gC.lineTo(width - widthVar + drawX, height - heightVar + drawY - (lineSize - lineSizeVar));
            //upLeftCorner
            gC.moveTo(-(width - widthVar) + drawX + (lineSize - lineSizeVar), -(height - heightVar) + drawY);
            gC.lineTo(-(width - widthVar) + drawX, -(height - heightVar) + drawY + (lineSize - lineSizeVar));
            //downLeftCorner
            gC.moveTo(-(width - widthVar) + drawX + (lineSize - lineSizeVar), height - heightVar + drawY);
            gC.lineTo(-(width - widthVar) + drawX, height - heightVar + drawY - (lineSize - lineSizeVar));
            gC.stroke();
            loops++;
        }
        canvas.getGraphicsContext2D().drawImage(sprite[spriteIndex], drawX - width / 2 + 1, drawY - height / 2 + 1, width, height);
        spriteIndex = (spriteIndex + 1) % sprite.length;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }



    public String toString() {
        return "Cursor";
    }

	public boolean isDrawing() {
		return isDrawing;
	}

	public void setDrawing(boolean isDrawing) {
		this.isDrawing = isDrawing;
	}

	public boolean isFast() {
		return isFast;
	}

	public void setFast(boolean isFast) {
		this.isFast = isFast;
	}
}