package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameAcceptanceTest {

    @Test
    @DisplayName("A-TEST: JP-Scenario")
    void A_test_JP_scenario() {
        /*
        1) Start game, decks are created, hands of the 4 players are set up with random cards
        */
        Game game = new Game(new GameLogic(), new GameDisplay());
        game.setDecks();
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();
        adventureDeck.shuffle();
        eventDeck.shuffle();
        game.setPlayers();
        game.dealInitial12AdventureCards();

        /*
        2) Rig the 4 hands to the hold the cards of the 4 posted initial hands
        */
        Player p1 = game.gameLogic.getPlayer("P1");
        Player p2 = game.gameLogic.getPlayer("P2");
        Player p3 = game.gameLogic.getPlayer("P3");
        Player p4 = game.gameLogic.getPlayer("P4");

        String p1ID = game.gameLogic.getPlayerIDs()[0];
        String p2ID = game.gameLogic.getPlayerIDs()[1];
        String p3ID = game.gameLogic.getPlayerIDs()[2];
        String p4ID = game.gameLogic.getPlayerIDs()[3];

        for (int i = 0; i < 12; i++) {
            game.gameLogic.discardCard(p1ID, adventureDeck, p1.getHand().getFirst());
            game.gameLogic.discardCard(p2ID, adventureDeck, p2.getHand().getFirst());
            game.gameLogic.discardCard(p3ID, adventureDeck, p3.getHand().getFirst());
            game.gameLogic.discardCard(p4ID, adventureDeck, p4.getHand().getFirst());
        }
        /* Posted Initial Hands:

        P1 - F5 F5 F15 F15 D5 S10 S10 H10 H10 B15 B15 L20
        P2 - F5 F5 F15 F15 F40 D5 S10 H10 H10 B15 B15 E30
        P3 - F5 F5 F5 F15 D5 S10 S10 S10 H10 H10 B15 L20
        P4 - F5 F15 F15 F40 D5 D5 S10 H10 H10 B15 L20 E30
        */

        //Player 1 Hand Rig
        //P1 - F5 F5 F15 F15 D5 S10 S10 H10 H10 B15 B15 L20
        p1.addCardToHand(new FoeCard(5));
        p1.addCardToHand(new FoeCard(5));
        p1.addCardToHand(new FoeCard(15));
        p1.addCardToHand(new FoeCard(15));
        p1.addCardToHand(new WeaponCard("D",5));
        p1.addCardToHand(new WeaponCard("S",10));
        p1.addCardToHand(new WeaponCard("S",10));
        p1.addCardToHand(new WeaponCard("H",10));
        p1.addCardToHand(new WeaponCard("H",10));
        p1.addCardToHand(new WeaponCard("B",15));
        p1.addCardToHand(new WeaponCard("B",15));
        p1.addCardToHand(new WeaponCard("L",20));

        //Player 2 Hand Rig
        //P2 - F5 F5 F15 F15 F40 D5 S10 H10 H10 B15 B15 E30
        p2.addCardToHand(new FoeCard(5));
        p2.addCardToHand(new FoeCard(5));
        p2.addCardToHand(new FoeCard(15));
        p2.addCardToHand(new FoeCard(15));
        p2.addCardToHand(new FoeCard(40));
        p2.addCardToHand(new WeaponCard("D",5));
        p2.addCardToHand(new WeaponCard("S",10));
        p2.addCardToHand(new WeaponCard("H",10));
        p2.addCardToHand(new WeaponCard("H",10));
        p2.addCardToHand(new WeaponCard("B",15));
        p2.addCardToHand(new WeaponCard("B",15));
        p2.addCardToHand(new WeaponCard("E",30));

        //Player 3 Hand Rig
        //P3 - F5 F5 F5 F15 D5 S10 S10 S10 H10 H10 B15 L20
        p3.addCardToHand(new FoeCard(5));
        p3.addCardToHand(new FoeCard(5));
        p3.addCardToHand(new FoeCard(5));
        p3.addCardToHand(new FoeCard(15));
        p3.addCardToHand(new WeaponCard("D",5));
        p3.addCardToHand(new WeaponCard("S",10));
        p3.addCardToHand(new WeaponCard("S",10));
        p3.addCardToHand(new WeaponCard("S",10));
        p3.addCardToHand(new WeaponCard("H",10));
        p3.addCardToHand(new WeaponCard("H",10));
        p3.addCardToHand(new WeaponCard("B",15));
        p3.addCardToHand(new WeaponCard("L",20));

        //Player 4 Hand Rig
        //P4 - F5 F15 F15 F40 D5 D5 S10 H10 H10 B15 L20 E30
        p4.addCardToHand(new FoeCard(5));
        p4.addCardToHand(new FoeCard(15));
        p4.addCardToHand(new FoeCard(15));
        p4.addCardToHand(new FoeCard(40));
        p4.addCardToHand(new WeaponCard("D",5));
        p4.addCardToHand(new WeaponCard("D",5));
        p4.addCardToHand(new WeaponCard("S",10));
        p4.addCardToHand(new WeaponCard("H",10));
        p4.addCardToHand(new WeaponCard("H",10));
        p4.addCardToHand(new WeaponCard("B",15));
        p4.addCardToHand(new WeaponCard("L",20));
        p4.addCardToHand(new WeaponCard("E",30));


        //Rig Event Deck to has a Q4 Card at the "top"
        QuestCard q4 = new QuestCard(4);
        game.getEventDeck().getDeck().addFirst(q4);

        //In it's current state my program cannot trim random cards as functionality is different from the specifications
        //I have the user entering a String of the card...
        adventureDeck.getDeck().addFirst(new WeaponCard("H",10));
        adventureDeck.getDeck().addFirst(new FoeCard(40));
        adventureDeck.getDeck().addFirst(new WeaponCard("S",10));
        adventureDeck.getDeck().addFirst(new WeaponCard("B",15));
        adventureDeck.getDeck().addFirst(new WeaponCard("L",20));
        adventureDeck.getDeck().addFirst(new WeaponCard("S",10));
        adventureDeck.getDeck().addFirst(new FoeCard(5));
        adventureDeck.getDeck().addFirst(new WeaponCard("B",15));
        adventureDeck.getDeck().addFirst(new WeaponCard("S",10));
        adventureDeck.getDeck().addFirst(new FoeCard(10));
        adventureDeck.getDeck().addFirst(new WeaponCard("L",20));

        //Rig adventure deck to draw the right cards
        adventureDeck.getDeck().addFirst(new WeaponCard("L",20));
        adventureDeck.getDeck().addFirst(new FoeCard(30));
        adventureDeck.getDeck().addFirst(new WeaponCard("S",10));
        adventureDeck.getDeck().addFirst(new WeaponCard("B",15));
        adventureDeck.getDeck().addFirst(new WeaponCard("L",20));
        adventureDeck.getDeck().addFirst(new WeaponCard("L",20));
        adventureDeck.getDeck().addFirst(new FoeCard(10));
        adventureDeck.getDeck().addFirst(new WeaponCard("B",15));
        adventureDeck.getDeck().addFirst(new WeaponCard("S",10));
        adventureDeck.getDeck().addFirst(new FoeCard(30));

        //Rig input from user
        String userInput = """
                no
                yes
                F5
                H10
                quit
                F15
                S10
                Quit
                F15
                D5
                B15
                quit
                F40
                B15
                quit
                yes
                F5
                yes
                F5
                yes
                F5
                D5
                S10
                quit
                S10
                D5
                quit
                D5
                H10
                quit
                yes
                yes
                yes
                H10
                S10
                quit
                B15
                S10
                quit
                H10
                B15
                quit
                yes
                yes
                L20
                H10
                S10
                quit
                B15
                S10
                L20
                quit
                yes
                yes
                B15
                H10
                L20
                quit
                D5
                S10
                L20
                E30
                quit
                L20
                F10
                S10
                B15
                F5
                S10
                L20
                B15
                S10
                F40
                H10
                """;

        Scanner overrideInput = new Scanner(userInput);
        game.setInput(overrideInput);

        /* P2 QUEST:
        STAGE 1:
        CARDS - F5 H10
        VALUE = 15

        STAGE 2:
        CARDS - F15 S10
        VALUE = 25

        STAGE 3:
        CARDS - F15 D5 B15
        VALUE = 35

        STAGE 4:
        CARDS - F40 B15
        VALUE = 55 */

        /*
        3) P1 draws a quest of 4 stages
         */
        game.playTurn();

        /*
        4) P1 is asked but declines to sponsor
        5) P2 is asked and sponsors and then builds the 4 stages of the quest as posted.
        a. Other players do NOT see these stages
         */

        //Assert P1 has no shields
        assertEquals(0, p1.getShieldCount());
        //flushing display to get correct output easily
        game.gameDisplay.flush();
        //Get player 1 hand
        game.gameDisplay.displayPlayerHand(p1);
        //Assert P1 hand is F5 F10 F15 F15 F30 Horse Axe Axe Lance (displayed in this order)
        String expectedOutput = "P1 hand: F5 F10 F15 F15 F30 H10 B15 B15 L20\n\n";
        String output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);

        //Assert P3 has no shields
        assertEquals(0, p3.getShieldCount());
        //flushing display to get correct output easily
        game.gameDisplay.flush();
        //Get player 3 hand
        game.gameDisplay.displayPlayerHand(p3);
        //Assert P3 hand is F5 F5 F15 F30 Sword
        expectedOutput = "P3 hand: F5 F5 F15 F30 S10\n\n";
        output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);

        //Assert P4 has 4 shields
        assertEquals(4, p4.getShieldCount());
        //flushing display to get correct output easily
        game.gameDisplay.flush();
        //Get player 4 hand
        game.gameDisplay.displayPlayerHand(p4);
        //Assert P4 hand is F15 F15 F40 Lance
        expectedOutput = "P4 hand: F15 F15 F40 L20\n\n";
        output = game.gameDisplay.getOutput();
        assertEquals(expectedOutput, output);

        //assert P2 has 12 cards in hand
        assertEquals(12,p2.getHandSize());
    }
}
