package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class QuestMessage {
    private String content;
    private String id;
    private String sponsorFound;
    private String playerHand;
    
    public QuestMessage(String content, String id) {
        this.content = content;
        this.id = id;
    }

    //sponsor found yes or no
    public QuestMessage(String content, String id, String sponsorFound) {
        this.content = content;
        this.id = id;
        this.sponsorFound = sponsorFound;
    }

    //sponsor found and playerhand
    public QuestMessage(String content, String id, String sponsorFound, String playerHand) {
        this.content = content;
        this.id = id;
        this.sponsorFound = sponsorFound;
        this.playerHand = playerHand;
    }

    public String getContent() {return content;}
    public String getSponsorFound() {return sponsorFound;}
    public String getId(){return id;}
    public String getPlayerHand(){return playerHand;}
}
