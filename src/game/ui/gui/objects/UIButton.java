/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects;

import game.allocation.InitWithDependency;
import game.animations.Anime;
import game.ui.gui.components.UIAnimator;
import game.ui.gui.components.ui.UIWidget;

/**
 * Specialized type of cursor interactive object as a button.
 * @author jerrykim
 */
public class UIButton extends UICursorInteractive {
    
    protected UIAnimator animator;
    protected Anime cursorOverAni;
    protected Anime cursorOutAni;
    protected Anime cursorPressAni;
    protected Anime cursorReleaseAni;
    
    
    public UIButton()
    {
    }
    
    @InitWithDependency
    private void Init()
    {
        animator = AddComponent(new UIAnimator());
        cursorOverAni = animator.CreateAnime("cursorover");
        cursorOutAni = animator.CreateAnime("cursorout");
        cursorPressAni = animator.CreateAnime("cursorpress");
        cursorReleaseAni = animator.CreateAnime("cursorrelease");
    }
    
    public @Override void OnCursorOver()
    {
        super.OnCursorOver();
        cursorOutAni.Stop();
        cursorOverAni.PlayAt(0);
    }
    
    public @Override void OnCursorOut()
    {
        super.OnCursorOut();
        cursorOverAni.Stop();
        cursorOutAni.PlayAt(0);
    }
    
    public @Override void OnCursorPress()
    {
        super.OnCursorPress();
        cursorReleaseAni.Stop();
        cursorPressAni.PlayAt(0);
    }
    
    public @Override void OnCursorRelease()
    {
        super.OnCursorRelease();
        cursorPressAni.Stop();
        cursorReleaseAni.PlayAt(0);
    }
}
