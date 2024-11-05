package com.example.comp5901_web_based_implementation.back_end_game_of_quests;
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