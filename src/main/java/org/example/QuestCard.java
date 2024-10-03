package org.example;

public class QuestCard extends Card {

    private final int stages;

    public QuestCard(int stages) {
        super("Q");
        this.stages = stages;
    }

    public int getStages(){
        return stages;
    }
}
