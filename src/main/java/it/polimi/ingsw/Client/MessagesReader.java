package it.polimi.ingsw.Client;

public class MessagesReader {

    MessagesHandler messagesHandler;

    public MessagesReader(MessagesHandler messagesHandler) {
        this.messagesHandler = messagesHandler;
    }

    public void read(int messageID) {
        int firstDigit = firstDigit(messageID);
        String message = "";

        switch (firstDigit) {

            case (1) : {
                switch (messageID) {
                    case (101): { message = "Where do you want to move?"; break;}
                    case (102): { message = "Where do you want to build?"; break; }
                    case (103): { message = "Choose your worker"; break; }
                    case (104): { message = "Choose another worker"; break; }
                    case (105): { message = "Draw"; break; }
                    case (106): { message = "Pick your card"; break; }
                    case (107): { message = "Pick another card"; break; }
                    case (108): { message = "Position your fist worker"; break; }
                    case (109): { message = "Position your second worker"; break; }
                    case (110): { message = "Select another tile"; break; }
                    case (111): { message = "Select the action you want to perform"; break; }
                    case (112): { message = "What's your name?"; break; }
                    case (113): { message = "Insert lobby players number"; break; }
                }
                messagesHandler.sendRequest(message);
                break;
            }

            //Successes
            case (2) : {
                switch (messageID) {
                    case (201): { message = "All players joined the lobby. The game has started"; break; }
                    case (202): { message = "Worker selected!"; break; }
                }
                messagesHandler.sendSuccess(message);
                break;
            }

            //Messages
            case (3) : {
                switch (messageID) {
                    case (301): { message = "Your worker can't play"; break; }
                    case (302): { message = "Waiting for other players"; break; }
                    case (303): { message = "No lobbies found. Create one"; break; }
                    case (304): { message = "Waiting for another player"; break; }
                }
                messagesHandler.sendMessage(message);
                break;
            }

            //Errors
            case (4) : {
                switch (messageID) {
                    case (401): { message = "Undo no longer available"; break; }
                    case (402): { message = "Invalid action"; break; }
                    case (403): { message = "You can't move in that tile"; break; }
                    case (404): { message = "You can't build in that tile"; break; }
                    case (405): { message = "Please, specify the name of the card you want to pick"; break; }
                    case (406): { message = "Card already picked"; break; }
                    case (407): { message = "Invalid card"; break; }
                    case (408): { message = "Invalid tile"; break; }
                    case (409): { message = "It's not your worker"; break; }
                    case (410): { message = "This worker is unavailable"; break; }
                    case (411): { message = "It's not your turn"; break; }
                    case (412): { message = "Invalid input"; break; }
                    case (413): { message = "Another player is creating the lobby"; break; }
                    case (414): { message = "The lobby is full"; break; }
                    case (415): { message = "Invalid input. Name required"; break; }
                    case (416): { message = "This name has already been chosen"; break; }
                    case (417): { message = "Invalid input. Number required"; break; }
                    case (418): { message = "Invalid lobby size"; break; }
                    case (419): { message = "The game hasn't started yet"; }
                }
                messagesHandler.sendError(message);
                break;
            }
        }
    }

    public void lobbyInfo(String lobbyName, int lobbySize) { messagesHandler.sendLobbyInfo(lobbyName, lobbySize); }

    private int firstDigit(int n) {
        while (n >= 10) n /= 10;
        return n;
    }
}
