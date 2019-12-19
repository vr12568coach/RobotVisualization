package RobotVizCode;

import java.io.*;
import java.util.ArrayList;


public class ReadAccessories {
    static boolean writeOutput = false;//used for troubleshooting and printing out data

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
                outputData.jackDir[cnt] = Integer.parseInt(data[0]);
                outputData.gripperWidth[cnt] = Double.parseDouble(data[1]);
                cnt+=1;
            }
            if (writeOutput){
                System.out.print("Writing Accessory Data\n");

                for (int i = 0; i < cnt; i++) {
                    System.out.print(String.format("%d, %d, %.1f\n", i, outputData.jackDir[i], outputData.gripperWidth[i]));
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
