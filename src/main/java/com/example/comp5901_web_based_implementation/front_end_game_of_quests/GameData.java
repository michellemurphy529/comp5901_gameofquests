package com.example.comp5901_web_based_implementation.front_end_game_of_quests;
import java.util.ArrayList;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.*;

public interface GameData {

    public ArrayList<Player> getPlayers();

    public void setPlayers(ArrayList<Player> players);

    public Deck getEventDeck();

    public void setEventDeck(Deck eventDeck);

    public Deck getAdventureDeck();

    public void setAdventureDeck(Deck adventureDeck);

    public void addPlayer(Player player);

    public void setCurrentPlayerInHotseat(String playerID);

    public String getCurrentPlayerInHotseat();

    public void setSponsorID(String playerID);

    public String getSponsorID();

    public void setPlayerBeingAsked(String playerID);

    public String getPlayerBeingAsked();

    public void setTotalStages(String stages);

    public int getTotalStages();

    public void setEligiblePlayers(ArrayList<String> eligiblePlayers);

    public ArrayList<String> getEligiblePlayers();

    public void setParticpants(ArrayList<String> eligiblePlayers);

    public ArrayList<String> getParticipants();
}
