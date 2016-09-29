package nl.tudelft.sem.group2.scenes;

import javafx.beans.NamedArg;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class BackgroundScene extends SubScene {
    Image image;
    Color fastColor;
    Color slowColor;

    public BackgroundScene(@NamedArg("root") Parent root,
                           @NamedArg("width") double width,
                           @NamedArg("height") double height) {
        super(root, width, height);
    }


}
