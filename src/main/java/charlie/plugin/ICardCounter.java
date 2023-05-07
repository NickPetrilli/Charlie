package charlie.plugin;

import charlie.card.Card;
import java.awt.Graphics2D;

/**
 * Client side interface to the ATable for counting cards
 */
public interface ICardCounter {
    /**
    * Starts of a new game.
    * @param shoeSize Size of the shoe in number of cards.
    */
    public void startGame(int shoeSize);
	
    /**
    * Ends a game.<p>
    * Here reset the count if a shuffle is pending so that before the start of
    * a new game the player has an opportunity to change the bet amount.
    * @param shoeSize Size of the shoe in number of cards.
    */
    public void endGame(int shoeSize);

    /**
    * Updates the counter.
    * @param card New card from shoe, might be null if dealer's hole card.
    */
    public void update(Card card);

    /**
     * Renders card count info.
     * @param g Graphics context referencing the ATable canvas.
     */
    public void render(Graphics2D g);

    /**
    * Indicates the burn card has arrived.
    * but before the start of the next game.
    */
    public void shufflePending();
    
    /**
     * Gets the bet amount 
     * @return bet amount in chips
     */
    public int getBetAmt();
}