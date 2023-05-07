package charlie.client;

import charlie.card.Card;
import charlie.plugin.ICardCounter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import static java.lang.String.format;
import org.apache.log4j.Logger;

/**
 * This class implements the Hi-Lo card counting system (Dubner, 1963)
 * Used for rendering info on the ATable, and correcting the player's bets
 * @author Nick Petrilli
 */
public class HiLoCounter implements ICardCounter {
    Logger LOG = null;
    protected int totalCardsInShoe;
    protected int currentCards = 0;
    protected double decksInShoe = 0;
    protected int runningCount = 0;
    protected double trueCount = 0;
    protected int betAmount = 1;
    protected boolean shufflePending = false;
    
    /**
     * Constructor
     */
    public HiLoCounter() {
        LOG = Logger.getLogger(HiLoCounter.class);
    }
    
    /**
    * Starts a game.<p>
    * Here reset the count if a shuffle is pending so that before the start of
    * a new game the player has an opportunity to change the bet amount.
    * @param shoeSize Size of the shoe in number of cards.
    */
    @Override
    public void startGame(int shoeSize) {
        /* The counts are cleared at the start of the game rather than the end
        of the game to sync with the visual and audio components. The burn card
        disapears off the screen while the shuffle sound is played, and then the
        counts get reset on the table, rather than before burn card disappears.
        */
        if (shufflePending) {
            this.totalCardsInShoe = shoeSize;
            this.currentCards = 0;
            this.runningCount = 0;
            this.trueCount = 0;
            this.betAmount = 1;
            this.shufflePending = false;
        }
        this.totalCardsInShoe = shoeSize;
    }

    /**
    * Ends a game.
    * @param shoeSize Size of the shoe in number of cards.
    */
    @Override
    public void endGame(int shoeSize) {
        /* Since the bet has to be placed before the start of the game, but the
        cards haven't been shuffled yet, only update the bet amount to advise
        the user that they are betting on a new shoe. The counts are reset at 
        the start of the game, when cards are actually shuffled */
        if (shufflePending) {
            this.betAmount = 1;
        }
        this.totalCardsInShoe = shoeSize;
        this.currentCards = 0;
    }

    /**
    * Updates the counter.
    * @param card New card from shoe
    */
    @Override
    public void update(Card card) {
        int value = card.value();
        this.currentCards++;  
        this.updateShoe();
        this.runningCount(value);
        this.betAmount();
    }

    /**
     * Renders card count info.
     * @param g Graphics context referencing the ATable canvas.
     */
    @Override
    public void render(Graphics2D g) {
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.WHITE);
        g.drawString("System: Hi-Lo", 20, 275);
        g.drawString("Shoe size: " + format("%2.1f",decksInShoe), 20, 300);
        
        if (runningCount > 2.0) {
            g.drawString("Running count: ", 20, 325);
            g.setColor(Color.GREEN);
            g.drawString("+" + runningCount, 135, 325);
        }
        else if (runningCount > 0 && runningCount <= 2.0) {
            g.drawString("Running count: ", 20, 325);
            g.setColor(Color.YELLOW);
            g.drawString("+" + runningCount, 135, 325);
        }
        else if (runningCount == 0) {
            g.drawString("Running count: ", 20, 325);
            g.setColor(Color.YELLOW);
            g.drawString("" + runningCount, 135, 325);
        }
        else {
            g.drawString("Running count: ", 20, 325);
            g.setColor(Color.RED);
            g.drawString("" + runningCount, 135, 325);
        }
        
        g.setColor(Color.WHITE);
        
        if (trueCount > 2.0) {
           g.drawString("True count: ", 20, 350);
           g.setColor(Color.GREEN);
           g.drawString("+" + format("%3.1f",trueCount), 105, 350); 
        }
        else if (trueCount > 0 && trueCount <= 2.0){
            g.drawString("True count: ", 20, 350); 
            g.setColor(Color.YELLOW);
            g.drawString("+" + format("%3.1f",trueCount), 105, 350); 
        }
        else if (trueCount == 0) {
            g.drawString("True count: ", 20, 350); 
            g.setColor(Color.YELLOW);
            g.drawString(format("%3.1f",trueCount), 105, 350); 
        }
        else {
            g.drawString("True count: ", 20, 350); 
            g.setColor(Color.RED);
            g.drawString(format("%3.1f",trueCount), 105, 350); 
        }
        
        g.setColor(Color.WHITE);
        g.drawString("Bet chips: " + betAmount, 20, 375);
        
    }

    /**
    * Indicates the burn card has arrived.
    * but before the start of the next game.
    */
    @Override
    public void shufflePending() {
        this.shufflePending = true;
    }
    
    /**
     * Takes the most recent card value and calculates a running count using Hi-Lo
     * @param value int
     */
    public void runningCount(int value) {
        if(value >= 2 && value <= 6){
            runningCount++;
        }
        else if(value >= 7 && value <= 9){
            //Do Nothing
        }
        else if(value >= 10 || value == Card.ACE){
            runningCount--;
        }
        this.trueCount();
    }
    
    /**
     * Takes the current running count and gets a true count by dividing it by
     * the current number of decks in the shoe
     */
    public void trueCount() {
        trueCount = runningCount / decksInShoe;
    }
    
    
    /**
     * Calculates the bet amount by rounding the true count +1 to a whole number
     * and comparing it to the minimum bet(in chips)
     */
    public void betAmount() {
        double nonRoundedBet = Math.max(1, (1 + trueCount));
        betAmount = (int) nonRoundedBet;
    }
    
    /**
     * Calculates the current number of decks in the shoe
     */
    public void updateShoe() {
        double cardsLeft = totalCardsInShoe - currentCards;
        decksInShoe = cardsLeft / 52.0;
    }
    
    /**
     * Gets the bet amount for the GameFrame to use
     * @return bet amount in chips
     */
    @Override
    public int getBetAmt() {
        return betAmount;
    }
}
