/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.animations.AnimationWrap;
import game.animations.Anime;
import game.animations.Easing;
import game.data.Action;
import game.ui.gui.components.ui.UIDisplayer;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIButton;
import game.ui.gui.objects.UIObject;
import java.awt.Color;

/**
 * Mode selection button.
 * @author jerrykim
 */
public class UIModeButton extends UIButton {
    
    private UIDisplayer myDisplayer;
    
    private UISprite iconSprite;
    private UISprite titleSprite;
    private UISprite titleGlow;
    
    private Anime selectedAni;
    private Anime unselectedAni;
    private Anime repeatAni;
    
    private Action onSelectAni;
    
    
    public UIModeButton()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        myDisplayer = AddComponent(new UIDisplayer());
        
        UISprite glow = CreateChild().AddComponent(new UISprite());
        {
            glow.GetObject().SetDepth(-1);
            glow.SetSpritename("glow");
            glow.SetSize(340, 480);
            glow.SetAlpha(0f);
        }
        UICardDisplayer card = AddChild(new UICardDisplayer());
        {
            card.GetTransform().SetLocalScale(0.9f, 0.9f);
            
            iconSprite = card.CreateChild().AddComponent(new UISprite());
        }
        
        titleSprite = CreateChild().AddComponent(new UISprite());
        {
            titleSprite.GetTransform().SetLocalPosition(0, 230);
            
            // Make the title sprite the button area.
            SetTarget(titleSprite);
            
            UIObject titleObj = titleSprite.GetObject();
            titleGlow = titleObj.CreateChild().AddComponent(new UISprite());
            {
                titleGlow.SetSpritename("mode-button-glow");
                titleGlow.ResetSize();
                titleGlow.SetColor(Color.black);
            }
        }
        
        cursorOverAni.AddSection(0, 12, (progress) -> {
            float col = Easing.QuadEaseOut(progress, 0, 1, 0);
            titleGlow.SetColor(col, col, col);
        });
        cursorOutAni.AddSection(0, 12, (progress) -> {
            float col = Easing.QuadEaseOut(progress, 1, -1, 0);
            titleGlow.SetColor(col, col, col);
        });
        cursorPressAni.AddSection(0, 9, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1, 0.1f, 0);
            titleSprite.GetTransform().SetLocalScale(scale, scale);
        });
        cursorReleaseAni.AddSection(0, 9, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1.1f, -0.1f, 0);
            titleSprite.GetTransform().SetLocalScale(scale, scale);
        });
        selectedAni = animator.CreateAnime("selected");
        {
            selectedAni.AddSection(0, 30, (progress) -> {
                float scale = Easing.QuadEaseOut(progress, 1, 0.08f, 0f);
                GetTransform().SetLocalScale(scale, scale);
            });
            selectedAni.AddEvent(60, () -> {
                if(onSelectAni != null)
                    onSelectAni.Invoke();
                onSelectAni = null;
            });
        }
        unselectedAni = animator.CreateAnime("unselected");
        {
            unselectedAni.AddSection(0, 30, (progress) -> {
                float scale = Easing.QuadEaseOut(progress, 1, -0.15f, 0);
                GetTransform().SetLocalScale(scale, scale);
                myDisplayer.SetAlpha(Easing.QuadEaseOut(progress, 1, -0.5f, 0));
            });
        }
        repeatAni = animator.CreateAnime("repeat");
        {
            repeatAni.AddSection(0, 45, (progress) -> {
                glow.SetAlpha(Easing.QuadEaseOut(progress, 0, 1, 0));
            });
            repeatAni.AddSection(45, 90, (progress) -> {
                glow.SetAlpha(Easing.QuadEaseIn(progress, 1, -1, 0));
            });
            repeatAni.SetWrapMode(AnimationWrap.Loop);
            repeatAni.Play();
        }
    }
    
    /**
     * Resets states modified from animations.
     */
    public void ResetState()
    {
        GetTransform().SetLocalScale(1, 1);
        myDisplayer.SetAlpha(1f);
    }
    
    /**
     * Setup display for desired mode.
     * If not survival, it's casual.
     */
    public void SetupMode(boolean isSurvival)
    {
        if(isSurvival)
        {
            iconSprite.SetSpritename("survival-icon");
            titleSprite.SetSpritename("survival-title");
        }
        else
        {
            iconSprite.SetSpritename("casual-icon");
            titleSprite.SetSpritename("casual-title");
        }
        iconSprite.ResetSize();
        titleSprite.ResetSize();
    }
    
    /**
     * Plays selection animation.
     */
    public void SelectMode(boolean select, Action selectCallback)
    {
        if(select)
        {
            selectedAni.PlayAt(0);
            onSelectAni = selectCallback;
        }
        else
            unselectedAni.PlayAt(0);
    }
}
