package RobotVizCode;

public class AccessoryData {
    int collectorOn;
    int conveyorOn;
    int shooterOn;
    double wobbleArmAngleRad;

    public AccessoryData(){

    }
    public AccessoryData(int col, int con, int shoot, double wgaAngle){
        this.collectorOn = col;
        this.conveyorOn = con;
        this.shooterOn = shoot;
        this.wobbleArmAngleRad=wgaAngle;
    }
    /* NOTE: this is just a single timestep of data, it needs to be grouped into an Array or ArrayList*/
}
