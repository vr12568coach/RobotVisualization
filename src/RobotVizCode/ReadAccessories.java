package RobotVizCode;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class ReadAccessories {
    public  void ReadAccessories(){

    }
    public static AccessoryList readAcc(String fileName) {
        FileInputStream fileInStream = null;
        DataInputStream dataInStream = null;
        AccessoryList outputData = new AccessoryList();
        int cnt = 0;
        try {
            fileInStream = new FileInputStream(System.getProperty("user.dir") + "/" + fileName);
            dataInStream = new DataInputStream(fileInStream);
            //            String header = dataInStream.readChar();
            while (dataInStream.available() > 0) {

                int readJack = dataInStream.readInt();
                double readGrip = dataInStream.readDouble();

                outputData.jackDir[cnt] = readJack;
                outputData.gripperWidth[cnt] = readGrip;
                cnt+=1;
            }
            fileInStream.close();
            dataInStream.close();

        }
        catch (IOException e) {
            e.printStackTrace();

        }
        return outputData;
    }
}
