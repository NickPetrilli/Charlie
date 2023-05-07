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
 * Tests my null hand vs dealer 5 which should be NONE
 * @author Nick Petrilli
 */
public class NullHandTest {
    @Test
        public void test() {
        // Generate an initially empty hand
        Hand myHand = new Hand(new Hid(Seat.YOU));
        
        // Put two cards in hand: null
        // Null rank causes null pointer exception
        Card card1 = new Card(0, null);
        Card card2 = new Card(0, null);
        
        myHand.hit(card1);
        myHand.hit(card2);
        
        // Create dealer up card: 5
        Card upCard = new Card(5, Card.Suit.DIAMONDS);
        
        // Construct advisor and test it.
        IAdvisor advisor = new Advisor();
  
        Play advice = advisor.advise(myHand, upCard);
        // Validate the advice.
        assertEquals(advice, Play.NONE);
    }
}
