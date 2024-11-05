package com.example.comp5901_web_based_implementation.front_end_game_of_quests;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.*;

@Component
public class NonRiggedData implements GameData {
    ArrayList<Player> players;
    Deck eventDeck;
    Deck adventureDeck;
    Card topEventCard;
    Card topAdventureCard;
    int currentPlayer;

    public NonRiggedData() {
        players = new ArrayList<>();
        eventDeck = new EventDeck();
        adventureDeck = new AdventureDeck();
        topEventCard = new EventCard(null);
        topAdventureCard = new AdventureCard("",0);
        this.currentPlayer = 1;
    }

    @Override
    public Card getTopAdventureCard() {
        try {
            if (this.topAdventureCard == null) {
                throw new IllegalStateException("Top Adventure Card has not been initialized.");
            }
            return this.topAdventureCard;
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void setTopAdventureCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null!");
        }
        if (!(card instanceof AdventureCard)) {
            throw new IllegalArgumentException("Invalid card type. Expected AdventureCard.");
        }
        AdventureCard adventureCard = (AdventureCard) card;
        this.topAdventureCard = adventureCard;
    }

    @Override
    public Card getTopEventCard() {
        try {
            if (this.topEventCard == null) {
                throw new IllegalStateException("Top Event Card has not been initialized.");
            }
            return this.topEventCard;
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void setTopEventCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null!");
        }
            if (!(card instanceof EventCard)) {
            throw new IllegalArgumentException("Invalid card type. Expected EventCard.");
        }
        EventCard eventCard = (EventCard) card;
        this.topEventCard = eventCard;
    }

    @Override
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    @Override
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    @Override
    public Deck getEventDeck() {
        return this.eventDeck;
    }

    @Override
    public void setEventDeck(Deck eventDeck) {
        this.eventDeck = eventDeck;
    }

    @Override
    public Deck getAdventureDeck() {
        return this.adventureDeck;
    }

    @Override
    public void setAdventureDeck(Deck adventureDeck) {
        this.adventureDeck = adventureDeck;
    }

    @Override
    public void addPlayer(Player p) {
        this.players.add(p);
    }

    public void setCurrentPlayer(int id) {
        this.currentPlayer = id;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }
}
