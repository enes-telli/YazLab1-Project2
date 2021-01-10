package elevatorcontroller;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExitThread implements Runnable {

    private Thread thread;

    //referenced variables
    ArrayList<Integer>[] queues;
    ArrayList<Integer>[] totalCustomers;

    public ExitThread() {
        this.queues = Base.queues;
        this.totalCustomers = Base.totalCustomers;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!isAllFloorsEmpty()) {
                    Random random = new Random();
                    int randomFloor = 1;
                    do {
                        randomFloor = random.nextInt(4) + 1;
                    } while (totalCustomers[randomFloor].size() <= 0);
                    int randomCustomerCount = totalCustomers[randomFloor].size();
                    if (randomCustomerCount > 5) {
                        randomCustomerCount = random.nextInt(5) + 1;
                    } else {
                        randomCustomerCount = random.nextInt(randomCustomerCount) + 1;
                    }

                    for (int i = 0; i < randomCustomerCount; i++) {
                        queues[randomFloor].add(0);
                        totalCustomers[randomFloor].remove(0);
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(LoginThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    private boolean isAllFloorsEmpty() {
        for (int i = 1; i < 5; i++) {
            if (totalCustomers[i].size() > 0) {
                return false;
            }
        }
        return true;
    }
}
