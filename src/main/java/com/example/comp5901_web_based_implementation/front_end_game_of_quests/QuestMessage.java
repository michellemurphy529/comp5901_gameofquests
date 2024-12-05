package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class QuestMessage {
    private String content;
    private String id;
    private String sponsorFound;
    private String playerHand;
    private String stageBeingBuilt;
    
    public QuestMessage(String content, String id) {
        this.content = content;
        this.id = id;
    }

    //Sponsor found
    public QuestMessage(String content, String id, String sponsorFound) {
        this.content = content;
        this.id = id;
        this.sponsorFound = sponsorFound;
    }

    //Sponsor found
    public QuestMessage(String content, String id, String sponsorFound, String stageBeingBuilt) {
        this.content = content;
        this.id = id;
        this.sponsorFound = sponsorFound;
        this.stageBeingBuilt = stageBeingBuilt;
    }

    //Sponsor builds Quest
    public QuestMessage(String content, String id, String sponsorFound, String playerHand, String stageBeingBuilt) {
        this.content = content;
        this.id = id;
        this.sponsorFound = sponsorFound;
        this.playerHand = playerHand;
        this.stageBeingBuilt = stageBeingBuilt;
    }

    public String getContent() {return content;}
    public String getSponsorFound() {return sponsorFound;}
    public String getId(){return id;}
    public String getPlayerHand(){return playerHand;}
    public String getStageBeingBuilt(){return stageBeingBuilt;}
}
