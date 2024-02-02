import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<String> cards;
    private int handValue;
    private int numAcesChanged; 

    //constructor
    public Hand() {
        cards = new ArrayList<>();
        handValue = 0;
        numAcesChanged = 0;
    }

    public void addCard(String card) {
        cards.add(card);
    }

    public List<String> getCards() {
        return cards;
    }

    public int getHandValue() {
        return handValue;
    }

    //gets the value of the hand for the dealer, and updates the handValue variable
    //by default dealer "chooses" 11 for aces, but if the hand value is over 21, it will change the value of the ace to 1
    public void calcDealerHandValue() {
        int value = 0;
        int numAces = 0;

        for (String card : cards) {
            int cardValue = Shoe.getCardValue(card);
            value += cardValue;
            if (cardValue == 11) {
                numAces++;
            }
        }

        //if the dealer has a hand value over 21, and has aces, change the value of the ace to 1
        if (value > 21 && numAces > 0) {
            while (value > 21 && numAces > 0) {
                value -= 10;
                numAces--;
                
            }
        }

        handValue = value;
    }

    //similar to getDealerHandValue, but the player can choose whether to use 1 or 11 for aces
    public void calcPlayerHandValue() {
        int value = 0;
        int numAces = 0;

        for (String card : cards) {
            int cardValue = Shoe.getCardValue(card);
            value += cardValue;
            if (cardValue == 11) {
                numAces++;
            }
        }

        if (numAces > numAcesChanged && value >= 21) {
            System.out.println("You've drawn an ace! It counts as 11, making your new sum " + value + 
                                ". Would you like to change the ace to 1 or keep the value at 11 ffor your ace(s)?" +
                                "\nNote: Any input other than 1 will be interpreted as 11.");
            int aceValue = Integer.parseInt(System.console().readLine());
            //if the user does not enter 1, program assumes 11 was selected. This acts as a failsafe for invalid inputs
            if (aceValue == 1) {
                while (numAces > 0) {
                    value -= 10;
                    numAces--;
                    //to avoid asking the user to change the value of an ace they already changed, keep track of how many aces have been changed
                    numAcesChanged++; 
                }
            }
        }

        handValue = value;
    }

    public void printHand(String playerOrDealer) {
        System.out.println(playerOrDealer + " hand: " + String.join(", ", cards) + "\nWith a value of: " + handValue);
    }

    //resets the hand
    public void clearHand() {
        cards.clear();
        handValue = 0;
        numAcesChanged = 0;
    }

    //checks if the hand is a blackjack
    public Boolean isBlackjack() {
        return handValue == 21 && cards.size() == 2;
    }

    //checks if the hand is a bust
    public Boolean isBust() {
        return handValue > 21;
    }
}
