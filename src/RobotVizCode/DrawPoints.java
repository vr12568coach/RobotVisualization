package RobotVizCode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/* THIS CLASS RETIRED, MOVED INTO MAIN FILE */
public class DrawPoints {
    static double fieldHalfWidth = (12*12/2);//12 foot field half width in inches
//    static double radius = 5;
    public static void drawPoints(GraphicsContext gc, int cnt, ArrayList<RobotLocation> pointList, double[] RandB, double pntSize) {


        for (int i = 0; i < cnt; i++) {

            DefinePoint displayLocation = FieldToScreen.convertToScreenPoint(
                    new DefinePoint(fieldHalfWidth + pointList.get(i).x, fieldHalfWidth + pointList.get(i).y));

            gc.setStroke(new Color(0,0,0,.8));
            gc.setLineWidth(3.0);
            gc.setLineDashes(0.0);
            gc.strokeOval(displayLocation.x-pntSize,displayLocation.y-pntSize,2*pntSize,2*pntSize);
            double greenValue = 0.25 + .75 *((double) i / (double) pointList.size());
            gc.setFill(new Color(RandB[0], greenValue, RandB[1], 0.8));
//            gc.fillOval(displayLocation.x - radius, displayLocation.y - radius, 2 * radius, 2 * radius);
            gc.fillOval(displayLocation.x - pntSize, displayLocation.y - pntSize, 2 * pntSize, 2 * pntSize);


        }
    }
}
