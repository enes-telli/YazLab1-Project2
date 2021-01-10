package elevatorcontroller;

import java.util.ArrayList;

public class Base {

    public static ArrayList<Integer>[] queues = new ArrayList[5];
    public static ArrayList<Integer> exitQueue = new ArrayList<>();
    public static ArrayList<Integer>[] totalCustomers = new ArrayList[5];
    public static ElevatorThread[] elevatorThreads = new ElevatorThread[5];

    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            queues[i] = new ArrayList<Integer>();
            totalCustomers[i] = new ArrayList<Integer>();
        }

        LoginThread loginThread = new LoginThread(queues);
        loginThread.start();

        ExitThread exitThread = new ExitThread();
        exitThread.start();

        for (int i = 0; i < 5; i++) {
            elevatorThreads[i] = new ElevatorThread((i == 0));
        }
        elevatorThreads[0].start();

        ControlThread controlThread = new ControlThread();
        controlThread.start();

        PrinterThread printer = new PrinterThread();
        printer.start();

    }

}
