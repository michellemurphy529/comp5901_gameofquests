package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class Message {
    private String content;
    private String id;
    public Message() {
    }
    public Message(String content, String id) {
        this.content = content;
        this.id = id;
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

    @Override
    public String toString() {
        return "Message{content='" + content +
                ", id='" + id + '}';
    }
}