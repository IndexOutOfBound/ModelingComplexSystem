public class Operator extends Thread{

    private Berth berth;
    public static final Object shieldLock = new Object();

    public Operator(Berth berth){
        this.berth = berth;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                // let some time pass before the next debris
                sleep(Params.debrisLapse());

                // open the sheld
                berth.openSheld();
                System.out.println("Shield open");
                sleep(Params.DEBRIS_TIME);
                berth.closeSheld();
                System.out.println("Shield closed");
                synchronized (shieldLock){
                    shieldLock.notify();
                }
            }
            catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
