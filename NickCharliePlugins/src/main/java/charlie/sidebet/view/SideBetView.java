/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package charlie.sidebet.view;

import charlie.audio.Effect;
import charlie.audio.SoundFactory;
import charlie.card.Hid;
import charlie.plugin.ISideBetView;
import charlie.util.Constant;
import charlie.view.AMoneyManager;
import charlie.view.sprite.AtStakeSprite;
import charlie.view.sprite.Chip;

import charlie.view.sprite.ChipButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

/**
 * This class implements the side bet view
 * @author Ron.Coleman
 */
public class SideBetView implements ISideBetView {
    private final Logger LOG = Logger.getLogger(SideBetView.class);

    public final static int X = 400;
    public final static int Y = 200;
    public final static int DIAMETER = 50;
    
    // Corresponding chips equal to the stake
    public final static int PLACE_HOME_X = X+(DIAMETER/2)+10;
    public final static int PLACE_HOME_Y = Y-(DIAMETER/3);

    protected Font font = new Font("Arial", Font.BOLD, 16);
    protected BasicStroke stroke = new BasicStroke(3);

    // See http://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
    protected float dash1[] = {10.0f};
    protected BasicStroke dashed
            = new BasicStroke(3.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);

    protected List<ChipButton> buttons;
    protected int amt = 0;
    protected AMoneyManager moneyManager;
    
    protected AtStakeSprite wager = new AtStakeSprite(X,Y,0);
    protected List<Chip> chips = new ArrayList<>();
    protected Random ran = new Random();
    protected int w;
    protected int h;
    
    protected final static String[] UP_FILES =
        {"chip-100-1.png","chip-25-1.png","chip-5-1.png"};
    
    protected double sidebetAmt = -1;
    protected boolean gameOver = false;

    /**
     * Constructor
     */
    public SideBetView() {
        LOG.info("side bet view constructed");
    }

    /**
     * Sets the money manager.
     * @param moneyManager
     */
    @Override
    public void setMoneyManager(AMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
        this.buttons = moneyManager.getButtons();
    }

    /**
     * Registers a click for the side bet.
     * This method gets invoked on right mouse click.
     * @param x X coordinate
     * @param y Y coordinate
     */
    @Override
    public void click(int x, int y) {
        int oldAmt = amt;

        // Test if any chip button has been pressed.
        for(ChipButton button: buttons) {
            if(button.isReady() && button.isPressed(x, y)) {
                amt += button.getAmt();
                upBet(button.getAmt(), false);
                LOG.info("A. side bet amount "+button.getAmt()+" updated new amt = "+amt);
            }
        }

        //if(this.wager.isPressed(x, y)) {
        if (x >=(X-DIAMETER) && x <= X+DIAMETER && y >=(Y-DIAMETER) && y <= Y+DIAMETER) {
            amt = 0;
            clearBet();
            LOG.info("B. side bet amount cleared");
        }
    }
    
    /**
     * Clears the bet amount
     */
    public void clearBet() {
        this.wager.zero();
        
        chips.clear();
        
        SoundFactory.play(Effect.CHIPS_OUT);
    }

    
    
    /**
     * Informs view the game is over and it's time to update the bankroll for the hand.
     * @param hid Hand id
     */
    @Override
    public void ending(Hid hid) {
        double bet = hid.getSideAmt();
        
        this.sidebetAmt = bet;

        if(bet == 0)
            return;

        LOG.info("side bet outcome = "+bet);

        // Update the bankroll
        moneyManager.increase(bet);
        
        gameOver = true;

        LOG.info("new bankroll = "+moneyManager.getBankroll());
    }

    /**
     * Informs view the game is starting.
     */
    @Override
    public void starting() {
        gameOver = false;
    }

    /**
     * Gets the side bet amount.
     * @return Bet amount
     */
    @Override
    public Integer getAmt() {
        return amt;
    }

    /**
     * Updates the view.
     */
    @Override
    public void update() {
    }

    /**
     *
     * @param amt
     * @param autorelease
     */
    public void upBet(Integer amt, boolean autorelease)
    {
        ImageIcon icon = new ImageIcon(Constant.DIR_IMGS+UP_FILES[0]);
        Image img = icon.getImage();
        int width = img.getWidth(null);
        for (ChipButton button : buttons) {
            if (button.getAmt() != amt) {
                continue;
            }

            button.pressed();

            int n = chips.size();
            int placeX = PLACE_HOME_X + n * width / 3 + ran.nextInt(10) - 10;

            int placeY = PLACE_HOME_Y + ran.nextInt(5) - 5;

            Chip chip = new Chip(button.getImage(), placeX, placeY, amt);

            chips.add(chip);
            SoundFactory.play(Effect.CHIPS_IN);

            // Releases the chip button, if needed
            if (autorelease) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(250);
                            unclick();
                        } catch (InterruptedException ex) {

                        }
                    }
                }).start();
            }
            return;
        }
    }
    
    /**
     * Handles unclicking mouse.
     */
    public void unclick() {
        for(int i=0; i < buttons.size(); i++) {
            ChipButton button = buttons.get(i);
            button.release();
        }        
    }
    
    /**
     * Renders the view.
     * @param g Graphics context
     */
    @Override
    public void render(Graphics2D g) {
        // Draw the at-stake place on the table
        g.setColor(Color.RED);
        g.setStroke(dashed);
        g.drawOval(X-DIAMETER/2, Y-DIAMETER/2, DIAMETER, DIAMETER);

        // Draw the at-stake amount
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(""+amt, X-6, Y+5);
        
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.setColor(Color.YELLOW);
        g.drawString("SUPER 7 pays 3:1", X-10, Y-140);
        g.drawString("ROYAL MATCH pays 25:1", X-10, Y-120);
        g.drawString("EXACTLY 13 pays 1:1", X-10, Y-100);
        
        
        for(int i=0; i < buttons.size(); i++) {
            ChipButton button = buttons.get(i);
            button.render(g);
        }
        
        for(int i=0; i < chips.size(); i++) {
            Chip chip = chips.get(i);
            chip.render(g);
        }
        
        g.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g.getFontMetrics(new Font("Arial", Font.BOLD, 18));
        
        //Might have a bug where the outcome isn't displayed if there is a bust
        //Also the outcome doesn't stay on the screen like the other ones
        if(gameOver) {
            if(this.sidebetAmt > 0) {
                g.setColor(new Color(116,255,4));
                this.w = fm.charsWidth(" WIN! ".toCharArray(), 0, 6);
                this.h = fm.getHeight();
                g.fillRoundRect(X-10, Y-h+5+40, w, h, 5, 5);
                g.setColor(Color.BLACK);
                g.drawString(" WIN!", X-10, Y+40);
            }
            else if(this.sidebetAmt < -1) {
                g.setColor(new Color(250,58,5));
                this.w = fm.charsWidth(" LOSE! ".toCharArray(), 0, 7);
                this.h = fm.getHeight();
                g.fillRoundRect(X-10, Y-h+5+40, w, h, 5, 5);
                g.setColor(Color.WHITE);
                g.drawString(" LOSE! ", X-10, Y+40);
            }
        }        
        
    }
}
