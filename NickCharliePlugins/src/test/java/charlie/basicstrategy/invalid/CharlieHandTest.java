package charlie.basicstrategy.invalid;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.client.Advisor;
import charlie.dealer.Seat;
import charlie.plugin.IAdvisor;
import charlie.util.Play;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests my hand of Charlie vs dealer 5 which should be NONE
 * @author Nick Petrilli
 */
public class CharlieHandTest {
    @Test
        public void test() {
        // Generate an initially empty hand
        Hand myHand = new Hand(new Hid(Seat.YOU));
        
        // Put five cards in hand for Charlie: 3+4+2+8+ACE 
        Card card1 = new Card(3, Card.Suit.HEARTS);
        Card card2 = new Card(4, Card.Suit.CLUBS);
        Card card3 = new Card(2, Card.Suit.SPADES);
        Card card4 = new Card(8, Card.Suit.DIAMONDS);
        Card card5 = new Card(Card.ACE, Card.Suit.HEARTS);
        
        myHand.hit(card1);
        myHand.hit(card2);
        myHand.hit(card3);
        myHand.hit(card4);
        myHand.hit(card5);
        
        // Create dealer up card: 5
        Card upCard = new Card(5, Card.Suit.DIAMONDS);
        
        // Construct advisor and test it.
        IAdvisor advisor = new Advisor();
  
        Play advice = advisor.advise(myHand, upCard);
        // Validate the advice.
        assertEquals(advice, Play.NONE);
    }
}
