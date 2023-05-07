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
 * Tests my hand vs dealer upCard with a negative value which should be NONE
 * @author Nick Petrilli
 */
public class NegativeValueUpCardTest {
    @Test
        public void test() {
        // Generate an initially empty hand
        Hand myHand = new Hand(new Hid(Seat.YOU));
        
        // Put two cards in hand: 3 + 4
        Card card1 = new Card(3, Card.Suit.HEARTS);
        Card card2 = new Card(4, Card.Suit.CLUBS);
        
        myHand.hit(card1);
        myHand.hit(card2);
        
        // Create dealer up card: -5
        Card upCard = new Card(-5, Card.Suit.DIAMONDS);
        
        // Construct advisor and test it.
        IAdvisor advisor = new Advisor();
  
        Play advice = advisor.advise(myHand, upCard);
        // Validate the advice.
        assertEquals(advice, Play.NONE);
    }
}
