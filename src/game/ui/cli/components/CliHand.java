/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.components;

import game.rulesets.Card;
import game.rulesets.GamePlayer;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;
import java.util.ArrayList;

/**
 * Displayer which shows a particular player's cards in their hands.
 * @author jerrykim
 */
public class CliHand extends CliDisplayer {
    
    /**
     * Current player whose hands are being shown.
     */
    private GamePlayer player;
    
    /**
     * Whether the player being displayed is a human.
     */
    private boolean isHuman;
    
    /**
     * List of drawable card instances.
     */
    private ArrayList<CliCard> cards = new ArrayList<CliCard>();
    
    
    /**
     * Sets the player instance to display the cards for.
     * @param player 
     */
    public void SetPlayer(GamePlayer player, boolean isHuman)
    {
        this.player = player;
        this.isHuman = isHuman;
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        RefreshCards();
        
        super.Render(buffer);
    }
    
    private void RefreshCards()
    {
        // Disable all card displayers.
        for(int i=0; i<cards.size(); i++)
            cards.get(i).SetActive(false);
        
        boolean isFirst = true;
        for(Card card : player.GetHand().GetAllCards())
        {
            CliCard displayer = GetNextDisplayer();
            displayer.SetActive(true);
            displayer.SetValue(card.GetNumber());
            displayer.SetHidden(false);
            
            // Special treatment for first cards.
            if(isFirst)
            {
                isFirst = false;
                if(!isHuman)
                {
                    if(!player.IsRevealing())
                        displayer.SetHidden(true);
                }
            }
        }
    }
    
    /**
     * Returns the next available card displayer.
     */
    private CliCard GetNextDisplayer()
    {
        // Find and return any available displayer.
        for(int i=0; i<cards.size(); i++)
        {
            if(!cards.get(i).IsActive())
                return cards.get(i);
        }
        
        // If no available card, create new.
        int newInx = cards.size();
        CliCard card = new CliCard();
        
        // Setup displayer.
        card.SetPosition((int)position.X + newInx * 4, (int)position.Y);
        
        // Add as child displayer.
        cards.add(card);
        AddChild(card);
        return card;
    }
}
