package com.example.comp5901_web_based_implementation.back_end_game_of_quests;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.springframework.stereotype.Component;

@Component
public class GameLogic {

    private Deck adventureDeck;
    private Deck eventDeck;
    protected ArrayList<Player> players;
    String[] playerIDs;
    private int currentPlayerIndex;
    private Card lastEventCardDrawn;
    private String sponsorsPlayerID;
    private ArrayList<String> eligiblePlayers;
    private ArrayList<Integer> attackValues;
    private HashMap<String,ArrayList<Card>> attackHands;
    private HashMap<Integer, ArrayList<Card>> questBuilt;
    private int currentStageValue;
    private ArrayList<String> stageLosers;
    private ArrayList<String> stageWinners;
    private int currentStageNumber;
    private int maxStages;

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
    //Get Sponsor PlayerID
    public String getSponsorID() {
        return sponsorsPlayerID;
    }
    //Set Sponsor PlayerID
    public void setSponsorID(String playerID) {
        sponsorsPlayerID = playerID;
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
        int cardValue = Integer.parseInt(cardStringToDiscard.substring(1));
        Card cardToDiscard = getCardFromHand(cardType, cardValue, playerID);
        discardCard(playerID, getAdventureDeck(), cardToDiscard);
    }
    public Card getCardFromHand(String cardTypeToFind, int cardValueToFind, String playerID) {
        //Changing to remove card from player hand when found
        Card cardFound = null;
        for (int i = 0; i < getPlayer(playerID).getHandSize(); i++) {
            Card card = getPlayer(playerID).getHand().get(i);
            if (card instanceof AdventureCard) {
                AdventureCard adventureCard = (AdventureCard) card;
                if (adventureCard.getType().equals(cardTypeToFind) && adventureCard.getValue() == cardValueToFind) {
                    cardFound = getPlayer(playerID).getCardFromHand(i);
                    break;
                }
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
    public HashMap<String, Integer> setUpWeaponCards() {
        HashMap<String, Integer> weapons = new HashMap<>();
        weapons.put("D", 0);
        weapons.put("S", 0);
        weapons.put("H", 0);
        weapons.put("B", 0);
        weapons.put("L", 0);
        weapons.put("E", 0);
        return weapons;
    }
    public void addToWeaponCards(String cardString, HashMap<String, Integer> weaponCards) {
        String weaponTypeString = cardString.substring(0, 1);
        int incrementWeapon = weaponCards.get(weaponTypeString);
        incrementWeapon++;
        weaponCards.put(weaponTypeString, incrementWeapon);
    }
    public boolean isValidStage(boolean singleFoe, HashMap<String, Integer> weaponCards) {
        for (Integer weaponCardNum : weaponCards.values()) {
            if(weaponCardNum > 1) {
                return false;
            }
        }
        return singleFoe;
    }
    public boolean hasRepeatingWeapon(String cardString, HashMap<String, Integer> weaponCards) {
        String weaponTypeString = cardString.substring(0, 1);
        return weaponCards.get(weaponTypeString) == 1;
    }
    public boolean compareCurrentStageValueIsGreaterThanPrevious(ArrayList<String> stageCards, int previousStageValue) {
        if(previousStageValue == 0) {
            return true;
        }
        int currentValue = 0;
        for (String card : stageCards) {
            String cardStringValue = card.substring(1);
            int cardValue = Integer.parseInt(cardStringValue);
            currentValue += cardValue;
        }
        return (currentValue >= previousStageValue);
    }
    public ArrayList<Card> getStageCardsFromSponsor(String sponsorID, ArrayList<String> stageStringCards) {
        ArrayList<Card> stageCards = new ArrayList<>();
        for (String cardString : stageStringCards) {
            String cardTypeString = cardString.substring(0,1);
            int cardValue = Integer.parseInt(cardString.substring(1));
            Card card = getCardFromHand(cardTypeString, cardValue, sponsorID);
            stageCards.add(card);
        }
        return stageCards;
    }
    public void sortStageCards(ArrayList<Card> stageCardsFromSponsorHand) {
        stageCardsFromSponsorHand.sort(new SortHand());
    }
    public int getValuesOfCards(ArrayList<Card> cards) {
        int stageValue = 0;
        for (Card card : cards) {
            AdventureCard adventureCard = (AdventureCard) card;
            //Add to stage value running total
            stageValue += adventureCard.getValue();
        }
        return stageValue;
    }
    public int getStageValue(int stage, HashMap<Integer, ArrayList<Card>> questBuilt) {
        ArrayList<Card> cards = questBuilt.get(stage);
        //get values of the cards in stage
        return getValuesOfCards(cards);
    }
    public boolean isValidAttack(ArrayList<Card> attackCards, HashMap<String, Integer> weaponCards) {
        if(attackCards.isEmpty()) {
            return true;
        }
        for (Integer weaponCardNum : weaponCards.values()) {
            if(weaponCardNum > 1) {
                return false;
            }
        }
        return true;
    }
    public String[] getOtherPlayerSponsorshipAskingOrder() {

        String[] currentOrder = getPlayerIDs();
        String[] sponsorOrder = new String[3];
        //new ordering that starts at next position in order
        int sponsorOrderIndex = currentPlayerIndex + 1;

        for (int i = 0; i < sponsorOrder.length; i++) {
            sponsorOrder[i] = currentOrder[sponsorOrderIndex % currentOrder.length];
            sponsorOrderIndex++;
        }
        return sponsorOrder;
    }
    public boolean isAbleToBuildQuest(Player player) {
        int numOfFoes = 0;
        for (Card card : player.getHand()) {
            if(card instanceof FoeCard foe) {
                numOfFoes++;
            }
        }
        QuestCard questCard = (QuestCard) lastEventCardDrawn;
        if(numOfFoes < questCard.getStages()) {
            return false;
        }
        return true;
    }
    public void setEligiblePlayers(String[] players) {
        eligiblePlayers = new ArrayList<>();
        eligiblePlayers.addAll(Arrays.asList(players));
    }
    public ArrayList<String> getEligiblePlayers() {
        return eligiblePlayers;
    }
    public void removePlayerFromSubsequentStages(ArrayList<String> playersToRemove) {
        eligiblePlayers.removeAll(playersToRemove);
    }
    public boolean checkForParticipants() {
        return getEligiblePlayers().isEmpty();
    }
    public ArrayList<Integer> getAttackValues() {
        return attackValues;
    }
    public HashMap<String, ArrayList<Card>> getAttackHands() {
        return attackHands;
    }
    public void setAttackValues() {
        attackValues = new ArrayList<>();
    }
    public void setAttackHands() {
        attackHands = new HashMap<>();
        for (String participantID : getEligiblePlayers()) {
            attackHands.put(participantID, new ArrayList<>());
        }
    }
    public void addAttackCards(String participantID, ArrayList<Card> attackCards) {
        ArrayList<Card> participantAttack = attackHands.get(participantID);
        participantAttack.addAll(attackCards);
    }
    public int getAttackValue(ArrayList<Card> attackCards) {
        int attackSum = 0;
        for (Card card : attackCards) {
            AdventureCard adventureCard = (AdventureCard) card;
            attackSum += adventureCard.getValue();
        }
        return attackSum;
    }
    public void addAttackValue(int positionInArray, int attackValue) {
        attackValues.add(positionInArray, attackValue);
    }
    public void discardAttackCards(ArrayList<Card> cards) {
        for (Card card : cards) {
            adventureDeck.discardCard(card);
        }
    }
    public void clearAttackHand(String key) {
        attackHands.get(key).clear();
    }
    public HashMap<Integer, ArrayList<Card>> getQuestInfo() {
        return questBuilt;
    }
    public void setQuestInfo(int stages) {
        questBuilt = new HashMap<>();
        for (int i = 0; i < stages; i++) {
            Integer stageNumber = i + 1;
            questBuilt.put(stageNumber, new ArrayList<>());
        }
    }
    public void addCardstoQuestInfo(int stage, ArrayList<Card> cards) {
        Integer stageKey = stage;
        questBuilt.get(stageKey).addAll(cards);
    }
    public void setCurrentStageValue(int stageValue) {
        currentStageValue = stageValue;
    }
    public int getCurrentStageValue() {
        return currentStageValue;
    }
    public void setStageLosers() {
        stageLosers = new ArrayList<>();
    }
    public ArrayList<String> getStageLosers() {
        return stageLosers;
    }
    public void setCurrentStageNumber(int currentStage) {
        currentStageNumber = currentStage;
    }
    public boolean compareStageValueToCurrentStageValue(int attackValue) {
        return attackValue < getCurrentStageValue();
    }
    public void addToLosers(String participantID) {
        stageLosers.add(participantID);
    }
    public void incrementStageNumber() {
        currentStageNumber++;
    }
    public void setMaxStages(int stages) {
        maxStages = stages;
    }
    public int getMaxStages() {
        return maxStages;
    }
    public int getCurrentStageNumber() {
        return currentStageNumber;
    }
    public void setEligiblePlayersWithoutSponsor() {
        ArrayList<String> eligiblePlayers = new ArrayList<>();
        String[] playerIDs = getPlayerIDs();

        for (String playerID : playerIDs) {
            if (!playerID.equals(getSponsorID())) {
                eligiblePlayers.add(playerID);
            }
        }
        setEligiblePlayers(eligiblePlayers.toArray(new String[0]));
    }
    public ArrayList<String> getStageWinners() {
        return stageWinners;
    }
    public void setStageWinners() {
        stageWinners = new ArrayList<>();
    }
    public void addToWinners(String participantID) {
        stageWinners.add(participantID);
    }
    public void addShieldsToWinners(ArrayList<String> stageWinners) {
        for (String participantID : stageWinners) {
            getPlayer(participantID).addShields(getMaxStages());
        }
    }
    public int getNumberofCardsUsedInQuestAndDiscard() {
        int numberOfCards = 0;
        for(Integer key : questBuilt.keySet()) {
            ArrayList<Card> cards = questBuilt.get(key);
            for(Card card : cards) {
                discardCard(getSponsorID(), adventureDeck, card);
                numberOfCards++;
            }
        }
        return numberOfCards;
    }
    public int getCurrentStageValueFromQuestInfo() {
        return getStageValue(getCurrentStageNumber(), getQuestInfo());
    }
}