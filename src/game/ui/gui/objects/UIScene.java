/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects;

import game.allocation.InitWithDependency;
import game.animations.AnimationWrap;
import game.animations.Anime;
import game.animations.EaseType;
import game.animations.Easing;
import game.debug.Debug;
import game.ui.gui.UIRootPanel;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.UIAnimator;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.screens.UISplashScreen;
import java.awt.Color;

/**
 * A specialization of UIObject for serving as the root object in the 
 * gameobject hierarchy.
 * The actual entry point of gameobject creation must originte from this object.
 * @author jerrykim
 */
public class UIScene extends UIObject {
    
    public UIScene()
    {
        super("Scene");
    }
    
    @InitWithDependency
    private void Init(UIScreenController screens)
    {
        // Adjust (0,0) to center of screen.
        GetTransform().SetLocalPosition(UIRootPanel.Width / 2, UIRootPanel.Height / 2);
        
        // Show splash screen.
        screens.ShowView(UISplashScreen.class);
        
        // Some test codes.
        /*UISprite sprite = CreateChild().AddComponent(new UISprite());
        sprite.SetSpritename("round-box");
        sprite.SetSize(250, 500);
        sprite.SetColor(Color.blue);
        sprite.SetWrapMode(UISprite.WrapModes.Sliced);
        sprite.SetAlpha(0.5f);
        
        UISprite sprite2 = CreateChild().AddComponent(new UISprite());
        sprite2.SetSpritename("icon-arrow-left");
        sprite2.ResetSize();
        sprite2.SetColor(Color.yellow);
        sprite2.SetAlpha(0.75f);
        sprite2.GetTransform().SetLocalPosition(100, 100);
        sprite2.SetSize(64, 64);
        
        UILabel label = CreateChild().AddComponent(new UILabel());
        label.SetText("My text");
        label.SetColor(Color.red);
        label.SetAlpha(0.5f);
        label.GetObject().SetDepth(10);
        label.GetTransform().SetLocalPosition(100, 100);
        
        UIAnimator animator = AddComponent(new UIAnimator());
        Anime labelAni = animator.CreateAnime("label");
        {
            labelAni.AddSection(0, 30, (progress) -> {
                label.GetTransform().SetLocalPosition(0, Easing.QuadEaseOut(progress, 0, 50, 0));
            });
            labelAni.AddSection(30, 60, (progress) -> {
                label.GetTransform().SetLocalPosition(0, Easing.QuadEaseIn(progress, 50, -50, 0));
            });
            labelAni.PlayAt(0);
            labelAni.SetWrapMode(AnimationWrap.Loop);
        }
        
        Anime sprite2Ani = animator.CreateAnime("sprite2");
        {
            sprite2Ani.AddSection(0, 20, (progress) -> {
                sprite2.SetColor(new Color(
                    Easing.QuadEaseOut(progress, 0.5f, 0.5f, 0),
                    0.5f,
                    0.5f
                ));
            });
            sprite2Ani.AddSection(20, 40, (progress) -> {
                sprite2.SetColor(new Color(
                    Easing.QuadEaseOut(progress, 1f, -0.5f, 0),
                    Easing.QuadEaseOut(progress, 0.5f, 0.5f, 0),
                    0.5f
                ));
            });
            sprite2Ani.AddSection(40, 60, (progress) -> {
                sprite2.SetColor(new Color(
                    0.5f,
                    Easing.QuadEaseOut(progress, 1f, -0.5f, 0),
                    Easing.QuadEaseOut(progress, 0.5f, 0.5f, 0)
                ));
            });
            sprite2Ani.AddSection(60, 80, (progress) -> {
                sprite2.SetColor(new Color(
                    0.5f,
                    0.5f,
                    Easing.QuadEaseOut(progress, 1f, -0.5f, 0)
                ));
            });
            sprite2Ani.PlayAt(0);
            sprite2Ani.SetWrapMode(AnimationWrap.Loop);
        }*/
    }
}
