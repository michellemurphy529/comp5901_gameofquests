package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    @Test
    @DisplayName("RESP-001-Test-001: Set up Adventure Deck with proper number of cards and their types + values")
    void RESP_001_test_001(){
        Game game = new Game(new GameLogic());
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
        Game game = new Game(new GameLogic());
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
}