/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.overlays;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.data.ActionT;
import game.entities.UserEntity;
import game.graphics.ColorPreset;
import game.ui.Pivot;
import game.ui.gui.components.UITransform;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIOverlay;
import game.ui.gui.components.ui.UISprite;
import java.awt.Font;

/**
 *
 * @author jerrykim
 */
public class UITopMenuOverlay extends UIOverlay {
    
    private UILabel nameLabel;
    private UILabel goldLabel;
    private UITransform goldIconTransform;
    
    private ActionT<Integer> onGoldChanged;
    
    @ReceivesDependency
    private UserEntity user;
    
    
    public UITopMenuOverlay()
    {
        super();
        
        onGoldChanged = (gold) -> {
            goldLabel.SetText(String.format("%,d", gold));
            goldIconTransform.SetLocalPosition(
                goldLabel.GetTransform().GetLocalPosition().X - goldLabel.GetWidth() - 34,
                2
            );
        };
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        UISprite bg = uiObject.CreateChild().AddComponent(new UISprite());
        {
            bg.SetSpritename("round-box");
            bg.SetWrapMode(UISprite.WrapModes.Sliced);
            bg.SetSize(1200, 42);
            bg.SetColor(colors.Darker);
            bg.SetAlpha(0.75f);
        }
        nameLabel = uiObject.CreateChild().AddComponent(new UILabel());
        {
            nameLabel.SetFontStyle(Font.BOLD | Font.ITALIC);
            nameLabel.SetPivot(Pivot.Left);
            nameLabel.GetTransform().SetLocalPosition(-580, 0);
        }
        goldLabel = uiObject.CreateChild().AddComponent(new UILabel());
        {
            goldLabel.SetColor(colors.Gold);
            goldLabel.SetPivot(Pivot.Right);
            goldLabel.GetTransform().SetLocalPosition(580, 0);
        }
        UISprite goldIcon = uiObject.CreateChild().AddComponent(new UISprite());
        {
            goldIconTransform = goldIcon.GetTransform();
            
            goldIcon.SetSpritename("icon-coin");
            goldIconTransform.SetLocalScale(0.3f, 0.3f);
        }
        
        GetTransform().SetLocalPosition(0, -330);
    }
    
    public @Override void Update(float deltaTime)
    {
        super.Update(deltaTime);
    }
    
    protected @Override void OnPreShowView()
    {
        super.OnPreShowView();
        
        nameLabel.SetText(user.Username.GetValue());
        
        user.Gold.Changed.Add(onGoldChanged);
        onGoldChanged.Invoke(user.Gold.GetValue());
    }
    
    protected @Override void OnPostHideView()
    {
        super.OnPostHideView();
        
        user.Gold.Changed.Remove(onGoldChanged);
    }
    
    
}
