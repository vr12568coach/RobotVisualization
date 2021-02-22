package RobotVizCode;

import java.io.*;
import java.util.ArrayList;


/* This file doesn't need to be a separate class, could be methods in "Main" file */
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
                    outputData.add(new RobotLocation(readX, readY, angleRad));

                }
                readHeader = true;

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
    public ArrayList<DefineLine> readLines(String fileName) {

        BufferedReader txtReader = null;
        String s = null;
        double x1 = 0;
        double y1 = 0;
        double x2 = 0;
        double y2 = 0;
        boolean readHeader = false; //false = skip initial row as the header, true = read first line
        int cnt = 0;
        ArrayList<DefineLine> outputData = new ArrayList<>();
        try {

// Update to person readable text file for reading in data
            txtReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/" + fileName));
            while ((s = txtReader.readLine()) != null) {
                String[] data = s.split("\t");

                if (readHeader) {
                    x1 = Double.parseDouble(data[0]);
                    y1 = Double.parseDouble(data[1]);
                    x2 = Double.parseDouble(data[2]);
                    y2 = Double.parseDouble(data[3]);
                    outputData.add(new DefineLine( x1,  y1,  x2,  y2) );
                }
                readHeader = true;

                cnt += 1;

            }
            if (writeOutput){
                System.out.print("Writing Robot or Field Item Data\n");

                for (int i = 0; i < cnt; i++) {
                    System.out.print(String.format("%d, %.1f, %.1f, %.1f\n", i, outputData.get(i).x1, outputData.get(i).y1, outputData.get(i).x2, outputData.get(i).y2));
                }
            }
            txtReader.close();

        }catch(Exception e){
            //                    // if an I/O error occurs
            e.printStackTrace();
        }
        return outputData;
    }
    public String readRingSet(String fileName) {

        BufferedReader txtReader;
        String s = "none";//default
        try {

           txtReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/" + fileName));
            s = txtReader.readLine();
            System.out.print(String.format(" Ring Set Condition: %s\n", s));

            txtReader.close();

        }catch(Exception e){
            //                    // if an I/O error occurs
            e.printStackTrace();
        }
        return s;
    }
    public ArrayList<AccessoryData> readAccData(String fileName) {

        BufferedReader txtReader = null;
        String s = null;
        //col = Collector ON/OFF,  con = Conveyor ON/OFF, shoot = Shooter On/OFF,  wgaAngle = WobbleGoalArmAngle (radians)
        int col = 0;
        int con = 0;
        int shoot = 0;
        double wgaAngle=0.0;
        boolean readHeader = false; //false = skip initial row as the header, true = read first line
        int cnt = 0;
        ArrayList<AccessoryData> outputData = new ArrayList<>();
        try {

// Update to person readable text file for reading in data
            txtReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/" + fileName));
            while ((s = txtReader.readLine()) != null) {
                String[] data = s.split("\t");

                if (readHeader) {
                    col = Integer.parseInt(data[0]);
                    con = Integer.parseInt(data[1]);
                    shoot = Integer.parseInt(data[2]);
                    wgaAngle = Double.parseDouble(data[3]);
                    outputData.add(new AccessoryData(col, con, shoot,wgaAngle));

                }
                readHeader = true;

                cnt += 1;

            }
            if (writeOutput){
                System.out.print("Writing Accessory Data\n");

                for (int i = 0; i < cnt; i++) {
                    System.out.print(String.format("Count: %d, Col: %d, Con: %d, Shoot: %d, WGA: %.1f\n", i,
                            outputData.get(i).collectorOn, outputData.get(i).conveyorOn, outputData.get(i).shooterOn,outputData.get(i).wobbleArmAngleRad));
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
