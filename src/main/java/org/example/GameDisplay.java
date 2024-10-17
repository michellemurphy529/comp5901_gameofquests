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
        message.append("\n");
        display(message);
    }
    public void displayTerminationMessage() {
        StringBuilder terminationMessage = new StringBuilder("Game is terminated... Goodbye!");
        display(terminationMessage);
    }
    public void displayNoWinners() {
        StringBuilder noWinnersMessage = new StringBuilder("There are no winner(s).").append("\n")
                .append("Game of Quest's continues...").append("\n");
        display(noWinnersMessage);
    }
    public void displayTurn(String playerID) {
        StringBuilder noWinnersMessage = new StringBuilder(playerID).append("'s Turn:").append("\n");
        display(noWinnersMessage);
    }
    public void drawingEventCardMessage() {
        StringBuilder drawingEventCardMessage = new StringBuilder("Drawing Event Card...");
        display(drawingEventCardMessage);
    }
    public void displayCardDrawn(Card card) {
        StringBuilder drawnCardMessage = new StringBuilder("You drew: ").append(card.displayCardName()).append("\n");
        display(drawnCardMessage);
    }
    public void displayPlayerHand(Player player) {
        StringBuilder displayPlayerHand = new StringBuilder(player.getPlayerID()).append(" hand: ");
        ArrayList<Card> playerHand = player.getHand();
        for (int i = 0; i < playerHand.size(); i++) {
            displayPlayerHand.append(playerHand.get(i).displayCardName());
            if (i < playerHand.size() - 1) {
                displayPlayerHand.append(" ");
            }
        }
        displayPlayerHand.append("\n");
        display(displayPlayerHand);
    }
    public void displayTrimmedHandMessage() {
        StringBuilder trimmedHandMessage = new StringBuilder("Trimming Complete... Here is your new hand!")
                .append("\n");
        display(trimmedHandMessage);
    }
    public void display(StringBuilder message) {
        output.println(message);
        consoleOutput.println(message);
    }
    //Quest output methods
    public void displayBuiltQuest(HashMap<Integer, ArrayList<Card>> questBuilt) {
        StringBuilder questBuiltMessage = new StringBuilder("Stage set up is completed...").append("\n")
                .append("\n");

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
                    .append("Value = ").append(stageValue).append("\n").append("\n");
        }
        display(questBuiltMessage);
    }
    public void displayBuildingQuestMessage(int numberOfStages) {
        StringBuilder buildingQuestMessage = new StringBuilder("Building a Quest with ").append(numberOfStages).
                append(" Stages...").append("\n");
        display(buildingQuestMessage);
    }
    public void displayBuildingStageMessage(int stageNumber) {
        StringBuilder currentStageMessage = new StringBuilder("Building Stage ").append(stageNumber).
                append(":").append("\n");
        display(currentStageMessage);
    }
    public void displayPromptForSelectingStageCards() {
        StringBuilder selectStagesCards = new StringBuilder("Select 1 Foe card and 0 or more non-repeating Weapon " +
                "cards from your hand to build this stage.").append("\n").append
                ("Enter 'Quit' to end this stage setup.").append("\n");
        display(selectStagesCards);
    }
    public void displayFoeAlreadyInStageMessage() {
        StringBuilder foeInStageMessage = new StringBuilder("There is already a Foe card in this stage. Try Again.")
                .append("\n");
        display(foeInStageMessage);
    }
    public void displayRepeatedWeaponCardMessage() {
        StringBuilder repeatedWeaponCard = new StringBuilder("There is already that same Weapon card in this stage. Try Again.")
                .append("\n");
        display(repeatedWeaponCard);
    }
    public void addedCardToStageMessage(String cardString) {
        StringBuilder addedToStageMessage = new StringBuilder(cardString).append(" added to Stage...");
        display(addedToStageMessage);
    }
    public void displayStageCards(ArrayList<String> stageCards, int stage) {
        StringBuilder displayUpdatedStageCards = new StringBuilder("Stage ").append(stage).append(" Card(s): ");
        for (int i = 0; i < stageCards.size(); i++) {
            displayUpdatedStageCards.append(stageCards.get(i));
            if (i < stageCards.size() - 1) {
                displayUpdatedStageCards.append(" ");
            }
        }
        displayUpdatedStageCards.append("\n");
        display(displayUpdatedStageCards);
    }
    public void displayStageCannotBeEmptyMessage() {
        StringBuilder stageCannotBeEmptyMessage = new StringBuilder("A stage cannot be empty").append("\n");
        display(stageCannotBeEmptyMessage);
    }
    public void displayInsufficientValueMessage() {
        StringBuilder insufficientValueMessage = new StringBuilder("Insufficient value for this stage").append("\n");
        display(insufficientValueMessage);
    }
    //Attack Output methods
    public void displaySettingUpAttackForPlayer() {
        StringBuilder settingUpAttackMessage = new StringBuilder("Setting up an Attack...").append("\n");
        display(settingUpAttackMessage);
    }
    public void displayPromptAttackSelectCards() {
        StringBuilder selectAttackCards = new StringBuilder("Select 0 or more non-repeating Weapon cards from " +
                "your hand to build this attack.").append("\n").append
                ("Enter 'Quit' to end the attack setup.").append("\n");
        display(selectAttackCards);
    }
    public void addedCardToAttackMessage(String cardString) {
        StringBuilder addedToAttackMessage = new StringBuilder(cardString).append(" added to Attack...");
        display(addedToAttackMessage);
    }
    public void displayAttackCards(ArrayList<Card> attackCards) {
        StringBuilder displayAttackCards = new StringBuilder("Attack Card(s): ");
        for (int i = 0; i < attackCards.size(); i++) {
            displayAttackCards.append(attackCards.get(i).displayCardName());
            if (i < attackCards.size() - 1) {
                displayAttackCards.append(" ");
            }
        }
        displayAttackCards.append("\n");
        display(displayAttackCards);
    }
    public void displayAttackSetUpCompleted() {
        StringBuilder attackCompletedMessage = new StringBuilder("Attack set up is completed...");
        display(attackCompletedMessage);
    }
    public void displayParticipantAttack(ArrayList<Card> participantAttack) {
        StringBuilder participantAttackCompleteAndDisplayed = new StringBuilder("Your Attack: ");
        if(!participantAttack.isEmpty()) {
            for (int i = 0; i < participantAttack.size(); i++) {
                participantAttackCompleteAndDisplayed.append(participantAttack.get(i).displayCardName());
                if (i < participantAttack.size() - 1) {
                    participantAttackCompleteAndDisplayed.append(" ");
                }
            }
        }else {
            participantAttackCompleteAndDisplayed.append("No attack");
        }
        participantAttackCompleteAndDisplayed.append("\n");
        display(participantAttackCompleteAndDisplayed);
    }
    //Quest Output Methods
    public void promptForSponsorship() {
        StringBuilder sponsorshipPromptMessage = new StringBuilder("Would you like to sponsor this Quest?").append("\n")
                .append("Type 'yes' or 'no':").append("\n");
        display(sponsorshipPromptMessage);
    }
    public void displaySponsorshipAccepted() {
        StringBuilder sponsorMessage = new StringBuilder("You have accepted to be the Sponsor!").append("\n");
        display(sponsorMessage);
    }
    public void displaySponsorshipNotAccepted() {
        StringBuilder notSponsorMessage = new StringBuilder("You have declined Sponsorship").append("\n")
                .append("Now asking other players...").append("\n");
        display(notSponsorMessage);
    }
    public void skippingForSponsorship(String playerID) {
        StringBuilder cannotSponsor = new StringBuilder(playerID).append(" you cannot build a valid Quest.").append("\n")
                .append("You are being skipped.").append("\n");
        display(cannotSponsor);
    }
    public void askingPlayerToSponsor(String playerID) {
        StringBuilder askingToSponsor = new StringBuilder("Asking ").append(playerID).append(":");
        display(askingToSponsor);
    }
    public void displayNoSponsor() {
        StringBuilder noSponsor = new StringBuilder("No sponsor for Quest");
        display(noSponsor);
    }
    public void displayQuestEnded() {
        StringBuilder questEnded = new StringBuilder("Quest has ended.").append("\n");
        display(questEnded);

    }
    public void displayQuestBegins() {
        StringBuilder questBegins = new StringBuilder("The Quest Begins!");
        display(questBegins);
    }
    public void displayEligiblePlayers(ArrayList<String> eligiblePlayers, int stageNumber) {
        StringBuilder displayPlayers = new StringBuilder("Eligible Players for Stage ").append(stageNumber)
                .append(": ");
        for (int i = 0; i < eligiblePlayers.size(); i++) {
            displayPlayers.append(eligiblePlayers.get(i));
            if (i < eligiblePlayers.size() - 1) {
                displayPlayers.append(" ");
            }
        }
        displayPlayers.append("\n");
        display(displayPlayers);
    }
    public void promptPlayersToParticipateInStage(String playerID) {
        StringBuilder promptParticipate = new StringBuilder("Asking ").append(playerID).append(":")
                .append("\n").append("Would you like to participate in the current stage?").append("\n")
                .append("Type 'yes' or 'no':").append("\n");
        display(promptParticipate);
    }
    public void displayNoParticipants() {
        StringBuilder noParticipants = new StringBuilder("No Participants for Current Stage...");
        display(noParticipants);
    }
    //Hotseat methods
    public void promptToLeaveHotSeat() {
        StringBuilder leaveHotseatMessage = new StringBuilder("Your turn is now over...").append("\n").
                append("Press the <RETURN> key to leave the hotseat:").append("\n");
        display(leaveHotseatMessage);
    }
    public String getLeaveHotseatInput(Scanner userInput) {
        return userInput.nextLine();
    }
    public void flushDisplay() {
        for (int i = 0; i < 50; i++) {
            output.println();
            consoleOutput.println();
        }
    }
    public void displayPlayerIDInHotSeat(String playerID) {
        StringBuilder playerIDForTurnInHotseat = new StringBuilder(playerID).append(" is now in the hotseat.")
                .append("\n");
        display(playerIDForTurnInHotseat);
    }

    //Input methods
    public void promptForDiscardCards(int n) {
        StringBuilder promptDiscard = new StringBuilder("Discard ").append(n).append(" cards")
                .append("\n\n").append("Type out cards in the format as it appears in your hand").append("\n")
                .append("For Example: 'F5' (WITHOUT '' around the card name)").append("\n")
                .append("then press the <return> key:").append("\n");
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
    public String displayPromptSelectCardForStage(Scanner userInput) {
        String input = userInput.nextLine();
        lastInput = input;
        return input.trim();
    }
    public void flush() {
        stringWriter.getBuffer().setLength(0);
    }
}
