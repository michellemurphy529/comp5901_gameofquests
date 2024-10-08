package org.example;
import java.util.ArrayList;

public class Game {

    protected GameLogic gameLogic;

    public Game(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void setDecks() {
        gameLogic.setAdventureDeck();
        gameLogic.setEventDeck();
    }

    public Deck getAdventureDeck() {
        return gameLogic.getAdventureDeck();
    }

    public ArrayList<Card> getAdventureDiscardDeck() {
        return gameLogic.getAdventureDiscardDeck();
    }

    public Deck getEventDeck() {
        return gameLogic.getEventDeck();
    }

    public ArrayList<Card> getEventDiscardDeck() {
        return gameLogic.getEventDiscardDeck();
    }

    public void setPlayers() {
        gameLogic.setUpPlayers();
    }

    public ArrayList<Player> getPlayers() {
        return gameLogic.getPlayers();
    }
    public String[] getPlayerIDs() {
        return gameLogic.getPlayerIDs();
    }

    public void dealInitial12AdventureCards() {
        gameLogic.distribute12AdventureCards();
    }

    public Card drawAdventureCard(String playerID) {
        return gameLogic.drawCard(playerID, gameLogic.getAdventureDeck());
    }

    public Card drawEventCard(String playerID) {
        return gameLogic.drawCard(playerID, gameLogic.getEventDeck());
    }

    public void discardAdventureCard(String playerID, Card card) {
        gameLogic.discardCard(playerID, gameLogic.getAdventureDeck(), card);
    }

    public void discardEventCard(String playerID, Card card) {
        gameLogic.discardCard(playerID, gameLogic.getEventDeck(), card);
    }

    public static void main(String[] args) {
        //Initialize game
        Game game = new Game(new GameLogic());

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
