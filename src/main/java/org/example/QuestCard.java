package org.example;

public class QuestCard extends EventCard {

    private final int stages;

    public QuestCard(int stages) {
        super("Q");
        this.stages = stages;
    }

    public int getStages(){
        return stages;
    }

    @Override
    public String displayCardName() {
        return getType() + getStages();
    }
}
