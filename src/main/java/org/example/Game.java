package org.example;

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

    public Deck getEventDeck() {
        return gameLogic.getEventDeck();
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
