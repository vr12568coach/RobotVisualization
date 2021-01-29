# RobotVisualization
IntelliJ Java project for plotting robot on FTC Skystone field

9/21/2019 -- UPDATED TO MAKE THIS THE OFFICIAL GitHub REPOSITORY
Copied from OfflineCode folder on local machine

9/21/2019 - Updated branch
Created BeforeTournament branch to capture any updates

1/29/21 - Updated Instructions for the RobotOnFieldVizMain and File_IO

#FILE_IO
Run the main static method to copy Robot application code to the offline directory
See more instructions under TestOpModesOffline Project

Notes:
    1. File paths need to be updated for your machine
    When updating files paths:
        a) comment the current path so it remains for other users
        b) create your own file path and label it with a comment so you remember
    2. Lines for file path updates
    
    line 9: static String mainPathString = "/Users/karl/LocalDocuments/FTC/UltimateGoal/Code/UltimateGoalOffline/TeamCode/src/main/java/UltimateGoal_RobotTeam";
     
    line 13: static File directoryPath = new File("/Users/karl/LocalDocuments/FTC/UltimateGoal/Code/UltimateGoalFTCApp/TeamCode/src/main/java/UltimateGoal_RobotTeam");
   
   3. Method checkOfflineExceptions() is where all onRobot to Offline code changes are made
   This method reads in the code and checks for an exception listed.
   When an exception is found it used the switch block to swap out the Robot or commented code for the active offline code
  
  #RobotOnFieldVizMain
  Run the main static method to display JavaFX window with robot graphics
  There are a few options the user can change when running this file
  
  a. User Display
    line 701 for setDisplay() method definition
    Inside this method you can set the size of your display window
    Change the variables under your name
        stageWidth = 780;
        stageHeight = 780;
    Monitor = 0 will be for a single display (laptop)
    The other else{} statement is for secondary monitors
  b. Monitor selection
    line 119 setDisplay(user.KARL, 0,primaryStage);//screen = 0 for single screen or 1 for dual displays
    Here you can select your display settings and monitor number
    Change the user enum to your name
    Set the monitor value
