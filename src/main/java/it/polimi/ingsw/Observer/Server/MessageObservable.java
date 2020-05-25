package it.polimi.ingsw.Observer.Server;

import it.polimi.ingsw.Events.Server.*;
import it.polimi.ingsw.Observer.Client.ClientObserver;

import java.io.IOException;
import java.util.ArrayList;

public class MessageObservable{
    protected final ArrayList<ServerObserver> observers = new ArrayList<>();

    public void addObserver(ServerObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(ServerObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }
    /**
     *This is a notify for the client of a messageEvent
     * @param messageEvent message event notified
     * @throws IOException when socket closes
     */
    public void notifyMessage(MessageEvent messageEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(messageEvent);
            }
        }
    }
    /**
     *This is a notify for the client of winEvent
     * @param winEvent win event notified
     * @throws IOException when socket closes
     */
    public void notifyWin(WinEvent winEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(winEvent);
            }
        }
    }
    /**
     *This is a notify for the client of loseEvent
     * @param loseEvent lose event notified
     * @throws IOException when socket closes
     */
    public void notifyLose(LoseEvent loseEvent) throws IOException {
        synchronized (observers) {
            for (ServerObserver observer : observers) {
                observer.update(loseEvent);
            }
        }
    }
}
