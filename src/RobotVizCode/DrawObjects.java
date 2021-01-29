package RobotVizCode;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import OfflineVizCode.DefinePoint;

/* THIS CLASS RETIRED, MOVED INTO MAIN FILE */
public class DrawObjects {
    //robot radius is half the diagonal length
    static double fieldHalfWidth = (12*12/2);//12 foot field half width in inches

    static double objectX = fieldHalfWidth;//set robot (0,0) form data at field center
    static double objectY = fieldHalfWidth;//set robot (0,0) form data at field center
    static double objectAngle = Math.toRadians(90);//Need to add 90 degrees = maybe be for reference EAST vs SOUTH

    public static DefinePoint drawImage(GraphicsContext gc, RobotLocation robotLoc, double w, double h, String imageFile) {

        double centerToCorner = Math.sqrt(Math.pow(w/2,2)+Math.pow(h/2,2));//update units to inches for diagonal distance of object
//        double rotCenterToCenterX = (robotLoc.x-robotLoc.rotx);
//        double rotCenterToCenterY = (robotLoc.y-robotLoc.roty);
//
//        double rotCenterToCorner = Math.sqrt(Math.pow(rotCenterToCenterX+w/2,2)+Math.pow(rotCenterToCenterY+h/2,2));//update units to inches for diagonal distance of object

//        double topLeftX = objectX + (rotCenterToCorner * (Math.cos(objectAngle + Math.toRadians(45))));
//        double topLeftY = objectY + (rotCenterToCorner * (Math.sin(objectAngle + Math.toRadians(45))));
        objectX = fieldHalfWidth+robotLoc.x;//set robot (0,0) form data at field center
        objectY = fieldHalfWidth+robotLoc.y;//set robot (0,0) form data at field center
        objectAngle = Math.toRadians(90) +robotLoc.theta;//  for reference EAST vs SOUTH?
        //added in the difference in robot angle to angle offset as the true rotation of object
//        followRobot(robotX,robotY);
        DefinePoint objectCenter = new DefinePoint(objectX, objectY);

//        double angleToCorner = Math.atan2(rotCenterToCenterY+h/2,rotCenterToCenterX+w/2);

        //Does the graphic creation need to go from top left or bottom left? - believe bottom left
        //Why is the angle +45 instead of 225?
        //Why is + 90 needed to be added to keep rotation center on robot.?
        double bottomLeftX = objectX + (centerToCorner * (Math.cos(objectAngle + Math.toRadians(45) )));
        double bottomLeftY = objectY + (centerToCorner * (Math.sin(objectAngle + Math.toRadians(45) )));

        try {
            DefinePoint bottomLeft = FieldToScreen.convertToScreenPoint(new DefinePoint(bottomLeftX,bottomLeftY));
            //why is the point defined above called bottomLeft when created from topLeft?
            double width = w/ FieldToScreen.getInchesPerPixelWidth();//calculate the width of the image in pixels (in inches)
            double height = h/ FieldToScreen.getInchesPerPixelHeight();//calculate the height of the image in pixels (in inches)

            gc.save();//save the gc
            gc.transform(new Affine(new Rotate(Math.toDegrees(-objectAngle)+90 , bottomLeft.x, bottomLeft.y)));
            //Need to add 90 degrees = maybe be for reference EAST vs SOUTH
            Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/"+imageFile));
            gc.drawImage(image,bottomLeft.x, bottomLeft.y,width,height);

            gc.restore();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return objectCenter;
    }


}
