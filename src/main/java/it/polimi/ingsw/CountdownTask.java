package it.polimi.ingsw;

import java.io.IOException;
import java.util.TimerTask;

public class CountdownTask extends TimerTask {

    int time;
    CountdownInterface countdownInterface;

    public CountdownTask(int time, CountdownInterface countdownInterface) {
        this.time = time;
        this.countdownInterface = countdownInterface;
    }

    @Override
    public void run() {
        if (time > 0) time--;
        else {
            try {
                countdownInterface.countdownEnded();
                cancel();
            }
            catch (IOException e) { e.printStackTrace(); }
        }
    }
}
