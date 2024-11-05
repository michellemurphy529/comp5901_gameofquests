package com.example.comp5901_web_based_implementation.back_end_game_of_quests;

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
