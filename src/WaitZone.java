/**
 * WaitZone of the space station docking simulator.
 *
 * It is responsible for:
 *  - Operating ship
 *
 * @author weikaiz1@student.unimelb.edu.au
 *
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class WaitZone {

    private Semaphore notFull ;
    private Semaphore empty;
    private String name;
    //
    private Queue<Ship> ships;
    private static String promotion = "%s %s %s zone.";

    public WaitZone(String name, int size){
        this.name = name;
        this.ships = new LinkedList<>();
        this.notFull = new Semaphore(size,true);
        this.empty = new Semaphore(0,true);
    }

    /**
     * Ship arrive wait zone
     */
    public void arrive(Ship ship){
        try {
            /**
             * if the wait zone is not full run next instruction
             * otherwise wait here
             */
            notFull.acquire();
            ships.add(ship);
            System.out.println(String.format(promotion,ship.toString(), " arrives at ", name));
            // when one ship arrive notify one thread who is wait for ship if there are such thread
            empty.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }

    /**
     * Ship depart from wait zone
     */
    public Ship depart(){
        Ship ship = null;
        try {
            /**
             * if the wait zone is not full run next instruction
             * otherwise wait here
             */
            empty.acquire();
            ship = ships.poll();
            notFull.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ship;
    }

}
