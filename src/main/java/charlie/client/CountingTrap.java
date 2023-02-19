
package charlie.client;


import charlie.message.Message;
import charlie.message.view.to.Deal;
import charlie.message.view.to.GameStart;
import charlie.message.view.to.Shuffle;
import charlie.card.HoleCard;
import charlie.card.Card;
import charlie.plugin.ITrap;
import static java.lang.String.format;
import org.apache.log4j.Logger;

/**
 *
 * @author Nick Petrilli and ShivaPriya Poolapolly
 */
public class CountingTrap implements ITrap {

    Logger LOG = null;
    
    int totalCardsInShoe;
    int currentCards = 0;
    float currentShoe;
    int runningCount = 0;
    String rCount;
    float trueCount = 0;
    int betAmount = 1;
    Card holecard=null;
    boolean flag = false;
    
    
    @Override
    public void onSend(Message msg) {
        
    }

    /**
     * Receives Game Messages to calculate a bet amount for players using the Hi-Lo method
     * @param msg Message
     */
    @Override
    public void onReceive(Message msg) {
        LOG = Logger.getLogger(CountingTrap.class);
        
        if(msg instanceof Shuffle){
            flag=true;
        }
        if(msg instanceof GameStart){
            if(flag){
                //shuffle deck
                //reset flag, current cards, running count, true count, and bet amount
                totalCardsInShoe= ((GameStart) msg).shoeSize();
                currentCards=0;
                runningCount=0;
                trueCount=0;
                betAmount=1;
                flag=false;
            }
            totalCardsInShoe= ((GameStart) msg).shoeSize();
            currentCards=0;
            
        }
        if(msg instanceof Deal){
            int value;
            var drawnCard = ((Deal) msg).getCard();
            if(drawnCard instanceof HoleCard){
                holecard=drawnCard;
                currentCards++;               
            }
            else{
                
                if(drawnCard==null){
                    value=holecard.value();
                } 
                else {
                    value = drawnCard.value();
                    currentCards++;
                    
                }                           
            updateShoe();
            runningCount(value);

            if(runningCount<0){
                rCount=format("%d  ",runningCount);
            }
            else{
                rCount=String.valueOf(runningCount);
            }
            LOG.info("shoe: "+format("%2.1f",currentShoe)+" running count: "+rCount+" true count: "+format("%3.1f",trueCount)+" bet amount: "+betAmount);
            }
          
        }
    }
    /**
     * Takes the most recent card value and calculates a running count using Hi-Lo
     * @param value int
     * @return runningCount
     */
    public float runningCount(int value){
        if(value>=2 && value<=6){
            runningCount++;
        }
        else if(value>=7 && value<=9){
            //Do Nothing
        }
        else if(value>=10 || value==1){
            runningCount--;
        }
        return trueCount();
    }
    /**
     * Takes the current running count and gets a true count by dividing it by the current number
     * of decks in the shoe
     * 
     * @return trueCount
     */
    public float trueCount(){
        trueCount=runningCount/currentShoe;
        return betAmount();
    }
    /**
     * Calculates the bet amount by rounding the true count +1 to a whole number
     * and comparing it to the minimum bet(in chips)
     * 
     * @return betAmount
     */
    public int betAmount(){
        //BigDecimal roundedCount=new BigDecimal(trueCount+1);
        //roundedCount = roundedCount.setScale(0, RoundingMode.HALF_UP);
        //int roundedBet=roundedCount.intValue();
        //betAmount=(int) Math.max(1, roundedBet);
        float nonRoundedBet = Math.max(1, (1 + trueCount));
        betAmount = (int) (nonRoundedBet + 0.5);
        return betAmount;
    }
    /**
     * Calculates the current number of decks in the shoe
     * 
     * 
     */
    public void updateShoe(){
        float cardsLeft=(float)totalCardsInShoe-currentCards;
        currentShoe=cardsLeft/52;
    }
    
    
}
