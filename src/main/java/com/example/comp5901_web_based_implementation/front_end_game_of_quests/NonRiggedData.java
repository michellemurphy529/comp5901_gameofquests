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
    String currentPlayer;

    public NonRiggedData() {
        players = new ArrayList<>();
        eventDeck = new EventDeck();
        adventureDeck = new AdventureDeck();
        topEventCard = new EventCard(null);
        this.currentPlayer = "";
    }

    @Override
    public Card getTopEventCard() {
        try {
            if (this.topEventCard == null) {
                throw new IllegalStateException("Top event Card has not been initialized.");
            }
            return this.topEventCard;
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void setTopEventCard(Card eventCard) {
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

    public void setCurrentPlayerInHotseat(String playerId) {
        this.currentPlayer = playerId;
    }

    public String getCurrentPlayerInHotseat() {
        return this.currentPlayer;
    }
}
