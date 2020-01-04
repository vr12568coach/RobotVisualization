package RobotVizCode;

import java.io.*;
import java.util.ArrayList;

public class ReadRobotData {
    boolean writeOutput = false;//used for troubleshooting and printing out data

    public ReadRobotData() {
// empty constructor
    }

    public ReadRobotData(boolean write) {
//        constructor to select writing output data
        this.writeOutput = write;
    }

    public ArrayList<RobotLocation> readData(String fileName) {

        BufferedReader txtReader = null;
        String s = null;
        double readX = 0;
        double readY = 0;
        double angleRad = 0;
        boolean readHeader = false; //false = skip initial row as the header, true = read first line
        int cnt = 0;
        ArrayList<RobotLocation> outputData = new ArrayList<>();
        try {

// Update to person readable text file for reading in data
            txtReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/" + fileName));
            while ((s = txtReader.readLine()) != null) {
                String[] data = s.split("\t");

                if (readHeader) {
                    readX = Double.parseDouble(data[0]);
                    readY = Double.parseDouble(data[1]);
                    angleRad = Double.parseDouble(data[2]);
                }
                readHeader = true;
                outputData.add(new RobotLocation(readX, readY, angleRad));

                cnt += 1;

            }
            if (writeOutput){
                System.out.print("Writing Robot or Field Item Data\n");

                for (int i = 0; i < cnt; i++) {
                    System.out.print(String.format("%d, %.1f, %.1f, %.1f\n", i, outputData.get(i).x, outputData.get(i).y, outputData.get(i).theta));
                }
            }
            txtReader.close();

        }catch(Exception e){
    //                    // if an I/O error occurs
            e.printStackTrace();
        }
        return outputData;
    }
}
