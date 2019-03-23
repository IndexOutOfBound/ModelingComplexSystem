public class TugController {

    private volatile int tugNum;

    public TugController(int tugNum){
        this.tugNum = tugNum;
    }

    public synchronized void requireTugs(int tugsNeed, int pilotIndex){
        try {
            System.out.println(
                    String.format("pilot %d acquires %d tugs (%d available).", pilotIndex, tugsNeed, tugNum));
            while(tugsNeed > tugNum){
                wait();
            }
            tugNum = tugNum - tugsNeed;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void releasesTugs(int tugReleased, int pilotIndex){
        tugNum = tugNum + tugReleased;
        System.out.println(
                String.format("pilot %d release %d tugs (%d available).", pilotIndex, tugReleased, tugNum));

        notify();
    }
}
