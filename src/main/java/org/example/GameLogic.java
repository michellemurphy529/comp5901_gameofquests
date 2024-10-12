package org.example;

import java.util.ArrayList;

public class GameLogic {

    private Deck adventureDeck;
    private Deck eventDeck;
    protected ArrayList<Player> players;
    String[] playerIDs;
    private int currentPlayerIndex;
    private Card lastEventCardDrawn;

    public GameLogic() {
        this.adventureDeck = new AdventureDeck();
        this.eventDeck = new EventDeck();
        this.players = new ArrayList<Player>();
        this.currentPlayerIndex = 0;
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
    public Deck getEventDeck() {
        return eventDeck;
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
    //Get Player Hand
    public ArrayList<Card> getPlayerHand(String playerID) {
        return getPlayer(playerID).getHand();
    }
    //Get Player
    public Player getPlayer(String playerID){
        for (Player player : players) {
            if (player.getPlayerID().equals(playerID)) {
                return player;
            }
        }
        return null;
    }
    //Deal 12 Cards at start
    public void distribute12AdventureCards() {
        for (int i = 0; i < 12; i++) {
            for (String playerID : getPlayerIDs()) {
                Player player = getPlayer(playerID);
                Card adventureCard = adventureDeck.drawCard();
                player.addCardToHand(adventureCard);
            }
        }
    }
    public Card drawCard(String playerID, Deck deck) {
        //Draw card from Deck
        Card cardDrawn = deck.drawCard();

        if(cardDrawn instanceof EventCard) {
            lastEventCardDrawn = cardDrawn;
        }

        //Add Card drawn to Players hand
        Player player = getPlayer(playerID);
        player.addCardToHand(cardDrawn);

        //return card
        return cardDrawn;
    }
    public void discardCard(String playerID, Deck deck, Card card) {
        //Remove card from Players hand
        Player player = getPlayer(playerID);
        player.removeFromHand(card);

        //Discard card into discard pile in Deck
        deck.discardCard(card);
    }
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    public ArrayList<Player> determineWinners() {
        ArrayList<Player> winners = new ArrayList<>();
        for (Player player : players) {
            if (player.getShieldCount() >= 7) {
                winners.add(player);
            }
        }
        return winners;
    }
    public Card getLastEventCardDrawn() {
        return lastEventCardDrawn;
    }
    public void carryOutPlagueAction() {
        getCurrentPlayer().removeShields(2);
    }
    public void sortPlayerHand(Player player) {
        player.sortHand();
    }
    public int getNumberOfCardsToDiscard(String playerID) {
        int handSize = getPlayer(playerID).getHandSize();
        return Math.max(0, (handSize - 12));
    }
    public void removeCardsAndDiscard(String cardStringToDiscard, String playerID) {
        String cardType = cardStringToDiscard.substring(0, 1);
        Card cardToDiscard = getCardFromHand(cardType, playerID);
        discardCard(playerID, getAdventureDeck(), cardToDiscard);
    }
    public Card getCardFromHand(String cardTypeToFind, String playerID) {
        Card cardFound = null;
        for (Card card : getPlayerHand(playerID)) {
            if(card.getType().equals(cardTypeToFind)) {
                cardFound = card;
            }
        }
        return cardFound;
    }
    public void dealNumberAdventureCards(String playerID, int cardCount) {
        for (int i = 0; i < cardCount; i++) {
            drawCard(playerID, getAdventureDeck());
        }
    }
    public void dealAllPlayersAdventureCards(String[] playerIDs, int cardCount) {
        for (String playerID : playerIDs) {
            dealNumberAdventureCards(playerID, cardCount);
        }
    }
    public ArrayList<String> determineWhatPlayersTrimHand(String[] playerIDs) {
        ArrayList<String> playersToTrim = new ArrayList<>();
        for (String playerID : playerIDs) {
            int n = getNumberOfCardsToDiscard(playerID);
            if(n > 0) {
                playersToTrim.add(playerID);
            }
        }
        return playersToTrim;
    }
}