package RobotVizCode;

import java.io.*;
import java.util.ArrayList;

public class ReadRobotData {
    public void ReadRobotData() {

    }

    public ArrayList<RobotLocation> readData(String fileName) {
        FileInputStream fileInStream = null;
        FileOutputStream fileOutStream = null;
        InputStream inStream = null;
        DataInputStream dataInStream = null;
        DataOutputStream dataOutStream = null;
//        System.out.println("Read from File");
//        System.out.println("X, Y, AngleRad");
        ArrayList<RobotLocation> outputData = new ArrayList<>();
        try{
            fileInStream = new FileInputStream(System.getProperty("user.dir")+"/"+fileName);
            dataInStream = new DataInputStream(fileInStream);
//            String header = dataInStream.readChar();
            int cnt = 0;
            while (dataInStream.available() > 0) {
    //
    //                        // read character
                double readX = dataInStream.readDouble();
                double readY = dataInStream.readDouble();
                double angleRad = dataInStream.readDouble();
                outputData.add(new RobotLocation(readX,readY,angleRad));

    //                double dAngle = dataInStream.readDouble();
    //                double dSin = dataInStream.readDouble();
    //                double dCos = dataInStream.readDouble();

    //                        // print
//                System.out.println(String.format("%.3f, %.3f, %.3f", readX, readY, angleRad));
//                System.out.println(String.format("%.3f, %.3f, %.3f", outputData.get(cnt).x, outputData.get(cnt).y, outputData.get(cnt).theta));

                cnt+=1;
            }
            fileInStream.close();
            dataInStream.close();

    //                sizeInput = inStream.available();
    //
    //                for(int i = 0; i < sizeInput; i++) {
    //                    System.out.print((char)inStream.read() + "  ");
    //                }
    //                inStream.close();
        }
        catch(Exception e){
    //                    // if an I/O error occurs
            e.printStackTrace();
        }
        return outputData;
    }
}
