package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class StartMessage {
    private String content;
    private String id1;
    private String id2;
    private String id3;
    private String id4;
    private String p1Hand;
    private String p2Hand;
    private String p3Hand;
    private String p4Hand;
    private String shields1;
    private String shields2;
    private String shields3;
    private String shields4;
    private String currentHotseatPlayer;

    public StartMessage(String content) {
        this.content = content;
    }
    public StartMessage(String content, String id1, String id2, String id3, String id4, String p1Hand, String p2Hand, String p3Hand, String p4Hand, String shields1, String shields2, String shields3, String shields4, String currentHotseatPlayer) {
        this.content = content;
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
        this.id4 = id4;
        this.p1Hand = p1Hand;
        this.p2Hand = p2Hand;
        this.p3Hand = p3Hand;
        this.p4Hand = p4Hand;
        this.shields1 = shields1;
        this.shields2 = shields2;
        this.shields3 = shields3;
        this.shields4 = shields4;
        this.currentHotseatPlayer = currentHotseatPlayer;
    }

    public String getContent() {
        return content;
    }
    public String getId1(){return id1;}
    public String getId2(){return id2;}
    public String getId3(){return id3;}
    public String getId4(){return id4;}
    public String getP1Hand(){return p1Hand;}
    public String getP2Hand(){return p2Hand;}
    public String getP3Hand(){return p3Hand;}
    public String getP4Hand(){return p4Hand;}
    public String getshields1(){return shields1;}
    public String getshields2(){return shields2;}
    public String getshields3(){return shields3;}
    public String getshields4(){return shields4;}
    public String getCurrentPlayerInHotseat(){return currentHotseatPlayer;}
}
