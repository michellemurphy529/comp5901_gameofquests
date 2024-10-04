package org.example;
import java.util.ArrayList;

public class Game {

    protected GameLogic gameLogic;
    protected ArrayList<Player> players;

    public Game(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        this.players = new ArrayList<Player>();
        setUpPlayers();
    }

    public void setDecks() {
        gameLogic.setAdventureDeck();
        gameLogic.setEventDeck();
    }

    public Deck getAdventureDeck() {
        return gameLogic.getAdventureDeck();
    }

    public Deck getEventDeck() {
        return gameLogic.getEventDeck();
    }

    private void setUpPlayers() {
    }

    public ArrayList<Player> getPlayers() {
        return players;
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
    }
}
