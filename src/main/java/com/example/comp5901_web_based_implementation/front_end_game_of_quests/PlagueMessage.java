package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class PlagueMessage {
    private String content;
    private String id;
    private String shields;
    
    public PlagueMessage(String content, String id, String shields) {
        this.content = content;
        this.id = id;
        this.shields = shields;
    }
    public String getContent() {return content;}
    public String getShields() {return shields;}
    public String getId(){return id;}
}
