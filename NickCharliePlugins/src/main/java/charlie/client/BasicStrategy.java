package charlie.client;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;
import org.apache.log4j.Logger;

/**
 * This class is an implementation of the Basic Strategy.
 * <p>It is table-driven, and implements the 360 rules to correctly play Blackjack
 * @author Nick Petrilli
 */
public class BasicStrategy {
    // These help make table formatting compact to look like the pocket card.
    public final static Play P = Play.SPLIT;
    public final static Play H = Play.HIT;
    public final static Play S = Play.STAY;
    public final static Play D = Play.DOUBLE_DOWN;
    Logger LOG = null;
    
    /** Rules for section 1; see Instructional Services (2000) pocket card */
    Play[][] section1Rules = {
        /*         2  3  4  5  6  7  8  9  T  A  */
        /* 21 */ { S, S, S, S, S, S, S, S, S, S },
        /* 20 */ { S, S, S, S, S, S, S, S, S, S },
        /* 19 */ { S, S, S, S, S, S, S, S, S, S },
        /* 18 */ { S, S, S, S, S, S, S, S, S, S },
        /* 17 */ { S, S, S, S, S, S, S, S, S, S },
        /* 16 */ { S, S, S, S, S, H, H, H, H, H },
        /* 15 */ { S, S, S, S, S, H, H, H, H, H },
        /* 14 */ { S, S, S, S, S, H, H, H, H, H },
        /* 13 */ { S, S, S, S, S, H, H, H, H, H },
        /* 12 */ { H, H, S, S, S, H, H, H, H, H }
    };
    
    /** Rules for section 2; see Instructional Services (2000) pocket card */
    Play[][] section2Rules = {
        /*         2  3  4  5  6  7  8  9  T  A  */
        /* 11 */ { D, D, D, D, D, D, D, D, D, H },
        /* 10 */ { D, D, D, D, D, D, D, D, H, H },
        /*  9 */ { H, D, D, D, D, H, H, H, H, H },
        /*  8 */ { H, H, H, H, H, H, H, H, H, H },
        /*  7 */ { H, H, H, H, H, H, H, H, H, H },
        /*  6 */ { H, H, H, H, H, H, H, H, H, H },
        /*  5 */ { H, H, H, H, H, H, H, H, H, H }
    };
    
    /** Rules for section 3; see Instructional Services (2000) pocket card */
    Play[][] section3Rules = {
        /*           2  3  4  5  6  7  8  9  T  A  */
        /* A,10 */ { S, S, S, S, S, S, S, S, S, S },
        /* A, 9 */ { S, S, S, S, S, S, S, S, S, S },
        /* A, 8 */ { S, S, S, S, S, S, S, S, S, S },
        /* A, 7 */ { S, D, D, D, D, S, S, H, H, H },
        /* A, 6 */ { H, D, D, D, D, H, H, H, H, H },
        /* A, 5 */ { H, H, D, D, D, H, H, H, H, H },
        /* A, 4 */ { H, H, D, D, D, H, H, H, H, H },
        /* A, 3 */ { H, H, H, D, D, H, H, H, H, H },
        /* A, 2 */ { H, H, H, D, D, H, H, H, H, H }
    };
    
    /** Rules for section 4; see Instructional Services (2000) pocket card */
    //Modified the order of section 4 to descending order for easier access
    Play[][] section4Rules = {
        /*            2  3  4  5  6  7  8  9  T  A  */
        /* 10,10 */ { S, S, S, S, S, S, S, S, S, S },
        /*  9, 9 */ { P, P, P, P, P, S, P, P, S, S },
        /*  8, 8 */ { P, P, P, P, P, P, P, P, P, P },
        /*  7, 7 */ { P, P, P, P, P, P, H, H, H, H },
        /*  6, 6 */ { P, P, P, P, P, H, H, H, H, H },
        /*  5, 5 */ { D, D, D, D, D, D, D, D, H, H },
        /*  4, 4 */ { H, H, H, P, P, H, H, H, H, H },
        /*  3, 3 */ { P, P, P, P, P, P, H, H, H, H },
        /*  2, 2 */ { P, P, P, P, P, P, H, H, H, H },
        /*  A, A */ { P, P, P, P, P, P, P, P, P, P }
    };
    
    /**
     * Constructor
     */
    public BasicStrategy() {
        LOG = Logger.getLogger(BasicStrategy.class);
    }
    
