package main.java.nl.tudelft.sem.group2.game;

import javafx.scene.canvas.Canvas;
import main.java.nl.tudelft.sem.group2.units.Unit;

import java.util.Set;

public class Board {
    Canvas canvas;
    Set<Unit> units;

    public Board(Set<Unit> units) {
        this.units = units;
    }

    public void setUnits(Set<Unit> units) {
        this.units = units;
    }

    public Set<Unit> getUnits() {
        return units;
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }

    public boolean removeUnit(Unit unit) {
        return this.units.remove(unit);
    }

    public void draw() {
        for (Unit unit : units) {
            unit.move();
            unit.draw(canvas);
        }
    }
}