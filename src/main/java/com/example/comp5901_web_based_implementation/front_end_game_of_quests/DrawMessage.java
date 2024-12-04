package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class DrawMessage {
    private String content;
    private String eventCard;
    private String id;
    private String discardsLeft;
    private String playerHand;
    private String p1Discard;
    private String p2Discard;
    private String p3Discard;
    private String p4Discard;
    private String p1Hand;
    private String p2Hand;
    private String p3Hand;
    private String p4Hand;
    private String shieldsOrStages;
    private String stages;

    // public DrawMessage(String content, String id, String shields, String p1Discard, String p2Discard, String p3Discard, String p4Discard, String p1Hand, String p2Hand, String p3Hand, String p4Hand) {
    //     this.content = content;
    //     this.id = id;
    //     this.p1Discard = p1Discard;
    //     this.p2Discard = p2Discard;
    //     this.p3Discard = p3Discard;
    //     this.p4Discard = p4Discard;
    //     this.p1Hand = p1Hand;
    //     this.p2Hand = p2Hand;
    //     this.p3Hand = p3Hand;
    //     this.p4Hand = p4Hand;
    // }

    //Plague or Quest
    // public DrawMessage(String content, String id, String shieldsOrStages) {
    //     this.content = content;
    //     this.id = id;
    //     this.shieldsOrStages = shieldsOrStages;
    // }

    //Queen's Favour
    public DrawMessage(String content, String id, String discardsLeft, String playerHand) {
        this.content = content;
        this.id = id;
        this.discardsLeft = discardsLeft;
        this.playerHand = playerHand;

    }

    //Prosperity
    // public DrawMessage(String content, String id, )
    public DrawMessage() {

    }


    public String getContent() {return content;}
    // public String getEventCard() {return eventCard;}
    public String getId(){return id;}
    public String getDiscardsLeft(){return discardsLeft;}
    public String getPlayerHand(){return playerHand;}
    public String getP1Discard(){return p1Discard;}
    public String getP2Discard(){return p2Discard;}
    public String getP3Discard(){return p3Discard;}
    public String getP4Discard(){return p4Discard;}
    public String getP1Hand(){return p1Hand;}
    public String getP2Hand(){return p2Hand;}
    public String getP3Hand(){return p3Hand;}
    public String getP4Hand(){return p4Hand;}
    public String getShieldsOrStages(){return shieldsOrStages;}
    public String getStages(){return stages;}
}
