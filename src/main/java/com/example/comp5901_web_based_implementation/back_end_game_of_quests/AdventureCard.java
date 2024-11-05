package com.example.comp5901_web_based_implementation.back_end_game_of_quests;

public class AdventureCard extends Card {
    protected final int value;

    public AdventureCard(String type, int value) {
        super(type);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String displayCardName() {
        return getType() + getValue();
    }
}
