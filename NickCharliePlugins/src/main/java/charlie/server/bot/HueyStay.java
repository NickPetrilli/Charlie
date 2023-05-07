
package charlie.server.bot;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.plugin.IBot;
import charlie.util.Constant;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the Huey Bot, which stays on every hand
 * @author Nick Petrilli
 */
public class HueyStay implements IBot, Runnable {

    protected final int MAX_THINKING = 5; 
    protected Seat mine;
    protected Hand myHand;
    protected Dealer dealer;
    protected Random ran = new Random();
    
    /**
     * Gets Huey's hand
     * @return myHand
     */
    @Override
    public Hand getHand() {
        return myHand;
    }

    /**
     * Sets the dealer 
     * @param dealer 
     */
    @Override
    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    /**
     * Assigns Huey to a seat at the table and constructs a Hand to play
     * @param seat 
     */
    @Override
    public void sit(Seat seat) {
        this.mine = seat;
        Hid hid = new Hid(seat,Constant.BOT_MIN_BET,0.0);
        this.myHand = new Hand(hid);
    }

    /**
     * 
     * @param hids
     * @param shoeSize 
     */
    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
      
    }

    /**
     * 
     * @param shoeSize 
     */
    @Override
    public void endGame(int shoeSize) {
        
    }

    /**
     * 
     * @param hid
     * @param card
     * @param values 
     */
    @Override
    public void deal(Hid hid, Card card, int[] values) {
        
    }

    /**
     * 
     */
    @Override
    public void insure() {
        
    }

    /**
     * 
     * @param hid 
     */
    @Override
    public void bust(Hid hid) {
       
    }

    /**
     * 
     * @param hid 
     */
    @Override
    public void win(Hid hid) {
        
    }

    /**
     * 
     * @param hid 
     */
    @Override
    public void blackjack(Hid hid) {
         
    }

    /**
     * 
     * @param hid 
     */
    @Override
    public void charlie(Hid hid) {
        
    }

    /**
     * 
     * @param hid 
     */
    @Override
    public void lose(Hid hid) {
        
    }

    /**
     * 
     * @param hid 
     */
    @Override
    public void push(Hid hid) {
        
    }

    /**
     * 
     */
    @Override
    public void shuffling() {
        
    }

    /**
     * Spawn a worker thread to make a play for Huey
     * @param hid 
     */
    @Override
    public void play(Hid hid) {
        if (hid.getSeat() != mine) {
            return;
        }
        new Thread(this).start();
    }

    /**
     * 
     * @param newHid
     * @param origHid 
     */
    @Override
    public void split(Hid newHid, Hid origHid) {
        
    }

    /**
     * Huey waits a few seconds to mimic playing like a human, and then makes a
     * play, which is always stay 
     */
    @Override
    public void run() {
        try {
            int thinking = ran.nextInt(MAX_THINKING * 1000);
            Thread.sleep(thinking);
            dealer.stay(this, myHand.getHid());
        } catch (InterruptedException ex) {
            Logger.getLogger(HueyStay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
