package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    @DisplayName("RESP-001-Test-001: Set up Adventure Deck with proper number of cards and their types + values")
    void RESP_001_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();

        int foeCounter = 0, f5 = 0, f10 = 0, f15 = 0, f20 = 0, f25 = 0, f30 = 0, f35 = 0, f40 = 0, f50 = 0, f70 = 0;
        int weaponCounter = 0, d5 = 0, h10 = 0, s10 = 0, b15 = 0, l20 = 0, e30 = 0;

        for (Card c : adventureDeck.getDeck()) {
            if (c instanceof FoeCard f) {
                foeCounter++;

                if (f.getValue() == 5) {
                    f5++;
                } else if (f.getValue() == 10) {
                    f10++;
                } else if (f.getValue() == 15) {
                    f15++;
                } else if (f.getValue() == 20) {
                    f20++;
                } else if (f.getValue() == 25) {
                    f25++;
                } else if (f.getValue() == 30) {
                    f30++;
                } else if (f.getValue() == 35) {
                    f35++;
                } else if (f.getValue() == 40) {
                    f40++;
                } else if (f.getValue() == 50) {
                    f50++;
                } else if (f.getValue() == 70) {
                    f70++;
                }
            } else if (c instanceof WeaponCard w) {
                weaponCounter++;

                if (w.getValue() == 5) {
                    d5++;
                } else if (w.getValue() == 10 && w.getType().contains("S")) {
                    s10++;
                } else if (w.getValue() == 10 && w.getType().contains("H")) {
                    h10++;
                } else if (w.getValue() == 15) {
                    b15++;
                } else if (w.getValue() == 20) {
                    l20++;
                } else if (w.getValue() == 30) {
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
    void RESP_001_test_002() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck eventDeck = game.getEventDeck();

        int questCounter = 0, q2 = 0, q3 = 0, q4 = 0, q5 = 0;
        int eventCounter = 0, plague = 0, queenFavor = 0, prosperity = 0;

        for (Card c : eventDeck.getDeck()) {
            if (c instanceof QuestCard q) {
                questCounter++;

                if (q.getStages() == 2) {
                    q2++;
                } else if (q.getStages() == 3) {
                    q3++;
                } else if (q.getStages() == 4) {
                    q4++;
                } else if (q.getStages() == 5) {
                    q5++;
                }
            } else if (c instanceof PlagueCard) {
                eventCounter++;
                plague++;
            } else if (c instanceof QueensFavorCard) {
                eventCounter++;
                queenFavor++;
            } else if (c instanceof ProsperityCard) {
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
    void RESP_001() {
        RESP_001_test_001();
        RESP_001_test_002();
    }

    @Test
    @DisplayName("RESP-002-Test-001: Properly shuffle Adventure Deck")
    void RESP_002_test_001() {
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
    void RESP_002_test_002() {
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
    void RESP_002() {
        RESP_002_test_001();
        RESP_002_test_002();
    }

    @Test
    @DisplayName("RESP-004-Test-001: The system ensures the game always has exactly 4 players (with the 4 unique ID’s as " +
            "follows, ‘P1’, ‘P2’, ‘P3’ and ‘P4’)")
    void RESP_004_test_001() {
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
    void RESP_004() {
        RESP_004_test_001();
    }

    @Test
    @DisplayName("RESP-005-Test-001: System distributes 12 cards from the adventure deck to each player")
    void RESP_005_test_001() {
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
    void RESP_005_test_002() {
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
    void RESP_005() {
        RESP_005_test_001();
        RESP_005_test_002();
    }

    @Test
    @DisplayName("RESP-003-Test-001: When the adventure deck runs out, the system reshuffles the adventure discard pile " +
            "and reuses it as the new adventure deck")
    void RESP_003_test_001() {
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
    void RESP_003_test_002() {
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
    void RESP_003() {
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
                game.gameLogic.nextTurn();
                game.discardEventCard(currentPlayerID, game.getLastEventCardDrawn());
            }
        }
    }

    @Test
    @DisplayName("RESP-006: The system follows the fixed order of play P1, then P2, then P3, then P4, then repeats with P1")
    void RESP_006() {
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
    void RESP_007() {
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

        //Force Plague event card to avoid other actions being carried out
        Card card1 = new PlagueCard();
        game.getEventDeck().getDeck().addFirst(card1);

        //Added in REFAC-007
        game.processEndOfQuest();

        String expectedOutput = "There are no winner(s).\nGame of Quest's continues...\n" +
                "\nP1's Turn:\n\n" +
                "Drawing Event Card..." +
                "\nYou drew: " + game.getLastEventCardDrawn().displayCardName() + "\n\n";

        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-008: System properly processes when there are no winners and continues with next players turn")
    void RESP_008() {
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
        //REFAC-019: Need to remove option of having Prosperity Card follow through with trimming of hand
        //removing cards from players hand to remove option of trimming
//        game.dealInitial12AdventureCards();

        //Force Plague event card to avoid other actions being carried out
        Card card1 = new PlagueCard();
        game.getEventDeck().getDeck().addFirst(card1);

        //Trigger next player's turn
        game.playTurn();

        //Test last card drawn is an event card
        Card lastCardDrawn = game.getLastEventCardDrawn();
        assertInstanceOf(EventCard.class, lastCardDrawn);

        //Test Last card drawn is the same card last added to the Player's hand
        //Changed to call specifically P1 as the next turn has already been invoked

        //REFAC-013 : REMOVING THIS ASSERTION AS DOES NOT APPLY ANYMORE AS PLAYER HAND SHOULD NOT HAVE EVENT CARD KEPT IN IT
//        Card cardInPlayerHand = game.gameLogic.getPlayer("P1").getHand().getLast();
//        assertEquals(lastCardDrawn, cardInPlayerHand);
    }

    @Test
    @DisplayName("RESP-009-Test-002: When the next player’s turn is triggered, the system draws an event card" +
            "and displays it")
    void RESP_009_test_002() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        //REFAC-019: Updating that the event deck is shuffled before drawing card
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();

        //Force Plague event card to avoid other actions being carried out
        Card card1 = new PlagueCard();
        game.getEventDeck().getDeck().addFirst(card1);

        //Trigger next player's turn
        game.playTurn();

        //Test Event Card drawn is displayed
        String expectedOutput = "P1's Turn:\n\n" +
                "Drawing Event Card...\n" +
                "You drew: " + game.getLastEventCardDrawn().displayCardName() + "\n\n";
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-009: When the next player’s turn is triggered, the system draws an event card and displays it")
    void RESP_009() {
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
    void RESP_010() {
        RESP_010_test_001();
    }

    @Test
    @DisplayName("RESP-011-Test-001: The system ensures that player 1 cannot have a shield count of less than 0")
    void RESP_011_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Test removing shields from every player 1 stays at 0
        Player p1 = game.gameLogic.getPlayer("P1");
        p1.removeShields(2);
        assertEquals(0, p1.getShieldCount());
    }

    @Test
    @DisplayName("RESP-011-Test-002: The system ensures that player 2 cannot have a shield count of less than 0")
    void RESP_011_test_002() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Test removing shields from every player 2 stays at 0
        Player p2 = game.gameLogic.getPlayer("P2");
        p2.removeShields(6);
        assertEquals(0, p2.getShieldCount());
    }

    @Test
    @DisplayName("RESP-011-Test-003: The system ensures that player 3 cannot have a shield count of less than 0")
    void RESP_011_test_003() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Test removing shields from every player 3 stays at 0
        Player p3 = game.gameLogic.getPlayer("P3");
        p3.removeShields(10);
        assertEquals(0, p3.getShieldCount());
    }

    @Test
    @DisplayName("RESP-011-Test-004: The system ensures that player 4 cannot have a shield count of less than 0")
    void RESP_011_test_004() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Test removing shields from every player 4 stays at 0
        Player p4 = game.gameLogic.getPlayer("P4");
        p4.removeShields(1);
        p4.removeShields(1);
        p4.removeShields(1);
        assertEquals(0, p4.getShieldCount());
    }

    @Test
    @DisplayName("RESP-011: The system ensures that players cannot have a shield count of less than 0")
    void RESP_011() {
        RESP_011_test_001();
        RESP_011_test_002();
        RESP_011_test_003();
        RESP_011_test_004();
    }

    @Test
    @DisplayName("RESP-015-Test-001: System displays the hand of Player 1")
    void RESP_015_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Display Player's hand
        game.displayCurrentPlayerHand();

        //Test player 1 hand is displayed
        ArrayList<Card> p1Hand = game.getCurrentPlayer().getHand();
        StringBuilder expectedOutput = new StringBuilder("P1 hand: ");
        for (int i = 0; i < p1Hand.size(); i++) {
            expectedOutput.append(p1Hand.get(i).displayCardName());
            if (i < p1Hand.size() - 1) {
                expectedOutput.append(" ");
            }
        }
        expectedOutput.append("\n");
        expectedOutput.append("\n");

        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput.toString(), output);
    }

    @Test
    @DisplayName("RESP-015-Test-002: System displays the hand of Player 2")
    void RESP_015_test_002() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Force Player 2 is current player
        game.gameLogic.nextTurn();

        //Display Player's hand
        game.displayCurrentPlayerHand();

        //Test player 2 hand is displayed
        ArrayList<Card> p2Hand = game.getCurrentPlayer().getHand();
        StringBuilder expectedOutput = new StringBuilder("P2 hand: ");
        for (int i = 0; i < p2Hand.size(); i++) {
            expectedOutput.append(p2Hand.get(i).displayCardName());
            if (i < p2Hand.size() - 1) {
                expectedOutput.append(" ");
            }
        }
        expectedOutput.append("\n");
        expectedOutput.append("\n");

        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput.toString(), output);
    }

    @Test
    @DisplayName("RESP-015-Test-003: System displays the hand of Player 3")
    void RESP_015_test_003() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Force Player 3 is current player
        game.gameLogic.nextTurn();
        game.gameLogic.nextTurn();

        //Display Player's hand
        game.displayCurrentPlayerHand();

        //Test player 3 hand is displayed
        ArrayList<Card> p3Hand = game.getCurrentPlayer().getHand();
        StringBuilder expectedOutput = new StringBuilder("P3 hand: ");
        for (int i = 0; i < p3Hand.size(); i++) {
            expectedOutput.append(p3Hand.get(i).displayCardName());
            if (i < p3Hand.size() - 1) {
                expectedOutput.append(" ");
            }
        }
        expectedOutput.append("\n");
        expectedOutput.append("\n");

        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput.toString(), output);
    }

    @Test
    @DisplayName("RESP-015-Test-004: System displays the hand of Player 4")
    void RESP_015_test_004() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Force Player 4 is current player
        game.gameLogic.nextTurn();
        game.gameLogic.nextTurn();
        game.gameLogic.nextTurn();

        //Display Player's hand
        game.displayCurrentPlayerHand();

        //Test player 4 hand is displayed
        ArrayList<Card> p4Hand = game.getCurrentPlayer().getHand();
        StringBuilder expectedOutput = new StringBuilder("P4 hand: ");
        for (int i = 0; i < p4Hand.size(); i++) {
            expectedOutput.append(p4Hand.get(i).displayCardName());
            if (i < p4Hand.size() - 1) {
                expectedOutput.append(" ");
            }
        }
        expectedOutput.append("\n");
        expectedOutput.append("\n");

        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput.toString(), output);
    }

    @Test
    @DisplayName("RESP-015: System displays the hand of the current player")
    void RESP_015() {
        RESP_015_test_001();
        RESP_015_test_002();
        RESP_015_test_003();
        RESP_015_test_004();
    }

    @Test
    @DisplayName("RESP-016-Test-001: System displays the players hand with foes listed first in increasing order, followed " +
            "by weapons in increasing order, with swords before horses.")
    void RESP_016_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Force Player 4 is current player
        game.gameLogic.nextTurn();
        game.gameLogic.nextTurn();
        game.gameLogic.nextTurn();

        //Add each type of Adventure Card to Player hand
        ArrayList<Card> playerHand = game.getCurrentPlayer().getHand();
        playerHand.clear();
        playerHand.add(new FoeCard(5));
        playerHand.add(new FoeCard(10));
        playerHand.add(new FoeCard(15));
        playerHand.add(new FoeCard(20));
        playerHand.add(new FoeCard(25));
        playerHand.add(new FoeCard(30));
        playerHand.add(new FoeCard(35));
        playerHand.add(new FoeCard(40));
        playerHand.add(new FoeCard(50));
        playerHand.add(new FoeCard(70));
        playerHand.add(new WeaponCard("D", 5));
        playerHand.add(new WeaponCard("H", 10));
        playerHand.add(new WeaponCard("S", 10));
        playerHand.add(new WeaponCard("B", 15));
        playerHand.add(new WeaponCard("L", 20));
        playerHand.add(new WeaponCard("E", 30));
        Collections.shuffle(playerHand);

        //Display Player's hand
        game.displayCurrentPlayerHand();

        //Test player 4 hand is displayed in the right order
        String expectedOutput = "P4 hand: F5 F10 F15 F20 F25 F30 F35 F40 F50 F70 D5 S10 H10 B15 L20 E30\n\n";
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-016: System displays the players hand with foes listed first in increasing order, followed " +
            "by weapons in increasing order, with swords before horses.")
    void RESP_016() {
        RESP_016_test_001();
    }

    @Test
    @DisplayName("RESP-017-Test-001: System computes the number of cards to discard (n) from Player 1 hand")
    void RESP_017_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Add 1 more card to Player 1 hand
        String playerID = game.getCurrentPlayer().getPlayerID();
        Card card1 = game.drawAdventureCard(playerID);

        int n = game.computeNumberOfCardsToDiscard(playerID);
        //Test n = 1
        assertEquals(1, n);
    }

    @Test
    @DisplayName("RESP-017-Test-002: System computes the number of cards to discard (n) from Player 2 hand")
    void RESP_017_test_002() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Add 4 more cards to Player 2 hand
        game.gameLogic.nextTurn();
        String playerID = game.getCurrentPlayer().getPlayerID();
        Card card1 = game.drawAdventureCard(playerID);
        Card card2 = game.drawAdventureCard(playerID);
        Card card3 = game.drawAdventureCard(playerID);
        Card card4 = game.drawAdventureCard(playerID);

        int n = game.computeNumberOfCardsToDiscard(playerID);
        //Test n = 4
        assertEquals(4, n);
    }

    @Test
    @DisplayName("RESP-017-Test-003: System computes the number of cards to discard (n) from Player 3 hand")
    void RESP_017_test_003() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //No cards added to Player 3 hand
        game.gameLogic.nextTurn();
        game.gameLogic.nextTurn();
        String playerID = game.getCurrentPlayer().getPlayerID();

        int n = game.computeNumberOfCardsToDiscard(playerID);
        //Test n = 0
        assertEquals(0, n);
    }

    @Test
    @DisplayName("RESP-017-Test-004: System computes the number of cards to discard (n) from Player 4 hand")
    void RESP_017_test_004() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //No cards added to Player 4 hand
        game.gameLogic.nextTurn();
        game.gameLogic.nextTurn();
        game.gameLogic.nextTurn();
        String playerID = game.getCurrentPlayer().getPlayerID();
        for (int i = 0; i < 14; i++) {
            Card card = game.drawAdventureCard(playerID);
        }

        int n = game.computeNumberOfCardsToDiscard(playerID);
        //Test n = 14
        assertEquals(14, n);
    }

    @Test
    @DisplayName("RESP-017: System computes the number of cards to discard (n)")
    void RESP_017() {
        RESP_017_test_001();
        RESP_017_test_002();
        RESP_017_test_003();
        RESP_017_test_004();
    }

    @Test
    @DisplayName("RESP-018-Test-001: System displays player’s hand and prompts the player to discard card(s) to trim hand")
    void RESP_018_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        adventureDeck.shuffle();
        game.setPlayers();

        //Add 3 cards to Player 1 hand
        String playerID = game.getCurrentPlayer().getPlayerID();
        ArrayList<Card> playerHand = game.getCurrentPlayer().getHand();
        //Add 12 cards
        playerHand.clear();
        playerHand.add(new FoeCard(5));
        playerHand.add(new FoeCard(10));
        playerHand.add(new FoeCard(15));
        playerHand.add(new FoeCard(20));
        playerHand.add(new FoeCard(25));
        playerHand.add(new FoeCard(30));
        playerHand.add(new FoeCard(35));
        playerHand.add(new FoeCard(40));
        playerHand.add(new FoeCard(50));
        playerHand.add(new FoeCard(70));
        playerHand.add(new WeaponCard("D", 5));
        playerHand.add(new WeaponCard("H", 10));
        //Add F50, D5, L20
        playerHand.add(new FoeCard(50));
        playerHand.add(new WeaponCard("D", 5));
        playerHand.add(new WeaponCard("L", 20));

        //Display hand
        game.displayCurrentPlayerHand();
        //Prompt user to discard
        int n = game.computeNumberOfCardsToDiscard(playerID);
        game.gameDisplay.promptForDiscardCards(n);
        //Get input from user 3 times
        String userInput1 = "F50\n";
        String userInput2 = "D5\n";
        String userInput3 = "L20\n";

        //Test User input matches what is received each time
        String cardsToDiscard1 = game.gameDisplay.getDiscardInput(new Scanner(userInput1));
        assertEquals(cardsToDiscard1, game.gameDisplay.lastInput);

        String cardsToDiscard2 = game.gameDisplay.getDiscardInput(new Scanner(userInput2));
        assertEquals(cardsToDiscard2, game.gameDisplay.lastInput);

        String cardsToDiscard3 = game.gameDisplay.getDiscardInput(new Scanner(userInput3));
        assertEquals(cardsToDiscard3, game.gameDisplay.lastInput);

        String expectedOutput = "P1 hand: F5 F10 F15 F20 F25 F30 F35 F40 F50 F50 F70 D5 D5 H10 L20\n\n" +
                "Discard 3 cards\n\n" +
                "Type out cards in the format as it appears in your hand\n" +
                "For Example: 'F5' (WITHOUT '' around the card name)\n" +
                "then press the <return> key:\n\n";

        //Test display hand and prompts discard
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-018: System displays player’s hand and prompts the player to discard card(s) to trim hand")
    void RESP_018() {
        RESP_018_test_001();
    }

    @Test
    @DisplayName("RESP-019-Test-001: System removes card from the hand and discards it")
    void RESP_019_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        adventureDeck.shuffle();
        game.setPlayers();

        //Add 3 cards to Player 1 hand
        String playerID = game.getCurrentPlayer().getPlayerID();
        ArrayList<Card> playerHand = game.getCurrentPlayer().getHand();
        //Add 12 cards
        playerHand.clear();
        playerHand.add(new FoeCard(5));
        playerHand.add(new FoeCard(10));
        playerHand.add(new FoeCard(15));
        playerHand.add(new FoeCard(20));
        playerHand.add(new FoeCard(25));
        playerHand.add(new FoeCard(30));
        playerHand.add(new FoeCard(35));
        playerHand.add(new FoeCard(40));
        playerHand.add(new FoeCard(50));
        playerHand.add(new FoeCard(70));
        playerHand.add(new WeaponCard("D", 5));
        playerHand.add(new WeaponCard("H", 10));
        //Add F50, D5, L20
        playerHand.add(new FoeCard(50));
        playerHand.add(new WeaponCard("D", 5));
        playerHand.add(new WeaponCard("L", 20));

        //Get input from user for 1 card
        String userInput = "F50\n";
        String cardsToDiscard = game.gameDisplay.getDiscardInput(new Scanner(userInput));

        //Test player hand has 15 cards in it
        Player player = game.getCurrentPlayer();
        assertEquals(15, player.getHandSize());

        //Remove cards and discard
        game.gameLogic.removeCardsAndDiscard(cardsToDiscard, playerID);

        //Test player hand has 14 cards in it
        assertEquals(14, player.getHandSize());
    }

    @Test
    @DisplayName("RESP-019-Test-002: System removes card from the hand and discards it in adventure discard pile")
    void RESP_019_test_002() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        adventureDeck.shuffle();
        game.setPlayers();

        //Add 3 cards to Player 1 hand
        String playerID = game.getCurrentPlayer().getPlayerID();
        ArrayList<Card> playerHand = game.getCurrentPlayer().getHand();
        //Add 12 cards
        playerHand.clear();
        playerHand.add(new FoeCard(5));
        playerHand.add(new FoeCard(10));
        playerHand.add(new FoeCard(15));
        playerHand.add(new FoeCard(20));
        playerHand.add(new FoeCard(25));
        playerHand.add(new FoeCard(30));
        playerHand.add(new FoeCard(35));
        playerHand.add(new FoeCard(40));
        playerHand.add(new FoeCard(50));
        playerHand.add(new FoeCard(70));
        playerHand.add(new WeaponCard("D", 5));
        playerHand.add(new WeaponCard("H", 10));
        //Add F50, D5, L20
        playerHand.add(new FoeCard(50));
        playerHand.add(new WeaponCard("D", 5));
        playerHand.add(new WeaponCard("L", 20));

        //Get input from user for 1 card
        String userInput1 = "D5\n";
        String cardToDiscard1 = game.gameDisplay.getDiscardInput(new Scanner(userInput1));

        //Test discard pile size is 0
        assertEquals(0, game.gameLogic.getAdventureDeck().getDiscardPileSize());
        //Remove card D5 and discard
        game.gameLogic.removeCardsAndDiscard(cardToDiscard1, playerID);
        //Test discard pile size is 1
        assertEquals(1, game.gameLogic.getAdventureDeck().getDiscardPileSize());

        //Test same thing for a second card
        String userInput2 = "L20\n";
        String cardToDiscard2 = game.gameDisplay.getDiscardInput(new Scanner(userInput2));

        //Test discard pile size is 1
        assertEquals(1, game.gameLogic.getAdventureDeck().getDiscardPileSize());
        //Remove card L20 and discard
        game.gameLogic.removeCardsAndDiscard(cardToDiscard2, playerID);
        //Test discard pile size is 2
        assertEquals(2, game.gameLogic.getAdventureDeck().getDiscardPileSize());
    }
