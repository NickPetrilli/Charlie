package charlie.server.bot;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.client.BasicStrategy;
import charlie.util.Play;

/**
 * This class is an extension of the BasicStrategy, specifically for bots
 * because they cannot split
 * @author Nick Petrilli
 */
public class BotBasicStrategy extends BasicStrategy {
    
    /**
     * Gets the play for player's hand vs. dealer up-card.
     * @param hand Hand player hand
     * @param upCard Dealer up-card
     * @return Play based on basic strategy
     */
    @Override
    public Play getPlay(Hand hand, Card upCard) {
        Play play = super.getPlay(hand, upCard);
        //Splitting is not supported for Bots
        if (play == Play.SPLIT) {
            if (hand.getValue() >= 5 && hand.getValue() < 12) {
                play = super.doSection2(hand, upCard);
            }
            else if (hand.getValue() >= 12 && hand.getValue() <= 21) {
                play = super.doSection1(hand, upCard);
            }
            //Section 2 doesn't include hand value of 4, so just change to hit
            //since 5-8 is all hit
            else if (hand.getValue() == 4) {
                play = Play.HIT;
            } 
        }

        return play;
    }
}
