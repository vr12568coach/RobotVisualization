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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
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

    public static ArrayList<RobotVizCode.RobotLocation> BlueFoundationPoints = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> BlueSkyStone1Points = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> BlueSkyStone2Points = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> RedSkyStone1Points = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> RedSkyStone2Points = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> RedFoundationPoints = new ArrayList<>();//all the points to display

    public static AccessoryList robot1Acc = new AccessoryList();
    public static AccessoryList robot2Acc = new AccessoryList();
    public static AccessoryList robot3Acc = new AccessoryList();
    public static AccessoryList robot4Acc = new AccessoryList();

    public static ArrayList<RobotVizCode.RobotLocation> robot1Gripper = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot2Gripper = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot3Gripper = new ArrayList<>();//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot4Gripper = new ArrayList<>();//all the points to display

    public static ArrayList<RobotVizCode.DefineLine> displayLines = new ArrayList<>();//all the lines to display
    public static int counter = 0;

    static double  standardWidthPixels = 800;//standard width for scaling of window 800
    static double  standardHeightPixels = 800;//standard height for scaling of window 800 or 816 or 823

    public double robotWidth = 14;//inches across front to back w/o gripper since robots start front to back in X (Width)
    public double robotHeight = 16.25;//inches across wheels


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
        //WINDOW STUFF//
        primaryStage.setTitle("Test Robot Plotter");
        double stageWidth = 960;//User input for the desired size
        double stageHeight = 983;//User input for the desired size (needs to be 1.02 larger to keep the scene square)


        ////////////////
//        if((counter == 0 & displayPoints.size() < 1)) {
//            displayPoints.add(0, new RobotLocation(0, 0, 0));
//        }
//        System.out.println(String.format("size of display points: %d, counter value: %d",displayPoints.size(),counter));

        //this is the group that holds everything
        rootGroup = new Group();
        //create a new scene, pass the rootGroup
        Scene scene = new Scene(rootGroup);
        //Now we can setup the HBox
        mainHBox = new HBox();
        //bind the main h box width to the primary stage width so that changes with it
        mainHBox.prefWidthProperty().bind(primaryStage.widthProperty());
        mainHBox.prefHeightProperty().bind(primaryStage.heightProperty());


        ///////////////////////////////////Setup the background image/////////////////////////////////
        Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/FieldNoFoundations4.png"));

        fieldBackgroundImageView = new ImageView();
        fieldBackgroundImageView.setImage(image);//set the image

        //add the background image
        rootGroup.getChildren().add(fieldBackgroundImageView);
        //////////////////////////////////////////////////////////////////////////////////////////////


        //Setup the canvas//
        fieldCanvas = new Canvas(stageWidth,stageHeight);// was primaryStage.getWidth(),primaryStage.getHeight()
        //the GraphicsContext is what we use to draw on the fieldCanvas
        GraphicsContext gc = fieldCanvas.getGraphicsContext2D();
        rootGroup.getChildren().add(fieldCanvas);//add the canvas
        ////////////////////

        /**
         * We will use a vbox and set it's width to create a spacer in the window
         * USE THIS TO CHANGE THE SPACING
         */
