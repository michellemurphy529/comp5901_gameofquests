package com.example.comp5901_web_based_implementation.front_end_game_of_quests;
import java.util.ArrayList;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.*;

public interface GameData {
    public Card getTopEventCard();

    public void setTopEventCard(Card eventCard);

    public ArrayList<Player> getPlayers();

    public void setPlayers(ArrayList<Player> players);

    public Deck getEventDeck();

    public void setEventDeck(Deck eventDeck);

    public Deck getAdventureDeck();

    public void setAdventureDeck(Deck adventureDeck);

    public void addPlayer(Player player);

    public void setCurrentPlayerInHotseat(String playerID);

    public String getCurrentPlayerInHotseat();
}
