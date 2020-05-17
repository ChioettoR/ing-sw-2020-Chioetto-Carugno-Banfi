package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Color;
import it.polimi.ingsw.Model.TileSimplified;
import it.polimi.ingsw.Model.WorkerSimplified;

import java.util.ArrayList;
import java.util.regex.Pattern;

class CLIGridChanger {

    CLIPlayersManager cliPlayersManager;

    public CLIGridChanger(CLIPlayersManager cliPlayersManager) {
        this.cliPlayersManager = cliPlayersManager;
    }

    void change(CLIGrid CLIGrid, ArrayList<TileSimplified> tiles) {

        for(TileSimplified tileSimplified : tiles) {

            CLITile cliTile = CLIGrid.getTile(tileSimplified.getX(), tileSimplified.getY());
            WorkerSimplified workerSimplified = tileSimplified.getWorkerSimplified();
            int buildLevel = tileSimplified.getBuildLevel();

            //Empty tile
            if(cliTile.getWords()[0].getString().isBlank() && cliTile.getWords()[1].getString().isBlank()  && cliTile.getWords()[2].getString().isBlank()) {
                if(workerSimplified==null) changeLevel(cliTile, buildLevel, true);
                else if(buildLevel == 0) changeWorker(cliTile, workerSimplified, true);
                else changeAll(cliTile, buildLevel, workerSimplified);
            }

            //Not empty tile (only a worker or a level)
            else if(!cliTile.getWords()[1].getString().equals("/")) {

                //Only level
                if(isNumeric(cliTile.getWords()[1].getString())) {

                    //New buildLevel !=0 and no worker
                    if(workerSimplified==null && buildLevel!=0)
                        changeLevel(cliTile, buildLevel, true);

                        //Build level 0 and no worker
                    else if(workerSimplified == null) clearAll(cliTile);

                        //New worker and old build level
                    else if (Integer.toString(buildLevel).equals(cliTile.getWords()[1].getString())) {
                        shiftPosition(cliTile, 2);
                        changeWorker(cliTile, workerSimplified, false);
                    }

                    //New worker and build level 0
                    else if(buildLevel == 0) changeWorker(cliTile, workerSimplified, true);

                        //New worker and new level
                    else changeAll(cliTile, buildLevel, workerSimplified);
                }

                //Only worker
                else {

                    //New worker and no build Level
                    if(workerSimplified!=null && buildLevel==0)
                        changeWorker(cliTile, workerSimplified, true);

                        //No worker and no build level
                    else if(workerSimplified == null && buildLevel==0) clearAll(cliTile);

                        //No worker and build level
                    else if(workerSimplified == null) {
                        shiftPosition(cliTile, 0);
                        changeLevel(cliTile, buildLevel, false);
                    }

                    //New worker and build Level
                    else changeAll(cliTile, buildLevel, workerSimplified);
                }
            }

            //Worker and build level both present
            else {

                //Everything null
                if(workerSimplified==null && buildLevel==0) clearAll(cliTile);

                    //Worker null
                else if(workerSimplified == null) {
                    clearAll(cliTile);
                    changeLevel(cliTile, buildLevel, true);
                }

                //New worker and old level
                else if(Integer.toString(buildLevel).equals(cliTile.getWords()[2].getString()))
                    changeWorker(cliTile, workerSimplified, false);

                    //New worker and level 0
                else if(buildLevel==0) {
                    clearAll(cliTile);
                    changeWorker(cliTile, workerSimplified, true);
                }

                //Everything new
                else {
                    changeWorker(cliTile, workerSimplified, false);
                    changeLevel(cliTile, buildLevel, false);
                }
            }
        }
    }

    private void changeLevel(CLITile cliTile, int buildLevel, boolean levelOnly) {
        int position;
        if(levelOnly) position = 1;
        else position = 2;
        cliTile.getWords()[position] = new StringWrapper(Integer.toString(buildLevel));
    }

    private void changeWorker(CLITile cliTile, WorkerSimplified workerSimplified, boolean workerOnly) {
        int position;
        if(workerOnly) position = 1;
        else position = 0;
        String name = workerSimplified.getPlayerName();
        int workerID = workerSimplified.getLocalID();
        Color color = cliPlayersManager.color(name);
        cliTile.getWords()[position] = new StringWrapper(color.escape() + getCharForNumber(workerID) + Color.RESET);
    }

    private void changeAll(CLITile cliTile, int buildLevel, WorkerSimplified workerSimplified) {
        changeLevel(cliTile, buildLevel, false);
        changeWorker(cliTile, workerSimplified, false);
        cliTile.getWords()[1] = new StringWrapper("/");
    }

    private void shiftPosition(CLITile cliTile, int newPosition) {
        StringWrapper oldPosition = cliTile.getWords()[1];
        cliTile.getWords()[1] = new StringWrapper("/");
        cliTile.getWords()[newPosition] = oldPosition;
    }

    private void clearAll(CLITile cliTile) {
        cliTile.getWords()[0] = new StringWrapper(" ");
        cliTile.getWords()[1] = new StringWrapper(" ");
        cliTile.getWords()[2] = new StringWrapper(" ");
    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)).trim() : null;
    }

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null)
            return false;
        return pattern.matcher(strNum).matches();
    }
}
