import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//creates a shoe of cards and deals them
public class Shoe {
    private List<String> shoe;
    private List<String> discardPile;
    private double shufflePercent = 0.3;


    //constructor
    public Shoe(int numDecks) {
        initializeShoe(numDecks);
        shuffle();
    }

    //initialize the shoe with desired decks of cards
    private void initializeShoe(int numDecks) {
        shoe = new ArrayList<>();
        discardPile = new ArrayList<>();
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

        //add all cards to the shoe
        for (int i = 0; i < numDecks; i++) {
            for (String rank : ranks) {
                for (String suit : suits) {
                    shoe.add(rank + " of " + suit);
                }
            }
        }
    }

    public void shuffle() {
        shoe.addAll(discardPile);
        discardPile.clear();
        Collections.shuffle(shoe);
    }

    public String dealCard() {
        //set an arbitrary number of cards to be left in the shoe before reshuffling
        //in this case, shuffle when 70% of the shoe has been dealt
        int stopShuffle = (int) (shoe.size() * shufflePercent);
        if (shoe.size() < stopShuffle) {
            shuffle();
        }
        String dealtCard = shoe.remove(0);
        discardPile.add(dealtCard);
        return dealtCard;
    }

    //get the value of a card
    public static int getCardValue(String card) {
        String rank = card.split(" ")[0];
        switch (rank) {
            case "A":
                return 11;
            case "K":
            case "Q":
            case "J":
                return 10;
            default:
                return Integer.parseInt(rank);
        }
    }
}
