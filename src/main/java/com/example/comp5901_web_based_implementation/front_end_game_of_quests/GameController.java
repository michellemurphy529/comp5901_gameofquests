package com.example.comp5901_web_based_implementation.front_end_game_of_quests;
import static org.junit.jupiter.api.DynamicTest.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.devtools.v121.v121CdpInfo;
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
    // String sponsorID = "";
    ArrayList<String> playersToRemoveFromEligibility;
    Boolean sponsorRedraw = false;
    Boolean questFinishedEarly = false;
    Boolean questFinished = false;

    public GameController(GameData gameData) {
        this.game = new Game(new GameLogic(), new GameDisplay());
        this.gameData = gameData;
    }

    // private final Game game;
    // private final GameData gameData;

    // public GameController(Game game, GameData gameData) {
    //     this.game = game;
    //     this.gameData = gameData;
    // }

    @MessageMapping("/connect")
    @SendTo("/topic/message")
    public Message connect(ConnectionMessage message) throws Exception {
        numPlayers++;
        //First player connects and clicks register
        if (numPlayers == 1) {
            //Set up backend Game
            game.setDecks();
            game.getAdventureDeck().shuffle();
            game.getEventDeck().shuffle();
            game.setPlayers();
            //Feed Game info to GameData
            gameData.setAdventureDeck(game.getAdventureDeck());
            gameData.setEventDeck(game.getEventDeck());
            gameData.setCurrentPlayerInHotseat("P" + numPlayers);


            // gameData.setHands(new ArrayList<>(Arrays.asList(
            //     ,
            //     game.gameLogic.getPlayerHand("P2"),
            //     game.gameLogic.getPlayerHand("P3"),
            //     game.gameLogic.getPlayerHand("P4")
            // )));

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
        //For rigging purposes reset the backend Adventure deck if it has changed in gameData
        // System.out.println("BEFORE ADECK: " + game.getAdventureDeck().getSize());
        // System.out.println("BEFORE ADECK gamedata: " + gameData.getAdventureDeck().getSize());
        // updateBackendAdventureDeck();
        // System.out.println("AFTER ADECK update: " + game.getAdventureDeck().getSize());
        // System.out.println("AFTER ADECK gamedata: " + gameData.getAdventureDeck().getSize());
        // gameData.setAdventureDeck(game.getAdventureDeck());

        // System.out.println("BEFORE EDECK: " + game.getEventDeck().getSize());
        // System.out.println("BEFORE EDECK gamedata: " + gameData.getEventDeck().getSize());
        // updateBackendEventDeck();
        // System.out.println("AFTER EDECK: " + game.getEventDeck().getSize());
        // System.out.println("AFTER EDECK gamedata: " + gameData.getEventDeck().getSize());
        // gameData.setEventDeck(game.getEventDeck());
        // Start game and deal out initial 12 adventure cards
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
    
        if (sponsorRedraw && questFinished) {
            if (game.computeNumberOfCardsToDiscard(id) == 0) {
                sponsorRedraw = false;
            }
            Thread.sleep(500);
            return new DiscardMessage("endQuestDiscard", id, discardsLeft, playerHand.toString());
        }
        else if (currentEventCard.equals("Prosperity")) {
            Thread.sleep(500);
            return new DiscardMessage(currentEventCard, "no", id, discardsLeft, playerHand.toString());
        }else if (currentEventCard.contains("Q") && currentEventCard.length() == 2) {
            Thread.sleep(500);
            return new DiscardMessage("askStageDiscard", id, discardsLeft, playerHand.toString());
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
            gameData.setSponsorID(id);
            // gameData.getSponsorID() = new String(id);

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

    @MessageMapping("/carryOutQuest")
    @SendTo("/topic/message")
    public Object carryOutQuest(ConnectionMessage message) throws Exception {

        //Sponsor has built the Quest Stages & triggered the beginning of the Quest
        if (message.getName().equals("") &&
            stage == 0) {
            game.gameLogic.setCurrentStageNumber(0);
            game.gameLogic.setMaxStages(gameData.getTotalStages());
            game.gameLogic.setEligiblePlayersWithoutSponsor();

            // System.out.println("carryOutQuest CONTROLLER line 309 setting without sponsor: eligPlayers " + game.gameLogic.getEligiblePlayers() );
            game.gameLogic.incrementStageNumber();
            stage = game.gameLogic.getCurrentStageNumber();
            ArrayList<String> eligiblePlayers = game.gameLogic.getEligiblePlayers();
            gameData.setEligiblePlayers(eligiblePlayers);
            // System.out.println("carryOutQuest CONTROLLER line 314 setting gameData: eligPlayers " + game.gameLogic.getEligiblePlayers() );

            // System.out.println(eligiblePlayersAsked);
            playersToRemoveFromEligibility = new ArrayList<>();
            //TO DO add try catc
            // gameData.getEligiblePlayers().remove(0);

            // eligiblePlayersAsked++;
            // System.out.println("IN no string BLOCK eligible players asked count = " + eligiblePlayersAsked);
            Thread.sleep(500);
            return new Message("askStage", getFirstPlayer(gameData.getEligiblePlayers()), String.valueOf(stage));
        }

        //Stage 1 - 5 has begun and players begin being asked
        // else if (message.getName().equals("") &&
        //     stage > 0) {
        //         game.gameLogic.incrementStageNumber();
        //         stage = game.gameLogic.getCurrentStageNumber();
        //         ArrayList<String> eligiblePlayers = game.gameLogic.getEligiblePlayers();
        //         gameData.setEligiblePlayers(new ArrayList<>(eligiblePlayers));
        //         // System.out.println(eligiblePlayersAsked);
        //         playersToRemoveFromEligibility = new ArrayList<>();
        //         //add try catch
        //         // gameData.getEligiblePlayers().remove(0);
    
        //         eligiblePlayersAsked++;
        //         // System.out.println("IN no string BLOCK eligible players asked count = " + eligiblePlayersAsked);
        //         Thread.sleep(500);
        //         return new Message("askStage", getFirstPlayer(gameData.getEligiblePlayers()), String.valueOf(stage));
        // }
        
        //Player's have responded to being asked to participate
        String [] msg = message.getName().split(" ");
        String id = msg[0];
        String response = msg[1];

        //Participant responded YES and has discarded card        
        if (response.equals("playerDiscardDone") && questFinished) {

            //reset questFinished
            questFinished = false;

            //Process end of Quest in the backend
            ArrayList<Player> playersWhoHave7Shields = game.gameLogic.determineWinners();
            String gameWinners = "";
            if (playersWhoHave7Shields.size() > 0) {
                for (Player player : playersWhoHave7Shields) {
                    gameWinners += player.getPlayerID() + " ";
                }
                gameWinners = gameWinners.trim();
            }else {
                gameWinners = "none";
            }

            if (gameWinners.equals("none")) {
                System.out.println("currentplayer in game backend before turn change" + game.getCurrentPlayer().getPlayerID());
                for (int i = 0; i < 4; i++) {
                    if (gameData.getCurrentPlayerInHotseat().equals(game.getCurrentPlayer().getPlayerID())) {
                        break;
                    }
                    game.gameLogic.nextTurn();
                }
                System.out.println("currentplayer in game backend after turn change" + game.getCurrentPlayer().getPlayerID());
                // gameData.setCurrentPlayerInHotseat(game.getCurrentPlayer().getPlayerID());

                Thread.sleep(500);
                return new Message("continueGame", gameData.getCurrentPlayerInHotseat());
            }

            Thread.sleep(500);
            return new Message("winnersFound", gameWinners);
        } 
        else if (response.equals("playerDiscardDone") && gameData.getEligiblePlayers().size() != 0) {

            Thread.sleep(500);
            return new Message("askStage", getFirstPlayer(gameData.getEligiblePlayers()), String.valueOf(stage));
        } 
        //Participant responded YES and has discarded card    
        else if (response.equals("playerDiscardDone") && gameData.getEligiblePlayers().size() == 0) {
            game.gameLogic.removePlayerFromSubsequentStages(playersToRemoveFromEligibility);
            gameData.setParticpants(game.gameLogic.getEligiblePlayers());
            gameData.setEligiblePlayers(game.gameLogic.getEligiblePlayers());
            if (gameData.getParticipants().size() > 1 && stage <= gameData.getTotalStages()) {
                game.gameLogic.setAttackHands();
                game.gameLogic.setAttackValues();
                // System.out.println(gameData.getParticipants());
    
                //set up for attacks
                Thread.sleep(500);
                return new Message("setUpAttacks", getFirstPlayer(gameData.getParticipants()), String.valueOf(stage));
            } else if (gameData.getParticipants().size() < 1){
                // eligiblePlayersAsked = 0;
                Thread.sleep(500);
                return new Message("endQuestEarly", gameData.getCurrentPlayerInHotseat());
            } else {
                // eligiblePlayersAsked = 0;
                Thread.sleep(500);
                return new Message("endQuest", gameData.getCurrentPlayerInHotseat());
            }
        } 

        //Participant responded NO and is added to list of players to remove from eligiblity
        else if (response.equals("no") && gameData.getEligiblePlayers().size() != 0) {
            playersToRemoveFromEligibility.add(id);
            // eligiblePlayersAsked++;
            Thread.sleep(500);
            return new Message("askStage", getFirstPlayer(gameData.getParticipants()), String.valueOf(stage));
        } 
        else if (response.equals("no") && gameData.getEligiblePlayers().size() == 0) {
            game.gameLogic.removePlayerFromSubsequentStages(playersToRemoveFromEligibility);
            gameData.setParticpants(game.gameLogic.getEligiblePlayers());
            gameData.setEligiblePlayers(game.gameLogic.getEligiblePlayers());
            if (gameData.getParticipants().size() > 1 && stage != gameData.getTotalStages()) {
                game.gameLogic.setAttackHands();
                game.gameLogic.setAttackValues();
                // System.out.println(gameData.getParticipants());
    
                //set up for attacks
                Thread.sleep(500);
                return new Message("setUpAttacks", getFirstPlayer(gameData.getParticipants()), String.valueOf(stage));
            } else if (gameData.getParticipants().size() < 1 && stage != gameData.getTotalStages()) {
                //sponsor redraw
                //end quest boolean
                //sponsorRedraw boolean
                //
                Thread.sleep(500);
                return new Message("endQuestEarly", gameData.getCurrentPlayerInHotseat());
            } else {
                
                Thread.sleep(500);
                return new Message("endQuest", gameData.getCurrentPlayerInHotseat());
            }
        } 
        //Participant responded YES
        //Gets dealt 1 ADVENTURE CARD
        //Discards whether they need to or not because of updating the hand
        else if (response.equals("yes")) {
            game.gameLogic.dealNumberAdventureCards(id, 1);
            String discardsLeft = Integer.toString(game.computeNumberOfCardsToDiscard(id));
            StringBuilder playerHand = generatePlayerHand(id);
            // eligiblePlayersAsked++;
            // System.out.println("IN 3rd BLOCK YES eligible players asked count = " + eligiblePlayersAsked);
            //Reusing the Queen's Favor functionality
            Thread.sleep(500);
            return new QueenMessage("askStageDiscard", id, discardsLeft, playerHand.toString());
        }
        //Won't need to discard and is not last player to be asked
        // else if (response.equals("yes") && game.gameLogic.getPlayer(id).getHandSize() < 12 ) {
        //     game.gameLogic.dealNumberAdventureCards(id, 1);
        //     StringBuilder playerHand = generatePlayerHand(id);
        //     eligiblePlayersAsked++;
        //     Thread.sleep(500);
        //     return new Message("askStage", getFirstPlayer(gameData.getEligiblePlayers()), String.valueOf(stage), playerHand.toString());
        // }
        else if (gameData.getEligiblePlayers().size() == 0) {
            game.gameLogic.dealNumberAdventureCards(id, 1);
            // eligiblePlayersAsked++;
            game.gameLogic.removePlayerFromSubsequentStages(playersToRemoveFromEligibility);
            gameData.setParticpants(game.gameLogic.getEligiblePlayers());
            gameData.setEligiblePlayers(game.gameLogic.getEligiblePlayers());
            StringBuilder playerHand = generatePlayerHand(id);
            if (gameData.getParticipants().size() > 1 && stage != gameData.getTotalStages()) {
                game.gameLogic.setAttackHands();
                game.gameLogic.setAttackValues();
                Thread.sleep(500);
                return new Message("setUpAttacks", getFirstPlayer(gameData.getParticipants()), String.valueOf(stage), playerHand.toString());
            } else if (gameData.getParticipants().size() < 1 && stage != gameData.getTotalStages()){
                // eligiblePlayersAsked = 0;
                Thread.sleep(500);
                //change this to update last player asked hand first then endQuestEarly??
                return new Message("endQuestEarly", gameData.getCurrentPlayerInHotseat(), playerHand.toString());
            } else {
                // eligiblePlayersAsked = 0;
                Thread.sleep(500);
                //change this to update last player asked hand first then endQuest??

                //gameData.getSponsorID() send 
                //sponsor redraw boolean
                //add cards to sponsor hand
                //add shields for end of quest gained
                //sponsor player hand

                return new Message("endQuest", gameData.getCurrentPlayerInHotseat(), playerHand.toString());
            }
        }
        //ALL eligible players have been asked to participate
        // if (game.gameLogic.getEligiblePlayers().size() < eligiblePlayersAsked) {
        //     //Remove ineligible players
        //     game.gameLogic.removePlayerFromSubsequentStages(playersToRemoveFromEligibility);
        //     // System.out.println("in 404 block CONTROLLER players to removePlayers " + playersToRemoveFromEligibility );
        //     // System.out.println("in 404 block CONTROLLER eligplayers " + game.gameLogic.getEligiblePlayers() );

        //     gameData.setParticpants(game.gameLogic.getEligiblePlayers());
        //     // System.out.println("in 404 block CONTROLLER eligplayers " + game.gameLogic.getEligiblePlayers() );

        //     //There are enough participants to continue quest
        //     //Set up attacks
        //     if (gameData.getParticipants().size() > 1) {
        //         game.gameLogic.setAttackHands();
        //         game.gameLogic.setAttackValues();
        //         System.out.println(gameData.getParticipants());
    
        //         //set up for attacks
        //         Thread.sleep(500);
        //         return new Message("setUpAttacks", getFirstPlayer(gameData.getParticipants()), String.valueOf(stage));
        //     }
        //     //Quest ends since all players have been asked to participate
        //     // AND there are not enough participants for the quest to continue (<= 1)
        //     else if (gameData.getParticipants().size() <= 1) {
        //         eligiblePlayersAsked = 0;
        //         Thread.sleep(500);
        //         return new Message("endQuestEarly", gameData.getCurrentPlayerInHotseat());
        //     }
        // }
        return new Message();
    }

    @MessageMapping("/buildAttack")
    @SendTo("/topic/message")
    public Object buildAttack(ConnectionMessage message) throws Exception {
        String [] msg = message.getName().split(" ");
        System.out.println(message.getName());
        String id = msg[0];
        ArrayList<Card> attackCards = new ArrayList<>();
        if (!msg[1].equals("none")) {
            String [] cards = msg[1].split(",");
            for (String card : cards) {
                String cardType = card.substring(0,1);
                int cardValue = Integer.valueOf(card.substring(1));
                attackCards.add(game.gameLogic.getCardFromHand(cardType, cardValue, id));
            }
        }

        // System.out.println("id and attack cards from message : id=" + id + " attackCards" + attackCards );
        // System.out.println(id);
        // System.out.println(cards);
        System.out.println(attackCards.size());
        System.out.println(attackCards);

        //Add attack cards to backend
        // System.out.println("gameData parts=" + gameData.getParticipants());
        // System.out.println("before addAttackCards: " +game.gameLogic.getEligiblePlayers());
        game.gameLogic.addAttackCards(id, attackCards);
        int positionInArray = getPlayerIDPosition(id, game.gameLogic.getEligiblePlayers());
        if (msg[1].equals("none")) {
            System.out.println("hit in none with id " + id);
            game.gameLogic.addAttackValue(positionInArray, 0);
        }else {
            System.out.println("hit in else with id " + id);
            game.gameLogic.addAttackValue(positionInArray, game.gameLogic.getAttackValue(attackCards));
        }
        game.gameLogic.getPlayer(id).sortHand();
        
        // System.out.println("line 456: " +game.gameLogic.getEligiblePlayers());
        // System.out.println("line 457: " +gameData.getEligiblePlayers());
        
        // System.out.println("line 459: " + game.gameLogic.getEligiblePlayers().size());
        // System.out.println("line 460: " +game.gameLogic.getEligiblePlayers());
        // System.out.println("line 461: " + positionInArray);
        

        String playerToBuildNextAttack = getFirstPlayer(gameData.getParticipants());

        System.out.println("player next " + playerToBuildNextAttack);

        if(!playerToBuildNextAttack.equals("")) {
            // System.out.println("hit does not equal empty str");
            // gameData.setEligiblePlayers(game.gameLogic.getEligiblePlayers());
            Thread.sleep(500);
            return new Message("setUpAttacks", playerToBuildNextAttack, String.valueOf(stage));
        }

        //Begin Resolve Attacks
        game.gameLogic.setCurrentStageValue(game.gameLogic.getCurrentStageValueFromQuestInfo());
        game.resolveAttacks();
        game.discardParticipantsCards();

        String stageLosers = String.join(" ", game.gameLogic.getStageLosers());
        String stageWinners = String.join(" ", game.gameLogic.getStageWinners());
        System.out.println(game.gameLogic.getEligiblePlayers().size());
        System.out.println(game.gameLogic.getEligiblePlayers());
        System.out.println(stageLosers);
        System.out.println(stageWinners);

        //Not enough eligible participants to continue quest
        if(stage < gameData.getTotalStages() && game.gameLogic.getEligiblePlayers().size() < 2) {
            //End quest early - Discard All Sponsors cards and redraws (Boolean FLAG)
            int numberToDraw = (gameData.getTotalStages()+game.gameLogic.getNumberofCardsUsedInQuestAndDiscard());
            System.out.println(numberToDraw);
            game.dealNumberOfAdventureCardsToPlayer(gameData.getSponsorID(), numberToDraw);
            game.gameLogic.getPlayer(gameData.getSponsorID()).sortHand();
            String discardsLeft = String.valueOf(game.computeNumberOfCardsToDiscard(gameData.getSponsorID()));
            StringBuilder sponsorHand = generatePlayerHand(gameData.getSponsorID());
            sponsorRedraw = true;
            questFinished = true;

            System.out.println("hit if");

            Thread.sleep(500);
            return new EndQuestMessage("endQuestEarly", gameData.getSponsorID(), discardsLeft, sponsorHand.toString(), stageLosers, stageWinners);
        }

        //Last Stage of Quest - ends naturally
        else if(stage == gameData.getTotalStages() && game.gameLogic.getEligiblePlayers().size() >= 1) {

            //Display Stage Losers and Winners
            //Remove Attack Cards Message
            //boolean to check for sponsor redraw 
            //+ questDone boolean to trigger winner situation when quest is over once discards are 0
            // String stageLosers = String.join(" ", game.gameLogic.getStageLosers());
            // String stageWinners = String.join(" ", game.gameLogic.getStageWinners());
            //sponsorID send 
            //sponsor redraw boolean
            //add cards to sponsor hand
            //add shields for end of quest gained
            //sponsor player hand
            
            //End quest early - Discard All Sponsors cards and redraws (Boolean FLAG)
            //number of cards to draw is number of stages + cards used in quest
            int numberToDraw = (gameData.getTotalStages()+game.gameLogic.getNumberofCardsUsedInQuestAndDiscard());
            System.out.println(numberToDraw);
            game.dealNumberOfAdventureCardsToPlayer(gameData.getSponsorID(), numberToDraw);
            game.gameLogic.getPlayer(gameData.getSponsorID()).sortHand();
            String discardsLeft = String.valueOf(game.computeNumberOfCardsToDiscard(gameData.getSponsorID()));
            StringBuilder sponsorHand = generatePlayerHand(gameData.getSponsorID());

            if(!game.gameLogic.getStageWinners().isEmpty()) {
                game.gameLogic.addShieldsToWinners(game.gameLogic.getStageWinners());
            }

            String shields1 = String.valueOf(game.gameLogic.getPlayer("P1").getShieldCount());
            String shields2 = String.valueOf(game.gameLogic.getPlayer("P2").getShieldCount());
            String shields3 = String.valueOf(game.gameLogic.getPlayer("P3").getShieldCount());
            String shields4 = String.valueOf(game.gameLogic.getPlayer("P4").getShieldCount());

            sponsorRedraw = true;
            //Trigger Quest Finished
            questFinished = true;

            Thread.sleep(500);
            return new EndQuestMessage("endQuest", gameData.getSponsorID(), discardsLeft, sponsorHand.toString(), stageLosers, stageWinners, shields1, shields2, shields3, shields4);
        }

        //Enough participants to continue Quest to the next stage
        else if(stage < gameData.getTotalStages() && game.gameLogic.getEligiblePlayers().size() > 1) {
            //Increase stage number, set stage value in backend
            game.gameLogic.incrementStageNumber();
            game.gameLogic.setCurrentStageValue(game.gameLogic.getCurrentStageValueFromQuestInfo());
            //Set game data eligible players for next stage
            ArrayList<String> eligiblePlayers = game.gameLogic.getEligiblePlayers();
            gameData.setEligiblePlayers(eligiblePlayers);

            //Reset or update controller variables for next stage
            playersToRemoveFromEligibility = new ArrayList<>();
            stage++;
    
            Thread.sleep(500);
            return new Message("askStage", getFirstPlayer(gameData.getEligiblePlayers()), String.valueOf(stage), stageLosers, stageWinners);
        }
        return new Message("null", "fromBuildAttack", String.valueOf(stage), String.join(" ", game.gameLogic.getEligiblePlayers()));
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
    public int getPlayerIDPosition(String id, ArrayList<String> eligiblePlayers) {
        // System.out.println("in getPlayerIDPostion eligplayers=" + eligiblePlayers);
        for (int i = 0; i < eligiblePlayers.size(); i++) {
            if (eligiblePlayers.get(i).equals(id)) {
                // System.out.println("in LOOP getPlayerIDPostion eligplayers=" + eligiblePlayers);
                return i;
            }
        }
        return -1;
    }
    public String getFirstPlayer(ArrayList<String> players) {
        return players.isEmpty() ? "" : players.remove(0);
    }
    private void updateBackendAdventureDeck() {
        ArrayList<Card> backendDeck = game.getAdventureDeck().getDeck();
        ArrayList<Card> riggedDeck = gameData.getAdventureDeck().getDeck();

        if (!backendDeck.equals(riggedDeck)) {
            backendDeck.clear();
            backendDeck.addAll(riggedDeck);
        }
    }
    private void updateBackendEventDeck() {
        ArrayList<Card> backendDeck = game.getEventDeck().getDeck();
        ArrayList<Card> riggedDeck = gameData.getEventDeck().getDeck();

        if (!backendDeck.equals(riggedDeck)) {
            backendDeck.clear();
            backendDeck.addAll(riggedDeck);
        }
    }
}
