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
    String sponsorPlayer;
    String playerBeingAsked;
    int stages;
    ArrayList<String> eligiblePlayers;
    ArrayList<String> participants;

    public NonRiggedData() {
        players = new ArrayList<>();
        eventDeck = new EventDeck();
        adventureDeck = new AdventureDeck();
        this.currentPlayer = "";
        this.sponsorPlayer = "";
        this.playerBeingAsked = "";
        this.stages = 0;
        eligiblePlayers = new ArrayList<>();
        participants = new ArrayList<>();
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

    @Override
    public void setCurrentPlayerInHotseat(String playerId) {
        this.currentPlayer = playerId;
    }

    @Override
    public String getCurrentPlayerInHotseat() {
        return this.currentPlayer;
    }

    @Override
    public void setSponsorID(String playerId) {
        this.sponsorPlayer = new String(playerId);
    }

    @Override
    public String getSponsorID() {
        return this.sponsorPlayer;
    }

    @Override
    public void setPlayerBeingAsked(String playerId) {
        this.playerBeingAsked = playerId;
    }

    @Override
    public String getPlayerBeingAsked() {
        return this.playerBeingAsked;
    }

    @Override
    public void setTotalStages(String stages) {
        int newStages = Integer.valueOf(stages);
        this.stages = newStages;
    }

    @Override
    public int getTotalStages() {
        return this.stages;
    }

    @Override
    public void setEligiblePlayers(ArrayList<String> eligiblePlayers) {
        this.eligiblePlayers = new ArrayList<>(eligiblePlayers);
    }

    @Override
    public ArrayList<String> getEligiblePlayers() {
        return this.eligiblePlayers;
    }

    @Override
    public void setParticpants(ArrayList<String> participants) {
        this.participants = new ArrayList<>(participants);
    }

    @Override
    public ArrayList<String> getParticipants() {
        return this.participants;
    }
}
