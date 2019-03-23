/**
 * Pilot of the space station docking simulator.
 *
 * It is responsible for:
 *  - Operating ship
 *
 * @author weikaiz1@student.unimelb.edu.au
 *
 */


public class Pilot extends Thread{

    private int index;
    private WaitZone arrivaleZone;
    private WaitZone departureZone;
    private TugController tugController;
    private Berth berth;
    private Ship operatingShip;

    public Pilot(int index, WaitZone arrivaleZone, WaitZone departureZone,TugController tugController, Berth berth){
        this.index = index;
        this.arrivaleZone = arrivaleZone;
        this.departureZone = departureZone;
        this.tugController = tugController;
        this.berth = berth;
    }

    @Override
    /**
     * The driver of pilot
     */
    public void run() {
        try {
            while(true){
                requireShip();
                travelToBerth();
                docking();
                unload();
                undocking();
                releaseShip();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Require ship from arrival zone
     */
    private void requireShip() throws InterruptedException {
        Ship ship = arrivaleZone.depart();
        System.out.println("pilot " + index + " acquires    " + ship.toString());
        tugController.requireTugs(Params.DOCKING_TUGS, index);
        operatingShip = ship;
    }


    /**
     * Drive ship to vicinity of berth
     */
    private void travelToBerth() throws InterruptedException{
        Thread.sleep(Params.TRAVEL_TIME);
    }

    /**
     * Drive ship to vicinity of berth
     */
    private void docking() {
        synchronized (Operator.shieldLock) {
            try {
                //while the berth is empty and shield is closed
                while (!berth.isAccessable()) {
                    Operator.shieldLock.wait();
                }
                berth.docking();
                Thread.sleep(Params.DOCKING_TIME);
                System.out.println(operatingShip.toString() + " docks at berth.");
                tugController.releasesTugs(Params.DOCKING_TUGS, index);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void unload() throws InterruptedException {
        Thread.sleep(Params.UNLOADING_TIME);
        System.out.println("ship "+ operatingShip.toString() + " being unloaded.");
    }

    private void undocking() throws InterruptedException{
        synchronized (Operator.shieldLock) {
            tugController.requireTugs(Params.UNDOCKING_TUGS, index);
            Thread.sleep(Params.UNDOCKING_TIME);
            berth.undocking();
            Operator.shieldLock.notify();
            System.out.println("ship " + operatingShip.toString() + " undocks from berth.");
            tugController.releasesTugs(Params.UNDOCKING_TUGS, index);
        }
    }

    private void releaseShip(){
        departureZone.arrive(operatingShip);
        System.out.println("pilot " + index + " releases " + operatingShip.toString());
    }
}
