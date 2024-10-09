package org.example;
import java.util.ArrayList;

public class Game {

    protected GameLogic gameLogic;
    protected GameDisplay gameDisplay;

    public Game(GameLogic gameLogic, GameDisplay gameDisplay) {
        this.gameLogic = gameLogic;
        this.gameDisplay = gameDisplay;
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

        //Next Player Logic
        //Next Turn invoked
        gameLogic.nextTurn();
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
