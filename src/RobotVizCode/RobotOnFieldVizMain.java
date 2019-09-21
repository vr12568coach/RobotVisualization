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

    RobotVizCode.ReadRobotData roboRead = new RobotVizCode.ReadRobotData();

    //////////////////////ALL LAYOUT PARAMETERS////////////////////////
    private final int MAIN_GRID_HORIZONTAL_GAP = 100;//horizontal spacing of the main grid
    private final int MAIN_GRID_VERTICAL_GAP = 100;//vertical spacing of the main grid
    ///////////////////////////////////////////////////////////////////

    public static int ARRAY_SIZE = 300;
    public static ArrayList<RobotVizCode.RobotLocation> robot1Points = new ArrayList<>() ;//all the points to display
    public static ArrayList<RobotVizCode.RobotLocation> robot2Points = new ArrayList<>() ;//all the points to display

    public static ArrayList<RobotVizCode.DefineLine> displayLines = new ArrayList<>();//all the lines to display
    public static int counter = 0;




    public static Semaphore drawSemaphore = new Semaphore(1);


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
        Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/SkyStoneFieldStones.png"));
//        Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/field dark.png"));

        fieldBackgroundImageView = new ImageView();

        fieldBackgroundImageView.setImage(image);//set the image

        //add the background image
        rootGroup.getChildren().add(fieldBackgroundImageView);
        //////////////////////////////////////////////////////////////////////////////////////////////


        //Setup the canvas//
        fieldCanvas = new Canvas(primaryStage.getWidth(),primaryStage.getHeight());
        //the GraphicsContext is what we use to draw on the fieldCanvas
        GraphicsContext gc = fieldCanvas.getGraphicsContext2D();
        rootGroup.getChildren().add(fieldCanvas);//add the canvas
        ////////////////////

        /**
         * We will use a vbox and set it's width to create a spacer in the window
         * USE THIS TO CHANGE THE SPACING
         */
        VBox debuggingHSpacer = new VBox();
        mainHBox.getChildren().add(debuggingHSpacer);

        Group logGroup = new Group();

