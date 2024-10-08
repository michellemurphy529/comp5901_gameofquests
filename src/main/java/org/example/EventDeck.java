package org.example;

import java.util.ArrayList;

public class EventDeck extends Deck {

    public EventDeck() {
        super();
    }

    @Override
    public int getSize() {
        return cards.size();
    }

    @Override
    public int getDiscardPileSize() {
        return 0;
    }

    @Override
    public ArrayList<Card> getDeck() {
        return cards;
    }

    @Override
    public ArrayList<Card> getDiscardPile() {
        return null;
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
        return null;
    }
}

