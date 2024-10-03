package org.example;

public class Game {

    protected GameLogic gameLogic;

    public Game(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void setDecks() {
    }

    public Deck getAdventureDeck() {
        return gameLogic.getAdventureDeck();
    }

    public Deck getEventDeck() {
        return gameLogic.getEventDeck();
    }

    public static void main(String[] args) {
    }
}
