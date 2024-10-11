package org.example;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    protected GameLogic gameLogic;
    protected GameDisplay gameDisplay;
    protected Scanner input;

    public Game(GameLogic gameLogic, GameDisplay gameDisplay) {
        this.gameLogic = gameLogic;
        this.gameDisplay = gameDisplay;
        this.input = new Scanner(System.in);
    }
    //Forcing Test Inputs
    public void setInput(Scanner overrideInput) {
        this.input = overrideInput;
    }
    public void setDecks() {
        gameLogic.setAdventureDeck();
        gameLogic.setEventDeck();
    }
    //Get Decks
    public Deck getAdventureDeck() {
        return gameLogic.getAdventureDeck();
    }
    public Deck getEventDeck() {
        return gameLogic.getEventDeck();
    }
    //Set Players
    public void setPlayers() {
        gameLogic.setUpPlayers();
    }
    //Get Players
    public ArrayList<Player> getPlayers() {
        return gameLogic.getPlayers();
    }
    //Get Player IDs
    public String[] getPlayerIDs() {
        return gameLogic.getPlayerIDs();
    }
    //Deal first 12 cards
    public void dealInitial12AdventureCards() {
        gameLogic.distribute12AdventureCards();
    }
    //Draw Cards
    public Card drawAdventureCard(String playerID) {
        return gameLogic.drawCard(playerID, gameLogic.getAdventureDeck());
    }
    public Card drawEventCard(String playerID) {
        return gameLogic.drawCard(playerID, gameLogic.getEventDeck());
    }
    //Discard Cards
    public void discardAdventureCard(String playerID, Card card) {
        gameLogic.discardCard(playerID, gameLogic.getAdventureDeck(), card);
    }
    public void discardEventCard(String playerID, Card card) {
        gameLogic.discardCard(playerID, gameLogic.getEventDeck(), card);
    }
    //Get Current Player Turn
    public Player getCurrentPlayer() {
        return gameLogic.getCurrentPlayer();
    }
    //Get Current player hand
    public void displayCurrentPlayerHand() {
        gameLogic.sortPlayerHand(getCurrentPlayer());
        gameDisplay.displayPlayerHand(getCurrentPlayer());
    }
    public void displayPlayerHand(String playerID) {
        gameLogic.sortPlayerHand(gameLogic.getPlayer(playerID));
        gameDisplay.displayPlayerHand(gameLogic.getPlayer(playerID));
    }
    public void playTurn() {
        //Logic for Current Player Turn
        //Display to user which Player's turn it is
        String playerID = getCurrentPlayer().getPlayerID();
        gameDisplay.displayTurn(playerID);
        //Draw Event Card on next Player's turn
        gameDisplay.drawingEventCardMessage();
        Card cardDrawn = drawEventCard(playerID);
        //Display Card to user
        gameDisplay.displayCardDrawn(cardDrawn);

        //Plague Card Drawn
        if(cardDrawn.getType().equals("Plague")) {
            gameLogic.carryOutPlagueAction();
        }
        //Queen's Favor Card Drawn
        if(cardDrawn.getType().equals("Queen's Favor")) {
            //Current player draws 2 Adventure Cards
            dealNumberOfAdventureCardsToPlayer(playerID, 2);
            //Discard Event Card immediately before possibility of Trimming Hand occurs
            discardEventCard(playerID, cardDrawn);
            //Possibly Trims their hand
            determineIfPlayerNeedsToTrimHand(playerID);
        }
        //Prosperity Card Drawn
        if(cardDrawn.getType().equals("Prosperity")) {
            //All Players draw 2 Adventure Cards
            for (Player player : getPlayers()) {
                dealNumberOfAdventureCardsToPlayer(player.getPlayerID(), 2);
            }
            //Discard Event Card immediately before possibility of Trimming Hand occurs
            discardEventCard(playerID, cardDrawn);
            //Possibly Trims all players hands
            for (Player player : getPlayers()) {
                determineIfPlayerNeedsToTrimHand(player.getPlayerID());
            }
        }

        //Next Player Logic
        //Next Turn invoked
        gameLogic.nextTurn();
    }
    public void dealNumberOfAdventureCardsToPlayer(String playerID, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            drawAdventureCard(playerID);
        }
    }
    public ArrayList<Player> getWinners() {
        return gameLogic.determineWinners();
    }
    public void displayWinnersAndTerminate(ArrayList<Player> winners) {
        gameDisplay.displayWinners(winners);
        gameDisplay.displayTerminationMessage();
    }
    public void displayNoWinners() {
        gameDisplay.displayNoWinners();
    }
    public void processEndOfQuest() {
        ArrayList<Player> winners = getWinners();
        if(winners.isEmpty()){
            displayNoWinners();
            playTurn();
        }else{
            displayWinnersAndTerminate(winners);
        }
    }
    public Card getLastCardDrawn() {
        return gameLogic.getLastCardDrawn();
    }
    public int computeNumberOfCardsToDiscard(String playerID) {
        return gameLogic.getNumberOfCardsToDiscard(playerID);
    }
    public void displayTrimmedHand(String playerID) {
        //Calling Trim hand message
        gameDisplay.displayTrimmedHandMessage();
        //Sort player hand who has had their trim completed
        gameLogic.sortPlayerHand(gameLogic.getPlayer(playerID));
        //Display trimmed hand of player
        gameDisplay.displayPlayerHand(gameLogic.getPlayer(playerID));
    }
    public void determineIfPlayerNeedsToTrimHand(String playerID) {
        int n = computeNumberOfCardsToDiscard(playerID);
        //Trim player hand
        if(n > 0) {
            trimHand(playerID, n);
        }
    }
    public void trimHand(String playerID, int n) {
        //Display current Player Hand
        displayPlayerHand(playerID);
        int numberOfCardsToDiscard = n;

        //Begin Trimming loop
        for (int i = 0; i < numberOfCardsToDiscard; i++) {
            //Display Prompt for player to discard n cards
            gameDisplay.promptForDiscardCards(n);
            String cardToDiscard = gameDisplay.getDiscardInput(input);
            gameLogic.removeCardsAndDiscard(cardToDiscard, playerID);
            n -= 1;
        }
        displayTrimmedHand(playerID);
    }

    public static void main(String[] args) {
        //Initialize game
        Game game = new Game(new GameLogic(), new GameDisplay());

        //Set up decks
        game.setDecks();

        //Shuffle decks
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();

        adventureDeck.shuffle();
        eventDeck.shuffle();

        //Set Players
        game.setPlayers();
        //Distribute 12 Adventure Cards to each player
        game.dealInitial12AdventureCards();
    }
}
