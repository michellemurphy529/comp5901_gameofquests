package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class QueenMessage {
    private String content;
    private String id;
    private String discardsLeft;
    private String playerHand;
    
    public QueenMessage(String content, String id, String discardsLeft, String playerHand) {
        this.content = content;
        this.id = id;
        this.discardsLeft = discardsLeft;
        this.playerHand = playerHand;
    }
    public String getContent() {return content;}
    public String getDiscardsLeft() {return discardsLeft;}
    public String getPlayerHand() {return playerHand;}
    public String getId(){return id;}
    
}