//        VBox debuggingHSpacer = new VBox();
//        mainHBox.getChildren().add(debuggingHSpacer);


        Group logGroup = new Group();
        Image logImage = new Image(new FileInputStream(System.getProperty("user.dir") + "/RobotLog3.png"));

        ImageView logImageView = new ImageView();
        logImageView.setImage(logImage);//set the image

        logImageView.setFitHeight(270 * stageHeight / standardHeightPixels);//was logImage.getHeight()/1.25
        logImageView.setFitWidth(200 * stageWidth / standardWidthPixels);//was logImage.getWidth()/1.6

        logGroup.setTranslateY(10 * stageHeight/ standardHeightPixels);//was 10 or 100
        //Set so that as stage Width is changed the relative location of the text box is the same
        logGroup.setTranslateX(285 * stageWidth / standardWidthPixels);
        // use 0 for LH side, 285 for center, 550 for RH side
        //Set so that as stage Width is changed the relative location of the text box is the same

        //add the background image
        logGroup.getChildren().add(logImageView);

        Label robotCoordsLabel = new Label();
        robotCoordsLabel.setFont(new Font("Arial",12* stageHeight / standardHeightPixels));
        robotCoordsLabel.textFillProperty().setValue(new Color(0,0.2,1.0,1));
        robotCoordsLabel.setPrefWidth(logImageView.getFitWidth()-25);//was setPrefWidth(logImageView.getFitWidth()-25)

        robotCoordsLabel.setLayoutX(16* stageWidth / standardWidthPixels);
        robotCoordsLabel.setLayoutY(40 * stageHeight / standardHeightPixels);//was logImageView.getFitHeight()/5

        robotCoordsLabel.setWrapText(true);

        logGroup.getChildren().add(robotCoordsLabel);

        mainHBox.getChildren().add(logGroup);//add the log group


        //now we can add the mainHBox to the root group
        rootGroup.getChildren().add(mainHBox);
        scene.setFill(Color.BLUE);//background color is blue)
        primaryStage.setScene(scene);//set the primary stage's scene

        primaryStage.setWidth(stageWidth);
        primaryStage.setHeight(stageHeight);

        primaryStage.setMaximized(false);

        //show the primaryStage
        primaryStage.show();


        //CREATE A NEW ANIMATION TIMER THAT WILL CALL THE DRAWING OF THE SCREEN
        new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                try {
                    //Load data on initial pass
                    if(counter == 0) {
                        robot1Points.clear();
                        robot1Points.addAll(roboRead.readData("Robot1OnField.txt"));
                        robot1Acc = ReadAccessories.readAcc("Robot1Accessories.txt");
                        robot1Gripper.clear();
                        robot1Gripper.addAll(roboRead.readData("Robot1Gripper.txt"));

                        robot2Points.clear();
                        robot2Points.addAll(roboRead.readData("Robot2OnField.txt"));
                        robot2Acc = ReadAccessories.readAcc("Robot2Accessories.txt");
                        robot2Gripper.clear();
                        robot2Gripper.addAll(roboRead.readData("Robot2Gripper.txt"));

                        robot3Points.clear();
                        robot3Points.addAll(roboRead.readData("Robot3OnField.txt"));
                        robot3Acc = ReadAccessories.readAcc("Robot3Accessories.txt");
                        robot3Gripper.clear();
                        robot3Gripper.addAll(roboRead.readData("Robot3Gripper.txt"));

                        robot4Points.clear();
                        robot4Points.addAll(roboRead.readData("Robot4OnField.txt"));
                        robot4Acc = ReadAccessories.readAcc("Robot4Accessories.txt");
                        robot4Gripper.clear();
                        robot4Gripper.addAll(roboRead.readData("Robot4Gripper.txt"));

                        RedFoundationPoints.clear();
                        RedFoundationPoints.addAll(roboRead.readData("RedFoundation.txt"));
                        BlueFoundationPoints.clear();
                        BlueFoundationPoints.addAll(roboRead.readData("BlueFoundation.txt"));
                        BlueSkyStone1Points.clear();
                        BlueSkyStone1Points.addAll(roboRead.readData("BlueSkyStone1.txt"));
                        BlueSkyStone2Points.clear();
                        BlueSkyStone2Points.addAll(roboRead.readData("BlueSkyStone2.txt"));
                        RedSkyStone1Points.clear();
                        RedSkyStone1Points.addAll(roboRead.readData("RedSkyStone1.txt"));
                        RedSkyStone2Points.clear();
                        RedSkyStone2Points.addAll(roboRead.readData("RedSkyStone2.txt"));

                    }
                    //acquire the drawing semaphore
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
//                    fieldBackgroundImageView.setTranslateX(-3* scene.getWidth()/standardWidthPixels);
//                    fieldBackgroundImageView.setTranslateY(-4* scene.getHeight()/standardHeightPixels);
                    logImageView.setFitHeight(270 * scene.getHeight() / standardHeightPixels);//was logImage.getHeight()/1.25
                    logImageView.setFitWidth(200 * scene.getWidth()/ standardWidthPixels);//was logImage.getWidth()/1.6
                    robotCoordsLabel.setFont(new Font("Arial",12* scene.getHeight() / standardHeightPixels));
                    logGroup.setTranslateY(10 * scene.getHeight() / standardHeightPixels);//was 10 or 100
                    //Set so that as stage Width is changed the relative location of the text box is the same
                    logGroup.setTranslateX(285 * scene.getWidth() / standardWidthPixels);// use 0 for LH side, 285 for center, 550 for RH side
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
                            String.format("\ncounter: %d",counter));
