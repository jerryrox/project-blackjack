/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.components.ui;

import game.allocation.InitWithDependency;
import game.animations.Easing;

/**
 * Type of view which represents a main view in the game.
 * @author jerrykim
 */
public class UIScreen extends UIView {
    
    public UIScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        // Add scaling effects on screen transition animations.
        showAni.AddSection(0, 12, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1.1f, -0.1f, 0);
            GetTransform().SetLocalScale(scale, scale);
        });
        hideAni.AddSection(0, 12, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1f, 0.1f, 0);
            GetTransform().SetLocalScale(scale, scale);
        });
    }
}
