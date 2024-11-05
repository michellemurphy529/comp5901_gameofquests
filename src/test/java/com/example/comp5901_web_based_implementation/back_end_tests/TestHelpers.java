package com.example.comp5901_web_based_implementation.back_end_tests;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.*;

public class TestHelpers {

    //Helper methods
    public void setUpForTestsBuildingQuest(Game game) {
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();
    }

    public void forcePlayerTurn(Game game, int playerNum) {
        int numberOfNextTurns = playerNum - 1;
        for (int i = 0; i < numberOfNextTurns; i++) {
            game.gameLogic.nextTurn();
        }
    }

    public void setUpForTestsSettingUpAttack(Game game) {
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();
    }

    public void setUpForTestGeneral(Game game) {
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();
    }
}
