package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class DiscardMessage {
    private String content;
    private String id;
    private String discardsLeft;
    
    public DiscardMessage(String content, String id, String discardsLeft) {
        this.content = content;
        this.id = id;
        this.discardsLeft = discardsLeft;
    }

    public String getContent() {return content;}
    public String getId(){return id;}
    public String getDiscardsLeft(){return discardsLeft;}
}
