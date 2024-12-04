package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class QuestMessage {
    private String content;
    private String id;
    private String stages;
    
    public QuestMessage(String content, String id, String stages) {
        this.content = content;
        this.id = id;
        this.stages = stages;
    }
    public String getContent() {return content;}
    public String getStages() {return stages;}
    public String getId(){return id;}
}
