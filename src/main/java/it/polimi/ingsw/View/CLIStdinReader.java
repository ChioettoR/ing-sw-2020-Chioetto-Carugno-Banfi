package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.Client.*;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CLIStdinReader {

    Client client;
    Scanner stdin;

    public CLIStdinReader(Client client) {
        this.client = client;
        stdin = new Scanner(System.in);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() throws IOException, NoSuchElementException, IllegalStateException {

        String inputLine = "";

        while (true) {
            inputLine = stdin.nextLine();
            if (inputLine.isEmpty() || StringUtils.isBlank(inputLine)) System.out.println("Please insert a valid input");
            else read(inputLine.split("\\s+"));
        }
    }

    private void read(String[] strings) throws IOException {

        if(client.isLogin())
            loginEvents(strings);

        else if(strings.length==1)
            readOneString(strings[0]);

        else if(strings.length==2)
            readTwoStrings(strings[0], strings[1]);

        else if(strings.length==3 && !client.isLogin())
            readThreeString(strings[0], strings[1], strings[2]);

        else unknownInput();
    }

    private void readOneString(String string) throws IOException {

        if(string.equalsIgnoreCase("draw"))
            client.update(new DrawEvent());

        else if(isGridPosition(string)) {
            int[] coordinates = readGridString(string);
            System.out.println(coordinates[0]);
            System.out.println(coordinates[1]);
            client.update(new PositioningEvent(coordinates[0], coordinates[1]));
        }

        else if(string.equalsIgnoreCase("A"))
            client.update(new SelectionEvent(1));

        else if(string.equalsIgnoreCase("B"))
            client.update(new SelectionEvent(2));

        else if(string.equalsIgnoreCase("pick"))
            client.update(new PickCardEvent(""));

        else if(string.equalsIgnoreCase("move") || string.equalsIgnoreCase("build") || string.equalsIgnoreCase("undo"))
            client.update(new ActionSelectEvent(string.toUpperCase()));

        else unknownInput();
    }

    private void readTwoStrings(String firstString, String secondString) throws IOException {

        if(firstString.equalsIgnoreCase("pick"))
            client.update(new PickCardEvent(secondString));

        else if(firstString.equalsIgnoreCase("end") && secondString.equalsIgnoreCase("round"))
            client.update(new ActionSelectEvent("ENDROUND"));

        else if(isGridPosition(secondString))
            actionEvents(firstString, secondString);

        else unknownInput();
    }

    private void readThreeString(String firstString, String secondString, String thirdString) throws IOException {

        if(firstString.equalsIgnoreCase("build") && isGridPosition(secondString) && isNumeric(thirdString))
            buildEventWithLevel(secondString, Integer.parseInt(thirdString));
    }

    private void loginEvents(String[] strings) throws IOException {

        if(strings.length==1) {
            if(isNumeric(strings[0])) client.update(new LobbySizeEvent(Integer.parseInt(strings[0])));
            else client.update(new LoginNameEvent(strings[0]));
        }

        else if(strings.length>=2 && !isNumeric(strings[0]))
            System.out.println("Names longer than one words are not accepted");

        else unknownInput();
    }

    private void actionEvents(String actionString, String gridString) throws IOException {
        int[] coordinates = readGridString(gridString);
        int x = coordinates[0];
        int y = coordinates[1];

        if(actionString.equalsIgnoreCase("move"))
            client.update(new MoveDecisionEvent(x, y));

        else if(actionString.equalsIgnoreCase("build"))
            client.update(new BuildDecisionEvent(x, y));

        else unknownInput();
    }

    private void buildEventWithLevel(String gridString, int buildLevel) throws IOException {
        int[] coordinates = readGridString(gridString);
        client.update(new BuildDecisionEvent(coordinates[0], coordinates[1], buildLevel));
    }

    private int[] readGridString(String gridString) {
        int x = Integer.parseInt(String.valueOf(gridString.charAt(0)));
        int y = Integer.parseInt(String.valueOf(gridString.charAt(2)));
        return new int[] {x,y};
    }

    private void unknownInput() {
        System.out.println("Unknown Input");
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
}


