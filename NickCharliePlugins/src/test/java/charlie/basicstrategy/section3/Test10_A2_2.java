package charlie.basicstrategy.section3;

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
 * Tests my ACE, 2 vs dealer 2 which should be HIT
 * @author Nick Petrilli
 */
public class Test10_A2_2 {
    @Test
        public void test() {
        // Generate an initially empty hand
        Hand myHand = new Hand(new Hid(Seat.YOU));
        
        // Put two cards in hand: ACE+2
        Card card1 = new Card(Card.ACE, Card.Suit.CLUBS);
        Card card2 = new Card(2, Card.Suit.DIAMONDS);
        
        myHand.hit(card1);
        myHand.hit(card2);
        
        // Create dealer up card: 2
        Card upCard = new Card(2, Card.Suit.HEARTS);
        
        // Construct advisor and test it.
        IAdvisor advisor = new Advisor();
  
        Play advice = advisor.advise(myHand, upCard);
        // Validate the advice.
        assertEquals(advice, Play.HIT);
    }
}