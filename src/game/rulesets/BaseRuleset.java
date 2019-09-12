/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.allocation.IDependencyContainer;
import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.debug.Debug;
import game.entities.UserEntity;
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
     * Game process manager instance.
     */
    protected GameProcessor gameProcessor;
    
    @ReceivesDependency
    private IDependencyContainer dependencies;
    
    
    @InitWithDependency
    private void Init(UserEntity user)
    {
        gameProcessor = new GameProcessor(user);
    }
    
    public @Override void OnStartSession()
    {
        // Temporarily include the game processor as dependency within this session.
        dependencies.Cache(gameProcessor);
        
        gameProcessor.OnStartSession();
        drawableRuleset.OnStartSession();
    }
    
    public @Override void OnStopSession()
    {
        gameProcessor.OnStopSession();
        drawableRuleset.OnStopSession();
        
        // Remove game processor dependency.
        dependencies.Remove(GameProcessor.class);
    }
    
    /**
     * Returns the current game processor instance.
     */
    public GameProcessor GetGameProcessor() { return gameProcessor; }
    
    /**
     * Returns the visual representation of this ruleset.
     */
    public IDrawableRuleset GetDrawableRuleset()
    {
        if(drawableRuleset == null)
        {
            drawableRuleset = CreateDrawableRuleset();
            if(drawableRuleset == null)
            {
                Debug.LogError("Could not create drawable ruleset!");
                return null;
            }
        }
        drawableRuleset.SetRuleset(this);
        return drawableRuleset;
    }
    
    /**
     * Returns the game result object.
     */
    public GameResult GetResult()
    {
        GamePlayer humanPlayer = gameProcessor.GetHumanPlayer();
        GameAIPlayer aiPlayer = gameProcessor.GetAIPlayer();
        
        int rewards = aiPlayer.GetUser().Gold.GetValue();
        GameResultTypes resultType = GameResultTypes.Draw;
        
        if(!humanPlayer.IsDead())
            resultType = GameResultTypes.Win;
        else if(!aiPlayer.IsDead())
        {
            resultType = GameResultTypes.Lose;
            rewards = (int)(rewards * 0.25);
        }
        
        
        return new GameResult(GetGameMode(), resultType, rewards, aiPlayer.GetDifficulty());
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