//Moved to below most recent RESP-019 test
//    @Test
//    @DisplayName("RESP-019: System removes card from the hand and discards it in the correct discard pile")
//    void RESP_019() {
//        RESP_019_test_001();
//        RESP_019_test_002();
//    }

    @Test
    @DisplayName("RESP-020-Test-001: System displays the trimmed hand")
    void RESP_020_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        adventureDeck.shuffle();
        game.setPlayers();

        //Add 3 extra cards to Player 1 hand
        String playerID = game.getCurrentPlayer().getPlayerID();
        ArrayList<Card> playerHand = game.getCurrentPlayer().getHand();
        //Add 12 cards
        playerHand.clear();
        playerHand.add(new FoeCard(5));
        playerHand.add(new FoeCard(10));
        playerHand.add(new FoeCard(15));
        playerHand.add(new FoeCard(20));
        playerHand.add(new FoeCard(25));
        playerHand.add(new FoeCard(30));
        playerHand.add(new FoeCard(35));
        playerHand.add(new FoeCard(40));
        playerHand.add(new FoeCard(50));
        playerHand.add(new FoeCard(70));
        playerHand.add(new WeaponCard("D", 5));
        playerHand.add(new WeaponCard("H", 10));
        //Add F50, D5, L20
        playerHand.add(new FoeCard(50));
        playerHand.add(new WeaponCard("D", 5));
        playerHand.add(new WeaponCard("L", 20));

        //Get input from user 3 times
        String userInput1 = "F50\n";
        String userInput2 = "D5\n";
        String userInput3 = "L20\n";

        //Get user input for trimming
        String cardToDiscard1 = game.gameDisplay.getDiscardInput(new Scanner(userInput1));
        game.gameLogic.removeCardsAndDiscard(cardToDiscard1, playerID);
        String cardToDiscard2 = game.gameDisplay.getDiscardInput(new Scanner(userInput2));
        game.gameLogic.removeCardsAndDiscard(cardToDiscard2, playerID);
        String cardToDiscard3 = game.gameDisplay.getDiscardInput(new Scanner(userInput3));
        game.gameLogic.removeCardsAndDiscard(cardToDiscard3, playerID);

        //Display Trimmed Hand
        game.displayTrimmedHand(playerID);

        String expectedOutput = "Trimming Complete... Here is your new hand!\n\n" +   //Had to add an extra newline char
                "P1 hand: F5 F10 F15 F20 F25 F30 F35 F40 F50 F70 D5 H10\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-020: System displays the trimmed hand")
    void RESP_020() {
        RESP_020_test_001();
    }

    @Test
    @DisplayName("RESP-012-Test-001: System carries out Event card action for Queen’s Favor current player draws " +
            "2 adventure cards and does not trim hand")
    void RESP_012_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();

        //Add Queen's Favor Card as first card that will be drawn from Event Deck
        Card queenFavorCard = new QueensFavorCard();
        eventDeck.cards.addFirst(queenFavorCard);

        //Test that before Queens Favor Card player has 0 cards in their hand
        assertEquals(0, game.getCurrentPlayer().getHandSize());

        //Get PlayerID before next turn is invoked
        String playerID = game.getCurrentPlayer().getPlayerID();

        //Trigger player 1 turn
        game.playTurn();

        //Test trim hand for player is not invoked
        int handSize2 = game.gameLogic.getPlayer(playerID).getHandSize();
        //Test that after Queens Favor Card player has 2 cards in their hand
        assertEquals(2, handSize2);
    }

    @Test
    @DisplayName("RESP-012-Test-002: System carries out Event card action for Queen’s Favor current player draws 2 " +
            "adventure cards and does trim hand")
    void RESP_012_test_002() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Force Player 3 drawing Queen favor
        game.gameLogic.nextTurn();
        game.gameLogic.nextTurn();

        //Get PlayerID before next turn is invoked
        String playerID = game.getCurrentPlayer().getPlayerID();

        //Remove 2 cards from Player 3 Hand and add F5 and H10 to ensure they can be discarded in the trim
        for (int i = 0; i < 2; i++) {
            Card card = game.getCurrentPlayer().getHand().getFirst();
            game.discardAdventureCard(playerID, card);
        }
        game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
        game.gameLogic.getPlayer(playerID).addCardToHand(new WeaponCard("H", 10));

        String userInput = "F5\nH10\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Add Queen's Favor Card as first card that will be drawn from Event Deck
        Card queenFavorCard = new QueensFavorCard();
        eventDeck.cards.addFirst(queenFavorCard);

        //Player hand size
        int handSize1 = game.gameLogic.getPlayer(playerID).getHandSize();
        //Test that before Queens Favor Card player has 0 cards in their hand
        assertEquals(12, handSize1);

        //Trigger player 3 turn
        game.playTurn();

        //Player hand size
        int handSize2 = game.gameLogic.getPlayer(playerID).getHandSize();

        //Trim hand for player in invoked
        //Test player has been trimmed down to 12 cards again
        assertEquals(12, handSize2);
    }

    @Test
    @DisplayName("RESP-012: System carries out Event card action for Queen’s Favor (current player draws 2 adventure " +
            "cards and possibly trims hand)")
    void RESP_012() {
        RESP_012_test_001();
        RESP_012_test_002();
    }

    @Test
    @DisplayName("RESP-013-Test-001: System carries out Event card action for Prosperity (all players draw 2 adventure cards " +
            "and no players trim hand)")
    void RESP_013_test_001() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();

        //Player 2 drawing Prosperity card
        game.gameLogic.nextTurn();

        //Add Prosperity Card as first card that will be drawn from Event Deck
        Card prosperity = new ProsperityCard();
        eventDeck.cards.addFirst(prosperity);

        //Test that all players have 0 cards in their hand before Prosperity Card drawn
        assertEquals(0, game.gameLogic.getPlayer("P1").getHandSize());
        assertEquals(0, game.gameLogic.getPlayer("P2").getHandSize());
        assertEquals(0, game.gameLogic.getPlayer("P3").getHandSize());
        assertEquals(0, game.gameLogic.getPlayer("P4").getHandSize());

        //player 2 turn
        game.playTurn();

        //Test that all players have 2 cards in their hand after Prosperity Card drawn
        assertEquals(2, game.gameLogic.getPlayer("P1").getHandSize());
        assertEquals(2, game.gameLogic.getPlayer("P2").getHandSize());
        assertEquals(2, game.gameLogic.getPlayer("P3").getHandSize());
        assertEquals(2, game.gameLogic.getPlayer("P4").getHandSize());
    }

    @Test
    @DisplayName("RESP-013-Test-002: System carries out Event card action for Prosperity (all players draw 2 adventure cards " +
            "and all players trim hand)")
    void RESP_013_test_002() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        //Remove 2 cards from all players hands and add F5 and H10 to ensure they can be discarded in the trim
        for (int i = 0; i < 2; i++) {
            Card card1 = game.gameLogic.getPlayerHand("P1").getFirst();
            Card card2 = game.gameLogic.getPlayerHand("P2").getFirst();
            Card card3 = game.gameLogic.getPlayerHand("P3").getFirst();
            Card card4 = game.gameLogic.getPlayerHand("P4").getFirst();
            game.discardAdventureCard("P1", card1);
            game.discardAdventureCard("P2", card2);
            game.discardAdventureCard("P3", card3);
            game.discardAdventureCard("P4", card4);
        }
        game.gameLogic.getPlayer("P1").addCardToHand(new FoeCard(5));
        game.gameLogic.getPlayer("P1").addCardToHand(new WeaponCard("H", 10));
        game.gameLogic.getPlayer("P2").addCardToHand(new FoeCard(5));
        game.gameLogic.getPlayer("P2").addCardToHand(new WeaponCard("H", 10));
        game.gameLogic.getPlayer("P3").addCardToHand(new FoeCard(5));
        game.gameLogic.getPlayer("P3").addCardToHand(new WeaponCard("H", 10));
        game.gameLogic.getPlayer("P4").addCardToHand(new FoeCard(5));
        game.gameLogic.getPlayer("P4").addCardToHand(new WeaponCard("H", 10));

        String userInput = "F5\nH10\nF5\nH10\nF5\nH10\nF5\nH10\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Player 2 drawing Prosperity card
        game.gameLogic.nextTurn();

        //Add Prosperity Card as first card that will be drawn from Event Deck
        Card prosperity = new ProsperityCard();
        eventDeck.cards.addFirst(prosperity);

        //Get all players hand before Prosperity card is drawn
        ArrayList<Card> p1Hand = game.gameLogic.getPlayer("P1").getHand();
        ArrayList<Card> p2Hand = game.gameLogic.getPlayer("P2").getHand();
        ArrayList<Card> p3Hand = game.gameLogic.getPlayer("P3").getHand();
        ArrayList<Card> p4Hand = game.gameLogic.getPlayer("P4").getHand();

        //Test that all players have 12 cards in their hand before Prosperity Card drawn
        assertEquals(12, game.gameLogic.getPlayer("P1").getHandSize());
        assertEquals(12, game.gameLogic.getPlayer("P2").getHandSize());
        assertEquals(12, game.gameLogic.getPlayer("P3").getHandSize());
        assertEquals(12, game.gameLogic.getPlayer("P4").getHandSize());

        //player 2 turn
        game.playTurn();

        //Test that all players have 12 cards in their hand after Prosperity Card drawn
        assertEquals(12, game.gameLogic.getPlayer("P1").getHandSize());
        assertEquals(12, game.gameLogic.getPlayer("P2").getHandSize());
        assertEquals(12, game.gameLogic.getPlayer("P3").getHandSize());
        assertEquals(12, game.gameLogic.getPlayer("P4").getHandSize());
    }

    @Test
    @DisplayName("RESP-013: System carries out Event card action for Prosperity (all players draw 2 adventure cards " +
            "and possibly trim hand)")
    void RESP_013() {
        RESP_013_test_001();
        RESP_013_test_002();
    }

    @Test
    @DisplayName("RESP-041-Test-001: System checks that if the sponsor enters ‘Quit’ and the stage is valid then it" +
            " displays the cards that are used in the stages of the quest")
    void RESP_041_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-05 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsBuildingQuest(game);

        //Force Player 4 to be sponsoring Quest
        helper.forcePlayerTurn(game, 4);

        //Input quit
        String userInput = "Quit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Force a Quest card to be the first event card to be drawn
        QuestCard questCard = new QuestCard(2);
        game.gameLogic.getEventDeck().cards.addFirst(questCard);

        //Player 4 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Number of stages from Last Quest card
        Card card = game.gameLogic.drawCard(playerID, game.getEventDeck());
        int numberOfStages = questCard.getStages();
        //Test these are the same cards
        assertEquals(questCard, card);

        //Add cards to players hand and to the quest being built
        Card card1 = new FoeCard(5);
        Card card2 = new FoeCard(10);
        Card card3 = new WeaponCard("H", 10);
        game.getCurrentPlayer().addCardToHand(card1);
        game.getCurrentPlayer().addCardToHand(card2);
        game.getCurrentPlayer().addCardToHand(card3);

        //Add to have Hashmap of questBuilt passed in to method
        HashMap<Integer, ArrayList<Card>> questBuilt = new HashMap<Integer, ArrayList<Card>>();
        ArrayList<Card> stage1 = new ArrayList<>();
        stage1.add(card1);
        ArrayList<Card> stage2 = new ArrayList<>();
        stage2.add(card2);
        stage2.add(card3);

        //build hashmap
        questBuilt.put(1, stage1);
        questBuilt.put(2, stage2);

        game.stageIsValidAndDisplayCards(questBuilt);

        String expectedOutput = "Stage set up is completed...\n\n" +
                "STAGE 1:\n" +
                "Cards = F5\n" +
                "Value = 5\n\n" +
                "STAGE 2:\n" +
                "Cards = F10 H10\n" +
                "Value = 20\n\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-041: System checks that if the sponsor enters ‘Quit’ and the stage is valid then it displays" +
            " the cards that are used in the stages of the quest")
    void RESP_041() {
        RESP_041_test_001();
    }

    @Test
    @DisplayName("RESP-035-Test-001: System displays sponsor’s hand and prompts the sponsor to select a card " +
            "or enter ‘Quit’ to end a stage setup. System must ensure that the sponsor selected card is a valid one " +
            "(a single Foe card and zero or more non-repeated weapon cards)")
    void RESP_035_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-05 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsBuildingQuest(game);

        //Force Player 2 to be sponsoring Quest
        helper.forcePlayerTurn(game, 2);

        //Input quit
        String userInput = "F5\nD5\nQuit\nF10\nH10\nQuit\nF15\nS10\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Force a Quest card to be the first event card to be drawn
        QuestCard questCard = new QuestCard(3);
        game.gameLogic.getEventDeck().cards.addFirst(questCard);

        //Player 2 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Number of stages from Last Quest card
        Card card = game.gameLogic.drawCard(playerID, game.getEventDeck());
        //Test these are the same cards
        assertEquals(questCard, card);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new FoeCard(5);
        Card card2 = new WeaponCard("D", 5);
        Card card3 = new FoeCard(10);
        Card card4 = new WeaponCard("H", 10);
        Card card5 = new FoeCard(15);
        Card card6 = new WeaponCard("S", 10);
        game.getCurrentPlayer().addCardToHand(card1);
        game.getCurrentPlayer().addCardToHand(card2);
        game.getCurrentPlayer().addCardToHand(card3);
        game.getCurrentPlayer().addCardToHand(card4);
        game.getCurrentPlayer().addCardToHand(card5);
        game.getCurrentPlayer().addCardToHand(card6);

        game.displaySponsorHandAndSetUpStages(playerID, questCard.getStages());

        String expectedOutput = "Building a Quest with 3 Stages...\n\n" +
                "Building Stage 1:\n\n" +
                "P2 hand: F5 F10 F15 D5 S10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "F5 added to Stage...\n" +
                "Stage 1 Card(s): F5\n\n" +
                "P2 hand: F5 F10 F15 D5 S10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "D5 added to Stage...\n" +
                "Stage 1 Card(s): F5 D5\n\n" +
                "P2 hand: F5 F10 F15 D5 S10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "Building Stage 2:\n\n" +
                "P2 hand: F5 F10 F15 D5 S10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "F10 added to Stage...\n" +
                "Stage 2 Card(s): F10\n\n" +
                "P2 hand: F5 F10 F15 D5 S10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "H10 added to Stage...\n" +
                "Stage 2 Card(s): F10 H10\n\n" +
                "P2 hand: F5 F10 F15 D5 S10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "Building Stage 3:\n\n" +
                "P2 hand: F5 F10 F15 D5 S10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "F15 added to Stage...\n" +
                "Stage 3 Card(s): F15\n\n" +
                "P2 hand: F5 F10 F15 D5 S10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "S10 added to Stage...\n" +
                "Stage 3 Card(s): F15 S10\n\n" +
                "P2 hand: F5 F10 F15 D5 S10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-035: System displays sponsor’s hand and prompts the sponsor to select a card position or " +
            "enter ‘Quit’ to end a stage setup. System must ensure that the sponsor selected card is a valid one " +
            "(a single Foe card and zero or more non-repeated weapon cards)")
    void RESP_035() {
        RESP_035_test_001();
    }

    @Test
    @DisplayName("RESP-036-Test-001: System must ensure that when sponsor’s select an invalid card its" +
            " reasoning is explained to the user and re-prompted. 2 Foe's & Repeating weapon cards")
    void RESP_036_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-05 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsBuildingQuest(game);

        //Force Player 3 to be sponsoring Quest
        helper.forcePlayerTurn(game, 3);

        //Input quit
        String userInput = "F5\nF5\nQuit\nF10\nH10\nH10\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Force a Quest card to be the first event card to be drawn
        QuestCard questCard = new QuestCard(2);
        game.gameLogic.getEventDeck().cards.addFirst(questCard);

        //Player 3 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Number of stages from Last Quest card
        Card card = game.gameLogic.drawCard(playerID, game.getEventDeck());
        //Test these are the same cards
        assertEquals(questCard, card);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new FoeCard(5);
        Card card2 = new FoeCard(5);
        Card card3 = new FoeCard(10);
        Card card4 = new WeaponCard("H", 10);
        Card card5 = new WeaponCard("H", 10);
        game.getCurrentPlayer().addCardToHand(card1);
        game.getCurrentPlayer().addCardToHand(card2);
        game.getCurrentPlayer().addCardToHand(card3);
        game.getCurrentPlayer().addCardToHand(card4);
        game.getCurrentPlayer().addCardToHand(card5);

        game.displaySponsorHandAndSetUpStages(playerID, questCard.getStages());

        String expectedOutput = "Building a Quest with 2 Stages...\n\n" +
                "Building Stage 1:\n\n" +
                "P3 hand: F5 F5 F10 H10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "F5 added to Stage...\n" +
                "Stage 1 Card(s): F5\n\n" +
                "P3 hand: F5 F5 F10 H10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "There is already a Foe card in this stage. Try Again.\n\n" +
                "P3 hand: F5 F5 F10 H10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "Building Stage 2:\n\n" +
                "P3 hand: F5 F5 F10 H10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "F10 added to Stage...\n" +
                "Stage 2 Card(s): F10\n\n" +
                "P3 hand: F5 F5 F10 H10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "H10 added to Stage...\n" +
                "Stage 2 Card(s): F10 H10\n\n" +
                "P3 hand: F5 F5 F10 H10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "There is already that same Weapon card in this stage. Try Again.\n\n" +
                "P3 hand: F5 F5 F10 H10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-036: System must ensure that when sponsor’s select an invalid card its reasoning is " +
            "explained to the user and re-prompted")
    void RESP_036() {
        RESP_036_test_001();
    }

    @Test
    @DisplayName("RESP-037-Test-001: System must add the selected valid card to the current stage and display " +
            "the updated set of cards")
    void RESP_037_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-05 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsBuildingQuest(game);

        //Force Player 1 to be sponsoring Quest
        helper.forcePlayerTurn(game, 1);

        //Input quit
        String userInput = "F5\nQuit\nF10\nH10\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Force a Quest card to be the first event card to be drawn
        QuestCard questCard = new QuestCard(2);
        game.gameLogic.getEventDeck().cards.addFirst(questCard);

        //Player 1 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Number of stages from Last Quest card
        Card card = game.gameLogic.drawCard(playerID, game.getEventDeck());
        //Test these are the same cards
        assertEquals(questCard, card);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new FoeCard(5);
        Card card2 = new FoeCard(10);
        Card card3 = new WeaponCard("H", 10);
        game.getCurrentPlayer().addCardToHand(card1);
        game.getCurrentPlayer().addCardToHand(card2);
        game.getCurrentPlayer().addCardToHand(card3);

        game.displaySponsorHandAndSetUpStages(playerID, questCard.getStages());

        String expectedOutput = "Building a Quest with 2 Stages...\n\n" +
                "Building Stage 1:\n\n" +
                "P1 hand: F5 F10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "F5 added to Stage...\n" +
                "Stage 1 Card(s): F5\n\n" +
                "P1 hand: F5 F10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "Building Stage 2:\n\n" +
                "P1 hand: F5 F10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "F10 added to Stage...\n" +
                "Stage 2 Card(s): F10\n\n" +
                "P1 hand: F5 F10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "H10 added to Stage...\n" +
                "Stage 2 Card(s): F10 H10\n\n" +
                "P1 hand: F5 F10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }
