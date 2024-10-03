package org.example;

import java.util.ArrayList;

public class EventDeck extends Deck {

    public EventDeck() {
    }

    @Override
    public int getSize() {
        return cards.size();
    }

    @Override
    public ArrayList<Card> getDeck() {
        return cards;
    }

    @Override
    protected void initializeDeck() {
    }
}

