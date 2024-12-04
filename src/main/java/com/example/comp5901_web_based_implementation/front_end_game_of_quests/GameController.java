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
    String currentEventCard;

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
    public Object drawCard(ConnectionMessage message) throws Exception {
        String id = message.getName();
        System.out.println("Player ID: " + id);
        
        Card cardDrawn = game.drawEventCard(id);
        String eventCard = cardDrawn.getType();
        currentEventCard = eventCard;

        String stages = "0";
        if(eventCard.equals("Q")) {
            currentEventCard = cardDrawn.displayCardName();
            eventCard = cardDrawn.displayCardName();
            System.out.println("Drew : " + eventCard);
            stages = cardDrawn.displayCardName().substring(1);
            System.out.println("Quest card drawn with " + stages + " stages");
        }
        System.out.println("Current Event Card GLOBAL: " + currentEventCard);

        game.discardEventCard(id, cardDrawn);
        String shields = "0";

        if(eventCard.equals("Plague")) {
            game.gameLogic.carryOutPlagueAction();
            shields = String.valueOf(game.gameLogic.getPlayer(id).getShieldCount());
            return new PlagueMessage(eventCard, id, shields);
        }else if(eventCard.equals("Queen's Favor")) {
            currentEventCard = "QueensFavor";
            eventCard = "QueensFavor";
            game.gameLogic.dealNumberAdventureCards(id, 2);
            String discardsLeft = Integer.toString(game.computeNumberOfCardsToDiscard(id));
            StringBuilder playerHand = generatePlayerHand(id);

            return new QueenMessage(eventCard, id, discardsLeft, playerHand.toString());
        }
        return new DrawMessage();
    }

    @MessageMapping("/discardCard")
    @SendTo("/topic/message")
    public DiscardMessage discardCard(ConnectionMessage message) throws Exception {
        String [] msg = message.getName().split(" ");
        String id = msg[0];
        String cardToDiscard = msg[1];

        game.gameLogic.removeCardsAndDiscard(cardToDiscard, id);
        String discardsLeft = String.valueOf(game.computeNumberOfCardsToDiscard(id));
        StringBuilder playerHand = generatePlayerHand(id);

        return new DiscardMessage(currentEventCard, id, discardsLeft, playerHand.toString());
    }

    @MessageMapping("/changeHotseat")
    @SendTo("/topic/message")
    public Message changePlayerHotseat(ConnectionMessage message) throws Exception {
        game.gameLogic.nextTurn();
        gameData.setCurrentPlayerInHotseat(game.getCurrentPlayer().getPlayerID());

        return new Message("changeHotseat", gameData.getCurrentPlayerInHotseat());
    }

    //helpers
    private Map<String, StringBuilder> generatePlayerHands(GameLogic gameLogic, List<Player> players) {
        Map<String, StringBuilder> playerHands = new HashMap<>();

        for (Player player : players) {
            StringBuilder playerHand = generatePlayerHand(player.getPlayerID());
            playerHands.put(player.getPlayerID(), playerHand);
        }

        return playerHands;
    }
    private StringBuilder generatePlayerHand(String id) {
        game.gameLogic.getPlayer(id).sortHand();
        ArrayList<Card> hand = game.gameLogic.getPlayer(id).getHand();
        StringBuilder cardNames = new StringBuilder();
        for (Card card : hand) {
            AdventureCard aCard = (AdventureCard) card;
            if (cardNames.length() > 0) {
                cardNames.append(" ");
            }
            cardNames.append(aCard.displayCardName());
        }
        return cardNames;
    }
}