//Moved this to the bottom of the file for an additional test
//    @Test
//    @DisplayName("RESP-037: System must add the selected valid card to the current stage and display the " +
//            "updated set of cards")
//    void RESP_037() {
//        RESP_037_test_001();
//    }

    @Test
    @DisplayName("RESP-038-Test-001: System displays ‘A stage cannot be empty’ message when sponsor" +
            " enters ‘Quit’ with no cards in the stage ")
    void RESP_038_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-05 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsBuildingQuest(game);

        //Force Player 1 to be sponsoring Quest
        helper.forcePlayerTurn(game, 1);

        //Input quit
        String userInput = "Quit\nF5\nQuit\nQuit\nF10\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Force a Quest card to be the first event card to be drawn
        QuestCard questCard = new QuestCard(2);
        game.gameLogic.getEventDeck().cards.addFirst(questCard);

        //Player 1 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Number of stages from Last Quest card
        Card card = game.gameLogic.drawCard(playerID, game.getEventDeck());
        //Test these are the same cards
        assertEquals(questCard, card);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new FoeCard(5);
        Card card2 = new FoeCard(10);
        Card card3 = new WeaponCard("H", 10);
        game.getCurrentPlayer().addCardToHand(card1);
        game.getCurrentPlayer().addCardToHand(card2);
        game.getCurrentPlayer().addCardToHand(card3);

        game.displaySponsorHandAndSetUpStages(playerID, questCard.getStages());

        String expectedOutput = "Building a Quest with 2 Stages...\n\n" +
                "Building Stage 1:\n\n" +
                "P1 hand: F5 F10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "A stage cannot be empty\n\n" +
                "P1 hand: F5 F10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "F5 added to Stage...\n" +
                "Stage 1 Card(s): F5\n\n" +
                "P1 hand: F5 F10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "Building Stage 2:\n\n" +
                "P1 hand: F5 F10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "A stage cannot be empty\n\n" +
                "P1 hand: F5 F10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "F10 added to Stage...\n" +
                "Stage 2 Card(s): F10\n\n" +
                "P1 hand: F5 F10 H10\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-038: System displays ‘A stage cannot be empty’ message when sponsor enters ‘Quit’ with no cards in the stage ")
    void RESP_038() {
        RESP_038_test_001();
    }

    @Test
    @DisplayName("RESP-039-Test-001: System compares the value of the current stage with the previous stage " +
            "(if any) and validate that the value is greater. Valid Case greater than last (prev = 5, current = 20)")
    void RESP_039_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-05 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsBuildingQuest(game);

        ArrayList<String> stageCards = new ArrayList<>();
        stageCards.add("F5");
        stageCards.add("D5");
        stageCards.add("H10");

        boolean isGreater = game.gameLogic.compareCurrentStageValueIsGreaterThanPrevious(stageCards, 5);
        assertEquals(isGreater, true);
    }

    @Test
    @DisplayName("RESP-039-Test-002: System compares the value of the current stage with the previous stage " +
            "(if any) and validate that the value is greater. valid case (prev = 0, current = 10)")
    void RESP_039_test_002() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-05 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsBuildingQuest(game);

        ArrayList<String> stageCards = new ArrayList<>();
        stageCards.add("F5");
        stageCards.add("D5");

        boolean isGreater = game.gameLogic.compareCurrentStageValueIsGreaterThanPrevious(stageCards, 0);
        assertEquals(isGreater, true);
    }

    @Test
    @DisplayName("RESP-039-Test-003: System compares the value of the current stage with the previous stage " +
            "(if any) and validate that the value is equal. valid case (prev = 40, current = 40). Stage = 5")
    void RESP_039_test_003() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-05 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsBuildingQuest(game);

        ArrayList<String> stageCards = new ArrayList<>();
        stageCards.add("F40");

        boolean isGreater = game.gameLogic.compareCurrentStageValueIsGreaterThanPrevious(stageCards, 40);
        assertEquals(isGreater, true);
    }

    @Test
    @DisplayName("RESP-039: System compares the value of the current stage with the previous stage (if any) " +
            "and validate that the value is equal or greater")
    void RESP_039() {
        RESP_039_test_001();
        RESP_039_test_002();
        RESP_039_test_003();
    }

    @Test
    @DisplayName("RESP-040-Test-001: System displays ‘Insufficient value for this stage’ message when sponsor " +
            "enters ‘Quit’ and the stage value is insufficient")
    void RESP_040_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-05 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsBuildingQuest(game);

        //Force Player 4 to be sponsoring Quest
        helper.forcePlayerTurn(game, 4);

        //Input quit
        String userInput = "F15\nQuit\nF5\nQuit\nB15\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Force a Quest card to be the first event card to be drawn
        QuestCard questCard = new QuestCard(2);
        game.gameLogic.getEventDeck().cards.addFirst(questCard);

        //Player 4 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Number of stages from Last Quest card
        Card card = game.gameLogic.drawCard(playerID, game.getEventDeck());
        //Test these are the same cards
        assertEquals(questCard, card);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new FoeCard(15);
        Card card2 = new FoeCard(5);
        Card card3 = new WeaponCard("B", 15);
        game.getCurrentPlayer().addCardToHand(card1);
        game.getCurrentPlayer().addCardToHand(card2);
        game.getCurrentPlayer().addCardToHand(card3);

        game.displaySponsorHandAndSetUpStages(playerID, questCard.getStages());

        String expectedOutput = "Building a Quest with 2 Stages...\n\n" +
                "Building Stage 1:\n\n" +
                "P4 hand: F5 F15 B15\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "F15 added to Stage...\n" +
                "Stage 1 Card(s): F15\n\n" +
                "P4 hand: F5 F15 B15\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "Building Stage 2:\n\n" +
                "P4 hand: F5 F15 B15\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "F5 added to Stage...\n" +
                "Stage 2 Card(s): F5\n\n" +
                "P4 hand: F5 F15 B15\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "Insufficient value for this stage\n\n" +
                "P4 hand: F5 F15 B15\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n" +
                "B15 added to Stage...\n" +
                "Stage 2 Card(s): F5 B15\n\n" +
                "P4 hand: F5 F15 B15\n\n" +
                "Select 1 Foe card and 0 or more non-repeating Weapon cards from your hand to build this stage.\n" +
                "Enter 'Quit' to end this stage setup.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-040: System displays ‘Insufficient value for this stage’ message when sponsor enters " +
            "‘Quit’ and the stage value is insufficient")
    void RESP_040() {
        RESP_040_test_001();
    }

    @Test
    @DisplayName("RESP-042-Test-001: System displays participants hand of cards when setting up a valid attack. Player 1")
    void RESP_042_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 1 to set up attack
        helper.forcePlayerTurn(game, 1);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new FoeCard(5);
        Card card2 = new FoeCard(10);
        Card card3 = new FoeCard(10);
        Card card4 = new FoeCard(15);
        Card card5 = new FoeCard(15);
        Card card6 = new WeaponCard("D", 5);
        Card card7 = new WeaponCard("D", 5);
        Card card8 = new WeaponCard("S", 10);
        Card card9 = new WeaponCard("S", 10);
        Card card10 = new WeaponCard("H", 10);
        Card card11 = new WeaponCard("H", 10);
        Card card12 = new WeaponCard("B", 15);
        game.getCurrentPlayer().addCardToHand(card1);
        game.getCurrentPlayer().addCardToHand(card2);
        game.getCurrentPlayer().addCardToHand(card3);
        game.getCurrentPlayer().addCardToHand(card4);
        game.getCurrentPlayer().addCardToHand(card5);
        game.getCurrentPlayer().addCardToHand(card6);
        game.getCurrentPlayer().addCardToHand(card7);
        game.getCurrentPlayer().addCardToHand(card8);
        game.getCurrentPlayer().addCardToHand(card9);
        game.getCurrentPlayer().addCardToHand(card10);
        game.getCurrentPlayer().addCardToHand(card11);
        game.getCurrentPlayer().addCardToHand(card12);

        //Player 1 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Display hand of participant setting up a Attack
        game.displaySetUpForAttackAndPlayerHand(playerID);

        String expectedOutput = "Setting up an Attack...\n\n" +
                "P1 hand: F5 F10 F10 F15 F15 D5 D5 S10 S10 H10 H10 B15\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-042-Test-002: System displays participants hand of cards when setting up a valid attack. Player 2")
    void RESP_042_test_002() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 2 to set up attack
        helper.forcePlayerTurn(game, 2);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new FoeCard(5);
        Card card2 = new FoeCard(10);
        Card card3 = new FoeCard(10);
        Card card4 = new FoeCard(15);
        Card card5 = new FoeCard(15);
        Card card6 = new WeaponCard("D", 5);
        Card card7 = new WeaponCard("D", 5);
        Card card8 = new WeaponCard("S", 10);
        Card card9 = new WeaponCard("S", 10);
        Card card10 = new WeaponCard("H", 10);
        Card card11 = new WeaponCard("H", 10);
        Card card12 = new WeaponCard("B", 15);
        game.getCurrentPlayer().addCardToHand(card1);
        game.getCurrentPlayer().addCardToHand(card2);
        game.getCurrentPlayer().addCardToHand(card3);
        game.getCurrentPlayer().addCardToHand(card4);
        game.getCurrentPlayer().addCardToHand(card5);
        game.getCurrentPlayer().addCardToHand(card6);
        game.getCurrentPlayer().addCardToHand(card7);
        game.getCurrentPlayer().addCardToHand(card8);
        game.getCurrentPlayer().addCardToHand(card9);
        game.getCurrentPlayer().addCardToHand(card10);
        game.getCurrentPlayer().addCardToHand(card11);
        game.getCurrentPlayer().addCardToHand(card12);

        //Player 2 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Display hand of participant setting up a Attack
        game.displaySetUpForAttackAndPlayerHand(playerID);

        String expectedOutput = "Setting up an Attack...\n\n" +
                "P2 hand: F5 F10 F10 F15 F15 D5 D5 S10 S10 H10 H10 B15\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-042-Test-003: System displays participants hand of cards when setting up a valid attack. Player 3")
    void RESP_042_test_003() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 3 to set up attack
        helper.forcePlayerTurn(game, 3);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new FoeCard(5);
        Card card2 = new FoeCard(10);
        Card card3 = new FoeCard(10);
        Card card4 = new FoeCard(15);
        Card card5 = new FoeCard(15);
        Card card6 = new WeaponCard("D", 5);
        Card card7 = new WeaponCard("D", 5);
        Card card8 = new WeaponCard("S", 10);
        Card card9 = new WeaponCard("S", 10);
        Card card10 = new WeaponCard("H", 10);
        Card card11 = new WeaponCard("H", 10);
        Card card12 = new WeaponCard("B", 15);
        game.getCurrentPlayer().addCardToHand(card1);
        game.getCurrentPlayer().addCardToHand(card2);
        game.getCurrentPlayer().addCardToHand(card3);
        game.getCurrentPlayer().addCardToHand(card4);
        game.getCurrentPlayer().addCardToHand(card5);
        game.getCurrentPlayer().addCardToHand(card6);
        game.getCurrentPlayer().addCardToHand(card7);
        game.getCurrentPlayer().addCardToHand(card8);
        game.getCurrentPlayer().addCardToHand(card9);
        game.getCurrentPlayer().addCardToHand(card10);
        game.getCurrentPlayer().addCardToHand(card11);
        game.getCurrentPlayer().addCardToHand(card12);

        //Player 3 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Display hand of participant setting up a Attack
        game.displaySetUpForAttackAndPlayerHand(playerID);

        String expectedOutput = "Setting up an Attack...\n\n" +
                "P3 hand: F5 F10 F10 F15 F15 D5 D5 S10 S10 H10 H10 B15\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-042-Test-004: System displays participants hand of cards when setting up a valid attack. Player 4")
    void RESP_042_test_004() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 4 to set up attack
        helper.forcePlayerTurn(game, 4);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new FoeCard(5);
        Card card2 = new FoeCard(10);
        Card card3 = new FoeCard(10);
        Card card4 = new FoeCard(15);
        Card card5 = new FoeCard(15);
        Card card6 = new WeaponCard("D", 5);
        Card card7 = new WeaponCard("D", 5);
        Card card8 = new WeaponCard("S", 10);
        Card card9 = new WeaponCard("S", 10);
        Card card10 = new WeaponCard("H", 10);
        Card card11 = new WeaponCard("H", 10);
        Card card12 = new WeaponCard("B", 15);
        game.getCurrentPlayer().addCardToHand(card1);
        game.getCurrentPlayer().addCardToHand(card2);
        game.getCurrentPlayer().addCardToHand(card3);
        game.getCurrentPlayer().addCardToHand(card4);
        game.getCurrentPlayer().addCardToHand(card5);
        game.getCurrentPlayer().addCardToHand(card6);
        game.getCurrentPlayer().addCardToHand(card7);
        game.getCurrentPlayer().addCardToHand(card8);
        game.getCurrentPlayer().addCardToHand(card9);
        game.getCurrentPlayer().addCardToHand(card10);
        game.getCurrentPlayer().addCardToHand(card11);
        game.getCurrentPlayer().addCardToHand(card12);

        //Player 3 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Display hand of participant setting up a Attack
        game.displaySetUpForAttackAndPlayerHand(playerID);

        String expectedOutput = "Setting up an Attack...\n\n" +
                "P4 hand: F5 F10 F10 F15 F15 D5 D5 S10 S10 H10 H10 B15\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-042: System displays participants hand of cards when setting up a valid attack")
    void RESP_042() {
        RESP_042_test_001();
        RESP_042_test_002();
        RESP_042_test_003();
        RESP_042_test_004();
    }

    @Test
    @DisplayName("RESP-043-Test-001: System prompts the participant to select a card or enter ‘Quit’ to end a valid attack." +
            "Enter Quit right away no card entered.")
    void RESP_043_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 4 to set up attack
        helper.forcePlayerTurn(game, 4);

        //Input quit
        String userInput = "Quit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Player 4 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Display prompt the participant to select card or enter 'Quit' to end valid attack
        ArrayList<Card> attackCards = game.promptPlayerToSelectCardOrQuit(playerID);
        assertEquals("Quit", game.gameDisplay.lastInput);

        String expectedOutput = "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-043-Test-002: System prompts the participant to select a card or enter ‘Quit’ to end a valid attack." +
            "Enter card and then quit.")
    void RESP_043_test_002() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 4 to set up attack
        helper.forcePlayerTurn(game, 4);

        //Input quit
        String userInput = "E30\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new WeaponCard("E", 30);
        game.gameLogic.getCurrentPlayer().addCardToHand(card1);

        //Player 4 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Display prompt the participant to select card or enter 'Quit' to end valid attack
        ArrayList<Card> attackCards = game.promptPlayerToSelectCardOrQuit(playerID);
        assertEquals("Quit", game.gameDisplay.lastInput);
        assertEquals(card1, attackCards.getFirst());

        String expectedOutput = "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "E30 added to Attack...\n" +
                "Attack Card(s): E30\n\n" +
                "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-043: System prompts the participant to select a card or enter ‘Quit’ to end a valid attack")
    void RESP_043() {
        RESP_043_test_001();
        RESP_043_test_002();
    }

    @Test
    @DisplayName("RESP-044-Test-001: System must ensure that the participant selected a card " +
            "for an attack is a valid one and display the updated set of card(s). One card in attack")
    void RESP_044_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 2 to set up attack
        helper.forcePlayerTurn(game, 2);

        //Input quit
        String userInput = "D5\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new WeaponCard("D", 5);
        game.gameLogic.getCurrentPlayer().addCardToHand(card1);

        //Player 2 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Display prompt the participant to select card or enter 'Quit' to end valid attack
        ArrayList<Card> attackCards = game.promptPlayerToSelectCardOrQuit(playerID);
        assertEquals(card1, attackCards.getFirst());

        String expectedOutput = "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "D5 added to Attack...\n" +
                "Attack Card(s): D5\n\n" +
                "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-044-Test-002: System must ensure that the participant selected a card for an attack is a valid " +
            "one and display the updated set of card(s). Two cards non-repeating weapon.")
    void RESP_044_test_002() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 1 to set up attack
        helper.forcePlayerTurn(game, 1);

        //Input quit
        String userInput = "D5\nL20\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new WeaponCard("D", 5);
        Card card2 = new WeaponCard("L", 20);
        game.gameLogic.getCurrentPlayer().addCardToHand(card1);
        game.gameLogic.getCurrentPlayer().addCardToHand(card2);

        //Player 1 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Display prompt the participant to select card or enter 'Quit' to end valid attack
        ArrayList<Card> attackCards = game.promptPlayerToSelectCardOrQuit(playerID);
        assertEquals(card1, attackCards.getFirst());

        String expectedOutput = "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "D5 added to Attack...\n" +
                "Attack Card(s): D5\n\n" +
                "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "L20 added to Attack...\n" +
                "Attack Card(s): D5 L20\n\n" +
                "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-044: System must ensure that the participant selected a card for an attack is a valid one " +
            "(i.e. possibly empty set of non-repeated weapon card) and must add the selected valid card for an attack " +
            "and display the updated set of card(s)")
    void RESP_044() {
        RESP_044_test_001();
        RESP_044_test_002();
    }

    @Test
    @DisplayName("RESP-045-Test-001: System must ensure the invalid card selected by participant for attack is " +
            "explained to the user and re-prompted. Repeated weapon card")
    void RESP_045_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 1 to set up attack
        helper.forcePlayerTurn(game, 1);

        //Input quit
        String userInput = "D5\nD5\nL20\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new WeaponCard("D", 5);
        Card card2 = new WeaponCard("D", 5);
        Card card3 = new WeaponCard("L", 20);
        game.gameLogic.getCurrentPlayer().addCardToHand(card1);
        game.gameLogic.getCurrentPlayer().addCardToHand(card2);
        game.gameLogic.getCurrentPlayer().addCardToHand(card3);

        //Player 1 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Display prompt the participant to select card or enter 'Quit' to end valid attack
        ArrayList<Card> attackCards = game.promptPlayerToSelectCardOrQuit(playerID);
        //Test the attack cards do not contain another D5 card
        assertEquals(2, attackCards.size());
        assertEquals(card1.getType(), attackCards.get(0).getType());
        assertEquals(card3.getType(), attackCards.get(1).getType());

        String expectedOutput = "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "D5 added to Attack...\n" +
                "Attack Card(s): D5\n\n" +
                "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "There is already that same Weapon card in this stage. Try Again.\n\n" +
                "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "L20 added to Attack...\n" +
                "Attack Card(s): D5 L20\n\n" +
                "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-045: System must ensure the invalid card selected by participant for attack is explained " +
            "to the user and re-prompted")
    void RESP_045() {
        RESP_045_test_001();
    }

    @Test
    @DisplayName("RESP-046-Test-001: System checks that if the participant setting up an attack enters " +
            "‘Quit’ that the cards (if any) are displayed. no cards displayed for this attack")
    void RESP_046_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 1 to set up attack
        helper.forcePlayerTurn(game, 1);

        //Input quit
        String userInput = "Quit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Player 1 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //participant enters 'quit' and display attack set up
        ArrayList<Card> attackCards = game.getAttackCardsAndDisplayToUser(playerID);
        assertEquals(0, attackCards.size());

        String expectedOutput = "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "Attack set up is completed...\n" +
                "Your Attack: No attack\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-046-Test-002: System checks that if the participant setting up an attack enters " +
            "‘Quit’ that the cards (if any) are displayed. one card added to attack")
    void RESP_046_test_002() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 1 to set up attack
        helper.forcePlayerTurn(game, 1);

        //Input quit
        String userInput = "D5\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new WeaponCard("D", 5);
        game.gameLogic.getCurrentPlayer().addCardToHand(card1);

        //Player 1 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Display prompt the participant to select card or enter 'Quit' to end valid attack
        ArrayList<Card> attackCards = game.getAttackCardsAndDisplayToUser(playerID);
        //Test the attack cards do not contain another D5 card
        assertEquals(1, attackCards.size());
        assertEquals(card1.getType(), attackCards.get(0).getType());

        String expectedOutput = "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "D5 added to Attack...\n" +
                "Attack Card(s): D5\n\n" +
                "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "Attack set up is completed...\n" +
                "Your Attack: D5\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-046-Test-003: System checks that if the participant setting up an attack enters " +
            "‘Quit’ that the cards (if any) are displayed. two cards added to attack")
    void RESP_046_test_003() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-06 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsSettingUpAttack(game);

        //Force Player 1 to set up attack
        helper.forcePlayerTurn(game, 1);

        //Input quit
        String userInput = "D5\nL20\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new WeaponCard("D", 5);
        Card card3 = new WeaponCard("L", 20);
        game.gameLogic.getCurrentPlayer().addCardToHand(card1);
        game.gameLogic.getCurrentPlayer().addCardToHand(card3);

        //Player 1 playerID
        String playerID = game.getCurrentPlayer().getPlayerID();
        //Display prompt the participant to select card or enter 'Quit' to end valid attack
        ArrayList<Card> attackCards = game.getAttackCardsAndDisplayToUser(playerID);
        //Test the attack cards do not contain another D5 card
        assertEquals(2, attackCards.size());
        assertEquals(card1.getType(), attackCards.get(0).getType());
        assertEquals(card3.getType(), attackCards.get(1).getType());

        String expectedOutput = "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "D5 added to Attack...\n" +
                "Attack Card(s): D5\n\n" +
                "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "L20 added to Attack...\n" +
                "Attack Card(s): D5 L20\n\n" +
                "Select 0 or more non-repeating Weapon cards from your hand to build this attack.\n" +
                "Enter 'Quit' to end the attack setup.\n\n" +
                "Attack set up is completed...\n" +
                "Your Attack: D5 L20\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-046: System checks that if the participant setting up an attack enters ‘Quit’ that the cards " +
            "(if any) are displayed")
    void RESP_046() {
        RESP_046_test_001();
        RESP_046_test_002();
        RESP_046_test_003();
    }

    @Test
    @DisplayName("RESP-027-Test-001: System draws 1 adventure card to add to a participant’s hand and does not trim hand")
    void RESP_027_test_001() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 2);

        //Remove 3 cards from participant hand to not trigger trimming
        String participantID = game.getCurrentPlayer().getPlayerID();
        Card card1 = game.gameLogic.getPlayerHand(participantID).getFirst();
        game.discardAdventureCard(participantID, card1);
        Card card2 = game.gameLogic.getPlayerHand(participantID).getFirst();
        game.discardAdventureCard(participantID, card2);
        Card card3 = game.gameLogic.getPlayerHand(participantID).getFirst();
        game.discardAdventureCard(participantID, card3);

        //Test the size is what we expect after discarding
        assertEquals(9, game.gameLogic.getPlayerHand(participantID).size());

        //Call method that draws 1 adventure card to add to a participant’s hand and possibly trims hand
        game.draw1AdventureCardForParticipantAndTrim(participantID);

        //Test the size is what we expect after drawing 1 card
        assertEquals(10, game.gameLogic.getPlayerHand(participantID).size());
    }

    @Test
    @DisplayName("RESP-027-Test-002: System draws 1 adventure card to add to a participant’s hand and does trim hand")
    void RESP_027_test_002() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 3);
        //Get participant and their ID
        String participantID = game.getCurrentPlayer().getPlayerID();
        Player participant = game.gameLogic.getPlayer(participantID);
        //Remove 1 card and add one to ensure trim hand works as expected
        Card card1 = game.gameLogic.getPlayerHand(participantID).getFirst();
        game.discardAdventureCard(participantID, card1);
        Card card2 = new FoeCard(50);
        participant.addCardToHand(card2);

        //Input F50 for trimming
        String userInput = "F50\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Test the size is what we expect after discarding and adding
        assertEquals(12, participant.getHandSize());
        //Test the size of discard pile
        assertEquals(1, game.getAdventureDeck().getDiscardPileSize());

        //Call method that draws 1 adventure card to add to a participant’s hand and possibly trims hand
        game.draw1AdventureCardForParticipantAndTrim(participantID);

        //Test the size is what we expect after trimming
        assertEquals(12, participant.getHandSize());
        //Test the size of discard pile
        assertEquals(2, game.getAdventureDeck().getDiscardPileSize());
        //Discard pile has Foe card at the top
        assertEquals(card2, game.getAdventureDeck().getDiscardPile().getLast());
    }

    @Test
    @DisplayName("RESP-027: System draws 1 adventure card to add to a participant’s hand and possibly trims hand")
    void RESP_027() {
        RESP_027_test_001();
        RESP_027_test_002();
    }

    @Test
    @DisplayName("RESP-021-Test-001: System draws a Quest card and the current player sponsors.")
    void RESP_021_test_001() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 4);
        //user input
        String userInput = "Yes\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //current player ID
        game.promptCurrentPlayerToSponsor();

        //Test current player is the Sponsor
        assertEquals(game.getCurrentPlayer().getPlayerID(), game.getSponsorPlayerID());

        String expectedOutput = "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have accepted to be the Sponsor!\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-021-Test-002: System draws a Quest card and the current player declines sponsorship.")
    void RESP_021_test_002() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 4);
        //user input
        String userInput = "no\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //current player ID
        game.promptCurrentPlayerToSponsor();

        //Test current player is not the Sponsor
        assertNotEquals(game.getCurrentPlayer().getPlayerID(), game.getSponsorPlayerID());

        String expectedOutput = "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-021: System draws a Quest card and the current player can either sponsor the quest or decline.")
    void RESP_021() {
        RESP_021_test_001();
        RESP_021_test_002();
        RESP_021_test_003();
    }

    @Test
    @DisplayName("RESP-022-Test-001: If player that draws Quest card declines to sponsor, the system continues" +
            " to offer the quest to the next player in the order until a sponsor has been found.")
    void RESP_022_test_001() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 3);
        //user input
        String userInput = "No\nNo\nNo\nyes\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //draw Quest card
        Card questCard = new QuestCard(4);
        ArrayList<Card> eventDeck = game.getEventDeck().getDeck();
        eventDeck.addFirst(questCard);
        Card card = game.drawEventCard(game.getCurrentPlayer().getPlayerID());

        //Force 4 Foe cards in each player hand to not encounter issue of not having enough cards in output
        for (String playerID : game.gameLogic.getPlayerIDs()) {
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
        }

        //P3 declines to Sponsor
        game.promptCurrentPlayerToSponsor();
        //should have order -> P4, P1, P2
        //Player 2 will accept sponsorship
        game.promptOtherPlayersToSponsor();

        //Test current player 2 is the Sponsor
        assertEquals("P2", game.getSponsorPlayerID());

        String expectedOutput = "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "Asking P4:\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "Asking P1:\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "Asking P2:\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have accepted to be the Sponsor!\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-022-Test-002: The system ensures that any player who sponsors a quest has the necessary " +
            "cards to build a valid quest. A player will not sponsor a quest they cannot complete.")
    void RESP_022_test_002() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 1);
        //user input
        String userInput = "No\nyes\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //draw Quest card
        Card questCard = new QuestCard(4);
        ArrayList<Card> eventDeck = game.getEventDeck().getDeck();
        eventDeck.addFirst(questCard);
        Card card = game.drawEventCard(game.getCurrentPlayer().getPlayerID());

        assertEquals(questCard, game.getLastEventCardDrawn());

        //Player 2 hand has 3 Foe cards
        Player p2 = game.gameLogic.getPlayer("P2");
        p2.getHand().clear();
        p2.addCardToHand(new FoeCard(5));
        p2.addCardToHand(new FoeCard(5));
        p2.addCardToHand(new FoeCard(5));

        //Force 4 Foe cards player 3 hand
        game.gameLogic.getPlayer("P3").addCardToHand(new FoeCard(5));
        game.gameLogic.getPlayer("P3").addCardToHand(new FoeCard(5));
        game.gameLogic.getPlayer("P3").addCardToHand(new FoeCard(5));
        game.gameLogic.getPlayer("P3").addCardToHand(new FoeCard(5));

        //P1 declines to Sponsor
        game.promptCurrentPlayerToSponsor();
        //should have order -> P2 does not have enough foe cards they are not offered sponsorship
        //P3 says yes
        game.promptOtherPlayersToSponsor();

        //Test current player 3 is the Sponsor
        assertEquals("P3", game.getSponsorPlayerID());

        String expectedOutput = "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "P2 you cannot build a valid Quest.\n" +
                "You are being skipped.\n\n" +
                "Asking P3:\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have accepted to be the Sponsor!\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-022: If player that draws Quest card declines to sponsor, the system continues to offer the" +
            " quest to the next player in the order until a sponsor has been found. The system ensures that any player " +
            "who sponsors a quest has the necessary cards to build a valid quest. A player will not sponsor a quest " +
            "they cannot complete.")
    void RESP_022() {
        RESP_022_test_001();
        RESP_022_test_002();
        RESP_022_test_003();
    }

    @Test
    @DisplayName("RESP-023-Test-001: System ends quest and the current player’s turn if all players decline sponsorship")
    void RESP_023_test_001() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 1);
        //user input
        String userInput = "No\nno\nno\nno\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //draw Quest card
        Card questCard = new QuestCard(4);
        ArrayList<Card> eventDeck = game.getEventDeck().getDeck();
        eventDeck.addFirst(questCard);
        Card card = game.drawEventCard(game.getCurrentPlayer().getPlayerID());

        assertEquals(questCard, game.getLastEventCardDrawn());

        //Force 4 Foe cards in each player hand to not encounter issue of not having enough cards in output
        for (String playerID : game.gameLogic.getPlayerIDs()) {
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
        }

        //P1 declines to Sponsor
        game.promptCurrentPlayerToSponsor();
        //all others player decline sponsorship
        game.promptOtherPlayersToSponsor();

        //Test current player 3 is the Sponsor
        assertNull(game.getSponsorPlayerID());

        String expectedOutput = "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "Asking P2:\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "Asking P3:\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "Asking P4:\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "No sponsor for Quest\n" +
                "Quest has ended.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-023: System ends quest and the current player’s turn if all players decline sponsorship")
    void RESP_023() {
        RESP_023_test_001();
        RESP_023_test_002();
    }

    @Test
    @DisplayName("RESP-024-Test-001: System displays set of eligible players for each quest stage")
    void RESP_024_test_001() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 1);

        //set up players that would be eligible
        String[] players = new String[] {"P1", "P2", "P3", "P4"};
        game.gameLogic.setEligiblePlayers(players);

        //List of eligible players is displayed at the start of each stage
        game.showEligiblePlayersForStage(2);

        String expectedOutput = "Eligible Players for Stage 2: P1 P2 P3 P4\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-024: System displays set of eligible players for each quest stage")
    void RESP_024() {
        RESP_024_test_001();
        RESP_024_test_002();
    }

    @Test
    @DisplayName("RESP-025-Test-001: System prompts players to participate or withdraw the current quest stage. " +
            "Players 1, 3 accept and 4 declines")
    void RESP_025_test_001() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //user input
        String userInput = "yes\nyes\nno\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //set up players that would be eligible
        String[] players = new String[] {"P1", "P3", "P4"};
        game.gameLogic.setEligiblePlayers(players);

        //prompt players to participate
        game.promptToParticipateInCurrentStage();

        String expectedOutput = "Asking P1:\n" +
                "Would you like to participate in the current stage?\n" +
                "Type 'yes' or 'no':\n\n" +
                "Asking P3:\n" +
                "Would you like to participate in the current stage?\n" +
                "Type 'yes' or 'no':\n\n" +
                "Asking P4:\n" +
                "Would you like to participate in the current stage?\n" +
                "Type 'yes' or 'no':\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-025: System prompts players to participate or withdraw the current quest stage")
    void RESP_025() {
        RESP_025_test_001();
    }

    @Test
    @DisplayName("RESP-026-Test-001: System ensures eligible player who withdraws becomes ineligible to " +
            "participate in subsequent stages of quest. Players 1 and 3 decline, Player 2 and 4 accept, then all decline")
    void RESP_026_test_001() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //user input
        String userInput = "no\nyes\nno\nyes\nno\nno\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //set up players that would be eligible
        String[] players = new String[] {"P1", "P2", "P3", "P4"};
        game.gameLogic.setEligiblePlayers(players);

        //Test that before the method is called the size is 4
        assertEquals(4, game.gameLogic.getEligiblePlayers().size());

        //prompt players to participate
        game.promptToParticipateInCurrentStage();

        //Test that after the method is called the size is 2
        assertEquals(2, game.gameLogic.getEligiblePlayers().size());

        //prompt players to participate again
        game.promptToParticipateInCurrentStage();

        //Test that after the method is called again the size is 0
        assertEquals(0, game.gameLogic.getEligiblePlayers().size());
    }

    @Test
    @DisplayName("RESP-026-Test-002: System ensures eligible player who withdraws becomes ineligible to " +
            "participate in subsequent stages of quest. Players 1 and 3 decline, Player 2 and 4 accept, then all decline.")
    void RESP_026_test_002() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //user input
        String userInput = "no\nyes\nno\nyes\nno\nno\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //set up players that would be eligible
        String[] players = new String[] {"P1", "P2", "P3", "P4"};
        game.gameLogic.setEligiblePlayers(players);

        //prompt players to participate
        game.promptToParticipateInCurrentStage();
        //prompt players to participate again
        game.promptToParticipateInCurrentStage();

        String expectedOutput = "Asking P1:\n" +
                "Would you like to participate in the current stage?\n" +
                "Type 'yes' or 'no':\n\n" +
                "Asking P2:\n" +
                "Would you like to participate in the current stage?\n" +
                "Type 'yes' or 'no':\n\n" +
                "Asking P3:\n" +
                "Would you like to participate in the current stage?\n" +
                "Type 'yes' or 'no':\n\n" +
                "Asking P4:\n" +
                "Would you like to participate in the current stage?\n" +
                "Type 'yes' or 'no':\n\n" +
                "Asking P2:\n" +
                "Would you like to participate in the current stage?\n" +
                "Type 'yes' or 'no':\n\n" +
                "Asking P4:\n" +
                "Would you like to participate in the current stage?\n" +
                "Type 'yes' or 'no':\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertTrue(output.contains(expectedOutput));
    }

    @Test
    @DisplayName("RESP-026: System ensures eligible player who withdraws becomes ineligible to participate in " +
            "subsequent stages of quest")
    void RESP_026() {
        RESP_026_test_001();
        RESP_026_test_002();
    }

    @Test
    @DisplayName("RESP-028-Test-001: System ends quest stage, if there are no participants")
    void RESP_028_test_001() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //user input
        String userInput = "no\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //set up players that would be eligible
        String[] players = new String[] {"P1"};
        game.gameLogic.setEligiblePlayers(players);

        //Test there are participants
        assertEquals(false, game.noParticipantsFound());

        //prompt players to participate
        game.promptToParticipateInCurrentStage();

        //Test that now there are no participants found
        assertEquals(true, game.noParticipantsFound());
    }

    @Test
    @DisplayName("RESP-028-Test-002: System ends quest stage, if there are no participants after asking")
    void RESP_028_test_002() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //user input
        String userInput = "no\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //set up players that would be eligible
        String[] players = new String[] {"P1"};
        game.gameLogic.setEligiblePlayers(players);

        //prompt players to participate
        game.promptToParticipateInCurrentStage();

        String expectedOutput = "Asking P1:\n" +
                "Would you like to participate in the current stage?\n" +
                "Type 'yes' or 'no':\n\n" +
                "No Participants for Current Stage...\n" +
                "Quest has ended.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-028: System ends quest stage, if there are no participants")
    void RESP_028() {
        RESP_028_test_001();
        RESP_028_test_002();
    }

    @Test
    @DisplayName("RESP-014-Test-001: System prompts player to hit <return> key to leave 'hotseat'.")
    void RESP_014_test_001() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        String userInput = "\n";
        Scanner overrideInput = new Scanner(userInput);
        game.setInput(overrideInput);

        game.promptToLeaveHotSeat();

        String expectedOutput = "Your turn is now over...\n" +
                "Press the <RETURN> key to leave the hotseat:\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-014-Test-002: System flushes display for the next player")
    void RESP_014_test_002() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        game.flushDisplay();

        //Test the output contains 50 newlines to simulate clearing the display
        String expectedOutput = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-014-Test-003: System flushes display for the next player and displays the ID of the " +
            "next player now in the 'hotseat'")
    void RESP_014_test_003() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 1);

        game.gameLogic.nextTurn();

        game.displayPlayerInHotSeat();

        String expectedOutput = "P2 is now in the hotseat.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }

    @Test
    @DisplayName("RESP-014: System prompts player to hit <return> key to leave 'hotseat'." +
            "System flushes display for the next player and displays the ID of the next player now in the 'hotseat'")
    void RESP_014() {
        RESP_014_test_001();
        RESP_014_test_002();
        RESP_014_test_003();
    }

    @Test
    @DisplayName("RESP-029-Test-001: Each participant prepares an attack with one or more non-repeated weapon cards, " +
            "and the system calculates the attack value as the sum of the weapon values. Testing sizes.")
    void RESP_029_test_001() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //user input
        String userInput = "D5\nquit\nH10\nD5\nquit\nB15\nquit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //set up players that would be eligible
        String[] players = new String[] {"P1", "P3", "P4"};
        game.gameLogic.setEligiblePlayers(players);

        //Force cards from input in the participants hands
        Card card1 = new WeaponCard("D", 5);
        Card card2 = new WeaponCard("H", 10);
        Card card3 = new WeaponCard("D", 5);
        Card card4 = new WeaponCard("B", 15);
        game.gameLogic.getPlayer("P1").addCardToHand(card1);
        game.gameLogic.getPlayer("P3").addCardToHand(card2);
        game.gameLogic.getPlayer("P3").addCardToHand(card3);
        game.gameLogic.getPlayer("P4").addCardToHand(card4);

        //Set up the ArrayList<int> for the attack values
        ArrayList<Integer> attackValues = game.gameLogic.getAttackValues();

        //set up the HashMap<PlayerId, Attack cards> for the attack
        HashMap<String,ArrayList<Card>> attackHands = game.gameLogic.getAttackHands();

        //Test attack values and the attackhands are null
        assertNull(attackValues);
        assertNull(attackHands);

        //players prepare an attack and the attack values are calculated
        game.participantsSetUpAttacks();

        //Get the list and hashmap after setupAttack
        attackValues = game.gameLogic.getAttackValues();
        attackHands = game.gameLogic.getAttackHands();

        //Test attack values are of correct expected size
        assertEquals(3, attackValues.size());
        //Test attack hashmap has attacks in it
        assertEquals(3, attackHands.size());
        assertEquals(1, attackHands.get("P1").size());
        assertEquals(2, attackHands.get("P3").size());
        assertEquals(1, attackHands.get("P4").size());
    }

    @Test
    @DisplayName("RESP-029-Test-002: Each participant prepares an attack with one or more non-repeated weapon cards, " +
            "and the system calculates the attack value as the sum of the weapon values. Testing attack values.")
    void RESP_029_test_002() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //user input
        String userInput = "D5\nquit\nH10\nD5\nquit\nB15\nquit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //set up players that would be eligible
        String[] players = new String[] {"P1", "P3", "P4"};
        game.gameLogic.setEligiblePlayers(players);

        //Force cards from input in the participants hands
        Card card1 = new WeaponCard("D", 5);
        Card card2 = new WeaponCard("H", 10);
        Card card3 = new WeaponCard("D", 5);
        Card card4 = new WeaponCard("B", 15);
        game.gameLogic.getPlayer("P1").addCardToHand(card1);
        game.gameLogic.getPlayer("P3").addCardToHand(card2);
        game.gameLogic.getPlayer("P3").addCardToHand(card3);
        game.gameLogic.getPlayer("P4").addCardToHand(card4);

        //players prepare an attack and the attack values are calculated
        game.participantsSetUpAttacks();

        //get attack values after initalized
        ArrayList<Integer> attackValues = game.gameLogic.getAttackValues();

        //Test attack values are 5, 15, 15
        assertEquals(5, attackValues.get(0));
        assertEquals(15, attackValues.get(1));
        assertEquals(15, attackValues.get(2));
    }

    @Test
    @DisplayName("RESP-029-Test-003: Each participant prepares an attack with one or more non-repeated weapon cards, " +
            "and the system calculates the attack value as the sum of the weapon values. Testing attack hands.")
    void RESP_029_test_003() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //user input
        String userInput = "D5\nquit\nH10\nD5\nquit\nB15\nquit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //set up players that would be eligible
        String[] players = new String[] {"P1", "P3", "P4"};
        game.gameLogic.setEligiblePlayers(players);

        //Force cards from input in the participants hands
        Card card1 = new WeaponCard("D", 5);
        Card card2 = new WeaponCard("H", 10);
        Card card3 = new WeaponCard("D", 5);
        Card card4 = new WeaponCard("B", 15);
        game.gameLogic.getPlayer("P1").addCardToHand(card1);
        game.gameLogic.getPlayer("P3").addCardToHand(card2);
        game.gameLogic.getPlayer("P3").addCardToHand(card3);
        game.gameLogic.getPlayer("P4").addCardToHand(card4);

        //players prepare an attack and the attack values are calculated
        game.participantsSetUpAttacks();

        //Get attackHands after initalized
        HashMap<String,ArrayList<Card>> attackHands = game.gameLogic.getAttackHands();

        //Test attack hashmap has attacks cards in it
        ArrayList<Card> attackHand1 = attackHands.get("P1");
        ArrayList<Card> attackHand2 = attackHands.get("P3");
        ArrayList<Card> attackHand3 = attackHands.get("P4");

        assertEquals(card1, attackHand1.getFirst());
        assertEquals(card2, attackHand2.get(0));
        assertEquals(card3, attackHand2.get(1));
        assertEquals(card4, attackHand3.getFirst());
    }

    @Test
    @DisplayName("RESP-029: Each participant prepares an attack with one or more non-repeated weapon cards, " +
            "and the system calculates the attack value as the sum of the weapon values")
    void RESP_029() {
        RESP_029_test_001();
        RESP_029_test_002();
        RESP_029_test_003();
    }

    @Test
    @DisplayName("RESP-032-Test-001: System discards all cards used by participants for a stage, once resolved. Testing " +
            "size of attackHands hashmap before and after.")
    void RESP_032_test_001() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //user input
        String userInput = "D5\nquit\nH10\nD5\nquit\nB15\nquit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //set up players that would be eligible
        String[] players = new String[] {"P1", "P3", "P4"};
        game.gameLogic.setEligiblePlayers(players);

        //Force cards from input in the participants hands
        Card card1 = new WeaponCard("D", 5);
        Card card2 = new WeaponCard("H", 10);
        Card card3 = new WeaponCard("D", 5);
        Card card4 = new WeaponCard("B", 15);
        game.gameLogic.getPlayer("P1").addCardToHand(card1);
        game.gameLogic.getPlayer("P3").addCardToHand(card2);
        game.gameLogic.getPlayer("P3").addCardToHand(card3);
        game.gameLogic.getPlayer("P4").addCardToHand(card4);

        //players prepare an attack and the attack values are calculated
        game.participantsSetUpAttacks();

        //Test attackHands before discarding occurs
        HashMap<String,ArrayList<Card>> attackHands = game.gameLogic.getAttackHands();
        assertEquals(1, attackHands.get("P1").size());
        assertEquals(2, attackHands.get("P3").size());
        assertEquals(1, attackHands.get("P4").size());

        //Discard once Stage is resolved
        game.discardParticipantsCards();

        //Test attackHands after discarding occurs
        attackHands = game.gameLogic.getAttackHands();
        assertEquals(0, attackHands.get("P1").size());
        assertEquals(0, attackHands.get("P3").size());
        assertEquals(0, attackHands.get("P4").size());
    }

    @Test
    @DisplayName("RESP-032-Test-002: System discards all cards used by participants for a stage, once resolved. Testing " +
            "discard pile")
    void RESP_032_test_002() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //user input
        String userInput = "D5\nquit\nH10\nD5\nquit\nB15\nquit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //set up players that would be eligible
        String[] players = new String[] {"P1", "P3", "P4"};
        game.gameLogic.setEligiblePlayers(players);

        //Force cards from input in the participants hands
        Card card1 = new WeaponCard("D", 5);
        Card card2 = new WeaponCard("H", 10);
        Card card3 = new WeaponCard("D", 5);
        Card card4 = new WeaponCard("B", 15);
        game.gameLogic.getPlayer("P1").addCardToHand(card1);
        game.gameLogic.getPlayer("P3").addCardToHand(card2);
        game.gameLogic.getPlayer("P3").addCardToHand(card3);
        game.gameLogic.getPlayer("P4").addCardToHand(card4);

        //players prepare an attack and the attack values are calculated
        game.participantsSetUpAttacks();

        //Test discardPile before discarding
        assertEquals(0, game.getAdventureDeck().getDiscardPileSize());

        //Discard once Stage is resolved
        game.discardParticipantsCards();

        //Test discardPile after discarding
        assertEquals(4, game.getAdventureDeck().getDiscardPileSize());
        //Test each card is the same
        assertEquals(card1, game.getAdventureDeck().getDiscardPile().get(0));
        assertEquals(card2, game.getAdventureDeck().getDiscardPile().get(1));
        assertEquals(card3, game.getAdventureDeck().getDiscardPile().get(2));
        assertEquals(card4, game.getAdventureDeck().getDiscardPile().get(3));
    }

    @Test
    @DisplayName("RESP-032: System discards all cards used by participants for a stage, once resolved")
    void RESP_032() {
        RESP_032_test_001();
        RESP_032_test_002();
    }

    @Test
    @DisplayName("RESP-037-Test-002: System must add the selected valid card to the current stage and display " +
            "the updated set of cards. testing that questBuilt is set and can get the quest info via getter and size")
    void RESP_037_test_002() {
        //Test helpers
        TestHelpers helper = new TestHelpers();

        //Created set up for UC-05 Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestsBuildingQuest(game);

        //Force Player 1 to be sponsoring Quest
        helper.forcePlayerTurn(game, 1);

        //Input quit
        String userInput = "F5\nQuit\nF10\nH10\nQuit\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //Force a Quest card to be the first event card to be drawn
        QuestCard questCard = new QuestCard(2);
        game.gameLogic.getEventDeck().cards.addFirst(questCard);

        //Build Player hand
        game.getCurrentPlayer().getHand().clear();
        Card card1 = new FoeCard(5);
        Card card2 = new FoeCard(10);
        Card card3 = new WeaponCard("H", 10);
        game.getCurrentPlayer().addCardToHand(card1);
        game.getCurrentPlayer().addCardToHand(card2);
        game.getCurrentPlayer().addCardToHand(card3);

        //Test questBuilt is null
        HashMap<Integer, ArrayList<Card>> questBuilt = game.gameLogic.getQuestInfo();
        assertNull(questBuilt);

        game.displaySponsorHandAndSetUpStages("P1", questCard.getStages());

        //Test not null and the size
        questBuilt = game.gameLogic.getQuestInfo();
        assertNotNull(questBuilt);
        assertEquals(2, questBuilt.size());
    }

    @Test
    @DisplayName("RESP-037: System must add the selected valid card to the current stage and display the " +
            "updated set of cards")
    void RESP_037() {
        RESP_037_test_001();
        RESP_037_test_002();
    }

    @Test
    @DisplayName("RESP-019-Test-003: System removes card from the hand and discards it. " +
            "Testing that it get the right foe value")
    void RESP_019_test_003() {
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        adventureDeck.shuffle();
        game.setPlayers();

        //Add 3 cards to Player 1 hand
        String playerID = game.getCurrentPlayer().getPlayerID();
        ArrayList<Card> playerHand = game.getCurrentPlayer().getHand();
        //Add 12 cards
        playerHand.clear();
        playerHand.add(new FoeCard(5));
        playerHand.add(new FoeCard(10));
        playerHand.add(new FoeCard(15));
        playerHand.add(new FoeCard(20));
        playerHand.add(new FoeCard(25));
        playerHand.add(new FoeCard(30));
        playerHand.add(new FoeCard(35));
        playerHand.add(new FoeCard(40));
        playerHand.add(new FoeCard(50));
        playerHand.add(new FoeCard(70));

        //Get input from user for 1 card
        String userInput = "F50\n";
        String cardsToDiscard = game.gameDisplay.getDiscardInput(new Scanner(userInput));

        //Test player hand has 10 cards in it
        Player player = game.getCurrentPlayer();
        assertEquals(10, player.getHandSize());

        //Remove cards and discard
        game.gameLogic.removeCardsAndDiscard(cardsToDiscard, playerID);

        //Test player hand has 9 cards in it
        assertEquals(9, player.getHandSize());
        //Test Same Foe card
        FoeCard foe =  (FoeCard) game.getAdventureDeck().getDiscardPile().getLast();
        assertEquals(50, foe.getValue());
    }

    @Test
    @DisplayName("RESP-019: System removes card from the hand and discards it in the correct discard pile")
    void RESP_019() {
        RESP_019_test_001();
        RESP_019_test_002();
        RESP_019_test_003();
    }

    @Test
    @DisplayName("RESP-030-Test-001: The system decides ineligibility for the next Stage by comparing each " +
            "participant’s attack value with the current stage value. testing stage values and losers of stage")
    void RESP_030_test_001() {
        //Test helpers
        TestHelpers helper = new TestHelpers();
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //set up players that would be eligible
        String[] players = new String[] {"P1", "P2"};
        game.gameLogic.setEligiblePlayers(players);
        //set currentStageNumber
        game.gameLogic.setCurrentStageNumber(1);

        //set current stage value
        game.gameLogic.setCurrentStageValue(10);
        int currentStageValue = game.gameLogic.getCurrentStageValue();
        assertEquals(10, currentStageValue);

        //set attack values
        game.gameLogic.setAttackValues();
        game.gameLogic.addAttackValue(0,5);
        game.gameLogic.addAttackValue(1,10);

        //Test losers arrray is null
        assertNull(game.gameLogic.getStageLosers());

        game.resolveAttacks();

        //Test there are 1 loser of this stage
        assertEquals(1, game.gameLogic.getStageLosers().size());
    }

    @Test
    @DisplayName("RESP-030-Test-002: If the participant’s attack is less than the value of the current stage, " +
            "they lose and become ineligible to further participate in this quest.")
    void RESP_030_test_002() {
        //Test helpers
        TestHelpers helper = new TestHelpers();
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);

        //set up players that would be eligible
        String[] players = new String[] {"P1", "P2"};
        game.gameLogic.setEligiblePlayers(players);

        //set current stage value
        game.gameLogic.setCurrentStageValue(10);
        int currentStageValue = game.gameLogic.getCurrentStageValue();
        assertEquals(10, currentStageValue);

        //set attack values
        game.gameLogic.setAttackValues();
        game.gameLogic.addAttackValue(0,5);
        game.gameLogic.addAttackValue(1,10);

        //Test 2 eligble players
        assertEquals(2, game.gameLogic.getEligiblePlayers().size());

        game.resolveAttacks();

        //Test there are 1 eligble
        assertEquals(1, game.gameLogic.getEligiblePlayers().size());
    }

    @Test
    @DisplayName("RESP-030: The system decides ineligibility for the next Stage by comparing each participant’s " +
            "attack value with the current stage value. If the participant’s attack is less than the value of the " +
            "current stage, they lose and become ineligible to further participate in this quest.")
    void RESP_030() {
        RESP_030_test_001();
        RESP_030_test_002();
    }
    @Test
    @DisplayName("RESP-021-Test-003: System draws a Quest card. ")
    void RESP_021_test_003() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 1);

        //Force a Quest card to be the first event card to be drawn
        QuestCard questCard = new QuestCard(2);
        game.gameLogic.getEventDeck().cards.addFirst(questCard);

        //user input
        String userInput = "yes\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        game.playTurn();

        String expectedOutput = "P1's Turn:\n\n" +
                "Drawing Event Card...\n" +
                "You drew: Q2\n\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have accepted to be the Sponsor!\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertTrue(output.contains(expectedOutput));
    }

    @Test
    @DisplayName("RESP-022-Test-003: If player that draws Quest card declines to sponsor, the system continues" +
            " to offer the quest to the next player in the order until a sponsor has been found.")
    void RESP_022_test_003() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 1);

        //Force a Quest card to be the first event card to be drawn
        QuestCard questCard = new QuestCard(2);
        game.gameLogic.getEventDeck().cards.addFirst(questCard);

        //user input
        String userInput = "no\nyes\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        game.playTurn();

        String expectedOutput = "P1's Turn:\n\n" +
                "Drawing Event Card...\n" +
                "You drew: Q2\n\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "Asking P2:\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have accepted to be the Sponsor!\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertTrue(output.contains(expectedOutput));
    }
    @Test
    @DisplayName("RESP-023-Test-002: System ends quest and the current player’s turn if all players decline sponsorship")
    void RESP_023_test_002() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 1);
        //user input
        String userInput = "No\nno\nno\nno\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        //draw Quest card
        Card questCard = new QuestCard(4);
        game.getEventDeck().getDeck().addFirst(questCard);

        //Force 4 Foe cards in each player hand to not encounter issue of not having enough cards in output
        for (String playerID : game.gameLogic.getPlayerIDs()) {
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
            game.gameLogic.getPlayer(playerID).addCardToHand(new FoeCard(5));
        }

        //P1 declines to Sponsor
        game.playTurn();

        //Test current player 3 is the Sponsor
        assertNull(game.getSponsorPlayerID());

        String expectedOutput = "P1's Turn:\n\n"+
                "Drawing Event Card...\n" +
                "You drew: Q4\n\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "Asking P2:\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "Asking P3:\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "Asking P4:\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have declined Sponsorship\n" +
                "Now asking other players...\n\n" +
                "No sponsor for Quest\n" +
                "Quest has ended.\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);
    }
    @Test
    @DisplayName("RESP-024-Test-002: System displays set of eligible players for each quest stage")
    void RESP_024_test_002() {
        //SETUP
        //Test helpers
        TestHelpers helper = new TestHelpers();
        //Created set up for general Tests
        Game game = new Game(new GameLogic(), new GameDisplay());
        helper.setUpForTestGeneral(game);
        helper.forcePlayerTurn(game, 1);

        //draw Quest card
        Card questCard = new QuestCard(4);
        game.getEventDeck().getDeck().addFirst(questCard);

        //user input
        String userInput = "yes\n";
        Scanner overrideInput = new Scanner(userInput);
        //Forcing overriding of input
        game.setInput(overrideInput);

        game.playTurn();

        String expectedOutput = "P1's Turn:\n\n"+
                "Drawing Event Card...\n" +
                "You drew: Q4\n\n" +
                "Would you like to sponsor this Quest?\n" +
                "Type 'yes' or 'no':\n\n" +
                "You have accepted to be the Sponsor!\n\n" +
                "The Quest Begins!\n" +
                "Eligible Players for Stage 1: P2 P3 P4\n\n" +
                "Eligible Players for Stage 2: P2 P3 P4\n\n" +
                "Eligible Players for Stage 3: P2 P3 P4\n\n" +
                "Eligible Players for Stage 4: P2 P3 P4\n\n";

        //Test expected output
        String output = game.gameDisplay.getOutput();
        assertTrue(output.contains(expectedOutput));
    }
}