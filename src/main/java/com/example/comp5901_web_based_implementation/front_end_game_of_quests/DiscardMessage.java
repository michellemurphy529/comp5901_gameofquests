package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class DiscardMessage {
    //Plague & QueensFavor
    private String content;
    private String id;
    private String discardsLeft;
    private String playerHand;

    //Prosperity
    private String isDone;

    public DiscardMessage(String content, String id, String discardsLeft, String playerHand) {
        this.content = content;
        this.id = id;
        this.discardsLeft = discardsLeft;
        this.playerHand = playerHand;
    }

    //Prosperity Discards
    public DiscardMessage(String content, String isDone, String id, String discardsLeft, String playerHand) {
        this.content = content;
        this.isDone = isDone;
        this.id = id;
        this.discardsLeft = discardsLeft;
        this.playerHand = playerHand;
    }

    //Plague & QueensFavor
    public String getContent() {return content;}
    public String getId(){return id;}
    public String getDiscardsLeft(){return discardsLeft;}
    public String getPlayerHand(){return playerHand;}

    //Prosperity Discards
    public String getIsDone(){return isDone;}
}
