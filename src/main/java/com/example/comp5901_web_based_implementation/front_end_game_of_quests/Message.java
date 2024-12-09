package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class Message {
    private String content;
    private String id;
    //Asking players to participate
    private String stage;
    private String playerHand;
    private String stageLosers;
    private String stageWinners;

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
    public Message(String content, String id, String stage, String playerHand) {
        this.content = content;
        this.id = id;
        this.stage = stage;
        this.playerHand = playerHand;
    }
    //Asking players to participate after a stage is complete and there are enough players to continue
    public Message(String content, String id, String stage, String stageLosers, String stageWinners) {
        this.content = content;
        this.id = id;
        this.stage = stage;
        this.stageLosers = stageLosers;
        this.stageWinners = stageWinners;
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
    public String getPlayerHand() {return playerHand;}
    public String getStageLosers() {return stageLosers;}
    public String getStageWinners() {return stageWinners;}
    

    @Override
    public String toString() {
        return "Message{content='" + content +
                ", id='" + id + '}';
    }
}