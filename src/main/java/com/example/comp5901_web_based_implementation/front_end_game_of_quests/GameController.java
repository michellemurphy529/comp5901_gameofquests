package com.example.comp5901_web_based_implementation.front_end_game_of_quests;
import java.util.ArrayList;
import java.util.Arrays;
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
    int prosperityChange = 0;
    int playersAskedSponsorship = 0;
    int stage = 0;
    int previousStageValue = 0;

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
        Thread.sleep(500);

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

        if(!eventCard.equals("Q")) {
            game.discardEventCard(id, cardDrawn);
        }

        if (eventCard.equals("Plague")) {
            game.gameLogic.carryOutPlagueAction();
            String shields = String.valueOf(game.gameLogic.getPlayer(id).getShieldCount());
            Thread.sleep(500);

            return new PlagueMessage(eventCard, id, shields);
        }
        else if (eventCard.equals("Queen's Favor")) {
            currentEventCard = "QueensFavor";
            eventCard = "QueensFavor";
            game.gameLogic.dealNumberAdventureCards(id, 2);
            String discardsLeft = Integer.toString(game.computeNumberOfCardsToDiscard(id));
            StringBuilder playerHand = generatePlayerHand(id);
            Thread.sleep(500);

            return new QueenMessage(eventCard, id, discardsLeft, playerHand.toString());
        }
        else if (eventCard.equals("Prosperity")) {
            prosperityChange = 0;
            game.gameLogic.dealAllPlayersAdventureCards(game.getPlayerIDs(), 2);
            String discardsLeft = Integer.toString(game.computeNumberOfCardsToDiscard(id));
            StringBuilder playerHand = generatePlayerHand(id);
            Thread.sleep(500);

            return new ProsperityMessage(eventCard, "no", id, discardsLeft, playerHand.toString());
        }
        else if (eventCard.equals("Q")) {
            //reset Quest card variables
            playersAskedSponsorship = 0;
            stage = 1;
            previousStageValue = 0;

            //Quest functionality
            currentEventCard = cardDrawn.displayCardName();
            eventCard = cardDrawn.displayCardName();
            System.out.println("Drew : " + eventCard);
            gameData.setTotalStages(eventCard.substring(1));

            game.discardEventCard(id, cardDrawn);
            Thread.sleep(500);

            return new QuestMessage(eventCard, id, "no");
        }

        return new Message("noMessage", "fromDrawCard");
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
    
        if (currentEventCard.equals("Prosperity")) {
            Thread.sleep(500);
            return new DiscardMessage(currentEventCard, "no", id, discardsLeft, playerHand.toString());
        }
        Thread.sleep(500);
        return new DiscardMessage(currentEventCard, id, discardsLeft, playerHand.toString());
    }

    @MessageMapping("/changeHotseat")
    @SendTo("/topic/message")
    public Message changePlayerHotseat(ConnectionMessage message) throws Exception {
        game.gameLogic.nextTurn();
        gameData.setCurrentPlayerInHotseat(game.getCurrentPlayer().getPlayerID());

        Thread.sleep(500);
        return new Message("changeHotseat", gameData.getCurrentPlayerInHotseat());
    }

    @MessageMapping("/prosperityChange")
    @SendTo("/topic/message")
    public ProsperityMessage prosperityChange(ConnectionMessage message) throws Exception {
        prosperityChange++;
        game.gameLogic.nextTurn();
        gameData.setCurrentPlayerInHotseat(game.getCurrentPlayer().getPlayerID());

        String discardsLeft = String.valueOf(game.computeNumberOfCardsToDiscard(gameData.getCurrentPlayerInHotseat()));
        StringBuilder playerHand = generatePlayerHand(gameData.getCurrentPlayerInHotseat());

        if (prosperityChange == 4) {
            Thread.sleep(500);
            return new ProsperityMessage(currentEventCard, "yes", gameData.getCurrentPlayerInHotseat(), discardsLeft, playerHand.toString());
        }
        Thread.sleep(500);
        return new ProsperityMessage(currentEventCard, "no", gameData.getCurrentPlayerInHotseat(), discardsLeft, playerHand.toString());
    }

    @MessageMapping("/sponsorshipResponse")
    @SendTo("/topic/message")
    public QuestMessage sponsorResponse(ConnectionMessage message) throws Exception {

        String [] msg = message.getName().split(" ");
        String id = msg[0];
        String response = msg[1];

        playersAskedSponsorship++;

        if (playersAskedSponsorship == 4 && response.equals("no")) {
            //No players sponsor - change turn to next player
            game.gameLogic.nextTurn();
            //Update gameData hotseat player to continue
            gameData.setCurrentPlayerInHotseat(game.getCurrentPlayer().getPlayerID());

            Thread.sleep(500);
            return new QuestMessage(currentEventCard, gameData.getCurrentPlayerInHotseat(), "done");
        }

        if (response.equals("yes")) {
            //set Sponsor ID in Game
            game.gameLogic.setSponsorID(id);
            //Set GameData sponsorID
            gameData.setSponsorID(game.getSponsorPlayerID());

            Thread.sleep(500);
            return new QuestMessage(currentEventCard, gameData.getSponsorID(), "yes", String.valueOf(stage));

        }

        if (response.equals("no")) {
            //Ask next player
            game.gameLogic.nextTurn();
            //Set GameData player being asked
            gameData.setPlayerBeingAsked(game.getCurrentPlayer().getPlayerID());

            Thread.sleep(500);
            return new QuestMessage(currentEventCard, gameData.getPlayerBeingAsked(), "no");
        }
    
        Thread.sleep(500);
        return new QuestMessage("noMessage", "fromSponsorResponse");
    }

    @MessageMapping("/buildStage")
    @SendTo("/topic/message")
    public QuestMessage buildStage(ConnectionMessage message) throws Exception {
        String [] msg = message.getName().split(" ");
        String id = msg[0];
        String [] cards = msg[1].split(",");
        ArrayList<String> stageCards = new ArrayList<>(Arrays.asList(cards));

        if (stage == 1) {
            game.gameLogic.setQuestInfo(gameData.getTotalStages());
        }

        //Calling Game to build quest in backend
        ArrayList<Card> stageCardsFromSponsorHand = game.gameLogic.getStageCardsFromSponsor(gameData.getSponsorID(), stageCards);
        game.gameLogic.sortStageCards(stageCardsFromSponsorHand);
        game.gameLogic.addCardstoQuestInfo(stage, stageCardsFromSponsorHand);
        previousStageValue = game.gameLogic.getStageValue(stage, game.gameLogic.getQuestInfo());
        StringBuilder playerHand = generatePlayerHand(id);

        if (stage == gameData.getTotalStages()) {
            //Reset stage number for the quest stages
            stage = 0;

            Thread.sleep(500);
            return new QuestMessage(currentEventCard, id, "yes", playerHand.toString(), "finishedBuilding");
        }

        stage++;
        Thread.sleep(500);
        return new QuestMessage(currentEventCard, id, "yes", playerHand.toString(), String.valueOf(stage));
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