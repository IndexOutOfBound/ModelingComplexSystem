public class Berth {

    private String name;
    private volatile boolean empty;
    private volatile boolean sheldOpen;

    public Berth(String name){
        this.name = name;
        this.empty = true;
        this.sheldOpen = false;
    }

    public synchronized void closeSheld(){
        this.sheldOpen = false;
    }

    public synchronized void openSheld(){
        this.sheldOpen = true;
    }

    public synchronized void docking(){
        empty = false;
    }

    public synchronized void undocking(){
        empty = true;
    }

    public boolean isSheldOpen(){
        return sheldOpen;
    }

    public boolean isAccessable(){
        return empty && !sheldOpen;
    }

}
