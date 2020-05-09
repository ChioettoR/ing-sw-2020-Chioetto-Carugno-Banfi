package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.Client.*;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CLIStdinReader {

    Client client;
    Scanner stdin;
    ObjectOutputStream oos;

    public CLIStdinReader(Client client, Scanner stdin, ObjectOutputStream oos) {
        this.client = client;
        this.stdin = stdin;
        this.oos = oos;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() throws IOException, NoSuchElementException, IllegalStateException {

        String inputLine = "";
        while (true) {
            inputLine = stdin.nextLine();

            if (inputLine.isEmpty() || StringUtils.isBlank(inputLine))
                System.out.println("Please insert a valid input");

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
            oos.writeObject(new DrawEvent());

        else if(isGridPosition(string)) {
            int[] coordinates = readGridString(string);
            System.out.println(coordinates[0]);
            System.out.println(coordinates[1]);
            oos.writeObject(new PositioningEvent(coordinates[0], coordinates[1]));
        }

        else if(string.equalsIgnoreCase("A"))
            oos.writeObject(new SelectionEvent(1));

        else if(string.equalsIgnoreCase("B"))
            oos.writeObject(new SelectionEvent(2));

        else if(string.equalsIgnoreCase("pick"))
            oos.writeObject(new PickCardEvent(""));

        else if(string.equalsIgnoreCase("move") || string.equalsIgnoreCase("build") || string.equalsIgnoreCase("undo"))
            oos.writeObject(new ActionSelectEvent(string.toUpperCase()));

        else unknownInput();
    }

    private void readTwoStrings(String firstString, String secondString) throws IOException {

        if(firstString.equalsIgnoreCase("pick"))
            oos.writeObject(new PickCardEvent(secondString));

        else if(firstString.equalsIgnoreCase("end") && secondString.equalsIgnoreCase("round"))
            oos.writeObject(new ActionSelectEvent("ENDROUND"));

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
            if(isNumeric(strings[0])) oos.writeObject(new LobbySizeEvent(Integer.parseInt(strings[0])));
            else oos.writeObject(new LoginNameEvent(strings[0]));
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
            oos.writeObject(new MoveDecisionEvent(x, y));

        else if(actionString.equalsIgnoreCase("build"))
            oos.writeObject(new BuildDecisionEvent(x, y));

        else unknownInput();
    }

    private void buildEventWithLevel(String gridString, int buildLevel) throws IOException {
        int[] coordinates = readGridString(gridString);
        oos.writeObject(new BuildDecisionEvent(coordinates[0], coordinates[1], buildLevel));
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


