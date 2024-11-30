package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class DrawMessage {
    private String content;
    private String eventCard;
    private String id;
    // private String id2;
    // private String id3;
    // private String id4;
    private String p1Discard;
    private String p2Discard;
    private String p3Discard;
    private String p4Discard;
    private String p1Hand;
    private String p2Hand;
    private String p3Hand;
    private String p4Hand;
    private String shields;
    // private String shields2;
    // private String shields3;
    // private String shields4;
    private String stages;

    public DrawMessage(String content, String eventCard, String id, String shields, String p1Discard, String p2Discard, String p3Discard, String p4Discard, String p1Hand, String p2Hand, String p3Hand, String p4Hand, String stages) {
        this.content = content;
        this.eventCard = eventCard;
        this.id = id;
        // this.id2 = id2;
        // this.id3 = id3;
        // this.id4 = id4;
        this.p1Discard = p1Discard;
        this.p2Discard = p2Discard;
        this.p3Discard = p3Discard;
        this.p4Discard = p4Discard;
        this.p1Hand = p1Hand;
        this.p2Hand = p2Hand;
        this.p3Hand = p3Hand;
        this.p4Hand = p4Hand;
        this.shields = shields;
        // this.shields2 = shields2;
        // this.shields3 = shields3;
        // this.shields4 = shields4;
        this.stages = stages;
    }

    public String getContent() {return content;}
    public String getEventCard() {return eventCard;}
    public String getId(){return id;}
    // public String getId2(){return id2;}
    // public String getId3(){return id3;}
    // public String getId4(){return id4;}
    public String getP1Discard(){return p1Discard;}
    public String getP2Discard(){return p2Discard;}
    public String getP3Discard(){return p3Discard;}
    public String getP4Discard(){return p4Discard;}
    public String getP1Hand(){return p1Hand;}
    public String getP2Hand(){return p2Hand;}
    public String getP3Hand(){return p3Hand;}
    public String getP4Hand(){return p4Hand;}
    public String getShields(){return shields;}
    // public String getShields2(){return shields2;}
    // public String getShields3(){return shields3;}
    // public String getShields4(){return shields4;}
    public String getStages(){return stages;}
}
