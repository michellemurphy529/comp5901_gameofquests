package org.example;

import java.util.ArrayList;

public class AdventureDeck extends Deck {

    public AdventureDeck() {
        super();
    }

    @Override
    public int getSize() {
        return cards.size();
    }

    @Override
    public ArrayList<Card> getDeck() {
        return cards;
    }

    @Override
    protected void initializeDeck() {
        // Adding 50 Foe cards
        addFoeCards(5, 8);      // 8 - F5
        addFoeCards(10, 7);     // 7 - F10
        addFoeCards(15, 8);     // 8 - F15
        addFoeCards(20, 7);     // 7 - F20
        addFoeCards(25, 7);     // 7 - F25
        addFoeCards(30, 4);     // 4 - F30
        addFoeCards(35, 4);     // 4 - F35
        addFoeCards(40, 2);     // 2 - F40
        addFoeCards(50, 2);     // 2 - F50
        addFoeCards(70, 1);     // 1 - F70

        // Adding 50 Weapon cards
        addWeaponCards("D", 5, 6);      // 6 Daggers - value 5
        addWeaponCards("S", 10, 16);    // 16 Swords - value 10
        addWeaponCards("H", 10, 12);    // 12 Horses - value 10
        addWeaponCards("B", 15, 8);     // 8 Battle-axes - value 15
        addWeaponCards("L", 20, 6);     // 6 Lances - value 20
        addWeaponCards("E", 30, 2);     // 2 Excalibur's - value 30
    }

    private void addFoeCards(int value, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cards.add(new FoeCard(value));
        }
    }

    private void addWeaponCards(String type, int value, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cards.add(new WeaponCard(type, value));
        }
    }

    @Override
    public Card drawCard() {
        Card cardDrawn = cards.getFirst();
        cards.removeFirst();
        return cardDrawn;
    }
}

