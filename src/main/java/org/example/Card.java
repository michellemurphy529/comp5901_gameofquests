package org.example;

public abstract class Card {
    protected String type;

    public Card(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public abstract String displayCardName();
}