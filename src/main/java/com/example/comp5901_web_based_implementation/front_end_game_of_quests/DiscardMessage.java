package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class DiscardMessage {
    private String content;
    // private String eventCard;
    private String id;
    private String discardsLeft;
    private String playerHand;
    
    public DiscardMessage(String content, String id, String discardsLeft, String playerHand) {
        this.content = content;
        // this.eventCard = eventCard;
        this.id = id;
        this.discardsLeft = discardsLeft;
        this.playerHand = playerHand;
    }

    public String getContent() {return content;}
    // public String getEventCard(){return eventCard;}
    public String getId(){return id;}
    public String getDiscardsLeft(){return discardsLeft;}
    public String getPlayerHand(){return playerHand;}
}
