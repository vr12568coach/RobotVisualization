package RobotVizCode;

import java.io.*;
import java.util.ArrayList;


public class ReadAccessories {
    static boolean writeOutput = true;//used for troubleshooting and printing out data

    public ReadAccessories() {
// empty constructor
    }

    public ReadAccessories(boolean write) {
//        constructor to select writing output data
        this.writeOutput = write;
    }
    public static AccessoryList readAcc(String fileName) {
        BufferedReader txtReader = null;
        String s = null;
        AccessoryList outputData = new AccessoryList();
        int cnt = 0;
        try {
            txtReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/" + fileName));
            while ((s = txtReader.readLine()) != null) {
                String[] data = s.split("\t");
                outputData.collectorOn[cnt] = Integer.parseInt(data[0]);
                outputData.conveyorOn[cnt] = Integer.parseInt(data[1]);
                outputData.shooterOn[cnt] = Integer.parseInt(data[2]);
                outputData.wobbleArmAngleRad[cnt] = Double.parseDouble(data[3]);
                cnt+=1;
            }
            if (writeOutput){
                System.out.print("Writing Accessory Data\n");

                for (int i = 0; i < cnt; i++) {
                    System.out.print(String.format("%d, %d,%d,%d, %.1f\n", i, outputData.collectorOn[i],outputData.conveyorOn[i] ,outputData.shooterOn[i], outputData.wobbleArmAngleRad[i]));
                }
            }
            txtReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();

        }
        return outputData;
    }
}
