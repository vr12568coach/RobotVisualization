package RobotVizCode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import OfflineVizCode.DefinePoint;


public class DrawRobots {
    //robot radius is half the diagonal length
    static double robotRadius = Math.sqrt(2) * 18.0/ 2.0;//update units to inches for 18" square
    static double fieldHalfWidth = (12*12/2);//12 foot field half width in inches

    static double robotX = fieldHalfWidth;//set robot (0,0) form data at field center
    static double robotY = fieldHalfWidth;//set robot (0,0) form data at field center
    static double robotAngle = Math.toRadians(90);//Need to add 90 degrees = maybe be for reference EAST vs SOUTH

    static double topLeftX = robotX + (robotRadius * (Math.cos(robotAngle+ Math.toRadians(45))));
    static double topLeftY = robotY + (robotRadius * (Math.sin(robotAngle+ Math.toRadians(45))));
    static double topRightX = robotX + (robotRadius * (Math.cos(robotAngle- Math.toRadians(45))));
    static double topRightY = robotY + (robotRadius * (Math.sin(robotAngle- Math.toRadians(45))));
    static double bottomLeftX = robotX + (robotRadius * (Math.cos(robotAngle+ Math.toRadians(135))));
    static double bottomLeftY = robotY + (robotRadius * (Math.sin(robotAngle+ Math.toRadians(135))));
    static double bottomRightX = robotX + (robotRadius * (Math.cos(robotAngle- Math.toRadians(135))));
    static double bottomRightY = robotY + (robotRadius * (Math.sin(robotAngle- Math.toRadians(135))));

    public static DefinePoint drawImage(GraphicsContext gc, RobotLocation robotLoc, String imageFile) {


        robotX = fieldHalfWidth+robotLoc.x;//set robot (0,0) form data at field center
        robotY = fieldHalfWidth+robotLoc.y;//set robot (0,0) form data at field center
        robotAngle = Math.toRadians(90) + robotLoc.theta;//Need to add 90 degrees = maybe be for reference EAST vs SOUTH
//        followRobot(robotX,robotY);
        DefinePoint robotCenter = new DefinePoint(robotX,robotY);



        topLeftX = robotX + (robotRadius * (Math.cos(robotAngle+ Math.toRadians(45))));
        topLeftY = robotY + (robotRadius * (Math.sin(robotAngle+ Math.toRadians(45))));
//        topRightX = robotX + (robotRadius * (Math.cos(robotAngle- Math.toRadians(45))));
//        topRightY = robotY + (robotRadius * (Math.sin(robotAngle- Math.toRadians(45))));
//        bottomLeftX = robotX + (robotRadius * (Math.cos(robotAngle+ Math.toRadians(135))));
//        bottomLeftY = robotY + (robotRadius * (Math.sin(robotAngle+ Math.toRadians(135))));
//        bottomRightX = robotX + (robotRadius * (Math.cos(robotAngle- Math.toRadians(135))));
//        bottomRightY = robotY + (robotRadius * (Math.sin(robotAngle- Math.toRadians(135))));

//        Color c = Color.color(0.0,0.5,0.25);
        //draw the points
//        drawLineField(gc,topLeftX, topLeftY, topRightX, topRightY,c);
//        drawLineField(gc,topRightX, topRightY, bottomRightX, bottomRightY,c);
//        drawLineField(gc,bottomRightX, bottomRightY, bottomLeftX, bottomLeftY,c);
//        drawLineField(gc,bottomLeftX, bottomLeftY, topLeftX, topLeftY,c);
//

        try {
            DefinePoint bottomLeft = FieldToScreen.convertToScreenPoint(new DefinePoint(topLeftX,topLeftY));
            double width = 18/ FieldToScreen.getInchesPerPixel();//calculate the width of the image in pixels (in inches)

            gc.save();//save the gc
            gc.transform(new Affine(new Rotate(Math.toDegrees(-robotAngle)+90, bottomLeft.x, bottomLeft.y)));//Need to add 90 degrees = maybe be for reference EAST vs SOUTH
//            Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/robot.png"));
            Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/"+imageFile));
            gc.drawImage(image,bottomLeft.x, bottomLeft.y,width,width);


            gc.restore();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return robotCenter;
    }


}