//************ UPDATES FOR WINDOW SIZING OUTPUT ************************************

                    System.out.println(String.format("Stage Input W: %.1f, H: %.1f & Current W: %.1f, H: %.1f,",stageWidth,stageHeight,primaryStage.getWidth(),primaryStage.getHeight()));
                    System.out.println(String.format(" > Current Scene Width: %.1f, Height: %.1f",scene.getWidth(),scene.getHeight()));
                    System.out.println(String.format(" > fieldCanvas Width: %.1f, Height: %.1f",fieldCanvas.getWidth(),fieldCanvas.getHeight()));
                    System.out.println(String.format(" > fieldBackgroundImageView Width: %.1f, Height: %.1f",fieldBackgroundImageView.getFitWidth(),fieldBackgroundImageView.getFitHeight()));
//************ UPDATES FOR WINDOW SIZING OUTPUT ************************************

                    //                    System.out.println(String.format("counter value: %d",counter));
//                    gc.setLineWidth(10);
//                    buildPoints();


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
        gc.clearRect(0,0, FieldToScreen.widthScreen, FieldToScreen.heightScreen);
//        gc.fillRect(0,0,Screen.widthScreen,Screen.heightScreen);

        //then draw the robot
        //Alternate robot draw option for multi-robots

        DefinePoint robot1Center = DrawObjects.drawImage(gc, robot1Points.get(counter), robotWidth, robotHeight,"NewRobot1.png");
        followRobot(robot1Center.x,robot1Center.y);

        if(robot1Acc.jackDir[counter]==1){
            DrawObjects.drawImage(gc, robot1Points.get(counter), 6, 6,"UpArrow.png");
        }
        else if(robot1Acc.jackDir[counter]==-1){
            DrawObjects.drawImage(gc, robot1Points.get(counter), 6, 6,"DownArrow.png");
        }
        double[] robot1ServoData = defineServo(robot1Points.get(counter),robot1Acc);
        DefinePoint robot1BlueServo = DrawObjects.drawImage(gc, new RobotLocation(robot1ServoData[0],robot1ServoData[1],robot1ServoData[2]),
                1.5,robot1ServoData[3],"ServoArm.png");
        DefinePoint robot1RedServo = DrawObjects.drawImage(gc, new RobotLocation(robot1ServoData[0],robot1ServoData[1],robot1ServoData[2]),
                1.5,robot1ServoData[3],"ServoArm.png");
        DefinePoint robot1G = DrawObjects.drawImage(gc, robot1Gripper.get(counter), 4, robot1Acc.gripperWidth[counter],"Gripper.png");

        DefinePoint robot2Center = DrawObjects.drawImage(gc,robot2Points.get(counter),robotWidth, robotHeight,"NewRobot2.png");
