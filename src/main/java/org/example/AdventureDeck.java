package org.example;

import java.util.ArrayList;

public class AdventureDeck extends Deck {

    public AdventureDeck() {
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

