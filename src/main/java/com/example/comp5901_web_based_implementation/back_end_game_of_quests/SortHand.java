package com.example.comp5901_web_based_implementation.back_end_game_of_quests;
import java.util.Comparator;

public class SortHand implements Comparator<Card> {

    @Override
    public int compare(Card card1, Card card2) {
        //Foe card ordered before Weapon card
        if(card1 instanceof FoeCard && card2 instanceof WeaponCard) { return -1; }
        //Weapon card ordered before Foe
        if(card1 instanceof WeaponCard && card2 instanceof FoeCard) { return 1; }
        //Foe's are ordered by ascending values
        if(card1 instanceof FoeCard && card2 instanceof FoeCard) {
            return Integer.compare(((FoeCard) card1).getValue(), ((FoeCard) card2).getValue());
        }
        //Weapon's are ordered by ascending value except for S going before H, (equal value)
        if(card1 instanceof WeaponCard weapon1 && card2 instanceof WeaponCard weapon2) {
            //Always order Sword then Horse Weapon card
            if (weapon1.getType().equals("S") && weapon2.getType().equals("H")) { return -1; }
            if (weapon1.getType().equals("H") && weapon2.getType().equals("S")) { return 1; }
            //Otherwise ordering is done by value ascension
            return Integer.compare(weapon1.getValue(), weapon2.getValue());
        }
        //Neither Foe nor Weapon card, no sorting occurs
        return 0;
    }
}
