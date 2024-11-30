package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class ConnectionMessage {
    private String name;

    public ConnectionMessage() {
    }

    public ConnectionMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}