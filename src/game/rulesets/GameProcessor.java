/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.entities.UserEntity;

/**
 * Game process management object.
 * @author jerrykim
 */
public class GameProcessor implements IGameSession {
    
    /**
     * Current phase number during this battle (session).
     */
    private int curPhase;
    
    /**
     * Current deck instance in use.
     */
    private Deck deck;
    
    /**
     * Human user instance.
     */
    private UserEntity user;
    
    /**
     * Representation of human player as game player.
     */
    private GamePlayer humanPlayer;
    
    /**
     * Representation of ai player as game player.
     */
    private GameAIPlayer aiPlayer;
    
    /**
     * Whether it's currently the human player's turn.
     */
    private boolean isHumanTurn;
    
    /**
     * Whether the battle had a conclusion.
     */
    private boolean isFinished;
    
    
    public GameProcessor(UserEntity user)
    {
        this.user = user;
        
        humanPlayer = new GamePlayer(user);
        aiPlayer = new GameAIPlayer();
    }
    
    public @Override void OnStartSession()
    {
        isFinished = false;
        curPhase = 0;
        deck = new Deck(1);
        
        humanPlayer.ResetState();
        aiPlayer.ResetState();
    }

    public @Override void OnStopSession()
    {
        humanPlayer.GetHand().ClearCards();
        aiPlayer.GetHand().ClearCards();
    }
    
    /**
     * Trigger phase end event.
     */
    public void SetPhaseEnd()
    {
        if(humanPlayer.IsDead() || aiPlayer.IsDead())
            isFinished = true;
    }
    
    /**
     * Prepares new phase of the battle.
     */
    public void NewPhase()
    {
        curPhase ++;
        
        // Notify phase change.
        humanPlayer.OnNextPhase();
        aiPlayer.OnNextPhase();
        
        // Return all cards to deck.
        humanPlayer.GetHand().ReturnAllCards(deck);
        aiPlayer.GetHand().ReturnAllCards(deck);
        
        // Shuffle deck and put random initial cards on player.
        deck.Shuffle();
        humanPlayer.GetHand().DrawCard(deck);
        aiPlayer.GetHand().DrawCard(deck);
    }
    
    /**
     * Sets whether human player is on turn.
     */
    public void SetHumanTurn(boolean isHumanTurn) { this.isHumanTurn = isHumanTurn; }
    
    /**
     * Changes turn to other player.
     */
    public void SwitchTurn() { isHumanTurn = !isHumanTurn; }
    
    /**
     * Evaluates the phase result and returns it.
     */
    public PhaseResults GetPhaseResult()
    {
        int humanValue = humanPlayer.GetHand().GetTotalCardValue();
        int aiValue = aiPlayer.GetHand().GetTotalCardValue();
        
        // Draw conditions
        if(humanValue == aiValue || (humanValue > 21 && aiValue > 21))
            return PhaseResults.Draw;
        else if(humanValue <= 21 && (aiValue > 21 || humanValue > aiValue))
            return PhaseResults.HumanWin;
        return PhaseResults.AiWin;
    }
    
    /**
     * Whether current phase should be evaluated for result.
     */
    public boolean ShouldEvalPhase()
    {
        return !humanPlayer.DidDraw() && !aiPlayer.DidDraw();
    }
    
    /**
     * Returns current phase number.
     */
    public int GetPhase() { return curPhase; }
    
    /**
     * Returns the deck in use.
     */
    public Deck GetDeck() { return deck; }
    
    /**
     * Returns whether it's currently the human player's turn.
     */
    public boolean IsHumanTurn() { return isHumanTurn; }
    
    /**
     * Returns the player on turn.
     */
    public GamePlayer GetPlayerOnTurn() { return isHumanTurn ? humanPlayer : aiPlayer; }
    
    /**
     * Returns the player not on turn.
     */
    public GamePlayer GetPlayerNotOnTurn() { return isHumanTurn ? aiPlayer : humanPlayer; }
    
    /**
     * Returns the human player instance.
     */
    public GamePlayer GetHumanPlayer() { return humanPlayer; }
    
    /**
     * Returns the AI play instance.
     */
    public GameAIPlayer GetAIPlayer() { return aiPlayer; }
    
    /**
     * Returns whether the game has finished.
     */
    public boolean IsFinished() { return isFinished; }
}
