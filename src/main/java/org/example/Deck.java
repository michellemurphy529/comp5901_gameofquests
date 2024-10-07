package org.example;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Deck {
    protected ArrayList<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public abstract int getSize();
    public abstract ArrayList<Card> getDeck();
    protected abstract void initializeDeck();

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public abstract Card drawCard();
}
