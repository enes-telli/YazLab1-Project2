package elevatorcontroller;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginThread implements Runnable {

    private Thread thread;
    ArrayList<Integer>[] queues;

    public LoginThread(ArrayList<Integer>[] queues) {
        this.queues = queues;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Random random = new Random();
                int randomCustomerCount = random.nextInt(10) + 1; // [1-10]
                for (int i = 0; i < randomCustomerCount; i++) {
                    int randomFloorToGo = random.nextInt(4) + 1; // [1-4]
                    queues[0].add(randomFloorToGo);
                }
                Thread.sleep(500);
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
}
