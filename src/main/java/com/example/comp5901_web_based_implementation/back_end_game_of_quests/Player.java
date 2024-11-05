package com.example.comp5901_web_based_implementation.back_end_game_of_quests;
import java.util.ArrayList;

public class Player {

    protected String playerID;
    protected ArrayList<Card> hand;
    private int shields;

    public Player(String playerID) {
        this.playerID = playerID;
        this.hand = new ArrayList<Card>();
        this.shields = 0;
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
    public void addShields(int shieldCount) {
        shields += shieldCount;
    }
    public int getShieldCount() {
        return shields;
    }
    public void removeShields(int shieldCount) {
        shields = Math.max(shields - shieldCount, 0);
    }
    public void sortHand() {
        hand.sort(new SortHand());
    }
    public int getHandSize() { return hand.size(); }
    public Card getCardFromHand(int index) {
        return hand.remove(index);
    }
}
