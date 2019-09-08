/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.allocation.InitWithDependency;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.components.ui.UISprite;

/**
 * Home screen.
 * @author jerrykim
 */
public class UIHomeScreen extends UIScreen {
    
    public UIHomeScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        UISprite sprite = uiObject.CreateChild().AddComponent(new UISprite());
        sprite.SetSpritename("box");
        sprite.SetSize(1280, 720);
    }
}
