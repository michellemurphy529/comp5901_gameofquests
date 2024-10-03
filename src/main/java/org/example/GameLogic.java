package org.example;

public class GameLogic {

    private Deck adventureDeck;
    private Deck eventDeck;

    public GameLogic() {
        this.adventureDeck = new AdventureDeck();
        this.eventDeck = new EventDeck();
    }

    //Set Decks
    public void setAdventureDeck() {
        adventureDeck.initializeDeck();
    }
    public void setEventDeck() {
        eventDeck.initializeDeck();
    }

    //Get Decks
    public Deck getAdventureDeck() {
        return adventureDeck;
    }
    public Deck getEventDeck() {
        return eventDeck;
    }
}