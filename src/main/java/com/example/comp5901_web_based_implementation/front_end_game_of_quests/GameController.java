package com.example.comp5901_web_based_implementation.front_end_game_of_quests;
import java.util.ArrayList;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.comp5901_web_based_implementation.back_end_game_of_quests.*;

@Controller
public class GameController {
    Game game;
    GameData gameData;
    int numPlayers = 0;

    public GameController(GameData gameData) {
        this.game = new Game(new GameLogic(), new GameDisplay());
        this.gameData = gameData;
    }

    @MessageMapping("/connect")
    @SendTo("/topic/message")
    public Message connect(ConnectionMessage message) throws Exception {
        numPlayers++;
        //First player connects and clicks register
        if (numPlayers == 1) {
            //Set up backend Game
            game.setDecks();
            game.getAdventureDeck().shuffle();;
            game.getEventDeck().shuffle();
            game.setPlayers();
            //Feed Game info to GameData
            gameData.setAdventureDeck(game.getAdventureDeck());
            gameData.setEventDeck(game.getEventDeck());
            gameData.setCurrentPlayerInHotseat("P" + numPlayers);

        }
        if (numPlayers > 4) {
            return new Message("game started", "");
        } else {
            Thread.sleep(500);
            Player p = new Player("P" + numPlayers);
            gameData.addPlayer(p);
            return new Message("id", String.valueOf(p.getPlayerID()));
        }
    }

    @MessageMapping("/start")
    @SendTo("/topic/message")
    public StartMessage start(ConnectionMessage message) throws Exception {
        //start game and deal out initial 12 adventure cards
        game.dealInitial12AdventureCards();

        //Build player hands as strings for the javascript to display
        StringBuilder p1Hand = new StringBuilder();
        StringBuilder p2Hand = new StringBuilder();
        StringBuilder p3Hand = new StringBuilder();
        StringBuilder p4Hand = new StringBuilder();
        
        for (Player player : gameData.getPlayers()) {
            game.gameLogic.getPlayer(player.getPlayerID()).sortHand();
            ArrayList<Card> hand = game.gameLogic.getPlayer(player.getPlayerID()).getHand();
            StringBuilder cardNames = new StringBuilder();
            for (Card card : hand) {
                AdventureCard aCard = (AdventureCard) card;
                if (cardNames.length() > 0) {
                    cardNames.append(" ");
                }
                cardNames.append(aCard.displayCardName());
            }
            switch (player.getPlayerID()) {
                case "P1":
                    p1Hand = cardNames;
                    break;
                case "P2":
                    p2Hand = cardNames;
                    break;
                case "P3":
                    p3Hand = cardNames;
                    break;
                case "P4":
                    p4Hand = cardNames;
                    break;
            }
        }

        return new StartMessage("start", "P1", "P2", "P3", "P4",p1Hand.toString(), p2Hand.toString(), p3Hand.toString(), p4Hand.toString(), "0", "0", "0", "0", gameData.getCurrentPlayerInHotseat());
        }
    }