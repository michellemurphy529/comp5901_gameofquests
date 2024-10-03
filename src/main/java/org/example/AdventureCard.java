package org.example;

public class AdventureCard extends Card {
    protected final int value;

    public AdventureCard(String type, int value) {
        super(type);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
