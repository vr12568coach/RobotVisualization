package RobotVizCode;


import java.util.ArrayList;

public class RobotLocationReader {
    private ReadRobotData roboRead;
    public static void main(String args[]) {
        ArrayList<RobotLocation> displayPoints = new ArrayList<>() ;//all the points to display
        ReadRobotData roboRead = new ReadRobotData(true);
        displayPoints.addAll(roboRead.readData("Robot1OnField.txt"));

        AccessoryList accessories = new AccessoryList() ;//all the points to display
        ReadAccessories roboAcc = new ReadAccessories(true);
        accessories = roboAcc.readAcc("Robot1Accessories.txt");
    }
}
