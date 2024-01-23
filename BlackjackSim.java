
public class BlackjackSim {

    static int playerMoney = 1000;
    public static void main(String[] args) {
        //create shoe with 4 decks
        Shoe shoe = new Shoe(4);

        
        int playerBet = 0;

        System.out.println("\nWelcome to Blackjack! Type 'quit' to end the game or go bankrupt!");

        while (playerMoney > 0) {
            Boolean playerBust = false;
            Boolean playerBlackjack = false;

            System.out.println("\n-------------------------");
            System.out.println("\tNew Round");
            System.out.println("-------------------------");


            System.out.println("You have $" + playerMoney + ". How much would you like to bet?");
            String input = System.console().readLine();
            if(input.equalsIgnoreCase("quit")) {
                break;
            }
            try {
                playerBet = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            if (playerBet > playerMoney) {
                System.out.println("You don't have that much money!");
                continue;
            }
            if (playerBet <= 0) {
                System.out.println("You must bet at least $1!");
                continue;
            }

            //create initial hands
            Hand playerHand = new Hand();
            playerHand.addCard(shoe.dealCard());
            playerHand.addCard(shoe.dealCard());
            playerHand.calcPlayerHandValue();

            if (playerHand.isBlackjack()) {
                System.out.println("Natural Blackjack! You win!");
                playerMoney += playerBet*2;
                continue;
            }

            Hand dealerHand = new Hand();
            dealerHand.addCard(shoe.dealCard());
            dealerHand.calcDealerHandValue();
            
            System.out.println("Your hand: " + String.join(", ", playerHand.getCards()) + "\nWith a value of: " + playerHand.getHandValue());
            
            delay(2000);

            System.out.println("Dealer's hand: " + dealerHand.getCards().get(0) + "\nWith a value of: " + dealerHand.getHandValue());
            
            delay(2000);

            System.out.println("\n-------------------------");
            System.out.println("\tYour Turn");
            System.out.println("-------------------------");
            
            String playerAction = "";
            System.out.println("Would you like to hit or stand?");
            playerAction = System.console().readLine();
            //input validation
            if (playerAction.equalsIgnoreCase("quit")) {
                break;
            }
            while (!playerAction.equalsIgnoreCase("hit") && !playerAction.equalsIgnoreCase("stand")) {
                System.out.println("Invalid input! Please enter 'hit' or 'stand'.");
                playerAction = System.console().readLine();
            }

            //Player's turn
            while (playerAction.equalsIgnoreCase("hit")) {
                delay(1000);
                playerHand.addCard(shoe.dealCard());
                playerHand.calcPlayerHandValue();
                //playerHandValue = playerHand.getPlayerHandValue();
                System.out.println("Your hand: " + String.join(", ", playerHand.getCards()) + "\nWith a value of: " + playerHand.getHandValue());
                if (playerHand.isBust()) {
                    System.out.println("You busted!");
                    playerBust = true;
                    playerMoney -= playerBet;
                    break;
                }
                if (playerHand.isBlackjack()) {
                    System.out.println("Blackjack! You win!");
                    playerMoney += playerBet*2;
                    playerBlackjack = true;
                    break;
                }
                System.out.println("Would you like to hit or stand?");
                playerAction = System.console().readLine();
                if (playerAction.equalsIgnoreCase("quit")) {
                    break;
                }
                while (!playerAction.equalsIgnoreCase("hit") && !playerAction.equalsIgnoreCase("stand")) {
                    System.out.println("Invalid input! Please enter 'hit' or 'stand'.");
                    playerAction = System.console().readLine();
                }
            }
  
            //Dealer's turn
            if (!playerBust && !playerBlackjack) {
                System.out.println("\n-------------------------");
                System.out.println("\tDealer's Turn");
                System.out.println("-------------------------");
                //it is common practice for the dealer to hit until they have a hand value of 17 or greater
                //so that is simulated here
                while (dealerHand.getHandValue() < 17) {
                    dealerHand.addCard(shoe.dealCard());
                    dealerHand.calcDealerHandValue();
                }
                System.out.println("Dealer's hand: " + String.join(", ", dealerHand.getCards()) + "\nWith a value of: " + dealerHand.getHandValue());
                
                delay(2000);
                
                //call determineWinner to determine who won the round
                determineWinner(playerHand.getHandValue(), dealerHand.getHandValue(), playerBet);
            }

        }
        System.out.println("\nGame over! You ended with $" + playerMoney);
        System.out.println("\nThanks for playing!");
    }
    
    public static void determineWinner(int playerHandValue, int dealerHandValue, int playerBet) {
        if (dealerHandValue > 21) {
            System.out.println("Dealer busted! You win!");
            playerMoney += playerBet*2;
        } else if (dealerHandValue == 21) {
            System.out.println("Dealer has blackjack! You lose!");
            playerMoney -= playerBet;
        } else if (dealerHandValue > playerHandValue) {
            System.out.println("Dealer wins with the greater hand!");
            playerMoney -= playerBet;
        } else if (dealerHandValue < playerHandValue) {
            System.out.println("You win with the greater hand!");
            playerMoney += playerBet*2;
        } else {
            System.out.println("Push! Bet returned!");
        }
    }

    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
