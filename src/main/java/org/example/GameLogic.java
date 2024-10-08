package org.example;

import java.util.ArrayList;

public class GameLogic {

    private Deck adventureDeck;
    private Deck eventDeck;
    protected ArrayList<Player> players;
    String[] playerIDs;
    private Deck adventureDiscard;
    private Deck eventDiscard;

    public GameLogic() {
        //Adventure Decks
        this.adventureDeck = new AdventureDeck();
        this.adventureDiscard = new AdventureDeck();
        //Event Decks
        this.eventDeck = new EventDeck();
        this.eventDiscard = new EventDeck();
        //Players
        this.players = new ArrayList<Player>();
    }

    //Set Decks
    public void setAdventureDeck() {
        adventureDeck.initializeDeck();
    }
    public void setEventDeck() {
        eventDeck.initializeDeck();
    }

    //Get Decks
    public Deck getAdventureDeck() {
        return adventureDeck;
    }
    public Deck getAdventureDiscardDeck() {
        return adventureDiscard;
    }
    public Deck getEventDeck() {
        return eventDeck;
    }
    public Deck getEventDiscardDeck() {
        return eventDiscard;
    }

    //Set Players
    public void setUpPlayers() {
        setUpPlayerIDs();
        for (String ID : getPlayerIDs()) {
            players.add(new Player(ID));
        }
    }
    //Get Players
    public ArrayList<Player> getPlayers() {
        return players;
    }

    //Set Player IDs
    public void setUpPlayerIDs() {
        playerIDs = new String[] {"P1", "P2", "P3", "P4"};
    }
    //Get Player IDs
    public String[] getPlayerIDs() {
        return playerIDs;
    }

    public void distribute12AdventureCards() {
        for (int i = 0; i < 12; i++) {
            for (String playerID : getPlayerIDs()) {
                Player player = getPlayer(playerID);
                Card adventureCard = adventureDeck.drawCard();
                player.addCardToHand(adventureCard);
            }
        }
    }

    public ArrayList<Card> getPlayerHand(String playerID) {
        return getPlayer(playerID).getHand();
    }

    public Player getPlayer(String playerID){
        for (Player player : players) {
            if (player.getPlayerID().equals(playerID)) {
                return player;
            }
        }
        return null;
    }

    public Card drawCard(String playerID, Deck deck) {
        return null;
    }
}