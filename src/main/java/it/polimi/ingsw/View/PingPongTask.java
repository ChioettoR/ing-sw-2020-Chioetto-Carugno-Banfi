package it.polimi.ingsw.View;

import it.polimi.ingsw.CountdownTask;
import it.polimi.ingsw.Events.Server.PingEvent;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PingPongTask extends java.util.TimerTask {

    Connection connection;
    TimerTask countdownTask;
    Timer pongCountdownTimer;

    public PingPongTask(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            System.out.println("PING SENT");
            connection.send(new PingEvent());
            countdownTask = new CountdownTask(5, connection);
            pongCountdownTimer = new Timer();
            pongCountdownTimer.schedule(countdownTask, 0, 1000);
        }
        catch (IOException e) { System.err.println(e.getMessage()); }
    }

    public void cancelCountdown() {
        pongCountdownTimer.cancel();
        pongCountdownTimer.purge();
    }
}
