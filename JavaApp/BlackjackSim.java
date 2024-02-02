import java.util.Scanner;

public class BlackjackSim {

    //static variables
    static int playerMoney = 1000;
    static Shoe shoe = new Shoe(4);
    static Hand playerHand = new Hand();
    static Hand dealerHand = new Hand();

    static String Player = "Player";
    static String Dealer = "Dealer";

    public static void main(String[] args) {
        int playerBet = 0;
        shoe.shuffle();

        System.out.println("\nWelcome to Blackjack! Type 'quit' to end the game or go bankrupt!");
        System.out.println("You have $" + playerMoney + ". How much would you like to bet?");

        playerBet = getBet();

        while (playerMoney > 0){
            String roundResult = gameLoop(playerBet);
            if (roundResult.equals("quit")) {
                break;
            }
            if (playerMoney <= 0) {
                System.out.println("\nYou're out of money! Better luck next time!");
                break;
            }
            System.out.println("\nYou have $" + playerMoney + ". How much would you like to bet?");
            playerBet = 0;
            playerBet = getBet();
            
        }
        
        System.out.println("\nGame ended! You ended with $" + playerMoney);
        System.out.println("\nThanks for playing!");

    }
    //function handles the initial deal of the game
    public static void initialDeal() {
        playerHand.addCard(shoe.dealCard());
        playerHand.addCard(shoe.dealCard());
        dealerHand.addCard(shoe.dealCard());   
    }

    //function deals with basic game loop of blackjack, calls playerTurn and dealerTurn
    public static String gameLoop(int playerBet){
        //ensure hands are empty before start of round
        if (!playerHand.getCards().isEmpty() || !dealerHand.getCards().isEmpty()) {
            playerHand.clearHand();
            dealerHand.clearHand();
        }

        System.out.println("\n-------------------------\n\tRound Start\n-------------------------");

        //deal initial cards and calculate hand values
        initialDeal();
        playerHand.calcPlayerHandValue();
        dealerHand.calcDealerHandValue();

        playerHand.printHand(Player);
        delay(1000);
        dealerHand.printHand(Dealer);
        delay(1000);

        //check for natural blackjack
        if (playerHand.isBlackjack()) {
            System.out.println("\nNatural Blackjack! You win!");
            return "blackjack";
        }

        //player turn
        String playerResult = playerTurn(playerBet);
        if (playerResult.equals("quit")) {
            return "quit";
        } else if (playerResult.equals("bust")) {
            System.out.println("\nYou busted! You lose!");
            playerMoney -= playerBet;
            return "bust";
        } else if (playerResult.equals("blackjack")) {
            System.out.println("\nBlackjack! You win!");
            playerMoney += playerBet*2;
            return "blackjack";
        }

        dealerTurn();

        //determine game end scenario
        determineGameEndScenario(playerHand.getHandValue(), dealerHand.getHandValue(), playerBet);

        System.out.println("\n-------------------------\n\tRound End\n-------------------------");

        return "roundEnd";
    }

    public static String playerTurn(int playerBet){
        String playerAction = "";
    
        System.out.println("\n-------------------------\n\tPlayer's Turn\n-------------------------");
    
        Scanner scanner = new Scanner(System.in);
    
        while (!playerAction.equalsIgnoreCase("stand")) {
            System.out.println("\nWhat would you like to do? (hit/stand)");
            playerAction = scanner.nextLine();
    
            //input validation
            if (playerAction.equalsIgnoreCase("quit")) {
                return "quit";
            }
            while (!playerAction.equalsIgnoreCase("hit") && !playerAction.equalsIgnoreCase("stand")) {
                System.out.println("Invalid input! Please enter 'hit' or 'stand'.");
                playerAction = scanner.nextLine();
            }
    
            if (playerAction.equalsIgnoreCase("hit")) {
                playerHand.addCard(shoe.dealCard());
                playerHand.calcPlayerHandValue();
    
                //print hand
                playerHand.printHand(Player);
                delay(1000);
    
                //check for bust
                if (playerHand.getHandValue() > 21) {
                    return "bust";
                }
                //check for blackjack
                if (playerHand.isBlackjack()) {   
                    return "blackjack";
                }
            }
        }
    
        return "endPlayerTurn";
    }
    
    public static void dealerTurn(){
        System.out.println("\n-------------------------\n\tDealer's Turn\n-------------------------");
    
        //print dealer's hand
        dealerHand.printHand(Dealer);
        delay(1000);
    
        //dealer hits until hand value is 17 or greater
        while (dealerHand.getHandValue() < 17) {
            dealerHand.addCard(shoe.dealCard());
            dealerHand.calcDealerHandValue();  
            delay(500);
            System.out.println("\nDealer hits!");
        }

        //print dealer's hand
        dealerHand.printHand(Dealer);
        delay(1000);
        return;
    }
 
    public static void determineGameEndScenario(int playerHandValue, int dealerHandValue, int playerBet) {
        if (dealerHandValue > 21) {
            System.out.println("\nDealer busted! You win!");
            playerMoney += playerBet*2;
        } else if (dealerHandValue == 21) {
            System.out.println("\nDealer has blackjack! You lose!");
            playerMoney -= playerBet;
        } else if (dealerHandValue > playerHandValue) {
            System.out.println("\nDealer wins with the greater hand!");
            playerMoney -= playerBet;
        } else if (dealerHandValue < playerHandValue) {
            System.out.println("\nYou win with the greater hand!");
            playerMoney += playerBet*2;
        } else {
            System.out.println("\nPush! Bet returned!");
        }
    }

    public static int getBet(){
        int bet = 0;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();   
            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Thanks for playing!");
                System.exit(0);
            }
            try {
                bet = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }
            if (bet > playerMoney) {
                System.out.println("You don't have that much money!");
                continue;
            }
            if (bet <= 0) {
                System.out.println("You must bet at least $1!");
                continue;
            }
            break;
        }
        return bet;
    }

    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
