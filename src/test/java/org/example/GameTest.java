package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    @DisplayName("RESP-001-Test-001: Set up Adventure Deck with proper number of cards and their types + values")
    void RESP_001_test_001(){
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();

        int foeCounter = 0, f5 = 0, f10 = 0, f15 = 0, f20 = 0, f25 = 0, f30 = 0, f35 = 0, f40 = 0, f50 = 0, f70 = 0;
        int weaponCounter = 0, d5 = 0, h10 = 0, s10 = 0, b15 = 0, l20 = 0, e30 = 0;

        for (Card c : adventureDeck.getDeck()){
            if (c instanceof FoeCard f){
                foeCounter++;

                if (f.getValue() == 5){
                    f5++;
                }else if (f.getValue() == 10){
                    f10++;
                }else if (f.getValue() == 15){
                    f15++;
                }else if (f.getValue() == 20){
                    f20++;
                }else if (f.getValue() == 25){
                    f25++;
                }else if (f.getValue() == 30){
                    f30++;
                }else if (f.getValue() == 35){
                    f35++;
                }else if (f.getValue() == 40){
                    f40++;
                }else if (f.getValue() == 50){
                    f50++;
                }else if (f.getValue() == 70){
                    f70++;
                }
            }else if (c instanceof WeaponCard w) {
                weaponCounter++;

                if (w.getValue() == 5){
                    d5++;
                }else if (w.getValue() == 10 && w.getType().contains("S")){
                    s10++;
                }else if (w.getValue() == 10 && w.getType().contains("H")){
                    h10++;
                }else if (w.getValue() == 15){
                    b15++;
                }else if (w.getValue() == 20){
                    l20++;
                }else if (w.getValue() == 30){
                    e30++;
                }
            }
        }

        //Test there are 100 cards in an Adventure Deck
        assertEquals(100, adventureDeck.getSize());

        /*
        Test there are 50 Foe Cards with the following quantities:
        8 of F5, 7 of F10, 8 of F15, 7 of F20, 7 of F25, 4 of F30, 4 of F35, 2 of F40, 2 of F50, 1 of F70
        */
        assertEquals(50, foeCounter);
        assertEquals(8, f5);
        assertEquals(7, f10);
        assertEquals(8, f15);
        assertEquals(7, f20);
        assertEquals(7, f25);
        assertEquals(4, f30);
        assertEquals(4, f35);
        assertEquals(2, f40);
        assertEquals(2, f50);
        assertEquals(1, f70);

        /*
        Test there are 50 Weapon Cards with the following quantities:
        6 of D5, 12 of H10, 16 of S10, 8 of B15, 6 of L20, 2 of E30
        */
        assertEquals(50, weaponCounter);
        assertEquals(6, d5);
        assertEquals(12, h10);
        assertEquals(16, s10);
        assertEquals(8, b15);
        assertEquals(6, l20);
        assertEquals(2, e30);
    }

    @Test
    @DisplayName("RESP-001-Test-002: Set up Event Deck with proper number of cards and their types + values")
    void RESP_001_test_002(){
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck eventDeck = game.getEventDeck();

        int questCounter = 0, q2 = 0, q3 = 0, q4 = 0, q5 = 0;
        int eventCounter = 0, plague = 0, queenFavor = 0, prosperity = 0;

        for (Card c : eventDeck.getDeck()){
            if (c instanceof QuestCard q){
                questCounter++;

                if (q.getStages() == 2){
                    q2++;
                }else if (q.getStages() == 3){
                    q3++;
                }else if (q.getStages() == 4){
                    q4++;
                }else if (q.getStages() == 5){
                    q5++;
                }
            }else if (c instanceof PlagueCard){
                eventCounter++;
                plague++;
            }else if (c instanceof QueensFavorCard){
                eventCounter++;
                queenFavor++;
            }else if (c instanceof ProsperityCard){
                eventCounter++;
                prosperity++;
            }
        }

        //Test there are 17 cards in an Event Deck
        assertEquals(17, eventDeck.getSize());

        /*
        Test there are 12 Quest Cards with the following quantities:
        3 of Q2, 4 of Q3, 3 of Q4, 2 of Q5
        */
        assertEquals(12, questCounter);
        assertEquals(3, q2);
        assertEquals(4, q3);
        assertEquals(3, q4);
        assertEquals(2, q5);

        /*
        Test there are 5 Event Cards with the following quantities:
        1 of Plague, 2 of Queen's favor, 2 of Prosperity
        */
        assertEquals(5, eventCounter);
        assertEquals(1, plague);
        assertEquals(2, queenFavor);
        assertEquals(2, prosperity);
    }

    @Test
    @DisplayName("RESP-001: System properly sets up adventure and event decks")
    void RESP_001(){
        RESP_001_test_001();
        RESP_001_test_002();
    }

    @Test
    @DisplayName("RESP-002-Test-001: Properly shuffle Adventure Deck")
    void RESP_002_test_001(){
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();

        //Get original ordering of the deck
        ArrayList<Card> originalOrder = new ArrayList<>(adventureDeck.getDeck());

        //Shuffle the deck
        adventureDeck.shuffle();

        //Get shuffled ordering of the deck
        ArrayList<Card> shuffledOrder = adventureDeck.getDeck();

        assertEquals(100, adventureDeck.getSize());
        assertNotEquals(originalOrder, shuffledOrder);
    }

    @Test
    @DisplayName("RESP-002-Test-002: Properly shuffle Event Deck")
    void RESP_002_test_002(){
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck eventDeck = game.getEventDeck();

        ArrayList<Card> originalOrder = new ArrayList<>(eventDeck.getDeck());
        eventDeck.shuffle();
        ArrayList<Card> shuffledOrder = eventDeck.getDeck();

        assertEquals(17, eventDeck.getSize());
        assertNotEquals(originalOrder, shuffledOrder);
    }

    @Test
    @DisplayName("RESP-002: System properly shuffles decks (event and adventure), before distribution")
    void RESP_002(){
        RESP_002_test_001();
        RESP_002_test_002();
    }

    @Test
    @DisplayName("RESP-004-Test-001: The system ensures the game always has exactly 4 players (with the 4 unique ID’s as " +
            "follows, ‘P1’, ‘P2’, ‘P3’ and ‘P4’)")
    void RESP_004_test_001(){
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setPlayers();

        //Test there are exactly 4 players
        assertEquals(4, game.getPlayers().size());

        //Test Player ID's are 'P1', 'P2', 'P3' & 'P4'
        assertEquals("P1", game.getPlayers().get(0).getPlayerID());
        assertEquals("P2", game.getPlayers().get(1).getPlayerID());
        assertEquals("P3", game.getPlayers().get(2).getPlayerID());
        assertEquals("P4", game.getPlayers().get(3).getPlayerID());
    }

    @Test
    @DisplayName("RESP-004: The system ensures the game always has exactly 4 players (with the 4 unique ID’s as " +
            "follows, ‘P1’, ‘P2’, ‘P3’ and ‘P4’)")
    void RESP_004(){
        RESP_004_test_001();
    }

    @Test
    @DisplayName("RESP-005-Test-001: System distributes 12 cards from the adventure deck to each player")
    void RESP_005_test_001(){
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Test each player has 12 cards
        for (String playerID : game.getPlayerIDs()) {
            assertEquals(12, game.gameLogic.getPlayerHand(playerID).size());

            //Test each card is an AdventureCard
            for (Card card : game.gameLogic.getPlayerHand(playerID)) {
                assertInstanceOf(AdventureCard.class, card);
            }
        }
    }

    @Test
    @DisplayName("RESP-005-Test-002: System updates deck after distribution of 12 cards")
    void RESP_005_test_002(){
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Test Adventure Deck has had 12 * 4 = 48 removed
        int expectedSizeUpdated = 100 - (12 * 4);
        assertEquals(expectedSizeUpdated, game.getAdventureDeck().getSize());
    }

    @Test
    @DisplayName("RESP-005: System distributes 12 cards from the adventure deck to each player, updating the deck")
    void RESP_005(){
        RESP_005_test_001();
        RESP_005_test_002();
    }

    @Test
    @DisplayName("RESP-003-Test-001: When the adventure deck runs out, the system reshuffles the adventure discard pile " +
            "and reuses it as the new adventure deck")
    void RESP_003_test_001(){
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        adventureDeck.shuffle();
        game.setPlayers();

        //Test Adventure Deck has total number of cards (100) and Discard Deck has 0
        assertEquals(100, adventureDeck.getSize());
        assertEquals(0, adventureDeck.getDiscardPileSize());

        //Mock distributing all cards from Adventure Deck
        for (int i = 0; i < 100; i++) {
            Card card = game.drawAdventureCard("P1");
            game.discardAdventureCard("P1", card);
        }

        //Test Adventure Deck has 0 cards and Discard Deck has 100
        assertEquals(0, adventureDeck.getSize());
        assertEquals(100, adventureDeck.getDiscardPileSize());

        //Test for reshuffle ordering before discards become card deck
        ArrayList<Card> orderBeforeReshuffle = new ArrayList<>(adventureDeck.getDiscardPile());

        //Distribute last Adventure Card from deck to invoke reshuffle of discard pile and reuse as new deck
        Card card = game.drawAdventureCard("P2");

        //Test Adventure Deck has 99 cards (1 card is in P2 hand) and Discard Deck has 0
        assertEquals(99, adventureDeck.getSize());
        assertEquals(0, adventureDeck.getDiscardPileSize());

        //Confirm P1 has 0 cards in hand, P2 has 1 card in hand
        assertEquals(0, game.gameLogic.getPlayer("P1").getHand().size());
        assertEquals(1, game.gameLogic.getPlayer("P2").getHand().size());

        //Test for reshuffle after cards are regular deck again
        ArrayList<Card> orderAfterReshuffle = adventureDeck.getDeck();
        assertNotEquals(orderBeforeReshuffle, orderAfterReshuffle);
    }

    @Test
    @DisplayName("RESP-003-Test-002: When the event deck runs out, the system reshuffles the event discard pile " +
            "and reuses it as the new event deck")
    void RESP_003_test_002(){
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck eventDeck = game.getEventDeck();
        eventDeck.shuffle();
        game.setPlayers();

        //Test Event Deck has total number of cards (17) and Discard Deck has 0
        assertEquals(17, eventDeck.getSize());
        assertEquals(0, eventDeck.getDiscardPileSize());

        //Mock distributing all cards from Event Deck
        for (int i = 0; i < 17; i++) {
            Card card = game.drawEventCard("P3");
            game.discardEventCard("P3", card);
        }

        //Test Event Deck has 0 cards and Discard Deck has 17
        assertEquals(0, eventDeck.getSize());
        assertEquals(17, eventDeck.getDiscardPileSize());

        //Test for reshuffle ordering before discards become card deck
        ArrayList<Card> orderBeforeReshuffle = new ArrayList<>(eventDeck.getDiscardPile());

        //Distribute last Event Card from deck to invoke reshuffle of discard pile and reuse as new deck
        Card card = game.drawEventCard("P4");

        //Test Event Deck has 16 cards and Discard Deck has 0
        assertEquals(16, eventDeck.getSize());
        assertEquals(0, eventDeck.getDiscardPileSize());

        //Confirm P3 has 0 cards in hand, P4 has 1 card in hand
        assertEquals(0, game.gameLogic.getPlayer("P3").getHand().size());
        assertEquals(1, game.gameLogic.getPlayer("P4").getHand().size());

        //Test for reshuffle after cards are regular deck again
        ArrayList<Card> orderAfterReshuffle = eventDeck.getDeck();
        assertNotEquals(orderBeforeReshuffle, orderAfterReshuffle);
    }

    @Test
    @DisplayName("RESP-003: When a deck runs out, the system reshuffles the discard pile and reuses it as the new deck")
    void RESP_003(){
        RESP_003_test_001();
        RESP_003_test_002();
    }

    @Test
    @DisplayName("RESP-006-Test-001: Follows fixed order of player P1, P2, P3, P4, then P1 etc.")
    void RESP_006_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setPlayers();

        String[] expectedOrderOfPlayerIDs = {"P1", "P2", "P3", "P4"};
        int roundOfTurns = 3;

        for (int playerTurn = 0; playerTurn < roundOfTurns; playerTurn++) {
            for (int i = 0; i < expectedOrderOfPlayerIDs.length; i++) {
                //Test current player follows order P1, P2, P3, P4, then P1 again
                String currentPlayerID = game.getCurrentPlayer().getPlayerID();
                assertEquals(expectedOrderOfPlayerIDs[i], currentPlayerID);
                //Next Player in order has a turn
                game.gameLogic.nextTurn();
            }
        }
    }

    @Test
    @DisplayName("RESP-006-Test-002: System properly invokes playing of turns in the fixed order")
    void RESP_006_test_002() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck eventDeck = game.getEventDeck();
        eventDeck.shuffle();
        game.setPlayers();

        String[] expectedOrderOfPlayerIDs = {"P1", "P2", "P3", "P4"};
        int roundOfTurns = 5;

        for (int playerTurn = 0; playerTurn < roundOfTurns; playerTurn++) {
            for (int i = 0; i < expectedOrderOfPlayerIDs.length; i++) {
                String currentPlayerID = game.getCurrentPlayer().getPlayerID();
                assertEquals(expectedOrderOfPlayerIDs[i], currentPlayerID);
                game.playTurn();
                game.discardEventCard(currentPlayerID, game.getLastCardDrawn());
            }
        }
    }

    @Test
    @DisplayName("RESP-006: The system follows the fixed order of play P1, then P2, then P3, then P4, then repeats with P1")
    void RESP_006(){
        RESP_006_test_001();
        RESP_006_test_002();
    }

    @Test
    @DisplayName("RESP-007-Test-001: System determines if one player has 7 shields and displays their " +
            "PlayerID to the users and displays a game termination message")
    void RESP_007_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setPlayers();

        //Test one player is winner with 7 shields
        game.gameLogic.getPlayer("P1").addShields(6);
        game.gameLogic.getPlayer("P2").addShields(7);
        game.gameLogic.getPlayer("P3").addShields(4);
        game.gameLogic.getPlayer("P4").addShields(5);
        ArrayList<Player> winners = game.gameLogic.determineWinners();
        assertEquals(1, winners.size());

        //Display winner ID and termination message
        game.displayWinnersAndTerminate(winners);

        //Test Output from GameDisplay class
        String output = game.gameDisplay.getOutput();
        assertTrue(output.contains("Winner(s) with 7 or more shields are: P2"));
        assertTrue(output.contains("Game is terminated... Goodbye!"));
    }

    @Test
    @DisplayName("RESP-007-Test-002: System determines if all players have more than 7 shields and displays their " +
            "PlayerID to the users and displays a game termination message")
    void RESP_007_test_002() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setPlayers();

        //Test all players are winners with 7 or more shields
        game.gameLogic.getPlayer("P1").addShields(10);
        game.gameLogic.getPlayer("P2").addShields(7);
        game.gameLogic.getPlayer("P3").addShields(8);
        game.gameLogic.getPlayer("P4").addShields(20);
        ArrayList<Player> winners = game.getWinners();
        assertEquals(4, winners.size());

        //Display winner ID and termination message
        game.displayWinnersAndTerminate(winners);

        //Test Output from GameDisplay class
        String output = game.gameDisplay.getOutput();
        assertTrue(output.contains("Winner(s) with 7 or more shields are: P1, P2, P3, P4"));
        assertTrue(output.contains("Game is terminated... Goodbye!"));
    }

    @Test
    @DisplayName("RESP-007: System determines if one or more players have 7 shields, and System displays " +
            "the ID of each winner and then terminates")
    void RESP_007(){
        RESP_007_test_001();
        RESP_007_test_002();
    }

    @Test
    @DisplayName("RESP-008-Test-001: System properly processes when there are no winners and continues with next players turn")
    void RESP_008_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck eventDeck = game.getEventDeck();
        eventDeck.shuffle();
        game.setPlayers();

        //Test there are no winners
        game.gameLogic.getPlayer("P1").addShields(2);
        game.gameLogic.getPlayer("P2").addShields(0);
        game.gameLogic.getPlayer("P3").addShields(6);
        game.gameLogic.getPlayer("P4").addShields(0);
        ArrayList<Player> winners = game.getWinners();
        assertEquals(0, winners.size());

        //Added in REFAC-007
        game.processEndOfQuest();

        String expectedOutput = "There are no winner(s).\nGame of Quest's continues...\n" +
                "\nP1's Turn:\n\n" +
                "Drawing Event Card..." +
                "\nYou drew: " + game.getLastCardDrawn().displayCardName() + "\n\n";

        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-008: System properly processes when there are no winners and continues with next players turn")
    void RESP_008(){
        RESP_008_test_001();
    }

    @Test
    @DisplayName("RESP-009-Test-001: When the next player’s turn is triggered, the system draws an event card")
    void RESP_009_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Trigger next player's turn
        game.playTurn();

        //Test last card drawn is an event card
        Card lastCardDrawn = game.getLastCardDrawn();
        assertInstanceOf(EventCard.class, lastCardDrawn);

        //Test Last card drawn is the same card last added to the Player's hand
        //Changed to call specifically P1 as the next turn has already been invoked
        Card cardInPlayerHand = game.gameLogic.getPlayer("P1").getHand().getLast();
        assertEquals(lastCardDrawn, cardInPlayerHand);
    }

    @Test
    @DisplayName("RESP-009-Test-002: When the next player’s turn is triggered, the system draws an event card" +
            "and displays it")
    void RESP_009_test_002() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        game.setPlayers();

        //Trigger next player's turn
        game.playTurn();

        //Test Event Card drawn is displayed
        String expectedOutput = "\nP1's Turn:\n\n" +
                "Drawing Event Card..." +
                "\nYou drew: " + game.getLastCardDrawn().displayCardName() + "\n\n";
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-009: When the next player’s turn is triggered, the system draws an event card and displays it")
    void RESP_009(){
        RESP_009_test_001();
        RESP_009_test_002();
    }

    @Test
    @DisplayName("RESP-010-Test-001: System carries out Event card action for Plague (current player loses 2 shields)")
    void RESP_010_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Add Plague Card as first card that will be drawn from Event Deck
        Card plagueCard = new PlagueCard();
        eventDeck.cards.addFirst(plagueCard);

        //Add 2 shields to Player 1
        Player p1 = game.gameLogic.getPlayer("P1");
        p1.addShields(2);
        //Test that before Plague Card Player 1 has 2 shields
        assertEquals(2, p1.getShieldCount());

        //Trigger Player 1 turn
        game.playTurn();

        //Test Player that draws Plague Card loses 2 shields
        assertEquals(0, p1.getShieldCount());
    }

    @Test
    @DisplayName("RESP-010: System carries out Event card action for Plague (current player loses 2 shields)")
    void RESP_010(){
        RESP_010_test_001();
    }

}