package charlie.server.bot;


import charlie.card.Card;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a functional bot that uses the Basic Strategy to
 * make the correct play each turn (without splitting)
 * @author Nick Petrilli
 */
public class Huey extends HueyStay{

    protected Card upCard;
    protected BotBasicStrategy botBasicStrategy = new BotBasicStrategy();

        /**
     * Deals a card to player.
     * All players receive all cards which is useful for card counting.
     * @param hid Hand id which might not necessarily belong to player.
     * @param card Card being dealt
     * @param values Hand values, literal and soft
     */
    @Override
    public void deal(Hid hid, Card card, int[] values) {
        if (hid.getSeat() == Seat.DEALER) {
            this.upCard = card;
        }
        if (hid.getSeat() == mine) {
            this.play(hid);
        }
    }
    
    /**
     * Huey waits a few seconds to mimic playing like a human, and then makes a
     * play by consulting the Bot Basic Strategy
     */
    @Override
    public void run() {
        try {
            int thinking = ran.nextInt(MAX_THINKING * 1000);
            Thread.sleep(thinking);
            Play play = botBasicStrategy.getPlay(myHand, upCard);
            switch (play) {
                case STAY:
                    dealer.stay(this, myHand.getHid());
                    break;
                case HIT:
                    dealer.hit(this, myHand.getHid());
                    break;
                case DOUBLE_DOWN:
                    dealer.doubleDown(this, myHand.getHid());
                    break;
                default:
                    dealer.stay(this, myHand.getHid());
                    break;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(HueyStay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
