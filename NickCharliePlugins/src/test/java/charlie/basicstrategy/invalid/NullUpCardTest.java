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
 * Tests my 13 vs dealer null upCard which should be NONE
 * @author Nick Petrilli
 */
public class NullUpCardTest {
    @Test
        public void test() {
        // Generate an initially empty hand
        Hand myHand = new Hand(new Hid(Seat.YOU));
        
        // Put two cards in hand: 5+8
        Card card1 = new Card(5, Card.Suit.CLUBS);
        Card card2 = new Card(8, Card.Suit.DIAMONDS);
        
        myHand.hit(card1);
        myHand.hit(card2);
        
        // Create dealer up card: null
        /* Null rank causes null pointer exception, and value 0 will throw 
        an index out of bounds error, therefore its 2 but still an invalid card */  
        Card upCard = new Card(2, null);
        
        // Construct advisor and test it.
        IAdvisor advisor = new Advisor();
  
        Play advice = advisor.advise(myHand, upCard);
        // Validate the advice.
        assertEquals(advice, Play.NONE);
    }
}
