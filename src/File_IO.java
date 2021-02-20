import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class File_IO {

    static String mainPathString = "/Users/karl/LocalDocuments/FTC/UltimateGoal/Code/UltimateGoalOffline/TeamCode/src/main/java/UltimateGoal_RobotTeam";
//    static String mainPathString = "/Users/karl/LocalDocuments/FTC/UltimateGoal/Code/TestWrite";
//    static String mainPathString = "/Users/caleb/Documents/FTC/Android Studio/UltimateGoal Offline/TeamCode/src/main/java/UltimateGoal_RobotTeam";
//    static String mainPathString = "/Users/young/Desktop/Android Studio/UltimateGoalOfflineCode/TeamCode/src/main/java/UltimateGoal_RobotTeam";
    static Path mainPath = Paths.get(mainPathString);
    static String exclude = ".git";
    public Path filePath;
    static File directoryPath = new File("/Users/karl/LocalDocuments/FTC/UltimateGoal/Code/UltimateGoalFTCApp/TeamCode/src/main/java/UltimateGoal_RobotTeam");
//    static File directoryPath = new File("/Users/caleb/Documents/FTC/Android Studio/UltimateGoal/TeamCode/src/main/java/UltimateGoal_RobotTeam");
//    static File directoryPath = new File("/Users/young/Desktop/Android Studio/UltimateGoal/TeamCode/src/main/java/UltimateGoal_RobotTeam");
    public static void main(String args[]) throws IOException {
        //Creating a File object for directory
        //List of all files and directories
        File_IO fio = new File_IO();
        fio.makeDirectory(mainPathString);//Make the desired home directory
        System.out.println("List of files and directories in the directory:"+directoryPath);
        //List all files except for file exceptions noted in exclude
        //Write all .java files to their corresponding directory structure
        //  * Files are written with the changes for imports or classes based on the checkOfflineExceptions() method
        File filesList[] = fio.writeDirectory(directoryPath);

        System.out.println("***** Read/Write Completed ******");
        System.out.println("Check Screen Output for Errors");

    }


    public File[] writeDirectory(File dir) throws IOException {
        File filesList[] = dir.listFiles();
        System.out.println("Directory name: " + dir.getName());
        System.out.println("Directory path: " + dir.getAbsolutePath());
        if(filesList != null){
            System.out.println("Directory Contents");
            for(File file : filesList) {
                System.out.println("\tFile name: " + file.getName());
                System.out.println("\tFile path: " + file.getAbsolutePath());
                System.out.println(" ");
//                File subFilesList2[] = file.listFiles();
                writeSubDirectory(file, 1,mainPathString);
            }
        }
        else{
            System.out.println("\tNot a directory");
        }
        return filesList;
    }
    public boolean writeSubDirectory(File dir, int level, String filePath) throws IOException {
        File filesList[] = dir.listFiles();
        boolean writeFile = false;
        if (!dir.getName().equals(exclude)) {
            String indent = "";
            String subIndent = "\t";
            for (int i = 1; i <= level; i++) {
                indent += "\t";
                subIndent += "\t";
            }
            if (filesList != null) {
                System.out.println(indent + "Directory Contents");
                for (File file : filesList) {
                    System.out.println(subIndent + "File/Dir name: " + file.getName());
                    System.out.println(subIndent + "File/DIr path: " + file.getAbsolutePath());
                    String outputPathStr = String.format("%s/%s/",filePath,dir.getName());
                    boolean write = writeSubDirectory(file, level + 1, outputPathStr);
                    if(write){
                        makeDirectory(outputPathStr);
                        writeClassFile(outputPathStr, file);
                    }
                }
            }
            else {
                System.out.println(subIndent + "Not a directory");
                if (dir.getName().endsWith(".java")) {
                    System.out.println(subIndent + "Java Class File");
                    writeFile = true;
                }
                System.out.println(" ");
            }
        }
        return writeFile;
    }
    public void writeClassFile(String pathStr, File javaFile) throws IOException {
        try{
            String outputFilename = pathStr + javaFile.getName();

            FileOutputStream fos = new FileOutputStream(outputFilename);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            FileReader in = new FileReader(javaFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(in);
            String s;
            System.out.println("Reading from " + javaFile.getAbsolutePath());
            System.out.println("Writing to " + outputFilename);

            while((s = br.readLine()) != null){
                s = checkOfflineExceptions(s);//Looks for offline changes
                osw.write(s + "\n");
            }
            in.close();
            osw.close();
            System.out.println("Writing Complete");
            System.out.println("*********************************");
            System.out.println(" ");
        }
        catch(Exception e) {
            System.out.println("There was an Error Writing: " + e.toString());
        }
    }

    public void makeDirectory(String checkPath) throws IOException {
        filePath = Paths.get(checkPath);
        if(!Files.exists(filePath)){
            Files.createDirectory(filePath);
            System.out.println(filePath +" was created");
        }
        else {
            System.out.println("Directory Already Exists, No directory made");
        }
    }
    public String checkOfflineExceptions(String input){
        String output = input;
        //Method looks for the exceptions for Offline HW classes and other classes and makes updates
        switch(input){

        /* ITEMS BELOW ARE FOR IMPORT STATEMENTS IN OFFLINE HW CLASSES */
            case "import com.qualcomm.robotcore.hardware.DcMotor;":// DcMotor
                output ="import OfflineCode.OfflineHW.DcMotor;";
                break;
            case "import com.qualcomm.robotcore.hardware.CRServo;":// CRServo
                output ="import OfflineCode.OfflineHW.CRServo;";
                break;
            case "import com.qualcomm.robotcore.hardware.Servo;":// Servo
                output ="import OfflineCode.OfflineHW.Servo;";
                break;
            case "//import OfflineCode.OfflineHW.CameraSimulant;//NEEDED FOR OFFLINE":// ImageRecognition camera simulant
                output ="import OfflineCode.OfflineHW.CameraSimulant;";
                break;
//            case "    public TFObjectDetector tfod;":// ImageRecognition CameraSimulant tfod definition
//                output ="    public CameraSimulant tfod;";
//                break;

            case "import com.qualcomm.hardware.bosch.BNO055IMU;":// IMU
                output ="import OfflineCode.OfflineHW.BNO055IMU;";
                break;
            case "import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;":// IMU logging
                output ="import OfflineCode.OfflineHW.JustLoggingAccelerationIntegrator;";
                break;
            case "import com.qualcomm.robotcore.hardware.ColorSensor;":// color sensor - not used
                output ="import OfflineCode.OfflineHW.ColorSensor;";
                break;

        /* ITEMS BELOW ARE FOR BasicOpMode IMPORTS AND MEMBERS*/
            case "//import OfflineCode.OfflineHW.Telemetry;//NEEDED FOR OFFLINE":// telemetry import
                output ="import OfflineCode.OfflineHW.Telemetry;";
                break;
            case "//import OfflineCode.OfflineOpModes.OpModeParamFunctions;//NEEDED FOR OFFLINE":// OpModeParamFunctions import
                output ="import OfflineCode.OfflineOpModes.OpModeParamFunctions;";
                break;
            case "//public Telemetry telemetry = new Telemetry();//NEEDED FOR OFFLINE":// telemetry class
                output ="public Telemetry telemetry = new Telemetry();//used for OfflineCode";
                break;
            case "//    public OpModeParamFunctions ompf = new OpModeParamFunctions();//NEEDED FOR OFFLINE":// OpModeParamFunctions - instantiation
                output ="   public OpModeParamFunctions ompf = new OpModeParamFunctions();";
                break;
            case "//public double timeStep = 135;//NEEDED FOR OFFLINE":// timeStep for offline IMU calcs
                output ="public double timeStep = 135;//NEEDED FOR OFFLINE";
                break;
            case "//    public FieldConfiguration fc = new FieldConfiguration();//NEEDED FOR OFFLINE":// field configuration
                output ="    public FieldConfiguration fc = new FieldConfiguration();//NEEDED FOR OFFLINE";
                break;
            case "//            robotUG.driveTrain.imu.flCnt = robotUG.driveTrain.frontLeft.getCurrentPosition();":// Needed for updateIMU()
                output ="            robotUG.driveTrain.imu.flCnt = robotUG.driveTrain.frontLeft.getCurrentPosition();";
                break;
            case "//            robotUG.driveTrain.imu.frCnt = robotUG.driveTrain.frontRight.getCurrentPosition();":// Needed for updateIMU()
                output ="            robotUG.driveTrain.imu.frCnt = robotUG.driveTrain.frontRight.getCurrentPosition();";
                break;
            case "//            robotUG.driveTrain.imu.brCnt = robotUG.driveTrain.backRight.getCurrentPosition();":// Needed for updateIMU()
                output ="            robotUG.driveTrain.imu.brCnt = robotUG.driveTrain.backRight.getCurrentPosition();";
                break;
            case "//            robotUG.driveTrain.imu.blCnt = robotUG.driveTrain.backLeft.getCurrentPosition();":// Needed for updateIMU()
                output ="            robotUG.driveTrain.imu.blCnt = robotUG.driveTrain.backLeft.getCurrentPosition();";
                break;
            case "//            IMUCounter = robotUG.driveTrain.imu.counter;":// Needed for updateIMU()
                output ="            IMUCounter = robotUG.driveTrain.imu.counter;";
                break;
            case "//            fc.updateField(this);":// Needed for updateIMU()
                output ="            fc.updateField(this);";
                break;

            case "//import OfflineCode.Field.FieldConfiguration;":// field configuration
                output ="import OfflineCode.Field.FieldConfiguration;";
                break;
            case "\t\tboolean[] configArray = new boolean[]{true, true, true, true, true, true};":// HW config Array
                output ="\t\tboolean[] configArray = new boolean[]{ true, true, true, true, true,false};//Offline needs to disable collector";
                break;
            case "\t\treadOrWriteHashMap();":// HASHMAP
                output ="\t\treadOrWriteHashMapOffline();";
                break;

            /* ITEMS BELOW ARE FOR OFFLINE TEST MODE HW CONSTRUCTORS */
            case "//            collectorWheel = new DcMotor();collectorWheel.timeStep = om.timeStep;//NEEDED FOR OFFLINE":// COLLECTOR
                output ="            collectorWheel = new DcMotor();collectorWheel.timeStep = om.timeStep;//NEEDED FOR OFFLINE";
                break;
            case "//            conveyorLeft = new CRServo();conveyorRight = new CRServo();conveyorLeft.timeStep = om.timeStep;conveyorRight.timeStep = om.timeStep;//NEEDED FOR OFFLINE":// CONVEYOR
                output ="            conveyorLeft = new CRServo();conveyorRight = new CRServo();conveyorLeft.timeStep = om.timeStep;conveyorRight.timeStep = om.timeStep;//NEEDED FOR OFFLINE";
                break;
            case "//            frontLeft = new DcMotor();frontRight = new DcMotor();backLeft = new DcMotor();backRight = new DcMotor();imu = new BNO055IMU();//NEEDED FOR OFFLINE":// DRIVETRAIN NEW HW
                output ="            frontLeft = new DcMotor();frontRight = new DcMotor();backLeft = new DcMotor();backRight = new DcMotor();imu = new BNO055IMU();//NEEDED FOR OFFLINE";
                break;
            case "//            imu.timeStep = om.timeStep;frontLeft.timeStep = om.timeStep;frontRight.timeStep = om.timeStep;backRight.timeStep = om.timeStep;backLeft.timeStep = om.timeStep;//NEEDED FOR OFFLINE":// DRIVETRAIN TIMESTEPS
                output ="            imu.timeStep = om.timeStep;frontLeft.timeStep = om.timeStep;frontRight.timeStep = om.timeStep;backRight.timeStep = om.timeStep;backLeft.timeStep = om.timeStep;//NEEDED FOR OFFLINE";
                break;
//            case "//            tfod = new CameraSimulant();//NEEDED FOR OFFLINE":// IMAGE RECOGNITION
//                output ="            tfod = new CameraSimulant();//NEEDED FOR OFFLINE";
//                break;
            case "//            shooterLeft = new DcMotor();shooterRight = new DcMotor();shooterLeft.timeStep = om.timeStep;shooterRight.timeStep = om.timeStep;//NEEDED FOR OFFLINE":// SHOOTER
                output ="            shooterLeft = new DcMotor();shooterRight = new DcMotor();shooterLeft.timeStep = om.timeStep;shooterRight.timeStep = om.timeStep;//NEEDED FOR OFFLINE";
                break;
            case "//            wobbleGoalServo = new Servo();wobbleGoalArm = new DcMotor();wobbleGoalArm.timeStep = om.timeStep * (0.5);//NEEDED FOR OFFLINE":// WOBBLE GOAL ARM
                output ="            wobbleGoalServo = new Servo();wobbleGoalArm = new DcMotor();wobbleGoalArm.timeStep = om.timeStep * (0.5);//NEEDED FOR OFFLINE";
                break;
            default:
                output = input;//is this needed for items that don't meet cases?
                break;

        }

        return output;
    }
}