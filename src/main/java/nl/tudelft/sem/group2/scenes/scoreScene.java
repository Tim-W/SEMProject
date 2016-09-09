package nl.tudelft.sem.group2.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import nl.tudelft.sem.group2.LaunchApp;

public class ScoreScene extends Scene{

	

	
	public ScoreScene(Parent arg0, double arg1, double arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
		
		Font.loadFont(LaunchApp.class.getResource("QixFont.ttf").toExternalForm(),10);

	}

	
	
}
