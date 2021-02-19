package RobotVizCode;




import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class RobotOnFieldVizMain extends Application {
    //this is the ImageView that will hold the field background
    private ImageView fieldBackgroundImageView;
    private Canvas fieldCanvas;

    private Group rootGroup;//holds the grid and the field stuff

    //this will overlay stuff for other debugging purposes. This is inside the rootGroup
    private HBox mainHBox;
    public static Semaphore drawSemaphore = new Semaphore(1);


    //////////////////////ALL LAYOUT PARAMETERS////////////////////////
    private final int MAIN_GRID_HORIZONTAL_GAP = 100;//horizontal spacing of the main grid
    private final int MAIN_GRID_VERTICAL_GAP = 100;//vertical spacing of the main grid
    ///////////////////////////////////////////////////////////////////

    RobotVizCode.ReadRobotData roboRead = new RobotVizCode.ReadRobotData();

    public static int ARRAY_SIZE = 300;

    public static ArrayList<RobotVizCode.RobotLocation> robot1Points = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot2Points = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot3Points = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot4Points = new ArrayList<>();//all the points to display

    public static ArrayList<RobotVizCode.RobotLocation> robot1PursuitPoints = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot2PursuitPoints = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot3PursuitPoints = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot4PursuitPoints = new ArrayList<>();//all the points to display

    public static ArrayList<RobotVizCode.RobotLocation> robot1NavPoints = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot2NavPoints = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot3NavPoints = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot4NavPoints = new ArrayList<>();//all the points to display

    public static ArrayList<RobotVizCode.RobotLocation> BlueWobbleGoal1Points = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> BlueRingPoints = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> BlueWobbleGoal2Points = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> RedRingPoints = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> RedWobbleGoal2Points = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> RedWobbleGoal1Points = new ArrayList<>();//all the points to display

    public static AccessoryList robot1Acc = new AccessoryList();
    public static AccessoryList robot2Acc = new AccessoryList();
    public static AccessoryList robot3Acc = new AccessoryList();
    public static AccessoryList robot4Acc = new AccessoryList();

    public static ArrayList<RobotVizCode.DefineLine> robot1Lines = new ArrayList<>();//all the lines to display
    public static ArrayList<RobotVizCode.DefineLine> robot2Lines = new ArrayList<>();//all the lines to display
    public static ArrayList<RobotVizCode.DefineLine> robot3Lines = new ArrayList<>();//all the lines to display
    public static ArrayList<RobotVizCode.DefineLine> robot4Lines = new ArrayList<>();//all the lines to display

    public static String ringSetString = "none";

    public static int counter = 0;

    static double  standardWidthPixels = 800;//standard width for scaling of window 800
    static double  standardHeightPixels = 800;//standard height for scaling of window 800 or 816 or 823
    /** SET THE INPUT VALUES FOR THE SIZE AND LOCATION
     * stageWidth is the horizontal size
     * stageHEight is the vertical size
     *
     */
    public static double stageWidth = 680;//defaults but actual values are defined in method setDisplay()
    public static double stageHeight = 680;//defaults but actual values are defined in method setDisplay()

    public final double FIELD_HALF_WIDTH = (12.0*12.0/2.0);//12 foot field half width in inches
    //Sets the robot image graphics size
    public double robotWidth = 12;//inches across front to back w/o gripper since robots start front to back in X (Width)
    public double robotHeight = 16.25;//inches across wheels

    private enum user {KARL,CALEB, WILL};
    private enum colorType {BLUE, RED};
    /**
     * Launches
     */
    public static void main(String[] args){
        launch(args);
    }

    /**
     * Runs at the initialization of the window (after main)
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
//    public void start(JFrame frame, Stage primaryStage) throws Exception {
        //WINDOW STUFF//

        primaryStage.setTitle("Test Robot Visualization");

        setDisplay(user.KARL, 1,primaryStage);//screen = 0 for single screen or 1 for dual displays

        //this is the group that holds everything
        rootGroup = new Group();
        //create a new scene, pass the rootGroup
        Scene scene = new Scene(rootGroup);
        //Now we can setup the HBox
        mainHBox = new HBox();

        //bind the main hbox width to the primary stage width so that changes with it
        mainHBox.prefWidthProperty().bind(primaryStage.widthProperty());
        mainHBox.prefHeightProperty().bind(primaryStage.heightProperty());

        ///////////////////////////////////Setup the background image/////////////////////////////////
        Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/UltGoalField.png"));

        fieldBackgroundImageView = new ImageView();
        fieldBackgroundImageView.setImage(image);//set the image

        //add the background image
        rootGroup.getChildren().add(fieldBackgroundImageView);
        //////////////////////////////////////////////////////////////////////////////////////////////


        //Setup the canvas//


        fieldCanvas = new Canvas(stageWidth,stageHeight);// set to teh user input width

        //the GraphicsContext is what we use to draw on the fieldCanvas
        GraphicsContext gc = fieldCanvas.getGraphicsContext2D();

        rootGroup.getChildren().add(fieldCanvas);//add the canvas
        ////////////////////

        /**
         * We will use a vbox and set it's width to create a spacer in the window
         * USE THIS TO CHANGE THE SPACING (KS: not sure what this really does)
         */
//        VBox debuggingHSpacer = new VBox();
//        mainHBox.getChildren().add(debuggingHSpacer);

        //Create the robot coordinates log display
        Group logGroup = new Group();
        Image logImage = new Image(new FileInputStream(System.getProperty("user.dir") + "/RobotLog3.png"));

        ImageView logImageView = new ImageView();
        logImageView.setImage(logImage);//set the image

        logImageView.setFitHeight(270 * stageHeight / standardHeightPixels);//set height based on scaling user input size
        logImageView.setFitWidth(200 * stageWidth / standardWidthPixels);//set width based on scaling user input size

        /** set location to remain in same position regardless of window size
         * use 0 for LH side, 285 for center, 550 for RH side
         * Set so that as logGroup position is changed keeping the relative location of the text box the same
         */
        logGroup.setTranslateY(10 * stageHeight/ standardHeightPixels);
        logGroup.setTranslateX(0 * stageWidth / standardWidthPixels);


        //add the background image
        logGroup.getChildren().add(logImageView);

        //Create the labels and test output for the robot coordinates
        Label robotCoordsLabel = new Label();
        robotCoordsLabel.setFont(new Font("Arial",12* stageHeight / standardHeightPixels));
        robotCoordsLabel.textFillProperty().setValue(new Color(0,0.2,1.0,1));
        robotCoordsLabel.setPrefWidth(logImageView.getFitWidth()-25);//was setPrefWidth(logImageView.getFitWidth()-25)

        robotCoordsLabel.setLayoutX(16* stageWidth / standardWidthPixels);
        robotCoordsLabel.setLayoutY(40 * stageHeight / standardHeightPixels);//was logImageView.getFitHeight()/5

        robotCoordsLabel.setWrapText(true);

        logGroup.getChildren().add(robotCoordsLabel);

        mainHBox.getChildren().add(logGroup);//add the log group
        //appears that items needed to be added from the background or lower levels first and last is the top level group

        //now we can add the mainHBox to the root group
        rootGroup.getChildren().add(mainHBox);
        scene.setFill(Color.AZURE);//set the background color for any of the scene not covered by the background image
        primaryStage.setScene(scene);//set the primary stage's scene

        primaryStage.setWidth(stageWidth);//set based on user inputs
        primaryStage.setHeight(stageHeight);//set based on user inputs
        primaryStage.setMaximized(false);//do not start the stage maximized, size controlled by inputs

        //show the primaryStage
        primaryStage.show();

        ringSetString = roboRead.readRingSet("RingSet.txt");
        //CREATE A NEW ANIMATION TIMER THAT WILL CALL THE DRAWING OF THE SCREEN
        new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                try {
                    //Load data on initial pass
                    if(counter == 0) {
                        /**should look to make a method to simplify and reduce the lines of code
                         *
                         */
                        robot1Lines.clear();
                        robot1Lines.addAll(roboRead.readLines("Robot1Path.txt"));
                        robot2Lines.clear();
                        robot2Lines.addAll(roboRead.readLines("Robot2Path.txt"));
                        robot3Lines.clear();
                        robot3Lines.addAll(roboRead.readLines("Robot3Path.txt"));
                        robot4Lines.clear();
                        robot4Lines.addAll(roboRead.readLines("Robot4Path.txt"));

                        robot1Points.clear();
                        robot1Points.addAll(roboRead.readData("Robot1OnField.txt"));
                        robot1Acc = ReadAccessories.readAcc("Robot1Accessories.txt");
//                        robot1Gripper.clear();
//                        robot1Gripper.addAll(roboRead.readData("Robot1Gripper.txt"));
                        robot1PursuitPoints.clear();
                        robot1PursuitPoints.addAll(roboRead.readData("Robot1Pursuit.txt"));
                        robot1NavPoints.clear();
                        robot1NavPoints.addAll(roboRead.readData("Robot1Nav.txt"));

                        robot2Points.clear();
                        robot2Points.addAll(roboRead.readData("Robot2OnField.txt"));
                        robot2PursuitPoints.clear();
                        robot2PursuitPoints.addAll(roboRead.readData("Robot2Pursuit.txt"));
                        robot2Acc = ReadAccessories.readAcc("Robot2Accessories.txt");
                        robot2NavPoints.clear();
                        robot2NavPoints.addAll(roboRead.readData("Robot2Nav.txt"));

                        robot3Points.clear();
                        robot3Points.addAll(roboRead.readData("Robot3OnField.txt"));
                        robot3PursuitPoints.clear();
                        robot3PursuitPoints.addAll(roboRead.readData("Robot3Pursuit.txt"));
                        robot3Acc = ReadAccessories.readAcc("Robot3Accessories.txt");
                        robot3NavPoints.clear();
                        robot3NavPoints.addAll(roboRead.readData("Robot3Nav.txt"));

                        robot4Points.clear();
                        robot4Points.addAll(roboRead.readData("Robot4OnField.txt"));
                        robot4PursuitPoints.clear();
                        robot4PursuitPoints.addAll(roboRead.readData("Robot4Pursuit.txt"));
                        robot4Acc = ReadAccessories.readAcc("Robot4Accessories.txt");
                        robot4NavPoints.clear();
                        robot4NavPoints.addAll(roboRead.readData("Robot4Nav.txt"));

                        RedWobbleGoal1Points.clear();
                        RedWobbleGoal1Points.addAll(roboRead.readData("RedWobble1.txt"));
                        RedWobbleGoal2Points.clear();
                        RedWobbleGoal2Points.addAll(roboRead.readData("RedWobble2.txt"));
                        BlueWobbleGoal1Points.clear();
                        BlueWobbleGoal1Points.addAll(roboRead.readData("BlueWobble1.txt"));
                        BlueWobbleGoal2Points.clear();
                        BlueWobbleGoal2Points.addAll(roboRead.readData("BlueWobble2.txt"));
                        BlueRingPoints.clear();
                        BlueRingPoints.addAll(roboRead.readData("BlueRing.txt"));
                        RedRingPoints.clear();
                        RedRingPoints.addAll(roboRead.readData("RedRing.txt"));
//

                    }
                    /**acquire the drawing semaphore
                     * should learn more about this aspect of the graphics
                     */
                    drawSemaphore.acquire();

                    //set the width and height
                    FieldToScreen.setDimensionsPixels(scene.getWidth(),scene.getHeight());

                    fieldCanvas.setWidth(FieldToScreen.getFieldWidthPixels());
                    fieldCanvas.setHeight(FieldToScreen.getFieldHeightPixels());

                    fieldBackgroundImageView.setFitWidth(FieldToScreen.getFieldWidthPixels());
                    fieldBackgroundImageView.setFitHeight(FieldToScreen.getFieldHeightPixels());

//                    debuggingHSpacer.setPrefWidth(scene.getWidth() * 0.01); //not sure what this spacer does

                    robotCoordsLabel.setMaxWidth(scene.getWidth() * 0.5);
//************ UPDATES FOR WINDOW SIZING ************************************
                    /** optional code for moving the background image within the scene
                     * fieldBackgroundImageView.setTranslateX(-3* scene.getWidth()/standardWidthPixels);
                     * fieldBackgroundImageView.setTranslateY(-4* scene.getHeight()/standardHeightPixels);
                     *
                     */
                    logImageView.setFitHeight(270 * scene.getHeight() / standardHeightPixels);//was logImage.getHeight()/1.25
                    logImageView.setFitWidth(200 * scene.getWidth()/ standardWidthPixels);//was logImage.getWidth()/1.6
                    robotCoordsLabel.setFont(new Font("Arial",12* scene.getHeight() / standardHeightPixels));
                    logGroup.setTranslateY(10 * scene.getHeight() / standardHeightPixels);//was 10 or 100
                    //Set so that as stage Width is changed the relative location of the text box is the same
                    logGroup.setTranslateX(550 * scene.getWidth() / standardWidthPixels);// use 0 for LH side, 285 for center, 550 for RH side
                    //Set so that as stage Width is changed the relative location of the text box is the same
                    robotCoordsLabel.setLayoutX(16* scene.getWidth() / standardWidthPixels);
                    robotCoordsLabel.setLayoutY(50* scene.getHeight()  / standardHeightPixels );//was logImageView.getFitHeight()/5
//************ UPDATES FOR WINDOW SIZING ************************************

                    drawScreen(gc);
                    robotCoordsLabel.setText("COORDINATES:"+
                            String.format("\n\nBlue Alliance:")+
                            String.format("\nX1: %.2f  |  X2: %.2f", robot1Points.get(counter).x,robot2Points.get(counter).x)+
                            String.format("\nY1: %.2f  |  Y2: %.2f", robot1Points.get(counter).y,robot2Points.get(counter).y) +
                            String.format("\nAng1:%.1f°  |  Ang2:%.1f°",Math.toDegrees(robot1Points.get(counter).theta),Math.toDegrees(robot2Points.get(counter).theta)) +
                            String.format("\n\nRed Alliance:")+
                            String.format("\nX3: %.2f  |  X4: %.2f", robot3Points.get(counter).x,robot4Points.get(counter).x)+
                            String.format("\nY3: %.2f  |  Y4: %.2f", robot3Points.get(counter).y,robot4Points.get(counter).y) +
                            String.format("\nAng3:%.1f°  |  Ang4:%.1f°",Math.toDegrees(robot3Points.get(counter).theta),Math.toDegrees(robot4Points.get(counter).theta)) +
                            String.format("\nElapsed Time: %.1f",(double)counter/10.0));
//************ UPDATES FOR WINDOW SIZING OUTPUT ************************************
                    System.out.println(String.format("Stage Input W: %.1f, H: %.1f & Current W: %.1f, H: %.1f,",stageWidth,stageHeight,primaryStage.getWidth(),primaryStage.getHeight()));
                    System.out.println(String.format(" > Current Scene Width: %.1f, Height: %.1f",scene.getWidth(),scene.getHeight()));
                    System.out.println(String.format(" > fieldCanvas Width: %.1f, Height: %.1f",fieldCanvas.getWidth(),fieldCanvas.getHeight()));
                    System.out.println(String.format(" > fieldBackgroundImageView Width: %.1f, Height: %.1f",fieldBackgroundImageView.getFitWidth(),fieldBackgroundImageView.getFitHeight()));
//************ UPDATES FOR WINDOW SIZING OUTPUT ************************************

                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                drawSemaphore.release();

            }
        }.start();
    }


    /**
     * This will draw the screen using the graphics context
     * @param gc the graphics context
     */
    private void drawScreen(GraphicsContext gc) {
        //clear everything first
        gc.clearRect(0, 0, FieldToScreen.widthScreen, FieldToScreen.heightScreen);
//        gc.fillRect(0,0,Screen.widthScreen,Screen.heightScreen);

        //Draw the robot pursuit lines
        double[] lineBlack = {0, 0, 0};
        double[] lineRed = {1, 0, 0};
        double[] lineBlue = {0, 0, 1};

        drawLines(gc, robot1Lines, lineBlack,0);//no dashses
        drawLines(gc, robot2Lines,lineBlue,10);//dashes
        drawLines(gc, robot3Lines,lineBlack,0);
        drawLines(gc, robot4Lines,lineRed,10);

        //then draw the robot
        //Alternate robot draw option for multi-robots

        DefinePoint robot1Center = drawImage(gc, robot1Points.get(counter), robotWidth, robotHeight, "NewRobot1.png");
        setScreenCenter(robot1Center.x, robot1Center.y); // sets screen center for field, only run for 1st image
        drawAccessories(gc,robot1Points.get(counter), robot1Acc, colorType.BLUE);

        drawImage(gc, robot2Points.get(counter), robotWidth, robotHeight, "NewRobot2.png");
        drawAccessories(gc,robot2Points.get(counter), robot2Acc, colorType.BLUE);


        drawImage(gc, BlueWobbleGoal1Points.get(counter), 8, 8, "blueWobbleGoal.png");
        drawImage(gc, BlueWobbleGoal2Points.get(counter), 8, 8, "blueWobbleGoal.png");

        if (ringSetString.equals("ring")) {
            drawImage(gc, BlueRingPoints.get(counter), 5, 5, "ring.png");
            drawImage(gc, RedRingPoints.get(counter),5, 5,"ring.png");
        }
        else if (ringSetString.equals("ringStack")){
            drawImage(gc, BlueRingPoints.get(counter),5,5.5,"ringStack.png");
            drawImage(gc, RedRingPoints.get(counter),5, 5.5,"ringStack.png");
        }


        drawImage(gc, robot3Points.get(counter), robotWidth, robotHeight,"RedRobot1.png");
        drawAccessories(gc,robot3Points.get(counter), robot3Acc, colorType.RED);

        drawImage(gc, robot4Points.get(counter), robotWidth, robotHeight,"RedRobot2.png");
        drawAccessories(gc,robot4Points.get(counter), robot4Acc, colorType.RED);

        drawImage(gc, RedWobbleGoal1Points.get(counter),8,8,"redWobbleGoal.png");
        drawImage(gc, RedWobbleGoal2Points.get(counter),8,8,"redWobbleGoal.png");

        //draw all the lines and points from Offline Simulation
        // Colors are defined for Red, Green, and Blue in RGB format
        // If color should change during the display, only define 2 values
        // drawPoints uses RGB and checks length of array, if ==2, then it defines G internally as 0.25 + .75 *(step / size); size = 300
        double[] colorBlueRobot = {0.3, 1.0};//purple to cyan
//        double[] colors2 = {0.4, 0.9};//purple to cyan
        double[] colorBluePursuit = {0.0, 0.8, 0.8};//cyan
        double[] colorBlueNav = {0.0, 1.0, 0.0};//green

        double[] colorRedRobot = {1, 0};// red to yellow
//        double[] colors4 = {0.9, 0.0};// red to yellow
        double[] colorRedPursuit = {1.0, 1.0, 0.4};//light yellow
        double[] colorRedNav= {0.4, 0.4, 0.4};//gray

        gc.setStroke(new Color(0.4,0.4,0.4,1));//use this to test colors

        drawPoints(gc, counter, robot1Points,colorBlueRobot,5,false);
        drawPoints(gc, counter, robot1PursuitPoints,colorBluePursuit,5,false);
        drawPoints(gc, counter, robot1NavPoints,colorBlueNav,2.5,false);

        drawPoints(gc, counter, robot2Points,colorBlueRobot,5,true);
        drawPoints(gc, counter, robot2PursuitPoints,colorBluePursuit,5,true);
        drawPoints(gc, counter, robot2NavPoints,colorBlueNav,2.5,true);

        drawPoints(gc, counter, robot3Points,colorRedRobot,5,false);
        drawPoints(gc, counter, robot3PursuitPoints,colorRedPursuit,5,false);
        drawPoints(gc, counter, robot3NavPoints,colorRedNav,2.5,false);

        drawPoints(gc, counter, robot4Points,colorRedRobot,5,true);
        drawPoints(gc, counter, robot4PursuitPoints,colorRedPursuit,5,true);
        drawPoints(gc, counter, robot4NavPoints,colorRedNav,2.5,true);

        counter+=1;
        if(counter > (ARRAY_SIZE - 1)) {
            counter = 0;
        }

        /** COMMENTED SECTION CAN BE USED TO SLOW DOWN GRAPHICS DISPLAY

        try{
            Thread.sleep(25);//(was 50)This can slow down the time between points and therefore the robot speed
            //WIth 4 robots this may not be necessary because of processing time for each step
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
         */
    }
    /**
     * Plot lines to see desired path for Pure Pursuit
     */
    private void drawLines(GraphicsContext gc, ArrayList<DefineLine> lineList, double[] colorArray, double dashVal) {
        double fieldHalfWidth = (12*12/2);//12 foot field half width in inches

        for(int i = 0; i < lineList.size(); i ++){
            DefinePoint displayLocation1 = FieldToScreen.convertToScreenPoint(
                    new DefinePoint(fieldHalfWidth+lineList.get(i).x1, fieldHalfWidth+lineList.get(i).y1));
            DefinePoint displayLocation2 = FieldToScreen.convertToScreenPoint(
                    new DefinePoint(fieldHalfWidth+lineList.get(i).x2, fieldHalfWidth+lineList.get(i).y2));


            gc.setLineWidth(4);
            gc.setStroke(new Color(colorArray[0],colorArray[1],colorArray[2], 1.0));

            gc.setLineDashes(dashVal);
            gc.strokeLine(displayLocation1.x,displayLocation1.y,displayLocation2.x,displayLocation2.y);
        }
    }

    /**
     *
     * @param gc
     * @param cnt
     * @param pointList
     * @param RandB
     * @param pntSize
     */
    private void drawPoints(GraphicsContext gc, int cnt, ArrayList<RobotLocation> pointList, double[] RandB, double pntSize, boolean outline) {


        for (int i = 0; i < cnt; i++) {

            DefinePoint displayLocation = FieldToScreen.convertToScreenPoint(
                    new DefinePoint(FIELD_HALF_WIDTH + pointList.get(i).x, FIELD_HALF_WIDTH + pointList.get(i).y));
            if(outline) {
                gc.setStroke(new Color(0, 0, 0, .8));
                gc.setLineWidth(3.0);
                gc.setLineDashes(0.0);
                gc.strokeOval(displayLocation.x - pntSize, displayLocation.y - pntSize, 2 * pntSize, 2 * pntSize);
            }
            if(RandB.length == 2) {
                double greenValue = 0.25 + .75 * ((double) i / (double) pointList.size());
                gc.setFill(new Color(RandB[0], greenValue, RandB[1], 0.8));
            }
            else{
                gc.setFill(new Color(RandB[0], RandB[1], RandB[2], 0.8));

            }
//            gc.fillOval(displayLocation.x - radius, displayLocation.y - radius, 2 * radius, 2 * radius);
            gc.fillOval(displayLocation.x - pntSize, displayLocation.y - pntSize, 2 * pntSize, 2 * pntSize);


        }
    }

    /**
     * drawImage creates images on the field based on the inputs provided
     * @param gc
     * @param robotLoc
     * @param w
     * @param h
     * @param imageFile
     * @return
     */
    public DefinePoint drawImage(GraphicsContext gc, RobotLocation robotLoc, double w, double h, String imageFile) {

        double centerToCorner = Math.sqrt(Math.pow(w/2,2)+Math.pow(h/2,2));//update units to inches for diagonal distance of object

//robot radius is half the diagonal length

        double objectX = FIELD_HALF_WIDTH+robotLoc.x;//set robot (0,0) form data at field center
        double objectY = FIELD_HALF_WIDTH+robotLoc.y;//set robot (0,0) form data at field center
        double objectAngle = Math.toRadians(90) +robotLoc.theta;//  for reference EAST vs SOUTH?
        //added in the difference in robot angle to angle offset as the true rotation of object
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


    /**
     * This will move the background image and everything else to be aligned to teh screen center
     * Needs to be run once on Robot1
     */
    private void setScreenCenter(double robotX, double robotY){
        //set the center point to the robot
//        TestScreen.setCenterPoint(robotX, robotY);

//        Set center to be 50% of width & height
        FieldToScreen.setCenterPoint(FieldToScreen.getInchesPerPixelWidth()* FieldToScreen.widthScreen/2.0,
                FieldToScreen.getInchesPerPixelHeight()* FieldToScreen.heightScreen/2.0);

        //        Set center to be (0,0))
//        TestScreen.setCenterPoint(0,0);

        //get where the origin of the field is in pixels
        DefinePoint originInPixels = FieldToScreen.convertToScreenPoint(new DefinePoint(0, FieldToScreen.ACTUAL_FIELD_SIZE));
        //get origin as (0,0)
//        DefinePoint originInPixels = convertToScreenPoint(new DefinePoint(0, 0));
        fieldBackgroundImageView.setX(originInPixels.x);
        fieldBackgroundImageView.setY(originInPixels.y);
    }


    /** This will calculate the wobbleGoalArm graphics size and position based on the WGA angle accessory data
     *
     * @param robot is a robot field location point (x, y, theta)
     * @param accList is a robot accessories list - shooter, conveyor, collector, and WGA angle info
     * @return
     */
    private double[] defineWGA(RobotLocation robot, AccessoryList accList){
        double length = 12.0;
        double[] returnData = new double[5];

        double theta = robot.theta;
        returnData[4] = 3.0; //width of WGA arm (Y)
        returnData[3] =length * Math.cos(Math.PI - 0.7*accList.wobbleArmAngleRad[counter]-(30.0*Math.PI/180.0)); //length of arm viewed top down (X)
        double deltaY = 8.0 -3.0/2.0;//distance to arm center from robot center
        double deltaX = 3.5 + Math.abs(returnData[3]/2);//distance to arm center from robot center, must include projected arm length
        returnData[0] = robot.x + deltaX*Math.cos(theta) - deltaY*Math.sin(theta);//wobble arm X location on field
        returnData[1] = robot.y + deltaX*Math.sin(theta) + deltaY*Math.cos(theta);//wobble arm Y location on field
        returnData[2] = theta;//- Math.PI/2.0; //servo arm angle on field

        return returnData;//x,y,theta, width, length,
    }

    /**
     * defineAccessory determines whether each of the accessories is ON
     * method uses the locations of the accessories on teh robot for setting the points
     * @param robot is the set of robot Field Location points
     * @param accList is the robot accessory list object with integer arrays for collector, conveyor, shooter
     * @param indexAcc is the index to select for this evaluation {0 = collector, 1 = conveyor, 2 = shooter}
     * @return
     */
    private double[] defineAccessory(RobotLocation robot, AccessoryList accList, int indexAcc){

        double[] returnData = new double[5];//x,y,theta,width,height
        double deltaX = 0.0;
        double deltaY = 0.0;
        double theta = robot.theta;
        // COORDINATES APPEAR TO BE X forward and Y positive out right
        switch(indexAcc){
            case 0://collector
                deltaX = -6.0;//distance to collector center from robot center (FWD/Back)
                deltaY = 0.0;//distance to collector center from robot center (right/left)
                returnData[0] = robot.x + deltaX*Math.cos(theta) - deltaY*Math.sin(theta);//collector X location on field
                returnData[1] = robot.y + deltaX*Math.sin(theta) + deltaY*Math.cos(theta);//collector Y location on field
                returnData[3] = 2.0; //height (X)
                returnData[4] = 8.0; //width (Y)

                break;
            case 1://conveyor
                deltaX = -2.0;//distance to conveyor center from robot center (FWD/Back)
                deltaY = -6.0/4.0;//distance to conveyor center from robot center (right/left)
                returnData[0] = robot.x + deltaX*Math.cos(theta) - deltaY*Math.sin(theta);//conveyor X location on field
                returnData[1] = robot.y + deltaX*Math.sin(theta) + deltaY*Math.cos(theta);//conveyorY location on field
                returnData[3]  = 8.0; //height (X)
                returnData[4]  = 6; //width (Y)

                break;
            case 2://shooter
                deltaX = 9.0;//distance to shooter center from robot center (FWD/Back)
                deltaY = 0.75;//distance to shooter center from robot center (right/left)
                returnData[0] = robot.x + deltaX*Math.cos(theta) - deltaY*Math.sin(theta);//shooter X location on field
                returnData[1] = robot.y + deltaX*Math.sin(theta) + deltaY*Math.cos(theta);//shooterY location on field
                returnData[3]  = 4.0; //height (X)
                returnData[4]  = 12.0; //width (Y)

                break;
        }

        returnData[2] = robot.theta;
        return returnData;//x,y,theta,width,height
    }



    /**
     * drawAccessories draws the accessories attached to the robot on the screen
     * @param gc is the GraphicsContext to draw to
     * @param robot is the robot location for this loop through the code
     * @param accList is the list of accessory (collector, conveyor, shooter, wobble goal arm) data
     */
    private void drawAccessories(GraphicsContext gc,RobotLocation robot, AccessoryList accList, colorType color){
        double[] robotAccData = defineAccessory(robot, accList, 0);//COLLECTOR
        String imageFile;
        if (accList.collectorOn[counter] == 0) {
                imageFile = "collectorOff.png";
        } else {
                imageFile = "collectorOn.png";
        }
        DefinePoint robotAcc = DrawObjects.drawImage(gc, new RobotLocation(robotAccData[0], robotAccData[1], robotAccData[2]),
                robotAccData[3], robotAccData[4], imageFile);
        robotAccData = defineAccessory(robot, accList, 1);//CONVEYOR
        if (accList.conveyorOn[counter] == 0) {
            if(color == colorType.BLUE){
                imageFile = "conveyorOffBLUE.png";
            }
            else{
                imageFile = "conveyorOffRED.png";
            }
        } else {
            if(color == colorType.BLUE){
                imageFile = "conveyorOnBLUE.png";
            }
            else{
                imageFile = "conveyorOnRED.png";
            }
        }
        robotAcc = DrawObjects.drawImage(gc, new RobotLocation(robotAccData[0],robotAccData[1],robotAccData[2]),
                    robotAccData[3],robotAccData[4],imageFile);

        robotAccData = defineAccessory(robot, accList, 2);//SHOOTER
        if (accList.shooterOn[counter] == 0) {
            imageFile = "shooterOff.png";
        } else {
            imageFile = "shooterOn.png";
        }
        robotAcc = DrawObjects.drawImage(gc, new RobotLocation(robotAccData[0],robotAccData[1],robotAccData[2]),
                robotAccData[3],robotAccData[4],imageFile);
        robotAccData = defineWGA(robot,accList);
        robotAcc = DrawObjects.drawImage(gc, new RobotLocation(robotAccData[0],robotAccData[1],robotAccData[2]),
                robotAccData[3],robotAccData[4],"wobbleArm.png");
    }


    /** setDisplay(user userName, int screen, Stage stage)
     * This will set the graphics window location on the selected monitor
     * Sets the size of the stage for the selected monitor
     * @param userName enum select from [KARL, CALEB, WILL}
     * @param screen int for selected screen either 0, 1, 2 for screen 1, 2, 3
     * @param stage primary stage used to display info
     */
    public static void setDisplay(user userName, int screen, Stage stage){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if(screen >= 0 && screen < gd.length){
            stage.setX(gd[screen].getDefaultConfiguration().getBounds().x +
                    gd[screen].getDefaultConfiguration().getBounds().getWidth()/2 - stageWidth/2);
            stage.setY(gd[screen].getDefaultConfiguration().getBounds().y +
                    gd[screen].getDefaultConfiguration().getBounds().getHeight()/2 - stageHeight/2);
        } else if( gd.length > 0 ) {
            stage.setX(gd[0].getDefaultConfiguration().getBounds().getWidth()/2 - stageWidth/2);
            stage.setY(gd[0].getDefaultConfiguration().getBounds().getHeight()/2 - stageHeight/2);
        } else {
            throw new RuntimeException( "No Screens Found" );
        }
        if(screen == 0){
            switch(userName){
                case KARL:
                    stageWidth = 730;
                    stageHeight = 730;
                    break;
                case CALEB:
                    stageWidth = 760;
                    stageHeight = 680;
                    break;
                case WILL:
                    stageWidth = 780;
                    stageHeight = 780;
                    break;
            }
        }

        else {
            switch(userName){
                case KARL:
                    stageWidth = 1080;
                    stageHeight = 1080;
                    break;
                case CALEB:
                    stageWidth = 1000;
                    stageHeight = 1000;
                    break;
                case WILL:
                    stageWidth = 960;
                    stageHeight = 960;
                    break;
            }
        }

    }


}