    /**
     * Gets the play for player's hand vs. dealer up-card.
     * @param hand Hand player hand
     * @param upCard Dealer up-card
     * @return Play based on basic strategy
     */
    public Play getPlay(Hand hand, Card upCard) {
        //12 validation checks before getting play
        if (isNullHand(hand) 
            || isEmptyHand(hand) 
            || isOneCardHand(hand) 
            || isHandGreaterThan21(hand) 
            || isHandLessThan0(hand) 
            || isHandCharlie(hand) 
            || isHandBlackjack(hand)
            || isHandValue21(hand) 
            || isHandInvalid(hand) 
            || isUpCardNull(upCard) 
            || isUpCardLessThan0(upCard) 
            || isUpCardGreaterThan10(upCard)) {
            return Play.NONE;
        }
        Card card1 = hand.getCard(0);
        Card card2 = hand.getCard(1);

        if(hand.isPair()) {
            return doSection4(hand,upCard);
        }
        else if(hand.size() == 2 && (card1.getRank() == Card.ACE || card2.getRank() == Card.ACE)) {
            return doSection3(hand,upCard);
        }
        else if(hand.getValue() >=5 && hand.getValue() < 12) {
            return doSection2(hand,upCard);
        }
        else if(hand.getValue() >= 12)
            return doSection1(hand,upCard);
        
        return Play.NONE;
    }
    
    /**
     * Does section 1 processing of the basic strategy, 12-21 (player) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     * @return Play based on Basic Strategy
     */
    protected Play doSection1(Hand hand, Card upCard) {
        int value = hand.getValue();
        
        // Section 1 only supports hands >= 12 (see above).
        if(value < 12)
            return Play.NONE;
        
        ////// Get the row in the table.
        
        // Subtract 21 since the player's hand starts at 21 and we're working
        // our way down through section 1 from index 0.
        int rowIndex = 21 - value;
        
        Play[] row = section1Rules[rowIndex];
        
        ////// Get the column in the table
        
        // Subtract 2 since the dealer's up-card starts at 2
        int colIndex = upCard.getRank() - 2;
         
        if(upCard.isFace())
            colIndex = 10 - 2;

        // Ace is the 10th card (index 9)
        else if(upCard.isAce())
            colIndex = 9;
        
        
        // At this row, col we have the correct play defined.
        Play play = row[colIndex];
        
        return play;
    }
    
    /**
     * Does section 2 processing of the basic strategy, 5-11 (player) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     * @return Play based on Basic Strategy
     */
    protected Play doSection2(Hand hand, Card upCard) {
        int value = hand.getValue();
        
        // Section 2 only supports hands >= 5 (see above).
        if(value < 5)
            return Play.NONE;
        
        ////// Get the row in the table.
        
        // Subtract 11 since the player's hand starts at 11 and we're working
        // our way down through section 2 from index 0.
        int rowIndex = 11 - value;
        
        Play[] row = section2Rules[rowIndex];
        
        ////// Get the column in the table
        
        // Subtract 2 since the dealer's up-card starts at 2
        int colIndex = upCard.getRank() - 2;
         
        if(upCard.isFace())
            colIndex = 10 - 2;

        // Ace is the 10th card (index 9)
        else if(upCard.isAce())
            colIndex = 9;
        
        
        // At this row, col we have the correct play defined.
        Play play = row[colIndex];
        
        //Cannot double down after already hitting the hand
        if (hand.size() > 2 && play == Play.DOUBLE_DOWN) {
            play = Play.HIT;
        }
        
        return play;
    }
    
    /**
     * Does section 3 processing of the basic strategy, A,2-A,10 (player) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     * @return Play based on Basic Strategy
     */
    protected Play doSection3(Hand hand, Card upCard) {
        int value = hand.getValue();
        
        ////// Get the row in the table.
        
        // Subtract 21 since the player's hand starts at 21 (A,10 = 21) and we're working
        // our way down through section 1 from index 0.
        int rowIndex = 21 - value;
        
        Play[] row = section3Rules[rowIndex];
        
        ////// Get the column in the table
        
        // Subtract 2 since the dealer's up-card starts at 2
        int colIndex = upCard.getRank() - 2;
         
        if(upCard.isFace())
            colIndex = 10 - 2;

        // Ace is the 10th card (index 9)
        else if(upCard.isAce())
            colIndex = 9;
        
        
        // At this row, col we have the correct play defined.
        Play play = row[colIndex];
        
        return play;
    }
    
