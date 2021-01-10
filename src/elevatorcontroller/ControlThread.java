
package elevatorcontroller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlThread implements Runnable {
    
    private Thread thread;
    ArrayList<Integer>[] queues;
    ElevatorThread[] elevatorThreads;
    
    public ControlThread() {
        this.queues = Base.queues;
        this.elevatorThreads = Base.elevatorThreads;
    }
    
    @Override
    public void run() {
        while (true) 
        {
            try {
                if (isNewElevatorRequired())
                {
                    for (int i = 0; i < 5; i++)
                    {
                        if (!elevatorThreads[i].isActive)
                        {
                            elevatorThreads[i].isActive = true;
                            elevatorThreads[i].start();
                            break;
                        }
                    }
                } 
                if (isElevatorDone())
                {
                    for (int i = 4; i >= 0; i--)
                    {
                        if (elevatorThreads[i].isActive)
                        {
                            // buraya asansörün içindeki adamları boşlatma gibi bir şey eklenebilir.
                            elevatorThreads[i].isActive = false;
                            break;
                        }
                    }
                }
                
                Thread.sleep(100);
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
    
    private boolean isNewElevatorRequired()
    {
        // bütün asansörler halihazırda çalışıyorsa
        int check = 0;
        for(int i = 0; i < 5; i++)
        {
            if (elevatorThreads[i].isActive)
            {
                check++;
            }
        }
        if (check == 5)
        {
            return false;
        }
        
        // kuyrukların doluluk kontrolü
        int totalCustomers = 0;
        for (int i = 0; i < 5; i++)
        {
            totalCustomers += queues[i].size();
        }
        return (totalCustomers > 20);
    }
    
    private boolean isElevatorDone()
    {
        int check = 0;
        for(int i = 0; i < 5; i++)
        {
            if (elevatorThreads[i].isActive)
            {
                check++;
            }
        }
        if (check <= 1)
        {
            return false;
        }
        
        int totalCustomers = 0;
        for (int i = 0; i < 5; i++)
        {
            totalCustomers += queues[i].size();
        }
        return (totalCustomers < 10);
    }
}
