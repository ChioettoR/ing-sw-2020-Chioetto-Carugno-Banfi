package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Events.Client.*;
import it.polimi.ingsw.Events.Server.MessageEvent;
import it.polimi.ingsw.Model.ActionType;
import org.junit.platform.commons.util.StringUtils;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CLIStdinReader {

    Client client;
    Scanner stdin;
    boolean isLogin = true;
    boolean waiting = false;
    ActionType selectedActionType;

    public void setSelectedActionType(ActionType selectedActionType) {
        this.selectedActionType = selectedActionType;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public CLIStdinReader(Client client) {
        this.client = client;
        stdin = new Scanner(System.in);
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() throws NoSuchElementException, IllegalStateException {

        String inputLine;

        while (true) {
            inputLine = stdin.nextLine();
            if (inputLine.isEmpty() || StringUtils.isBlank(inputLine)) client.read(new MessageEvent(420));
            else read(inputLine.split("\\s+"));
        }
    }

    private void read(String[] strings) {

        if(waiting) waitingEvents();

        else if(isLogin) loginEvents(strings);

        else if(strings.length==1)
            readOneString(strings[0]);

        else if(strings.length==2)
            readTwoStrings(strings[0], strings[1]);

        else unknownInput();
    }

    private void readOneString(String string) {

        if(compareString(string, Input.DRAW))
            client.update(new DrawEvent());

        else if(isGridPosition(string) && selectedActionType==null) {
            int[] coordinates = readGridString(string);
            client.update(new PositioningEvent(coordinates[0], coordinates[1]));
        }

        else if(isGridPosition(string))
            actionEvents(string);

        else if(string.length()==1 && !isNumeric(string) && isLetter(string.charAt(0)))
            client.update(new SelectionEvent(convertLetter(string.charAt(0))));

        else if(compareString(string, Input.PICK))
            client.update(new PickCardEvent(""));

        else if(compareString(string, Input.MOVE))
            client.update(new ActionSelectEvent(ActionType.MOVE.toString()));

        else if(compareString(string, Input.BUILD))
            client.update(new ActionSelectEvent(ActionType.BUILD.toString()));

        else if(compareString(string, Input.UNDO))
            client.update(new ActionSelectEvent(ActionType.UNDO.toString()));

        else if(compareString(string, Input.CONFIRM))
            client.update(new ActionSelectEvent(ActionType.CONFIRM.toString()));

        else unknownInput();
    }

    private void readTwoStrings(String firstString, String secondString) {

        if(compareString(firstString, Input.PICK))
            client.update(new PickCardEvent(secondString));

        else if(compareString(firstString, Input.INFO))
            client.getEventsCommunication().infoEffect(secondString);

        else if(compareString(firstString+secondString, Input.END_ROUND))
            client.update(new ActionSelectEvent(ActionType.ENDROUND.toString()));

        else if(selectedActionType!=null && isGridPosition(firstString) && isNumeric(secondString))
            buildEventWithLevel(firstString, Integer.parseInt(secondString));

        else unknownInput();
    }

    private void loginEvents(String[] strings) {

        if(strings.length==1) {
            if(isNumeric(strings[0])) client.update(new LobbySizeEvent(Integer.parseInt(strings[0])));
            else client.update(new LoginNameEvent(strings[0]));
        }

        else if(strings.length>=2 && !isNumeric(strings[0]))
            client.read(new MessageEvent(421));

        else unknownInput();
    }

    private void waitingEvents() {
        client.read(new MessageEvent(307));
    }

    private void actionEvents(String gridString) {
        int[] coordinates = readGridString(gridString);
        int x = coordinates[0];
        int y = coordinates[1];

        switch (selectedActionType) {
            case MOVE: {
                client.update(new MoveDecisionEvent(x, y));
                break;
            }
            case BUILD: {
                client.update(new BuildDecisionEvent(x, y));
            }
        }
    }

    private void buildEventWithLevel(String gridString, int buildLevel) {
        int[] coordinates = readGridString(gridString);
        client.update(new BuildDecisionEvent(coordinates[0], coordinates[1], buildLevel));
    }

    private int[] readGridString(String gridString) {
        int x = Integer.parseInt(String.valueOf(gridString.charAt(0)));
        int y = Integer.parseInt(String.valueOf(gridString.charAt(2)));
        return new int[] {x,y};
    }

    private void unknownInput() {
        client.read(new MessageEvent(412));
    }

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null)
            return false;
        return pattern.matcher(strNum).matches();
    }

    private boolean isGridPosition(String string) {
        return (string.length() == 3 && Character.isDigit(string.charAt(0)) && string.charAt(1) == 'x' && Character.isDigit(string.charAt(2)));
    }

    private int convertLetter(char c) {
        return Character.toUpperCase(c) - 64;
    }

    private boolean isLetter(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    private boolean compareString(String userInput, Input input) {

        String[] inputs = input.toString().split("_");
        String[] userInputs = userInput.split("\\s+");
        if(inputs.length!=userInputs.length) return false;

        for(int i=0; i<inputs.length; i++) {
            if(!inputs[i].equalsIgnoreCase(userInputs[i]))
                return false;
        }
        return true;
    }
}


