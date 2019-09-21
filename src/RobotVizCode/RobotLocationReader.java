package RobotVizCode;

import java.util.ArrayList;

public class RobotLocationReader {
    private ReadRobotData roboRead;
    public static void main(String args[]) {
        ArrayList<RobotLocation> displayPoints = new ArrayList<>() ;//all the points to display
        ReadRobotData roboRead = new ReadRobotData();
        displayPoints.addAll(roboRead.readData("Robot1OnField.dat"));
    }
}
