package com.example.comp5901_web_based_implementation.back_end_game_of_quests;
import java.util.ArrayList;

public class EventDeck extends Deck {
    protected ArrayList<Card> discards;

    public EventDeck() {
        super();
        this.discards = new ArrayList<Card>();
    }
    @Override
    public int getSize() {
        return cards.size();
    }
    @Override
    public int getDiscardPileSize() {
        return discards.size();
    }
    @Override
    public ArrayList<Card> getDeck() {
        return cards;
    }
    @Override
    public ArrayList<Card> getDiscardPile() {
        return discards;
    }
    @Override
    protected void initializeDeck() {
        // Adding 12 Quest Cards
        addQuestCards(2, 3);    //3 - Q2
        addQuestCards(3, 4);    //4 - Q3
        addQuestCards(4, 3);    //3 - Q4
        addQuestCards(5, 2);    //2 - Q5

        // Adding 5 Event Cards
        //1 Plague Card
        cards.add(new PlagueCard());
        //2 Queen's favor Cards
        cards.add(new QueensFavorCard());
        cards.add(new QueensFavorCard());
        //2 Prosperity Cards
        cards.add(new ProsperityCard());
        cards.add(new ProsperityCard());
    }
    private void addQuestCards(int stages, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cards.add(new QuestCard(stages));
        }
    }
    @Override
    public Card drawCard() {
        if(cards.isEmpty()){
            useDiscardsAndReshuffle();
        }
        return cards.remove(0);
    }
    private void useDiscardsAndReshuffle() {
        cards.addAll(discards);
        shuffle();
        discards.clear();
    }
    public void discardCard(Card card) {
        discards.add(card);
    }
}

