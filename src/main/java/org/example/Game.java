package org.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Game {

    protected GameLogic gameLogic;
    protected GameDisplay gameDisplay;
    protected Scanner input;

    public Game(GameLogic gameLogic, GameDisplay gameDisplay) {
        this.gameLogic = gameLogic;
        this.gameDisplay = gameDisplay;
        this.input = new Scanner(System.in);
    }
    //Forcing Test Inputs
    public void setInput(Scanner overrideInput) {
        this.input = overrideInput;
    }
    public void setDecks() {
        gameLogic.setAdventureDeck();
        gameLogic.setEventDeck();
    }
    //Get Decks
    public Deck getAdventureDeck() {
        return gameLogic.getAdventureDeck();
    }
    public Deck getEventDeck() {
        return gameLogic.getEventDeck();
    }
    //Set Players
    public void setPlayers() {
        gameLogic.setUpPlayers();
    }
    //Get Players
    public ArrayList<Player> getPlayers() {
        return gameLogic.getPlayers();
    }
    //Get Player IDs
    public String[] getPlayerIDs() {
        return gameLogic.getPlayerIDs();
    }
    //Deal first 12 cards
    public void dealInitial12AdventureCards() {
        gameLogic.distribute12AdventureCards();
    }
    //Draw Cards
    public Card drawAdventureCard(String playerID) {
        return gameLogic.drawCard(playerID, gameLogic.getAdventureDeck());
    }
    public Card drawEventCard(String playerID) {
        return gameLogic.drawCard(playerID, gameLogic.getEventDeck());
    }
    //Discard Cards
    public void discardAdventureCard(String playerID, Card card) {
        gameLogic.discardCard(playerID, gameLogic.getAdventureDeck(), card);
    }
    public void discardEventCard(String playerID, Card card) {
        gameLogic.discardCard(playerID, gameLogic.getEventDeck(), card);
    }
    //Get Current Player Turn
    public Player getCurrentPlayer() {
        return gameLogic.getCurrentPlayer();
    }
    //Get Current player hand
    public void displayCurrentPlayerHand() {
        gameLogic.sortPlayerHand(getCurrentPlayer());
        gameDisplay.displayPlayerHand(getCurrentPlayer());
    }
    public void displayPlayerHand(String playerID) {
        gameLogic.sortPlayerHand(gameLogic.getPlayer(playerID));
        gameDisplay.displayPlayerHand(gameLogic.getPlayer(playerID));
    }
    public void playTurn() {
        //Logic for Current Player Turn
        //Display to user which Player's turn it is
        String playerID = getCurrentPlayer().getPlayerID();
        gameDisplay.displayTurn(playerID);
        //Draw Event Card on next Player's turn
        gameDisplay.drawingEventCardMessage();
        Card cardDrawn = drawEventCard(playerID);
        //Display Card to user
        gameDisplay.displayCardDrawn(cardDrawn);
        //Discard Event card from hand before carrying out the Event card actions
        discardEventCard(playerID, cardDrawn);

        //EVENT CARD ACTIONS
        //Plague Card Drawn
        if(cardDrawn.getType().equals("Plague")) {
            gameLogic.carryOutPlagueAction();
        }
        //Queen's Favor Card Drawn
        if(cardDrawn.getType().equals("Queen's Favor")) {
            //Current player draws 2 Adventure Cards
            gameLogic.dealNumberAdventureCards(playerID, 2);
            //Possibly Trims Player Hand (Queen's favor)
            trimHandPlayer(playerID, computeNumberOfCardsToDiscard(playerID));
        }
        //Prosperity Card Drawn
        if(cardDrawn.getType().equals("Prosperity")) {
            //All Players draw 2 Adventure Cards
            gameLogic.dealAllPlayersAdventureCards(getPlayerIDs(), 2);
            //Possibly trim all Player's Hand (Prosperity)
            trimHandAction(getPlayerIDs());
        }
        if(cardDrawn.getType().equals("Q")) {
            carryOutQuestAction();
        }

        //Next Player Logic
        //Next Turn invoked
        gameLogic.nextTurn();
    }
    public void dealNumberOfAdventureCardsToPlayer(String playerID, int numberOfCards) {
        gameLogic.dealNumberAdventureCards(playerID, numberOfCards);
    }
    public ArrayList<Player> getWinners() {
        return gameLogic.determineWinners();
    }
    public void displayWinnersAndTerminate(ArrayList<Player> winners) {
        gameDisplay.displayWinners(winners);
        gameDisplay.displayTerminationMessage();
    }
    public void displayNoWinners() {
        gameDisplay.displayNoWinners();
    }
    public void processEndOfQuest() {
        ArrayList<Player> winners = getWinners();
        if(winners.isEmpty()){
            displayNoWinners();
            playTurn();
        }else{
            displayWinnersAndTerminate(winners);
        }
    }
    public Card getLastEventCardDrawn() {
        return gameLogic.getLastEventCardDrawn();
    }
    public int computeNumberOfCardsToDiscard(String playerID) {
        return gameLogic.getNumberOfCardsToDiscard(playerID);
    }
    public void displayTrimmedHand(String playerID) {
        //Calling Trim hand message
        gameDisplay.displayTrimmedHandMessage();
        //Sort player hand who has had their trim completed
        gameLogic.sortPlayerHand(gameLogic.getPlayer(playerID));
        //Display trimmed hand of player
        gameDisplay.displayPlayerHand(gameLogic.getPlayer(playerID));
    }
    public void trimHandAction(String[] playerIDs) {
        //All Players are checked for their hand to be trimmed
        ArrayList<String> playerIDsToTrim = gameLogic.determineWhatPlayersTrimHand(playerIDs);
        if(!playerIDsToTrim.isEmpty()) {
            for (String playerID : playerIDsToTrim) {
                trimHandPlayer(playerID, computeNumberOfCardsToDiscard(playerID));
            }
        }
    }
    public void trimHandPlayer(String playerID, int n) {
        //Display current Player Hand
        displayPlayerHand(playerID);
        int numberOfCardsToDiscard = n;

        //Begin Trimming loop
        for (int i = 0; i < numberOfCardsToDiscard; i++) {
            //Display Prompt for player to discard n cards
            gameDisplay.promptForDiscardCards(n);
            String cardToDiscard = gameDisplay.getDiscardInput(input);
            gameLogic.removeCardsAndDiscard(cardToDiscard, playerID);
            n -= 1;
        }
        displayTrimmedHand(playerID);
    }
    public void stageIsValidAndDisplayCards(HashMap<Integer, ArrayList<Card>> questBuilt) {
        boolean stageIsValid = gameDisplay.getUserInputBuildStage(input);
        if(stageIsValid) {
            gameDisplay.displayBuiltQuest(questBuilt);
        }
    }
    public void displaySponsorHandAndSetUpStages(String playerID, int stages) {
        //Building Quest from stageCards
        //Initialize questBuilt
        gameLogic.setQuestInfo(stages);
        //Display building Quest with stage number
        gameDisplay.displayBuildingQuestMessage(stages);

        //make sure current stage equals the number of stages to break loop
        int currentStage = 0;
        //Previous stage value
        int previousStageValue = 0;

        while (currentStage != stages) {
            //Display current Stage
            gameDisplay.displayBuildingStageMessage(currentStage + 1);

            //Get cards from user for stage selection
            ArrayList<String> stageStringCards = getCardsForStage(playerID, currentStage + 1, previousStageValue);
            //Get actual cards from sponsor hand
            ArrayList<Card> stageCardsFromSponsorHand = gameLogic.getStageCardsFromSponsor(playerID, stageStringCards);
            //sort stage cards before adding to quest hashmap
            gameLogic.sortStageCards(stageCardsFromSponsorHand);

            //Get cards from sponsor hand
            gameLogic.addCardstoQuestInfo(currentStage + 1, stageCardsFromSponsorHand);
            //Update Stage value to have it updated after we get the cards from sponsor
            previousStageValue = gameLogic.getStageValue(currentStage + 1, gameLogic.getQuestInfo());

            currentStage++;
        }
    }
    public ArrayList<String> getCardsForStage(String playerID, int currentStage, int previousStageValue) {
        //check weapons are non repeating
        HashMap<String, Integer> weaponCards = gameLogic.setUpWeaponCards();
        //ArrayList to return the stage
        ArrayList<String> stageCards = new ArrayList<>();

        //Input loop
        //quit entered and a single foe entered to leave loop
        boolean quitEntered = false, singleFoe = false;

        while (!(quitEntered && gameLogic.isValidStage(singleFoe, weaponCards))) {
            //Display Sponsor hand
            displayPlayerHand(playerID);
            //Prompt Sponsor to select card or 'Quit'
            gameDisplay.displayPromptForSelectingStageCards();
            String inputReceived = gameDisplay.displayPromptSelectCardForStage(input);

            //Determining if Stage has sufficient value
            boolean sufficientValue = gameLogic.compareCurrentStageValueIsGreaterThanPrevious(stageCards, previousStageValue);

            //Deal with string received
            //Quit is entered and the stage is not empty and there is sufficient value for this stage
            if (inputReceived.equalsIgnoreCase("quit") && !stageCards.isEmpty() && sufficientValue) {
                quitEntered = true;
            }
            else if (inputReceived.equalsIgnoreCase("quit") && stageCards.isEmpty()) {
                gameDisplay.displayStageCannotBeEmptyMessage();
            }
            //Case where there is not a sufficient value between stages
            else if (inputReceived.equalsIgnoreCase("quit") && !sufficientValue) {
                gameDisplay.displayInsufficientValueMessage();
            }
            //Case where Foe has not been added to stage yet
            else if (inputReceived.contains("F") && !singleFoe) {
                //single Foe added and flag turned to true so no more Foe's are added
                singleFoe = true;

                //Add card to stage and display to user
                stageCards.addLast(inputReceived);
                gameDisplay.addedCardToStageMessage(inputReceived);
                gameDisplay.displayStageCards(stageCards, currentStage);
            }
            //Case where Single Foe is already in stage
            else if (inputReceived.contains("F") && singleFoe) {
                gameDisplay.displayFoeAlreadyInStageMessage();
            }
            //Case where weapon card is checked to see if it's already in stage
            else if(!gameLogic.hasRepeatingWeapon(inputReceived, weaponCards)) {
                //Add weaponcard to make sure not repeating in future
                gameLogic.addToWeaponCards(inputReceived, weaponCards);

                //Add card to stage and display to user
                stageCards.addLast(inputReceived);
                gameDisplay.addedCardToStageMessage(inputReceived);
                gameDisplay.displayStageCards(stageCards, currentStage);
            }
            //Case where it is a repeated weapon card
            else {
                gameDisplay.displayRepeatedWeaponCardMessage();
            }
        }
        return stageCards;
    }
    //Attack methods
    public void displaySetUpForAttackAndPlayerHand(String playerID) {
        gameDisplay.displaySettingUpAttackForPlayer();
        gameDisplay.displayPlayerHand(gameLogic.getPlayer(playerID));
    }
    public ArrayList<Card> promptPlayerToSelectCardOrQuit(String playerID) {
        //check weapons are non repeating
        HashMap<String, Integer> weaponCards = gameLogic.setUpWeaponCards();
        ArrayList<Card> attackCards = new ArrayList<>();

        boolean quitEntered = false;
        while(!quitEntered) {

            //Prompt participant to select card or 'quit' to end valid attack
            gameDisplay.displayPromptAttackSelectCards();
            String inputReceived = gameDisplay.displayPromptSelectCardForStage(input);

            if(inputReceived.equalsIgnoreCase("quit") && gameLogic.isValidAttack(attackCards, weaponCards)) {
                quitEntered = true;
            }
            //Case where weapon card is checked to see if it's already in attack
            else if(!gameLogic.hasRepeatingWeapon(inputReceived, weaponCards)) {
                //Add weaponcard to make sure not repeating in future
                gameLogic.addToWeaponCards(inputReceived, weaponCards);

                //Add card to attack and display to user
                String cardType = inputReceived.substring(0,1);
                int cardValue = Integer.parseInt(inputReceived.substring(1));
                Card cardFromParticipant = gameLogic.getCardFromHand(cardType, cardValue, playerID);
                attackCards.addLast(cardFromParticipant);
                gameDisplay.addedCardToAttackMessage(inputReceived);
                gameDisplay.displayAttackCards(attackCards);
            }
            //Case where weapon cards are repeated
            else if(gameLogic.hasRepeatingWeapon(inputReceived, weaponCards)) {
                gameDisplay.displayRepeatedWeaponCardMessage();
            }
        }
        return attackCards;
    }
    public ArrayList<Card> getAttackCardsAndDisplayToUser(String participantID) {
        //Get the participants attack
        ArrayList<Card> participantAttack = promptPlayerToSelectCardOrQuit(participantID);
        //display attack is complete
        gameDisplay.displayAttackSetUpCompleted();
        gameDisplay.displayParticipantAttack(participantAttack);

        return participantAttack;
    }
    //Quest methods
    public void carryOutQuestAction() {
        //Current player can either sponsor or decline
        promptCurrentPlayerToSponsor();
        if(getSponsorPlayerID() != null) {
            gameLogic.setCurrentStageNumber(0);
            QuestCard questCard = (QuestCard) getLastEventCardDrawn();
            //Set max stages
            gameLogic.setMaxStages(questCard.getStages());
            //Set eligible players without sponsor
            gameLogic.setEligiblePlayersWithoutSponsor();
            beginQuest();
        }
    }
    public void beginQuest() {
        gameDisplay.displayQuestBegins();
        while(gameLogic.getCurrentStageNumber() != gameLogic.getMaxStages()) {
            gameLogic.incrementStageNumber();
            showEligiblePlayersForStage(gameLogic.getCurrentStageNumber());
            promptToParticipateInCurrentStage();
            //participants set up attack
            participantsSetUpAttacks();
            resolveAttacks();
            discardParticipantsCards();
        }
    }
    public void draw1AdventureCardForParticipantAndTrim(String participantID) {
        //System draws 1 adventure card to add to a participant’s hand and possibly trims hand
        dealNumberOfAdventureCardsToPlayer(participantID, 1);
        int n = computeNumberOfCardsToDiscard(participantID);
        if(n > 0) {
            trimHandPlayer(participantID, n);
        }
    }
    public void promptCurrentPlayerToSponsor() {
        //prompt current player for sponsorship
        gameDisplay.promptForSponsorship();
        String inputReceived = gameDisplay.displayPromptSelectCardForStage(input);
        if(inputReceived.equalsIgnoreCase("yes")) {
            gameLogic.setSponsorID(getCurrentPlayer().getPlayerID());
            gameDisplay.displaySponsorshipAccepted();
        }else if(inputReceived.equalsIgnoreCase("no")){
            gameDisplay.displaySponsorshipNotAccepted();
            promptOtherPlayersToSponsor();
        }
    }
    public String getSponsorPlayerID() {
        return gameLogic.getSponsorID();
    }
    public void promptOtherPlayersToSponsor() {
        String[] askingOrder = gameLogic.getOtherPlayerSponsorshipAskingOrder();
        int playersDeclined = 0;
        for (String playerID : askingOrder) {
            //If player does not have enough foe cards for the number of stages they are skipped
            if(!gameLogic.isAbleToBuildQuest(gameLogic.getPlayer(playerID))) {
                gameDisplay.skippingForSponsorship(playerID);
            }
            else {
                //prompt next player for sponsorship
                gameDisplay.askingPlayerToSponsor(playerID);
                gameDisplay.promptForSponsorship();
                //get input from player
                String inputReceived = gameDisplay.displayPromptSelectCardForStage(input);
                if(inputReceived.equalsIgnoreCase("yes")) {
                    //set sponsor as player that accepted
                    gameLogic.setSponsorID(playerID);
                    gameDisplay.displaySponsorshipAccepted();
                    break;
                }else if(inputReceived.equalsIgnoreCase("no")){
                    gameDisplay.displaySponsorshipNotAccepted();
                    playersDeclined++;
                }
            }
            if(playersDeclined == 3) {
                gameDisplay.displayNoSponsor();
                gameDisplay.displayQuestEnded();
            }
        }
    }
    public void showEligiblePlayersForStage(int stageNumber) {
        //get list of eligbile players
        ArrayList<String> eligiblePlayers = gameLogic.getEligiblePlayers();
        gameDisplay.displayEligiblePlayers(eligiblePlayers, stageNumber);
    }
    public void promptToParticipateInCurrentStage() {
        ArrayList<String> playersToRemove = new ArrayList<>();
        //get list of eligbile players
        ArrayList<String> eligiblePlayers = gameLogic.getEligiblePlayers();
        for (String playerID : eligiblePlayers) {
            gameDisplay.promptPlayersToParticipateInStage(playerID);
            String inputReceived = gameDisplay.displayPromptSelectCardForStage(input);
            if(inputReceived.equalsIgnoreCase("no")) {
                playersToRemove.add(playerID);
            }
            if(inputReceived.equalsIgnoreCase("yes")) {
                draw1AdventureCardForParticipantAndTrim(playerID);
            }
        }
        gameLogic.removePlayerFromSubsequentStages(playersToRemove);
        if(noParticipantsFound()) {
            gameDisplay.displayNoParticipants();
            gameDisplay.displayQuestEnded();
        }
    }
    public boolean noParticipantsFound() {
        return gameLogic.checkForParticipants();
    }
    public void participantsSetUpAttacks() {
        //Set up HashMap and arrayList to hold attack cards and attack values
        gameLogic.setAttackHands();
        gameLogic.setAttackValues();
        //Each participant prepares an attack with one or more non-repeated weapon cards
        for(int i = 0; i < gameLogic.getEligiblePlayers().size(); i++) {
            String participantID = gameLogic.getEligiblePlayers().get(i);
            //Display to participant who's turn it is
            displaySetUpForAttackAndPlayerHand(participantID);
            //Prompt for attack cards
            ArrayList<Card> attackCards = getAttackCardsAndDisplayToUser(participantID);
            //Add attack cards to array list that holds the same index as the place in the eligible participant list
            gameLogic.addAttackCards(participantID, attackCards);
            //Get the sum of the attack
            int attackValue = gameLogic.getAttackValue(attackCards);
            //Add to the attack value list in the right position
            gameLogic.addAttackValue(i, attackValue);
        }
    }
    public void discardParticipantsCards() {
        for (String participantID : gameLogic.getAttackHands().keySet()) {
            gameLogic.discardAttackCards(gameLogic.getAttackHands().get(participantID));
            gameLogic.clearAttackHand(participantID);
        }
    }
    //Clear display methods
    public void promptToLeaveHotSeat() {
        gameDisplay.promptToLeaveHotSeat();
        String inputRecevied = gameDisplay.getLeaveHotseatInput(input);
    }
    public void flushDisplay() {
        gameDisplay.flushDisplay();
    }
    public void displayPlayerInHotSeat() {
        gameDisplay.displayPlayerIDInHotSeat(getCurrentPlayer().getPlayerID());
    }
    public void resolveAttacks() {
        //Set stage losers array list
        gameLogic.setStageLosers();
        for(int i = 0; i < gameLogic.getEligiblePlayers().size(); i++) {
            boolean isLoser = gameLogic.compareStageValueToCurrentStageValue(gameLogic.getAttackValues().get(i));
            if(isLoser) {
                gameLogic.addToLosers(gameLogic.getEligiblePlayers().get(i));
            }
        }
        gameLogic.removePlayerFromSubsequentStages(gameLogic.getStageLosers());
    }

    public static void main(String[] args) {
        //Initialize game
        Game game = new Game(new GameLogic(), new GameDisplay());

        //Set up decks
        game.setDecks();

        //Shuffle decks
        Deck adventureDeck = game.getAdventureDeck();
        Deck eventDeck = game.getEventDeck();

        adventureDeck.shuffle();
        eventDeck.shuffle();

        //Set Players
        game.setPlayers();
        //Distribute 12 Adventure Cards to each player
        game.dealInitial12AdventureCards();
    }
}
