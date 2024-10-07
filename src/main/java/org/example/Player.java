package org.example;

import java.util.ArrayList;

public class Player {

    protected String playerID;
    private ArrayList<AdventureCard> hand;

    public Player(String playerID) {
        this.playerID = playerID;
        this.hand = new ArrayList<AdventureCard>();
    }

    public String getPlayerID() {
        return playerID;
    }

    public ArrayList<AdventureCard> getHand() {
        return hand;
    }

    public void addCardToHand(Card card){
        hand.add((AdventureCard) card);
    }
}