//        Image logImage = new Image(new FileInputStream(System.getProperty("user.dir") + "/log background.png"));
        Image logImage = new Image(new FileInputStream(System.getProperty("user.dir") + "/RobotLog3.png"));

        ImageView logImageView = new ImageView();
        logImageView.setImage(logImage);//set the image

        logImageView.setFitHeight(logImage.getHeight()/1.6);//was 2.5
        logImageView.setFitWidth(logImage.getWidth()/1.6);//was 2.5

        logGroup.setTranslateY(100);//was 10
        //add the background image
        logGroup.getChildren().add(logImageView);


        Label robotCoordsLabel = new Label();
        robotCoordsLabel.setFont(new Font("Arial",14));
        robotCoordsLabel.textFillProperty().setValue(new Color(0,0.2,1.0,1));
        robotCoordsLabel.setPrefWidth(logImageView.getFitWidth()-25);//was setPrefWidth(logImageView.getFitWidth()-25)

        robotCoordsLabel.setLayoutX(16);
        robotCoordsLabel.setLayoutY(logImageView.getFitHeight()/5);//was 4.7
        robotCoordsLabel.setWrapText(true);

        logGroup.getChildren().add(robotCoordsLabel);

        mainHBox.getChildren().add(logGroup);//add the log group


        //now we can add the mainHBox to the root group
        rootGroup.getChildren().add(mainHBox);
        scene.setFill(Color.BLUE);//we'll be black (9)now blue)
        primaryStage.setScene(scene);//set the primary stage's scene
        primaryStage.setWidth(800);
        primaryStage.setHeight(800);
        primaryStage.setMaximized(false);

        //show the primaryStage
        primaryStage.show();


        //CREATE A NEW ANIMATION TIMER THAT WILL CALL THE DRAWING OF THE SCREEN
        new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                try {
                    if(counter == 0) {
                        robot1Points.clear();
                        robot1Points.addAll(roboRead.readData("Robot1OnField.dat"));
                        robot2Points.clear();
                        robot2Points.addAll(roboRead.readData("Robot2OnField.dat"));
                    }
                    //acquire the drawing semaphore
                    drawSemaphore.acquire();

                    //set the width and height
                    FieldToScreen.setDimensionsPixels(scene.getWidth(),
                            scene.getHeight());
                    fieldCanvas.setWidth(FieldToScreen.getFieldSizePixels());
                    fieldCanvas.setHeight(FieldToScreen.getFieldSizePixels());

                    fieldBackgroundImageView.setFitWidth(FieldToScreen.getFieldSizePixels());
                    fieldBackgroundImageView.setFitHeight(FieldToScreen.getFieldSizePixels());

                    debuggingHSpacer.setPrefWidth(scene.getWidth() * 0.01);

                    robotCoordsLabel.setMaxWidth(scene.getWidth() * 0.5);

                    drawScreen(gc);
                    robotCoordsLabel.setText("COORDINATES:"+
                            String.format("\n\nX1: %.2f  |  X2: %.2f", robot1Points.get(counter).x,robot2Points.get(counter).x)+
                            String.format("\nY1: %.2f  |  Y2: %.2f", robot1Points.get(counter).y,robot2Points.get(counter).y) +
                            String.format("\nAng1:%.1f°  |  Ang2:%.1f°",Math.toDegrees(robot1Points.get(counter).theta),Math.toDegrees(robot2Points.get(counter).theta)) +
                            String.format("\ncounter: %d",counter));
                    System.out.println(primaryStage.getWidth());
//                    System.out.println(String.format("counter value: %d",counter));
//                    gc.setLineWidth(10);
//                    buildPoints();


                } catch (InterruptedException e) {
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
//        DefinePoint center = drawRobot(gc);
//        followRobot(center.x,center.y);
        //Alternate robot draw option for multi-robots
//        DefinePoint robot1Center = DrawRobots.drawImage(gc, robot1Points.get(counter),"SimpleRobot1.png");
        DefinePoint robot1Center = DrawRobots.drawImage(gc, robot1Points.get(counter),"NewRobot1.png");

        followRobot(robot1Center.x,robot1Center.y);

        RobotLocation rb2 = new RobotLocation(60,36,180);
//        DefinePoint robot2Center = DrawRobots.drawImage(gc,robot2Points.get(counter),"robot.png");
        DefinePoint robot2Center = DrawRobots.drawImage(gc,robot2Points.get(counter),"NewRobot2.png");

        followRobot(robot2Center.x,robot2Center.y);
        //draw all the lines and points retrieved from the phone

//        drawDebugPoints(gc);
        double[] colors1 = {1, 0};
        double[] colors2 = {0, 0.25};
        DrawPoints.drawPoints(gc, counter, robot1Points,colors1);
        DrawPoints.drawPoints(gc, counter, robot2Points,colors2);
//        drawDebugLines(gc);


        counter+=1;
        if(counter > (ARRAY_SIZE - 1)) {
            counter = 0;
        }
        try{
            Thread.sleep(50);//(was 50)This can slow down the time between points and therefore the robot speed
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

//    public void gatherData(GraphicsContext gc){
//        double robotX = robot1Points.get(counter).x;
//        double robotY = robot1Points.get(counter).y;
//        double robotAngle = counter/ARRAY_SIZE * 45;
//    }

//    private void buildPoints(){
//
//        for(int i =0; i < ARRAY_SIZE; i ++){
//            robot1Points.add(i, new RobotLocation(counter*2, counter/2, counter/ARRAY_SIZE * 45));
//        }
//    }

//
//
//
//    private void drawDebugPoints(GraphicsContext gc) {
//
//
//        for(int i =0; i < counter; i ++){
//
//            DefinePoint displayLocation = TestScreen.convertToScreenPoint(
//                    new DefinePoint((12*12)/2+ robot1Points.get(i).x, (12*12)/2+ robot1Points.get(i).y));
//            double radius = 5;
////            gc.setStroke(new Color(i/displayPoints.size() ,0.1,0.5,0.8));
////            gc.strokeOval(displayLocation.x-radius,displayLocation.y-radius,2*radius,2*radius);
//            double colorValue = (double)i/(double) robot1Points.size();
//            gc.setFill(new Color(1.0 ,colorValue,0.0,0.8));
//            gc.fillOval(displayLocation.x-radius,displayLocation.y-radius,2*radius,2*radius);
////            System.out.println(String.format("writing oval number %d @ location (%.2f,%.2f), color = %.3f",i,displayLocation.x-radius,displayLocation.y-radius,colorValue));
//
//        }
//
//
////        for(int i = 0; i < MessageProcessing.pointLog.size(); i ++){
////            DefinePoint displayLocation = convertToScreenPoint(
////                    new DefinePoint(MessageProcessing.pointLog.get(i).x,
////                            MessageProcessing.pointLog.get(i).y));
////            double radius = 10;
//////            gc.setFill(new Color(0.0,0+ (double) i/MessageProcessing.pointLog.size(),0,0.9));
////            gc.setFill(new Color(0.0+ (double) i/ MessageProcessing.pointLog.size(),0.9,0.1,0.9));
////            gc.fillOval(displayLocation.x-radius,displayLocation.y-radius,2*radius,2*radius);
////
////        }
//
//
//    }
//    private void drawDebugLines(GraphicsContext gc) {
//        for(int i =0; i < displayLines.size(); i ++){
//            DefinePoint displayLocation1 = TestScreen.convertToScreenPoint(
//                    new DefinePoint(displayLines.get(i).x1, displayLines.get(i).y1));
//            DefinePoint displayLocation2 = TestScreen.convertToScreenPoint(
//                    new DefinePoint(displayLines.get(i).x2, displayLines.get(i).y2));
//
//
//            gc.setLineWidth(3);
//            gc.setStroke(new Color(0.0,1.0,1.0,0.6));
//
//
//            gc.strokeLine(displayLocation1.x,displayLocation1.y,displayLocation2.x,displayLocation2.y);
//        }
//    }
//
//

    /**
     * This will move the background image and everything else to follow the robot
     */
    private void followRobot(double robotX, double robotY){
        //set the center point to the robot
//        TestScreen.setCenterPoint(robotX, robotY);

//        Set center to be 50% of width & height
        FieldToScreen.setCenterPoint(FieldToScreen.getInchesPerPixel()* FieldToScreen.widthScreen/2.0,
                FieldToScreen.getInchesPerPixel()* FieldToScreen.heightScreen/2.0);

        //        Set center to be (0,0))
//        TestScreen.setCenterPoint(0,0);

        //get where the origin of the field is in pixels
        DefinePoint originInPixels = FieldToScreen.convertToScreenPoint(new DefinePoint(0, FieldToScreen.ACTUAL_FIELD_SIZE));
        //get origin as (0,0)
//        DefinePoint originInPixels = convertToScreenPoint(new DefinePoint(0, 0));
        fieldBackgroundImageView.setX(originInPixels.x);
        fieldBackgroundImageView.setY(originInPixels.y);
    }




//    //the last position of the robot
//    double lastRobotX = 0;
//    double lastRobotY = 0;
//    double lastRobotAngle = 0;

    /**
     * Draws the robot
     * @param gc the graphics context
     */
//    private DefinePoint drawRobot(GraphicsContext gc) {
//        //robot radius is half the diagonal length
//        double robotRadius = Math.sqrt(2) * 18.0/ 2.0;//update units to inches for 18" square
//
////        double robotX = MessageProcessing.getInterpolatedRobotX();
////        double robotY = MessageProcessing.getInterpolatedRobotY();
////        double robotAngle = MessageProcessing.getInterpolatedRobotAngle();
//        double robotX = (12*12/2)+ robot1Points.get(counter).x;//set robot (0,0) form data at field center
//        double robotY = (12*12/2)+ robot1Points.get(counter).y;//set robot (0,0) form data at field center
//        double robotAngle = Math.toRadians(90) + robot1Points.get(counter).theta;//Need to add 90 degrees = maybe be for reference EAST vs SOUTH
////        followRobot(robotX,robotY);
//        DefinePoint robotCenter = new DefinePoint(robotX,robotY);
//
//
//        double topLeftX = robotX + (robotRadius * (Math.cos(robotAngle+ Math.toRadians(45))));
//        double topLeftY = robotY + (robotRadius * (Math.sin(robotAngle+ Math.toRadians(45))));
//        double topRightX = robotX + (robotRadius * (Math.cos(robotAngle- Math.toRadians(45))));
//        double topRightY = robotY + (robotRadius * (Math.sin(robotAngle- Math.toRadians(45))));
//        double bottomLeftX = robotX + (robotRadius * (Math.cos(robotAngle+ Math.toRadians(135))));
//        double bottomLeftY = robotY + (robotRadius * (Math.sin(robotAngle+ Math.toRadians(135))));
//        double bottomRightX = robotX + (robotRadius * (Math.cos(robotAngle- Math.toRadians(135))));
//        double bottomRightY = robotY + (robotRadius * (Math.sin(robotAngle- Math.toRadians(135))));
//
//        Color c = Color.color(1.0,1.0,0.0);
//        //draw the points
////        drawLineField(gc,topLeftX, topLeftY, topRightX, topRightY,c);
////        drawLineField(gc,topRightX, topRightY, bottomRightX, bottomRightY,c);
////        drawLineField(gc,bottomRightX, bottomRightY, bottomLeftX, bottomLeftY,c);
////        drawLineField(gc,bottomLeftX, bottomLeftY, topLeftX, topLeftY,c);
////
//
//        try {
//            DefinePoint bottomLeft = TestScreen.convertToScreenPoint(new DefinePoint(topLeftX,topLeftY));
//            double width = 18/ TestScreen.getInchesPerPixel();//calculate the width of the image in pixels (in inches)
//
//            gc.save();//save the gc
//            gc.transform(new Affine(new Rotate(Math.toDegrees(-robotAngle)+90, bottomLeft.x, bottomLeft.y)));//Need to add 90 degrees = maybe be for reference EAST vs SOUTH
////            Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/robot.png"));
//            Image image = new Image(new FileInputStream(System.getProperty("user.dir") + "/SimpleRobot1.png"));
//            gc.drawImage(image,bottomLeft.x, bottomLeft.y,width,width);
//
//
//            gc.restore();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return robotCenter;
//    }
//
//    public void drawLineField(GraphicsContext gc,double x1, double y1, double x2, double y2,Color color){
//        DefinePoint first = TestScreen.convertToScreenPoint(new DefinePoint(x1,y1));
//        DefinePoint second = TestScreen.convertToScreenPoint(new DefinePoint(x2,y2));
//        gc.setStroke(color);
//        gc.strokeLine(first.x,first.y,second.x,second.y);
//        gc.setStroke(Color.BLACK);
//    }
//

}