//        followRobot(robot2Center.x,robot2Center.y);

        if(robot2Acc.jackDir[counter]==1){
            DefinePoint robot1Jack = DrawObjects.drawImage(gc, robot2Points.get(counter), 6, 6,"UpArrow.png");
        }
        else if(robot2Acc.jackDir[counter]==-1){
            DefinePoint robot2Jack = DrawObjects.drawImage(gc, robot2Points.get(counter), 6, 6,"DownArrow.png");
        }
        double[] robot2ServoData = defineServo(robot2Points.get(counter),robot2Acc);
        DefinePoint robot2BlueServo = DrawObjects.drawImage(gc, new RobotLocation(robot2ServoData[0],robot2ServoData[1],robot2ServoData[2]),
                1.5,robot2ServoData[3],"ServoArm.png");

        DefinePoint robot2RedServo = DrawObjects.drawImage(gc, new RobotLocation(robot2ServoData[4],robot2ServoData[5],robot2ServoData[6]),
                1.5, robot2ServoData[7], "ServoArm.png");
        DefinePoint robot2G = DrawObjects.drawImage(gc, robot2Gripper.get(counter), 4, robot2Acc.gripperWidth[counter],"Gripper.png");




        DefinePoint BlueFoundCenter = DrawObjects.drawImage(gc, BlueFoundationPoints.get(counter),18.5, 34.5,"BlueFoundation.png");
//        followRobot(BlueFoundCenter.x,BlueFoundCenter.y);

        DefinePoint BlueStone1Center = DrawObjects.drawImage(gc, BlueSkyStone1Points.get(counter),4,8,"BlueStone.png");
//        followRobot(BlueStone1Center.x,BlueStone1Center.y);
        DefinePoint BlueStone2Center = DrawObjects.drawImage(gc, BlueSkyStone2Points.get(counter),4,8,"BlueStone.png");
//        followRobot(BlueStone2Center.x,BlueStone2Center.y);



        DefinePoint robot3Center = DrawObjects.drawImage(gc, robot3Points.get(counter), robotWidth, robotHeight,"RedRobot1.png");
//        followRobot(robot3Center.x,robot3Center.y);

        if(robot3Acc.jackDir[counter]==1){
            DefinePoint robot3Jack = DrawObjects.drawImage(gc, robot3Points.get(counter), 6, 6,"UpArrow.png");
        }
        else if(robot3Acc.jackDir[counter]==-1){
            DefinePoint robot3Jack = DrawObjects.drawImage(gc, robot3Points.get(counter), 6, 6,"DownArrow.png");
        }
        double[] robot3ServoData = defineServo(robot3Points.get(counter),robot3Acc);
        DefinePoint robot3BlueServo = DrawObjects.drawImage(gc, new RobotLocation(robot3ServoData[0],robot3ServoData[1],robot3ServoData[2]),
                1.5,robot3ServoData[3],"ServoArm.png");
        DefinePoint robot3RedServo = DrawObjects.drawImage(gc, new RobotLocation(robot3ServoData[4],robot3ServoData[5],robot3ServoData[6]),
                1.5, robot3ServoData[7], "ServoArm.png");
        DefinePoint robot3G = DrawObjects.drawImage(gc, robot3Gripper.get(counter), 4, robot3Acc.gripperWidth[counter],"Gripper.png");

        DefinePoint robot4Center = DrawObjects.drawImage(gc, robot4Points.get(counter), robotWidth, robotHeight,"RedRobot2.png");
