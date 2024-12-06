package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class Message {
    private String content;
    private String id;
    //Asking players to participate
    private String stage;
    public Message() {
    }
    public Message(String content, String id) {
        this.content = content;
        this.id = id;
    }
    //Asking players to participate
    public Message(String content, String id, String stage) {
        this.content = content;
        this.id = id;
        this.stage = stage;
    }
    public Message(String content) {
        this.content = content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setID(String id) {
        this.id = id;
    }
    public String getContent() {return content;}
    public String getID() {return id;}
    //Asking players to participate
    public String getStage() {return stage;}

    @Override
    public String toString() {
        return "Message{content='" + content +
                ", id='" + id + '}';
    }
}