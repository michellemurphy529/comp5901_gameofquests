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
    public void display(StringBuilder message) {
        output.println(message);
        consoleOutput.println(message);
    }
}
