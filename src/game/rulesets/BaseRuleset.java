/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.allocation.IDependencyContainer;
import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.debug.Debug;
import game.entities.User;
import game.rulesets.ui.IDrawableRuleset;

/**
 * The base implementation for a game ruleset.
 * @author jerrykim
 */
public abstract class BaseRuleset implements IGameSession {
    
    /**
     * Cached ruleset drawable instance.
     */
    protected IDrawableRuleset drawableRuleset;
    
    /**
     * Current phase number during this battle (session).
     */
    protected int curPhase;
    
    /**
     * Players participating in the game.
     * Should only be two players anyway.
     */
    protected GamePlayer[] players;
    
    /**
     * Current player on turn.
     */
    protected GamePlayer playerOnTurn;
    
    /**
     * Current deck instance in use.
     */
    protected Deck deck;
    
    /**
     * Human user instance.
     */
    protected User user;
    
    
    @InitWithDependency
    private void Init(User user)
    {
        this.user = user;
        
        players = new GamePlayer[] {
            new GamePlayer(user),
            new GameAIPlayer()
        };
    }
    
    public @Override void OnStartSession()
    {
        deck = new Deck(1);
        deck.Shuffle();
        
        curPhase = 1;
        for(int i=0; i<players.length; i++)
            players[i].ResetState();
        playerOnTurn = players[0];
        
        drawableRuleset.OnStartSession();
    }
    
    public @Override void OnStopSession()
    {
        drawableRuleset.OnStopSession();
    }
    
    /**
     * Returns the deck in use.
     */
    public Deck GetDeck() { return deck; }
    
    /**
     * Returns the player on turn.
     */
    public GamePlayer GetPlayerOnTurn() { return playerOnTurn; }
    
    /**
     * Returns the player not on turn.
     */
    public GamePlayer GetPlayerNotOnTurn() { return IsHumanPlayerTurn() ? players[1] : players[0]; }
    
    /**
     * Returns the players array.
     */
    public GamePlayer[] GetPlayers() { return players; }
    
    /**
     * Changes turn to other player.
     */
    public void SwitchTurn()
    {
        if(IsHumanPlayerTurn())
            playerOnTurn = players[1];
        else
        {
            playerOnTurn = players[0];
            curPhase ++;
        }
    }
    
    /**
     * Returns whether it's currently the human player's turn.
     */
    public boolean IsHumanPlayerTurn() { return playerOnTurn == players[0]; }
    
    /**
     * Returns current phase number.
     */
    public int GetPhase() { return curPhase; }
    
    /**
     * Returns the visual representation of this ruleset.
     */
    public IDrawableRuleset GetDrawableRuleset()
    {
        if(drawableRuleset == null)
        {
            drawableRuleset = CreateDrawableRuleset();
            if(drawableRuleset == null)
                Debug.LogError("Could not create drawable ruleset!");
            else
                drawableRuleset.SetRuleset(this);
        }
        return drawableRuleset;
    }
    
    /**
     * Returns the game mode this ruleset works for.
     */
    public abstract GameModes GetGameMode();
    
    /**
     * Instantiates a new drawable ruleset instance.
     */
    protected abstract IDrawableRuleset CreateDrawableRuleset();
}
