package RobotVizCode;


/**
 * FieldToScreen.java
 * This class keeps track of where the robot is
 * and provides the methods to transform real coordinates into screen coordinates
 */
public class FieldToScreen {

    //the real width of the field
    public static final double ACTUAL_FIELD_SIZE = 12*12;// width in inches for 12 foot field (maybe need slight adjustment for actual size)


    //These are the REAL coordinates on the field that will be the center of the screen
    private static double centerXReal = 0;
    private static double centerYReal = 0;


    //These are the SCREEN dimensions
    public static double widthScreen = 800;//was 1000
    public static double heightScreen = 800;//was 1000


    //Increasing this makes the width and height in real scale smaller since it is zooming in
    private static double zoomPercent = 1.0;





    /**
     * Sets the center point of the screen in real coordinates
     * This is used to follow the robot
     */
    public static void setCenterPoint(double centerX, double centerY){
        centerXReal = centerX;
        centerYReal = centerY;
    }


    /**
     * This sets our width and height in pixels.
     * We need to know this to transform real coordinates into screen coordinates.
     * @param width width in pixels
     * @param height height in pixels
     */
    public static void setDimensionsPixels(double width, double height){
        widthScreen = width;
        heightScreen = height;
    }

    /**
     * Converts a real coordinate to a screen coordinate, relative to the top left of the window
     * @param p real coordinates
     * @return screen coordinates
     */
    public static DefinePoint convertToScreenPoint(DefinePoint p){
        //get where the top left of the screen is in real coordinates
        DefinePoint topLeft = getTopLeftScreenRealPosition();
        //now get where the point is in respect to the top left.
        //HOWEVER the top left is above us, we don't want y to be negative so swap the y
        DefinePoint relativeFromTopLeft = new DefinePoint(p.x-topLeft.x,topLeft.y-p.y);

        //now we can get the percent that we are across the screen
        double percentX = relativeFromTopLeft.x / getWindowSizeInRealScale().x;
        double percentY = relativeFromTopLeft.y / getWindowSizeInRealScale().y;

        //now that we have percents, multiply by the width and height to get pixel coordinates
        return new DefinePoint(percentX * widthScreen,percentY * heightScreen);
    }


    /**
     * This is only used by us to find the theoretical field location of the top left of the window
     * @return real coordinates of where the top left of the window is
     */
    private static DefinePoint getTopLeftScreenRealPosition(){
        //first get the window size in real dimensions
        DefinePoint windowSizeReal = getWindowSizeInRealScale();
        // the top left point of the screen is just the center of the screen translated up
        return new DefinePoint(centerXReal - windowSizeReal.x/2.0,
                centerYReal + windowSizeReal.y/2.0);
    }


    /**
     * Gets the screen size in real scale. This needs to consider zoom
     * @return floatPoint of the window's size in real coordinates
     */
    private static DefinePoint getWindowSizeInRealScale(){
        //now we can return size in real scale by multiplying the screen sizes by the screen pixel sizes
        return new DefinePoint(widthScreen * getInchesPerPixelWidth(),heightScreen * getInchesPerPixelHeight());
    }


    /**
     * Set the zoom of teh screen with this
     * @param zoom the zoom where 1.0 means the screen is one field size big, 2.0 means it is half
     */
    private static void setZoomPercent(double zoom){
        zoomPercent = zoom;
    }


    /**
     * Gets how many centimeters are in each pixel of the screen
     * @return
     */
    public static double getInchesPerPixelWidth(){
        //Get the conversion of centimeters per pixel. This is the size in inches of the biggest dimension
        //divided by the size in pixels of the biggest dimension
        return ACTUAL_FIELD_SIZE/ getFieldWidthPixels();
    }
    public static double getInchesPerPixelHeight(){
        //Get the conversion of centimeters per pixel. This is the size in inches of the biggest dimension
        //divided by the size in pixels of the biggest dimension
        return ACTUAL_FIELD_SIZE/ getFieldHeightPixels();
    }


    /**
     * Gets the field size in pixels
     * @return the field size in pixels
     */
    public static double getFieldWidthPixels(){
        return widthScreen/zoomPercent;
    }
    public static double getFieldHeightPixels(){
        return heightScreen/zoomPercent;
    }

}
