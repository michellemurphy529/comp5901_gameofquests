package org.example;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GameDisplay {
    protected StringWriter stringWriter;
    protected PrintWriter output;
    protected PrintWriter consoleOutput;
    public String lastInput;

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
    public void displayPlayerHand(Player player) {
        StringBuilder displayPlayerHand = new StringBuilder("\n" + player.getPlayerID() + " hand: ");
        ArrayList<Card> playerHand = player.getHand();
        for (int i = 0; i < playerHand.size(); i++) {
            displayPlayerHand.append(playerHand.get(i).displayCardName());
            if (i < playerHand.size() - 1) {
                displayPlayerHand.append(" ");
            }
        }
        display(displayPlayerHand);
    }
    public void displayTrimmedHandMessage() {
        StringBuilder trimmedHandMessage = new StringBuilder("\nTrimming Complete... Here is your new hand!");
        display(trimmedHandMessage);
    }
    public void display(StringBuilder message) {
        output.println(message);
        consoleOutput.println(message);
    }
    //Quest output methods
    public void displayBuiltQuest(HashMap<Integer, ArrayList<Card>> questBuilt) {
        StringBuilder questBuiltMessage = new StringBuilder("\nStage set up is completed...\n\n");

        for (HashMap.Entry<Integer, ArrayList<Card>> stage : questBuilt.entrySet()) {
            //Get Stage number from Key in hashmap
            int stageNumber = stage.getKey();
            //Get cards to of Stage from Value arraylist of cards
            ArrayList<Card> cards = stage.getValue();

            questBuiltMessage.append("STAGE ").append(stageNumber).append(":\n");

            //Loop through to get the cards and the value for each stage
            StringBuilder cardsString = new StringBuilder();
            int stageValue = 0;
            for (Card card : cards) {
                AdventureCard adventureCard = (AdventureCard) card;
                //Add to stage value running total
                stageValue += adventureCard.getValue();
                cardsString.append(adventureCard.displayCardName()).append(" ");
            }

            //Appending all strings together
            questBuiltMessage.append("Cards = ").append(cardsString.toString().trim()).append("\n")
                    .append("Value = ").append(stageValue).append("\n\n");
        }
        display(questBuiltMessage);
    }

    //Input methods
    public void promptForDiscardCards(int n) {
        StringBuilder promptDiscard = new StringBuilder("\nDiscard " + n + " cards\n\n" +
                "Type out cards in the format as it appears in your hand\n" +
                "For Example: 'F5' (WITHOUT '' around the card name)\n" +
                "then press the <return> key:");
        display(promptDiscard);
    }
    public String getDiscardInput(Scanner userInput) {
        String input = userInput.nextLine();
        lastInput = input;
        return input;
    }
    //Quest input methods
    public boolean getUserInputBuildStage(Scanner userInput) {
        boolean quitEntered = false;

        while(!quitEntered) {
            String input = userInput.nextLine();
            if(input.equalsIgnoreCase("quit")) {
                quitEntered = true;
            }
        }
        return quitEntered;
    }
}
