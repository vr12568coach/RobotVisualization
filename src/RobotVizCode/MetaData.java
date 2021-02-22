package RobotVizCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
/* NOTE: MetaData is a class of simple data types to define parameters used in an OfflineOpModesRunFile case
*       Parameters are written to a file in the Android Studio project and then read in here in RobotVisualization*/
public class MetaData {
    int numberPoints;
    int pointsPerSecond;


    public MetaData(){

    }
    public MetaData(int num, int pointsSec){
        this.numberPoints = num;
        this.pointsPerSecond = pointsSec;

    }

    public void readDataFile(String fileName) {
        //Create buffered text reader and string to fill later
        BufferedReader txtReader;
        String s;

        try {

            // Assign txtReader based on input filename within the "Try" block in case there is an IO exception (ie no file)
            txtReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/" + fileName));
            while ((s = txtReader.readLine()) != null) {//Read lines while data exists -- should just be 1 line
                String[] data = s.split("\t");//Split the string into String array based on tabs
                //Assign values based on order in file to the current class instance
                numberPoints = Integer.parseInt(data[0]);
                pointsPerSecond = Integer.parseInt(data[1]);

            }
            //Close txtReader
            txtReader.close();

        }catch(Exception e){
            //                    // if an I/O error occurs
            e.printStackTrace();
        }
    }
}
