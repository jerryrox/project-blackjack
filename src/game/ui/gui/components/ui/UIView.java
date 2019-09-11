/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.components.ui;

import game.allocation.InitWithDependency;
import game.animations.Anime;
import game.animations.EaseType;
import game.animations.Easing;
import game.ui.gui.components.UIAnimator;

/**
 * Basis of UI element group which makes up a "view".
 * @author jerrykim
 */
public abstract class UIView extends UIDisplayer {
    
    protected UIAnimator animator;
    protected Anime showAni;
    protected Anime hideAni;
    
    private boolean isShowing = false;
    private boolean isShowAniHooked = false;
    private boolean isHideAniHooked = false;
    
    private boolean isFirstInit = true;
    
    
    protected UIView()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        // Setup default animation.
        animator = GetObject().AddComponent(new UIAnimator());
        showAni = animator.CreateAnime("show-ani");
        {
            showAni.AddSection(0, 12, EaseType.QuadEaseOut, (progress) -> {
                SetAlpha(progress);
            });
        }
        hideAni = animator.CreateAnime("hide-ani");
        {
            hideAni.AddSection(0, 12, EaseType.QuadEaseOut, (progress) -> {
                SetAlpha(Easing.Linear(progress, 1, -1, 0));
            });
        }
    }
    
    /**
     * Returns whether the view is currently showing.
     * @return 
     */
    public boolean IsShowing() { return isShowing; }
    
    /**
     * Shows the view with animation, if applicable.
     */
    public void ShowView()
    {
        if(!isFirstInit && uiObject.IsActive() || IsAnimating())
            return;
        isFirstInit = false;
        
        if(!isShowAniHooked)
        {
            isShowAniHooked = true;
            showAni.AddEvent(showAni.GetDuration(), () -> OnPostShowView());
        }
        
        OnPreShowView();
        showAni.PlayAt(0);
    }
    
    /**
     * Hides the view with animation, if applicable.
     */
    public void HideView()
    {
        if(!uiObject.IsActive() || IsAnimating())
            return;
        
        if(!isHideAniHooked)
        {
            isHideAniHooked = true;
            hideAni.AddEvent(hideAni.GetDuration(), () -> OnPostHideView());
        }
        
        OnPreHideView();
        hideAni.PlayAt(0);
    }
    
    protected void OnPreShowView()
    {
        uiObject.SetActive(true);
        isShowing = true;
    }
    
    protected void OnPostShowView()
    {
    }
    
    protected void OnPreHideView()
    {
        isShowing = false;
    }
    
    protected void OnPostHideView()
    {
        uiObject.SetActive(false);
    }
    
    /**
     * Returns whether show or hide animation is playing.
     */
    protected boolean IsAnimating() { return showAni.IsPlaying() || hideAni.IsPlaying(); }
}
