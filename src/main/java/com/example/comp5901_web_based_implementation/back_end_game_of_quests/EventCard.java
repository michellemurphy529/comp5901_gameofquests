package com.example.comp5901_web_based_implementation.back_end_game_of_quests;

public class EventCard extends Card {
    public EventCard(String type) {
        super(type);
    }
    @Override
    public String displayCardName() {
        return getType();
    }
}
