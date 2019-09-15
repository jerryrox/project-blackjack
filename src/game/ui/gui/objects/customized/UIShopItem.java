/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.animations.Easing;
import game.data.Action;
import game.graphics.ColorPreset;
import game.rulesets.items.ItemInfo;
import game.ui.Pivot;
import game.ui.gui.components.ui.UIDisplayer;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIButton;
import java.awt.Font;

/**
 * Representation of a single item type in shop.
 * @author jerrykim
 */
public class UIShopItem extends UIButton implements IUIItemCell {
    
    private UIDisplayer panel;
    private UISprite bg;
    private UILabel nameLabel;
    private UILabel costLabel;
    private UILabel descriptionLabel;
    
    private ItemInfo item;
    
    private Action callback;
    
    
    public UIShopItem()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        panel = AddComponent(new UIDisplayer());
        
        bg = CreateChild().AddComponent(new UISprite());
        {
            bg.GetObject().SetDepth(-1);
            bg.SetSpritename("round-box");
            bg.SetWrapMode(UISprite.WrapModes.Sliced);
            bg.SetSize(960, 56);
            bg.SetColor(colors.Negative);
            bg.SetAlpha(0.25f);
            
            SetTarget(bg);
        }
        nameLabel = CreateChild().AddComponent(new UILabel());
        {
            nameLabel.SetFont(Font.BOLD | Font.ITALIC, 21);
            nameLabel.SetPivot(Pivot.Left);
            nameLabel.GetTransform().SetLocalPosition(-460, -9);
        }
        costLabel = CreateChild().AddComponent(new UILabel());
        {
            costLabel.SetPivot(Pivot.Right);
            costLabel.SetAlpha(0.5f);
            costLabel.SetFontSize(16);
            costLabel.GetTransform().SetLocalPosition(460, -9);
        }
        descriptionLabel = CreateChild().AddComponent(new UILabel());
        {
            descriptionLabel.SetPivot(Pivot.Left);
            descriptionLabel.SetAlpha(0.5f);
            descriptionLabel.SetFontSize(15);
            descriptionLabel.GetTransform().SetLocalPosition(-450, 12);
        }
        
        cursorOverAni.AddSection(0, 9, (progress) -> {
            bg.SetAlpha(Easing.QuadEaseOut(progress, 0.25f, 0.25f, 0));
        });
        cursorOutAni.AddSection(0, 9, (progress) -> {
            bg.SetAlpha(Easing.QuadEaseIn(progress, 0.5f, -0.25f, 0));
        });
        cursorPressAni.AddSection(0, 9, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1, 0.05f, 0);
            GetTransform().SetLocalScale(scale, scale);
        });
        cursorReleaseAni.AddSection(0, 9, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1.05f, -0.05f, 0);
            GetTransform().SetLocalScale(scale, scale);
        });
        
        Clicked.Add((arg) -> {
            if(callback != null)
                callback.Invoke();
        });
    }
    
    public @Override int GetHeight() { return bg.GetHeight(); }

    public @Override UIDisplayer GetPanel() { return panel; }

    public @Override ItemInfo GetItem() { return item; }

    public @Override void Setup(ItemInfo item, int value)
    {
        this.item = item;
        nameLabel.SetText(item.Name);
        costLabel.SetText("Cost: " + value);
        descriptionLabel.SetText(item.Description);
    }

    public @Override void SetCallback(Action callback)
    {
        this.callback = callback;
    }
}
