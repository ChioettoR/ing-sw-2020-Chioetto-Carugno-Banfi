package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Tile {
    private final int x;
    private final int y;
    private boolean isEmpty = true;
    private Worker worker;
    private ArrayList<Integer> levels = new ArrayList<>();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ArrayList<Integer> getLevels() {
        return levels;
    }

    public void setLevels(ArrayList<Integer> levels) {
        this.levels = levels;
    }

    /**
     * Returns the last level of the tile
     * @return The level of the tile
     */
    public int getLevel() {
        if(levels.size() == 0) {
            System.out.println("This tile has no levels");
            return -1;
        }
        return levels.get(levels.size() - 1);
    }

    /**
     * Removes the last level of the tile
     */
    public void removeLastLevel() {
        if(levels.size()==0)
            System.out.println("This tile has only ground level");
        else {
            if(levels.size()==5)
                Grid.getGrid().setCompleteTowersCount(Grid.getGrid().getCompleteTowersCount()-1);
            levels.remove(levels.size() - 1);
        }
    }

    public int getLevelsSize() {
        return levels.size();
    }

    public void setLevel(int level) {
        if(level <= getLevel() || level > 4){
            System.out.println("You can't build that block!");
            return;
        }

        this.levels.add(level);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
        if(empty) worker = null;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
        setEmpty(worker == null);
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        levels.add(0);
    }

    /**
     * Simplifies the tile
     * @return the tile simplified
     */
    public TileSimplified simplify() {
        WorkerSimplified workerSimplified;
        if(worker==null)
            workerSimplified = null;
        else
            workerSimplified = worker.simplify();
        return new TileSimplified(x,y,getLevel(),workerSimplified);
    }
}
