package elevatorcontroller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElevatorThread implements Runnable {

    private Thread thread;
    public boolean isActive;
    public int currentFloor;
    public int destination;
    public boolean isUp;
    public ArrayList<Integer> elevator;

    //referenced variables
    ArrayList<Integer>[] queues;
    ArrayList<Integer>[] totalCustomers;
    ArrayList<Integer> exitQueue;

    public ElevatorThread(boolean isActive) {
        currentFloor = 0;
        destination = 0;
        isUp = true;
        this.isActive = isActive;
        elevator = new ArrayList<>();
        //referenced variables
        queues = Base.queues;
        totalCustomers = Base.totalCustomers;
        exitQueue = Base.exitQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (isActive) {
                    if (isUp) {
                        // giriş kuyruğundan asansöre insan almak için
                        if (currentFloor == 0) {
                            while (elevator.size() < 10) {
                                if (queues[0].size() > 0) {
                                    elevator.add(queues[0].get(0));
                                    queues[0].remove(0);
                                } else {
                                    break;
                                }
                            }
                            destination = 1;
                        }

                        int maxFloor = 0;
                        for (int i = 0; i < elevator.size(); i++) {
                            if (elevator.get(i) > maxFloor) {
                                maxFloor = elevator.get(i);
                            }
                        }

                        for (int i = 4; i > 0; i--) {
                            if (totalCustomers[i].size() > 0 && i > maxFloor) {
                                maxFloor = i;
                                break;
                            }
                        }
                        // 0 dan başlıyor. 0.kat
                        for (int i = 1; i <= maxFloor; i++) {
                            Thread.sleep(200);
                            // o katta inecek varsa asansörü boşalt
                            ArrayList<Integer> removeIndexes = new ArrayList<>();
                            for (int j = 0; j < elevator.size(); j++) {
                                if (elevator.get(j) == i) {
                                    removeIndexes.add(j);
                                }
                            }
                            for (int j = removeIndexes.size() - 1; j >= 0; j--) {
                                totalCustomers[i].add(i);
                                int index = (int) removeIndexes.get(j);
                                elevator.remove(index);
                            }
                            // varsa müşteri al 
                            // (aşağı inecekse bile yukarı çıkanların çıkmasını asansörde beklerler.)
                            while (elevator.size() < 10) {
                                if (queues[i].size() <= 0) {
                                    break;
                                }
                                elevator.add(i);
                                queues[i].remove(0);

                            }
                            currentFloor++;
                            if (currentFloor < maxFloor) {
                                destination++;
                            } else {
                                destination--;
                                isUp = false;
                            }
                        }
                    } else {
                        for (int i = currentFloor; i > 0; i--) {
                            Thread.sleep(200);
                            while (elevator.size() < 10) {
                                if (queues[i].size() <= 0) {
                                    break;
                                }
                                elevator.add(i);
                                queues[i].remove(0);
                            }
                            currentFloor--;
                            if (currentFloor > 0) {
                                destination--;
                            } else {
                                destination++;
                                isUp = true;
                            }
                        }
                        Thread.sleep(200);
                        while (elevator.size() > 0) {
                            exitQueue.add(elevator.get(0));
                            elevator.remove(0);
                        }
                    }
                } else {
                    destination = currentFloor;
                    break;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlThread.class.getName()).log(Level.SEVERE, null, ex);
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