//        followRobot(robot4Center.x,robot4Center.y);

        if(robot4Acc.jackDir[counter]==1){
            DefinePoint robot1Jack = DrawObjects.drawImage(gc, robot4Points.get(counter), 6, 6,"UpArrow.png");
        }
        else if(robot4Acc.jackDir[counter]==-1){
            DefinePoint robot4Jack = DrawObjects.drawImage(gc, robot4Points.get(counter), 6, 6,"DownArrow.png");
        }
        double[] robot4ServoData = defineServo(robot4Points.get(counter),robot4Acc);
        DefinePoint robot4BlueServo = DrawObjects.drawImage(gc, new RobotLocation(robot4ServoData[0],robot4ServoData[1],robot4ServoData[2]),
                1.5,robot4ServoData[3],"ServoArm.png");
        DefinePoint robot4RedServo = DrawObjects.drawImage(gc, new RobotLocation(robot4ServoData[4],robot4ServoData[5],robot4ServoData[6]),
                1.5, robot4ServoData[7], "ServoArm.png");
        DefinePoint robot4G = DrawObjects.drawImage(gc, robot4Gripper.get(counter), 4, robot4Acc.gripperWidth[counter],"Gripper.png");

        DefinePoint RedFoundCenter = DrawObjects.drawImage(gc, RedFoundationPoints.get(counter),18.5, 34.5,"RedFoundation.png");
//        followRobot(RedFoundCenter.x,RedFoundCenter.y);

        DefinePoint RedStone1Center = DrawObjects.drawImage(gc, RedSkyStone1Points.get(counter),4,8,"RedStone.png");
//        followRobot(RedStone1Center.x,RedStone1Center.y);
        DefinePoint RedStone2Center = DrawObjects.drawImage(gc, RedSkyStone2Points.get(counter),4,8,"RedStone.png");
//        followRobot(RedStone2Center.x,RedStone2Center.y);


        //draw all the lines and points retrieved from the phone

//        drawDebugPoints(gc);
        double[] colors1 = {0.3, 1.0};
        double[] colors2 = {0.4, 0.9};
        double[] colors3 = {1, 0};
        double[] colors4 = {0.9, 0.0};
//        robotCoordsLabel.textFillProperty().setValue(new Color(0.4,0.9,1,1));
        DrawPoints.drawPoints(gc, counter, robot1Points,colors1);
        DrawPoints.drawPoints(gc, counter, robot2Points,colors2);
        DrawPoints.drawPoints(gc, counter, robot3Points,colors3);
        DrawPoints.drawPoints(gc, counter, robot4Points,colors4);
//        drawDebugLines(gc);


        counter+=1;
        if(counter > (ARRAY_SIZE - 1)) {
            counter = 0;
        }
//        try{
//            Thread.sleep(50);//(was 50)This can slow down the time between points and therefore the robot speed
//            //WIth 4 robots this may not be necessary because of processing time for each step
//        }
//        catch (InterruptedException e){
//            e.printStackTrace();
//        }
    }


    /**
     * This will move the background image and everything else to follow the robot
     */
    private void followRobot(double robotX, double robotY){
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
    private double[] defineServo(RobotLocation robot, AccessoryList accList){
        double length = 9;
        double[] returnData = new double[8];

        double theta = robot.theta;
        //Blue Servo
        returnData[3] =length/0.5 * accList.blueStoneServo[counter]; //length of servo
        double deltaX = -2;//distance to servo center from robot center
        double deltaY = -6-returnData[4]; //distance to servo center from robot center, must include servo length
        returnData[0] = robot.x + deltaX*Math.cos(theta) - deltaY*Math.sin(theta);//servo arm X location on field
        returnData[1] = robot.y + deltaX*Math.sin(theta) + deltaY*Math.cos(theta);//servo arm Y location on field
        returnData[2] = theta; //servo arm angle on field
        //Red Servo
        deltaX = -2;
        returnData[7] =length/0.5 * accList.redStoneServo[counter]; //length of servo
        deltaY = returnData[7]; //distance to servo center from robot center, must include servo length
        returnData[4] = robot.x + deltaX*Math.cos(theta) - deltaY*Math.sin(theta);//servo arm X location on field
        returnData[5] = robot.y + deltaX*Math.sin(theta) + deltaY*Math.cos(theta);//servo arm Y location on field
        returnData[6] = theta; //servo arm angle on field

        return returnData;//blue x,y,theta, length, then red x,y,theta, length
    }



}