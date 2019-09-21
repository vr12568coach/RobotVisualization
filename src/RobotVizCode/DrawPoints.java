package RobotVizCode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class DrawPoints {
    static double fieldHalfWidth = (12*12/2);//12 foot field half width in inches
    static double radius = 5;
    public static void drawPoints(GraphicsContext gc, int cnt, ArrayList<RobotLocation> pointList, double[] RandB) {


        for (int i = 0; i < cnt; i++) {

            DefinePoint displayLocation = FieldToScreen.convertToScreenPoint(
                    new DefinePoint(fieldHalfWidth + pointList.get(i).x, fieldHalfWidth + pointList.get(i).y));

//            gc.setStroke(new Color(i/displayPoints.size() ,0.1,0.5,0.8));
//            gc.strokeOval(displayLocation.x-radius,displayLocation.y-radius,2*radius,2*radius);
            double greenValue = 0.25 + .75 *((double) i / (double) pointList.size());
            gc.setFill(new Color(RandB[0], greenValue, RandB[1], 0.8));
            gc.fillOval(displayLocation.x - radius, displayLocation.y - radius, 2 * radius, 2 * radius);
//            System.out.println(String.format("writing oval number %d @ location (%.2f,%.2f), color = %.3f",i,displayLocation.x-radius,displayLocation.y-radius,colorValue));

        }
    }
}
