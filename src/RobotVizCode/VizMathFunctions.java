package RobotVizCode;

/* NOT USED?? */

public class VizMathFunctions {
    /**
     * Makes sure an angle is in the range of -180 to 180
     * @param angle
     * @return
     */
    public static double AngleWrap(double angle){
        while (angle<-Math.PI){
            angle += 2.0* Math.PI;
        }
        while (angle> Math.PI){
            angle -= 2.0* Math.PI;
        }
        return angle;
    }


}