    /**
     * Does section 4 processing of the basic strategy, any pairs of cards (player) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     * @return Play based on Basic Strategy
     */
    protected Play doSection4(Hand hand, Card upCard) {
        int value = hand.getValue();
        
        ////// Get the row in the table.
        
        // Subtract 20 since the player's hand starts at 20 (10,10 = 20) and we're working
        // our way down through section 4 from index 0 in descending order
        int rowIndex = (20 - value) / 2;
        //Changed the order of Section 4, now A,A is the last spot because it
        //doesn't follow the pattern above
        Card card = hand.getCard(0);
        if (card.equals(Card.ACE)) {
            rowIndex = 9;
        }
        
        Play[] row = section4Rules[rowIndex];
        
        ////// Get the column in the table
        
        // Subtract 2 since the dealer's up-card starts at 2
        int colIndex = upCard.getRank() - 2;
         
        if(upCard.isFace())
            colIndex = 10 - 2;

        // Ace is the 10th card (index 9)
        else if(upCard.isAce())
            colIndex = 9;
        
        
        // At this row, col we have the correct play defined.
        Play play = row[colIndex];
        
        return play;
    }
    
    /**
     * Checks if the hand is null
     * @param hand Player hand
     * @return true if hand is null, false otherwise
     */
    public boolean isNullHand(Hand hand) {
        if (hand == null) {
            LOG.info("BasicStrategy ERROR: hand is null");
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the hand is empty
     * @param hand Player hand
     * @return true if hand is empty, false otherwise
     */
    public boolean isEmptyHand(Hand hand) {
        if (hand.size() == 0) {
            LOG.info("BasicStrategy ERROR: hand has no cards");
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the hand only has one card
     * @param hand Player hand
     * @return true if hand has one card, false otherwise
     */
    public boolean isOneCardHand(Hand hand) {
        if (hand.size() == 1) {
            LOG.info("BasicStrategy ERROR: hand only has 1 card");
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the hand's value is greater than 21
     * @param hand Player hand
     * @return true if hand value is greater than 21, false otherwise
     */
    public boolean isHandGreaterThan21(Hand hand) {
        if (hand.getValue() > 21) {
            LOG.info("BasicStrategy ERROR: hand value > 21");
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the hand's value is less than 0
     * @param hand Player hand
     * @return true if hand value less than 0, false otherwise
     */
    public boolean isHandLessThan0(Hand hand) {
        if (hand.getValue() < 0) {
            LOG.info("BasicStrategy ERROR: hand value < 0");
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the hand is Charlie
     * @param hand Player hand
     * @return true if hand is Charlie, false otherwise
     */
    public boolean isHandCharlie(Hand hand) {
        if (hand.isCharlie()) {
            LOG.info("BasicStrategy ERROR: hand is Charlie");
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the hand is Blackjack
     * @param hand Player hand
     * @return true if hand is Blackjack, false otherwise
     */
    public boolean isHandBlackjack(Hand hand) {
        if (hand.isBlackjack()) {
            LOG.info("BasicStrategy ERROR: hand is Blackjack");
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the hand's value is equal to 21
     * @param hand Player hand
     * @return true if hand value is equal to 21, false otherwise
     */
    public boolean isHandValue21(Hand hand) {
        if (hand.getValue() == 21) {
            LOG.info("BasicStrategy ERROR: hand value == 21");
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the hand has invalid cards
     * @param hand Player hand
     * @return true if hand has invalid cards, false otherwise
     */
    public boolean isHandInvalid(Hand hand) {
        //Card is invalid if the suit is null bc the rank cannot be
        if (hand.getCard(0) == null || hand.getCard(1) == null || 
            hand.getCard(0).getSuit() == null || hand.getCard(1).getSuit() == null) {
            LOG.info("BasicStrategy ERROR: hand has invalid cards");
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the dealer's up-card is null
     * @param upCard Dealer up-card
     * @return true if up-card is null, false otherwise 
     */
    public boolean isUpCardNull(Card upCard) {
        if (upCard == null || upCard.getSuit() == null) {
            LOG.info("BasicStrategy ERROR: upCard is null");
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the dealer's up-card has value less than 0
     * @param upCard Dealer up-card
     * @return true if up-card has value less than 0, false otherwise 
     */
    public boolean isUpCardLessThan0(Card upCard) {
        if (upCard.value() < 0) {
            LOG.info("BasicStrategy ERROR: upCard has value < 0");
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the dealer's up-card has value greater than 10
     * @param upCard Dealer up-card
     * @return true if up-card has value greater than 10, false otherwise 
     */
    public boolean isUpCardGreaterThan10(Card upCard) {
        //Math.max() only allows 2 parameters, so split up into two statements
        if (upCard.value() > Math.max(Card.ACE, Card.JACK) &&
            upCard.value() > Math.max(Card.KING, Card.QUEEN)) {
            LOG.info("BasicStrategy ERROR: upCard has value > max(KING,QUEEN, JACK, ACE)");
            return true;
        }
        return false;
    }
}
