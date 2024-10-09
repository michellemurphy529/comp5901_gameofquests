package org.example;

public class EventCard extends Card {
    public EventCard(String type) {
        super(type);
    }
    @Override
    public String displayCardName() {
        return getType();
    }
}
