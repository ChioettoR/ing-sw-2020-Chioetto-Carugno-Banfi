package it.polimi.ingsw;

import java.io.IOException;
import java.util.TimerTask;

public class CountdownTask extends TimerTask {

    private int time;
    private final CountdownInterface countdownInterface;

    public CountdownTask(int time, CountdownInterface countdownInterface) {
        this.time = time;
        this.countdownInterface = countdownInterface;
    }

    @Override
    public void run() {
        if (time > 0) {
            time--;
        }
        else {
            try {
                System.out.println("Countdown ended");
                countdownInterface.countdownEnded();
                cancel();
            }
            catch (IOException e) { System.out.println("Client Unreachable");}
        }
    }
}
