package org.example;

import java.util.ArrayList;

public class Player {

    protected String playerID;
    private ArrayList<Card> hand;

    public Player(String playerID) {
        this.playerID = playerID;
        this.hand = new ArrayList<Card>();
    }

    public String getPlayerID() {
        return playerID;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void addCardToHand(Card card){
        hand.add(card);
    }

    public void removeFromHand(Card card) {
        hand.remove(card);
    }
}
