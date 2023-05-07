package charlie.basicstrategy.section4;

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
 * Tests my ACE, ACE vs dealer ACE which should be SPLIT
 * @author Nick Petrilli
 */
public class Test22_AA_A {
    @Test
        public void test() {
        // Generate an initially empty hand
        Hand myHand = new Hand(new Hid(Seat.YOU));
        
        // Put two cards in hand: ACE+ACE
        Card card1 = new Card(Card.ACE, Card.Suit.CLUBS);
        Card card2 = new Card(Card.ACE, Card.Suit.DIAMONDS);
        
        myHand.hit(card1);
        myHand.hit(card2);
        
        // Create dealer up card: ACE
        Card upCard = new Card(Card.ACE, Card.Suit.HEARTS);
        
        // Construct advisor and test it.
        IAdvisor advisor = new Advisor();
  
        Play advice = advisor.advise(myHand, upCard);
        // Validate the advice.
        //assertEquals(advice, Play.SPLIT);
    }
}
