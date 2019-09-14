/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.data.Events;
import game.rulesets.Card;
import game.rulesets.GameItem;
import game.rulesets.GamePlayer;
import game.rulesets.PhaseResults;
import game.rulesets.ui.gui.IGameStateListener;
import game.ui.Pivot;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.objects.UIObject;

/**
 * Displays items currently in use.
 * @author jerrykim
 */
public class UIGameItemList extends UIObject implements IGameStateListener {
    
    private UILabel itemLabel;
    
    private GamePlayer player;
    
    
    public UIGameItemList()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        itemLabel = CreateChild().AddComponent(new UILabel());
        {
            itemLabel.SetPivot(Pivot.Left);
            itemLabel.SetFontSize(15);
            itemLabel.SetAlpha(0.5f);
            itemLabel.SetText("Items:");
        }
    }
    
    /**
     * Sets player instance to display items list for.
     */
    public void SetPlayer(GamePlayer player)
    {
        this.player = player;
        
        Refresh();
    }
    
    public @Override Events OnSetTurnState(GamePlayer player)
    {
        if(player != this.player)
            return null;
        Refresh();
        return null;
    }

    public @Override Events OnTurnEndState(GamePlayer player) { return null; }

    public @Override Events OnNewPhaseState() { return null; }

    public @Override Events OnPhaseEndState() { return null; }

    public @Override Events OnEvaluatedState(PhaseResults result, int humanDmg, int aiDmg) { return null; }

    public @Override Events OnDrawState(GamePlayer player, Card card) { return null; }

    public @Override Events OnSkipState(GamePlayer player) { return null; }
    
    public @Override Events OnItemUseState(GamePlayer player, GameItem item)
    {
        if(player != this.player || item.GetDuration() == 0)
            return null;
        Refresh();
        return null;
    }
    
    private void Refresh()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Items: ");
        
        boolean isFirst = true;
        for(GameItem item : player.GetUsingItems())
        {
            if(!isFirst)
                sb.append(", ");
            sb.append(item.Info.Name);
            isFirst = false;
        }
        itemLabel.SetText(sb.toString());
    }
}
