package charlie.basicstrategy.section2;

import charlie.client.Advisor;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.plugin.IAdvisor;
import charlie.util.Play;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests my 10 vs dealer 9 which should be DOUBLE DOWN
 * @author Nick Petrilli
 */
public class Test08_10_9 {
   @Test
        public void test() {
        // Generate an initially empty hand
        Hand myHand = new Hand(new Hid(Seat.YOU));
        
        // Put two cards in hand: 3+7
        Card card1 = new Card(3, Card.Suit.CLUBS);
        Card card2 = new Card(7, Card.Suit.DIAMONDS);
        
        myHand.hit(card1);
        myHand.hit(card2);
        
        // Create dealer up card: 9
        Card upCard = new Card(9, Card.Suit.HEARTS);
        
        // Construct advisor and test it.
        IAdvisor advisor = new Advisor();
  
        Play advice = advisor.advise(myHand, upCard);
        // Validate the advice.
        assertEquals(advice, Play.DOUBLE_DOWN);
    } 
}
