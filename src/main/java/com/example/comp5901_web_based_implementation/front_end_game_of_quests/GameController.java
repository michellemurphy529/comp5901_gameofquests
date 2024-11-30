package com.example.comp5901_web_based_implementation.front_end_game_of_quests;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //helper
    private Map<String, StringBuilder> generatePlayerHands(GameLogic gameLogic, List<Player> players) {
        Map<String, StringBuilder> playerHands = new HashMap<>();

        for (Player player : players) {
            gameLogic.getPlayer(player.getPlayerID()).sortHand();
            ArrayList<Card> hand = gameLogic.getPlayer(player.getPlayerID()).getHand();
            StringBuilder cardNames = new StringBuilder();
            for (Card card : hand) {
                AdventureCard aCard = (AdventureCard) card;
                if (cardNames.length() > 0) {
                    cardNames.append(" ");
                }
                cardNames.append(aCard.displayCardName());
            }
            playerHands.put(player.getPlayerID(), cardNames);
        }

        return playerHands;
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

        //convert player hands to strings
        Map<String, StringBuilder> playerHands = generatePlayerHands(game.gameLogic, gameData.getPlayers());
        StringBuilder p1Hand = playerHands.getOrDefault("P1", new StringBuilder());
        StringBuilder p2Hand = playerHands.getOrDefault("P2", new StringBuilder());
        StringBuilder p3Hand = playerHands.getOrDefault("P3", new StringBuilder());
        StringBuilder p4Hand = playerHands.getOrDefault("P4", new StringBuilder());

        return new StartMessage("start", "P1", "P2", "P3", "P4",p1Hand.toString(), p2Hand.toString(), p3Hand.toString(), p4Hand.toString(), "0", "0", "0", "0", gameData.getCurrentPlayerInHotseat());
    }

    @MessageMapping("/drawCard")
    @SendTo("/topic/message")
    public DrawMessage drawCard(ConnectionMessage message) throws Exception {
        String id = message.getName();
        System.out.println("Player ID: " + id);
        
        Card cardDrawn = game.drawEventCard(id);
        String eventCard = cardDrawn.getType();

        String stages = "0";
        if(eventCard.equals("Q")) {
            eventCard = cardDrawn.displayCardName();
            stages = cardDrawn.displayCardName().substring(1);
            System.out.println("Quest card drawn with " + stages + " stages");
        }
        game.discardEventCard(id, cardDrawn);
        String shields = "0";
        String p1Discard = "0";
        String p2Discard = "0";
        String p3Discard = "0";
        String p4Discard = "0";

        switch (eventCard) {
            case "Plague":
                game.gameLogic.carryOutPlagueAction();
                shields = String.valueOf(game.gameLogic.getPlayer(id).getShieldCount());
                break;
            case "Queen's Favor":
                eventCard = "QueensFavor";
                game.gameLogic.dealNumberAdventureCards(id, 2);
                game.gameLogic.getPlayer(id).sortHand();
                int cardsToDiscard = game.computeNumberOfCardsToDiscard(id);
                if (cardsToDiscard > 0) {
                    switch (id) {
                        case "P1":
                            p1Discard = String.valueOf(game.computeNumberOfCardsToDiscard(id));
                            break;

                        case "P2":
                            p2Discard = String.valueOf(game.computeNumberOfCardsToDiscard(id));
                            break;

                        case "P3":
                            p3Discard = String.valueOf(game.computeNumberOfCardsToDiscard(id));
                            break;

                        case "P4":
                            p4Discard = String.valueOf(game.computeNumberOfCardsToDiscard(id));
                            break;
                    }
                }
                break;
            case "Prosperity":
                game.gameLogic.dealAllPlayersAdventureCards(game.getPlayerIDs(), 2);
                for(String playerID : game.getPlayerIDs()) {
                    game.gameLogic.getPlayer(playerID).sortHand();
                }
                break;
        }

        //convert players hand to strings
        Map<String, StringBuilder> playerHands = generatePlayerHands(game.gameLogic, gameData.getPlayers());
        StringBuilder p1Hand = playerHands.getOrDefault("P1", new StringBuilder());
        StringBuilder p2Hand = playerHands.getOrDefault("P2", new StringBuilder());
        StringBuilder p3Hand = playerHands.getOrDefault("P3", new StringBuilder());
        StringBuilder p4Hand = playerHands.getOrDefault("P4", new StringBuilder());

        return new DrawMessage("drawEvent", eventCard, id, shields, p1Discard, p2Discard, p3Discard, p4Discard, p1Hand.toString(), p2Hand.toString(), p3Hand.toString(), p4Hand.toString(), stages);
    }

    @MessageMapping("/discardCard")
    @SendTo("/topic/message")
    public DiscardMessage discardCard(ConnectionMessage message) throws Exception {
        String [] msg = message.getName().split(" ");
        String id = msg[0];
        String cardToDiscard = msg[1];

        game.gameLogic.removeCardsAndDiscard(cardToDiscard, id);
        String discardsLeft = String.valueOf(game.computeNumberOfCardsToDiscard(id));

        return new DiscardMessage("discard", id, discardsLeft);
    }
}