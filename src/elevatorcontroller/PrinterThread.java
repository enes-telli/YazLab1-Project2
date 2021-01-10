package elevatorcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrinterThread implements Runnable {

    private Thread thread;
    private float time;

    //referenced variables
    private ArrayList<Integer>[] queues;
    private ArrayList<Integer> exitQueue;
    private ArrayList<Integer>[] totalCustomers;
    ElevatorThread[] elevatorThreads;

    public PrinterThread() {
        time = 0;
        //referenced variables
        this.queues = Base.queues;
        this.exitQueue = Base.exitQueue;
        this.totalCustomers = Base.totalCustomers;
        this.elevatorThreads = Base.elevatorThreads;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(String.format("\n[t = %.1f]", time));
                time += 0.2;
                printFloors();
                for (int i = 0; i < 5; i++) {
                    System.out.println("Elevator-" + (i + 1) + ", Active: " + elevatorThreads[i].isActive);
                    System.out.println("\tmode: " + ((elevatorThreads[i].isActive) ? "working" : "idle"));
                    System.out.println("\tfloor: " + elevatorThreads[i].currentFloor);
                    System.out.println("\tdestination: " + elevatorThreads[i].destination);
                    System.out.println("\tdirection: " + ((elevatorThreads[i].isUp) ? "up" : "down"));
                    System.out.println("\tcapacity: 10");
                    System.out.println("\tcount inside: " + elevatorThreads[i].elevator.size());
                    System.out.println("\tinside: " + setInsideString(elevatorThreads[i].elevator));
                }
                for (int i = 0; i < 5; i++) {
                    System.out.println(i + ".floor: " + setInsideString(queues[i]));
                }
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(PrinterThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    private void printFloors() {
        System.out.println("0.floor => queue: " + queues[0].size());
        for (int i = 1; i < 5; i++) {
            System.out.println("" + i + ".floor => queue: " + queues[i].size() + ", all: " + totalCustomers[i].size());
        }
        System.out.println("Exit count: " + exitQueue.size());
    }

    private String setInsideString(ArrayList<Integer> elevator) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < elevator.size(); i++) {
            if (map.containsKey(elevator.get(i))) {
                map.put(elevator.get(i), map.get(elevator.get(i)) + 1);
            } else {
                map.put(elevator.get(i), 1);
            }
        }
        ArrayList<String> result = new ArrayList<String>();
        for (int i : map.keySet()) {
            result.add("(" + i + ", " + map.get(i) + ")");
        }
        String inside = String.join(", ", result);
        return "[" + inside + "]";
    }
}
