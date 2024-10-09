package org.example;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class GameDisplay {
    protected StringWriter stringWriter;
    protected PrintWriter output;
    protected PrintWriter consoleOutput;

    public GameDisplay() {
        this.stringWriter = new StringWriter();
        this.output = new PrintWriter(stringWriter);
        this.consoleOutput = new PrintWriter(System.out, true);
    }
    public String getOutput() {
        return stringWriter.toString();
    }
    public void displayWinners(ArrayList<Player> winners) {
        StringBuilder message = new StringBuilder("Winner(s) with 7 or more shields are: ");
        for (int i = 0; i < winners.size(); i++) {
            message.append(winners.get(i).getPlayerID());
            if (i < winners.size() - 1) {
                message.append(", ");
            }
        }
        display(message);
    }
    public void displayTerminationMessage() {
        StringBuilder terminationMessage = new StringBuilder("Game is terminated... Goodbye!");
        display(terminationMessage);
    }
    public void displayNoWinners() {
        StringBuilder noWinnersMessage = new StringBuilder("There are no winner(s).\nGame of Quest's continues...");
        display(noWinnersMessage);
    }
    public void displayTurn(String playerID) {
        StringBuilder noWinnersMessage = new StringBuilder("\n" + playerID + "'s Turn:\n");
        display(noWinnersMessage);
    }
    public void drawingEventCardMessage() {
        StringBuilder drawingEventCardMessage = new StringBuilder("Drawing Event Card...");
        display(drawingEventCardMessage);
    }
    public void displayCardDrawn(Card card) {
        StringBuilder drawnCardMessage = new StringBuilder("You drew: " + card.displayCardName() + "\n");
        display(drawnCardMessage);
    }
    public void display(StringBuilder message) {
        output.println(message);
        consoleOutput.println(message);
    }
}
