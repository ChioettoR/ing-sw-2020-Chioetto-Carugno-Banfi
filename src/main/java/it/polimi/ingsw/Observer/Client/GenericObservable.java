package it.polimi.ingsw.Observer.Client;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericObservable {
    protected final List<ClientObserver> observers = new ArrayList<>();

    public void addObserver(ClientObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(ClientObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }
}
