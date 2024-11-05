package com.example.comp5901_web_based_implementation.front_end_game_of_quests;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.*;

@Controller
public class GameController {
    GameLogic gameLogic;
    GameData gameData;
    int numPlayers = 0;

    public GameController(GameLogic gameLogic, GameData gameData) {
        this.gameLogic = gameLogic;
        this.gameData = gameData;
    }

    @MessageMapping("/connect")
    @SendTo("/topic/message")
    public Message connect(ConnectionMessage message) throws Exception {
        numPlayers++;
        if (numPlayers == 1) {
            //Set decks
            gameLogic.setAdventureDeck();
            gameLogic.setEventDeck();
            //Shuffle decks
            Deck adventureDeck = gameLogic.getAdventureDeck();
            Deck eventDeck = gameLogic.getEventDeck();
            adventureDeck.shuffle();
            eventDeck.shuffle();
        }
        if (numPlayers > 4) {
            return new Message("game started", "");
        } else {
            Thread.sleep(500);
            Player p = new Player("P" + numPlayers);
            gameData.addPlayer(p);

            gameLogic.distribute12AdventureCards();
            return new Message("id", String.valueOf(p.getPlayerID()));
        }
    }
}