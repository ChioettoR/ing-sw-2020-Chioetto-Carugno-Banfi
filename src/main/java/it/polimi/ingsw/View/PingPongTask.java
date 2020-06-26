package it.polimi.ingsw.View;

import it.polimi.ingsw.CountdownTask;
import it.polimi.ingsw.Events.Server.PingEvent;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PingPongTask extends java.util.TimerTask {

    private final Connection connection;
    private Timer pongCountdownTimer;
    private final int time;

    public PingPongTask(int time, Connection connection) {
        this.connection = connection;
        this.time = time;
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                connection.send(new PingEvent());
                TimerTask countdownTask = new CountdownTask(time, connection);
                pongCountdownTimer = new Timer();
                pongCountdownTimer.schedule(countdownTask, 0, 1000);
            }
        }
        catch (IOException e) { System.err.println(e.getMessage()); }
    }

    public synchronized void cancelCountdown() {
        pongCountdownTimer.cancel();
    }
}
